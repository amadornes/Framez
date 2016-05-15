package com.amadornes.framez.tile;

import net.minecraft.tileentity.TileEntity;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.movement.MovementBlink;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;

public class TileMotorBlinkDrive extends TileMotor {

    private MovementBlink movement;
    private TileMotorBlinkDrive linked;

    @Override
    public boolean canMove() {

        return linked != null && super.canMove();
    }

    @Override
    public MovementBlink getMovement() {

        if (movement == null || movement.getDirection() != getFace())
            movement = new MovementBlink(getFace(), 0, 1);
        return movement;
    }

    @Override
    public IMovement getMovement(BlockSet blocks) {

        if (linked == null)
            return null;

        int face = getFace();
        int mDist = (int) new BlockPos(linked.xCoord - xCoord, linked.yCoord - yCoord, linked.zCoord - zCoord).mag();
        BlockPos min = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), max = new BlockPos(Integer.MIN_VALUE,
                Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (IMovingBlock b : blocks) {
            min.x = Math.min(min.x, b.getPosition().x);
            min.y = Math.min(min.y, b.getPosition().y);
            min.z = Math.min(min.z, b.getPosition().z);
            max.x = Math.max(max.x, b.getPosition().x);
            max.y = Math.max(max.y, b.getPosition().y);
            max.z = Math.max(max.z, b.getPosition().z);
        }

        getMovement().setDistance(mDist - 1);
        return new MovementBlink(face, mDist - (max.getSide(face) - min.getSide(face)) - 2, 1);
    }

    @Override
    public void onFirstTick_() {

        link();
        super.onFirstTick_();
    }

    @Override
    public void setFace(int face) {

        super.setFace(face);
        link();
    }

    private void link() {

        TileMotorBlinkDrive old = linked;
        linked = null;

        int face = getFace();

        BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);
        for (int i = 0; i <= 64; i++) {
            pos.offset(face);
            TileEntity te = getWorld().getTileEntity(pos.x, pos.y, pos.z);
            if (te != null && te instanceof TileMotorBlinkDrive && ((TileMotor) te).getFace() == (face ^ 1)) {
                linked = (TileMotorBlinkDrive) te;
                break;
            }
        }

        if (old != linked && linked != null)
            linked.link();

        movement = null;
        getMovement();
        sendUpdate();
    }

    @Override
    public void onFirstTick() {

        super.onFirstTick();
        link();
    }

    @Override
    public void onUnloaded() {

        super.onUnloaded();

        if (linked != null) {
            linked.linked = null;
            linked = null;
        }
    }
}
