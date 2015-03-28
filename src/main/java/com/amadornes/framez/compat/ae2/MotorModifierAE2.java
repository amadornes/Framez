package com.amadornes.framez.compat.ae2;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.GridFlags;
import appeng.api.networking.GridNotification;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridBlock;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.jtraits.ITrait;
import com.amadornes.jtraits.JTrait;

public class MotorModifierAE2 implements IMotorModifierPower {

    @Override
    public String getType() {

        return "ae2";
    }

    @Override
    public boolean isCompatibleWith(IMotorModifier mod) {

        return true;
    }

    @Override
    public boolean isValidCombination(Collection<IMotorModifier> combination) {

        return true;
    }

    @Override
    public Class<? extends ITrait> getTraitClass() {

        return TMotorAE2.class;
    }

    public static abstract class TMotorAE2 extends JTrait<IMotor> implements IMotor, IAEPowerStorage, IGridHost, IGridBlock {

        private IGridNode node = null;

        @Override
        public EnumSet<GridFlags> getFlags() {

            return EnumSet.copyOf(Arrays.asList(GridFlags.CANNOT_CARRY));
        }

        @Override
        public boolean isWorldAccessible() {

            return true;
        }

        @Override
        public DimensionalCoord getLocation() {

            return new DimensionalCoord(getWorld(), getX(), getY(), getZ());
        }

        @Override
        public AEColor getGridColor() {

            return AEColor.Transparent;
        }

        @Override
        public void onGridNotification(GridNotification notification) {

        }

        @Override
        public void setNetworkStatus(IGrid grid, int channelsInUse) {

        }

        @Override
        public EnumSet<ForgeDirection> getConnectableSides() {

            return EnumSet.copyOf(Arrays.asList(ForgeDirection.VALID_DIRECTIONS));
        }

        @Override
        public IGridHost getMachine() {

            return this;
        }

        @Override
        public void gridChanged() {

        }

        @Override
        public ItemStack getMachineRepresentation() {

            return new ItemStack(Blocks.stone);
        }

        @Override
        public IGridNode getGridNode(ForgeDirection dir) {

            // if (getWorld() != null && !getWorld().isRemote) {
            // if (node == null)
            // node = AEApi.instance().createGridNode(this);
            // node.updateState();
            // }

            return node;
        }

        @Override
        public AECableType getCableConnectionType(ForgeDirection dir) {

            // if (node != null)
            // for (IGridNode n : node.getGrid().getNodes())
            // if (n.getMachine() instanceof IPartCable && getLocation().copy().add(dir, 1).equals(n.getGridBlock().getLocation()))
            // return n.getMachine().getCableConnectionType(dir.getOpposite());

            return AECableType.SMART;
        }

        @Override
        public void securityBreak() {

        }

        @Override
        public double injectAEPower(double amt, Actionable mode) {

            return amt - injectPower(amt, mode == Actionable.MODULATE);
        }

        @Override
        public double extractAEPower(double amt, Actionable mode, PowerMultiplier usePowerMultiplier) {

            return 0;
        }

        @Override
        public double getIdlePowerUsage() {

            return 0;
        }

        @Override
        public double getAEMaxPower() {

            return getEnergyBufferSize(); // FIXME
        }

        @Override
        public double getAECurrentPower() {

            return getEnergyBuffer(); // FIXME
        }

        @Override
        public boolean isAEPublicPowerStorage() {

            return false;
        }

        @Override
        public AccessRestriction getPowerFlow() {

            return AccessRestriction.WRITE;
        }

        @Override
        public void onFirstTick() {

            // FIXME _super.onFirstTick();

            if (getWorld() != null && !getWorld().isRemote) {
                if (node == null)
                    node = AEApi.instance().createGridNode(this);
                node.updateState();
            }
        }

        @Override
        public void onUnload() {

            // FIXME _super.onUnload();

            if (node != null)
                node.destroy();
        }

        public double getTotalStored() {

            double nodePower = 0;

            if (node != null) {
                for (IGridNode n : node.getGrid().getNodes()) {
                    if (n != node) {
                        IGridHost host = n.getGridBlock().getMachine();
                        if (host instanceof IAEPowerStorage) {
                            IAEPowerStorage storage = (IAEPowerStorage) host;
                            if (storage.isAEPublicPowerStorage()
                                    && (storage.getPowerFlow() == AccessRestriction.READ || storage.getPowerFlow() == AccessRestriction.READ_WRITE))
                                nodePower += storage.getAECurrentPower();
                        }
                    }
                }
            }

            return Math.min(getEnergyBuffer() + nodePower, getEnergyBufferSize());// FIXME
        }

    }

}
