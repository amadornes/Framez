package com.amadornes.framez.api.modifier;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import com.amadornes.framez.api.movement.IFrameRenderData.IFrameTexture;
import com.amadornes.framez.api.movement.IModifiableFrame;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFrameSideModifier extends IModifier<IFrameSideModifier> {

    public boolean canApplyTo(IModifiableFrame frame, int side);

    public ItemStack getCraftingItem();

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(IBlockAccess world, BlockPos position, IModifiableFrame frame, int side, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(IBlockAccess world, BlockPos position, IModifiableFrame frame, int side, int pass, float f);

    @SideOnly(Side.CLIENT)
    public void renderItem(ItemStack stack, int side, ItemRenderType type);

    public static interface IFrameSideModifierOverlay extends IFrameSideModifier {

        @SideOnly(Side.CLIENT)
        public IFrameTexture getOverlay(IModifiableFrame frame, int type);

    }

}