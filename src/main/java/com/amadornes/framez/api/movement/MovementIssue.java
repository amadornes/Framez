package com.amadornes.framez.api.movement;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class MovementIssue implements INBTSerializable<NBTTagCompound> {

    private MovementIssue() {

    }

    @Override
    public NBTTagCompound serializeNBT() {

        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

    public static final class Builder {

        public MovementIssue build() {

            return null;// TODO Build issues
        }

    }

}
