package com.amadornes.framez.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler {

    public static Configuration cfg;

    public static final String CATEGORY_MOTORS = "motors";
    public static final String CATEGORY_POWER_USAGE = "power_usage";
    public static final String CATEGORY_POWER_RATIOS = "power_ratios";

    public static void init(File configFile) {

        if (cfg == null) {
            cfg = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {

        // Motors
        {
            cfg.getCategory(CATEGORY_MOTORS).setComment("Enables/disables specific motors. Power ratio is not configurable as of now.");
            Config.Motors.isRedstoneMotorEnabled = cfg.getBoolean("isRedstoneMotorEnabled", CATEGORY_MOTORS,
                    Config.Motors.isRedstoneMotorEnabled,
                    "Enables/disables the redstone motor. This one doesn't use any power at all, it just requires a redstone signal");
            Config.Motors.isHydCraftMotorEnabled = cfg.getBoolean("isHydCraftMotorEnabled", CATEGORY_MOTORS,
                    Config.Motors.isHydCraftMotorEnabled,
                    "Enables/disables the HydrauliCraft motor. This one needs HC pressure to run and it also requires a redstone signal");
            Config.Motors.isIC2MotorEnabled = cfg.getBoolean("isIC2MotorEnabled", CATEGORY_MOTORS, Config.Motors.isIC2MotorEnabled,
                    "Enables/disables the IC2 motor. This one needs EU to run and it also requires a redstone signal");
            Config.Motors.isPneumaticCraftMotorEnabled = cfg.getBoolean("isPneumaticCraftMotorEnabled", CATEGORY_MOTORS,
                    Config.Motors.isPneumaticCraftMotorEnabled,
                    "Enables/disables the PneumaticCraft motor. This one needs PC pressure to run and it also requires a redstone signal");
            Config.Motors.isRedstoneFluxMotorEnabled = cfg.getBoolean("isRedstoneFluxMotorEnabled", CATEGORY_MOTORS,
                    Config.Motors.isRedstoneFluxMotorEnabled,
                    "Enables/disables the redstone flux motor. This one needs RF to run and it also requires a redstone signal");
            Config.Motors.isBloodMagicMotorEnabled = cfg.getBoolean("isBloodMagicMotorEnabled", CATEGORY_MOTORS,
                    Config.Motors.isBloodMagicMotorEnabled,
                    "Enables/disables the Blood Magic motor. This one needs LP to run and it also requires a redstone signal");
            Config.Motors.isAEMotorEnabled = cfg
                    .getBoolean("isAEMotorEnabled", CATEGORY_MOTORS, Config.Motors.isAEMotorEnabled,
                            "Enables/disables the Applied Energistics motor. This one needs AE power to run and it also requires a redstone signal");
        }

        // Power Usage
        {
            cfg.getCategory(CATEGORY_POWER_USAGE).setComment("Allows you to customize how much power motors use in Framez Power Units.");
            Config.PowerUsage.getPowerUsedPerBlock = cfg.getFloat("getPowerUsedPerBlock", CATEGORY_POWER_USAGE,
                    (float) Config.PowerUsage.getPowerUsedPerBlock, 0, Float.MAX_VALUE, "Power that moving one block will use");
            Config.PowerUsage.getPowerUsedPerTileEntity = cfg.getFloat("getPowerUsedPerTileEntity", CATEGORY_POWER_USAGE,
                    (float) Config.PowerUsage.getPowerUsedPerTileEntity, 0, Float.MAX_VALUE,
                    "Power that moving one TileEntity will use. Will get added to the amount moving a block uses");
            Config.PowerUsage.getPowerUsedPerMove = cfg.getFloat("getPowerUsedPerMove", CATEGORY_POWER_USAGE,
                    (float) Config.PowerUsage.getPowerUsedPerMove, 0, Float.MAX_VALUE,
                    "Power that moving a structure will take (no matter how many blocks/how big)");
        }

        // Power Ratios
        {
            cfg.getCategory(CATEGORY_POWER_RATIOS).setComment(
                    "Allows you to customize the power ratios for all the power systems supported by Framez itself.");
            Config.PowerRatios.hcPressure = cfg.getFloat("hcPressure", CATEGORY_POWER_RATIOS, (float) Config.PowerRatios.hcPressure, 1,
                    Float.MAX_VALUE, "Ratio from HC pressure to FPUs (default is 5:1)");
            Config.PowerRatios.eu = cfg.getFloat("eu", CATEGORY_POWER_RATIOS, (float) Config.PowerRatios.eu, 1, Float.MAX_VALUE,
                    "Ratio from EUs to FPUs (default is 1:5)");
            Config.PowerRatios.pcPressure = cfg.getFloat("pcPressure", CATEGORY_POWER_RATIOS, (float) Config.PowerRatios.pcPressure, 1,
                    Float.MAX_VALUE, "Ratio from PC pressure to FPUs (default is 1:1000)");
            Config.PowerRatios.rf = cfg.getFloat("rf", CATEGORY_POWER_RATIOS, (float) Config.PowerRatios.rf, 1, Float.MAX_VALUE,
                    "Ratio from RF to FPUs (default is 1:1)");
            Config.PowerRatios.bmLP = cfg.getFloat("bmLP", CATEGORY_POWER_RATIOS, (float) Config.PowerRatios.bmLP, 1, Float.MAX_VALUE,
                    "Ratio from BloodMagic LPs to FPUs (default is 7:4)");

        }

        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {

        if (event.modID.equalsIgnoreCase(ModInfo.MODID)) {
            loadConfiguration();
        }
    }
}
