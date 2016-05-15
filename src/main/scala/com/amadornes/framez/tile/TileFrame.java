package com.amadornes.framez.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IModifiableFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.IFramezFrame;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;

public class TileFrame extends TileFramez implements IFramezFrame {

    private IFrameMaterial material;
    private IFrameSideModifier[][] sideModifiers = new IFrameSideModifier[6][0];

    private boolean[] blocked = new boolean[6];

    @Override
    public void init(IFrameMaterial material) {

        this.material = material;
    }

    @Override
    public IFrameMaterial getMaterial() {

        return material;
    }

    @Override
    public IFrameSideModifier[] getSideModifiers(int side) {

        return sideModifiers[side];
    }

    @Override
    public int getMultipartCount() {

        return 0;
    }

    @Override
    public void addSideModifier(int side, IFrameSideModifier modifier) {

        sideModifiers[side] = FramezUtils.addElement(sideModifiers[side], modifier);
    }

    @Override
    public void removeSideModifier(int side, IFrameSideModifier modifier) {

        sideModifiers[side] = FramezUtils.removeElement(sideModifiers[side], modifier);
    }

    @Override
    public boolean canHaveCovers() {

        return false;
    }

    @Override
    public boolean hasPanel(int side) {

        return false;
    }

    @Override
    public boolean shouldRenderCross(int side) {

        return true;
    }

    @Override
    public boolean isSideBlocked(int side) {

        return blocked[side];
    }

    public boolean setSideBlocked(int side, boolean blocked) {

        if (this.blocked[side] == blocked)
            return false;

        this.blocked[side] = blocked;
        notifyChange();
        return true;
    }

    @Override
    public boolean isSideHidden(int side) {

        return false;
    }

    public boolean setSideHidden(int side, boolean hidden) {

        return false;
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        return !isSideBlocked(side);
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        return isSideSticky(world, position, side, movement);
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public void onNeighborChanged() {

    }

    @Override
    public boolean onActivated(EntityPlayer player, int side, Vector3 hit) {

        return false;
    }

    @Override
    public void cloneFrame(IFrame frame) {

        for (int side = 0; side < 6; side++) {
            if (frame instanceof IModifiableFrame) {
                // Copy side modifiers (re-instantiate instantiable ones and just copy the rest)
                IFrameSideModifier[] frameSideModifiers = ((IModifiableFrame) frame).getSideModifiers(side);
                sideModifiers[side] = new IFrameSideModifier[frameSideModifiers.length];
                for (int i = 0; i < frameSideModifiers.length; i++)
                    sideModifiers[side][i] = frameSideModifiers[i];
            }

            // Copy blocked sides
            blocked[side] = frame.isSideBlocked(side);
        }
    }

    @Override
    public void harvest() {

        getWorld().setBlockToAir(getX(), getY(), getZ());
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        writeFrame(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        readFrame(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        writeFrame(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        readFrame(tag);
    }

    @Override
    public void writeFrame(NBTTagCompound tag) {

        tag.setString("material", material.getType());

        NBTTagList sideMods = new NBTTagList();
        for (IFrameSideModifier[] sMods : sideModifiers) {
            NBTTagList side = new NBTTagList();
            for (IFrameSideModifier m : sMods)
                side.appendTag(new NBTTagString(m.getType()));
            NBTTagCompound t = new NBTTagCompound();
            t.setTag("l", side);
            sideMods.appendTag(t);
        }
        tag.setTag("sideModifiers", sideMods);

        tag.setIntArray("blocked", new int[] { blocked[0] ? 1 : 0, blocked[1] ? 1 : 0, blocked[2] ? 1 : 0, blocked[3] ? 1 : 0,
                blocked[4] ? 1 : 0, blocked[5] ? 1 : 0 });
    }

    @Override
    public void readFrame(NBTTagCompound tag) {

        material = ModifierRegistry.instance.findFrameMaterial(tag.getString("material"));

        NBTTagList sideMods = tag.getTagList("sideModifiers", new NBTTagCompound().getId());
        for (int i = 0; i < sideMods.tagCount(); i++) {
            NBTTagList side = sideMods.getCompoundTagAt(i).getTagList("l", new NBTTagString().getId());
            sideModifiers[i] = new IFrameSideModifier[side.tagCount()];
            for (int j = 0; j < side.tagCount(); j++)
                sideModifiers[i][j] = ModifierRegistry.instance.findFrameSideModifier(side.getStringTagAt(j));
        }

        int[] blockedSides = tag.getIntArray("blocked");
        for (int i = 0; i < blockedSides.length; i++)
            blocked[i] = blockedSides[i] == 1;
    }
}
