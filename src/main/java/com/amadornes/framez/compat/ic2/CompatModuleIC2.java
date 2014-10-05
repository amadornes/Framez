package com.amadornes.framez.compat.ic2;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.ref.Dependencies;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompatModuleIC2 extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        FramezApi.inst().getMotorRegistry().registerMotor(MotorProviderEU.inst);
        if (ev.getSide() == Side.CLIENT) {
            preInitClient();
        }
    }

    @SideOnly(Side.CLIENT)
    private void preInitClient() {

        FramezApi.inst().getMotorRegistry().registerSpecialRenderer(new RenderSpecialEU());

        FramezApi.inst().getMovementApi().registerMovementHandler(new MovementListenerIC2());
    }

    @Override
    public void init(FMLInitializationEvent ev) {

        GameRegistry.addRecipe(new ShapedOreRecipe(GameRegistry.findBlock(ModInfo.MODID,
                References.Names.Registry.MOTOR + "." + MotorProviderEU.inst.getId()), "rcr", "cbc", "rcr", 'b', new ItemStack(GameRegistry
                        .findBlock(Dependencies.IC2, "blockMachine"), 1, 0), 'c', "circuitBasic", 'r', Blocks.redstone_block));
    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerRenders() {

    }

}
