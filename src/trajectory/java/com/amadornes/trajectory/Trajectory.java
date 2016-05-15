package com.amadornes.trajectory;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.block.BlockMoving;
import com.amadornes.trajectory.block.TileMoving;
import com.amadornes.trajectory.config.BlockConfig;
import com.amadornes.trajectory.config.BlockMatcher;
import com.amadornes.trajectory.config.BlockMovementHandlerConfig;
import com.amadornes.trajectory.config.ConfigHandler;
import com.amadornes.trajectory.hax.GuiHax;
import com.amadornes.trajectory.movement.BlockDescriptionProviderDefault;
import com.amadornes.trajectory.movement.BlockDescriptionProviderSign;
import com.amadornes.trajectory.movement.DynamicWorldReferenceMoving;
import com.amadornes.trajectory.movement.MovementManager;
import com.amadornes.trajectory.movement.MovementScheduler;
import com.amadornes.trajectory.movement.MovingStructure;
import com.amadornes.trajectory.movement.TrajectoryRotation;
import com.amadornes.trajectory.movement.TrajectoryTranslation;
import com.amadornes.trajectory.network.NetworkHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class Trajectory {

    @Instance(ModInfo.MODID)
    public static Trajectory instance;
    @SidedProxy(serverSide = "com.amadornes.trajectory.CommonProxy", clientSide = "com.amadornes.trajectory.client.ClientProxy")
    public static CommonProxy proxy;
    public static Logger log;

    public static Block moving;

    public File configFolder;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        log = event.getModLog();

        // Initialize the API
        TrajectoryAPI.setup(MovementManager.instance);

        // Register default trajectories
        TrajectoryAPI.instance().registerTrajectoryType(TrajectoryTranslation.class, "translation");
        TrajectoryAPI.instance().registerTrajectoryType(TrajectoryRotation.class, "rotation");

        // Register default dynamic world reference
        TrajectoryAPI.instance().registerDynamicWorldReference(new DynamicWorldReferenceMoving());

        // Initialize and register the moving block
        moving = new BlockMoving();
        GameRegistry.registerBlock(moving, "moving");
        GameRegistry.registerTileEntity(TileMoving.class, ModInfo.MODID + ":moving");

        // Register the movement scheduler so it can listen to events
        MinecraftForge.EVENT_BUS.register(MovementScheduler.instance);
        FMLCommonHandler.instance().bus().register(MovementScheduler.instance);

        // Load the config files
        try {
            ConfigHandler.load(configFolder = event.getSuggestedConfigurationFile().getParentFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Register the config movement handler
        TrajectoryAPI.instance().registerBlockMovementHandler(new BlockMovementHandlerConfig());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        // Initialize proxy
        proxy.init();

        // Initialize the network handler
        NetworkHandler.init();

        // Register default data movement providers
        TrajectoryAPI.instance().registerBlockDescriptionProvider(new BlockDescriptionProviderDefault());
        TrajectoryAPI.instance().registerBlockDescriptionProvider(new BlockDescriptionProviderSign());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        // Do some GUI hackery to allow most GUIs to be open while moving
        try {
            GuiHax.doGuiHax();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        for (MovingStructure structure : new ArrayList<MovingStructure>(MovementManager.instance.getStructures(false)))
            structure.finishMoving();
        for (MovingStructure structure : new ArrayList<MovingStructure>(MovementManager.instance.getStructures(true)))
            structure.finishMoving();
        event.registerServerCommand(new CommandTrajectory());
    }

    @EventHandler
    public void onIMCMessage(IMCEvent event) {

        for (IMCMessage m : event.getMessages()) {
            if (m.key.toLowerCase().startsWith("blacklist")) {
                BlockMatcher matcher = null;
                if (m.key.equalsIgnoreCase("blacklist_block_mod")) {
                    matcher = new BlockMatcher.BlockMatcherBlockMod(m.getStringValue());
                } else if (m.key.equalsIgnoreCase("blacklist_block")) {
                    String[] arr = m.getStringValue().split(":");
                    matcher = new BlockMatcher.BlockMatcherBlockModType(arr[0], arr[1], arr.length == 3 ? Integer.parseInt(arr[2]) : -1);
                } else if (m.key.equals("blacklist_tile_mod")) {
                    matcher = new BlockMatcher.BlockMatcherTileMod(m.getStringValue());
                } else if (m.key.equals("blacklist_tile_class")) {
                    matcher = new BlockMatcher.BlockMatcherTileClass(m.getStringValue());
                }
                if (matcher == null)
                    log.error("Received an invalid IMC message from \"" + m.getSender() + "\". \"" + m.key
                            + "\" is an invalid matching pattern!");
                boolean found = false;
                for (BlockConfig cfg : ConfigHandler.blockConfigs) {
                    if (cfg.matcher.equals(matcher)) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    ConfigHandler.blockConfigs.add(new BlockConfig(matcher, true, true, true, true, true, false, false));
            } else if (m.key.toLowerCase().startsWith("no_invalidate")) {
                BlockMatcher matcher = null;
                if (m.key.equalsIgnoreCase("no_invalidate_block_mod")) {
                    matcher = new BlockMatcher.BlockMatcherBlockMod(m.getStringValue());
                } else if (m.key.equalsIgnoreCase("no_invalidate_block")) {
                    String[] arr = m.getStringValue().split(":");
                    matcher = new BlockMatcher.BlockMatcherBlockModType(arr[0], arr[1], arr.length == 3 ? Integer.parseInt(arr[2]) : -1);
                } else if (m.key.equals("no_invalidate_tile_mod")) {
                    matcher = new BlockMatcher.BlockMatcherTileMod(m.getStringValue());
                } else if (m.key.equals("no_invalidate_tile_class")) {
                    matcher = new BlockMatcher.BlockMatcherTileClass(m.getStringValue());
                }
                if (matcher == null)
                    log.error("Received an invalid IMC message from \"" + m.getSender() + "\". \"" + m.key
                            + "\" is an invalid matching pattern!");
                boolean found = false;
                for (BlockConfig cfg : ConfigHandler.blockConfigs) {
                    if (cfg.matcher.equals(matcher)) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    ConfigHandler.blockConfigs.add(new BlockConfig(matcher, false, false, false, false, true, true, false));
            }
        }
        try {
            ConfigHandler.saveBlockConfigs(configFolder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
