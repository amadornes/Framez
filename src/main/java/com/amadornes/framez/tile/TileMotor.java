package com.amadornes.framez.tile;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.api.motor.EnumMotorStatus;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorAction;
import com.amadornes.framez.api.motor.IMotorExtension;
import com.amadornes.framez.api.motor.IMotorTrigger;
import com.amadornes.framez.api.motor.IMotorUpgrade;
import com.amadornes.framez.api.motor.IMotorUpgradeFactory;
import com.amadornes.framez.api.motor.IMotorVariable;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.motor.MotorHelper;
import com.amadornes.framez.motor.MotorRegistry;
import com.amadornes.framez.motor.MotorRegistry.MotorExtension;
import com.amadornes.framez.motor.MotorTrigger;
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
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

@SuppressWarnings("unchecked")
public class TileMotor extends TileEntity implements IMotor, IItemHandler, IItemHandlerModifiable, ITickable {

    public static final int UPGRADE_SLOTS = 9;

    public static final IMotorVariable<Double> POWER_STORED = new SimpleMotorVariable<Double>("var.framez:power_stored", d -> d + " kJ");
    public static final IMotorVariable<Double> POWER_STORAGE_SIZE = new SimpleMotorVariable<Double>("var.framez:power_storage_size",
            d -> d + " kJ");
    public static final IMotorVariable<Integer> MOVEMENT_TIME = new SimpleMotorVariable<Integer>();
    public static final IMotorVariable<EnumSet<EnumFacing>> STICKY_FACES = new SimpleMotorVariable<EnumSet<EnumFacing>>();

    public final DynamicReference<TileMotor> reference;
    private IMotorLogic logic;

    private final List<IMotorAction> actionIdMap = new LinkedList<IMotorAction>();
    public final Map<IMotorAction, MotorTrigger> triggers = new HashMap<IMotorAction, MotorTrigger>();
    public final Set<IMotorTrigger> availableTriggers = new LinkedHashSet<IMotorTrigger>();
    public final Pair<IMotorUpgrade, ItemStack>[] upgrades = new Pair[getUpgradeSlots()];
    public final Map<IMotorVariable<?>, Supplier<Object>> nativeVariables = new HashMap<IMotorVariable<?>, Supplier<Object>>();
    private final Map<ResourceLocation, IMotorExtension> extensions = new HashMap<ResourceLocation, IMotorExtension>();

    private IMotorAction currentAction;
    private DynamicReference<Boolean> moving = null;
    private int currentMovementTicks = -1;

    public TileMotor() {

        this(null);
    }

    public TileMotor(IMotorLogic logic) {

        this(null, logic);
    }

    public TileMotor(TileMotor motor, IMotorLogic logic) {

        if (motor == null) {
            this.reference = new DynamicReference<TileMotor>(this);

            this.availableTriggers.add(new MotorTriggerConstant());
            this.availableTriggers.add(new MotorTriggerRedstone(this.reference));

            this.nativeVariables.put(TileMotor.POWER_STORED, () -> 0.0D);
            this.nativeVariables.put(TileMotor.POWER_STORAGE_SIZE, () -> 0.0D);
            this.nativeVariables.put(TileMotor.MOVEMENT_TIME, () -> 20);
            this.nativeVariables.put(TileMotor.STICKY_FACES, () -> EnumSet.of(getLogic().getFace()));

            IMotorExtension ext;
            for (Entry<ResourceLocation, MotorExtension> e : MotorRegistry.INSTANCE.extensions.entrySet()) {
                this.extensions.put(e.getKey(), ext = e.getValue().instantiate(this.reference));
                availableTriggers.addAll(ext.getProvidedTriggers());
            }
        } else {
            this.reference = motor.reference;

            this.triggers.putAll(motor.triggers);
            this.availableTriggers.addAll(motor.availableTriggers);
            for (int i = 0; i < getUpgradeSlots(); i++)
                this.upgrades[i] = motor.upgrades[i];
            this.nativeVariables.putAll(motor.nativeVariables);
            this.extensions.putAll(motor.extensions);

            this.currentAction = motor.currentAction;
            this.moving = motor.moving;
            this.currentMovementTicks = motor.currentMovementTicks;
        }
        if (logic != null) initLogic(logic);
    }

    private void initLogic(IMotorLogic logic) {

        this.logic = logic;
        if (logic == null) return;

        logic.setMotor(reference);

        currentAction = logic.initTriggers(triggers, actionIdMap);
    }

    @Override
    public void update() {

        if (logic == null) getBlockMetadata();
        if (moving != null) {
            if (moving.get()) currentMovementTicks++;
            if (currentMovementTicks >= getVariable(MOVEMENT_TIME)) {
                getLogic().onMovementComplete();
                currentMovementTicks = -1;
                moving.set(false);
                moving = null;
                sendUpdatePacket();
            }
        }
        if (!getMotorWorld().isRemote) {
            IMotorAction newAction = currentAction;
            int activeTriggers = 0;
            for (Entry<IMotorAction, MotorTrigger> e : triggers.entrySet()) {
                if (e.getValue().getTrigger().isActive() == !e.getValue().isInverted()) {
                    newAction = e.getKey();
                    activeTriggers++;
                }
            }
            if (activeTriggers == 1) currentAction = newAction;
            if (currentAction.isMoving()) move(currentAction);
        }
    }

