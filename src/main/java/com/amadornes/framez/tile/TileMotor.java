package com.amadornes.framez.tile;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.api.motor.EnumMotorStatus;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorTrigger;
import com.amadornes.framez.api.motor.IMotorUpgrade;
import com.amadornes.framez.api.motor.IMotorUpgradeFactory;
import com.amadornes.framez.api.motor.IMotorVariable;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.motor.MotorHelper;
import com.amadornes.framez.motor.MotorTriggerConstant;
import com.amadornes.framez.motor.MotorTriggerRedstone;
import com.amadornes.framez.motor.SimpleMotorVariable;
import com.amadornes.framez.motor.logic.IMotorLogic;
import com.amadornes.framez.motor.upgrade.UpgradeCamouflage;
import com.amadornes.framez.movement.MovingStructure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

@SuppressWarnings("unchecked")
public class TileMotor extends TileEntity implements IMotor, IItemHandler, IItemHandlerModifiable, ITickable {

    public static final int UPGRADE_SLOTS = 9;

    public static final IMotorVariable<Double> POWER_STORED = new SimpleMotorVariable<Double>("var.framez:power_stored", d -> d + " kJ");
    public static final IMotorVariable<Double> POWER_STORAGE_SIZE = new SimpleMotorVariable<Double>("var.framez:power_storage_size",
            d -> d + " kJ");
    public static final IMotorVariable<Double> MOVEMENT_TIME = new SimpleMotorVariable<Double>();
    public static final IMotorVariable<EnumSet<EnumFacing>> STICKY_FACES = new SimpleMotorVariable<EnumSet<EnumFacing>>();

    public final DynamicReference<TileMotor> reference;
    private IMotorLogic logic;

    public final Map<EnumMotorAction, IMotorTrigger> triggers = new HashMap<EnumMotorAction, IMotorTrigger>();
    public final Pair<IMotorUpgrade, ItemStack>[] upgrades = new Pair[getUpgradeSlots()];
    public final Map<EnumMotorStatus, Boolean> statuses = new HashMap<EnumMotorStatus, Boolean>();
    public final Map<IMotorVariable<?>, Supplier<Object>> nativeVariables = new HashMap<IMotorVariable<?>, Supplier<Object>>();

    public TileMotor() {

        this(null);
    }

    public TileMotor(DynamicReference<TileMotor> reference) {

        if (reference == null) {
            this.reference = new DynamicReference<TileMotor>(this);
            this.logic = null;
        } else {
            this.reference = reference;
            this.logic = reference.get().getLogic();
        }
        if (this.logic != null) this.logic.setMotor(this.reference);

        triggers.put(EnumMotorAction.MOVE_FORWARD, new MotorTriggerRedstone(this, false));
        triggers.put(EnumMotorAction.STOP, new MotorTriggerRedstone(this, true));
        triggers.put(EnumMotorAction.MOVE_BACKWARD, new MotorTriggerConstant(false));
        if (triggers.size() != EnumMotorAction.VALUES.length) throw new IllegalStateException("Somebody's been tampering with reality!");

        for (EnumMotorStatus status : EnumMotorStatus.VALUES)
            statuses.put(status, status == EnumMotorStatus.STOPPED);

        nativeVariables.put(TileMotor.POWER_STORED, () -> 0.0D);
        nativeVariables.put(TileMotor.POWER_STORAGE_SIZE, () -> 0.0D);
        nativeVariables.put(TileMotor.MOVEMENT_TIME, () -> 0.0D);
        nativeVariables.put(TileMotor.STICKY_FACES, () -> EnumSet.of(getLogic().getFace()));
    }

    @Override
    public void update() {

        if (!getMotorWorld().isRemote && triggers.get(EnumMotorAction.MOVE_FORWARD).isActive()) move();
    }

    public Class<? extends TileMotor> getBaseClass() {

        return TileMotor.class;
    }

    public TileMotor setLogic(IMotorLogic logic) {

        this.logic = logic;
        this.logic.setMotor(this.reference);
        return this;
    }

    @Override
    public World getMotorWorld() {

        return getWorld();
    }

    @Override
    public BlockPos getMotorPos() {

        return getPos();
    }

    @Override
    public DynamicReference<TileMotor> getSafeReference() {

        return reference;
    }

    private long lastMoveCheck = 0;
    private boolean couldMove = false;
    private MovingStructure movedStructure = null;

    @Override
    public boolean canMove() {

        IMotorLogic logic = getLogic();
        if (logic == null) return false;
        if (lastMoveCheck == getWorld().getTotalWorldTime()) return couldMove;

        lastMoveCheck = getWorld().getTotalWorldTime();
        movedStructure = MovingStructure.discover(getMotorPos(), (w, p) -> w.getBlockState(p).getBlock().isAir(w, p), getStickyFaces(),
                false, s -> logic.getMovement(s), () -> getMotorWorld());
        return couldMove = logic.canMove(movedStructure)
                && logic.getConsumedEnergy(movedStructure, getVariable(TileMotor.MOVEMENT_TIME)) <= getVariable(TileMotor.POWER_STORED);
    }

    @Override
    public DynamicReference<Boolean> move() {

        if (canMove()) return getLogic().move(movedStructure);
        return new DynamicReference<Boolean>(false);
    }

    @Override
    public IMotorTrigger getTrigger(EnumMotorAction action) {

        return triggers.get(action);
    }

