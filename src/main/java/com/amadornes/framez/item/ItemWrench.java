package com.amadornes.framez.item;

import com.amadornes.framez.api.item.IFramezWrench;
import com.amadornes.framez.util.ICapabilityLambda;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemWrench extends ItemFramez implements IFramezWrench {

    public ItemWrench() {

        super("wrench");
        setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {

        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {

        return 0;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {

        player.worldObj.getBlockState(pos).getBlock().onBlockClicked(player.worldObj, pos, player);
        return true;
    }

    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {

        ItemStack stack = event.getItemStack();
        if (stack == null || stack.getItem() != this)
            return;
        event.setCanceled(true);
        event.setUseItem(Result.DENY);
        event.setUseBlock(Result.DENY);
        event.getWorld().getBlockState(event.getPos()).getBlock().onBlockClicked(event.getWorld(), event.getPos(), event.getEntityPlayer());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

        return (ICapabilityLambda) (c, f) -> c == IFramezWrench.CAPABILITY_WRENCH ? this : null;
    }

    @Override
    public boolean canSeeIssues(EntityPlayer player, ItemStack stack, EnumHand hand, boolean equipped) {

        if (equipped)
            return true;
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean("alwaysSeeIssues");
    }

}
