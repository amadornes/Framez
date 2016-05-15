package com.amadornes.framez;

import java.util.function.IntFunction;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class CommonProxy {

    public void preInit() {

    }

    public void init() {

    }

    public void postInit() {

    }

    public void registerItemRenderer(Item item, String name, IntFunction<String> i2s, int variant) {

    }

    public boolean isFullBlock(IBlockState state) {

        return state.getBlock().isFullBlock(state) && state.getBlock().isFullCube(state) && !state.getBlock().hasTileEntity(state);
    }

    public EntityPlayer getPlayer() {

        return null;
    }

    public boolean isGamePaused() {

        return false;
    }

    public World getWorld() {

        return null;
    }

    public boolean isShiftDown() {

        return false;
    }

    public boolean isCtrlDown() {

        return false;
    }

    public boolean isAltDown() {

        return false;
    }

}
