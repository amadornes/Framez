package com.amadornes.framez.block;

import com.amadornes.framez.api.item.IFramezWrench;
import com.amadornes.framez.client.gui.GuiMotorSettings;
import com.amadornes.framez.motor.logic.IMotorLogic;
import com.amadornes.framez.motor.logic.MotorLogicType;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.PropertyCamouflage;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

@SuppressWarnings("unchecked")
public class BlockMotor extends Block implements ITileEntityProvider {

    public static final IProperty<Integer> PROPERTY_LOGIC_TYPE = PropertyInteger.create("type", 0, MotorLogicType.VALUES.length);
    public static final IProperty<Integer> PROPERTY_PART_TYPE = PropertyInteger.create("part", 0, 2);
    public static final IUnlistedProperty<IBlockState> PROPERTY_CAMO_DOWN = new PropertyCamouflage("camo_down");
    public static final IUnlistedProperty<IBlockState> PROPERTY_CAMO_UP = new PropertyCamouflage("camo_up");
    public static final IUnlistedProperty<IBlockState> PROPERTY_CAMO_NORTH = new PropertyCamouflage("camo_north");
    public static final IUnlistedProperty<IBlockState> PROPERTY_CAMO_SOUTH = new PropertyCamouflage("camo_south");
    public static final IUnlistedProperty<IBlockState> PROPERTY_CAMO_WEST = new PropertyCamouflage("camo_west");
    public static final IUnlistedProperty<IBlockState> PROPERTY_CAMO_EAST = new PropertyCamouflage("camo_east");
    public static final IUnlistedProperty<IBlockState>[] PROPERTIES_CAMO = new IUnlistedProperty[] { PROPERTY_CAMO_DOWN, PROPERTY_CAMO_UP,
            PROPERTY_CAMO_NORTH, PROPERTY_CAMO_SOUTH, PROPERTY_CAMO_WEST, PROPERTY_CAMO_EAST };

    public BlockMotor() {

        super(Material.IRON);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new TileMotor(IMotorLogic.create(meta));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {

        return layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack,
            EnumFacing side, float hitX, float hitY, float hitZ) {

        return false;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {

        ItemStack stack = player.getHeldItemMainhand();
        if (stack != null && stack.hasCapability(IFramezWrench.CAPABILITY_WRENCH, null) && world.isRemote)
            Minecraft.getMinecraft().displayGuiScreen(new GuiMotorSettings(((TileMotor) world.getTileEntity(pos)).getSafeReference()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {

        ItemStack stack = player.getHeldItemMainhand();
        if (stack != null && stack.hasCapability(IFramezWrench.CAPABILITY_WRENCH, null)) return 0;
        return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

        ItemStack stack = player.getHeldItemMainhand();
        if (stack != null && stack.hasCapability(IFramezWrench.CAPABILITY_WRENCH, null)) {
            if (world.isRemote)
                Minecraft.getMinecraft().displayGuiScreen(new GuiMotorSettings(((TileMotor) world.getTileEntity(pos)).getSafeReference()));
            return false;
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {

        ((TileMotor) world.getTileEntity(pos)).onNeighborBlockChange(block);
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new ExtendedBlockState(this, new IProperty[] { PROPERTY_LOGIC_TYPE, PROPERTY_PART_TYPE }, new IUnlistedProperty[] {
                PROPERTY_CAMO_DOWN, PROPERTY_CAMO_UP, PROPERTY_CAMO_NORTH, PROPERTY_CAMO_SOUTH, PROPERTY_CAMO_WEST, PROPERTY_CAMO_EAST });
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileMotor) return ((TileMotor) te).getActualState(state.withProperty(PROPERTY_PART_TYPE, 0));
        return state;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {

        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileMotor) return ((TileMotor) te).getExtendedState((IExtendedBlockState) state);
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return state.getValue(PROPERTY_LOGIC_TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(PROPERTY_LOGIC_TYPE, meta).withProperty(PROPERTY_PART_TYPE, 0);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {

        // Ambient occlusion
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {

        // Block face clipping
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {

        // Solidity
        return true;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {

        // Full block? I guess? Meh, who cares
        return false;
    }

    @Override
    public int damageDropped(IBlockState state) {

        return getMetaFromState(state);
    }

}
