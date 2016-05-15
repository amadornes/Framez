package com.amadornes.framez.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import codechicken.lib.vec.Cuboid6;

import com.amadornes.framez.client.render.RenderMetamorphicStone;
import com.amadornes.framez.init.FramezCreativeTab;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMetamorphicStone extends Block {

    public BlockMetamorphicStone() {

        super(Material.iron);
        setHardness(1.5F);
        setResistance(30);
        setBlockName(ModInfo.MODID + ":metamorphic_stone");
        setHarvestLevel("pickaxe", 3);
        setCreativeTab(FramezCreativeTab.tab);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item i, CreativeTabs t, List l) {

        l.add(new ItemStack(i, 1, 0));
        l.add(new ItemStack(i, 1, 1));
        l.add(new ItemStack(i, 1, 2));
        l.add(new ItemStack(i, 1, 3));
        l.add(new ItemStack(i, 1, 4));
        l.add(new ItemStack(i, 1, 5));
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {

        return true;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        if (metadata == 0)
            return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(this, 1, 1)));

        return new ArrayList<ItemStack>(Arrays.asList(new ItemStack(this, 1, metadata)));
    }

    @Override
    public int getDamageValue(World w, int x, int y, int z) {

        return w.getBlockMetadata(x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        for (int side = 0; side < 6; side++)
            convertIfNeeded(world, x, y, z, side, world.getBlockMetadata(x, y, z));
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

        if (world.getBlockMetadata(x, y, z) >= 3)
            world.scheduleBlockUpdate(x, y, z, this, 1);

        for (int side = 0; side < 6; side++)
            convertIfNeeded(world, x, y, z, side, world.getBlockMetadata(x, y, z));
    }

    public void convertIfNeeded(World world, int x, int y, int z, int side, int meta) {

        if (world.isRemote)
            return;

        x += FramezUtils.getOffsetX(side);
        y += FramezUtils.getOffsetY(side);
        z += FramezUtils.getOffsetZ(side);

        if (meta == 3) {
            if (world.getBlock(x, y, z).getMaterial() == Material.lava) {
                int lavaMeta = world.getBlockMetadata(x, y, z);
                if (lavaMeta == 0)
                    world.setBlock(x, y, z, Blocks.obsidian);
                else if (lavaMeta <= 4)
                    world.setBlock(x, y, z, Blocks.stone);
            }
        } else if (meta == 4) {
            if (world.getBlock(x, y, z).getMaterial() == Material.water) {
                int waterMeta = world.getBlockMetadata(x, y, z);
                if (waterMeta == 0)
                    world.setBlock(x, y, z, Blocks.stone);
                else
                    world.setBlock(x, y, z, Blocks.cobblestone);
            }
        } else if (meta == 5) {
            if (world.getBlock(x, y, z).getMaterial() == Material.water) {
                int waterMeta = world.getBlockMetadata(x, y, z);
                if (waterMeta == 0)
                    world.setBlock(x, y, z, Blocks.packed_ice);
                else
                    world.setBlock(x, y, z, Blocks.ice);
            }
        }
    }

    // Client

    @SideOnly(Side.CLIENT)
    private IIcon stone, cracked, cracked_base, bricks, ice_still;

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        int meta = world == null ? x : world.getBlockMetadata(x, y, z);

        if (meta == 1)
            return cracked;
        else if (meta == 2)
            return bricks;
        else if (meta == 3 || meta == 4 || meta == 5)
            return cracked_base;

        return stone;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        if (meta == -1)
            return ice_still;

        // Return cracked here instead of cracked_base so breaking textures don't look funky
        return meta == 1 ? cracked : (meta == 2 ? bricks : (meta == 3 || meta == 4 || meta == 5 ? cracked : stone));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int meta) {

        return meta == 1 ? cracked : (meta == 2 ? bricks : (meta == 3 || meta == 4 || meta == 5 ? cracked_base : stone));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {

        stone = reg.registerIcon(ModInfo.MODID + ":metamorphic_stone/stone");
        cracked = reg.registerIcon(ModInfo.MODID + ":metamorphic_stone/cracked");
        cracked_base = reg.registerIcon(ModInfo.MODID + ":metamorphic_stone/cracked_base");
        bricks = reg.registerIcon(ModInfo.MODID + ":metamorphic_stone/bricks");

        ice_still = reg.registerIcon(ModInfo.MODID + ":metamorphic_stone/ice_animation");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {

        int meta = world.getBlockMetadata(x, y, z);

        for (int i = 0; i < 6; i++)
            spawnFancyParticles(world, x, y, z, i, meta, Cuboid6.full, rnd);
    }

    @SideOnly(Side.CLIENT)
    public void spawnFancyParticles(World world, int x, int y, int z, int side, int meta, Cuboid6 box, Random rnd) {

        Vector3 v = new Vector3(BlockPos.sideOffsets[side])
                .add(1, 1, 1)
                .div(2)
                .setSide(side, box.getSide(side))
                .add(BlockPos.sideOffsets[side].x != 0 ? 0.0625 * BlockPos.sideOffsets[side].x : rnd.nextDouble() * 0.8 - 0.4,
                        BlockPos.sideOffsets[side].y != 0 ? 0.0625 * BlockPos.sideOffsets[side].y : rnd.nextDouble() * 0.8 - 0.4,
                        BlockPos.sideOffsets[side].z != 0 ? 0.0625 * BlockPos.sideOffsets[side].z : rnd.nextDouble() * 0.8 - 0.4);

        if (meta == 1) {
            world.spawnParticle("depthsuspend", x + v.x, y + v.y, z + v.z, 0, 0, 0);
        } else if (meta == 3) {
            double amt = rnd.nextDouble() * 0.3 + 0.15;
            world.spawnParticle("reddust", x + v.x, y + v.y, z + v.z, -1 + amt * 3, amt * 2, 1 + amt);
        } else if (meta == 4) {
            world.spawnParticle("flame", x + v.x, y + v.y, z + v.z, 0, 0, 0);
            if (rnd.nextInt(50) == rnd.nextInt(50))
                world.spawnParticle("lava", x + v.x, y + v.y, z + v.z, 0, 0, 0);
        } else if (meta == 5) {
            world.spawnParticle("reddust", x + v.x, y + v.y, z + v.z, 1, 1 + rnd.nextDouble() * 0.825, 2);
        }
    }

    @Override
    public int tickRate(World p_149738_1_) {

        return 1;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random p_149674_5_) {

        int meta = world.getBlockMetadata(x, y, z);
        for (Object o : world.getEntitiesWithinAABBExcludingEntity(null,
                AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0, 0.0625, 0))) {
            Entity entity = (Entity) o;
            if (meta == 3) {
                entity.extinguish();
            } else if (meta == 4) {
                entity.setFire(5);
            } else if (meta == 5) {
                if (entity instanceof EntityLivingBase)
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1, 0, true));
            }
        }

        world.scheduleBlockUpdate(x, y, z, this, 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderMetamorphicStone.ID;
    }

}
