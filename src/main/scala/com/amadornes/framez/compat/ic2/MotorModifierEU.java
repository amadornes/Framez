package com.amadornes.framez.compat.ic2;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;

import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.compat.Dependencies;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.jtraits.JTrait;

import cpw.mods.fml.common.registry.GameRegistry;

public class MotorModifierEU implements IMotorModifierPower {

    @Override
    public String getType() {

        return "eu";
    }

    @Override
    public boolean isCompatibleWith(IMotorModifier mod) {

        return true;
    }

    @Override
    public boolean isCombinationValid(Collection<IMotorModifier> combination) {

        return true;
    }

    @Override
    public void registerRecipes(ItemStack unmodified, ItemStack modified) {

        Item plate = GameRegistry.findItem(Dependencies.IC2, "itemPlates");
        Item cable = GameRegistry.findItem(Dependencies.IC2, "itemCable");
        Item crystal = GameRegistry.findItem(Dependencies.IC2, "itemBatCrystal");
        Item recipe_part = GameRegistry.findItem(Dependencies.IC2, "itemRecipePart");

        if (plate != null && cable != null && crystal != null && recipe_part != null)
            GameRegistry.addShapedRecipe(modified, "ici", "wuw", "imi", 'i', new ItemStack(plate, 1, 4), 'c', new ItemStack(crystal, 1,
                    OreDictionary.WILDCARD_VALUE), 'w', new ItemStack(cable, 1, 3), 'u', unmodified, 'm', new ItemStack(recipe_part, 1, 1));
    }

    @Override
    public Class<? extends JTrait<? extends IMotor>> getTraitClass() {

        return TMotorEU.class;
    }

    public static class TMotorEU extends JTrait<IMotor> implements IEnergySink {

        @Override
        public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {

            return true;
        }

        @Override
        public double getDemandedEnergy() {

            return (_super.getEnergyBufferSize() - _super.getEnergyBuffer()) / FramezCompatConfig.ratio_eu;
        }

        @Override
        public int getSinkTier() {

            return 4;
        }

        @Override
        public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {

            amount *= FramezCompatConfig.ratio_eu;
            return (amount - _super.injectPower(amount, false)) / FramezCompatConfig.ratio_eu;
        }

        public void onFirstTick() {

            World world = _super.getWorld();
            IEnergyTile self = this;
            if (!world.isRemote)
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(self));
        }

        public void onUnload() {

            World world = _super.getWorld();
            IEnergyTile self = this;
            if (!world.isRemote)
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(self));
        }
    }

}
