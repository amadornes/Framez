package com.amadornes.framez.compat.bm;

import net.minecraft.item.Item;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompatModuleBM extends CompatModule {

    public static Item item = new DummyItemBM();

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        FramezApi.inst().getMotorRegistry().registerMotor(MotorProviderBM.inst);
        if (ev.getSide() == Side.CLIENT) {
            preInitClient();
        }
    }

    @SideOnly(Side.CLIENT)
    private void preInitClient() {

        FramezApi.inst().getMotorRegistry().registerSpecialRenderer(new RenderSpecialBM());
    }

    @Override
    public void init(FMLInitializationEvent ev) {

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
