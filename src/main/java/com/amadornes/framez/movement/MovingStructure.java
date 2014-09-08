package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.world.WorldWrapperClient;
import com.amadornes.framez.world.WorldWrapperServer;
import com.amadornes.framez.world.WorldWrapperServerProvider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MovingStructure {

    private World world;
    private ForgeDirection direction;
    private List<MovingBlock> blocks = new ArrayList<MovingBlock>();

    private double speed;

    private boolean moved = false;
    private double totalMoved = 0;

    private WorldWrapperServer wrapper;
    @SideOnly(Side.CLIENT)
    private WorldWrapperClient wrapperClient;

    public MovingStructure(World world, ForgeDirection direction, double speed) {

        this.world = world;
        this.direction = direction;
        this.speed = speed;

        if (world.isRemote)
            initClient();
        else
            initServer();
    }

    private void initServer() {

        wrapper = WorldWrapperServerProvider.getWrapper(world);
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

        return totalMoved - (getSpeed() * partialTick);
    }

    public double getSpeed() {

        return speed;
    }

    public World getWorld() {

        return world;
    }

    public World getWorldWrapper() {

        if (wrapper == null)
            return getWorldWrapperClient();

        return wrapper;
    }

    @SideOnly(Side.CLIENT)
    public WorldClient getWorldWrapperClient() {

        return wrapperClient;
    }

    public void tick() {

        if (!moved) {
            if (totalMoved == 0) {
                if (wrapper != null)
                    wrapper.addStructure(this);
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

            // Move entities
            moveEntities();

            getWorldWrapper().tick();

            for (MovingBlock b : blocks)
                getWorld().func_147451_t(b.getLocation().x, b.getLocation().y, b.getLocation().z);

            totalMoved += speed;

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
                if (wrapper != null)
                    wrapper.removeStructure(this);
            }
        }
    }

    public MovingBlock getBlock(int x, int y, int z) {

        BlockCoord coords = new BlockCoord(x, y, z);

        for (MovingBlock b : new ArrayList<MovingBlock>(getBlocks()))
            if (b.getLocation() != null && b.getLocation().equals(coords))
                return b;

        return null;
    }

    @SuppressWarnings("rawtypes")
    private void moveEntities() {

        List<Entity> entities = new ArrayList<Entity>();
        for (MovingBlock b : blocks) {
            List aabbs = new ArrayList();

            b.getBlock().addCollisionBoxesToList(
                    getWorldWrapper(),
                    b.getLocation().x,
                    b.getLocation().y,
                    b.getLocation().z,
                    AxisAlignedBB.getBoundingBox(b.getLocation().x, b.getLocation().y, b.getLocation().z, b.getLocation().x + 1,
                            b.getLocation().y + 1, b.getLocation().z + 1), aabbs, null);
            for (Object o : aabbs) {
                AxisAlignedBB aabb = ((AxisAlignedBB) o);

                if (aabb == null)
                    continue;
                aabb = aabb.copy();

                // Translate
                {
                    double x = direction.offsetX * getMoved();
                    double y = direction.offsetY * getMoved();
                    double z = direction.offsetZ * getMoved();

                    aabb.minX += x;
                    aabb.maxX += x;
                    aabb.minY += y;
                    aabb.maxY += y;
                    aabb.minZ += z;
                    aabb.maxZ += z;
                }

                aabb.maxY += 0.25;

                aabb.minX -= direction.offsetX < 0 ? 0.25 : 0;
                aabb.minZ -= direction.offsetZ < 0 ? 0.25 : 0;
                aabb.maxX += direction.offsetX > 0 ? 0.25 : 0;
                aabb.maxZ += direction.offsetZ > 0 ? 0.25 : 0;

                for (Object o2 : world.getEntitiesWithinAABB(Entity.class, aabb)) {
                    if (!entities.contains(o2))
                        entities.add((Entity) o2);
                }
            }
        }

        for (Entity e : entities) {
            e.motionX += direction.offsetX * (speed / 2);
            e.motionZ += direction.offsetZ * (speed / 2);
            if (direction.offsetY > 0) {
                e.motionY += (speed * 2) - 0.000125 + (totalMoved == 0 ? 0.1 : 0);
                e.velocityChanged = true;
                e.onGround = true;
                e.fallDistance = 0;
            }
        }
    }
}
