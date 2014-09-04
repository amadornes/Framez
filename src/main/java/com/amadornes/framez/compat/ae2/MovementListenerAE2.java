package com.amadornes.framez.compat.ae2;

import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.movement.IMovementListener;

public class MovementListenerAE2 implements IMovementListener {

    @Override
    public void onStartMoving(Object o, ForgeDirection direction) {

    }

    @Override
    public void onFinishMoving(Object o, ForgeDirection direction) {

        // TileEntity te = (TileEntity) o;
        // NBTTagCompound tag = new NBTTagCompound();
        // te.writeToNBT(tag);
        // Block block = te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord);
        // int meta = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
        // TileEntity newTe = ((ITileEntityProvider) block).createNewTileEntity(te.getWorldObj(), meta);
        // newTe.blockType = block;
        // newTe.blockMetadata = meta;
        // newTe.readFromNBT(tag);
        // te.getWorldObj().setTileEntity(te.xCoord, te.yCoord, te.zCoord, newTe);
    }

    @Override
    public boolean canHandle(Object o) {

        return false;// return o instanceof AENetworkTile || o instanceof AENetworkInvTile || o instanceof AENetworkPowerTile;
    }

}
