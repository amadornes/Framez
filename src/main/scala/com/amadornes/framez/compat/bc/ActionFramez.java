package com.amadornes.framez.compat.bc;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.statements.IActionExternal;
import buildcraft.api.statements.IStatement;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;

import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ActionFramez implements IActionExternal {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    private String id;
    private String description;

    public ActionFramez(String id, String description) {

        this.id = id;
        this.description = description;
    }

    @Override
    public String getUniqueTag() {

        return ModInfo.MODID + ":action_" + id;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon() {

        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {

        icon = iconRegister.registerIcon(ModInfo.MODID + ":bc/action_" + id);
    }

    @Override
    public int maxParameters() {

        return 0;
    }

    @Override
    public int minParameters() {

        return 0;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public IStatementParameter createParameter(int index) {

        return null;
    }

    @Override
    public IStatement rotateLeft() {

        return null;
    }

    @Override
    public void actionActivate(TileEntity target, ForgeDirection side, IStatementContainer source, IStatementParameter[] parameters) {

        if (target != null && target instanceof TileMotor)
            activate((TileMotor) target);
    }

    public abstract void activate(TileMotor motor);
}