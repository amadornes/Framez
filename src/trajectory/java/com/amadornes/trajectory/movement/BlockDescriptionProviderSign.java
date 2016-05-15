package com.amadornes.trajectory.movement;

import net.minecraft.block.BlockSign;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.trajectory.api.IMovingBlock;

/**
 * Because signs are special!
 */
public class BlockDescriptionProviderSign extends BlockDescriptionProviderBase {

    @Override
    public int priority() {

        return Integer.MIN_VALUE + 1001;
    }

    @Override
    public String getType() {

        return "sign";
    }

    @Override
    public boolean canHandle(IMovingBlock block) {

        return block.getBlock() instanceof BlockSign;
    }

    @Override
    public void writeBlockData(IMovingBlock block, NBTTagCompound tag) {

        super.writeBlockData(block, tag);
        if (block.getTileEntity() != null) {
            NBTTagCompound t = new NBTTagCompound();
            block.getTileEntity().writeToNBT(t);
            tag.setTag("sign", t);
        }
    }

    @Override
    public void readBlockData(IMovingBlock block, NBTTagCompound tag) {

        super.readBlockData(block, tag);

        TileEntity te = block.getTileEntity();
        if (te != null && tag.hasKey("sign"))
            te.readFromNBT(tag.getCompoundTag("sign"));
    }
}
