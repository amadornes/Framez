package com.amadornes.framez.init;

import java.io.File;
import java.util.regex.Pattern;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class FramezConfig {

    // --------------- //
    // RUNTIME OPTIONS //
    // --------------- //

    public static boolean click_through_frames = false;

    // -------------- //
    // CONFIG OPTIONS //
    // -------------- //

    // Main
    public static int max_movement_ticks = 20 * 60;
    public static boolean simple_mode = false;
    public static boolean craftable_metamorphic_stone = false;
    public static boolean enable_dc_motors = false;
    public static boolean craftable_dc_motors = false;

    // Issue Colors
    public static int color_issue_in_the_way = 0xFFFF00;
    public static int color_issue_multiparts = 0x4444EE;
    public static int color_issue_too_many_neighbours = 0xFF0000;
    public static int color_issue_chest = 0xFF7530;

    // Motors
    public static boolean enable_frame_sliders = true;
    public static boolean enable_frame_rotators = true;
    public static boolean enable_frame_linear_actuators = true;
    public static boolean enable_frame_blink_drives = true;

    // Frame Materials
    public static boolean material_wood_enabled = true;
    public static int material_wood_max_moved_blocks = 3;
    public static int material_wood_max_multiparts = 3;
    public static int material_wood_min_movement_time = 40;

    public static boolean material_iron_enabled = true;
    public static int material_iron_max_moved_blocks = 4;
    public static int material_iron_max_multiparts = 4;
    public static int material_iron_min_movement_time = 20;

    public static boolean material_copper_enabled = true;
    public static int material_copper_max_moved_blocks = 4;
    public static int material_copper_max_multiparts = 4;
    public static int material_copper_min_movement_time = 20;

    public static boolean material_tin_enabled = true;
    public static int material_tin_max_moved_blocks = 4;
    public static int material_tin_max_multiparts = 4;
    public static int material_tin_min_movement_time = 20;

    public static boolean material_gold_enabled = true;
    public static int material_gold_max_moved_blocks = 5;
    public static int material_gold_max_multiparts = 3;
    public static int material_gold_min_movement_time = 10;

    public static boolean material_bronze_enabled = true;
    public static int material_bronze_max_moved_blocks = 5;
    public static int material_bronze_max_multiparts = 5;
    public static int material_bronze_min_movement_time = 15;

    public static boolean material_silver_enabled = true;
    public static int material_silver_max_moved_blocks = 5;
    public static int material_silver_max_multiparts = 5;
    public static int material_silver_min_movement_time = 15;

    // -------- //
    // EXTERNAL //
    // -------- //

    public static boolean enable_frame_blocks = true;

    private static Configuration config = null;

    public static void load(File cfgFile) {

        config = new Configuration(cfgFile);
        config.load();

        // Main
        max_movement_ticks = config.getInt("Max Movement Ticks", "Main", max_movement_ticks, 20, Integer.MAX_VALUE,
                "Maximum amount of ticks a motor can move for.");
        simple_mode = config.getBoolean("Simple Mode", "Main", simple_mode,
                "Simple mode adds just one frame material and disables frame stats.");
        craftable_metamorphic_stone = config.getBoolean("Craftable Metamorphic Stone", "Main", craftable_metamorphic_stone,
                "Allow players to craft Cracked Metamorphic Stone. Useful if the world has "
                        + "already been generated and you don't want to retrogen Metamorphic Stone in.");
        enable_dc_motors = config.getBoolean("Enable DC Motors", "Main", enable_dc_motors,
                "DC Motors are meant for testing. They don't require power.");
        craftable_dc_motors = config.getBoolean("Craftable DC Motors", "Main", craftable_dc_motors,
                "Makes DC Motors craftable. Useful if you don't have any power mods installed.");

        // Issue Colors
        color_issue_in_the_way = Integer.parseInt(
                config.getString("In the way", "Issue_Colors", "#" + Integer.toHexString(color_issue_in_the_way),
                        "Color that will be shown around a block that's in the way of a moving block.",
                        Pattern.compile("^#([A-Fa-f0-9]{6})$")).substring(1), 16);
        color_issue_multiparts = Integer.parseInt(
                config.getString("Too Many Multiparts", "Issue_Colors", "#" + Integer.toHexString(color_issue_multiparts),
                        "Color that will be shown around a block that has too many multiparts in it.",
                        Pattern.compile("^#([A-Fa-f0-9]{6})$")).substring(1), 16);
        color_issue_too_many_neighbours = Integer.parseInt(
                config.getString("Too Many Neighbours", "Issue_Colors", "#" + Integer.toHexString(color_issue_too_many_neighbours),
                        "Color that will be shown around a block that has too many moving neighbours.",
                        Pattern.compile("^#([A-Fa-f0-9]{6})$")).substring(1), 16);
        color_issue_chest = Integer.parseInt(
                config.getString("Colliding Chests", "Issue_Colors", "#" + Integer.toHexString(color_issue_chest),
                        "Color that will be shown around a chest that will collide with another one when moved.",
                        Pattern.compile("^#([A-Fa-f0-9]{6})$")).substring(1), 16);

        // Motors
        enable_frame_sliders = config.get("Motors", "Enable Frame Sliders", enable_frame_sliders).getBoolean();
        enable_frame_rotators = config.get("Motors", "Enable Frame Rotators", enable_frame_rotators).getBoolean();
        enable_frame_linear_actuators = config.get("Motors", "Enable Frame Linear Actuators", enable_frame_linear_actuators).getBoolean();
        enable_frame_blink_drives = config.get("Motors", "Enable Frame Blink Drives", enable_frame_blink_drives).getBoolean();

        // Frame Materials
        material_wood_enabled = config.get("Material_Wood", "Enabled", material_wood_enabled).getBoolean();
        material_wood_max_moved_blocks = getInt("Max Moved Blocks", "Material_Wood", material_wood_max_moved_blocks, 1, 6);
        material_wood_max_multiparts = getInt("Max Multiparts", "Material_Wood", material_wood_max_multiparts, 0, 256);
        material_wood_min_movement_time = getInt("Min Movement Time", "Material_Wood", material_wood_min_movement_time, 0,
                Integer.MAX_VALUE);

        material_iron_enabled = config.get("Material_Iron", "Enabled", material_iron_enabled).getBoolean();
        material_iron_max_moved_blocks = getInt("Max Moved Blocks", "Material_Iron", material_iron_max_moved_blocks, 1, 6);
        material_iron_max_multiparts = getInt("Max Multiparts", "Material_Iron", material_iron_max_multiparts, 0, 256);
        material_iron_min_movement_time = getInt("Min Movement Time", "Material_Iron", material_iron_min_movement_time, 0,
                Integer.MAX_VALUE);

        material_copper_enabled = config.get("Material_Copper", "Enabled", material_copper_enabled).getBoolean();
        material_copper_max_moved_blocks = getInt("Max Moved Blocks", "Material_Copper", material_copper_max_moved_blocks, 1, 6);
        material_copper_max_multiparts = getInt("Max Multiparts", "Material_Copper", material_copper_max_multiparts, 0, 256);
        material_copper_min_movement_time = getInt("Min Movement Time", "Material_Copper", material_copper_min_movement_time, 0,
                Integer.MAX_VALUE);

        material_tin_enabled = config.get("Material_Tin", "Enabled", material_tin_enabled).getBoolean();
        material_tin_max_moved_blocks = getInt("Max Moved Blocks", "Material_Tin", material_tin_max_moved_blocks, 1, 6);
        material_tin_max_multiparts = getInt("Max Multiparts", "Material_Tin", material_tin_max_multiparts, 0, 256);
        material_tin_min_movement_time = getInt("Min Movement Time", "Material_Tin", material_tin_min_movement_time, 0, Integer.MAX_VALUE);

        material_gold_enabled = config.get("Material_Gold", "Enabled", material_gold_enabled).getBoolean();
        material_gold_max_moved_blocks = getInt("Max Moved Blocks", "Material_Gold", material_gold_max_moved_blocks, 1, 6);
        material_gold_max_multiparts = getInt("Max Multiparts", "Material_Gold", material_gold_max_multiparts, 0, 256);
        material_gold_min_movement_time = getInt("Min Movement Time", "Material_Gold", material_gold_min_movement_time, 0,
                Integer.MAX_VALUE);

        material_bronze_enabled = config.get("Material_Bronze", "Enabled", material_bronze_enabled).getBoolean();
        material_bronze_max_moved_blocks = getInt("Max Moved Blocks", "Material_Bronze", material_bronze_max_moved_blocks, 1, 6);
        material_bronze_max_multiparts = getInt("Max Multiparts", "Material_Bronze", material_bronze_max_multiparts, 0, 256);
        material_bronze_min_movement_time = getInt("Min Movement Time", "Material_Bronze", material_bronze_min_movement_time, 0,
                Integer.MAX_VALUE);

        material_silver_enabled = config.get("Material_Silver", "Enabled", material_silver_enabled).getBoolean();
        material_silver_max_moved_blocks = getInt("Max Moved Blocks", "Material_Silver", material_silver_max_moved_blocks, 1, 6);
        material_silver_max_multiparts = getInt("Max Multiparts", "Material_Silver", material_silver_max_multiparts, 0, 256);
        material_silver_min_movement_time = getInt("Min Movement Time", "Material_Silver", material_silver_min_movement_time, 0,
                Integer.MAX_VALUE);

        save();
    }

    public static void save() {

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
}
