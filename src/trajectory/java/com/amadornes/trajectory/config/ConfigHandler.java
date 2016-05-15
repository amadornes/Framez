package com.amadornes.trajectory.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import com.amadornes.trajectory.ModInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

public class ConfigHandler {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final List<BlockConfig> blockConfigs = new ArrayList<BlockConfig>();

    public static int block_cap = 2048;

    public static void load(File configFolder) throws Exception {

        File cfgDir = new File(configFolder, ModInfo.MODID);
        cfgDir.mkdirs();
        cfgDir.mkdir();

        loadModConfigs(cfgDir);
        loadBlockConfigs(cfgDir);
    }

    private static void loadModConfigs(File configFolder) throws Exception {

        Configuration cfg = new Configuration(new File(configFolder, "Config.cfg"));
        cfg.load();

        block_cap = cfg.getInt("block_cap", "main", block_cap, 0, Integer.MAX_VALUE,
                "Maximum amount of blocks in a structure, or 0 for no limit.");

        cfg.save();
    }

    private static void loadBlockConfigs(File configFolder) throws Exception {

        File blockMovementFile = new File(configFolder, "BlockMovement.json");
        if (!blockMovementFile.exists()) {
            blockMovementFile.createNewFile();
            FileWriter fw = new FileWriter(blockMovementFile);
            fw.write("[\n]");
            fw.close();
        }
        FileReader fr = new FileReader(blockMovementFile);
        JsonArray blockMovementData = gson.fromJson(fr, JsonArray.class);
        fr.close();

        for (int i = 0; i < blockMovementData.size(); i++)
            blockConfigs.add(new BlockConfig(blockMovementData.get(i).getAsJsonObject()));
    }

    public static void saveBlockConfigs(File configFolder) throws Exception {

        File cfgDir = new File(configFolder, ModInfo.MODID);
        cfgDir.mkdirs();
        cfgDir.mkdir();

        File blockMovementFile = new File(cfgDir, "BlockMovement.json");
        blockMovementFile.delete();
        blockMovementFile.createNewFile();
        FileWriter fw = new FileWriter(blockMovementFile);

        JsonArray array = new JsonArray();
        for (BlockConfig c : blockConfigs)
            array.add(c.toJSON());
        fw.write(gson.toJson(array));

        fw.close();
    }

}
