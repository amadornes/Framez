package com.amadornes.framez.tile;

import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.movement.MovementSlide;
import com.amadornes.trajectory.api.BlockSet;

public class TileMotorSlider extends TileMotor {

    private MovementSlide movement;

    @Override
    public MovementSlide getMovement() {

        int face = getFace();
        if (movement == null)
            return movement = new MovementSlide(face == 0 || face == 4 || face == 5 ? 3 : (face == 1 ? 2 : (face == 3 ? 1 : 0)), 1);

        boolean hasBouncyUpgrade = false;
        for (int i = 0; i < 7; i++) {
            IMotorUpgradeData d = getUpgrades()[i];
            if (d != null && d.getUpgrade().getType().equals("bouncy")) {
                hasBouncyUpgrade = true;
                break;
            }
        }
        if (hasBouncyUpgrade && redstoneInput)
            return new MovementSlide(movement.getDirection() ^ 1, 1);
        return movement;
    }

    @Override
    public IMovement getMovement(BlockSet blocks) {

        // boolean hasBouncyUpgrade = false;
        // for (int i = 0; i < 7; i++) {
        // IMotorUpgradeData d = getUpgrades()[i];
        // if (d != null && d.getUpgrade().getType().equals("bouncy")) {
        // hasBouncyUpgrade = true;
        // break;
        // }
        // }
        // if (hasBouncyUpgrade && !redstoneInput)
        // return new MovementSlide(getMovement().getDirection() ^ 1, 1);
        return super.getMovement(blocks);
    }

}
