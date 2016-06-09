package com.amadornes.framez.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IFramezWrench {

    @CapabilityInject(IFramezWrench.class)
    public static final Capability<IFramezWrench> CAPABILITY_WRENCH = null;

    public boolean canSeeIssues(EntityPlayer player, ItemStack stack, EnumHand hand, boolean equipped);

}
