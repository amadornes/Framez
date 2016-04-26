package com.amadornes.framez.api.movement;

import java.util.Map;
import java.util.Set;

import net.minecraft.util.BlockPos;

public interface IMovingStructure {

    public Map<? extends IMovingBlock, BlockPos> getBlocks();

    public Set<MovementIssue> getMovementIssues();

}
