package com.amadornes.framez;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.api.IModifierRegistry;
import com.amadornes.framez.client.IconProvider;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.util.SorterElementCount;
import com.amadornes.framez.util.SorterModifierProvider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModifierRegistry implements IModifierRegistry {

    public static final ModifierRegistry INST = new ModifierRegistry();

    private List<IFrameModifierProvider> providers = new ArrayList<IFrameModifierProvider>();

    private ModifierRegistry() {

    }

    @Override
    public void registerModifierProvider(IFrameModifierProvider provider) {

        if (provider == null)
            return;

        if (getModifier(provider.getIdentifier()) != null)
            return;

        providers.add(provider);
    }

    @Override
    public IFrameModifierProvider[] getProviders() {

        return providers.toArray(new IFrameModifierProvider[0]);
    }

    @Override
    public IFrameModifierProvider getModifierProvider(String modifier) {

        for (IFrameModifierProvider p : getProviders())
            if (p.getIdentifier().equals(modifier))
                return p;

        return null;
    }

    @Override
    public ItemStack getFrameStack(int amount, String... modifiers) {

        ItemStack item = new ItemStack(FramezItems.item_frame_part);

        NBTTagCompound tag = item.stackTagCompound = new NBTTagCompound();

        NBTTagList list = new NBTTagList();

        for (String s : modifiers) {
            boolean valid = false;
            for (IFrameModifierProvider p : providers) {
                if (p.getIdentifier().equals(s)) {
                    valid = true;
                    break;
                }
            }
            if (valid)
                list.appendTag(new NBTTagString(s));
        }

        tag.setTag("modifiers", list);

        return item;
    }

    @Override
    public ItemStack getFrameStack(String... modifiers) {

        return getFrameStack(1, modifiers);
    }

    @Override
    public IFrameModifierProvider[] getModifiers(ItemStack item) {

        List<IFrameModifierProvider> modifiers = new ArrayList<IFrameModifierProvider>();

        if (item.stackTagCompound != null) {
            NBTTagCompound tag = item.stackTagCompound;
            NBTTagList list = tag.getTagList("modifiers", 8);
            for (int i = 0; i < list.tagCount(); i++)
                modifiers.add(getModifier(list.getStringTagAt(i)));
        }

        return modifiers.toArray(new IFrameModifierProvider[0]);
    }

    public IFrameModifierProvider getModifier(String modifier) {

        for (IFrameModifierProvider p : providers)
            if (p.getIdentifier().equals(modifier))
                return p;

        return null;
    }

    private List<ItemStack> combinations = null;

    @Override
    public List<ItemStack> getAllPossibleCombinations() {

        if (combinations == null) {
            combinations = new ArrayList<ItemStack>();

            List<List<IFrameModifierProvider>> possibilities = new ArrayList<List<IFrameModifierProvider>>();

            for (IFrameModifierProvider p : providers) {
                List<List<IFrameModifierProvider>> pos = new ArrayList<List<IFrameModifierProvider>>();
                pos.add(Arrays.asList(new IFrameModifierProvider[] { p }));
                addModifiers(pos, Arrays.asList(new IFrameModifierProvider[] { p }));

                for (List<IFrameModifierProvider> l : pos) {
                    boolean found = false;

                    for (List<IFrameModifierProvider> l2 : possibilities) {
                        Collections.sort(l, new SorterModifierProvider());
                        Collections.sort(l2, new SorterModifierProvider());
                        found = l.equals(l2);
                        if (found)
                            break;
                    }

                    if (!found)
                        possibilities.add(l);
                }
            }

            Collections.sort(possibilities, new SorterElementCount());

            for (List<IFrameModifierProvider> possibility : possibilities)
                combinations.add(getFrameStack(getIdentifiers(possibility)));
        }

        return combinations;
    }

    private void addModifiers(List<List<IFrameModifierProvider>> possibilities, List<IFrameModifierProvider> current) {

        for (IFrameModifierProvider p : providers) {
            if (current.contains(p))
                continue;
            if (!areCompatible(current, p))
                continue;

            List<IFrameModifierProvider> l = new ArrayList<IFrameModifierProvider>();
            l.addAll(current);
            l.add(p);
            possibilities.add(l);

            addModifiers(possibilities, l);
        }
    }

    private boolean areCompatible(List<IFrameModifierProvider> mods, IFrameModifierProvider m) {

        if (!m.isCompatibleWith(getIdentifiers(mods)))
            return false;

        if (m.overridesBorderTexture())
            for (IFrameModifierProvider mod : mods)
                if (mod.overridesBorderTexture() && mod.overridePriorityBorder() == m.overridePriorityBorder())
                    return false;
        if (m.overridesCrossTexture())
            for (IFrameModifierProvider mod : mods)
                if (mod.overridesCrossTexture() && mod.overridePriorityCross() == m.overridePriorityCross())
                    return false;

        return true;
    }

    private String[] getIdentifiers(List<IFrameModifierProvider> modifiers) {

        List<String> identifiers = new ArrayList<String>();

        for (IFrameModifierProvider p : modifiers)
            identifiers.add(p.getIdentifier());

        return identifiers.toArray(new String[0]);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getBorderTexture(List<IFrameModifier> modifiers, ForgeDirection face) {

        IIcon icon = null;
        int priority = Integer.MIN_VALUE;

        for (IFrameModifier m : modifiers) {
            if (m.getProvider().overridesBorderTexture() && m.getProvider().overridePriorityBorder() > priority) {
                icon = m.getBorderTexture(face);
                if (icon == null)
                    icon = IconProvider.iconNothing;
                priority = m.getProvider().overridePriorityBorder();
            }
        }

        return icon;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getCrossTexture(List<IFrameModifier> modifiers, ForgeDirection face) {

        IIcon icon = null;
        int priority = Integer.MIN_VALUE;

        for (IFrameModifier m : modifiers) {
            if (m.getProvider().overridesCrossTexture() && m.getProvider().overridePriorityCross() > priority) {
                icon = m.getCrossTexture(face);
                if (icon == null)
                    icon = IconProvider.iconNothing;
                priority = m.getProvider().overridePriorityCross();
            }
        }

        return icon;
    }

}
