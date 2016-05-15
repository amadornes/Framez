package com.amadornes.framez;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.client.gui.GuiHandler;
import com.amadornes.framez.dispenser.BehaviorDispenserDeployCake;
import com.amadornes.framez.dispenser.BehaviorDispenserPlaceFrame;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.init.FramezOredict;
import com.amadornes.framez.init.FramezRecipes;
import com.amadornes.framez.item.ItemWrench;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.modifier.frame.FrameMaterial;
import com.amadornes.framez.modifier.frame.FrameSideModifierLatching;
import com.amadornes.framez.modifier.frame.FrameSideModifierSticky;
import com.amadornes.framez.modifier.motor.MotorModifierDC;
import com.amadornes.framez.movement.MovementBlink;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.movement.MovementRotation;
import com.amadornes.framez.movement.MovementSlide;
import com.amadornes.framez.movement.handler.BlockMovementHandlerStopper;
import com.amadornes.framez.movement.handler.MovementPluginFramez;
import com.amadornes.framez.movement.handler.MultiblockMovementHandlerDoor;
import com.amadornes.framez.movement.handler.PlacementHandlerDefault;
import com.amadornes.framez.movement.handler.StickinessHandlerDispenserFace;
import com.amadornes.framez.movement.handler.StructureMovementHandlerChest;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileFrame;
import com.amadornes.framez.upgrade.MotorUpgradeBouncy;
import com.amadornes.framez.upgrade.MotorUpgradeCamouflage;
import com.amadornes.framez.upgrade.MotorUpgradeMomentumConservation;
import com.amadornes.framez.util.WorldGenerationHandler;
import com.amadornes.trajectory.api.TrajectoryAPI;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.MissingModsException;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = "required-after:Trajectory")
public class Framez {

    @Instance(ModInfo.MODID)
    public static Framez instance;

    @SidedProxy(serverSide = "com.amadornes.framez.CommonProxy", clientSide = "com.amadornes.framez.client.ClientProxy")
    public static CommonProxy proxy;

    public static Logger log;

    public Class<? extends TileFrame> frameTileClass = TileFrame.class;
    public Class<? extends ItemWrench> wrenchItemClass = ItemWrench.class;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        try {
            Class.forName("codechicken.lib.util.Copyable");
        } catch (Exception ex) {
            throw new MissingModsException(new HashSet<ArtifactVersion>(Arrays.asList(new DefaultArtifactVersion("CodeChickenLib"))));
        }

        log = event.getModLog();

        // Init the API
        FramezApi.setup(new FramezApiImpl());

        // Load the config file
        FramezConfig.load(new File(event.getModConfigurationDirectory(), "Framez.cfg"));// Just so it has a capital "F" :P

