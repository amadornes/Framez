package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrameMovementRegistry;
import com.amadornes.framez.api.movement.IMovable;
import com.amadornes.framez.api.movement.IMovementDataProvider;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.api.movement.IStickyProvider;
import com.amadornes.framez.util.SorterPriority;

public class FrameMovementRegistry implements IFrameMovementRegistry {

    private static final FrameMovementRegistry instance = new FrameMovementRegistry();

    public static FrameMovementRegistry instance() {

        return instance;
    }

    private List<IMovementDataProvider> dataProviders = new ArrayList<IMovementDataProvider>();
    private List<IMovementHandler> movementHandlers = new ArrayList<IMovementHandler>();
    private List<IStickyProvider> stickyProviders = new ArrayList<IStickyProvider>();

    private boolean canRegisterMovementHandlers = true;
    private boolean canRegisterStickyProviders = true;

    @Override
    public void registerMovementDataProvider(IMovementDataProvider provider) {

        if (provider == null)
            return;
        if (dataProviders.contains(provider))
            return;

        dataProviders.add(provider);
    }

    @Override
    public void registerMovementHandler(IMovementHandler handler) {

        if (!canRegisterMovementHandlers)
            throw new RuntimeException("All the movement handlers have already been sorted and setup. Register yours before!");

        if (handler == null)
            return;
        if (movementHandlers.contains(handler))
            return;

        movementHandlers.add(handler);
    }

    @Override
    public void registerStickyProvider(IStickyProvider provider) {

        if (!canRegisterStickyProviders)
            throw new RuntimeException("All the movement handlers have already been sorted and setup. Register yours before!");

        if (provider == null)
            return;
        if (stickyProviders.contains(provider))
            return;

        stickyProviders.add(provider);
    }

    private void sortMovementHandlersAndDisableRegistration() {

        if (!canRegisterMovementHandlers)
            return;

        canRegisterMovementHandlers = false;
        Collections.sort(movementHandlers, new SorterPriority.SorterPriorityInstance());
    }

    private void sortStickyProvidersAndDisableRegistration() {

        if (!canRegisterStickyProviders)
            return;

        canRegisterStickyProviders = false;
        Collections.sort(stickyProviders, new SorterPriority.SorterPriorityInstance());
    }

    @Override
    public List<IMovable> findMovables(World world, int x, int y, int z) {

        if (world == null)
            return null;

        sortMovementHandlersAndDisableRegistration();

        List<IMovable> l = new ArrayList<IMovable>();

        Block b = world.getBlock(x, y, z);
        if (b instanceof IMovable)
            l.add((IMovable) b);

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IMovable)
            l.add((IMovable) te);

        for (IMovementHandler h : movementHandlers)
            if (h.canHandle(world, x, y, z))
                l.add(h);

        return l;
    }

    @Override
    public List<ISticky> findStickies(World world, int x, int y, int z) {

        if (world == null)
            return null;

        sortStickyProvidersAndDisableRegistration();

        List<ISticky> l = new ArrayList<ISticky>();

        Block b = world.getBlock(x, y, z);
        if (b instanceof ISticky)
            l.add((ISticky) b);

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof ISticky)
            l.add((ISticky) te);

        for (IStickyProvider p : stickyProviders) {
            ISticky sticky = p.getStickyAt(world, x, y, z);
            if (sticky != null)
                l.add(sticky);
        }

        return l;
    }

    @Override
    public List<IFrame> findFrames(World world, int x, int y, int z) {

        if (world == null)
            return null;

        List<IFrame> l = new ArrayList<IFrame>();

        Block b = world.getBlock(x, y, z);
        if (b instanceof IFrame)
            l.add((IFrame) b);

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IFrame)
            l.add((IFrame) te);

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp != null)
            for (TMultiPart p : tmp.jPartList())
                if (p instanceof IFrame)
                    l.add((IFrame) p);// FIXME actual multipart handling

        return l;
    }

    public NBTTagCompound writeInfo(IMovingBlock block) {

        NBTTagCompound tag = new NBTTagCompound();

        if (block == null)
            return tag;

        NBTTagList l = new NBTTagList();

        for (IMovementDataProvider p : dataProviders) {
            if (p.canHandle(block)) {
                NBTTagCompound t = new NBTTagCompound();
                p.writeMovementInfo(block, t);
                t.setString("___id", p.getID());
                l.appendTag(t);
            }
        }

        tag.setTag("info", l);

        return tag;
    }

    public void readInfo(IMovingBlock block, NBTTagCompound tag) {

        if (!tag.hasKey("info"))
            return;

        NBTTagList l = tag.getTagList("info", new NBTTagCompound().getId());

        for (int i = 0; i < l.tagCount(); i++) {
            NBTTagCompound t = l.getCompoundTagAt(i);
            for (IMovementDataProvider p : dataProviders) {
                if (p.getID().equals(t.getString("___id"))) {
                    p.readMovementInfo(block, t);
                    break;
                }
            }
        }
    }
}
