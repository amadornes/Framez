package com.amadornes.framez.compat.ae2;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.grid.AENetworkPowerTile;
import appeng.tile.grid.AENetworkTile;

import com.amadornes.framez.api.movement.IMovementListener;

public class MovementListenerAE2 implements IMovementListener {

    @Override
    public void onStartMoving(Object o, ForgeDirection direction) {

    }

    @Override
    public void onFinishMoving(Object o, ForgeDirection direction) {

        TileEntity te = (TileEntity) o;
        NBTTagCompound tag = new NBTTagCompound();
        te.writeToNBT(tag);
        Block block = te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord);
        int meta = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
        TileEntity newTe = ((ITileEntityProvider) block).createNewTileEntity(te.getWorldObj(), meta);
        newTe.blockType = block;
        newTe.blockMetadata = meta;
        newTe.readFromNBT(tag);
        te.getWorldObj().setTileEntity(te.xCoord, te.yCoord, te.zCoord, newTe);
    }

    @Override
    public boolean canHandle(Object o) {

        return o instanceof AENetworkTile || o instanceof AENetworkInvTile || o instanceof AENetworkPowerTile;
    }

}
