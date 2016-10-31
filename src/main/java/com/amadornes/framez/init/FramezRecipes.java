package com.amadornes.framez.init;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.frame.FrameHelper;
import com.amadornes.framez.item.ItemFramePanel;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class FramezRecipes {

    public static void register() {

        RecipeSorter.register(ModInfo.MODID + ":framecross", RecipeFrameCross.class, Category.SHAPED,
                "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register(ModInfo.MODID + ":framepanel", RecipeFramePanel.class, Category.SHAPED,
                "after:minecraft:shaped before:minecraft:shapeless after:" + ModInfo.MODID + ":framecross");
        RecipeSorter.register(ModInfo.MODID + ":frame", RecipeFrame.class, Category.SHAPED,
                "after:minecraft:shaped before:minecraft:shapeless after:" + ModInfo.MODID + ":framepanel");

        GameRegistry.addRecipe(new RecipeFrameCross());
        GameRegistry.addRecipe(new RecipeFramePanel());
        GameRegistry.addRecipe(new RecipeFrame());

        GameRegistry.addRecipe(
                new ShapedOreRecipe(new ItemStack(FramezItems.wrench), "  I", " n ", "I  ", 'I', "ingotIron", 'n', "nuggetIron"));
    }

    private static final class RecipeFrameCross implements IRecipe {

        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {

            return getCraftingResult(inv) != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {

            if (inv.getWidth() != 3 && inv.getHeight() != 3) {
                return null;
            }

            IFrameMaterial cross = null, binding = null;
            for (int i = 0; i < 9; i++) {
                ItemStack s = inv.getStackInSlot(i);
                if (i == 4) {
                    binding = FrameHelper.getMaterial(s);
                    if (binding == null) {
                        return null;
                    }
                } else if (i % 2 == 0) {
                    if (cross == null) {
                        cross = FrameHelper.getMaterial(s);
                        if (cross == null) {
                            return null;
                        }
                    } else {
                        IFrameMaterial mat = FrameHelper.getMaterial(s);
                        if (mat != cross) {
                            return null;
                        }
                    }
                } else {
                    if (s != null) {
                        return null;
                    }
                }
            }

            ItemStack stack = new ItemStack(FramezItems.frame_panel, 1, 0);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("cross", cross.getType().toString());
            tag.setString("binding", binding.getType().toString());
            stack.setTagCompound(tag);
            return stack;
        }

        @Override
        public int getRecipeSize() {

            return 5;
        }

        @Override
        public ItemStack getRecipeOutput() {

            return null;
        }

        @Override
        public ItemStack[] getRemainingItems(InventoryCrafting inv) {

            return ForgeHooks.defaultRecipeGetRemainingItems(inv);
        }

    }

    private static final class RecipeFramePanel implements IRecipe {

        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {

            return getCraftingResult(inv) != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {

            if (inv.getWidth() != 3 && inv.getHeight() != 3) {
                return null;
            }

            ItemStack cross = null;
            IFrameMaterial border = null;
            for (int i = 0; i < 9; i++) {
                ItemStack s = inv.getStackInSlot(i);
                if (i == 4) {
                    if ((cross = s) == null) {
                        return null;
                    }
                } else if (i % 2 == 1) {
                    if (border == null) {
                        border = FrameHelper.getMaterial(s);
                        if (border == null) {
                            return null;
                        }
                    } else {
                        IFrameMaterial mat = FrameHelper.getMaterial(s);
                        if (mat != border) {
                            return null;
                        }
                    }
                } else {
                    if (s != null) {
                        return null;
                    }
                }
            }

            ItemStack stack = new ItemStack(FramezItems.frame_panel, 1, 1);
            NBTTagCompound tag = cross.getTagCompound().copy();
            tag.setString("border", border.getType().toString());
            stack.setTagCompound(tag);
            return stack;
        }

        @Override
        public int getRecipeSize() {

            return 5;
        }

        @Override
        public ItemStack getRecipeOutput() {

            return null;
        }

        @Override
        public ItemStack[] getRemainingItems(InventoryCrafting inv) {

            return ForgeHooks.defaultRecipeGetRemainingItems(inv);
        }

    }

    private static final class RecipeFrame implements IRecipe {

        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {

            return getCraftingResult(inv) != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {

            if (inv.getSizeInventory() < 6) {
                return null;
            }

            ItemStack panel = null;
            int count = 0;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack s = inv.getStackInSlot(i);
                if (s != null) {
                    if (panel == null) {
                        if (s.getItem() instanceof ItemFramePanel) {
                            panel = s;
                            count++;
                        }
                    } else {
                        if (!ItemStack.areItemsEqual(s, panel) || !ItemStack.areItemStackTagsEqual(s, panel)) {
                            return null;
                        }
                        count++;
                    }
                }
            }

            if (count != 6) {
                return null;
            }

            ItemStack stack = new ItemStack(FramezItems.frame, 6);
            stack.setTagCompound(panel.getTagCompound().copy());
            return stack;
        }

        @Override
        public int getRecipeSize() {

            return 6;
        }

        @Override
        public ItemStack getRecipeOutput() {

            return null;
        }

        @Override
        public ItemStack[] getRemainingItems(InventoryCrafting inv) {

            return ForgeHooks.defaultRecipeGetRemainingItems(inv);
        }

    }

}
