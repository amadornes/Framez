package com.amadornes.framez.api.movement;

import java.util.ArrayList;
import java.util.List;

public enum MotorSetting {

    REDSTONE_PULSE, REDSTONE_INVERTED;

    static {
    }

    public final List<MotorSetting> related = new ArrayList<MotorSetting>();

    @SuppressWarnings("unused")
    private void relate(MotorSetting setting) {

        related.add(setting);
    }

}
