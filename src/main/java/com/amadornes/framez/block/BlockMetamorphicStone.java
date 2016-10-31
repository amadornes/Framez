package com.amadornes.framez.block;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.init.FramezCreativeTab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMetamorphicStone extends Block {

    public BlockMetamorphicStone() {

        super(Material.ROCK);
        setHardness(3F);
        setResistance(30);
        setUnlocalizedName(ModInfo.MODID + ":metamorphic_stone");
        setHarvestLevel("pickaxe", 3);
        setCreativeTab(FramezCreativeTab.tab);

        setDefaultState(getDefaultState().withProperty(TYPE, Type.STONE));
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
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

        return state.getValue(TYPE) == Type.STONE;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

        int metadata = getMetaFromState(state);
        if (metadata == 0) {
            return Arrays.asList(new ItemStack(this, 1, 1));
        }
        return Arrays.asList(new ItemStack(this, 1, metadata));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock) {

        Type type = state.getValue(TYPE);
        for (EnumFacing side : EnumFacing.VALUES) {
            convertIfNeeded(world, pos, side, type);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {

        Type type = state.getValue(TYPE);
        if (type.hasLogic) {
            world.scheduleBlockUpdate(pos, this, 1, 0);
        }

        for (EnumFacing side : EnumFacing.VALUES) {
            convertIfNeeded(world, pos, side, type);
        }
    }

    public static void convertIfNeeded(World world, BlockPos pos, EnumFacing side, Type type) {

        if (world.isRemote) {
            return;
        }

        pos = pos.offset(side);

        IBlockState fluidState = world.getBlockState(pos);
        if (type == Type.WATER) {
            if (fluidState.getMaterial() == Material.LAVA) {
                int lavaMeta = fluidState.getValue(BlockLiquid.LEVEL);
                if (lavaMeta == 0) {
                    world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                } else if (lavaMeta <= 4) {
                    world.setBlockState(pos, Blocks.STONE.getDefaultState());
                }
            }
        } else if (type == Type.FIRE) {
            if (fluidState.getMaterial() == Material.WATER) {
                int waterMeta = fluidState.getValue(BlockLiquid.LEVEL);
                if (waterMeta == 0) {
                    world.setBlockState(pos, Blocks.STONE.getDefaultState());
                } else {
                    world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
                }
            }
        } else if (type == Type.ICE) {
            if (fluidState.getMaterial() == Material.WATER) {
                int waterMeta = fluidState.getValue(BlockLiquid.LEVEL);
                if (waterMeta == 0) {
                    world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
                } else {
                    world.setBlockState(pos, Blocks.ICE.getDefaultState());
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

        for (EnumFacing side : EnumFacing.VALUES) {
            spawnFancyParticles(world, pos, side, state, new AxisAlignedBB(0, 0, 0, 1, 1, 1), rand);
        }
    }

    @SideOnly(Side.CLIENT)
    public void spawnFancyParticles(World world, BlockPos pos, EnumFacing side, IBlockState state, AxisAlignedBB box, Random rnd) {

        // EnumMetamorphicStoneType type = state.getValue(TYPE);
        // Vec3 v = new Vec3(new BlockPos(0, 0, 0).offset(side))
        // .addVector(1, 1, 1)
        // .div(2)
        // .setSide(side, box.getSide(side))
        // .add(BlockPos.sideOffsets[side].x != 0 ? 0.0625 * BlockPos.sideOffsets[side].x : rnd.nextDouble() * 0.8 - 0.4,
        // BlockPos.sideOffsets[side].y != 0 ? 0.0625 * BlockPos.sideOffsets[side].y : rnd.nextDouble() * 0.8 - 0.4,
        // BlockPos.sideOffsets[side].z != 0 ? 0.0625 * BlockPos.sideOffsets[side].z : rnd.nextDouble() * 0.8 - 0.4);
        // int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        //
        // if (type == EnumMetamorphicStoneType.CRACKED) {
        // world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, x + v.xCoord, y + v.yCoord, z + v.zCoord, 0, 0, 0);
        // } else if (type == EnumMetamorphicStoneType.WATER) {
        // double amt = rnd.nextDouble() * 0.3 + 0.15;
        // world.spawnParticle(EnumParticleTypes.REDSTONE, x + v.xCoord, y + v.yCoord, z + v.zCoord, -1 + amt * 3, amt * 2, 1 + amt);
        // } else if (type == EnumMetamorphicStoneType.FIRE) {
        // world.spawnParticle(EnumParticleTypes.FLAME, x + v.xCoord, y + v.yCoord, z + v.zCoord, 0, 0, 0);
        // if (rnd.nextInt(50) == rnd.nextInt(50))
        // world.spawnParticle(EnumParticleTypes.LAVA, x + v.xCoord, y + v.yCoord, z + v.zCoord, 0, 0, 0);
        // } else if (type == EnumMetamorphicStoneType.ICE) {
        // world.spawnParticle(EnumParticleTypes.REDSTONE, x + v.xCoord, y + v.yCoord, z + v.zCoord, 1, 1 + rnd.nextDouble() * 0.825, 2);
        // }
    }

    @Override
    public int tickRate(World p_149738_1_) {

        return 1;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

        Type type = state.getValue(TYPE);
        for (Object o : world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(0, 0.0625, 0))) {
            Entity entity = (Entity) o;
            if (type == Type.WATER) {
                entity.extinguish();
            } else if (type == Type.FIRE) {
                entity.setFire(5);
            } else if (type == Type.ICE) {
                if (entity instanceof EntityLivingBase) {
                    ((EntityLivingBase) entity)
                            .addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 1, 0, true, false));
                }
            }
        }

        world.scheduleBlockUpdate(pos, this, 1, 0);
    }

    public static final IProperty<Type> TYPE = PropertyEnum.create("type", Type.class);
    public static final IProperty<?>[] PROPERTIES = new IProperty[] { TYPE };

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, PROPERTIES);
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return state.getValue(TYPE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(TYPE, Type.VALUES[meta]);
    }

    @Override
    public int damageDropped(IBlockState state) {

        return getMetaFromState(state);
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {

        return layer == BlockRenderLayer.CUTOUT;
    }

    public static enum Type implements IStringSerializable {
        STONE(false),
        CRACKED(false),
        BRICK(false),
        WATER(true),
        FIRE(true),
        ICE(true);

        public static final Type[] VALUES = values();

        public final boolean hasLogic;

        private Type(boolean hasLogic) {

            this.hasLogic = hasLogic;
        }

        @Override
        public String getName() {

            return name().toLowerCase(Locale.ENGLISH);
        }
    }

}
