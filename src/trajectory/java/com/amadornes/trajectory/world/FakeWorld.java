package com.amadornes.trajectory.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.amadornes.trajectory.movement.MovingBlock;
import com.amadornes.trajectory.movement.MovingStructure;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FakeWorld {

    public static World getFakeWorld(World world) {

        if (world.isRemote)
            return getClientWorld(null);

        return FakeWorldServer.instance(world);
    }

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

    public static void initFakeWorld(World world) {

        if (world instanceof WorldServer)
            FakeWorldServer.instance(world);
    }

    public static boolean isFakeWorld(World world) {

        if (world.isRemote)
            return isFakeWorldClient(world);

        return world instanceof FakeWorldServer;
    }

    public static boolean isFakeWorldClient(World world) {

        return world instanceof FakeWorldClient;
    }

}
