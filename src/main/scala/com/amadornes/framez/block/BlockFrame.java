package com.amadornes.framez.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.client.render.RenderFrame;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.item.ItemBlockFrame;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.movement.IFramezFrame;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFrame extends Block implements IFrameBlock {

    public static Material materialFrame = new Material(MapColor.stoneColor) {

        @Override
        public boolean isToolNotRequired() {

            return true;
        }

        @Override
        public boolean isOpaque() {

            return false;
        }
    };

    public IFrameMaterial material;
    public int id;

    public BlockFrame(IFrameMaterial material, int id) {

        super(materialFrame);

        this.material = material;
        this.id = id;

        setHardness(1);
        setResistance(20);

        setBlockName("frame_" + material.getType());
        setHarvestLevel("wrench", 0);
    }

    @Override
    public float getBlockHardness(World p_149712_1_, int p_149712_2_, int p_149712_3_, int p_149712_4_) {

        setHardness(0.2F);
        return super.getBlockHardness(p_149712_1_, p_149712_2_, p_149712_3_, p_149712_4_);
    }

    @Override
    public int getMobilityFlag() {

        return 2;
    }

    @Override
    public boolean hasTileEntity(int metadata) {

        return id != 0;
    }

    @Override
    public IFrameMaterial getMaterial(IBlockAccess world, BlockPos position) {

        return material;
    }

    @Override
    public int getMultipartCount(World world, BlockPos position) {

        return 0;
    }

    @Override
    public boolean canHaveCovers() {

        return false;
    }

    @Override
    public boolean hasPanel(IBlockAccess world, BlockPos position, int side) {

        return false;
    }

    @Override
    public boolean shouldRenderCross(IBlockAccess world, BlockPos position, int side) {

        return true;
    }

    @Override
    public boolean isSideBlocked(IBlockAccess world, BlockPos position, int side) {

        if (world == null || world.getBlock(position.x, position.y, position.z) != this)
            return false;

        int meta = id * 16 + world.getBlockMetadata(position.x, position.y, position.z);
        return (meta & (1 << side)) != 0;
    }

    @Override
    public boolean isSideHidden(IBlockAccess world, BlockPos position, int side) {

        return false;
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        return !isSideBlocked(world, position, side);
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        return isSideSticky(world, position, side, movement);
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return false;
    }

    @Override
    public void cloneFrame(World world, BlockPos position, IFrame frame) {

    }

    @Override
    public void cloneFrameBlock(World world, BlockPos position, World frameWorld, BlockPos framePosition, IFrameBlock frame) {

    }

    @Override
    public void harvest(World world, BlockPos position) {

        world.setBlockToAir(position.x, position.y, position.z);
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
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderFrame.ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {

        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInPass(int pass) {

        RenderFrame.pass = pass;
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        return material.getTexture(FramezApi.instance().wrapFrameBlockRenderData(world, new BlockPos(x, y, z), this), side, 2).full();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {

        return material.getTexture(null, side, 2).full();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister map) {

    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {

        BlockPos position = new BlockPos(x, y, z);
        IFrame frame = MovementRegistry.instance.getFrameAt(world, position);
        if (frame != null && Framez.proxy.getPlayer().isSneaking()) {
            ItemStack item = Framez.proxy.getPlayer().getCurrentEquippedItem();
            if ((item != null && item.getItem() instanceof IFramezWrench && ((IFramezWrench) item.getItem()).isSilky(item))
                    || Framez.proxy.getPlayer().capabilities.isCreativeMode)
                return FramezUtils.silkHarvest(world, position, true);
        }
        return FramezUtils.getItem(world, new BlockPos(x, y, z));
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {

        return FramezUtils.rayTraceFrame(world, x, y, z, start, end);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float x_, float y_, float z_) {

        ItemStack stack = player.getCurrentEquippedItem();
        IFramezWrench wrench = FramezApi.instance().getWrench(stack);

        if (wrench != null) {
            if (!player.isSneaking()) {
                int blocked = id * 16 + world.getBlockMetadata(x, y, z);
                if ((blocked & (1 << side)) != 0)
                    blocked &= ~(1 << side);
                else
                    blocked |= (1 << side);

                Block block = FramezBlocks.frames.get(FrameFactory.getIdentifier("frame"
                        + (blocked < 16 ? "0" : (blocked < 32 ? "1" : (blocked < 48 ? "2" : "3"))), material));
                int metadata = blocked % 16;

                if (!world.isRemote) {
                    if (world.getBlock(x, y, z) != block && !world.setBlock(x, y, z, block))
                        return false;
                    world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
                }
                wrench.onUsed(stack, player);
                return true;
            }
            return false;
        }

        // BlockPos position = new BlockPos(x, y, z);
        // for (IFrameSideModifier mod : ModifierRegistry.instance.frameSideModifiers) {
        // if (!FramezUtils.contains(frame.getSideModifiers(world, position, side), mod) && mod.canApplyTo(world, position, frame, side)) {
        // frame.addSideModifier(side, mod);
        // if (!world.isRemote && !player.capabilities.isCreativeMode)
        // stack.stackSize--;
        // return true;
        // }
        // }

        return false;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {

        // TileEntity tile = world.getTileEntity(x, y, z);
        // if (tile == null || !(tile instanceof IFramezFrame))
        // return;
        // IFramezFrame te = (IFramezFrame) tile;
        //
        onClicked(FramezApi.instance().wrapFrameBlock(world, new BlockPos(x, y, z), this), world, x, y, z, player);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof IFramezFrame))
            return super.removedByPlayer(world, player, x, y, z);
        IFramezFrame te = (IFramezFrame) tile;

        if (!onClicked(te, world, x, y, z, player))
            return super.removedByPlayer(world, player, x, y, z);
        return false;
    }

    @Override
    public boolean isToolEffective(String type, int metadata) {

        return true;// type.equals("pickaxe") || type.equals("wrench");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {

        if (id == 0)
            list.add(new ItemStack(item));
    }

    public static boolean onClicked(IFrame frame, World world, int x, int y, int z, EntityPlayer player) {

        // ItemStack stack = player.getCurrentEquippedItem();
        // IFramezWrench wrench = FramezApi.instance().getWrench(stack);
        //
        // if (wrench != null && player.isSneaking() && wrench.isSilky(stack)) {
        // if (!world.isRemote)
        // FramezUtils.spawnItemInWorld(world, FramezUtils.silkHarvest(world, new BlockPos(x, y, z), false), x, y, z);
        // player.swingItem();
        // return true;
        // }

        return false;
    }

    public static IFrame getFrame(ItemStack stack) {

        if (stack == null || !(stack.getItem() instanceof ItemBlockFrame))
            return null;
        return FramezApi.instance().wrapFrameBlock(null, null, (BlockFrame) Block.getBlockFromItem(stack.getItem()));
    }

    public static <T extends IFramezFrame> T createFrame(Class<T> base, ItemStack stack) {

        return null;
    }

    public static IFrameMaterial getMaterial(ItemStack stack) {

        if (stack == null || !(stack.getItem() instanceof ItemBlockFrame))
            return null;

        return ((BlockFrame) Block.getBlockFromItem(stack.getItem())).material;
    }
}
