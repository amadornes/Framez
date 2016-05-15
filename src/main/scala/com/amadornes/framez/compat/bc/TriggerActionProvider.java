package com.amadornes.framez.compat.bc;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.statements.IActionExternal;
import buildcraft.api.statements.IActionInternal;
import buildcraft.api.statements.IActionProvider;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.ITriggerExternal;
import buildcraft.api.statements.ITriggerInternal;
import buildcraft.api.statements.ITriggerProvider;

import com.amadornes.framez.tile.TileMotor;

public class TriggerActionProvider implements ITriggerProvider, IActionProvider {

    @Override
    public Collection<ITriggerInternal> getInternalTriggers(IStatementContainer container) {

        return Arrays.asList();
    }

    @Override
    public Collection<ITriggerExternal> getExternalTriggers(ForgeDirection side, TileEntity tile) {

        if (tile != null && tile instanceof TileMotor)
            return Arrays.asList(TriggerIsMoving.INST);

        return Arrays.asList();
    }

    @Override
    public Collection<IActionInternal> getInternalActions(IStatementContainer container) {

        return Arrays.asList();
    }

    @Override
    public Collection<IActionExternal> getExternalActions(ForgeDirection side, TileEntity tile) {

        if (tile != null && tile instanceof TileMotor)
            return Arrays.asList(ActionMove.INST);

        return Arrays.asList();
    }

}