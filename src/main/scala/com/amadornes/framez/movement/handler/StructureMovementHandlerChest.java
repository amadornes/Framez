package com.amadornes.framez.movement.handler;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IStructureMovementHandler;
import com.amadornes.framez.api.movement.MovementIssue;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;

public class StructureMovementHandlerChest implements IStructureMovementHandler {

    @Override
    public void addIssues(BlockSet blocks, IMovement movement, Set<MovementIssue> issues) {

        Set<BlockPos> neighbors = new HashSet<BlockPos>();

        for (IMovingBlock b : blocks) {
            if (b.getBlock() == Blocks.chest || b.getBlock() == Blocks.trapped_chest) {

                int currentNeighbors = 0;
                for (int i = 2; i < 6; i++) {
                    BlockPos c = b.getPosition().copy().offset(i);
                    IMovingBlock bl = blocks.find(c.x, c.y, c.z);
                    if (bl != null && bl.getBlock() == b.getBlock())
                        currentNeighbors += 1;
                }

                BlockPos t = movement.transformPos(b.getPosition());
                for (int i = 2; i < 6; i++) {
                    BlockPos c = t.copy().offset(i);
                    if (c.equals(b.getPosition()))
                        continue;
                    Block bl = blocks.getWorld().getBlock(c.x, c.y, c.z);
                    if (bl == b.getBlock())
                        neighbors.add(c);
                }

                if (neighbors.size() + currentNeighbors > 1) {
                    for (BlockPos p : neighbors)
                        issues.add(MovementIssue.BLOCK.at(p).ofColor(FramezConfig.color_issue_chest).withInformation("Colliding chests!"));
                    issues.add(MovementIssue.BLOCK.at(b.getPosition()).ofColor(FramezConfig.color_issue_chest)
                            .withInformation("Colliding chests!"));
                }

                neighbors.clear();
            }
        }
    }
}
