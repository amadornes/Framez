package com.amadornes.framez.init;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.wrench.IFramePart;
import com.amadornes.framez.api.wrench.IFramePart.IFramePartCenter;
import com.amadornes.framez.api.wrench.IFramePart.IFramePartFace;
import com.amadornes.framez.api.wrench.IFramePartHandler;
import com.amadornes.framez.item.ItemBlockFrame;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.modifier.MotorFactory;
import com.amadornes.framez.modifier.frame.FrameMaterial.RecipeMaterialRecycle;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.common.registry.GameRegistry;

public class FramezRecipes {

    public static void register() {

        ItemStack metamorphic_stone = new ItemStack(FramezBlocks.metamorphic_stone, 1, 0);
        ItemStack metamorphic_stone_cracked = new ItemStack(FramezBlocks.metamorphic_stone, 1, 1);

        if (FramezConfig.craftable_metamorphic_stone)
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(FramezBlocks.metamorphic_stone, 1, 1), Blocks.obsidian,
                    Blocks.obsidian, Blocks.obsidian, "stone", "stone", "stone"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(FramezBlocks.metamorphic_stone, 4, 2), "ss", "ss", 's', metamorphic_stone));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezBlocks.metamorphic_stone, 1, 3), "sbs", "bcb", "sbs", 's',
                metamorphic_stone, 'c', metamorphic_stone_cracked, 'b', Items.water_bucket));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezBlocks.metamorphic_stone, 1, 4), "sbs", "bcb", "sbs", 's',
                metamorphic_stone, 'c', metamorphic_stone_cracked, 'b', Items.lava_bucket));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezBlocks.metamorphic_stone, 1, 5), "sis", "ici", "sis", 's',
                metamorphic_stone, 'c', metamorphic_stone_cracked, 'i', Blocks.ice));

        GameRegistry.addRecipe(new ShapelessOreRecipe(metamorphic_stone, metamorphic_stone_cracked, Blocks.obsidian, "stone"));

        String ni = "nuggetIron";
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.iron_ingot, ni, ni, ni, ni, ni, ni, ni, ni, ni));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(FramezItems.iron_nugget, 9), "ingotIron"));

        GameRegistry.addRecipe(new ShapedOreRecipe(FramezItems.wrench, " i ", " si", "n  ", 'i', "ingotIron", 's', "stickWood", 'n',
                "nuggetIron"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezItems.motor_core, 1, 0), "rir", "isi", "rir", 'r',
                Blocks.redstone_block, 'i', "ingotIron", 's', metamorphic_stone));
        GameRegistry.addShapelessRecipe(new ItemStack(FramezItems.motor_core, 1, 1), new ItemStack(FramezItems.motor_core, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(FramezItems.motor_core, 1, 2), new ItemStack(FramezItems.motor_core, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(FramezItems.motor_core, 1, 3), new ItemStack(FramezItems.motor_core, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(FramezItems.motor_core, 1, 0), new ItemStack(FramezItems.motor_core, 1, 3));
        GameRegistry.addShapedRecipe(new ItemStack(FramezItems.motor_core, 1, 4), "c", "c", "c", 'c', new ItemStack(FramezItems.motor_core,
                1, 3));
        GameRegistry.addShapelessRecipe(new ItemStack(FramezItems.motor_core, 3, 3), new ItemStack(FramezItems.motor_core, 1, 4));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezBlocks.stopper, 1, 0), "i i", " s ", "i i", 'i', "ingotIron", 's',
                metamorphic_stone));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezItems.upgrade_camouflage, 1, 0), "iwi", "rgb", "iki", 'i',
                "ingotIron", 'w', new ItemStack(Blocks.wool, 1, 0), 'r', new ItemStack(Blocks.wool, 1, 14), 'g', new ItemStack(Blocks.wool,
                        1, 13), 'b', new ItemStack(Blocks.wool, 1, 11), 'k', new ItemStack(Blocks.wool, 1, 15)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezItems.upgrade_bouncy, 1, 0), "iii", "ici", "iii", 'i', "ingotIron",
                'c', new ItemStack(FramezItems.motor_core, 1, 1)));

        // Register custom recipe types
        RecipeSorter.register(ModInfo.MODID + ":frame_recycle", RecipeMaterialRecycle.class, RecipeSorter.Category.SHAPELESS,
                "after:minecraft:shapeless");
        RecipeSorter.register(ModInfo.MODID + ":frame_customisation", RecipeFrameCustomisation.class, RecipeSorter.Category.SHAPED,
                "after:minecraft:shaped");

        // Register frame material recipes
        for (IFrameMaterial mat : ModifierRegistry.instance.frameMaterials) {
            Block blockWith = FramezBlocks.frames.get(FrameFactory.getIdentifier("frame0", mat));
            if (blockWith == null)
                continue;
            mat.registerRecipes(new ItemStack(blockWith));
        }

        // Register motor recipes
        for (List<IMotorModifier> l : ModifierRegistry.instance.getAllMotorCombinations()) {
            for (IMotorModifier m : l) {
                List<IMotorModifier> without = new ArrayList<IMotorModifier>(l);
                without.remove(m);

                Block blockWith = FramezBlocks.motors.get(MotorFactory.getIdentifier("motor", l));
                Block blockWithout = FramezBlocks.motors.get(MotorFactory.getIdentifier("motor", without));
                if (blockWith == null || (blockWithout == null && !without.isEmpty()))
                    continue;

                for (int i = 0; i < 4; i++)
                    m.registerRecipes(without.isEmpty() ? new ItemStack(FramezItems.motor_core, 1, i + 1) : new ItemStack(blockWithout, 1,
                            i), new ItemStack(blockWith, 1, i));
            }
        }

        // Register frame customisation recipe
        GameRegistry.addRecipe(new RecipeFrameCustomisation());
    }

    public static class RecipeFrameCustomisation implements IRecipe {

        @Override
        public boolean matches(InventoryCrafting inv, World world) {

            return getCraftingResult(inv) != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {

            // Make sure the inventory is the right size
            int size = inv.getSizeInventory();
            if (size != 4 && size != 9)
                return null;
            int width = (int) Math.sqrt(size);

            // Find the position of the frame
            int framePos = -1;
            ItemStack frameStack = null;
            for (int i = 0; i < size; i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null) {
                    if (stack.getItem() instanceof ItemBlockFrame) {
                        // If there's another frame in the grid, return null.
                        if (framePos >= 0)
                            return null;
                        framePos = i;
                        frameStack = stack;
                    }
                }
            }
            // If there's no frame in the grid, return null.
            if (framePos == -1)
                return null;
            // Calculate the actual position of the frame in the grid
            int frameX = framePos % width, frameY = (framePos - frameX) / width;

            // Positions of wrenches in the grid
            Set<BlockPos> wrenchPos = new HashSet<BlockPos>();

            // Make a list for all the frame parts
            List<IFramePart> parts = new ArrayList<IFramePart>();

            // Find all the current parts
            parts.addAll(FramezUtils.loadSilkyData(null, null, null, frameStack));
            int originalPartCount = parts.size();

            // Make sure all the items are frame parts and instantiate them
            for (int i = 0; i < size; i++) {
                if (i == framePos)
                    continue;
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null) {
                    int x = i % width, y = (i - x) / width;
                    IFramePart part = null;
                    // Try to instantiate the part
                    for (IFramePartHandler handler : ModifierRegistry.instance.framePartHandlers) {
                        part = handler.createLocated(x - frameX, y - frameY, stack);
                        if (part != null) {
                            parts.add(part);
                            break;
                        }
                    }
                    if (part != null)
                        continue;

                    // If none of the handlers succeeded, check if it's a wrench
                    if (FramezApi.instance().getWrench(stack) != null) {
                        wrenchPos.add(new BlockPos(x - frameX, y - frameY, 0));
                        continue;
                    }

                    // If none of the above worked, this item must not be valid. Return null.
                    return null;
                }
            }

            // If there are no parts or wrenches and the frame didn't have any parts, reset it to its original state.
            if (parts.isEmpty() && originalPartCount == 0 && wrenchPos.isEmpty()) {
                ItemStack stack = frameStack.copy();
                stack.stackTagCompound = null;
                return stack;
            }

            // If there are no parts or wrenches, return.
            if (parts.size() == originalPartCount && wrenchPos.isEmpty())
                return null;

            // Check for incompatible parts
            List<IFramePart> otherParts = new ArrayList<IFramePart>();
            for (IFramePart p1 : parts) {
                otherParts.addAll(parts);
                otherParts.remove(p1);
                // If it can't be placed with the other parts, return null.
                if (!p1.canPlaceWith(otherParts.toArray(new IFramePart[otherParts.size()])))
                    return null;
                for (IFramePart p2 : parts) {
                    if (p1 == p2)
                        continue;

                    // If they are both center parts, return null.
                    if (p1 instanceof IFramePartCenter && p2 instanceof IFramePartCenter)
                        return null;
                    // If they are both placed on the same side, return null.
                    if (p1 instanceof IFramePartFace && p2 instanceof IFramePartFace
                            && ((IFramePartFace) p1).getFace() == ((IFramePartFace) p2).getFace())
                        return null;
                }
                otherParts.clear();
            }

            // Write the frame frame parts in this block to the stack's tag
            ItemStack stack = frameStack.copy();
            NBTTagCompound tag = stack.stackTagCompound;
            if (tag == null)
                tag = stack.stackTagCompound = new NBTTagCompound();
            NBTTagList nbtParts = new NBTTagList();
            for (IFramePart part : parts) {
                NBTTagCompound t = new NBTTagCompound();
                t.setString("type", part.getType());
                part.writePickedToNBT(t);
                nbtParts.appendTag(t);
            }
            tag.setTag("parts", nbtParts);

            // Toggle blocked sides on the stack's tag
            int toggled = tag.getInteger(parts.isEmpty() ? "blocked" : "hidden");
            for (BlockPos p : wrenchPos) {
                int face = -1;
                if (p.x == 0 && p.y == 1)
                    face = 0;
                else if (p.x == 0 && p.y == -1)
                    face = 1;
                else if (p.x == 1 && p.y == 0)
                    face = 2;
                else if (p.x == -1 && p.y == 0)
                    face = 3;
                else if ((p.x == -1 || p.x == 1) && p.y == -1)
                    face = 4;
                else if ((p.x == -1 || p.x == 1) && p.y == 1)
                    face = 5;
                if (face == -1)
                    return null;

                toggled ^= (1 << face);
            }
            tag.setInteger(parts.isEmpty() ? "blocked" : "hidden", toggled);

            return stack;
        }

        @Override
        public int getRecipeSize() {

            return 4;
        }

        @Override
        public ItemStack getRecipeOutput() {

            return null;
        }

    }

}
