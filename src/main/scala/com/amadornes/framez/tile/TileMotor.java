package com.amadornes.framez.tile;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorUpgrade;
import com.amadornes.framez.api.modifier.IMotorUpgrade.IConfigurableMotorUpgrade;
import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementBlink;
import com.amadornes.framez.api.movement.IMovementRegistry.IgnoreMode;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.api.movement.ISticky.IAdvancedSticky;
import com.amadornes.framez.api.movement.MovementIssue;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.MotorSetting;
import com.amadornes.framez.movement.MovementHelper;
import com.amadornes.framez.movement.MovementHelper.MovementData;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.movement.RedstoneMode;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketSetMotorSetting;
import com.amadornes.framez.upgrade.MotorUpgradeData;
import com.amadornes.framez.util.MotorCache;
import com.amadornes.framez.util.RedstoneHelper;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovementCallback;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.IMovingStructure;
import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.api.TrajectoryFeature;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;

public abstract class TileMotor extends TileFramez implements IMotor, IMovementCallback, ISticky, IAdvancedSticky {

    private int face;

    private IMotorModifier[] modifiers;
    private IMotorUpgradeData[] upgrades = new IMotorUpgradeData[7];
    private Map<MotorSetting<?>, Object> settings = new HashMap<MotorSetting<?>, Object>();

    private Set<MovementIssue> issues = new HashSet<MovementIssue>();

    public boolean redstoneInput = false, lastInput = false;
    private boolean firstTick = true;

    private boolean needsRestart = false;
    private boolean neededRestart = false;

    private boolean attemptingMove = false;
    private boolean scheduled = false;
    public double minMovementTicks = 20D;

    private double powerStorageSize = 1000000;
    private double storedPower = 0;

    private int cooldown = 0;

    private IMovingStructure structure;

    public TileMotor() {

        for (MotorSetting<?> setting : MotorSetting.ALL_SETTINGS)
            settings.put(setting, setting.getDefault(this));
    }

    public void init(IMotorModifier[] modifiers) {

        this.modifiers = modifiers;
    }

    @Override
    public IMotorModifier[] getModifiers() {

        return modifiers;
    }

    @Override
    public IMotorUpgradeData[] getUpgrades() {

        return upgrades;
    }

    public void setUpgrade(int slot, IMotorUpgrade upgrade, ItemStack item) {

        if (upgrade == null || item == null) {
            upgrades[slot] = null;
            notifyChange();
            return;
        }

        upgrades[slot] = new MotorUpgradeData(upgrade, item,
                upgrade instanceof IConfigurableMotorUpgrade<?> ? ((IConfigurableMotorUpgrade<?>) upgrade).createUpgradeData(this) : null);
        notifyChange();
    }

    public <T> T getSetting(MotorSetting<T> setting) {

        Map<MotorSetting<?>, Object> m = settings;
        if (!m.containsKey(setting))
            return null;
        return setting.cast(m.get(setting));
    }

    private boolean preventSettingFeedback = false;

    public <T> void setSetting(MotorSetting<T> setting, T value) {

        if (getWorld() != null && getWorld().isRemote) {
            NetworkHandler.instance().sendToServer(new PacketSetMotorSetting(this, setting, value));
        } else {
            settings.put(setting, value);
            if (!preventSettingFeedback) {
                onNeighborChange();
            }
        }
    }

    @Override
    public int getFace() {

        return face;
    }

    @Override
    public void setFace(int face) {

        this.face = face;
        notifyChange();
    }

    @Override
    public boolean rotate(int axis) {

        if (getWorld().isRemote)
            return true;

        IMovement movement = getMovement();
        if (movement == null)
            return false;
        if (!movement.rotate(axis, this))
            return false;
        notifyChange();
        return true;
    }

    @Override
    public double injectPower(double amount, boolean simulated) {

        double injected = Math.min(getEnergyBufferSize() - getEnergyBuffer(), amount);

        if (simulated)
            return injected;

        storedPower += injected;
        notifyChange();
        return injected;
    }

    @Override
    public double drainPower(double amount, boolean simulated) {

        double drained = Math.min(getEnergyBuffer(), amount);

        if (simulated)
            return drained;

        storedPower -= drained;
        notifyChange();
        return drained;
    }

    @Override
    public double getEnergyBuffer() {

        return storedPower;
    }

    @Override
    public double getEnergyBufferSize() {

        return powerStorageSize;
    }

    @Override
    public boolean isMoving() {

        return structure != null && structure.getTrajectory().getProgress(structure.getTicksMoved()) < 1;
    }

    @Override
    public boolean canMove() {

        return true;
    }

