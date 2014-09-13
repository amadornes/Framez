package com.amadornes.framez.modifier.glass.clear;

import com.amadornes.framez.api.IFrameModifierRecipe;

public class ModifierRecipeGlassClear implements IFrameModifierRecipe {

    protected ModifierRecipeGlassClear() {

    }

    @Override
    /**
     * GLASS | GLASS | GLASS
     * ------|-------|------
     * GLASS | FRAME | GLASS
     * ------|-------|------
     * GLASS | GLASS | GLASS
     */
    public Object getItemInCraftingSlot(int slot) {

        return slot == 4 ? true : "glass";
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
