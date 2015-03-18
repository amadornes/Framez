package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import uk.co.qmunity.lib.vec.Vec3d;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.api.movement.IMovingStructure;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.PacketBlockSync;
import com.amadornes.framez.network.PacketStartMoving;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.world.FakeWorld;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class MovingStructure implements IMovingStructure {

    private TileMotor motor;
    private IMovement movement;

    private List<IMovingBlock> blocks = new ArrayList<IMovingBlock>();

    private double progress = 0, speed = 1;

    public MovingStructure(TileMotor motor, IMovement movement, double speed) {

        this.motor = motor;
        this.movement = movement;
        this.speed = speed;
    }

    public MovingStructure(TileMotor motor, IMovement movement, double speed, List<MovingBlock> blocks) {

        this(motor, movement, speed);

        this.blocks.addAll(blocks);
        for (MovingBlock b : blocks)
            b.setStructure(this);
    }

    @Override
    public World getWorld() {

        return motor.getWorld();
    }

    @Override
    public List<IMovingBlock> getBlocks() {

        return blocks;
    }

    public MovingBlock getBlock(int x, int y, int z) {

        for (IMovingBlock b : getBlocks())
            if (b.getX() == x && b.getY() == y && b.getZ() == z)
                return (MovingBlock) b;

        return null;
    }

    @Override
    public IMovement getMovement() {

        return movement;
    }

    public void addBlock(MovingBlock block) {

        blocks.add(block);
        block.setStructure(this);
    }

    @Override
    public double getProgress() {

        return progress;// Math.min(progress, 1);
    }

    @Override
    public double getInterpolatedProgress(double frame) {

        return Math.max(Math.min(((progress / getSpeed()) * (1 / (1 / getSpeed() - 1.75))) + (getSpeed() * frame) - getSpeed(), 1), 0);
    }

    public void setProgress(double progress) {

        this.progress = progress;
    }

    public double getSpeed() {

        return speed;
    }

    private void startMoving() {

        for (IMovingBlock bl : blocks)
            ((MovingBlock) bl).remove();

        for (IMovingBlock bl : blocks)
            ((MovingBlock) bl).notifyRemoval();
    }

    private void finishMoving() {

        for (IMovingBlock bl : blocks)
            ((MovingBlock) bl).place();

        for (IMovingBlock bl : blocks)
            ((MovingBlock) bl).notifyPlacement();
    }

    public void tick(Phase phase) {

        FakeWorld.getFakeWorld(this);

        if (progress >= 1)
            return;

        if (phase == Phase.END) {
            if (progress == 0) {
                if (!getWorld().isRemote)
                    NetworkHandler.instance().sendToAllAround(new PacketStartMoving(motor, this), getWorld(), 128);

                startMoving();
                if (!getWorld().isRemote)
                    NetworkHandler.instance().sendToAllAround(new PacketBlockSync(motor, this), getWorld(), 128);
            }

            progress += speed;
            moveEntities();

            if (progress >= 1)
                finishMoving();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void moveEntities() {

        IMovement m = getMovement();

        List<Entity> entities = new ArrayList<Entity>();
        for (IMovingBlock mb : getBlocks()) {
            MovingBlock b = (MovingBlock) mb;
            List aabbs = new ArrayList();

            try {
                b.getBlock().addCollisionBoxesToList(FakeWorld.getFakeWorld(this), b.getX(), b.getY(), b.getZ(),
                        AxisAlignedBB.getBoundingBox(b.getX(), b.getY(), b.getZ(), b.getX() + 1, b.getY() + 1, b.getZ() + 1), aabbs, null);
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
                    Vec3d min = m.transform(new Vec3d(aabb.minX, aabb.minY, aabb.minZ), getProgress());
                    Vec3d max = m.transform(new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ), getProgress());

                    aabb.minX = min.getX();
                    aabb.minY = min.getY();
                    aabb.minZ = min.getZ();
                    aabb.maxX = max.getX();
                    aabb.maxY = max.getY();
                    aabb.maxZ = max.getZ();
                }

                aabb.maxY += 0.25;

                aabb.minX -= 0.25;
                aabb.minZ -= 0.25;
                aabb.maxX += 0.25;
                aabb.maxZ += 0.25;

                for (Object o2 : getWorld().getEntitiesWithinAABB(Entity.class, aabb)) {
                    if (!entities.contains(o2)) {
                        entities.add((Entity) o2);
                    }
                }
            }
        }

        for (Entity e : entities) {
            Vec3d pos = m.transform(new Vec3d(e.posX, e.posY, e.posZ), getProgress());

            if (pos.getY() >= e.posY) {
                try {
                    moveEntity(e);
                } catch (Exception ex) {
                }
            }
        }
    }

    private void moveEntity(Entity entity) {

        Vec3d pos = getMovement().transform(new Vec3d(entity.posX, entity.posY, entity.posZ), 1).clone()
                .sub(new Vec3d(entity.posX, entity.posY, entity.posZ)).mul(1 / (1 / getSpeed() - 1));
        pos.setY(Math.max(pos.getY(), 0));

        entity.motionY = Math.max(entity.motionY, 0) + pos.getY();

        if (!(entity instanceof EntityPlayer) || !(entity.isSneaking()))
            entity.moveEntity(pos.getX(), 0, pos.getZ());

        entity.onGround = true;
        entity.fallDistance = 0;
    }

    @Override
    public String toString() {

        return "MovingStructure [world=" + getWorld() + ", blocks=" + blocks + ", progress=" + progress + "]";
    }

    public TileMotor getMotor() {

        return motor;
    }

}
