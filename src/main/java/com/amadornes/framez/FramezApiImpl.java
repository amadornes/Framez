package com.amadornes.framez;

import net.minecraft.world.World;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFramezApi;
import com.amadornes.framez.api.IModifierRegistry;
import com.amadornes.framez.api.IMotorRegistry;
import com.amadornes.framez.api.movement.IMovementApi;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.MovementApi;
import com.amadornes.framez.util.Utils;

public class FramezApiImpl implements IFramezApi {

    protected FramezApiImpl() {

    }

    @Override
    public IModifierRegistry getModifierRegistry() {

        return ModifierRegistry.INST;
    }

    @Override
    public IMotorRegistry getMotorRegistry() {

        return MotorRegistry.INST;
    }

    @Override
    public IMovementApi getMovementApi() {

        return MovementApi.INST;
    }

    @Override
    public IFrame getFrame(World world, int x, int y, int z) {

        return Utils.getFrame(world, x, y, z);
    }

}
