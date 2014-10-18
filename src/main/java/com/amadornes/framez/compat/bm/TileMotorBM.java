package com.amadornes.framez.compat.bm;

import java.util.AbstractMap;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.PowerHelper.PowerUnit;

public class TileMotorBM extends TileMotor {

    private ItemStack item = new ItemStack(CompatModuleBM.item);

    private void setupItem() {

        SoulNetworkHandler.checkAndSetItemOwner(item, getPlacer());
    }

    @Override
    public boolean shouldMove() {

        return isBeingPowered();
    }

    @Override
    public boolean hasEnoughPower(double power) {

        return SoulNetworkHandler.getCurrentEssence(getPlacer()) >= power;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @Override
    public PowerUnit getPowerUnit() {

        return PowerUnit.LP;
    }

    @Override
    public void consumePower(double power) {

        setupItem();
        SoulNetworkHandler.syphonAndDamageFromNetwork(item, MinecraftServer.getServer().getConfigurationManager().func_152612_a(getPlacer()),
                (int) power);

        sendUpdatePacket();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Entry<Double, Double> getExtraInfo() {

        double lp = SoulNetworkHandler.getCurrentEssence(getPlacer());
        return new AbstractMap.SimpleEntry(Math.max(lp, 1), lp);
    }

}
