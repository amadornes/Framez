package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IFrame.IFrameProvider;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovementRegistry;
import com.amadornes.framez.api.movement.IMultiblockMovementHandler;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.api.movement.ISticky.IAdvancedSticky;
import com.amadornes.framez.api.movement.ISticky.IStickinessHandler;
import com.amadornes.framez.api.movement.IStructureMovementHandler;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.vec.BlockPos;

public class MovementRegistry implements IMovementRegistry {

    public static final MovementRegistry instance = new MovementRegistry();

    public List<IStickinessHandler> stickinessHandlers = new ArrayList<IStickinessHandler>();
    public List<IFrameProvider> frameProviders = new ArrayList<IFrameProvider>();
    public List<IStructureMovementHandler> structureMovementHandlers = new ArrayList<IStructureMovementHandler>();
    public List<IMultiblockMovementHandler> multiblockMovementHandlers = new ArrayList<IMultiblockMovementHandler>();

    @Override
    public void registerStickinessHandler(IStickinessHandler handler) {

        if (handler == null)
            throw new NullPointerException("A mod attempted to register a null stickiness handler! Report this to the author!");
        if (stickinessHandlers.contains(handler))
            throw new IllegalStateException(
                    "A mod attempted to register a stickiness handler that was already registered! Report this to the author!");

        stickinessHandlers.add(handler);
    }

    @Override
    public void registerFrameProvider(IFrameProvider provider) {

        if (provider == null)
            throw new NullPointerException("A mod attempted to register a null frame provider! Report this to the author!");
        if (frameProviders.contains(provider))
            throw new IllegalStateException(
                    "A mod attempted to register a frame provider that was already registered! Report this to the author!");

        frameProviders.add(provider);
    }

    @Override
    public void registerStructureMovementHandler(IStructureMovementHandler handler) {

        if (handler == null)
            throw new NullPointerException("A mod attempted to register a null structure movement handler! Report this to the author!");
        if (structureMovementHandlers.contains(handler))
            throw new IllegalStateException(
                    "A mod attempted to register a structure movement handler that was already registered! Report this to the author!");

        structureMovementHandlers.add(handler);
    }

    @Override
    public void registerMultiblockMovementHandler(IMultiblockMovementHandler handler) {

        if (handler == null)
            throw new NullPointerException("A mod attempted to register a null multiblock movement handler! Report this to the author!");
        if (multiblockMovementHandlers.contains(handler))
            throw new IllegalStateException(
                    "A mod attempted to register a multiblock movement handler that was already registered! Report this to the author!");

        multiblockMovementHandlers.add(handler);
    }

    @Override
    public BlockSet findBlocks(World world, BlockPos position, IMovement movement) {

        return findBlocks(world, position, movement, IgnoreMode.PASS_THROUGH);
    }

    @Override
    public BlockSet findBlocks(World world, BlockPos position, IMovement movement, IgnoreMode ignoreMode, BlockPos... ignored) {

        return MovementHelper.findMovedBlocks(world, position, movement, ignoreMode, ignored);
    }

    // Wrapper for the handlers

    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        // If there's a frame, return whatever that frame would return.
        IFrame frame = getFrameAt(world, position);
        if (frame != null)
            return frame.isSideSticky(world, position, side, movement);

        // Go through the handers. If there's a valid one, return its value.
        for (IStickinessHandler handler : stickinessHandlers) {
            if (handler.canHandle(world, position)) {
                if (handler.providesFinalValues())
                    return handler.isSideSticky(world, position, side, movement);
                else if (handler.isSideSticky(world, position, side, movement))
                    return true;
            }
        }

        // If there's an ISticky in that world position, return what it would return.
        ISticky sticky = findWorldSticky(world, position);
        if (sticky != null)
            return sticky.isSideSticky(world, position, side, movement);

