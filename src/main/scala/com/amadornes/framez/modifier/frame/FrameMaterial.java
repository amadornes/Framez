package com.amadornes.framez.modifier.frame;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.commons.lang3.StringUtils;

import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IFrameRenderData;
import com.amadornes.framez.api.movement.IFrameRenderData.IFrameTexture;
import com.amadornes.framez.client.FrameTexture;
import com.amadornes.framez.client.IconSupplier;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FrameMaterial implements IFrameMaterial {

    private final Object nugget;
    private final String type;
    private final int maxMovedBlocks, maxMultiparts, minMovementTime;

    @SideOnly(Side.CLIENT)
    private IFrameTexture texture;

    public FrameMaterial(Object nugget, String type, int maxMovedBlocks, int maxMultiparts, int minMovementTime) {

        this.nugget = nugget;
        this.type = type;
        this.maxMovedBlocks = maxMovedBlocks;
        this.maxMultiparts = maxMultiparts;
        this.minMovementTime = minMovementTime;
    }

    public FrameMaterial(String type, int maxMovedBlocks, int maxMultiparts, int minMovementTime) {

        this("nugget" + StringUtils.capitalize(type), type, maxMovedBlocks, maxMultiparts, minMovementTime);
    }

    @Override
    public void registerRecipes(ItemStack modified) {

        Item craftingItem = FramezItems.crafting.get(this);
        if (craftingItem == null)
            return;
        Object nugget = getNugget();
        if (nugget == null)
            return;

        // Cross
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(craftingItem, 4, 0), "n n", " n ", "n n", 'n', nugget));
        // Panel
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(craftingItem, 1, 1), "nnn", "ncn", "nnn", 'n', nugget, 'c', new ItemStack(
                craftingItem, 1, 0)));
        // Frame
        ItemStack panel = new ItemStack(craftingItem, 1, 1);
        ItemStack modifiedx6 = modified.copy();
        modifiedx6.stackSize = 6;
        GameRegistry.addShapelessRecipe(modifiedx6, panel, panel, panel, panel, panel, panel);

        // Frame recycling recipes
        GameRegistry.addRecipe(new RecipeMaterialRecycle(this, modified));
        GameRegistry.addRecipe(new RecipeMaterialRecycle(this, panel));
    }

    public Object getNugget() {

        return nugget;
    }

    @Override
    public String getType() {

        return type;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerTextures() {

        IconSupplier.textures.add((FrameTexture) (texture = new FrameTexture(ModInfo.MODID + ":frame/" + getType())));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IFrameTexture getTexture(IFrameRenderData renderData, int side, int type) {

        return texture;
    }

    @Override
    public int getMaxMovedBlocks() {

        return maxMovedBlocks;
    }

    @Override
    public int getMaxMultiparts() {

        return maxMultiparts;
    }

    @Override
    public int getMinMovementTime() {

        return minMovementTime;
    }

    public static class RecipeMaterialRecycle implements IRecipe {

        private ItemStack frame;
        private List<ItemStack> results = new ArrayList<ItemStack>();

        public RecipeMaterialRecycle(FrameMaterial material, ItemStack frame) {

            this.frame = frame;

            Object in = material.getNugget();
            if (in instanceof ItemStack)
                results.add(((ItemStack) in).copy());
            else if (in instanceof Item)
                results.add(new ItemStack((Item) in));
            else if (in instanceof Block)
                results.add(new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
            else if (in instanceof String)
                results.addAll(OreDictionary.getOres((String) in));
            else
                throw new IllegalStateException("Invalid nugget material!");
        }

        @Override
        public boolean matches(InventoryCrafting inv, World world) {

            int slots = 0;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack is = inv.getStackInSlot(i);
                if (is == null)
                    continue;
                if (!is.isItemEqual(frame))
                    return false;
                slots++;
            }
            return slots == getRecipeSize();
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {

            return getRecipeOutput();
        }

        @Override
        public int getRecipeSize() {

            return 4;
        }

        @Override
        public ItemStack getRecipeOutput() {

            if (results.size() == 0)
                return null;
            ItemStack result = results.get(0).copy();
            result.stackSize = 37;
            return result;
        }

    }
}
