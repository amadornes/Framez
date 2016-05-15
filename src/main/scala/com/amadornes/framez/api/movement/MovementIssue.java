package com.amadornes.framez.api.movement;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.AxisAlignedBB;

import com.amadornes.trajectory.api.vec.BlockPos;

public class MovementIssue {

    public static final MovementIssue BLOCK = new MovementIssue("block", AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1));
    public static final MovementIssue FACE = new MovementIssue("face", AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 2 / 16D, 1));

    private final String type;
    private BlockPos position;
    private AxisAlignedBB aabb;
    private int face = -1, color = 0xFFFFFF;
    private String[] information = null;

    private MovementIssue(String type, AxisAlignedBB aabb) {

        this.type = type;
        this.aabb = aabb;
    }

    private MovementIssue(String type, AxisAlignedBB aabb, BlockPos position, int face, int color, String[] information) {

        this.type = type;
        this.aabb = aabb;
        this.position = position;
        this.face = face;
        this.color = color;
        this.information = information;
    }

    public MovementIssue at(BlockPos position) {

        MovementIssue issue = copy();
        issue.position = position.copy();
        return issue;
    }

    public MovementIssue onFace(int face) {

        MovementIssue issue = copy();
        issue.face = face;
        return issue;
    }

    public MovementIssue ofColor(int color) {

        MovementIssue issue = copy();
        issue.color = color;
        return issue;
    }

    public MovementIssue withInformation(String... information) {

        MovementIssue issue = copy();
        issue.information = Arrays.copyOf(information, information.length);
        return issue;
    }

    public MovementIssue copy() {

        return new MovementIssue(type, aabb.copy(), position != null ? position.copy() : null, face, color,
                information != null ? (String[]) Arrays.copyOf(information, information.length) : null);
    }

    public AxisAlignedBB getAABB() {

        return aabb;
    }

    public BlockPos getPosition() {

        return position;
    }

    public int getFace() {

        return face;
    }

    public int getColor() {

        return color;
    }

    public String[] getInformation() {

        return information;
    }

    @Override
    public int hashCode() {

        return getPosition().hashCode() * 31 + face * 17 + color;
    }

    public void writeToNBT(NBTTagCompound tag) {

        tag.setString("type", type);
        tag.setIntArray("position", getPosition().toIntArray());
        tag.setInteger("face", getFace());
        tag.setInteger("color", getColor());

        NBTTagList info = new NBTTagList();
        for (String s : getInformation())
            info.appendTag(new NBTTagString(s));
        tag.setTag("info", info);
    }

    public static MovementIssue loadFromNBT(NBTTagCompound tag) {

        String type = tag.getString("type");
        MovementIssue issue = type.equals("block") ? BLOCK : (type.equals("face") ? FACE : null);
        if (issue == null)
            return null;

        issue = issue.at(new BlockPos(tag.getIntArray("position")));
        issue = issue.onFace(tag.getInteger("face"));
        issue = issue.ofColor(tag.getInteger("color"));

        NBTTagList info = tag.getTagList("info", new NBTTagString().getId());
        String[] information = new String[info.tagCount()];
        for (int i = 0; i < info.tagCount(); i++)
            information[i] = info.getStringTagAt(i);
        issue = issue.withInformation(information);

        return issue;
    }
}
