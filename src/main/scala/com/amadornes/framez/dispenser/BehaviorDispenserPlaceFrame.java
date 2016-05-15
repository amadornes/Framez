package com.amadornes.framez.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import com.amadornes.framez.util.FramezFakePlayer;

public class BehaviorDispenserPlaceFrame implements IBehaviorDispenseItem {

    @Override
    public ItemStack dispense(IBlockSource location, ItemStack stack) {

        EnumFacing direction = BlockDispenser.func_149937_b(location.getBlockMetadata());

        FakePlayer fp = FramezFakePlayer.get((WorldServer) location.getWorld());
        fp.setCurrentItemOrArmor(0, stack);

        PlayerInteractEvent pie = new PlayerInteractEvent(fp, Action.RIGHT_CLICK_BLOCK, location.getXInt(), location.getYInt(),
                location.getZInt(), direction.ordinal(), location.getWorld());

        MinecraftForge.EVENT_BUS.post(pie);

        stack = fp.getHeldItem();

        if (!pie.isCanceled() && stack.stackSize > 0)
            ((ItemBlock) stack.getItem()).onItemUse(stack, fp, location.getWorld(), location.getXInt(), location.getYInt(),
                    location.getZInt(), direction.ordinal(), 0.5f, 0.5f, 0.5f);

        return stack;
    }
}
