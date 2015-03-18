package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IMovable;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.tile.TileMoving;
import com.amadornes.framez.util.BlockUtils;
import com.amadornes.framez.util.Graph.INode;
import com.amadornes.framez.world.FakeWorld;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MovingBlock implements IMovingBlock, INode {

    private Vec3i location;

    private MovingStructure structure;

    private Block block = Blocks.air;
    private int meta;
    private TileEntity te;
    private List<IMovable> movables = new ArrayList<IMovable>();
    private List<IFrame> frames = null;

    private TileMoving placeholder;

    public MovingBlock(Vec3i location, MovingStructure structure, List<IFrame> frames) {

        this.location = location;

        this.structure = structure;

        this.frames = frames;
    }

    @Override
    public World getWorld() {

        return location.getWorld();
    }

    @Override
    public int getX() {

        return location.getX();
    }

    @Override
    public int getY() {

        return location.getY();
    }

    @Override
    public int getZ() {

        return location.getZ();
    }

    @Override
    public MovingStructure getStructure() {

        return structure;
    }

    public void setStructure(MovingStructure structure) {

        this.structure = structure;
    }

    @Override
    public Block getBlock() {

        return block;
    }

    @Override
    public int getMetadata() {

        return meta;
    }

    @Override
    public TileEntity getTileEntity() {

        return te;
    }

    @Override
    public MovingBlock setBlock(Block block) {

        this.block = block;
        return this;
    }

    @Override
    public MovingBlock setMetadata(int metadata) {

        meta = metadata;
        return this;
    }

    @Override
    public MovingBlock setTileEntity(TileEntity tileentity) {

        te = tileentity;
        return this;
    }

    @Override
    public MovingBlock snapshot() {

        block = location.getBlock();
        meta = location.getBlockMeta();
        te = location.getTileEntity();
        movables = FrameMovementRegistry.instance().findMovables(getWorld(), getX(), getY(), getZ());

        return this;
    }

    protected void remove() {

        snapshot();

        boolean handled = false;
        for (IMovable movable : movables) {
            if (movable.startMoving(this)) {
                handled = true;
                break;
            }
        }

        if (!handled)
            startMoving(true, true);

        // Place placeholder
        if (getStructure().getMovement() instanceof IMovementSlide) {
            ForgeDirection direction = ((IMovementSlide) getStructure().getMovement()).getDirection();

            getWorld().setBlock(getX(), getY(), getZ(), FramezBlocks.moving, 0, 0);
            TileMoving ph1 = null;
            if (placeholder != null)
                ph1 = placeholder;
            else
                ph1 = placeholder = new TileMoving();
            ph1.setBlockA(this);
            getWorld().setTileEntity(getX(), getY(), getZ(), ph1);

            TileMoving ph2 = null;
            MovingBlock b = structure.getBlock(getX() + direction.offsetX, getY() + direction.offsetY, getZ() + direction.offsetZ);
            if (b != null) {
                if (b.placeholder == null)
                    ph2 = b.placeholder = new TileMoving();
                else
                    ph2 = b.placeholder;
            } else {
                getWorld().setBlock(getX() + direction.offsetX, getY() + direction.offsetY, getZ() + direction.offsetZ,
                        FramezBlocks.moving, 0, 0);
                getWorld().setTileEntity(getX() + direction.offsetX, getY() + direction.offsetY, getZ() + direction.offsetZ,
                        ph2 = new TileMoving());
            }
            ph2.setBlockB(this);
        }
    }

    @Override
    public void startMoving(boolean invalidate, boolean validate) {

        TileEntity te = getTileEntity();
        if (te != null) {
            if (invalidate)
                te.invalidate();
        }

        if (!getWorld().isRemote) {
            BlockUtils.setBlockSneaky(getWorld(), getX(), getY(), getZ(), Blocks.air);
            getWorld().setBlockMetadataWithNotify(getX(), getY(), getZ(), 0, 2);
        }

        if (te != null)
            BlockUtils.removeTileEntity(getWorld(), getX(), getY(), getZ());

        if (te != null && validate) {
            te.setWorldObj(FakeWorld.getFakeWorld(this));
            te.validate();
        }
    }

    protected void notifyRemoval() {

        if (!getWorld().isRemote) {
            getWorld().notifyBlocksOfNeighborChange(getX(), getY(), getZ(), getBlock());
            FakeWorld.getFakeWorld(this).notifyBlockOfNeighborChange(getX(), getY(), getZ(), getBlock());
        } else {
            getWorld().func_147479_m(getX(), getY(), getZ());
        }
    }

    protected void place() {

        // // Remove placeholder
        // if (getStructure().getMovement() instanceof IMovementSlide) {
        // ForgeDirection direction = ((IMovementSlide) getStructure().getMovement()).getDirection();
        //
        // if (placeholder != null) {
        // placeholder.setBlockA(null);
        // MovingBlock b = structure.getBlock(getX() + direction.offsetX, getY() + direction.offsetY, getZ() + direction.offsetZ);
        // if (b != null)
        // b.placeholder.setBlockB(null);
        // }
        // }

        boolean handled = false;
        for (IMovable movable : movables) {
            if (movable.finishMoving(this)) {
                handled = true;
                break;
            }
        }

        if (!handled)
            finishMoving(true, true);
    }

    @Override
    public void finishMoving(boolean invalidate, boolean validate) {

        Vec3i newLocation = getStructure().getMovement().transform(new Vec3i(this));
        TileEntity te = getTileEntity();

        if (te != null && invalidate)
            te.invalidate();

        // if (!newLocation.getWorld().isRemote) {
        if (getWorld().isRemote) {
            newLocation.getWorld().setBlock(newLocation.getX(), newLocation.getY(), newLocation.getZ(), getBlock(), 0, 0);
        } else {
            BlockUtils.setBlockSneaky(newLocation.getWorld(), newLocation.getX(), newLocation.getY(), newLocation.getZ(), getBlock());
        }
        newLocation.getWorld().setBlockMetadataWithNotify(newLocation.getX(), newLocation.getY(), newLocation.getZ(), getMetadata(), 2);
        // }

        if (te != null) {
            te.setWorldObj(newLocation.getWorld());
            te.xCoord = newLocation.getX();
            te.yCoord = newLocation.getY();
            te.zCoord = newLocation.getZ();

            if (validate)
                te.validate();
            BlockUtils.setTileEntity(newLocation.getWorld(), newLocation.getX(), newLocation.getY(), newLocation.getZ(), te);
        }
    }

    protected void notifyPlacement() {

        Vec3i newLocation = getStructure().getMovement().transform(new Vec3i(this));
        if (!newLocation.getWorld().isRemote) {
            newLocation.getWorld().notifyBlocksOfNeighborChange(newLocation.getX(), newLocation.getY(), newLocation.getZ(), getBlock());
            newLocation.getWorld().notifyBlockOfNeighborChange(newLocation.getX(), newLocation.getY(), newLocation.getZ(), getBlock());
        }
        newLocation.getWorld().markBlockRangeForRenderUpdate(newLocation.getX(), newLocation.getY(), newLocation.getZ(),
                newLocation.getX(), newLocation.getY(), newLocation.getZ());
    }

    @Override
    public String toString() {

        return "MovingBlock [location=" + location + ", block=" + block + ", meta=" + meta + ", te=" + te + "]";
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof MovingBlock) {
            MovingBlock b = (MovingBlock) obj;
            return b.getX() == getX() && b.getY() == getY() && b.getZ() == getZ();
        }
        return false;
    }

    @Override
    public int hashCode() {

        return getX() + getY() << 8 + getZ() << 16;
    }

    @Override
    public int getMaxNeighbors() {

        if (frames != null) {
            int max = 0;
            for (IFrame f : frames)
                max = Math.max(max, f.getMaxMovedBlocks());
            return max;
        }

        return 0;
    }

    public int getMaxMultiparts() {

        if (frames != null) {
            int max = 0;
            for (IFrame f : frames)
                max = Math.max(max, f.getMaxMultiparts());
            return max;
        }

        return 0;
    }

    public int getMultiparts() {

        if (frames != null) {
            int max = 0;
            for (IFrame f : frames)
                max = Math.max(max, f.getMultiparts());
            return max;
        }

        return 0;
    }

    // Clientside stuff (rendering, mostly)

    private int renderList = -1;

    @SideOnly(Side.CLIENT)
    public void scheduleReRender() {

        renderList = -1;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderList() {

        return renderList;
    }

    @SideOnly(Side.CLIENT)
    public void setRenderList(int renderList) {

        this.renderList = renderList;
    }

}