        // If there aren't any handlers, the side isn't sticky.
        return false;
    }

    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        // If there's a frame, return whatever that frame would return.
        IFrame frame = getFrameAt(world, position);
        if (frame != null)
            return frame.canStickToSide(world, position, side, movement);

        // Go through the handers. If there's a valid one, return its value.
        for (IStickinessHandler handler : stickinessHandlers) {
            if (handler.canHandle(world, position)) {
                if (handler.providesFinalValues())
                    return handler.canStickToSide(world, position, side, movement);
                else if (handler.canStickToSide(world, position, side, movement))
                    return true;
            }
        }

        // If there's an ISticky in that world position, return what it would return.
        ISticky sticky = findWorldSticky(world, position);
        if (sticky != null)
            return sticky.canStickToSide(world, position, side, movement);

        // If there aren't any handlers, fallback to the block hardness/is air
        Block block = world.getBlock(position.x, position.y, position.z);
        if (block.isAir(world, position.x, position.y, position.z))
            return false;
        return block.getBlockHardness(world, position.x, position.y, position.z) > -1;
    }

    public boolean canBeOverriden(World world, BlockPos position) {

        // If there's a frame, return whatever that frame would return.
        IFrame frame = getFrameAt(world, position);
        if (frame != null)
            return frame.canBeOverriden(world, position);

        // Go through the handers. If there's a valid one, return its value.
        for (IStickinessHandler handler : stickinessHandlers) {
            if (handler.canHandle(world, position)) {
                if (handler.providesFinalValues())
                    return handler.canBeOverriden(world, position);
                else if (handler.canBeOverriden(world, position))
                    return true;
            }
        }

        // If there's an ISticky in that world position, return what it would return.
        ISticky sticky = findWorldSticky(world, position);
        if (sticky != null)
            return sticky.canBeOverriden(world, position);

        // If there aren't handlers for this block, fall back to the isAir() and isReplaceable() methods in the block.
        Block block = world.getBlock(position.x, position.y, position.z);
        return block.isAir(world, position.x, position.y, position.z) && block.isReplaceable(world, position.x, position.y, position.z);
    }

    public int getSideStickiness(World world, BlockPos position, int side) {

        // If there's a frame, return whatever that frame would return.
        IFrame frame = getFrameAt(world, position);
        if (frame != null) {
            if (frame instanceof IAdvancedSticky)
                return ((IAdvancedSticky) frame).getSideStickiness(world, position, side);
            return 0;
        }

        // Go through the handers. If there's a valid one, return its value.
        boolean set = false;
        int max = Integer.MIN_VALUE;
        for (IStickinessHandler handler : stickinessHandlers) {
            if (handler instanceof IAdvancedSticky && handler.canHandle(world, position)) {
                if (handler.providesFinalValues()) {
                    return ((IAdvancedSticky) handler).getSideStickiness(world, position, side);
                } else {
                    set = true;
                    max = Math.max(max, ((IAdvancedSticky) handler).getSideStickiness(world, position, side));
                }
            }
        }
        if (set)
            return max;

        // If there's an ISticky in that world position, return what it would return.
        ISticky sticky = findWorldSticky(world, position);
        if (sticky != null && sticky instanceof IAdvancedSticky)
            return ((IAdvancedSticky) sticky).getSideStickiness(world, position, side);

        return 0;
    }

    public int getRequiredStickiness(World world, BlockPos position, int side) {

        // If there's a frame, return whatever that frame would return.
        IFrame frame = getFrameAt(world, position);
        if (frame != null) {
            if (frame instanceof IAdvancedSticky)
                return ((IAdvancedSticky) frame).getRequiredStickiness(world, position, side);
            return 0;
        }

        // Go through the handers. If there's a valid one, return its value.
        boolean set = false;
        int max = Integer.MIN_VALUE;
        for (IStickinessHandler handler : stickinessHandlers) {
            if (handler instanceof IAdvancedSticky && handler.canHandle(world, position)) {
                if (handler.providesFinalValues()) {
                    return ((IAdvancedSticky) handler).getRequiredStickiness(world, position, side);
                } else {
                    set = true;
                    max = Math.max(max, ((IAdvancedSticky) handler).getRequiredStickiness(world, position, side));
                }
            }
        }
        if (set)
            return max;

        // If there's an ISticky in that world position, return what it would return.
        ISticky sticky = findWorldSticky(world, position);
        if (sticky != null && sticky instanceof IAdvancedSticky)
            return ((IAdvancedSticky) sticky).getRequiredStickiness(world, position, side);

        return 0;
    }

    public IFrame getFrameAt(World world, BlockPos position) {

        // Go through the providers. If there's a valid one, return it.
        for (IFrameProvider provider : frameProviders) {
            IFrame frame = provider.getFrameAt(world, position);
            if (frame != null)
                return frame;
        }

        // If there aren't frames in this block, as determined by the providers, fallback to the Block and TileEntity.
        Block block = world.getBlock(position.x, position.y, position.z);
        if (block instanceof IFrameBlock)
            return FramezApi.instance().wrapFrameBlock(world, position, (IFrameBlock) block);
        TileEntity te = world.getTileEntity(position.x, position.y, position.z);
        if (te != null && te instanceof IFrame)
            return (IFrame) te;
        return null;
    }

    private ISticky findWorldSticky(World world, BlockPos position) {

        Block block = world.getBlock(position.x, position.y, position.z);
        if (block instanceof ISticky)
            return (ISticky) block;
        TileEntity te = world.getTileEntity(position.x, position.y, position.z);
        if (te != null && te instanceof ISticky)
            return (ISticky) te;
        return null;
    }

}
