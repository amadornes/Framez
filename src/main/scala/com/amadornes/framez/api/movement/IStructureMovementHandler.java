package com.amadornes.framez.api.movement;

import java.util.Set;

import com.amadornes.trajectory.api.BlockSet;

public interface IStructureMovementHandler {

    public void addIssues(BlockSet blocks, IMovement movement, Set<MovementIssue> issues);

}
