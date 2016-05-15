package com.amadornes.framez.compat.cofhlib;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import codechicken.lib.raytracer.RayTracer;
import cofh.api.item.IToolHammer;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.jtraits.JTrait;
import com.amadornes.jtraits.MixinFactory;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleCoFHLib extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (FramezCompatConfig.cofhlib_block_appearance_stickiness)
            MovementRegistry.instance.registerStickinessHandler(new StickinessHandlerCoFHBlockAppearance());
        if (FramezCompatConfig.cofhlib_wrench_support)
            Framez.instance.wrenchItemClass = MixinFactory.mixin(Framez.instance.wrenchItemClass, TWrenchCoFH.class);
    }

    public static class TWrenchCoFH extends JTrait<ItemStack> implements IToolHammer {

        @Override
        public boolean isUsable(ItemStack arg0, EntityLivingBase arg1, int arg2, int arg3, int arg4) {

            return true;
        }

        @Override
        public void toolUsed(ItemStack arg0, EntityLivingBase arg1, int arg2, int arg3, int arg4) {

        }

    }

    @Override
    public IFramezWrench getWrench(ItemStack stack) {

        if (FramezCompatConfig.cofhlib_wrench_support && stack.getItem() instanceof IToolHammer)
            return CoFHWrench.instance;

        return null;
    }

    public static class CoFHWrench implements IFramezWrench {

        public static final CoFHWrench instance = new CoFHWrench();

        @Override
        public WrenchAction getWrenchAction(ItemStack stack) {

            return WrenchAction.ROTATE;
        }

        @Override
        public void onUsed(ItemStack stack, EntityPlayer player) {

            MovingObjectPosition mop = RayTracer.reTrace(player.worldObj, player);
            ((IToolHammer) stack.getItem()).toolUsed(stack, player, mop.blockX, mop.blockY, mop.blockZ);
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
