package com.amadornes.framez.modifier.connected;

import com.amadornes.framez.api.IFrameModifierRecipe;

public class ModifierRecipeConnected implements IFrameModifierRecipe {

    protected ModifierRecipeConnected() {

    }

    @Override
    /**
     * FRAME | FRAME
     * ------|------
     * FRAME | FRAME
     */
    public Object getItemInCraftingSlot(int slot) {

        return slot == 0 || slot == 1 || slot == 3 || slot == 4 ? true : null;
    }

    @Override
    public boolean isShapeless() {

        return true;
    }

    @Override
    public boolean shouldReturnAllFrames() {

        return true;
    }

}
