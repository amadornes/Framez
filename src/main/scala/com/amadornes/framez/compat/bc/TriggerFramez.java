package com.amadornes.framez.compat.bc;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.statements.IStatement;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.ITriggerExternal;

import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TriggerFramez implements ITriggerExternal {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    private String id;
    private String description;

    public TriggerFramez(String id, String description) {

        this.id = id;
        this.description = description;
    }

    @Override
    public String getUniqueTag() {

        return ModInfo.MODID + ":trigger_" + id;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon() {

        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {

        icon = iconRegister.registerIcon(ModInfo.MODID + ":bc/trigger_" + id);
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
    public boolean isTriggerActive(TileEntity target, ForgeDirection side, IStatementContainer source, IStatementParameter[] parameters) {

        if (target != null && target instanceof TileMotor)
            return isActive((TileMotor) target);
        return false;
    }

    public abstract boolean isActive(TileMotor motor);
}