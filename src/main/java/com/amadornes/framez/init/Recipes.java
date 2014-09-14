package com.amadornes.framez.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.api.IFrameModifierRecipe;
import com.amadornes.framez.api.IModifierRegistry;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.util.SorterModifierProvider;

import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    public static void init() {

        // Wooden frame
        {
            // Frame cross
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezItems.framepart, 5, 0), "s s", " s ", "s s", 's', "stickWood"));
            // Frame side
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezItems.framepart, 1, 1), " s ", "scs", " s ", 's', "stickWood", 'c',
                    new ItemStack(FramezItems.framepart, 1, 0)));
            // Frame
            {
                ItemStack side = new ItemStack(FramezItems.framepart, 1, 1);
                GameRegistry.addRecipe(new ShapelessOreRecipe(FramezApi.inst().getModifierRegistry().getFrameStack(8), side, side, side, side, side,
                        side));
            }
        }

        // Iron frame
        {
            // Frame cross
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezItems.framepart, 5, 2), "s s", " s ", "s s", 's', "stickIron"));
            // Frame side
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezItems.framepart, 1, 3), " s ", "scs", " s ", 's', "stickIron", 'c',
                    new ItemStack(FramezItems.framepart, 1, 2)));
            // Frame
            {
                ItemStack side = new ItemStack(FramezItems.framepart, 1, 3);
                GameRegistry.addRecipe(new ShapelessOreRecipe(FramezApi.inst().getModifierRegistry().getFrameStack(8, References.Modifiers.IRON),
                        side, side, side, side, side, side));
            }
        }

        // Iron stick
        if (FramezItems.ironstick != null)
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FramezItems.ironstick, 4), "i", "i", 'i', "ingotIron"));

        Framez.log.info("Starting to generate recipes!");
        generateFrameRecipes();
        Framez.log.info("Finished generating recipes!");
    }

    private static void generateFrameRecipes() {

        IModifierRegistry reg = FramezApi.inst().getModifierRegistry();

        for (ItemStack frame : reg.getAllPossibleCombinations()) {
            Framez.log.debug("Generating recipes for " + frame.stackTagCompound);
            for (IFrameModifierProvider m : reg.getModifiers(frame)) {

                Framez.log.debug(" Without " + m.getIdentifier());

                IFrameModifierRecipe recipe = m.getRecipeProvider();
                Framez.log.debug("   Recipe: " + recipe);
                if (recipe == null)
                    continue;

                ItemStack without = getWithoutModifier(frame, m);
                Framez.log.debug("   NoMod: " + without);
                if (without == null)
                    continue;

                List<Object> r = new ArrayList<Object>();
                Map<Object, Character> chars = new HashMap<Object, Character>();
                char c = 48;// 0

                int frames = 0;
                for (int x = 0; x < 3; x++) {
                    String row = "";
                    for (int y = 0; y < 3; y++) {
                        int slot = x + (y * 3);
                        Object o = recipe.getItemInCraftingSlot(slot);
                        if (recipe.isShapeless()) {
                            if (o != null) {
                                if (o instanceof Boolean) {
                                    r.add(without);
                                    frames++;
                                } else {
                                    r.add(o);
                                }
                            }
                        } else {
                            if (o == null) {
                                row += " ";
                            } else if (o instanceof Boolean) {
                                row += "x";
                                if (!chars.containsKey(o))
                                    chars.put(without, 'x');

                                frames++;
                            } else {
                                if (chars.containsKey(o)) {
                                    row += chars.get(o);
                                } else {
                                    chars.put(o, c);
                                    row += "" + c;
                                    c++;
                                }
                            }
                        }
                    }
                    if (!recipe.isShapeless())
                        r.add(row);
                }
                Framez.log.debug("   Frames: " + frames);
                // If there's no frame in the recipe, delete it
                if (frames == 0) {
                    r.clear();
                    chars.clear();
                    continue;
                }

                if (!recipe.isShapeless()) {
                    for (Object o : chars.keySet()) {
                        r.add(chars.get(o).charValue());
                        r.add(o);
                    }
                }

                if (recipe.isShapeless()) {
                    if (recipe.shouldReturnAllFrames()) {
                        ItemStack is = frame.copy();
                        is.stackSize = frames;
                        GameRegistry.addRecipe(new ShapelessOreRecipe(is, r.toArray()));
                    } else {
                        GameRegistry.addRecipe(new ShapelessOreRecipe(frame.copy(), r.toArray()));
                    }
                } else {
                    GameRegistry.addRecipe(new ShapedOreRecipe(frame.copy(), r.toArray()));
                }
                Framez.log.debug("   ADDED!!! :D");
            }
        }
    }

    private static ItemStack getWithoutModifier(ItemStack frame, IFrameModifierProvider modifier) {

        IModifierRegistry reg = FramezApi.inst().getModifierRegistry();

        List<IFrameModifierProvider> modifiers = new ArrayList<IFrameModifierProvider>();
        modifiers.addAll(Arrays.asList(reg.getModifiers(frame)));
        modifiers.remove(modifier);
        Collections.sort(modifiers, new SorterModifierProvider());

        String[] mods = new String[modifiers.size()];
        int i = 0;
        for (IFrameModifierProvider p : modifiers) {
            mods[i] = p.getIdentifier();
            i++;
        }

        return reg.getFrameStack(mods);
    }

}