    @Override
    public synchronized boolean move(boolean simulated) {

        if (cooldown > 0)
            return false;
        if (structure != null || getWorld() == null || getWorld().isRemote)
            return false;

        IMovement movement = getMovement();
        if (movement == null)
            return false;

        if (lastInput != redstoneInput || needsRestart)
            neededRestart = needsRestart;

        attemptingMove = true;
        BlockPos position = new BlockPos(getX(), getY(), getZ());
        MovementData data = MovementHelper.findMovedBlocksAdv(getWorld(), position, movement, IgnoreMode.PASS_THROUGH, position);
        attemptingMove = false;

        movement = getMovement(data.blocks);
        if (movement == null)
            return false;

        Set<MovementIssue> oldIssues = issues;
        issues = MovementHelper.getMovementIssues(data, movement);

        if (data.blocks.size() == 0 || !issues.isEmpty()) {
            if (oldIssues.isEmpty() || !oldIssues.equals(issues))
                sendUpdate();
            return false;
        }

        int minMovementTime = -1;

        double totalConsumed = 0;
        for (IMovingBlock b : data.blocks) {
            IFrame frame = MovementRegistry.instance.getFrameAt(b.getWorld(), b.getPosition());
            if (frame != null)
                minMovementTime = Math.max(minMovementTime, frame.getMaterial().getMinMovementTime());

            totalConsumed += 100;
            if (b.getTileEntity() != null)
                totalConsumed += 200;
        }

        if (minMovementTime == -1)
            minMovementTime = 20;
        if (movement instanceof IMovementBlink)
            minMovementTime = 20;

        this.minMovementTicks = minMovementTime;
        double movementDuration = Math.max(getSetting(MotorSetting.MOVEMENT_DURATION), minMovementTime);
        if (movement instanceof IMovementBlink)
            movementDuration = 20;
        preventSettingFeedback = true;
        setSetting(MotorSetting.MOVEMENT_DURATION, movementDuration);
        preventSettingFeedback = false;

        sendUpdate();

        double actuallyConsumed = drainPower(totalConsumed, true);
        if (actuallyConsumed < totalConsumed)
            return false;

        if (!simulated) {
            drainPower(actuallyConsumed, false);

            structure = TrajectoryAPI.instance().startMoving(data.blocks,
                    movement.withSpeed(movementDuration == 0 ? 1 : movement.getSpeedMultiplier(data.blocks) / movementDuration),
                    EnumSet.of(TrajectoryFeature.MOVE_ENTITIES, TrajectoryFeature.MODIFIABLE), this);
            scheduled = false;
            cooldown = 4;
        }

        return true;
    }

    public Set<MovementIssue> getMovementIssues() {

        return issues;
    }

    @Override
    public void onStartMoving(IMovingStructure structure) {

        this.structure = structure;
    }

    @Override
    public void onFinishMoving(IMovingStructure structure) {

        if (this.structure == structure)
            this.structure = null;
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        return attemptingMove && side == getFace();
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        return side != getFace() && !isMoving();
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return false;
    }

    @Override
    public int getSideStickiness(World world, BlockPos position, int side) {

        return attemptingMove && side == getFace() ? Integer.MAX_VALUE : 0;
    }

    @Override
    public int getRequiredStickiness(World world, BlockPos position, int side) {

        return 0;
    }

    @Override
    public void validate() {

        super.validate();
        MotorCache.onLoad(this);
    }

    @Override
    public void invalidate() {

        super.invalidate();
        MotorCache.onUnload(this);
        onUnloaded();
    }

    @Override
    public void onChunkUnload() {

        super.onChunkUnload();
        MotorCache.onUnload(this);
        onUnloaded();
    }

    @Override
    public void updateEntity() {

        if (getWorld().isRemote)
            return;

        if (firstTick) {
            onFirstTick_();
            sendUpdate();
            firstTick = false;
        }

        update();

        if (cooldown > 0)
            cooldown--;

        if (scheduled) {
            scheduled = false;
            if (!isMoving() && canMove())
                if (!move(false) && !getSetting(MotorSetting.REDSTONE_PULSE))
                    scheduled = true;
        }
    }

    @Override
    public void update() {

    }

    public void onUnloaded() {

        if (structure != null)
            structure.finishMoving();
        onUnload();
    }

    @Override
    public void onUnload() {

    }

    @Override
    public void onNeighborChange() {

        if (getWorld().isRemote)
            return;

        lastInput = redstoneInput;
        redstoneInput = getSetting(MotorSetting.REDSTONE_MODE) == RedstoneMode.NONE
                || RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ()) > 0;
        if (getSetting(MotorSetting.REDSTONE_MODE) == RedstoneMode.INVERTED)
            redstoneInput = !redstoneInput;

        if (redstoneInput != lastInput)
            needsRestart = false;

        boolean hasBouncyUpgrade = false;
        for (int i = 0; i < 7; i++) {
            IMotorUpgradeData d = getUpgrades()[i];
            if (d != null && d.getUpgrade().getType().equals("bouncy")) {
                hasBouncyUpgrade = true;
                break;
            }
        }

