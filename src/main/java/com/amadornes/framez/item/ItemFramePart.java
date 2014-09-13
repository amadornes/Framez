package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TMultiPart;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.init.CreativeTabFramez;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.part.PartFrame;
import com.amadornes.framez.ref.References;

public class ItemFramePart extends JItemMultiPart {

    public ItemFramePart() {

        setUnlocalizedName(References.FRAME_NAME);

        setCreativeTab(CreativeTabFramez.inst);
    }

    @Override
    public TMultiPart newPart(ItemStack item, EntityPlayer player, World world, BlockCoord block, int side, Vector3 hit) {

        PartFrame f = new PartFrame();

        if (item.stackTagCompound != null) {
            for (IFrameModifierProvider m : FramezApi.inst().getModifierRegistry().getModifiers(item)) {
                f.addModifier(m.instantiate(f));
            }
        }

        return f;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float f, float f2, float f3) {

        if (super.onItemUse(stack, player, w, x, y, z, side, f, f2, f3)) {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundTypeWood.getBreakSound(), Block.soundTypeWood.getVolume(),
                    Block.soundTypeWood.getPitch());
            return true;
        }
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {

        list.add(FramezApi.inst().getModifierRegistry().getFrameStack());
        list.addAll(FramezApi.inst().getModifierRegistry().getAllPossibleCombinations());
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return getUnlocalizedName();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean shift) {

        list.add(I18n.format("framez.hud.modifiers") + ":");
        IFrameModifierProvider[] modifiers = FramezApi.inst().getModifierRegistry().getModifiers(item);
        if (modifiers.length == 0) {
            list.add(" " + I18n.format("framez.hud.modifiers.none"));
        } else {
            for (IFrameModifierProvider m : modifiers)
                list.add(" - " + I18n.format(m.getUnlocalizedName(item)));
        }
    }

    @Override
    public int getDamage(ItemStack stack) {

        if (stack.stackTagCompound == null)
            return 0;

        int dmg = 0;
        List<ItemStack> l = ModifierRegistry.INST.getAllPossibleCombinations();
        synchronized (l) {
            for (ItemStack is : l) {
                if (is.stackTagCompound != null && stack.stackTagCompound != null && ItemStack.areItemStackTagsEqual(is, stack))
                    return dmg;
                dmg++;
            }
        }

        return 9999;
    }

}
