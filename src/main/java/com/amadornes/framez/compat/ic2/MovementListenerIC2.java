package com.amadornes.framez.compat.ic2;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;

public class MovementListenerIC2 implements IMovementHandler {

    @Override
    public boolean handleStartMoving(IMovingBlock block) {

        if (block.getTileEntity() != null && block.getTileEntity() instanceof IEnergyTile && !block.getWorld().isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) block.getTileEntity()));
        return false;
    }

    @Override
    public boolean handleFinishMoving(IMovingBlock block) {

        if (block.getTileEntity() != null && block.getTileEntity() instanceof IEnergyTile && !block.getWorld().isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) block.getTileEntity()));

        return false;
    }

    @Override
    public BlockMovementType getMovementType(World w, Integer x, Integer y, Integer z) {

        return null;
    }

}
