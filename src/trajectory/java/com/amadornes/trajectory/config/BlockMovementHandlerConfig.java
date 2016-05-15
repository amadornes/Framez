package com.amadornes.trajectory.config;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.trajectory.api.IBlockMovementHandler;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.TrajectoryAPI;

public class BlockMovementHandlerConfig implements IBlockMovementHandler {

    @Override
    public boolean canHandle(IMovingBlock block, ITrajectory trajectory) {

        for (BlockConfig cfg : ConfigHandler.blockConfigs)
            if (cfg.matcher.matches(block))
                return true;

        return false;
    }

    @Override
    public boolean canBeMoved(IMovingBlock block, ITrajectory trajectory) {

        for (BlockConfig cfg : ConfigHandler.blockConfigs)
            if (cfg.matcher.matches(block))
                return cfg.canBeMoved;

        return false;
    }

    @Override
    public void startMoving(IMovingBlock block, ITrajectory trajectory) {

        for (BlockConfig cfg : ConfigHandler.blockConfigs) {
            if (cfg.matcher.matches(block)) {
                TrajectoryAPI.instance().defaultStartMoving(block, cfg.startInvalidate, cfg.startValidate, cfg.tick);
                return;
            }
        }
    }

    @Override
    public void finishMoving(IMovingBlock block, ITrajectory trajectory) {

        for (BlockConfig cfg : ConfigHandler.blockConfigs) {
            if (cfg.matcher.matches(block)) {
                if (block.getTileEntity() != null && cfg.rebuildTE) {
                    NBTTagCompound tag = new NBTTagCompound();
                    TileEntity old = block.getTileEntity(), te;
                    old.writeToNBT(tag);
                    block.setTileEntity(te = TileEntity.createAndLoadEntity(tag));
                    te.setWorldObj(old.getWorldObj());
                    te.xCoord = old.xCoord;
                    te.yCoord = old.yCoord;
                    te.zCoord = old.zCoord;
                    te.blockType = old.blockType;
                    te.blockMetadata = old.blockMetadata;
                }
                TrajectoryAPI.instance().defaultFinishMoving(block, cfg.finishInvalidate, cfg.finishValidate);
                return;
            }
        }
    }

}
