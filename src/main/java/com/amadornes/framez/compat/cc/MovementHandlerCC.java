package com.amadornes.framez.compat.cc;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;
import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.ref.Dependencies;

import cpw.mods.fml.common.registry.GameRegistry;

@Priority(PriorityEnum.VERY_HIGH)
public class MovementHandlerCC implements IMovementHandler {

    private Block b = null;

    private void initBlock() {

        if (b == null)
            b = GameRegistry.findBlock(Dependencies.CC, "CC-Computer");
    }

    @Override
    public BlockMovementType getMovementType(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        return null;
    }

    @Override
    public boolean startMoving(IMovingBlock block) {

        initBlock();

        block.startMoving(false, false);
        return true;
    }

    @Override
    public boolean finishMoving(IMovingBlock block) {

        initBlock();

        block.finishMoving(false, false);
        return true;
    }

    @Override
    public boolean canHandle(World world, int x, int y, int z) {

        initBlock();

        return world.getBlock(x, y, z) == b;
    }

}