    @Override
    public int getUpgradeSlots() {

        return UPGRADE_SLOTS;
    }

    @Override
    public Entry<IMotorUpgrade, ItemStack> getUpgrade(int slot) {

        return upgrades[slot];
    }

    @Override
    public boolean checkStatus(EnumMotorStatus status) {

        return statuses.get(status);
    }

    @Override
    public <T> T getVariable(IMotorVariable<T> variable) {

        List<IMotorUpgrade> sortedUpgrades = new ArrayList<IMotorUpgrade>();
        T value = (T) nativeVariables.get(variable).get();
        boolean foundValue = nativeVariables.containsKey(variable);
        for (Pair<IMotorUpgrade, ItemStack> pair : upgrades) {
            if (pair != null) {
                sortedUpgrades.add(pair.getKey());
                if (!foundValue && pair.getKey().getProvidedVariables().containsKey(variable)) {
                    value = (T) pair.getKey().getProvidedVariables().get(variable);
                    foundValue = true;
                }
            }
        }
        sortedUpgrades.sort((a, b) -> Integer.compare(b.getAlterationPriority(variable), a.getAlterationPriority(variable)));
        return sortedUpgrades.stream().reduce(value, (a, b) -> b.alterValue(a, variable), (a, b) -> b);
    }

    public Map<IMotorVariable<?>, Object> gatherVariables() {

        Map<IMotorVariable<?>, Object> variables = new LinkedHashMap<IMotorVariable<?>, Object>();
        for (IMotorVariable<?> var : nativeVariables.keySet())
            variables.put(var, getVariable(var));
        for (Pair<IMotorUpgrade, ItemStack> p : this.upgrades)
            if (p != null) for (IMotorVariable<?> var : p.getKey().getProvidedVariables().keySet())
                variables.put(var, getVariable(var));
        return variables;
    }

    private EnumSet<EnumFacing> getStickyFaces() {

        return getVariable(STICKY_FACES);
    }

    @Override
    public int getSlots() {

        return getUpgradeSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {

        return upgrades[slot] != null ? upgrades[slot].getRight() : null;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {

        // if (getStackInSlot(slot) != null) return stack;

        if (!stack.hasCapability(IMotorUpgradeFactory.CAPABILITY_ITEM_UPGRADE, null)) return stack;
        IMotorUpgradeFactory upgradeFactory = stack.getCapability(IMotorUpgradeFactory.CAPABILITY_ITEM_UPGRADE, null);
        if (MotorHelper.addUpgrade(this, upgradeFactory, slot, stack, simulate)) return stack.copy().splitStack(stack.stackSize - 1);
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

        if (getStackInSlot(slot) == null) return null;

        ItemStack stack = upgrades[slot].getRight();
        if (!simulate) MotorHelper.removeUpgrade(this, slot);
        return stack;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {

        if (stack == null) extractItem(slot, 1, false);
        else insertItem(slot, stack, false);
    }

    public void copyFrom(TileMotor motor) {

        this.logic = motor.getLogic();

        this.triggers.putAll(motor.triggers);
        for (int i = 0; i < getUpgradeSlots(); i++)
            this.upgrades[i] = motor.upgrades[i];
        this.statuses.putAll(motor.statuses);
        this.nativeVariables.putAll(motor.nativeVariables);
    }

    public IMotorLogic getLogic() {

        if (logic == null) getBlockMetadata();
        return logic;
    }

    public void setUpgrade(int slot, IMotorUpgrade upgrade, ItemStack stack) {

        if (upgrade == null || stack == null) upgrades[slot] = null;
        else upgrades[slot] = new ImmutablePair<IMotorUpgrade, ItemStack>(upgrade, stack);
    }

    public IBlockState getActualState(IBlockState state) {

        return state.withProperty(BlockMotor.PROPERTY_LOGIC_TYPE, getLogic() == null ? 0 : getLogic().getID());
    }

    public IExtendedBlockState getExtendedState(IExtendedBlockState state) {

        for (Pair<IMotorUpgrade, ItemStack> pair : upgrades) {
            if (pair != null) {
                if (pair.getKey() instanceof UpgradeCamouflage) {
                    UpgradeCamouflage camo = (UpgradeCamouflage) pair.getKey();
                    for (int i = 0; i < 6; i++) {
                        ItemStack stack = camo.getStackInSlot(i);
                        if (stack != null && stack.getItem() instanceof ItemBlock) {
                            IBlockState faceState = ((ItemBlock) stack.getItem()).block
                                    .getStateFromMeta(((ItemBlock) stack.getItem()).getMetadata(stack.getMetadata()));
                            if (Framez.proxy.isFullBlock(faceState)) state = state.withProperty(BlockMotor.PROPERTIES_CAMO[i], faceState);
                        }
                    }
                }
            }
        }
        return state;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
    }

    @Override
    public void markDirty() {

        super.markDirty();
        getBlockMetadata();
    }

    @Override
    public int getBlockMetadata() {

        int meta = super.getBlockMetadata();
        if (getWorld() != null) {
            Class<? extends IMotorLogic> logicClass = IMotorLogic.TYPES[meta];
            if (logic == null || logicClass.isAssignableFrom(logic.getClass())) {
                setLogic(IMotorLogic.create(meta));
            }
        }
        return meta;
    }

}
