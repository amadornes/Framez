package com.amadornes.framez.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.client.render.RenderMotor;
import com.amadornes.framez.init.CreativeTabFramez;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMotorCore extends Block {

    public BlockMotorCore() {

        super(Material.iron);

        setBlockName(References.Names.Registry.MOTORCORE);
        setHardness(4);

        setCreativeTab(CreativeTabFramez.inst);
    }

    @Override
    public String getUnlocalizedName() {

        return "tile." + References.Names.Unlocalized.MOTORCORE;
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {

        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderMotor.RENDER_ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {

        blockIcon = reg.registerIcon(References.Textures.MOTOR);
    }
}
