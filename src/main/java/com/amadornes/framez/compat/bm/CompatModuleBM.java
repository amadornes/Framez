package com.amadornes.framez.compat.bm;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.ref.Dependencies;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
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

        Item orbItem = GameRegistry.findItem(Dependencies.BM, "apprenticeBloodOrb");
        ItemStack orb = new ItemStack(orbItem, 1, OreDictionary.WILDCARD_VALUE);
        if (orbItem == null)
            System.exit(-1);

        GameRegistry.addRecipe(
                new ItemStack(GameRegistry.findBlock(ModInfo.MODID, References.Names.Registry.MOTOR + "." + MotorProviderBM.inst.getId()),
                        1), "rsr", "scs", "ror", 'r', Blocks.redstone_block, 's',
                        GameRegistry.findItem(Dependencies.BM, "reinforcedSlate"), 'c', FramezBlocks.motorcore, 'o', orb);
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
