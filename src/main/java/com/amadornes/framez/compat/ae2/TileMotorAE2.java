package com.amadornes.framez.compat.ae2;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.config.PowerUnits;
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

import com.amadornes.framez.config.Config;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileMotorAE2 extends TileMotor implements IAEPowerStorage, IGridHost, IGridBlock {

    private IGridNode node = null;

    private double getRatio() {

        return PowerUnits.RF.convertTo(PowerUnits.AE, Config.PowerRatios.rf);
    }

    @Override
    public boolean shouldMove() {

        return isBeingPowered();
    }

    @Override
    public boolean hasEnoughFramezPower(double power) {

        return getTotalStored() >= power;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @Override
    public void consumeFramezPower(double power) {

        drain(power);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Entry<Double, Double> getExtraInfo() {

        return new AbstractMap.SimpleEntry(maxStored, stored);
    }

    private double oldPower = 0;

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (node == null && !worldObj.isRemote) {
            node = AEApi.instance().createGridNode(this);
            node.updateState();
        }

        double pow = getTotalStored();
        if (pow != oldPower)
            sendUpdatePacket();
        oldPower = pow;
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

        return Math.min(stored + nodePower, maxStored);
    }

    public void drain(double amt) {

        double drainedFromThis = Math.min(amt, stored);
        stored -= drainedFromThis;

        amt *= getRatio();

        if (amt > 0) {
            for (IGridNode n : node.getGrid().getNodes()) {
                if (n != node) {
                    IGridHost host = n.getGridBlock().getMachine();
                    if (host instanceof IAEPowerStorage) {
                        IAEPowerStorage storage = (IAEPowerStorage) host;
                        if (storage.isAEPublicPowerStorage()
                                && (storage.getPowerFlow() == AccessRestriction.READ || storage.getPowerFlow() == AccessRestriction.READ_WRITE)) {
                            double extracted = storage.extractAEPower(amt, Actionable.MODULATE, PowerMultiplier.ONE);
                            amt -= extracted;
                            if (amt == 0)
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onChunkUnload() {

        super.onChunkUnload();
        if (node != null)
            node.destroy();
    }

    @Override
    public void invalidate() {

        super.invalidate();
        if (node != null)
            node.destroy();
    }

    @Override
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier usePowerMultiplier) {

        return 0;
    }

    @Override
    public double injectAEPower(double amt, Actionable mode) {

        double ratio = getRatio();

        int tot = (int) (stored + (amt / ratio));
        stored = Math.min(tot, maxStored);
        sendUpdatePacket();
        return (tot - stored) * ratio;
    }

    @Override
    public double getAEMaxPower() {

        return maxStored * getRatio();
    }

    @Override
    public double getAECurrentPower() {

        return stored * getRatio();
    }

    @Override
    public boolean isAEPublicPowerStorage() {

        return true;
    }

    @Override
    public AccessRestriction getPowerFlow() {

        return AccessRestriction.WRITE;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {

        if (dir == getFace())
            return null;
        if (node == null) {
            node = AEApi.instance().createGridNode(this);
            node.updateState();
        }
        return node;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection dir) {

        return AECableType.DENSE;
    }

    @Override
    public void securityBreak() {

        getWorldObj().setBlockToAir(xCoord, yCoord, zCoord);
        getWorldObj().removeTileEntity(xCoord, yCoord, zCoord);
    }

    @Override
    public double getIdlePowerUsage() {

        return 0;
    }

    @Override
    public EnumSet<GridFlags> getFlags() {

        return EnumSet.copyOf(Arrays.asList(GridFlags.CANNOT_CARRY));
    }

    @Override
    public DimensionalCoord getLocation() {

        return new DimensionalCoord(getWorldObj(), xCoord, yCoord, zCoord);
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

        List<ForgeDirection> sides = new ArrayList<ForgeDirection>(Arrays.asList(ForgeDirection.values()));
        sides.remove(getFace());
        return EnumSet.copyOf(sides);
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

        return new ItemStack(GameRegistry.findBlock(ModInfo.MODID, References.Names.Registry.MOTOR + "." + MotorProviderAE2.inst.getId()));
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);

        tag.setDouble("stored", getTotalStored());
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);

        stored = tag.getDouble("stored");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        tag.setDouble("stored", stored);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        stored = tag.getDouble("stored");
    }

    @Override
    public boolean isWorldAccessible() {

        return true;
    }

}
