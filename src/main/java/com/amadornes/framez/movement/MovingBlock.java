package com.amadornes.framez.movement;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.IDisposable;
import com.amadornes.framez.api.movement.IMovementListener;

public class MovingBlock implements IDisposable {

    private World world;
    private BlockCoord loc;

    private Block block;
    private int meta;
    private TileEntity te;
    private ForgeDirection direction;
    private double moved = 0;

    private boolean valid = true;

    public MovingBlock(BlockCoord location, World world, ForgeDirection direction) {

        loc = location;
        this.world = world;
        this.direction = direction;
    }

    public MovingBlock(int x, int y, int z, World world, ForgeDirection direction) {

        loc = new BlockCoord(x, y, z);
        this.world = world;
        this.direction = direction;
    }

    public World getWorld() {

        return world;
    }

    public BlockCoord getLocation() {

        return loc;
    }

    public Block getBlock() {

        return block;
    }

    public int getMetadata() {

        return meta;
    }

    public TileEntity getTileEntity() {

        return te;
    }

    public ForgeDirection getDirection() {

        return direction;
    }

    public double getMoved() {

        return moved;
    }

    public void move(double distance) {

        moved += distance;
    }

    public void storeData() {

        te = world.getTileEntity(loc.x, loc.y, loc.z);
        block = world.getBlock(loc.x, loc.y, loc.z);
        meta = world.getBlockMetadata(loc.x, loc.y, loc.z);
    }

    @SuppressWarnings("unchecked")
    public void placeTmp() {

        world.setBlock(loc.x, loc.y, loc.z, block, meta, 0);
        if (te != null)
            world.loadedTileEntityList.add(te);
    }

    public void place() {

        world.setBlock(loc.x + direction.offsetX, loc.y + direction.offsetY, loc.z + direction.offsetZ, block, meta, 2);

        if (te != null) {
            te.xCoord += direction.offsetX;
            te.yCoord += direction.offsetY;
            te.zCoord += direction.offsetZ;

            if (te instanceof IMovementListener) {
                ((IMovementListener) te).onFinishMoving(direction);
            } else if (te instanceof TileMultipart) {
                for (TMultiPart p : ((TileMultipart) te).jPartList()) {
                    if (p instanceof IMovementListener) {
                        ((IMovementListener) p).onFinishMoving(direction);
                    }
                }
            }

            world.setTileEntity(te.xCoord, te.yCoord, te.zCoord, te);
        }

        world.setBlockMetadataWithNotify(loc.x + direction.offsetX, loc.y + direction.offsetY, loc.z + direction.offsetZ, meta, 2);
    }

    public void removeTmp() {

        world.removeTileEntity(loc.x, loc.y, loc.z);
        world.setBlock(loc.x, loc.y, loc.z, Blocks.air);
    }

    public void remove() {

        removeTmp();

        // Notify the TE that it has started moving
        if (te != null) {
            if (te.isInvalid())
                te.validate();
            if (te instanceof IMovementListener) {
                ((IMovementListener) te).onStartMoving(direction);
            } else if (te instanceof TileMultipart) {
                for (TMultiPart p : ((TileMultipart) te).jPartList()) {
                    if (p instanceof IMovementListener) {
                        ((IMovementListener) p).onStartMoving(direction);
                    }
                }
            }
        }
    }

    public void tick() {

        if (te != null)
            te.updateEntity();
        block.updateTick(world, loc.x, loc.y, loc.z, world.rand);
    }

    public MovingObjectPosition rayTrace(Vec3 start, Vec3 end) {

        Vec3 end2 = end.addVector(-(direction.offsetX * moved), -(direction.offsetY * moved), -(direction.offsetZ * moved));

        return block.collisionRayTrace(world, loc.x, loc.y, loc.z, start, end2);
    }

    @Override
    public void dispose() {

        world = null;
        block = null;
        te = null;
        loc = null;

        valid = false;
    }

    @Override
    public boolean isValid() {

        return valid;
    }

}
