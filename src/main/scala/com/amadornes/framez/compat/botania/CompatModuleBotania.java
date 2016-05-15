package com.amadornes.framez.compat.botania;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.common.item.ItemTwigWand;

import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleBotania extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (FramezCompatConfig.botania_motor)
            ModifierRegistry.instance.registerMotorModifier(new MotorModifierMana());
    }

    @Override
    public IFramezWrench getWrench(ItemStack stack) {

        if (FramezCompatConfig.botania_wrench_support && stack.getItem() instanceof ItemTwigWand)
            return BotaniaWand.instance;

        return null;
    }

    public static class BotaniaWand implements IFramezWrench {

        public static final BotaniaWand instance = new BotaniaWand();

        @Override
        public WrenchAction getWrenchAction(ItemStack stack) {

            return null;
        }

        @Override
        public void onUsed(ItemStack stack, EntityPlayer player) {

        }

        @Override
        public boolean isSilky(ItemStack stack) {

            return false;
        }

        @Override
        public ItemStack silkPick(World world, BlockPos position, boolean simulated) {

            return null;
        }

    }
}
