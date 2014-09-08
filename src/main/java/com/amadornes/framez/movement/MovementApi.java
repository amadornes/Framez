package com.amadornes.framez.movement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.HandlingPriority;
import com.amadornes.framez.api.movement.HandlingPriority.Priority;
import com.amadornes.framez.api.movement.IMovementApi;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.movement.handler.BlockHandler;
import com.amadornes.framez.movement.handler.FluidHandler;
import com.amadornes.framez.movement.handler.UnbreakableHandler;
import com.amadornes.framez.util.Utils;

public class MovementApi implements IMovementApi {

    public static final MovementApi INST = new MovementApi();

    // Init some basic blocks
    static {
        INST.registerMovementHandler(new UnbreakableHandler());
        INST.registerMovementHandler(new FluidHandler());
    }

    private List<IMovementHandler> handlers = new ArrayList<IMovementHandler>();

    private MovementApi() {

    }

    @Override
    public void registerMovementHandler(IMovementHandler handler) {

        if (handler != null)
            handlers.add(handler);
    }

    @Override
    public IMovementHandler[] getRegisteredHandlers() {

        return handlers.toArray(new IMovementHandler[0]);
    }

    @Override
    public void setBlockMovementType(Block block, BlockMovementType type) {

        registerMovementHandler(new BlockHandler(block, type));
    }

    @Override
    public void setBlockMovementType(Block block, int metadata, BlockMovementType type) {

        registerMovementHandler(new BlockHandler(block, metadata, type));
    }

    public BlockMovementType getMovementType(World world, int x, int y, int z) {

        for (IMovementHandler h : getSortedHandlersForType()) {
            BlockMovementType t = h.getMovementType(world, x, y, z);
            if (t != null)
                return t;
        }
        return BlockMovementType.MOVABLE;
    }

    public boolean handlePlacement(IMovingBlock block, ForgeDirection d) {

        for (IMovementHandler h : getSortedHandlersForMovement(true))
            if (h.handleFinishMoving(block))
                return true;

        return false;
    }

    public boolean handleRemoval(IMovingBlock block, ForgeDirection d) {

        for (IMovementHandler h : getSortedHandlersForMovement(false))
            if (h.handleStartMoving(block))
                return true;

        return false;
    }

    private List<IMovementHandler> getSortedHandlersForMovement(boolean placing) {

        Map<IMovementHandler, HandlingPriority.Priority> handlers = new HashMap<IMovementHandler, HandlingPriority.Priority>();

        for (IMovementHandler h : getRegisteredHandlers())
            handlers.put(h, getPriorityForMovement(h, placing));

        List<IMovementHandler> sorted = Utils.sortByPriority(handlers);

        handlers.clear();
        handlers = null;

        return sorted;
    }

    private List<IMovementHandler> getSortedHandlersForType() {

        Map<IMovementHandler, HandlingPriority.Priority> handlers = new HashMap<IMovementHandler, HandlingPriority.Priority>();

        for (IMovementHandler h : getRegisteredHandlers())
            handlers.put(h, getPriorityForType(h));

        List<IMovementHandler> sorted = Utils.sortByPriority(handlers);

        handlers.clear();
        handlers = null;

        return sorted;
    }

    private HandlingPriority.Priority getPriorityForMovement(IMovementHandler handler, boolean placing) {

        try {
            Method m = null;
            if (placing) {
                m = handler.getClass().getDeclaredMethod("handleFinishMoving", IMovingBlock.class);
            } else {
                m = handler.getClass().getDeclaredMethod("handleStartMoving", IMovingBlock.class);
            }
            if (m != null)
                return m.getAnnotation(HandlingPriority.class).value();
        } catch (Exception ex) {
        }
        return Priority.LOW;
    }

    private HandlingPriority.Priority getPriorityForType(IMovementHandler handler) {

        try {
            Method m = handler.getClass().getDeclaredMethod("getMovementType", World.class, Integer.class, Integer.class, Integer.class);
            if (m != null)
                return m.getAnnotation(HandlingPriority.class).value();
        } catch (Exception ex) {
        }
        return Priority.LOW;
    }
}
