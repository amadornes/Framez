package com.amadornes.framez.movement.data;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.framez.api.movement.IMovementDataProvider;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.world.FakeWorld;

import cpw.mods.fml.common.registry.GameData;

public class MovementDataProviderDefault implements IMovementDataProvider {

    @Override
    public String getID() {

        return "default";
    }

    @Override
    public boolean canHandle(IMovingBlock block) {

        return true;
    }

    @Override
    public void writeMovementInfo(IMovingBlock block, NBTTagCompound tag) {

        tag.setString("block", GameData.getBlockRegistry().getNameForObject(block.getBlock()));
        tag.setInteger("metadata", block.getMetadata());
        NBTTagCompound t = null;
        if (block.getTileEntity() != null)
            block.getTileEntity().writeToNBT(t = new NBTTagCompound());
        if (t != null)
            tag.setTag("data", t);
    }

    @Override
    public void readMovementInfo(IMovingBlock block, NBTTagCompound tag) {

        block.setBlock(GameData.getBlockRegistry().getObject(tag.getString("block")));
        block.setMetadata(tag.getInteger("metadata"));
        TileEntity te = block.getTileEntity();

        if (tag.hasKey("data") && (te != null || block.getBlock() instanceof ITileEntityProvider)) {
            if (te == null) {
                te = ((ITileEntityProvider) block.getBlock()).createNewTileEntity(FakeWorld.getFakeWorld((MovingBlock) block),
                        block.getMetadata());
                System.out.println("creating!");
            }
            if (te != null) {
                te.readFromNBT(tag.getCompoundTag("data"));
                block.setTileEntity(te);
            }
        }

        ((MovingBlock) block).setRenderList(-1);
    }

}
