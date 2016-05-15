package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.wrench.IFramePart;
import com.amadornes.framez.api.wrench.IFramePart.IFramePartCenter;
import com.amadornes.framez.api.wrench.IFramePart.IFramePartFace;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.init.FramezCreativeTab;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;

public class ItemBlockFrame extends ItemBlockFramez {

    public BlockFrame b;

    public ItemBlockFrame(Block b) {

        super(b);
        this.b = (BlockFrame) b;
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return FramezCreativeTab.tab;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "Support Frame";
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        return StatCollector.translateToLocalFormatted(
                "tile." + ModInfo.MODID + ":frame.name",
                StatCollector.translateToLocal(FramezConfig.simple_mode ? "misc." + ModInfo.MODID + ":support" : "material."
                        + ModInfo.MODID + ":" + b.material.getType() + ".adj"));
    }

    @Override
    protected boolean hasTooltip(ItemStack stack, EntityPlayer player, String unlocalized, String localized) {

        return true;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void addTipInformation(ItemStack stack, EntityPlayer player, List l, String tip) {

        IFrame frame = FramezUtils.placementFrame;
        List<IFramePart> frameParts = FramezUtils.loadSilkyData(null, null, frame, stack);
        IFrameMaterial mat = frame.getMaterial();

        // Stats
        if (!FramezConfig.simple_mode) {
            int maxMovedBlocks = mat.getMaxMovedBlocks();
            int maxMultiparts = mat.getMaxMultiparts();
            int minMovementTime = mat.getMinMovementTime();

            l.add(EnumChatFormatting.YELLOW + I18n.format("tooltip." + ModInfo.MODID + ":frame.stats") + ":");
            l.add("  " + I18n.format("tooltip." + ModInfo.MODID + ":frame.stat.max_moved_blocks") + ": " + EnumChatFormatting.WHITE
                    + maxMovedBlocks);
            l.add("  " + I18n.format("tooltip." + ModInfo.MODID + ":frame.stat.max_multiparts") + ": " + EnumChatFormatting.WHITE
                    + maxMultiparts);
            l.add("  " + I18n.format("tooltip." + ModInfo.MODID + ":frame.stat.min_movement_time") + ": " + EnumChatFormatting.WHITE
                    + getFormattedMovementTime(minMovementTime));
        }

        String blockedSides = "", hiddenSides = "";
        for (int i = 0; i < 6; i++) {
            if (frame.isSideBlocked(i))
                blockedSides += I18n.format("misc." + ModInfo.MODID + ":side.short." + i);
            if (frame.isSideHidden(i))
                hiddenSides += I18n.format("misc." + ModInfo.MODID + ":side.short." + i);
        }
        if (blockedSides.length() == 0)
            blockedSides = I18n.format("misc." + ModInfo.MODID + ":none");
        if (hiddenSides.length() == 0)
            hiddenSides = I18n.format("misc." + ModInfo.MODID + ":none");

        l.add(EnumChatFormatting.YELLOW + I18n.format("tooltip." + ModInfo.MODID + ":frame.blocked") + ": " + EnumChatFormatting.WHITE
                + blockedSides);
        l.add(EnumChatFormatting.YELLOW + I18n.format("tooltip." + ModInfo.MODID + ":frame.hidden") + ": " + EnumChatFormatting.WHITE
                + hiddenSides);

        // Frame parts
        if (frameParts.isEmpty()) {
            l.add(EnumChatFormatting.RED + I18n.format("tooltip." + ModInfo.MODID + ":frame.noparts"));
        } else {
            if (!Framez.proxy.isCtrlDown()) {
                l.add(I18n.format("tooltip." + ModInfo.MODID + ":ctrl", EnumChatFormatting.GRAY, EnumChatFormatting.YELLOW + ""
                        + EnumChatFormatting.ITALIC, EnumChatFormatting.RESET + "" + EnumChatFormatting.GRAY));
            } else {
                l.add(EnumChatFormatting.YELLOW + I18n.format("tooltip." + ModInfo.MODID + ":frame.parts") + ":");
                for (IFramePart p : frameParts) {
                    String extra = null;
                    if (p instanceof IFramePartCenter)
                        extra = EnumChatFormatting.GRAY + "(" + I18n.format("misc." + ModInfo.MODID + ":direction.6") + ")";
                    else if (p instanceof IFramePartFace)
                        extra = EnumChatFormatting.GRAY + "("
                                + I18n.format("misc." + ModInfo.MODID + ":side." + ((IFramePartFace) p).getFace()) + ")";

                    l.add(" - " + EnumChatFormatting.WHITE + p.getDisplayName() + (extra != null ? " " + extra : ""));
                }
            }
        }
    }

    public static String getFormattedMovementTime(int ticks) {

        if (ticks == 0)
            return EnumChatFormatting.DARK_PURPLE
                    + StatCollector.translateToLocal("tooltip." + ModInfo.MODID + ":stat.frame.min_movement_time.warp");

        return (ticks / 20D) + "s";
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        BlockPos position = new BlockPos(x, y, z);
        double d = new Vector3(hitX, hitY, hitZ).getSide(side);
        if (!((d <= 0 && side % 2 == 0) || (d >= 1 && side % 2 == 1)) && FramezApi.instance().compat().placeFrame(world, position, stack)) {
            if (!player.capabilities.isCreativeMode)
                stack.stackSize--;
            return true;
        }
        position.offset(side);
        if (FramezApi.instance().compat().placeFrame(world, position, stack)) {
            if (!player.capabilities.isCreativeMode)
                stack.stackSize--;
            return true;
        }
        return false;
    }

    @Override
    public boolean func_150936_a(World p_150936_1_, int p_150936_2_, int p_150936_3_, int p_150936_4_, int p_150936_5_,
            EntityPlayer p_150936_6_, ItemStack p_150936_7_) {

        return true;
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List l) {

        super.getSubItems(item, tab, l);
    }
}