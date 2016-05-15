package com.amadornes.framez.api.wrench;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFramePart {

    public String getType();

    public void writePickedToNBT(NBTTagCompound tag);

    public void readPickedFromNBT(NBTTagCompound tag);

    public int getPlacementPriority();

    public boolean canPlace(World world, BlockPos position);

    public boolean canPlaceWith(IFramePart[] parts);

    public void place(World world, BlockPos position);

    @SideOnly(Side.CLIENT)
    public void renderItem(ItemRenderType type);

    public String getDisplayName();

    public static interface IFramePartFace extends IFramePart {

        public int getFace();

    }

    public static interface IFramePartCenter extends IFramePart {

    }

    public static interface IFeatureDependantFramePart extends IFramePart {

        public String[] getRequiredFeatures();

        public String[] getIncompatibleFeatures();

    }

}
