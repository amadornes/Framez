package com.amadornes.framez.compat.bc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import buildcraft.api.statements.StatementManager;
import buildcraft.api.tools.IToolWrench;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.jtraits.JTrait;
import com.amadornes.jtraits.MixinFactory;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleBC extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (FramezCompatConfig.bc_facade_stickiness)
            FramezApi.instance().movementRegistry().registerStickinessHandler(new BCStickinessHandler());
        if (FramezCompatConfig.bc_wrench_support)
            Framez.instance.wrenchItemClass = MixinFactory.mixin(Framez.instance.wrenchItemClass, TWrenchBC.class);

        StatementManager.registerStatement(TriggerIsMoving.INST);
        StatementManager.registerStatement(ActionMove.INST);
        TriggerActionProvider tap = new TriggerActionProvider();
        StatementManager.registerTriggerProvider(tap);
        StatementManager.registerActionProvider(tap);
    }

    public static class TWrenchBC extends JTrait<ItemStack> implements IToolWrench {

        @Override
        public boolean canWrench(EntityPlayer arg0, int arg1, int arg2, int arg3) {

            return true;
        }

        @Override
        public void wrenchUsed(EntityPlayer arg0, int arg1, int arg2, int arg3) {

        }

    }

    @Override
    public IFramezWrench getWrench(ItemStack stack) {

        if (FramezCompatConfig.bc_wrench_support && stack.getItem() instanceof IToolWrench)
            return BCWrench.instance;

        return null;
    }

    public static class BCWrench implements IFramezWrench {

        public static final BCWrench instance = new BCWrench();

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
