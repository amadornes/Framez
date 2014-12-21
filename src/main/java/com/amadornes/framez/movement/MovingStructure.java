package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.world.WorldWrapperClient;
import com.amadornes.framez.world.WorldWrapperProvider;
import com.amadornes.framez.world.WorldWrapperServer;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;
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

        wrapper = WorldWrapperProvider.getWrapper(world);
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
    public World getWorldWrapperClient() {

        return wrapperClient;
    }

    public void tick(Phase phase) {

        if (!moved) {

            // Move entities
            if (phase == Phase.END)
                moveEntities();

            if (phase == Phase.START) {
                if (totalMoved == 0) {
                    startMoving();
                }

                getWorldWrapper().tick();

                // Update lighting
                {
                    List<BlockCoord> l = new ArrayList<BlockCoord>();
                    for (MovingBlock b : blocks) {
                        if (!l.contains(b))
                            l.add(b.getLocation());
                        BlockCoord d = b.getLocation().copy().add(getDirection().offsetX, getDirection().offsetY, getDirection().offsetZ);
                        if (!l.contains(d))
                            l.add(d);
                    }

                    for (BlockCoord b : l) {
                        getWorld().func_147451_t(b.x, b.y, b.z);
                    }

                    l.clear();
                }

                totalMoved += speed;

                if (totalMoved >= 1) {
                    finishMoving();
                }
            }
        }
    }

    public void startMoving() {

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

    public void finishMoving() {

        totalMoved = 1;
        moved = true;

        for (MovingBlock b : blocks) {
            b.removePlaceholder();
            b.place();
        }

        Random rnd = new Random();

        for (MovingBlock b : blocks) {
            if (!world.isRemote) {
                b.getBlock().updateTick(world, b.getLocation().x + getDirection().offsetX, b.getLocation().y + getDirection().offsetY,
                        b.getLocation().z + getDirection().offsetZ, rnd);
                b.getBlock().onBlockAdded(world, b.getLocation().x + getDirection().offsetX, b.getLocation().y + getDirection().offsetY,
                        b.getLocation().z + getDirection().offsetZ);
                world.markBlockForUpdate(b.getLocation().x + getDirection().offsetX, b.getLocation().y + getDirection().offsetY,
                        b.getLocation().z + getDirection().offsetZ);
                world.getChunkFromBlockCoords(b.getX(), b.getZ()).setChunkModified();
            } else {
                world.markBlockRangeForRenderUpdate(b.getLocation().x + getDirection().offsetX, b.getLocation().y + getDirection().offsetY,
                        b.getLocation().z + getDirection().offsetZ, b.getLocation().x + getDirection().offsetX, b.getLocation().y
                        + getDirection().offsetY, b.getLocation().z + getDirection().offsetZ);
            }
        }

        if (wrapper != null)
            wrapper.removeStructure(this);

        MovedBlockHandler.onStructureFinishMoving(this);
    }

    public MovingBlock getBlock(int x, int y, int z) {

        BlockCoord coords = new BlockCoord(x, y, z);

        for (MovingBlock b : new ArrayList<MovingBlock>(getBlocks()))
            if (b.getLocation() != null && b.getLocation().equals(coords))
                return b;

        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void moveEntities() {

        List<Entity> entities = new ArrayList<Entity>();
        for (MovingBlock b : blocks) {
            List aabbs = new ArrayList();

            try {
                b.getBlock().addCollisionBoxesToList(
                        getWorldWrapper(),
                        b.getLocation().x,
                        b.getLocation().y,
                        b.getLocation().z,
                        AxisAlignedBB.getBoundingBox(b.getLocation().x, b.getLocation().y, b.getLocation().z, b.getLocation().x + 1,
                                b.getLocation().y + 1, b.getLocation().z + 1), aabbs, null);
            } catch (Exception ex) {
                aabbs.add(AxisAlignedBB.getBoundingBox(b.getX(), b.getY(), b.getZ(), b.getX() + 1, b.getY() + 1, b.getZ() + 1));
            }
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
                    if (!entities.contains(o2)) {
                        entities.add((Entity) o2);
                    }
                }
            }
        }

        for (Entity e : entities) {
            if (e instanceof EntityPlayer)
                if (e.isSneaking())
                    continue;

            if (direction.offsetY >= 0) {
                try {
                    moveEntity(e);
                } catch (Exception ex) {
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void moveEntity(Entity entity) {

        double movedX = speed * direction.offsetX;
        double movedY = (speed * direction.offsetY) + 0.001;
        double movedZ = speed * direction.offsetZ;

        if (entity.noClip) {
            entity.boundingBox.offset(movedX, movedY, movedZ);
            entity.posX = (entity.boundingBox.minX + entity.boundingBox.maxX) / 2.0D;
            entity.posY = entity.boundingBox.minY + entity.yOffset - entity.ySize;
            entity.posZ = (entity.boundingBox.minZ + entity.boundingBox.maxZ) / 2.0D;
        } else {
            entity.worldObj.theProfiler.startSection("move");
            entity.ySize *= 0.4F;
            double d3 = entity.posX;
            double d4 = entity.posY;
            double d5 = entity.posZ;

            double d6 = movedX;
            double d7 = movedY;
            double d8 = movedZ;
            AxisAlignedBB axisalignedbb = entity.boundingBox.copy();
            boolean flag = entity.onGround && entity.isSneaking() && entity instanceof EntityPlayer;

            if (flag) {
                double d9;

                for (d9 = 0.05D; movedX != 0.0D
                        && entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.getOffsetBoundingBox(movedX, -1.0D, 0.0D))
                        .isEmpty(); d6 = movedX) {
                    if (movedX < d9 && movedX >= -d9) {
                        movedX = 0.0D;
                    } else if (movedX > 0.0D) {
                        movedX -= d9;
                    } else {
                        movedX += d9;
                    }
                }

                for (; movedZ != 0.0D
                        && entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.getOffsetBoundingBox(0.0D, -1.0D, movedZ))
                        .isEmpty(); d8 = movedZ) {
                    if (movedZ < d9 && movedZ >= -d9) {
                        movedZ = 0.0D;
                    } else if (movedZ > 0.0D) {
                        movedZ -= d9;
                    } else {
                        movedZ += d9;
                    }
                }

                while (movedX != 0.0D
                        && movedZ != 0.0D
                        && entity.worldObj
                        .getCollidingBoundingBoxes(entity, entity.boundingBox.getOffsetBoundingBox(movedX, -1.0D, movedZ))
                        .isEmpty()) {
                    if (movedX < d9 && movedX >= -d9) {
                        movedX = 0.0D;
                    } else if (movedX > 0.0D) {
                        movedX -= d9;
                    } else {
                        movedX += d9;
                    }

                    if (movedZ < d9 && movedZ >= -d9) {
                        movedZ = 0.0D;
                    } else if (movedZ > 0.0D) {
                        movedZ -= d9;
                    } else {
                        movedZ += d9;
                    }

                    d6 = movedX;
                    d8 = movedZ;
                }
            }

            List list = entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.addCoord(movedX, movedY, movedZ));

            for (int i = 0; i < list.size(); ++i) {
                movedY = ((AxisAlignedBB) list.get(i)).calculateYOffset(entity.boundingBox, movedY);
            }

            entity.boundingBox.offset(0.0D, movedY, 0.0D);

            if (!entity.field_70135_K && d7 != movedY) {
                movedZ = 0.0D;
                movedY = 0.0D;
                movedX = 0.0D;
            }

            boolean flag1 = entity.onGround || d7 != movedY && d7 < 0.0D;
            int j;

            for (j = 0; j < list.size(); ++j) {
                movedX = ((AxisAlignedBB) list.get(j)).calculateXOffset(entity.boundingBox, movedX);
            }

            entity.boundingBox.offset(movedX, 0.0D, 0.0D);

            if (!entity.field_70135_K && d6 != movedX) {
                movedZ = 0.0D;
                movedY = 0.0D;
                movedX = 0.0D;
            }

            for (j = 0; j < list.size(); ++j) {
                movedZ = ((AxisAlignedBB) list.get(j)).calculateZOffset(entity.boundingBox, movedZ);
            }

            entity.boundingBox.offset(0.0D, 0.0D, movedZ);

            if (!entity.field_70135_K && d8 != movedZ) {
                movedZ = 0.0D;
                movedY = 0.0D;
                movedX = 0.0D;
            }

            double d10;
            double d11;
            int k;
            double d12;

            if (entity.stepHeight > 0.0F && flag1 && (flag || entity.ySize < 0.05F) && (d6 != movedX || d8 != movedZ)) {
                d12 = movedX;
                d10 = movedY;
                d11 = movedZ;
                movedX = d6;
                movedY = entity.stepHeight;
                movedZ = d8;
                AxisAlignedBB axisalignedbb1 = entity.boundingBox.copy();
                entity.boundingBox.setBB(axisalignedbb);
                list = entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.addCoord(d6, movedY, d8));

                for (k = 0; k < list.size(); ++k) {
                    movedY = ((AxisAlignedBB) list.get(k)).calculateYOffset(entity.boundingBox, movedY);
                }

                entity.boundingBox.offset(0.0D, movedY, 0.0D);

                if (!entity.field_70135_K && d7 != movedY) {
                    movedZ = 0.0D;
                    movedY = 0.0D;
                    movedX = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    movedX = ((AxisAlignedBB) list.get(k)).calculateXOffset(entity.boundingBox, movedX);
                }

                entity.boundingBox.offset(movedX, 0.0D, 0.0D);

                if (!entity.field_70135_K && d6 != movedX) {
                    movedZ = 0.0D;
                    movedY = 0.0D;
                    movedX = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    movedZ = ((AxisAlignedBB) list.get(k)).calculateZOffset(entity.boundingBox, movedZ);
                }

                entity.boundingBox.offset(0.0D, 0.0D, movedZ);

                if (!entity.field_70135_K && d8 != movedZ) {
                    movedZ = 0.0D;
                    movedY = 0.0D;
                    movedX = 0.0D;
                }

                if (!entity.field_70135_K && d7 != movedY) {
                    movedZ = 0.0D;
                    movedY = 0.0D;
                    movedX = 0.0D;
                } else {
                    movedY = (-entity.stepHeight);

                    for (k = 0; k < list.size(); ++k) {
                        movedY = ((AxisAlignedBB) list.get(k)).calculateYOffset(entity.boundingBox, movedY);
                    }

                    entity.boundingBox.offset(0.0D, movedY, 0.0D);
                }

                if (d12 * d12 + d11 * d11 >= movedX * movedX + movedZ * movedZ) {
                    movedX = d12;
                    movedY = d10;
                    movedZ = d11;
                    entity.boundingBox.setBB(axisalignedbb1);
                }
            }

            entity.worldObj.theProfiler.endSection();
            entity.worldObj.theProfiler.startSection("rest");
            entity.posX = (entity.boundingBox.minX + entity.boundingBox.maxX) / 2.0D;
            entity.posY = entity.boundingBox.minY + entity.yOffset - entity.ySize;
            entity.posZ = (entity.boundingBox.minZ + entity.boundingBox.maxZ) / 2.0D;
            entity.isCollidedHorizontally = d6 != movedX || d8 != movedZ;
            entity.isCollidedVertically = d7 != movedY;
            entity.onGround = true;
            entity.isCollided = entity.isCollidedHorizontally || entity.isCollidedVertically;
            entity.fallDistance = 0;

            d12 = entity.posX - d3;
            d10 = entity.posY - d4;
            d11 = entity.posZ - d5;

            entity.prevPosX -= d12;
            entity.prevPosY -= d10;
            entity.prevPosZ -= d11;
        }
    }
}
