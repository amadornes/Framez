package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.world.WorldWrapper;
import com.amadornes.framez.world.WorldWrapperClient;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MovingStructure {

    private World world;
    private ForgeDirection direction;
    private List<MovingBlock> blocks = new ArrayList<MovingBlock>();

    private double distanceMoved;

    private boolean moved = false;
    private double totalMoved = 0;

    private WorldWrapper wrapper;
    @SideOnly(Side.CLIENT)
    private WorldWrapperClient wrapperClient;

    public MovingStructure(World world, ForgeDirection direction, double distanceMoved) {

        this.world = world;
        this.direction = direction;
        this.distanceMoved = distanceMoved;

        wrapper = new WorldWrapper(this);

        if (world.isRemote)
            initClient();
    }

    @SideOnly(Side.CLIENT)
    private void initClient() {

        wrapperClient = new WorldWrapperClient(this);
    }

    public void addBlocks(List<BlockCoord> blocks) {

        for (BlockCoord b : blocks)
            addBlock(b);
    }

    public void addBlock(int x, int y, int z) {

        blocks.add(new MovingBlock(x, y, z, world, this));
    }

    public void addBlock(BlockCoord coords) {

        blocks.add(new MovingBlock(coords, world, this));
    }

    public List<MovingBlock> getBlocks() {

        return blocks;
    }

    public ForgeDirection getDirection() {

        return direction;
    }

    public double getMoved() {

        return totalMoved;
    }

    public double getMoved(float partialTick) {

        return totalMoved + (getSpeed() * partialTick);
    }

    public double getSpeed() {

        return distanceMoved;
    }

    public World getWorld() {

        return world;
    }

    public WorldWrapper getWorldWrapper() {

        return wrapper;
    }

    @SideOnly(Side.CLIENT)
    public WorldWrapperClient getWorldWrapperClient() {

        return wrapperClient;
    }

    public void tick() {

        if (!moved) {
            if (totalMoved == 0) {
                for (MovingBlock b : blocks) {
                    b.storeData();
                    b.remove();
                    b.placePlaceholder();
                }
                for (MovingBlock b : blocks) {
                    world.notifyBlocksOfNeighborChange(b.getLocation().x, b.getLocation().y, b.getLocation().z, b.getBlock());
                    world.markBlockRangeForRenderUpdate(b.getLocation().x, b.getLocation().y, b.getLocation().z, b.getLocation().x,
                            b.getLocation().y, b.getLocation().z);
                    world.markBlockForUpdate(b.getLocation().x, b.getLocation().y, b.getLocation().z);
                }
            }

            totalMoved += distanceMoved;

            if (totalMoved >= 1) {
                moved = true;

                for (MovingBlock b : blocks) {
                    b.removePlaceholder();
                    b.place();
                }
                for (MovingBlock b : blocks) {
                    world.notifyBlocksOfNeighborChange(b.getLocation().x + getDirection().offsetX, b.getLocation().y + getDirection().offsetY,
                            b.getLocation().z + getDirection().offsetZ, b.getBlock());
                    world.markBlockRangeForRenderUpdate(b.getLocation().x + getDirection().offsetX, b.getLocation().y + getDirection().offsetY,
                            b.getLocation().z + getDirection().offsetZ, b.getLocation().x + getDirection().offsetX, b.getLocation().y
                                    + getDirection().offsetY, b.getLocation().z + getDirection().offsetZ);
                    world.markBlockForUpdate(b.getLocation().x + getDirection().offsetX, b.getLocation().y + getDirection().offsetY,
                            b.getLocation().z + getDirection().offsetZ);
                }
            }
        }
    }

}
