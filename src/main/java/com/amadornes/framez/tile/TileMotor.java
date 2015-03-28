package com.amadornes.framez.tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.vec.IWorldLocation;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.IDebuggable;
import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;
import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.api.movement.IMovable;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.api.movement.MotorSetting;
import com.amadornes.framez.movement.MovementHelper;
import com.amadornes.framez.movement.MovementScheduler;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.PacketMotorSetting;
import com.amadornes.framez.util.MotorCache;
import com.amadornes.framez.util.ThreadBlockChecking;
import com.amadornes.framez.util.Timing;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public abstract class TileMotor extends TileEntity implements IMotor, IDebuggable, IMovable {

    protected MovingStructure structure;
    protected List<Vec3i> blocking = new ArrayList<Vec3i>();

    private boolean redstoneInput = false;
    private boolean scheduled = false;

    private ForgeDirection face = ForgeDirection.DOWN;
    private HashSet<MotorSetting> settings = new HashSet<MotorSetting>();
    private List<IMotorModifier> modifiers = new ArrayList<IMotorModifier>();

    private double powerStorageSize = 1000000;
    private double storedPower = 0;

    @Override
    public World getWorld() {

        return getWorldObj();
    }

    @Override
    public int getX() {

        return xCoord;
    }

    @Override
    public int getY() {

        return yCoord;
    }

    @Override
    public int getZ() {

        return zCoord;
    }

    @Override
    public Collection<IMotorModifier> getModifiers() {

        if (modifiers == null)
            modifiers = new ArrayList<IMotorModifier>();

        return modifiers;
    }

    @Override
    public boolean canWork() {

        return true;
    }

    @Override
    public boolean isWorking() {

        return structure != null && structure.getProgress() < 1;
    }

    @Override
    public boolean move() {

        try {

            if (getWorld() == null || getWorld().isRemote)
                return false;

            IMovement movement = getMovement();
            if (movement == null)
                return false;

            ForgeDirection face = getFace();

            Pair<List<MovingBlock>, List<Vec3i>> p = MovementHelper.findMovedBlocks(getWorld(), getX() + face.offsetX, getY()
                    + face.offsetY, getZ() + face.offsetZ, face.getOpposite(), movement);

            List<MovingBlock> blocks = p.getKey();
            blocks.remove(new MovingBlock(new Vec3i((IWorldLocation) this), null, null));

            if (blocks.size() == 0)
                return false;

            List<Vec3i> old = blocking;
            blocking = p.getValue();
            boolean send = false;
            if ((old.size() != blocking.size())) {
                send = true;
            } else {
                for (Vec3i v : old) {
                    if (!blocking.contains(v)) {
                        send = true;
                        break;
                    }
                }
                if (!send) {
                    for (Vec3i v : blocking) {
                        if (!old.contains(v)) {
                            send = true;
                            break;
                        }
                    }
                }
            }

            double totalConsumed = 0;
            for (MovingBlock b : blocks) {
                b.snapshot();
                int parts = b.getMultiparts();
                if (parts <= b.getMaxMultiparts()) {
                    totalConsumed += parts * 10;
                } else {
                    if (!blocking.contains(new Vec3i(b))) {
                        blocking.add(new Vec3i(b));
                        send = true;
                    }
                }
                totalConsumed += 100;
                if (b.getTileEntity() != null)
                    totalConsumed += 200;
            }

            if (send)
                sendUpdatePacket();
            if (blocking.size() > 0)
                return false;

            double actuallyConsumed = drainPower(totalConsumed, true);
            if (actuallyConsumed < totalConsumed)
                return false;
            drainPower(actuallyConsumed, false);

            Timing.SECONDS = 1;
            structure = new MovingStructure(this, movement.clone(), 1 / (20 * Timing.SECONDS), blocks);
            // structure.tick(Phase.START);
            // structure.tick(Phase.END);
            MovementScheduler.instance().addStructure(structure);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Set<MotorSetting> getSettings() {

        return settings;
    }

    @Override
    public void configure(MotorSetting setting) {

        if (getWorldObj().isRemote) {
            NetworkHandler.instance().sendToServer(new PacketMotorSetting(this, setting));
            return;
        }

        if (settings.contains(setting)) {
            settings.remove(setting);
        } else {
            for (MotorSetting s : setting.related)
                settings.remove(s);
            settings.add(setting);
        }

        sendUpdatePacket();

        onBlockUpdate();
    }

    private boolean firstTick = true;

    @Override
    public void updateEntity() {

        if (firstTick) {
            onFirstTick();
            firstTick = false;
        }

        tick();
    }

    @Override
    public void tick() {

        if (structure != null && structure.getProgress() >= 1)
            structure = null;

        if (scheduled) {
            if (!isWorking() && canWork())
                if (!move() && !getSettings().contains(MotorSetting.REDSTONE_PULSE))
                    ThreadBlockChecking.instance().addMotor(this);

            scheduled = false;
        }
    }

    @Override
    public void onFirstTick() {

    }

    public void onBlockUpdate() {

        boolean lastInput = redstoneInput;
        redstoneInput = RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ()) > 0;

        if ((lastInput != redstoneInput || !getSettings().contains(MotorSetting.REDSTONE_PULSE))
                && (getSettings().contains(MotorSetting.REDSTONE_INVERTED) ? !redstoneInput : redstoneInput)) {
            scheduled = true;
        } else {
            ThreadBlockChecking.instance().removeMotor(this);
        }
    }

    @Override
    public ForgeDirection getFace() {

        return face;
    }

    public boolean setFace(ForgeDirection face) {

        this.face = face;
        sendUpdatePacket();
        return true;
    }

    public boolean rotate(ForgeDirection axis) {

        if (getMovement().rotate(this, axis)) {
            sendUpdatePacket();
            return true;
        }

        return true;
    }

    @Override
    public boolean debug(World world, int x, int y, int z, ForgeDirection face, EntityPlayer player) {

        if (!world.isRemote)
            return true;

        player.addChatMessage(new ChatComponentText("Power: " + getEnergyBuffer() + "/" + getEnergyBufferSize()));
        player.addChatMessage(new ChatComponentText("Face: " + getFace().name().toLowerCase()));
        getMovement().debug(world, x, y, z, face, player);

        return true;
    }

    public MovingStructure getStructure() {

        return structure;
    }

    public void setStructure(MovingStructure structure) {

        this.structure = structure;
    }

    @Override
    public double getEnergyBufferSize() {

        return powerStorageSize;
    }

    @Override
    public double getEnergyBuffer() {

        return storedPower;
    }

    @Override
    public double injectPower(double amount, boolean simulated) {

        double injected = Math.min(getEnergyBufferSize() - getEnergyBuffer(), amount);

        if (simulated)
            return injected;

        storedPower += injected;

        sendUpdatePacket();

        return injected;
    }

    @Override
    public double drainPower(double amount, boolean simulated) {

        double drained = Math.min(getEnergyBuffer(), amount);

        if (simulated)
            return drained;

        storedPower -= drained;

        sendUpdatePacket();

        return drained;
    }

    // NBT saving and tile updates

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        tag.setBoolean("redstoneInput", redstoneInput);

        tag.setInteger("face", getFace().ordinal());

        NBTTagCompound movement = new NBTTagCompound();
        getMovement().writeToNBT(movement);
        tag.setTag("movement", movement);

        NBTTagList l = new NBTTagList();
        for (MotorSetting s : settings)
            l.appendTag(new NBTTagString(s.ordinal() + ""));
        tag.setTag("settings", l);

        tag.setDouble("power", getEnergyBuffer());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        redstoneInput = tag.getBoolean("redstoneInput");

        face = ForgeDirection.getOrientation(tag.getInteger("face"));

        getMovement().readFromNBT(tag.getCompoundTag("movement"));

        settings.clear();
        NBTTagList l = tag.getTagList("settings", new NBTTagString().getId());
        for (int i = 0; i < l.tagCount(); i++)
            settings.add(MotorSetting.values()[Integer.parseInt(l.getStringTagAt(i))]);

        storedPower = tag.getDouble("power");
    }

    protected void writeToPacketNBT(NBTTagCompound tag) {

        tag.setInteger("face", getFace().ordinal());

        NBTTagCompound movement = new NBTTagCompound();
        getMovement().writeToNBT(movement);
        tag.setTag("movement", movement);

        NBTTagList l = new NBTTagList();
        for (MotorSetting s : settings)
            l.appendTag(new NBTTagString(s.ordinal() + ""));
        tag.setTag("settings", l);

        if (blocking.size() > 0) {
            NBTTagList blocking = new NBTTagList();
            for (Vec3i v : this.blocking) {
                NBTTagCompound t = new NBTTagCompound();
                t.setInteger("x", v.getX());
                t.setInteger("y", v.getY());
                t.setInteger("z", v.getZ());
                blocking.appendTag(t);
            }
            tag.setTag("blocking", blocking);
        }

        tag.setDouble("power", getEnergyBuffer());
    }

    protected void readFromPacketNBT(NBTTagCompound tag) {

        face = ForgeDirection.getOrientation(tag.getInteger("face"));

        getMovement().readFromNBT(tag.getCompoundTag("movement"));

        settings.clear();
        NBTTagList l = tag.getTagList("settings", new NBTTagString().getId());
        for (int i = 0; i < l.tagCount(); i++)
            settings.add(MotorSetting.values()[Integer.parseInt(l.getStringTagAt(i))]);

        blocking = new ArrayList<Vec3i>();
        if (tag.hasKey("blocking")) {
            NBTTagList blocking = tag.getTagList("blocking", new NBTTagCompound().getId());
            for (int i = 0; i < blocking.tagCount(); i++) {
                NBTTagCompound t = blocking.getCompoundTagAt(i);
                this.blocking.add(new Vec3i(t.getInteger("x"), t.getInteger("y"), t.getInteger("z")));
            }
        }

        storedPower = tag.getDouble("power");

        markForRenderUpdate();
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tCompound = new NBTTagCompound();
        writeToPacketNBT(tCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        readFromPacketNBT(pkt.func_148857_g());
    }

    protected void sendUpdatePacket() {

        if (!worldObj.isRemote)
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    protected void markForRenderUpdate() {

        if (worldObj != null)
            worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    protected void notifyNeighborBlockUpdate() {

        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
    }

    @Override
    @Priority(PriorityEnum.OVERRIDE)
    public BlockMovementType getMovementType(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        if (side == getFace())
            return BlockMovementType.UNMOVABLE;

        return !isWorking() ? BlockMovementType.MOVABLE : BlockMovementType.UNMOVABLE;
    }

    @Override
    public boolean startMoving(IMovingBlock block) {

        return false;
    }

    @Override
    public boolean finishMoving(IMovingBlock block) {

        return false;
    }

    @Override
    public void onUnload() {

        MovingStructure s = getStructure();
        if (s != null)
            while (s.getProgress() < 1) {
                s.tick(Phase.START);
                s.tick(Phase.END);
            }
    }

    public List<Vec3i> getBlocking() {

        return blocking;
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
        onUnload();
    }

}
