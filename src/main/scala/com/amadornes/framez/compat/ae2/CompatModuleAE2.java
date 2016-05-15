package com.amadornes.framez.compat.ae2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import appeng.api.implementations.items.IAEWrench;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.jtraits.JTrait;
import com.amadornes.jtraits.MixinFactory;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleAE2 extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (FramezCompatConfig.ae2_wrench_support)
            Framez.instance.wrenchItemClass = MixinFactory.mixin(Framez.instance.wrenchItemClass, TWrenchAE2.class);
    }

    public static class TWrenchAE2 extends JTrait<ItemStack> implements IAEWrench {

        @Override
        public boolean canWrench(ItemStack wrench, EntityPlayer player, int x, int y, int z) {

            return true;
        }
    }

    @Override
    public IFramezWrench getWrench(ItemStack stack) {

        if (FramezCompatConfig.ae2_wrench_support && stack.getItem() instanceof IAEWrench)
            return AE2Wrench.instance;

        return null;
    }

    public static class AE2Wrench implements IFramezWrench {

        public static final AE2Wrench instance = new AE2Wrench();

        @Override
        public WrenchAction getWrenchAction(ItemStack stack) {

            return WrenchAction.ROTATE;
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
