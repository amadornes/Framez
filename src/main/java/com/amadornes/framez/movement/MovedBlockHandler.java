package com.amadornes.framez.movement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.tile.TileMotor;

public class MovedBlockHandler {

    private static List<Entry<World, BlockCoord>> blocks = new ArrayList<Entry<World, BlockCoord>>();

    public static boolean canMoveBlockAt(World world, int x, int y, int z) {

        return canMoveBlockAt(world, new BlockCoord(x, y, z));
    }

    public static boolean canMoveBlockAt(World world, BlockCoord location) {

        for (Entry<World, BlockCoord> e : blocks)
            if (e.getKey() == world && e.getValue().equals(location))
                return false;

        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void onStructureFinishMoving(MovingStructure structure) {

        ForgeDirection dir = structure.getDirection();
        int dX = dir.offsetX;
        int dY = dir.offsetY;
        int dZ = dir.offsetZ;

        for (MovingBlock b : structure.getBlocks())
            blocks.add(new AbstractMap.SimpleEntry(structure.getWorld(), b.getLocation().copy().add(dX, dY, dZ)));
    }

    public static void worldTick(World world) {

        Iterator<Entry<World, BlockCoord>> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Entry<World, BlockCoord> e = iterator.next();
            if (e.getKey() == world)
                iterator.remove();
        }
    }

    public boolean canMotorRun(TileMotor motor) {

        return canMoveBlockAt(motor.getWorldObj(), motor.xCoord, motor.yCoord, motor.zCoord);
    }

}
