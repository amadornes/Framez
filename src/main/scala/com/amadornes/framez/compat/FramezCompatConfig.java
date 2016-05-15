package com.amadornes.framez.compat;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class FramezCompatConfig {

    public static boolean ae2_enabled = true;
    public static boolean ae2_wrench_support = true;

    public static boolean bc_enabled = true;
    public static boolean bc_wrench_support = true;
    public static boolean bc_facade_stickiness = true;
    public static boolean bc_statements = true;

    public static boolean botania_enabled = true;
    public static boolean botania_wrench_support = true;
    public static boolean botania_motor = true;

    public static boolean cc_enabled = true;
    public static boolean cc_peripheral = true;
    public static boolean cc_require_upgrade = true;

    public static boolean cofhlib_enabled = true;
    public static boolean cofhlib_wrench_support = true;
    public static boolean cofhlib_block_appearance_stickiness = true;

    public static boolean ee3_enabled = true;
    public static boolean ee3_emc = true;

    public static boolean floco_enabled = true;
    public static boolean floco_cross_stickiness = true;

    public static boolean fmp_enabled = true;
    public static boolean fmp_frame_multipart = true;
    public static boolean fmp_force_multipart_frames = true;
    public static boolean fmp_frame_microblocks = true;
    public static boolean fmp_metamorphic_stone_microblocks = true;
    public static boolean fmp_microblock_frame_parts = true;

    public static boolean ic2_enabled = true;
    public static boolean ic2_motor = true;

    public static boolean nei_enabled = true;
    public static boolean nei_subsets = true;
    public static boolean nei_hide_items = true;

    public static boolean oc_enabled = true;
    public static boolean oc_peripheral = true;
    public static boolean oc_require_upgrade = true;

    public static boolean pc_enabled = true;
    public static boolean pc_motor = true;

    public static boolean rf_enabled = true;
    public static boolean rf_motor = true;

    public static boolean tf_enabled = true;
    public static boolean tf_invar_enabled = true;
    public static int tf_invar_max_moved_blocks = 5;
    public static int tf_invar_max_multiparts = 5;
    public static int tf_invar_min_movement_time = 20;
    public static boolean tf_electrum_enabled = true;
    public static int tf_electrum_max_moved_blocks = 5;
    public static int tf_electrum_max_multiparts = 7;
    public static int tf_electrum_min_movement_time = 15;
    public static boolean tf_enderium_enabled = true;
    public static int tf_enderium_max_moved_blocks = 6;
    public static int tf_enderium_max_multiparts = 10;
    public static int tf_enderium_min_movement_time = 0;

    public static boolean waila_enabled = true;
    public static boolean waila_motor_info = true;

    public static double ratio_eu = 2.5;
    public static double ratio_mana = 100;
    public static double ratio_rf = 1;
    public static double ratio_pressure = 1;

    private static Configuration config = null;

    public static void load(File cfgFile) {

        config = new Configuration(cfgFile);
        config.load();

        ae2_enabled = getBoolean("Enabled", "AppliedEnergistics2", ae2_enabled);
        ae2_wrench_support = getBoolean("Wrench Support", "AppliedEnergistics2", ae2_wrench_support);

        bc_enabled = getBoolean("Enabled", "BuildCraft", bc_enabled);
        bc_wrench_support = getBoolean("Wrench Support", "BuildCraft", bc_wrench_support);
        bc_facade_stickiness = getBoolean("Facade Stickiness", "BuildCraft", bc_facade_stickiness);
        bc_statements = getBoolean("Gate Actions/Triggers", "BuildCraft", bc_statements);

        botania_enabled = getBoolean("Enabled", "Botania", botania_enabled);
        botania_wrench_support = getBoolean("Wrench Support", "Botania", botania_wrench_support);
        botania_motor = getBoolean("Enable Motor", "Botania", botania_motor);

        cc_enabled = getBoolean("Enabled", "ComputerCraft", cc_enabled);
        cc_peripheral = getBoolean("Enable Motor Peripheral", "ComputerCraft", cc_peripheral);
        cc_require_upgrade = getBoolean("Require Motor Upgrade", "ComputerCraft", cc_require_upgrade);

        cofhlib_enabled = getBoolean("Enabled", "CoFHLib", cofhlib_enabled);
        cofhlib_wrench_support = getBoolean("Wrench Support", "CoFHLib", cofhlib_wrench_support);
        cofhlib_block_appearance_stickiness = getBoolean("IBlockAppearance Stickiness", "CoFHLib", cofhlib_block_appearance_stickiness);

        ee3_enabled = getBoolean("Enabled", "EquivalentExchange3", ee3_enabled);
        ee3_emc = getBoolean("Enable EMC", "EquivalentExchange3", ee3_emc);

        floco_enabled = getBoolean("Enabled", "FunkyLocomotion", floco_enabled);
        floco_cross_stickiness = getBoolean("Cross-Mod Stickiness", "FunkyLocomotion", floco_cross_stickiness);

        fmp_enabled = getBoolean("Enabled", "ForgeMultiPart", fmp_enabled);
        fmp_frame_multipart = getBoolean("Enabled", "ForgeMultiPart", fmp_enabled);
        fmp_force_multipart_frames = getBoolean("Force FMP Frames", "ForgeMultiPart", fmp_force_multipart_frames);
        fmp_frame_microblocks = getBoolean("Frame Microblocks", "ForgeMultiPart", fmp_frame_microblocks);
        fmp_metamorphic_stone_microblocks = getBoolean("Metamorphic Stone Microblocks", "ForgeMultiPart", fmp_metamorphic_stone_microblocks);
        fmp_microblock_frame_parts = getBoolean("Microblock Frame Parts", "ForgeMultiPart", fmp_microblock_frame_parts);

        ic2_enabled = getBoolean("Enabled", "IndustrialCraft2", ic2_enabled);
        ic2_motor = getBoolean("Enable Motor", "IndustrialCraft2", ic2_motor);

        nei_enabled = getBoolean("Enabled", "NotEnoughItems", nei_enabled);
        nei_subsets = getBoolean("Enable Subsets", "NotEnoughItems", nei_subsets);
        nei_hide_items = getBoolean("Hide Unnecessary Items", "NotEnoughItems", nei_hide_items);

        oc_enabled = getBoolean("Enabled", "OpenComputers", oc_enabled);
        oc_peripheral = getBoolean("Enable Motor Peripheral", "OpenComputers", oc_peripheral);
        oc_require_upgrade = getBoolean("Require Motor Upgrade", "OpenComputers", oc_require_upgrade);

        pc_enabled = getBoolean("Enabled", "PneumaticCraft", pc_enabled);
        pc_motor = getBoolean("Enable Motor", "PneumaticCraft", pc_motor);

        rf_enabled = getBoolean("Enabled", "RedstoneFluxAPI", rf_enabled);
        rf_motor = getBoolean("Enable motor", "RedstoneFluxAPI", rf_motor);

        tf_enabled = getBoolean("Enabled", "ThermalFoundation", tf_enabled);
        tf_invar_enabled = getBoolean("Invar Material Enabled", "ThermalFoundation", tf_invar_enabled);
        tf_invar_max_moved_blocks = getInt("Invar Max Moved Blocks", "ThermalFoundation", tf_invar_max_moved_blocks, 1, 6);
        tf_invar_max_multiparts = getInt("Invar Max Multiparts", "ThermalFoundation", tf_invar_max_multiparts, 1, 256);
        tf_invar_min_movement_time = getInt("Invar Min Movement Time", "ThermalFoundation", tf_invar_min_movement_time, 0,
                Integer.MAX_VALUE);
        tf_electrum_enabled = getBoolean("Electrum Material Enabled", "ThermalFoundation", tf_electrum_enabled);
        tf_electrum_max_moved_blocks = getInt("Electrum Max Moved Blocks", "ThermalFoundation", tf_electrum_max_moved_blocks, 1, 6);
        tf_electrum_max_multiparts = getInt("Electrum Max Multiparts", "ThermalFoundation", tf_electrum_max_multiparts, 1, 256);
        tf_electrum_min_movement_time = getInt("Electrum Min Movement Time", "ThermalFoundation", tf_electrum_min_movement_time, 0,
                Integer.MAX_VALUE);
        tf_enderium_enabled = getBoolean("Enderium Material Enabled", "ThermalFoundation", tf_enderium_enabled);
        tf_enderium_max_moved_blocks = getInt("Enderium Max Moved Blocks", "ThermalFoundation", tf_enderium_max_moved_blocks, 1, 6);
        tf_enderium_max_multiparts = getInt("Enderium Max Multiparts", "ThermalFoundation", tf_enderium_max_multiparts, 1, 256);
        tf_enderium_min_movement_time = getInt("Enderium Min Movement Time", "ThermalFoundation", tf_enderium_min_movement_time, 0,
                Integer.MAX_VALUE);

        waila_enabled = getBoolean("Enabled", "WAILA", waila_enabled);
        waila_motor_info = getBoolean("Enable Motor Info", "WAILA", waila_motor_info);

        ratio_eu = getDouble("EU To FEU Ratio", "Power_Ratios", ratio_eu, 1D / 1000000D, 1000000D);
        ratio_mana = getDouble("Mana To FEU Ratio", "Power_Ratios", ratio_mana, 1D / 1000000D, 1000000D);
        ratio_pressure = getDouble("Pressure To FEU Ratio", "Power_Ratios", ratio_pressure, 1D / 1000000D, 1000000D);
        ratio_rf = getDouble("RF To FEU Ratio", "Power_Ratios", ratio_rf, 1D / 1000000D, 1000000D);

        config.save();
    }

    private static int getInt(String name, String category, int defaultValue, int minValue, int maxValue) {

        Property prop = config.get(category, name, defaultValue);
        prop.setLanguageKey(name);
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        return prop.getInt(defaultValue) < minValue ? minValue : (prop.getInt(defaultValue) > maxValue ? maxValue : prop
                .getInt(defaultValue));
    }

    private static double getDouble(String name, String category, double defaultValue, double minValue, double maxValue) {

        Property prop = config.get(category, name, defaultValue);
        prop.setLanguageKey(name);
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        return prop.getDouble(defaultValue) < minValue ? minValue : (prop.getDouble(defaultValue) > maxValue ? maxValue : prop
                .getDouble(defaultValue));
    }

    private static boolean getBoolean(String name, String category, boolean defaultValue) {

        Property prop = config.get(category, name, defaultValue);
        prop.setLanguageKey(name);
        return prop.getBoolean(defaultValue);
    }

}
