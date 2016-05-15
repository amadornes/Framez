package com.amadornes.framez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.amadornes.framez.api.compat.ICompatRegistry;
import com.amadornes.framez.api.compat.IFMPCompatRegistry;
import com.amadornes.framez.api.compat.IFramePlacementHandler;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.util.ComparatorPlacementPriority;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;

public class CompatRegistryImpl implements ICompatRegistry {

    public static CompatRegistryImpl instance = new CompatRegistryImpl();

    public static final List<IFramePlacementHandler> framePlacementHandlers = new ArrayList<IFramePlacementHandler>();
    // public static final List<IGhostFramePlacementHandler> ghostFramePlacementHandlers = new ArrayList<IGhostFramePlacementHandler>();

    public static IFMPCompatRegistry compat_fmp = new IFMPCompatRegistry() {

        @Override
        public void registerIgnoredCover(String material) {

        }

        @Override
        public boolean hasCover(World world, int x, int y, int z, int side) {

            return false;
        }

        @Override
        public boolean hasCover(World world, int x, int y, int z, int side, String material) {

            return false;
        }
    };

    @Override
    public IFMPCompatRegistry fmp() {

        return compat_fmp;
    }

    @Override
    public IFramezWrench getModdedWrench(ItemStack stack) {

        return null;
    }

    @Override
    public boolean placeFrame(World world, BlockPos position, ItemStack stack) {

        return FramezUtils.placeFrame(world, position, stack);
    }

    @Override
    public void registerFramePlacementHandler(IFramePlacementHandler handler) {

        if (handler == null)
            throw new NullPointerException("Attempted to register a null placement handler.");
        if (framePlacementHandlers.contains(handler))
            throw new IllegalStateException("Attemted to register a placement handler that was already registered.");

        framePlacementHandlers.add(handler);

        Collections.sort(framePlacementHandlers, new ComparatorPlacementPriority());
    }

    // @Override
    // public void registerGhostFramePlacementHandler(IGhostFramePlacementHandler handler) {
    //
    // if (handler == null)
    // throw new NullPointerException("Attempted to register a null ghost placement handler.");
    // if (ghostFramePlacementHandlers.contains(handler))
    // throw new IllegalStateException("Attemted to register a ghost placement handler that was already registered.");
    //
    // ghostFramePlacementHandlers.add(handler);
    // }

}