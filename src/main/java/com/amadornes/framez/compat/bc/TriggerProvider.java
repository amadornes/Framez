package com.amadornes.framez.compat.bc;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.ITriggerExternal;
import buildcraft.api.statements.ITriggerInternal;
import buildcraft.api.statements.ITriggerProvider;

import com.amadornes.framez.tile.TileMotor;

public class TriggerProvider implements ITriggerProvider {

    @Override
    public Collection<ITriggerInternal> getInternalTriggers(IStatementContainer container) {

        return Arrays.asList();
    }

    @Override
    public Collection<ITriggerExternal> getExternalTriggers(ForgeDirection side, TileEntity tile) {

        if (tile != null && tile instanceof TileMotor)
            return Arrays.asList(TriggerCanMove.INST, TriggerIsMoving.INST);

        return Arrays.asList();
    }

}
