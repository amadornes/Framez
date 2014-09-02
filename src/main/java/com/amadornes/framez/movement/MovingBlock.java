package com.amadornes.framez.movement;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.movement.IMovementListenerSelf;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.tile.TileMoving;
import com.amadornes.framez.world.WorldWrapper;

public class MovingBlock {

    private World world;
    private BlockCoord loc;

    private Block block;
    private int meta;
    private TileEntity te;

    private MovingStructure structure;

    public MovingBlock(BlockCoord location, World world, MovingStructure structure) {

        loc = location;
        this.world = world;
        this.structure = structure;
    }

    public MovingBlock(int x, int y, int z, World world, MovingStructure structure) {

        loc = new BlockCoord(x, y, z);
        this.world = world;
        this.structure = structure;
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

        return structure.getDirection();
    }

    public double getMoved() {

        return structure.getMoved();
    }

    public WorldWrapper getWorldWrapper() {

        return structure.getWorldWrapper();
    }

    public MovingStructure getStructure() {

        return structure;
    }

    public void storeData() {

        te = world.getTileEntity(loc.x, loc.y, loc.z);
        block = world.getBlock(loc.x, loc.y, loc.z);
        meta = world.getBlockMetadata(loc.x, loc.y, loc.z);
    }

    public void place() {

        if (te != null) {
            te.xCoord += getDirection().offsetX;
            te.yCoord += getDirection().offsetY;
            te.zCoord += getDirection().offsetZ;
            te.setWorldObj(world);
        }

        world.setBlock(loc.x + getDirection().offsetX, loc.y + getDirection().offsetY, loc.z + getDirection().offsetZ, block, meta, 2);

        if (te != null) {
            if (te instanceof IMovementListenerSelf) {
                ((IMovementListenerSelf) te).onFinishMoving(getDirection());
            } else if (te instanceof TileMultipart) {
                for (TMultiPart p : ((TileMultipart) te).jPartList()) {
                    if (p instanceof IMovementListenerSelf) {
                        ((IMovementListenerSelf) p).onFinishMoving(getDirection());
                    }
                }
            }

            world.setTileEntity(te.xCoord, te.yCoord, te.zCoord, te);
        }

        world.setBlockMetadataWithNotify(loc.x + getDirection().offsetX, loc.y + getDirection().offsetY, loc.z + getDirection().offsetZ, meta, 2);

        MovementApi.INST.onPlace(te != null ? te : block, getDirection());
    }

    public void remove() {

        world.removeTileEntity(loc.x, loc.y, loc.z);
        world.setBlock(loc.x, loc.y, loc.z, Blocks.air, 0, 2);

        // Notify the TE that it has started moving
        if (te != null) {
            if (te.isInvalid())
                te.validate();
            if (te instanceof IMovementListenerSelf) {
                ((IMovementListenerSelf) te).onStartMoving(getDirection());
            } else if (te instanceof TileMultipart) {
                for (TMultiPart p : ((TileMultipart) te).jPartList()) {
                    if (p instanceof IMovementListenerSelf) {
                        ((IMovementListenerSelf) p).onStartMoving(getDirection());
                    }
                }
            }
        }

        MovementApi.INST.onRemove(te != null ? te : block, getDirection());

        if (te != null) {
            te.setWorldObj(getWorldWrapper());
        }
    }

    public void placePlaceholder() {

        world.setBlock(loc.x, loc.y, loc.z, FramezBlocks.block_moving, 0, 2);
        TileMoving te = new TileMoving();
        te.setBlockA(this);
        world.setTileEntity(loc.x, loc.y, loc.z, te);

        TileMoving te2 = null;
        if (world.getBlock(loc.x + getDirection().offsetX, loc.y + getDirection().offsetY, loc.z + getDirection().offsetZ) == FramezBlocks.block_moving) {
            te2 = (TileMoving) world.getTileEntity(loc.x + getDirection().offsetX, loc.y + getDirection().offsetY, loc.z + getDirection().offsetZ);
        } else {
            world.setBlock(loc.x + getDirection().offsetX, loc.y + getDirection().offsetY, loc.z + getDirection().offsetZ, FramezBlocks.block_moving,
                    0, 2);
            world.setTileEntity(loc.x + getDirection().offsetX, loc.y + getDirection().offsetY, loc.z + getDirection().offsetZ,
                    te2 = new TileMoving());
        }
        te2.setBlockB(this);
    }

    public void removePlaceholder() {

        world.setBlockToAir(loc.x, loc.y, loc.z);
        world.setBlockToAir(loc.x + getDirection().offsetX, loc.y + getDirection().offsetY, loc.z + getDirection().offsetZ);
    }

    public void tick() {

        if (te != null)
            te.updateEntity();
        block.updateTick(world, loc.x, loc.y, loc.z, world.rand);
    }

}
