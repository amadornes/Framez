package com.amadornes.framez.network;

import com.amadornes.framez.client.gui.GuiUpgradeSelect;
import com.amadornes.framez.container.ContainerUpgrade;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

        if (id >= 0 && id < 9) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te != null && te instanceof TileMotor)
                return new ContainerUpgrade(((TileMotor) te).getSafeReference(), id, player.inventory);
        } else if (id >= 9 && id < 18) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te != null && te instanceof TileMotor) return ((TileMotor) te).getUpgrade(id - 9).getKey().getGuiContainer(player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

        if (id >= 0 && id < 9) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te != null && te instanceof TileMotor)
                return new GuiUpgradeSelect(((TileMotor) te).getSafeReference(), id, player.inventory);
        } else if (id >= 9 && id < 18) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te != null && te instanceof TileMotor)
                return ((TileMotor) te).getUpgrade(id - 9).getKey().getConfigGUI(player, Minecraft.getMinecraft().currentScreen);
        }

        return null;
    }

}
