package com.amadornes.framez.api.movement;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.IWorldLocation;
import uk.co.qmunity.lib.vec.Vec3d;

import com.amadornes.framez.api.modifier.IFrameModifier;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFrame extends ISticky, IWorldLocation {

    public Collection<IFrameModifier> getModifiers();

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(RenderHelper renderer, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d pos, int pass, double frame);

    @SideOnly(Side.CLIENT)
    public void renderItem(ItemStack item, ItemRenderType type);

    @SideOnly(Side.CLIENT)
    public IIcon getBorderIcon();

    @SideOnly(Side.CLIENT)
    public IIcon getBorderPanelIcon();

    @SideOnly(Side.CLIENT)
    public IIcon getCrossIcon();

    @SideOnly(Side.CLIENT)
    public IIcon getSimpleIcon();

    public boolean is2D();

    public boolean canRenderInPass(int pass);

    public int getMaxMovedBlocks();

    public int getMaxMultiparts();

    public int getMultiparts();

    public int getMicroblock(ForgeDirection face);

    public boolean isSideHidden(ForgeDirection side);

    public void setSideHidden(ForgeDirection side, boolean hidden);

}