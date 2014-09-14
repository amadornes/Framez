package com.amadornes.framez.modifier.iron;

import com.amadornes.framez.api.IFrameModifierRecipe;

public class ModifierRecipeIron implements IFrameModifierRecipe {

    protected ModifierRecipeIron() {

    }

    @Override
    /**
     *       | STICK |
     * ------|-------|------
     * STICK | FRAME | STICK
     * ------|-------|------
     *       | STICK |
     */
    public Object getItemInCraftingSlot(int slot) {

        return slot == 1 || slot == 3 || slot == 5 || slot == 7 ? "stickIron" : (slot == 4 ? true : null);
    }

    @Override
    public boolean isShapeless() {

        return false;
    }

    @Override
    public boolean shouldReturnAllFrames() {

        return false;
    }

}
