package com.amadornes.framez.tile;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.api.movement.IFrameMove;
import com.amadornes.framez.config.Config;
import com.amadornes.framez.movement.MovementUtils;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.movement.StructureTickHandler;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketStartMoving;
import com.amadornes.framez.util.PowerHelper.PowerUnit;
import com.amadornes.framez.world.WorldWrapperProvider;

public abstract class TileMotor extends TileEntity implements IFrameMove {

    public abstract boolean shouldMove();

    public abstract boolean hasEnoughPower(double power);

    public abstract double getMovementSpeed();

    public abstract PowerUnit getPowerUnit();

    public abstract void consumePower(double power);

    public Object getExtraInfo() {

        return null;
    }

    private ForgeDirection face = ForgeDirection.DOWN;

    private ForgeDirection direction = ForgeDirection.SOUTH;

    private MovingStructure structure = null;

    private String placer = "";

    private boolean canMove = false;
    private List<BlockCoord> movedBlocks = null;
    private int lastCheck = 0;

    public ForgeDirection getFace() {

        return face;
    }

    public ForgeDirection getDirection() {

        return direction;
    }

    public boolean setFace(ForgeDirection face) {

        return setFace(face, false);
    }

    public boolean setFace(ForgeDirection face, boolean force) {

        if (structure != null)
            return false;

        if (face == null)
            return false;

        if ((face == direction || face == direction.getOpposite()) && !force)
            return false;

        this.face = face;
        sendUpdatePacket();

        return true;
    }

    public boolean setDirection(ForgeDirection direction) {

        return setDirection(direction, false);
    }

    public boolean setDirection(ForgeDirection direction, boolean force) {

        if (structure != null)
            return false;

        if (direction == null)
            return false;

        if ((direction == face || direction == face.getOpposite()) && !force)
            return false;

        this.direction = direction;
        sendUpdatePacket();

        return true;
    }

    public double getMoved() {

        return structure == null ? 0 : structure.getMoved();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        writeUpdatePacket(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        readUpdatePacket(tag);
    }

    public void writeUpdatePacket(NBTTagCompound tag) {

        tag.setInteger("face", getFace().ordinal());
        tag.setInteger("direction", getDirection().ordinal());

        tag.setString("placer", placer);
    }

    public void readUpdatePacket(NBTTagCompound tag) {

        face = ForgeDirection.getOrientation(tag.getInteger("face"));
        direction = ForgeDirection.getOrientation(tag.getInteger("direction"));

        placer = tag.getString("placer");
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tag = new NBTTagCompound();
        writeUpdatePacket(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        readUpdatePacket(pkt.func_148857_g());

        getWorldObj().markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    public void sendUpdatePacket() {

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!worldObj.isRemote) {
            lastCheck++;
            if (lastCheck == 10) {
                lastCheck = 0;
                checkIfCanMove();
            }
        }

        if (shouldMove())
            move();

        if (structure != null && structure.getMoved() >= 1)
            structure = null;
    }

    public MovingStructure getStructure() {

        return structure;
    }

    public void setStructure(MovingStructure structure) {

        this.structure = structure;
    }

    @Override
    public boolean canBeMoved(ForgeDirection face, ForgeDirection direction) {

        return structure == null && face != getFace();
    }

    public boolean move() {

        if (worldObj.isRemote)
            return false;
        if (structure != null)
            return false;

        if (lastCheck > 0)
            checkIfCanMove();
        if (canMove) {
            List<BlockCoord> blocks = movedBlocks;
            double power = 0;
            power += Config.Power.getPowerUsedPerMove;
            for (BlockCoord b : blocks) {
                power += Config.Power.getPowerUsedPerBlock;
                if (getWorldObj().getTileEntity(b.x, b.y, b.z) != null)
                    power += Config.Power.getPowerUsedPerTileEntity;
            }
            if (getPowerUnit() != null) {
                power = (power / PowerUnit.RF.getPowerMultiplier()) * getPowerUnit().getPowerMultiplier();
            }
            if (hasEnoughPower(power)) {
                MovingStructure structure = new MovingStructure(worldObj, direction, getMovementSpeed() / (20D * 0.75D));
                structure.addBlocks(blocks);

                this.structure = structure;
                StructureTickHandler.INST.addStructure(structure);

                NetworkHandler.sendToDimension(new PacketStartMoving(this), worldObj.provider.dimensionId);

                consumePower(power);

                return true;
            }
        }
        return false;
    }

    private void checkIfCanMove() {

        canMove = false;
        movedBlocks = null;

        if (worldObj == WorldWrapperProvider.getWrapper(worldObj.provider.dimensionId))
            return;

        if (worldObj.getBlock(xCoord + face.offsetX, yCoord + face.offsetY, zCoord + face.offsetZ) != Blocks.air) {
            List<BlockCoord> blocks = movedBlocks = MovementUtils.getMovedBlocks(this);
            if (blocks.size() > 0 && MovementUtils.canMove(blocks, getWorldObj(), direction)) {
                canMove = true;
                return;
            }
        }
        movedBlocks = null;
    }

    public boolean canMove() {

        return canMove;
    }

    public final int getColorMultiplier() {

        return getColorMultiplierForPlayer(placer);
    }

    public static final int getColorMultiplierForPlayer(String placer) {

        if (placer.equals("amadornes"))
            return 0xCC0000;

        if (placer.equals("KrystalRaven"))
            return 0x5100B3;

        if (placer.equals("Quetzz"))
            return 0x441E94;

        if (placer.equals("PurpleMentat"))
            return 0xAA00AA;

        if (placer.equals("Aureylian"))
            return 0xEEAAAA;

        if (placer.equals("Loneztar"))
            return 0xEEAAAA;

        return 0xAA0000;
    }

    public void setPlacer(String placer) {

        this.placer = placer;
    }

    public String getPlacer() {

        return placer;
    }

    protected boolean isBeingPowered() {

        return worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0 || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
    }

    @Override
    public void invalidate() {

        super.invalidate();

        onUnload();
    }

    public void onUnload() {

        if (structure != null) {
            structure.finishMoving();
            StructureTickHandler.INST.removeStructure(structure);
            structure = null;
        }
    }

}