        // Call the pre-initialization method in the compat module if it's available
        if (Loader.isModLoaded("framez_compat")) {
            try {
                Method prePreInit = Class.forName("com.amadornes.framez.compat.FramezCompat").getMethod("prePreInit", File.class);
                Field inst = Class.forName("com.amadornes.framez.compat.FramezCompat").getField("instance");
                if (prePreInit != null) {
                    prePreInit.setAccessible(true);
                    prePreInit.invoke(inst.get(null), event.getModConfigurationDirectory());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // After pre-initializing Framez Compat, save the config file again
        FramezConfig.save();

        // Register trajectories
        TrajectoryAPI.instance().registerTrajectoryType(MovementSlide.class, "framez_slide");
        TrajectoryAPI.instance().registerTrajectoryType(MovementRotation.class, "framez_rot");
        TrajectoryAPI.instance().registerTrajectoryType(MovementBlink.class, "framez_blink");

        // Dispenser face stickiness
        MovementRegistry.instance.registerStickinessHandler(new StickinessHandlerDispenserFace());

        // Register default frame modifiers
        if (!FramezConfig.simple_mode) {
            if (FramezConfig.material_wood_enabled)
                ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial("stickWood", References.Modifier.MATERIAL_WOOD,
                        FramezConfig.material_wood_max_moved_blocks, FramezConfig.material_wood_max_multiparts,
                        FramezConfig.material_wood_min_movement_time));
            if (FramezConfig.material_iron_enabled)
                ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_IRON,
                        FramezConfig.material_iron_max_moved_blocks, FramezConfig.material_iron_max_multiparts,
                        FramezConfig.material_iron_min_movement_time));
            if (FramezConfig.material_copper_enabled)
                ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_COPPER,
                        FramezConfig.material_copper_max_moved_blocks, FramezConfig.material_copper_max_multiparts,
                        FramezConfig.material_copper_min_movement_time));
            if (FramezConfig.material_tin_enabled)
                ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_TIN,
                        FramezConfig.material_tin_max_moved_blocks, FramezConfig.material_tin_max_multiparts,
                        FramezConfig.material_tin_min_movement_time));
            if (FramezConfig.material_gold_enabled)
                ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_GOLD,
                        FramezConfig.material_gold_max_moved_blocks, FramezConfig.material_gold_max_multiparts,
                        FramezConfig.material_gold_min_movement_time));
            if (FramezConfig.material_bronze_enabled)
                ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_BRONZE,
                        FramezConfig.material_bronze_max_moved_blocks, FramezConfig.material_bronze_max_multiparts,
                        FramezConfig.material_bronze_min_movement_time));
            if (FramezConfig.material_silver_enabled)
                ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_SILVER,
                        FramezConfig.material_silver_max_moved_blocks, FramezConfig.material_silver_max_multiparts,
                        FramezConfig.material_silver_min_movement_time));
        } else {
            ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial("stickWood", References.Modifier.MATERIAL_WOOD, 6, 256, 15));
        }
        // Register default frame side modifiers
        ModifierRegistry.instance.registerFrameSideModifier(new FrameSideModifierLatching());
        ModifierRegistry.instance.registerFrameSideModifier(new FrameSideModifierSticky());

        if (FramezConfig.enable_dc_motors) {
            // Register default motor modifiers
            ModifierRegistry.instance.registerMotorModifier(new MotorModifierDC());

            // Register default motor modifier combinations
            ModifierRegistry.instance.registerMotorCombination("dc");
        }

        // Register default motor upgrades
        ModifierRegistry.instance.registerMotorUpgrade(new MotorUpgradeBouncy());
        ModifierRegistry.instance.registerMotorUpgrade(new MotorUpgradeCamouflage());
        ModifierRegistry.instance.registerMotorUpgrade(new MotorUpgradeMomentumConservation());

        // Register frame rotation plugin
        MinecraftForge.EVENT_BUS.register(new MovementPluginFramez());

        // Init the proxy
        proxy.init();

        // Init the packet handler
        NetworkHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        // Init and register blocks
        FramezBlocks.init();
        FramezBlocks.register();
        // Init and register items
        FramezItems.init();
        FramezItems.register();

        // Register oredict entries
        FramezOredict.register();

        // Register renderers
        proxy.registerRenderers();

        // Register the world generator (metamorphic stone)
        GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 0);

        // Register the dispenser placer
        BehaviorDispenserPlaceFrame bdpf = new BehaviorDispenserPlaceFrame();
        for (BlockFrame b : FramezBlocks.frames.values())
            BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(b), bdpf);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.cake, new BehaviorDispenserDeployCake());

        // Register our GUI handler
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        // Register default frame placement handler
        if (FramezConfig.enable_frame_blocks)
            FramezApi.instance().compat().registerFramePlacementHandler(new PlacementHandlerDefault());
        // Register stopper block movement handler
        TrajectoryAPI.instance().registerBlockMovementHandler(new BlockMovementHandlerStopper());
        // Register door multiblock movement handler
        MovementRegistry.instance.registerMultiblockMovementHandler(new MultiblockMovementHandlerDoor());
        // Register chest structure movement handler
        MovementRegistry.instance.registerStructureMovementHandler(new StructureMovementHandlerChest());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        // Register recipes
        FramezRecipes.register();
    }

    @EventHandler
    public void onMissingMapping(FMLMissingMappingsEvent event) {

        for (MissingMapping mapping : event.get()) {
            String name = mapping.name;
            if (name.equals("framez:moving")) {
                if (mapping.type == Type.BLOCK) {
                    mapping.remap(Blocks.air);
                } else {
                    mapping.remap(Item.getItemFromBlock(Blocks.air));
                }
            } else if (name.startsWith("framez:motor")) {
                if (mapping.type == Type.BLOCK) {
                    mapping.remap(FramezBlocks.motors.values().iterator().next());
                } else {
                    mapping.remap(Item.getItemFromBlock(FramezBlocks.motors.values().iterator().next()));
                }
            }
        }
    }
}
