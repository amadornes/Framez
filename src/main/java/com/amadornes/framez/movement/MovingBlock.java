package com.amadornes.framez.movement;

import java.util.EnumSet;
import java.util.function.Supplier;

import com.amadornes.framez.api.frame.IFrame;
import com.amadornes.framez.api.frame.IStickable;
import com.amadornes.framez.api.frame.ISticky;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.frame.FrameHelper;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.util.Graph.INode;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class MovingBlock implements INode, IMovingBlock {

    public final Supplier<World> world;
    public final BlockPos pos;
    public final IBlockState state;
    public final TileEntity tile;
    public final IFrame frame;

    private EnumSet<EnumFacing> directions = null;
    private boolean canStickTo = true;

    private ISticky[] stickySides = null;
    private IStickable[] stickableSides = null;

    public MovingBlock(Supplier<World> world, BlockPos pos) {

        this(world, pos, null, true);
    }

    public MovingBlock(Supplier<World> world, BlockPos pos, EnumSet<EnumFacing> directions, boolean canStickTo) {

        this(world, pos, world.get().getBlockState(pos), world.get().getTileEntity(pos), directions);
        this.canStickTo = canStickTo;
    }

    public MovingBlock(Supplier<World> world, BlockPos pos, IBlockState state, TileEntity tile, EnumSet<EnumFacing> directions) {

        this.world = world;
        this.pos = pos;
        this.state = state;
        this.tile = tile;
        this.frame = FrameRegistry.INSTANCE.getFrame(this.tile);
        this.directions = directions;
    }

    @Override
    public int getMaxNeighbors() {

        return directions != null ? directions.size() : FrameHelper.getMaxCarriedBlocks(frame);
    }

    public boolean isSticky(EnumFacing face) {

        if (directions != null) return directions.contains(face);

        if (stickySides == null) {
            stickySides = new ISticky[6];
            for (int i = 0; i < 6; i++)
                stickySides[i] = FrameRegistry.INSTANCE.getSticky(world.get(), pos, face, tile);
        }
        return stickySides[face.ordinal()] != null && stickySides[face.ordinal()].isSticky();
    }

    public boolean canStickTo(EnumFacing face) {

        if (!canStickTo) return false;
        if (directions != null) return directions.contains(face);

        if (stickableSides == null) {
            stickableSides = new IStickable[6];
            for (int i = 0; i < 6; i++)
                stickableSides[i] = FrameRegistry.INSTANCE.getStickable(world.get(), pos, face, tile);
        }
        if (stickableSides[face.ordinal()] != null) {
            return stickableSides[face.ordinal()].canStickTo();
        } else {
            return !state.getBlock().isAir(world.get(), pos);
        }
    }

    @Override
    public int hashCode() {

        return pos.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (obj instanceof MovingBlock) return ((MovingBlock) obj).pos.equals(pos);
        if (obj instanceof BlockPos) return pos.equals(obj);
        return true;
    }

    @Override
    public World getWorld() {

        return world.get();
    }

    @Override
    public BlockPos getPos() {

        return pos;
    }

    @Override
    public IBlockState getState() {

        return state;
    }

    @Override
    public TileEntity getTile() {

        return tile;
    }

    public NBTTagCompound toNBT() {

        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("pos", pos.toLong());
        tag.setInteger("block", Block.blockRegistry.getIDForObject(state.getBlock()));
        tag.setInteger("meta", state.getBlock().getMetaFromState(state));
        return tag;
    }

    public static MovingBlock fromNBT(NBTTagCompound tag) {

        return null;// TODO: Deserialize
    }

}