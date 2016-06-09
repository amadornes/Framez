package com.amadornes.framez.api.motor;

import com.amadornes.framez.ModInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumMotorAction implements IMotorAction {

    STOP("stop", false, 0),
    MOVE_FORWARD("forward", true, 1),
    MOVE_BACKWARD("backward", true, 2),
    ROTATE_CLOCKWISE("clockwise", true, 3),
    ROTATE_CCLOCKWISE("cclockwise", true, 4);

    public static final EnumMotorAction[] VALUES = values();

    private final String name;
    private final boolean moving;
    private final int icon;

    private EnumMotorAction(String name, boolean moving, int icon) {

        this.name = name;
        this.moving = moving;
        this.icon = icon;
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

}
