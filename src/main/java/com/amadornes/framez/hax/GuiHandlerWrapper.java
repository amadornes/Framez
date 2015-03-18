package com.amadornes.framez.hax;

import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.tile.TileMoving;
import com.amadornes.framez.world.FakeWorld;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerWrapper implements IGuiHandler {

    private IGuiHandler handler;

    public GuiHandlerWrapper(IGuiHandler handler) {

        this.handler = handler;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMoving) {
            MovingObjectPosition mop = ((TileMoving) te).rayTrace(player);
            if (mop != null) {
                MovingBlock b = (MovingBlock) ((Entry<?, ?>) mop.hitInfo).getKey();
                return handler.getServerGuiElement(ID, player, FakeWorld.getFakeWorld(b), b.getX(), b.getY(), b.getZ());
            }
        }

        return handler.getServerGuiElement(ID, player, world, x, y, z);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMoving) {
            MovingObjectPosition mop = ((TileMoving) te).rayTrace(player);
            if (mop != null) {
                MovingBlock b = (MovingBlock) ((Entry<?, ?>) mop.hitInfo).getKey();
                return handler.getClientGuiElement(ID, player, FakeWorld.getFakeWorld(b), b.getX(), b.getY(), b.getZ());
            }
        }

        return handler.getClientGuiElement(ID, player, world, x, y, z);
    }

}
