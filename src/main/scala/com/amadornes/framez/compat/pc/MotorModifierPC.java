package com.amadornes.framez.compat.pc;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import pneumaticCraft.api.tileentity.AirHandlerSupplier;
import pneumaticCraft.api.tileentity.IAirHandler;
import pneumaticCraft.api.tileentity.IPneumaticMachine;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.jtraits.JTrait;

public class MotorModifierPC implements IMotorModifierPower {

    @Override
    public String getType() {

        return "pc";
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

        // TODO: Register recipes
    }

    @Override
    public Class<? extends JTrait<? extends IMotor>> getTraitClass() {

        return TMotorPC.class;
    }

    public static class TMotorPC extends JTrait<IMotor> implements IPneumaticMachine {

        private IAirHandler airHandler = AirHandlerSupplier.getAirHandler(5, 7, 10000);

        @Override
        public IAirHandler getAirHandler() {

            return airHandler;
        }

        @Override
        public boolean isConnectedTo(ForgeDirection side) {

            return true;
        }

        public void update() {

            _super.update();
            airHandler.updateEntityI();
        }

        public void writeMotor(NBTTagCompound tag) {

            _super.writeMotor(tag);
            airHandler.writeToNBTI(tag);
        }

        public void readMotor(NBTTagCompound tag) {

            _super.readMotor(tag);
            airHandler.readFromNBTI(tag);
        }

        public void onNeighborChange() {

            _super.onNeighborChange();
            airHandler.onNeighborChange();
        }

        public void onFirstTick() {

            _super.onFirstTick();
            airHandler.validateI((TileEntity) (Object) this);
        }

        public double getEnergyBuffer() {

            return airHandler.getCurrentAir(ForgeDirection.UNKNOWN);
        }

        public double getEnergyBufferSize() {

            return airHandler.getVolume();
        }

        public double drainPower(double amount, boolean simulated) {

            double drained = Math.min(getEnergyBuffer(), amount);

            if (simulated)
                return drained;

            airHandler.addAir((int) -drained, ForgeDirection.UNKNOWN);
            _super.notifyChange();
            return drained;
        }
    }
}