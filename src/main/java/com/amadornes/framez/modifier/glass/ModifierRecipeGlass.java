package com.amadornes.framez.modifier.glass;

import com.amadornes.framez.api.IFrameModifierRecipe;

public class ModifierRecipeGlass implements IFrameModifierRecipe {

    protected ModifierRecipeGlass() {

    }

    @Override
    /**
     *       | PANE  |
     * ------|-------|------
     * PANE  | FRAME | PANE 
     * ------|-------|------
     *       | PANE  |
     */
    public Object getItemInCraftingSlot(int slot) {

        return slot == 1 || slot == 3 || slot == 5 || slot == 7 ? "glassPane" : (slot == 4 ? true : null);
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
