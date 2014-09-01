package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.api.IDisposable;

public class MovingStructure implements IDisposable {

    private World world;
    private ForgeDirection direction;
    private List<MovingBlock> blocks = new ArrayList<MovingBlock>();

    private double distanceMoved;

    private boolean moved = false;
    private double totalMoved = 0;

    private boolean valid = true;

    public MovingStructure(World world, ForgeDirection direction, double distanceMoved) {

        this.world = world;
        this.direction = direction;
        this.distanceMoved = distanceMoved;
    }

    public void addBlocks(List<BlockCoord> blocks) {

        for (BlockCoord b : blocks)
            addBlock(b);
    }

    public void addBlock(int x, int y, int z) {

        blocks.add(new MovingBlock(x, y, z, world, direction));
    }

    public void addBlock(BlockCoord coords) {

        blocks.add(new MovingBlock(coords, world, direction));
    }

    public void tick() {

        if (!moved) {
            if (totalMoved == 0)
                for (MovingBlock b : blocks) {
                    b.storeData();
                    b.remove();
                }

            for (MovingBlock b : blocks) {
                b.move(distanceMoved);
            }

            totalMoved += distanceMoved;

            if (totalMoved >= 1) {
                moved = true;

                for (MovingBlock b : blocks)
                    b.place();

                dispose();
            }
        }
    }

    @Override
    public void dispose() {

        for (MovingBlock b : blocks)
            b.dispose();
        blocks.clear();
        blocks = null;

        world = null;

        valid = false;
    }

    @Override
    public boolean isValid() {

        return valid;
    }

}
