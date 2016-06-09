package com.amadornes.framez.item;

import java.util.List;

import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.part.PartFrame;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemFrame extends ItemMultiPart implements IFramezItem {

    @Override
    public String getName() {

        return "frame";
    }

    @Override
    public boolean isBlock() {

        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return IFramezItem.super.getUnlocalizedName(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return TextFormatting.RED + "ERROR";
        IFrameMaterial material = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("border")));
        return I18n.format(getUnlocalizedName(stack) + ".name", I18n.format("framemat." + material.getType() + ".adj"));
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

        for (ResourceLocation material : FrameRegistry.INSTANCE.getMaterials().keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("border", material.toString());
            tag.setString("cross", material.toString());
            tag.setString("binding", material.toString());
            ItemStack stack = new ItemStack(item);
            stack.setTagCompound(tag);
            list.add(stack);
        }
    }

    @Override
    public boolean hasTooltip(ItemStack stack, EntityPlayer player, String unlocalized, String localized, boolean advanced) {

        return true;
    }

    @Override
    public void addTipInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, String tip, boolean advanced) {

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tooltip.add(I18n.format("framez.tooltip:error", TextFormatting.RED));
            return;
        }

        IFrameMaterial[] materials = new IFrameMaterial[3];
        if (tag.hasKey("border")) materials[0] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("border")));
        if (tag.hasKey("cross")) materials[1] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("cross")));
        if (tag.hasKey("binding")) materials[2] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("binding")));

        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.framez:frame.border") + ": " + TextFormatting.YELLOW
                + I18n.format("framemat." + materials[0].getType() + ".name"));
        tooltip.add(TextFormatting.DARK_AQUA + "  " + I18n.format("tooltip.framez:frame.maxblocks") + ": " + TextFormatting.GREEN
                + materials[0].getMaxCarriedBlocks());

        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.framez:frame.cross") + ": " + TextFormatting.YELLOW
                + I18n.format("framemat." + materials[1].getType() + ".name"));
        tooltip.add(TextFormatting.DARK_AQUA + "  " + I18n.format("tooltip.framez:frame.maxparts") + ": " + TextFormatting.GREEN
                + materials[1].getMaxCarriedParts());

        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.framez:frame.binding") + ": " + TextFormatting.YELLOW
                + I18n.format("framemat." + materials[2].getType() + ".name"));
        tooltip.add(TextFormatting.DARK_AQUA + "  " + I18n.format("tooltip.framez:frame.mintime") + ": " + TextFormatting.GREEN
                + formatTime(materials[2].getMinMovementTime()));
    }

    private String formatTime(int time) {

        if (time <= 0) return I18n.format("tooltip.framez:frame.mintime.warp", TextFormatting.DARK_PURPLE);
        return (time / 20D) + "s";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {

        IFramezItem.super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return new PartFrame();
        IFrameMaterial[] materials = new IFrameMaterial[3];
        if (tag.hasKey("border")) materials[0] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("border")));
        if (tag.hasKey("cross")) materials[1] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("cross")));
        if (tag.hasKey("binding")) materials[2] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("binding")));
        return new PartFrame(materials);
    }

}
