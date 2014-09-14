package com.amadornes.framez.modifier.glass.clear;

import com.amadornes.framez.api.IFrameModifierRecipe;

public class ModifierRecipeGlassClear implements IFrameModifierRecipe {

    protected ModifierRecipeGlassClear() {

    }

    @Override
    /**
     * PANE  | PANE  | PANE 
     * ------|-------|------
     * PANE  | FRAME | PANE 
     * ------|-------|------
     * PANE  | PANE  | PANE 
     */
    public Object getItemInCraftingSlot(int slot) {

        return slot == 4 ? true : "glassPane";
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
