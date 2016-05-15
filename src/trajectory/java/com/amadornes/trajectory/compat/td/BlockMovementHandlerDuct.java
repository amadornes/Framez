package com.amadornes.trajectory.compat.td;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cofh.thermaldynamics.block.TileTDBase;

import com.amadornes.trajectory.api.IBlockMovementHandler;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.util.MiscUtils;

public class BlockMovementHandlerDuct implements IBlockMovementHandler {

    @Override
    public boolean canHandle(IMovingBlock block, ITrajectory trajectory) {

        return block.getTileEntity() instanceof TileTDBase;
    }

    @Override
    public boolean canBeMoved(IMovingBlock block, ITrajectory trajectory) {

        return true;
    }

    @Override
    public void startMoving(IMovingBlock block, ITrajectory trajectory) {

        TrajectoryAPI.instance().defaultStartMoving(block, true, true, true);
    }

    @Override
    public void finishMoving(IMovingBlock block, ITrajectory trajectory) {

        if (block.getTileEntity() != null) {
            NBTTagCompound tag = new NBTTagCompound();
            TileEntity old = block.getTileEntity(), te;
            old.writeToNBT(tag);

            NBTTagCompound newTag = (NBTTagCompound) tag.copy();
            if (trajectory instanceof ITrajectoryRotation) {
                ITrajectoryRotation rot = (ITrajectoryRotation) trajectory;
                for (int i = 0; i < 6; i++) {
                    newTag.removeTag("subTile" + i);
                    newTag.removeTag("attachment" + i);
                    newTag.removeTag("facade" + i);
                    int j = i;
                    for (int k = 0; k < rot.getRotations(); k++)
                        j = MiscUtils.rotate(j, rot.getAxis());
                    if (tag.hasKey("subTile" + j))
                        newTag.setTag("subTile" + i, tag.getTag("subTile" + j));
                    if (tag.hasKey("attachment" + j))
                        newTag.setTag("attachment" + i, tag.getTag("attachment" + j));
                    if (tag.hasKey("facade" + j))
                        newTag.setTag("facade" + i, tag.getTag("facade" + j));
                }
            }

            block.setTileEntity(te = TileEntity.createAndLoadEntity(newTag));
            te.setWorldObj(old.getWorldObj());
            te.xCoord = old.xCoord;
            te.yCoord = old.yCoord;
            te.zCoord = old.zCoord;
            te.blockType = old.blockType;
            te.blockMetadata = old.blockMetadata;
        }

        TrajectoryAPI.instance().defaultFinishMoving(block, true, true);
    }

}
