package com.amadornes.trajectory.config;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.trajectory.api.IMovingBlock;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameRegistry;

public interface BlockMatcher {

    public boolean matches(IMovingBlock block);

    public static class BlockMatcherBlockMod implements BlockMatcher {

        public final String modid;

        public BlockMatcherBlockMod(String modid) {

            this.modid = modid;
        }

        @Override
        public boolean matches(IMovingBlock block) {

            for (ModContainer mod : Loader.instance().getActiveModList())
                if (mod.getModId().equals(modid))
                    return mod.getOwnedPackages().contains(block.getBlock().getClass().getPackage().getName());

            return false;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof BlockMatcherBlockMod)
                return ((BlockMatcherBlockMod) obj).modid.equals(modid);

            return false;
        }
    }

    public static class BlockMatcherBlockModType implements BlockMatcher {

        public final String modid, block;
        public final int metadata;

        private Block theBlock;

        public BlockMatcherBlockModType(String modid, String block) {

            this(modid, block, -1);
        }

        public BlockMatcherBlockModType(String modid, String block, int metadata) {

            this.modid = modid;
            this.block = block;
            this.metadata = metadata;
        }

        @Override
        public boolean matches(IMovingBlock block) {

            if (theBlock == null)
                this.theBlock = GameRegistry.findBlock(modid, this.block);

            return block.getBlock() == theBlock && (metadata == -1 || block.getMetadata() == metadata);
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof BlockMatcherBlockModType)
                return ((BlockMatcherBlockModType) obj).modid.equals(modid) && ((BlockMatcherBlockModType) obj).block.equals(block)
                        && ((BlockMatcherBlockModType) obj).metadata == metadata;

            return false;
        }
    }

    public static class BlockMatcherTileMod implements BlockMatcher {

        public final String modid;

        public BlockMatcherTileMod(String modid) {

            this.modid = modid;
        }

        @Override
        public boolean matches(IMovingBlock block) {

            TileEntity te = block.getTileEntity();
            if (te == null)
                return false;

            for (ModContainer mod : Loader.instance().getActiveModList())
                if (mod.getModId().equals(modid))
                    return mod.getOwnedPackages().contains(te.getClass().getPackage().getName());

            return false;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof BlockMatcherTileMod)
                return ((BlockMatcherTileMod) obj).modid.equals(modid);

            return false;
        }
    }

    public static class BlockMatcherTileClass implements BlockMatcher {

        public final String clazz;

        public BlockMatcherTileClass(String clazz) {

            this.clazz = clazz;
        }

        @Override
        public boolean matches(IMovingBlock block) {

            TileEntity te = block.getTileEntity();
            if (te == null)
                return false;

            return te.getClass().getName().equals(clazz);
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof BlockMatcherTileClass)
                return ((BlockMatcherTileClass) obj).clazz.equals(clazz);

            return false;
        }
    }

}
