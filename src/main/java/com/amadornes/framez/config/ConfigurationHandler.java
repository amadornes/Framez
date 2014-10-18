package com.amadornes.framez.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.util.PowerHelper.PowerUnit;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler {

    public static Configuration cfg;

    public static final String CATEGORY_MOTORS = "motors";
    public static final String CATEGORY_POWER = "power";

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
            Config.Motors.isRedstoneMotorEnabled = cfg.getBoolean("isRedstoneMotorEnabled", CATEGORY_MOTORS, Config.Motors.isRedstoneMotorEnabled,
                    "Enables/disables the redstone motor. This one doesn't use any power at all, it just requires a redstone signal");
            Config.Motors.isHydCraftMotorEnabled = cfg.getBoolean("isHydCraftMotorEnabled", CATEGORY_MOTORS, Config.Motors.isHydCraftMotorEnabled,
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
        }

        // Power
        {
            cfg.getCategory(CATEGORY_POWER).setComment(
                    "Allows you to customize how much power motors use (based on RF). " + PowerUnit.RF.getPowerMultiplier() + "RF = "
                            + PowerUnit.EU.getPowerMultiplier() + "EU = " + PowerUnit.PC_PRESSURE.getPowerMultiplier() + " PC pressure = "
                            + PowerUnit.HC_PRESSURE.getPowerMultiplier() + " HC pressure");
            Config.Power.getPowerUsedPerBlock = cfg.getFloat("getPowerUsedPerBlock", CATEGORY_POWER, (float) Config.Power.getPowerUsedPerBlock, 0,
                    Float.MAX_VALUE, "Power that moving one block will use");
            Config.Power.getPowerUsedPerTileEntity = cfg.getFloat("getPowerUsedPerTileEntity", CATEGORY_POWER,
                    (float) Config.Power.getPowerUsedPerTileEntity, 0, Float.MAX_VALUE,
                    "Power that moving one TileEntity will use. Will get added to the amount moving a block uses");
            Config.Power.getPowerUsedPerMove = cfg.getFloat("getPowerUsedPerMove", CATEGORY_POWER, (float) Config.Power.getPowerUsedPerMove, 0,
                    Float.MAX_VALUE, "Power that moving a structure will take (no matter how many blocks/how big)");
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
