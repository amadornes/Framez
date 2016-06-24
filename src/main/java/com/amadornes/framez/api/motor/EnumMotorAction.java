package com.amadornes.framez.api.motor;

import com.amadornes.framez.ModInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumMotorAction implements IMotorAction {

    // @formatter:off
    STOP               ("stop",           false, 0, -1, 0, 1),
    MOVE_FORWARD       ("forward",        true,  1,  0, 0   ),
    MOVE_BACKWARD      ("backward",       true,  2,  0, 0   ),
    ROTATE_CLOCKWISE   ("clockwise",      true,  3,  1, 1   ),
    ROTATE_CCLOCKWISE  ("cclockwise",     true,  4,  1, 1   ),
    STICKINESS_ENABLED ("stickiness_on",  false, 5,  2, 2   ),
    STICKINESS_DISABLED("stickiness_off", false, 6,  2, 2   );
    // @formatter:on

    public static final EnumMotorAction[] VALUES = values();

    private final String name;
    private final boolean moving;
    private final int icon;
    private final int type;
    private final int[] categories;

    private EnumMotorAction(String name, boolean moving, int icon, int type, int... categories) {

        this.name = name;
        this.moving = moving;
        this.icon = icon;
        this.type = type;
        this.categories = categories;
    }

    @Override
    public String getUnlocalizedName() {

        return "action." + ModInfo.MODID + ":" + name;
    }

    @Override
    public ItemStack getIconStack() {

        return new ItemStack(GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("framez:icons")), 1, icon);
    }

    @Override
    public boolean isMoving() {

        return moving;
    }

    @Override
    public boolean clashesWith(IMotorAction action) {

        return action instanceof EnumMotorAction && ((EnumMotorAction) action).type == type;
    }

    @Override
    public int[] getCategories() {

        return categories;
    }

}
