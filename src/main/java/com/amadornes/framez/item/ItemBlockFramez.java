package com.amadornes.framez.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockFramez extends ItemBlock implements IFramezItem {

    private String name = "error";

    public ItemBlockFramez(Block block) {

        super(block);
    }

    public ItemBlockFramez(Block block, String name) {

        super(block);
        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public boolean isBlock() {

        return true;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
            float hitZ, IBlockState newState) {

        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            setTileEntityNBT_WTFVanilla(world, player, pos, stack);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }

    public static boolean setTileEntityNBT_WTFVanilla(World worldIn, @Nullable EntityPlayer player, BlockPos pos, ItemStack stackIn) {

        MinecraftServer minecraftserver = worldIn.getMinecraftServer();

        if (minecraftserver == null) {
            return false;
        } else {
            if (stackIn.hasTagCompound() && stackIn.getTagCompound().hasKey("BlockEntityTag", 10)) {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity != null) {
                    if (!worldIn.isRemote && tileentity.onlyOpsCanSetNbt() && (player == null
                            || !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile()))) { return false; }

                    NBTTagCompound nbttagcompound = (NBTTagCompound) stackIn.getTagCompound().getTag("BlockEntityTag");
                    nbttagcompound.setInteger("x", pos.getX());
                    nbttagcompound.setInteger("y", pos.getY());
                    nbttagcompound.setInteger("z", pos.getZ());

                    tileentity.readFromNBT(nbttagcompound);
                    tileentity.markDirty();
                    return true;
                }
            }

            return false;
        }
    }

}
