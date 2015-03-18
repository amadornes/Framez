package com.amadornes.framez;

import java.util.List;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import com.amadornes.framez.api.FramezAPI;
import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.client.gui.GuiHandler;
import com.amadornes.framez.compat.CompatibilityUtils;
import com.amadornes.framez.hax.GuiHax;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.modifier.FrameModifierRegistry;
import com.amadornes.framez.modifier.MotorFactory;
import com.amadornes.framez.modifier.MotorModifierRegistry;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialBronze;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialCopper;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialElectrum;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialEnderium;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialGold;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialInvar;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialIron;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialSilver;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialTin;
import com.amadornes.framez.modifier.frame.FrameModifierMaterialWood;
import com.amadornes.framez.modifier.frame.FrameModifierSimple;
import com.amadornes.framez.movement.FrameMovementRegistry;
import com.amadornes.framez.movement.MovementScheduler;
import com.amadornes.framez.movement.data.MovementDataProviderDefault;
import com.amadornes.framez.movement.handler.MovementHandlerDefault;
import com.amadornes.framez.movement.handler.MovementHandlerFMP;
import com.amadornes.framez.movement.handler.MovementHandlerFluid;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.part.PartFactory;
import com.amadornes.framez.part.PartFrame;
import com.amadornes.framez.proxy.CommonProxy;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotorSlider;
import com.amadornes.framez.util.ThreadBlockChecking;
import com.amadornes.framez.util.WrenchUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, dependencies = "required-after:qmunitylib")
public class Framez {

    @Instance(ModInfo.MODID)
    public static Framez instance;

    @SidedProxy(serverSide = "com.amadornes.framez.proxy.CommonProxy", clientSide = "com.amadornes.framez.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static Logger log;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        log = event.getModLog();

        FramezAPI.setup(new FramezAPIImpl());

        FrameMovementRegistry.instance().registerMovementHandler(new MovementHandlerDefault());
        FrameMovementRegistry.instance().registerMovementHandler(new MovementHandlerFluid());
        MovementHandlerFMP fmpHandler = new MovementHandlerFMP();
        FrameMovementRegistry.instance().registerMovementHandler(fmpHandler);
        FrameMovementRegistry.instance().registerStickyProvider(fmpHandler);

        FrameMovementRegistry.instance().registerMovementDataProvider(new MovementDataProviderDefault());

        ThreadBlockChecking.instance().start();

        FrameModifierRegistry.instance().registerModifier(new FrameModifierSimple());

        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialWood());

        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialIron());
        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialCopper());
        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialTin());
        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialSilver());

        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialGold());

        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialBronze());
        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialInvar());

        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialElectrum());

        FrameModifierRegistry.instance().registerModifier(new FrameModifierMaterialEnderium());

        CompatibilityUtils.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        FramezItems.init();
        FramezItems.register();

        FramezBlocks.init();
        FramezBlocks.register();

        MinecraftForge.EVENT_BUS.register(MovementScheduler.instance());
        FMLCommonHandler.instance().bus().register(MovementScheduler.instance());

        MinecraftForge.EVENT_BUS.register(new WrenchUtils());

        PartFactory.init();

        NetworkHandler.instance().init();

        proxy.registerRenderers();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        CompatibilityUtils.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        CompatibilityUtils.postInit(event);

        try {
            GuiHax.doGuiHax();
        } catch (Exception e) {
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(-1, true);
        }

        FrameModifierRegistry.instance().getAllCombinations(PartFrame.class);
        for (List<IMotorModifier> l : MotorModifierRegistry.instance().getAllCombinations(TileMotorSlider.class))
            GameRegistry
                    .registerTileEntity(MotorFactory.createMotorClass(TileMotorSlider.class, l), MotorFactory.getIdentifier("motor", l));
    }

    @EventHandler
    public void serverStart(FMLServerStartedEvent event) {

        ThreadBlockChecking.instance().onJoinWorld();
    }

    @EventHandler
    public void serverStop(FMLServerStoppedEvent event) {

        ThreadBlockChecking.instance().onLeaveWorld();
    }

}