    public Class<? extends TileMotor> getBaseClass() {

        return TileMotor.class;
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

    public int getCurrentMovementTicks() {

        return currentMovementTicks;
    }

    @Override
    public boolean canMove(IMotorAction action) {

        if (action == EnumMotorAction.STOP) return false;
        if (checkStatus(EnumMotorStatus.MOVING)) return false;
        IMotorLogic logic = getLogic();
        if (logic == null) return false;
        if (lastMoveCheck == getWorld().getTotalWorldTime()) return couldMove;

        lastMoveCheck = getWorld().getTotalWorldTime();
        movedStructure = MovingStructure.discover(logic.getStructureSearchLocation(action),
                (w, p) -> w.getBlockState(p).getBlock().isAir(w.getBlockState(p), w, p), getStickyFaces(), false,
                s -> logic.getMovement(s, action), () -> getMotorWorld());
        // TODO: Check consumed energy using the wrong variables
        return couldMove = (movedStructure != null && logic.canMove(movedStructure, action) && logic.getConsumedEnergy(movedStructure,
                getVariable(TileMotor.MOVEMENT_TIME).doubleValue()) <= getVariable(TileMotor.POWER_STORED));
    }

    @Override
    public DynamicReference<Boolean> move(IMotorAction action) {

        if (action == EnumMotorAction.STOP) return null;
        if (canMove(action)) {
            getLogic().move(movedStructure, action);
            currentMovementTicks = 0;
            moving = new DynamicReference<Boolean>(true);
            sendUpdatePacket();
            return moving;
        }
        return null;
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

        if (moving != null && moving.get()) return status == EnumMotorStatus.MOVING;
        if (!couldMove && status == EnumMotorStatus.BLOCKED) return true;
        if (status == EnumMotorStatus.STOPPED) return true;
        return false;
    }

    @Override
    public <T> T getVariable(IMotorVariable<T> variable) {

        List<IMotorExtension> sortedExtensions = new ArrayList<IMotorExtension>();
        List<IMotorUpgrade> sortedUpgrades = new ArrayList<IMotorUpgrade>();
        T value = (T) nativeVariables.get(variable).get();
        boolean foundValue = nativeVariables.containsKey(variable);
        if (!foundValue) {
            for (IMotorExtension extension : extensions.values()) {
                sortedExtensions.add(extension);
                if (!foundValue && extension.getProvidedVariables().containsKey(variable)) {
                    value = (T) extension.getProvidedVariables().get(variable);
                    foundValue = true;
                }
            }
            if (!foundValue) for (Pair<IMotorUpgrade, ItemStack> pair : upgrades) {
                if (pair != null) {
                    sortedUpgrades.add(pair.getKey());
                    if (!foundValue && pair.getKey().getProvidedVariables().containsKey(variable)) {
                        value = (T) pair.getKey().getProvidedVariables().get(variable);
                        foundValue = true;
                    }
                }
            }
        }
        sortedExtensions.sort((a, b) -> Integer.compare(b.getAlterationPriority(variable), a.getAlterationPriority(variable)));
        sortedUpgrades.sort((a, b) -> Integer.compare(b.getAlterationPriority(variable), a.getAlterationPriority(variable)));

        value = sortedExtensions.stream().reduce(value, (a, b) -> b.alterValue(a, variable), (a, b) -> b);
        value = sortedUpgrades.stream().reduce(value, (a, b) -> b.alterValue(a, variable), (a, b) -> b);
        return value;
    }

    public Map<IMotorVariable<?>, Object> gatherVariables() {

        Map<IMotorVariable<?>, Object> variables = new LinkedHashMap<IMotorVariable<?>, Object>();
        for (IMotorVariable<?> var : nativeVariables.keySet())
            variables.put(var, getVariable(var));
        for (Pair<IMotorUpgrade, ItemStack> p : this.upgrades)
            if (p != null) for (IMotorVariable<?> var : p.getKey().getProvidedVariables().keySet())
                variables.put(var, getVariable(var));
        for (IMotorExtension extension : extensions.values())
            for (IMotorVariable<?> var : extension.getProvidedVariables().keySet())
                variables.put(var, getVariable(var));
        return variables;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing side) {

        for (IMotorExtension extension : extensions.values())
            if (extension.hasCapability(capability, side)) return true;
        return super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {

        for (IMotorExtension extension : extensions.values())
            if (extension.hasCapability(capability, side)) return extension.getCapability(capability, side);
        return super.getCapability(capability, side);
    }

    @Override
    public <T extends IMotorExtension> T getExtension(ResourceLocation name, Class<T> type) {

        return (T) extensions.get(name);
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

    public void writeToPacketNBT(NBTTagCompound tag) {

        tag.setInteger("currentAction", actionIdMap.indexOf(currentAction));
        tag.setBoolean("moving", moving != null && moving.get());
        tag.setInteger("currentMovementTicks", currentMovementTicks);
        tag.setTag("logic", getLogic().serializeNBT());
    }

    public void readFromPacketNBT(NBTTagCompound tag) {

        if (logic == null) getBlockMetadata();
        currentAction = actionIdMap.get(tag.getInteger("currentAction"));
        moving = new DynamicReference<Boolean>(tag.getBoolean("moving"));
        currentMovementTicks = tag.getInteger("currentMovementTicks");
        getLogic().deserializeNBT(tag.getCompoundTag("logic"));
    }

    public void sendUpdatePacket() {

        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    public SPacketUpdateTileEntity getDescriptionPacket() {

        NBTTagCompound tag = new NBTTagCompound();
        writeToPacketNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {

        readFromPacketNBT(packet.getNbtCompound());
    }

    @Override
    public void markDirty() {

        super.markDirty();
        getBlockMetadata();
    }

    @Override
    public int getBlockMetadata() {

        int meta = super.getBlockMetadata();
        if (getWorld() != null && logic == null) initLogic(IMotorLogic.create(meta));
        return meta;
    }

    @Override
    public boolean hasFastRenderer() {

        return true;
    }

}
