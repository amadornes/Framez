package com.amadornes.framez.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TMultiPart;

import com.amadornes.framez.api.modifier.IFrameModifierMaterial;
import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.FrameModifierRegistry;
import com.amadornes.framez.part.PartFrame;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

public class ItemPartFrame extends JItemMultiPart {

    public ItemPartFrame() {

        setUnlocalizedName(ModInfo.MODID + ":" + References.Item.FRAME);
    }

    @Override
    public TMultiPart newPart(ItemStack item, EntityPlayer player, World world, BlockCoord loc, int side, Vector3 hit) {

        NBTTagCompound tag = item.getTagCompound();

        if (tag == null)
            return null;
        if (!tag.hasKey("modifiers"))
            return null;

        NBTTagList l = tag.getTagList("modifiers", new NBTTagString().getId());
        List<IFrameModifier> mods = new ArrayList<IFrameModifier>();
        for (int i = 0; i < l.tagCount(); i++) {
            IFrameModifier mod = FrameModifierRegistry.instance().findModifier(l.getStringTagAt(i));
            if (mod != null)
                mods.add(mod);
        }

        return FrameFactory.createFrame(PartFrame.class, mods);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item i, CreativeTabs t, List l) {

        for (List<IFrameModifier> mods : FrameModifierRegistry.instance().getAllCombinations(PartFrame.class)) {
            NBTTagList tagList = new NBTTagList();
            for (IFrameModifier mod : mods)
                tagList.appendTag(new NBTTagString(mod.getType()));

            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("modifiers", tagList);

            ItemStack is = new ItemStack(this);
            is.setTagCompound(tag);
            l.add(is);
        }
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tip, boolean shift) {

        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null)
            return;
        if (!tag.hasKey("modifiers"))
            return;

        tip.add(I18n.format("tooltip." + ModInfo.MODID + ":modifiers") + ":");

        NBTTagList l = tag.getTagList("modifiers", new NBTTagString().getId());
        for (int i = 0; i < l.tagCount(); i++) {
            String type = l.getStringTagAt(i);
            IFrameModifier mod = FrameModifierRegistry.instance().findModifier(type);
            boolean found = mod != null;

            tip.add((!found ? EnumChatFormatting.RED : "")
                    + " - "
                    + I18n.format("tooltip." + ModInfo.MODID + ":modifier." + type + ".name")
                    + (mod != null && mod instanceof IFrameModifierMaterial ? " ["
                            + I18n.format("tooltip." + ModInfo.MODID + ":modifier.material") + "]" : ""));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null)
            return super.getUnlocalizedName(stack);
        if (!tag.hasKey("modifiers"))
            return super.getUnlocalizedName(stack);

        NBTTagList l = tag.getTagList("modifiers", new NBTTagString().getId());
        for (int i = 0; i < l.tagCount(); i++) {
            String type = l.getStringTagAt(i);
            IFrameModifier mod = FrameModifierRegistry.instance().findModifier(type);
            if (mod != null && mod instanceof IFrameModifierMaterial)
                return super.getUnlocalizedName(stack) + "." + type;
        }

        return super.getUnlocalizedName(stack);
    }

}
