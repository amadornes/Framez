package com.amadornes.framez;

import com.amadornes.framez.frame.FrameMaterialBasic;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezCapabilities;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.init.FramezOredict;
import com.amadornes.framez.init.FramezParts;
import com.amadornes.framez.init.FramezRecipes;
import com.amadornes.framez.motor.MotorRegistry;
import com.amadornes.framez.motor.upgrade.UpgradeBase;
import com.amadornes.framez.motor.upgrade.UpgradeCamouflage;
import com.amadornes.framez.motor.upgrade.UpgradeCreative;
import com.amadornes.framez.motor.upgrade.UpgradeFactoryBase;
import com.amadornes.framez.network.GuiHandler;
import com.amadornes.framez.network.NetworkHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = "framez", name = "Framez", version = "1.0.0")
public class Framez {

    @Instance
    public static Framez instance;
    @SidedProxy(serverSide = "com.amadornes.framez.CommonProxy", clientSide = "com.amadornes.framez.client.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(proxy);

        FrameRegistry.INSTANCE.registerMaterial(new FrameMaterialBasic("wood", 20, 40, 3, 3, "stickWood"));
        FrameRegistry.INSTANCE.registerMaterial(new FrameMaterialBasic("iron", 20, 40, 3, 3, "nuggetIron"));
        FrameRegistry.INSTANCE.registerMaterial(new FrameMaterialBasic("gold", 20, 40, 3, 3, "nuggetGold"));

        MotorRegistry.INSTANCE
                .registerUpgradeInternal(new UpgradeFactoryBase("camouflage", (m, s) -> new UpgradeCamouflage(m, s, "camouflage")));
        MotorRegistry.INSTANCE
                .registerUpgradeInternal(new UpgradeFactoryBase("telekinetic", (m, s) -> new UpgradeBase(m, s, "telekinetic")));
        MotorRegistry.INSTANCE.registerUpgradeInternal(new UpgradeFactoryBase("advTrigger", (m, s) -> new UpgradeBase(m, s, "advTrigger")));
        MotorRegistry.INSTANCE.registerUpgradeInternal(new UpgradeFactoryBase("crawling", (m, s) -> new UpgradeBase(m, s, "crawling")));
        MotorRegistry.INSTANCE.registerUpgradeInternal(new UpgradeFactoryBase("speedReg", (m, s) -> new UpgradeBase(m, s, "speedReg")));
        MotorRegistry.INSTANCE
                .registerUpgradeInternal(new UpgradeFactoryBase("computerized", (m, s) -> new UpgradeBase(m, s, "computerized")));
        MotorRegistry.INSTANCE.registerUpgradeInternal(new UpgradeFactoryBase("stepInterp", (m, s) -> new UpgradeBase(m, s, "stepInterp")));
        MotorRegistry.INSTANCE
                .registerUpgradeInternal(new UpgradeFactoryBase("soundMuffler", (m, s) -> new UpgradeBase(m, s, "soundMuffler")));
        MotorRegistry.INSTANCE.registerUpgradeInternal(new UpgradeFactoryBase("extRange", (m, s) -> new UpgradeBase(m, s, "extRange")));
        MotorRegistry.INSTANCE.registerUpgradeInternal(new UpgradeFactoryBase("creative", (m, s) -> new UpgradeCreative(m, s, "creative")));

        FramezItems.initialize();
        FramezItems.register();
        FramezBlocks.initialize();
        FramezBlocks.register();
        FramezParts.register();

        FramezCapabilities.register();
        FramezOredict.register();
        FramezRecipes.register();

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        NetworkHandler.init();

        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit();
    }

}