        if (!isMoving() && (lastInput != redstoneInput || !getSetting(MotorSetting.REDSTONE_PULSE)) && (redstoneInput || hasBouncyUpgrade)
                && !needsRestart) {
            scheduled = true;
            move(false);
        }
        if (!redstoneInput)
            scheduled = false;
    }

    public void onFirstTick_() {

        onFirstTick();
        onNeighborChange();
    }

    @Override
    public void onFirstTick() {

    }

    @Override
    public boolean onActivated(EntityPlayer player, int side, Vector3 hit) {

        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        writeMotor(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        readMotor(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        writeMotor(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        readMotor(tag);
        markRender();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void writeMotor(NBTTagCompound tag) {

        tag.setInteger("face", face);

        NBTTagList upgr = new NBTTagList();
        for (IMotorUpgradeData u : upgrades) {
            NBTTagCompound t = new NBTTagCompound();
            if (u != null) {
                NBTTagCompound stack = new NBTTagCompound();
                u.getStack().writeToNBT(stack);
                t.setTag("stack", stack);

                t.setString("type", u.getUpgrade().getType());

                if (u.getUpgrade() instanceof IMotorUpgrade.IConfigurableMotorUpgrade) {
                    NBTTagCompound data = new NBTTagCompound();
                    ((IConfigurableMotorUpgrade) u.getUpgrade()).writeToNBT(data, u.getData());
                    t.setTag("data", data);
                }
            }
            upgr.appendTag(t);
        }
        tag.setTag("upgrades", upgr);

        NBTTagList sett = new NBTTagList();
        for (Entry<MotorSetting<?>, Object> e : settings.entrySet()) {
            NBTTagCompound t = new NBTTagCompound();
            ((MotorSetting) e.getKey()).writeToNBT(e.getValue(), t);
            t.setString("_name", e.getKey().getName());
            sett.appendTag(t);
        }
        tag.setTag("settings", sett);
        tag.setDouble("minMovTicks", minMovementTicks);

        NBTTagList iss = new NBTTagList();
        for (MovementIssue i : issues) {
            if (i.getPosition() == null) {
                System.err.println(i);
                continue;
            }
            NBTTagCompound t = new NBTTagCompound();
            i.writeToNBT(t);
            iss.appendTag(t);
        }
        tag.setTag("issues", iss);

        tag.setBoolean("redstoneInput", redstoneInput);
        tag.setBoolean("lastInput", lastInput);
        tag.setBoolean("needsRestart", needsRestart);
        tag.setBoolean("neededRestart", neededRestart);
        tag.setDouble("powerStorageSize", powerStorageSize);
        tag.setDouble("storedPower", storedPower);
        tag.setInteger("cooldown", cooldown);

        IMovement mov = getMovement();
        if (mov != null) {
            NBTTagCompound movement = new NBTTagCompound();
            mov.writeToNBT(movement);
            tag.setTag("movement", movement);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void readMotor(NBTTagCompound tag) {

        face = tag.getInteger("face");

        NBTTagList upgr = tag.getTagList("upgrades", new NBTTagCompound().getId());
        for (int i = 0; i < upgr.tagCount(); i++) {
            NBTTagCompound t = upgr.getCompoundTagAt(i);
            if (t.hasKey("type")) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(t.getCompoundTag("stack"));
                IMotorUpgrade upgrade = ModifierRegistry.instance.findMotorUpgrade(t.getString("type"));
                Object data = null;
                if (upgrade instanceof IMotorUpgrade.IConfigurableMotorUpgrade) {
                    data = ((IMotorUpgrade.IConfigurableMotorUpgrade) upgrade).createUpgradeData(this);
                    ((IMotorUpgrade.IConfigurableMotorUpgrade) upgrade).readFromNBT(t.getCompoundTag("data"), data);
                }
                upgrades[i] = new MotorUpgradeData(upgrade, stack, data);
            } else {
                upgrades[i] = null;
            }
        }

        NBTTagList sett = tag.getTagList("settings", new NBTTagCompound().getId());
        for (int i = 0; i < sett.tagCount(); i++) {
            NBTTagCompound t = sett.getCompoundTagAt(i);
            MotorSetting<?> s = MotorSetting.findSetting(t.getString("_name"));
            if (s == null)
                continue;
            Object val = s.readFromNBT(t);
            settings.put(s, val);
        }
        minMovementTicks = tag.getDouble("minMovTicks");

        NBTTagList iss = tag.getTagList("issues", new NBTTagCompound().getId());
        issues.clear();
        for (int i = 0; i < iss.tagCount(); i++)
            issues.add(MovementIssue.loadFromNBT(iss.getCompoundTagAt(i)));

        redstoneInput = tag.getBoolean("redstoneInput");
        lastInput = tag.getBoolean("lastInput");
        needsRestart = tag.getBoolean("needsRestart");
        neededRestart = tag.getBoolean("neededRestart");
        powerStorageSize = tag.getDouble("powerStorageSize");
        storedPower = tag.getDouble("storedPower");
        cooldown = tag.getInteger("cooldown");

        if (getMovement() != null)
            getMovement().readFromNBT(tag.getCompoundTag("movement"));
    }

    @Override
    public void setNeedsRestart() {

        needsRestart = true;
    }

    @Override
    public IMovement getMovement(BlockSet blocks) {

        return getMovement();
    }
}
