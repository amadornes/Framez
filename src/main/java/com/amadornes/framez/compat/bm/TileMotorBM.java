package com.amadornes.framez.compat.bm;

import java.util.AbstractMap;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

import com.amadornes.framez.config.Config;
import com.amadornes.framez.tile.TileMotor;

public class TileMotorBM extends TileMotor {

    private ItemStack item = new ItemStack(CompatModuleBM.item);

    @Override
    public boolean shouldMove() {

        return isBeingPowered();
    }

    @Override
    public boolean hasEnoughFramezPower(double power) {

        return SoulNetworkHandler.getCurrentEssence(getPlacer()) >= power;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @Override
    public void consumeFramezPower(double power) {

        setupItem();
        SoulNetworkHandler.syphonAndDamageFromNetwork(item, MinecraftServer.getServer().getConfigurationManager()
                .func_152612_a(getPlacer()), (int) (power * Config.PowerRatios.bmLP));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Entry<Double, Double> getExtraInfo() {

        double lp = SoulNetworkHandler.getCurrentEssence(getPlacer());
        return new AbstractMap.SimpleEntry(Math.max(lp, 1), lp);
    }

    private void setupItem() {

        SoulNetworkHandler.checkAndSetItemOwner(item, getPlacer());
    }

}
