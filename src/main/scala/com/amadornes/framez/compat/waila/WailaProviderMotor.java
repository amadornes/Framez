package com.amadornes.framez.compat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.tile.TileMotorBlinkDrive;
import com.amadornes.framez.tile.TileMotorRotator;
import com.amadornes.framez.tile.TileMotorSlider;

public class WailaProviderMotor implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor data, IWailaConfigHandler cfg) {

        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack stack, List<String> list, IWailaDataAccessor data, IWailaConfigHandler cfg) {

        return list;
    }

    @Override
    public List<String> getWailaBody(ItemStack stack, List<String> list, IWailaDataAccessor data, IWailaConfigHandler cfg) {

        list.add(I18n.format("misc.framez:face") + ": "
                + I18n.format("misc.framez:direction." + ((TileMotor) data.getTileEntity()).getFace()));
        if (data.getTileEntity() instanceof TileMotorSlider)
            list.add(I18n.format("misc.framez:direction") + ": "
                    + I18n.format("misc.framez:direction." + ((TileMotorSlider) data.getTileEntity()).getMovement().getDirection()));
        else if (data.getTileEntity() instanceof TileMotorRotator)
            list.add(I18n.format("misc.framez:direction") + ": "
                    + (((TileMotorRotator) data.getTileEntity()).getMovement().getAxis() % 2 == 1 ? "->" : "<-"));
        else if (data.getTileEntity() instanceof TileMotorBlinkDrive)
            list.add(I18n.format("misc.framez:distance") + ": " + ((TileMotorBlinkDrive) data.getTileEntity()).getMovement().getDistance());

        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack stack, List<String> list, IWailaDataAccessor data, IWailaConfigHandler cfg) {

        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {

        return null;
    }

}