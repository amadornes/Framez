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
import com.amadornes.framez.util.SorterModifierProvider;

import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    public static void init() {

        Framez.log.info("Starting to generate recipes!");
        generateFrameRecipes();
        Framez.log.info("Finished generating recipes!");
    }

    private static void generateFrameRecipes() {

        IModifierRegistry reg = FramezApi.inst().getModifierRegistry();

        for (ItemStack frame : reg.getAllPossibleCombinations()) {
            for (IFrameModifierProvider m : reg.getModifiers(frame)) {

                IFrameModifierRecipe recipe = m.getRecipeProvider();
                if (recipe == null)
                    continue;

                ItemStack without = getWithoutModifier(frame, m);
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
                                    c++;
                                    row += "" + c;
                                }
                            }
                        }
                    }
                    if (!recipe.isShapeless())
                        r.add(row);
                }
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
