package com.amadornes.framez.compat.ic2;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.movement.IMovementListener;

public class MovementListenerIC2 implements IMovementListener {

    @Override
    public void onStartMoving(Object o, ForgeDirection direction) {

        if (o instanceof TileEntity) {
            ((TileEntity) o).invalidate();
        } else {
            try {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) o));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onFinishMoving(Object o, ForgeDirection direction) {

        if (o instanceof TileEntity) {
            ((TileEntity) o).validate();
        } else {
            try {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) o));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean canHandle(Object o) {

        return o instanceof IEnergyTile;
    }
}
