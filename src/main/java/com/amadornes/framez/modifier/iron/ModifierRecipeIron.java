package com.amadornes.framez.modifier.iron;

import com.amadornes.framez.api.IFrameModifierRecipe;

public class ModifierRecipeIron implements IFrameModifierRecipe {

    protected ModifierRecipeIron() {

    }

    @Override
    /**
     *       | IRON  |
     * ------|-------|------
     * IRON  | FRAME | IRON
     * ------|-------|------
     *       | IRON  |
     */
    public Object getItemInCraftingSlot(int slot) {

        return slot == 1 || slot == 3 || slot == 5 || slot == 7 ? "ingotIron" : (slot == 4 ? true : null);
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
