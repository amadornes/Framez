package com.amadornes.framez.util;

import java.util.Comparator;

import com.amadornes.framez.api.movement.MovementIssue;

public class ComparatorMovementIssue implements Comparator<MovementIssue> {

    @Override
    public int compare(MovementIssue a, MovementIssue b) {

        return Integer.compare(a.getPosition().hashCode(), b.getPosition().hashCode());
    }

}
