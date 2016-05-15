package com.amadornes.framez.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.init.FramezCreativeTab;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.util.RedstoneHelper;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStopper extends Block implements ISticky {

    public static boolean findStopper(World world, int x, int y, int z, int side) {

        Block b = world.getBlock(x, y, z);
        if (b instanceof BlockStopper)
            return true;

        return FramezApi.instance().compat().fmp().hasCover(world, x, y, z, side, "framez:stopper");
    }

    @SideOnly(Side.CLIENT)
    private IIcon def, off, on;

    public BlockStopper() {

        super(Material.iron);
        setHardness(1.5F);
        setResistance(30);
        setBlockName(ModInfo.MODID + ":stopper");

        setCreativeTab(FramezCreativeTab.tab);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item i, CreativeTabs t, List l) {

        l.add(new ItemStack(i, 1, 0));
        // l.add(new ItemStack(i, 1, 1));
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {

        def = reg.registerIcon(ModInfo.MODID + ":stopper/stopper");
        off = reg.registerIcon(ModInfo.MODID + ":stopper/stopper_off");
        on = reg.registerIcon(ModInfo.MODID + ":stopper/stopper_on");
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        int meta = world.getBlockMetadata(x, y, z);
        return meta == 1 ? off : (meta == 2 ? on : def);
    }

    @Override
    public IIcon getIcon(int side, int meta) {

        return meta == 1 || meta == 2 ? on : def;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        int meta = world.getBlockMetadata(x, y, z);

        if (meta != 1 && meta != 2)
            return;

        boolean powered = RedstoneHelper.getInput(world, x, y, z) > 0;
        if (powered != (meta == 2))
            world.setBlockMetadataWithNotify(x, y, z, powered ? 2 : 1, 3);
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        if (movement != null && movement instanceof IMovementSlide) {
            int dir = ((IMovementSlide) movement).getDirection();
            if (!(dir == side || dir == (side ^ 1)))
                return false;
            return true;
        }
        return false;
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        if (movement != null && movement instanceof IMovementSlide) {
            int dir = ((IMovementSlide) movement).getDirection();
            return MovementRegistry.instance.canStickToSide(world, position.copy().offset(dir ^ 1), dir, movement) || dir == side;
        }
        return false;
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return false;
    }

    public boolean isActive(World world, int x, int y, int z) {

        return world.getBlockMetadata(x, y, z) != 1;
    }

    @Override
    public int getDamageValue(World w, int x, int y, int z) {

        int meta = w.getBlockMetadata(x, y, z);
        return meta == 1 || meta == 2 ? 1 : 0;
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

}
