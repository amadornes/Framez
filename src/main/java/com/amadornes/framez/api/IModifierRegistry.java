package com.amadornes.framez.api;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IModifierRegistry {

    public void registerModifierProvider(IFrameModifierProvider provider);

    public IFrameModifierProvider[] getProviders();

    public ItemStack getFrameStack(int amount, String... modifiers);

    public ItemStack getFrameStack(String... modifiers);

    public IFrameModifierProvider[] getModifiers(ItemStack item);

    public List<ItemStack> getAllPossibleCombinations();

}
