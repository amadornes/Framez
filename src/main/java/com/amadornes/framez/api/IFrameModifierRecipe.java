package com.amadornes.framez.api;

public interface IFrameModifierRecipe {

    /**
     * Return a string for OreDict items, an ItemStack for specific items or True if it's a frame
     * 
     * @param slot
     *            The slot
     * @return The item in the slot
     */
    public Object getItemInCraftingSlot(int slot);

    /**
     * Whether or not the recipe is shappeless
     * 
     * @return
     */
    public boolean isShapeless();

    /**
     * If the recipe is shapeless, will the amount of used frames be the same as the ones used in the recipe?
     */
    public boolean shouldReturnAllFrames();

}
