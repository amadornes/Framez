package com.amadornes.trajectory.config;

import com.google.gson.JsonObject;

public class BlockConfig {

    public final BlockMatcher matcher;
    public final boolean startInvalidate, startValidate;
    public final boolean finishInvalidate, finishValidate;
    public final boolean tick;
    public final boolean canBeMoved, rebuildTE;

    public BlockConfig(JsonObject config) throws Exception {

        if (!config.has("match"))
            throw new RuntimeException("No matcher was found!");

        JsonObject match = config.get("match").getAsJsonObject();
        if (!match.has("type"))
            throw new RuntimeException("No matcher type was defined!");
        String matchType = match.get("type").getAsString();
        if (matchType.equals("block_mod")) {
            if (!match.has("modid"))
                throw new RuntimeException("No modid was defined!");
            matcher = new BlockMatcher.BlockMatcherBlockMod(match.get("modid").getAsString());
        } else if (matchType.equals("block")) {
            if (!match.has("modid"))
                throw new RuntimeException("No modid was defined!");
            if (!match.has("block"))
                throw new RuntimeException("No block was defined!");
            if (match.has("metadata"))
                matcher = new BlockMatcher.BlockMatcherBlockModType(match.get("modid").getAsString(), match.get("block").getAsString(),
                        match.get("metadata").getAsInt());
            else
                matcher = new BlockMatcher.BlockMatcherBlockModType(match.get("modid").getAsString(), match.get("block").getAsString());
        } else if (matchType.equals("tile_mod")) {
            if (!match.has("modid"))
                throw new RuntimeException("No modid was defined!");
            matcher = new BlockMatcher.BlockMatcherTileMod(match.get("modid").getAsString());
        } else if (matchType.equals("tile_class")) {
            if (!match.has("class"))
                throw new RuntimeException("No class was defined!");
            matcher = new BlockMatcher.BlockMatcherTileClass(match.get("class").getAsString());
        } else {
            throw new RuntimeException("\"" + matchType + "\" is not a valid matcher type!");
        }

        startInvalidate = config.has("start_invalidate") ? config.get("start_invalidate").getAsBoolean() : true;
        startValidate = config.has("start_validate") ? config.get("start_validate").getAsBoolean() : true;
        finishInvalidate = config.has("finish_invalidate") ? config.get("finish_invalidate").getAsBoolean() : true;
        finishValidate = config.has("finish_validate") ? config.get("finish_validate").getAsBoolean() : true;

        tick = config.has("tick") ? config.get("tick").getAsBoolean() : true;
        canBeMoved = config.has("can_be_moved") ? config.get("can_be_moved").getAsBoolean() : true;
        rebuildTE = config.has("rebuild_tile") ? config.get("rebuild_tile").getAsBoolean() : false;
    }

    public BlockConfig(BlockMatcher matcher, boolean startInvalidate, boolean startValidate, boolean finishInvalidate,
            boolean finishValidate, boolean tick, boolean canBeMoved, boolean rebuildTE) {

        this.matcher = matcher;
        this.startInvalidate = startInvalidate;
        this.startValidate = startValidate;
        this.finishInvalidate = finishInvalidate;
        this.finishValidate = finishValidate;
        this.tick = tick;
        this.canBeMoved = canBeMoved;
        this.rebuildTE = rebuildTE;
    }

    public JsonObject toJSON() {

        JsonObject obj = new JsonObject();

        JsonObject match = new JsonObject();
        if (matcher instanceof BlockMatcher.BlockMatcherBlockMod) {
            match.addProperty("type", "block_mod");
            match.addProperty("modid", ((BlockMatcher.BlockMatcherBlockMod) matcher).modid);
        } else if (matcher instanceof BlockMatcher.BlockMatcherBlockModType) {
            match.addProperty("type", "block");
            match.addProperty("modid", ((BlockMatcher.BlockMatcherBlockModType) matcher).modid);
            match.addProperty("block", ((BlockMatcher.BlockMatcherBlockModType) matcher).block);
            match.addProperty("metadata", ((BlockMatcher.BlockMatcherBlockModType) matcher).metadata);
        } else if (matcher instanceof BlockMatcher.BlockMatcherTileMod) {
            match.addProperty("type", "tile_mod");
            match.addProperty("modid", ((BlockMatcher.BlockMatcherTileMod) matcher).modid);
        } else if (matcher instanceof BlockMatcher.BlockMatcherTileClass) {
            match.addProperty("type", "tile_class");
            match.addProperty("class", ((BlockMatcher.BlockMatcherTileClass) matcher).clazz);
        }
        obj.add("match", match);

        obj.addProperty("start_invalidate", startInvalidate);
        obj.addProperty("start_validate", startValidate);
        obj.addProperty("finish_invalidate", finishInvalidate);
        obj.addProperty("finish_validate", finishValidate);

        obj.addProperty("tick", tick);
        obj.addProperty("can_be_moved", canBeMoved);
        obj.addProperty("rebuild_tile", rebuildTE);

        return obj;
    }

}
