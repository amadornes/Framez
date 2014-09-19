package com.amadornes.framez.compat.rf;

import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.vanilla.MotorProviderDC;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompatModuleRF extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        FramezApi.inst().getMotorRegistry().registerMotor(MotorProviderRF.inst);
        if (ev.getSide() == Side.CLIENT) {
            preInitClient();
        }
    }

    @SideOnly(Side.CLIENT)
    private void preInitClient() {

        FramezApi.inst().getMotorRegistry().registerSpecialRenderer(new RenderSpecialRF());
    }

    @Override
    public void init(FMLInitializationEvent ev) {

        GameRegistry.addRecipe(new ShapedOreRecipe(GameRegistry.findBlock(ModInfo.MODID,
                References.Names.Registry.MOTOR + "." + MotorProviderRF.inst.getId()), "rrr", "rmr", "rrr", 'm', GameRegistry.findBlock(
                ModInfo.MODID, References.Names.Registry.MOTOR + "." + MotorProviderDC.inst.getId()), 'r', Blocks.redstone_block));
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
    public void registerRenders() {

    }

}
