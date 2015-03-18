package com.amadornes.framez.world;

import net.minecraft.world.World;

import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FakeWorld {

    public static World getFakeWorld(MovingStructure structure) {

        if (structure.getWorld().isRemote)
            return getClientWorld(structure);

        return FakeWorldServer.instance(structure);
    }

    public static World getFakeWorld(MovingBlock block) {

        return getFakeWorld(block.getStructure());
    }

    @SideOnly(Side.CLIENT)
    private static World getClientWorld(MovingStructure structure) {

        return FakeWorldClient.instance(structure);
    }

}
