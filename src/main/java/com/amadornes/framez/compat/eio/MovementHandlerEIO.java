package com.amadornes.framez.compat.eio;

import net.minecraft.world.World;

import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;

public class MovementHandlerEIO implements IMovementHandler {

    @Override
    public boolean handleStartMoving(IMovingBlock block) {

        refreshConnections(block);

        return false;
    }

    @Override
    public boolean handleFinishMoving(IMovingBlock block) {

        refreshConnections(block);

        return false;
    }

    private void refreshConnections(IMovingBlock block) {

        // TileEntity te = block.getTileEntity();
        //
        // if (te != null && te instanceof IConduitBundle) {
        // IConduitBundle b = (IConduitBundle) te;
        // for (IConduit c : b.getConduits()) {
        // c.onNeighborChange(block.getWorld(), block.getX(), block.getY(), block.getZ(), block.getX(), block.getY(), block.getZ());
        // }
        // }
    }

    @Override
    public BlockMovementType getMovementType(World w, Integer x, Integer y, Integer z) {

        return null;
    }

}
