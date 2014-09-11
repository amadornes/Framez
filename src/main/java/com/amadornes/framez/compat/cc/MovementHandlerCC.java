package com.amadornes.framez.compat.cc;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.ref.Dependencies;

import cpw.mods.fml.common.registry.GameRegistry;

public class MovementHandlerCC implements IMovementHandler {

    private Block b = null;

    @Override
    public boolean handleStartMoving(IMovingBlock block) {

        initBlock();

        if (block.getBlock() == b) {
            block.remove_do(false, false);
            return true;
        }

        return false;
    }

    @Override
    public boolean handleFinishMoving(IMovingBlock block) {

        initBlock();

        if (block.getBlock() == b) {
            block.place_do(false, false);
            return true;
        }

        return false;
    }

    private void initBlock() {

        if (b == null)
            b = GameRegistry.findBlock(Dependencies.CC, "CC-Computer");
    }

    @Override
    public BlockMovementType getMovementType(World w, Integer x, Integer y, Integer z) {

        return null;
    }

}
