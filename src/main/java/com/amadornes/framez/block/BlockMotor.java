package com.amadornes.framez.block;

import com.amadornes.framez.client.gui.GuiMotorSettings;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.motor.IMotorLogic;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.PropertyCamouflage;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

@SuppressWarnings("unchecked")
public class BlockMotor extends Block implements ITileEntityProvider {

    public static final IProperty<Integer> PROPERTY_LOGIC_TYPE = PropertyInteger.create("type", 0, IMotorLogic.TYPES.length);
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

        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new TileMotor();
    }

    @Override
    public int getRenderType() {

        return 3;
    }

    @Override
    public boolean canRenderInLayer(EnumWorldBlockLayer layer) {

        return layer == EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX,
            float hitY, float hitZ) {

        // TODO: Wrench API!
        if (player.isSneaking() && player.getCurrentEquippedItem() != null
                && player.getCurrentEquippedItem().getItem() == FramezItems.wrench) {
            if (world.isRemote)
                Minecraft.getMinecraft().displayGuiScreen(new GuiMotorSettings(((TileMotor) world.getTileEntity(pos)).getSafeReference()));
            return true;
        }
        return false;
    }

    @Override
    protected BlockState createBlockState() {

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
    public boolean isBlockNormalCube() {

        // Ambient occlusion
        return true;
    }

    @Override
    public boolean isOpaqueCube() {

        // Block face clipping
        return false;
    }

    @Override
    public boolean isFullCube() {

        // Solidity
        return true;
    }

    @Override
    public boolean isFullBlock() {

        // Full block? I guess? Meh, who cares
        return false;
    }

}
