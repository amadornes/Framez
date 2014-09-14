package com.amadornes.framez.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.init.CreativeTabFramez;
import com.amadornes.framez.ref.References;

public class ItemWrench extends Item {

    public ItemWrench() {

        setUnlocalizedName(References.Names.Unlocalized.WRENCH);

        setCreativeTab(CreativeTabFramez.inst);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

        return world.getBlock(x, y, z).rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
    }

    @Override
    public void registerIcons(IIconRegister reg) {

        itemIcon = reg.registerIcon(References.Textures.WRENCH);
    }

}
