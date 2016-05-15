package com.amadornes.framez.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;

public class RedstoneHelper {

    public static int getVanillaSignalStrength(IBlockAccess world, int x, int y, int z, int side, int face) {

        if (face != 0 && face != 6)
            return 0;

        Block block = world.getBlock(x, y, z);

        if (block == Blocks.redstone_wire) {
            if (side == 0)
                return world.getBlockMetadata(x, y, z);
            if (side == 1)
                return 0;
            int d = Direction.getMovementDirection(FramezUtils.getOffsetX(side), FramezUtils.getOffsetZ(side));
            if (BlockRedstoneWire.isPowerProviderOrWire(world, x, y, z, d)
                    || BlockRedstoneWire.isPowerProviderOrWire(world, x + FramezUtils.getOffsetX(side) + FramezUtils.getOffsetX(side), y
                            + FramezUtils.getOffsetY(side) + FramezUtils.getOffsetY(side),
                            z + FramezUtils.getOffsetZ(side) + FramezUtils.getOffsetZ(side), (d + 2) % 4)) {
                return world.getBlockMetadata(x, y, z);
            }
        }
        if (block instanceof BlockRedstoneComparator)

            if (block == Blocks.unpowered_repeater) {
                return 0;
            }
        if (block == Blocks.powered_repeater) {
            if (side == 0 || side == 1)
                return 0;
            int d = Direction.getMovementDirection(FramezUtils.getOffsetX(side), FramezUtils.getOffsetZ(side));
            return d == (world.getBlockMetadata(x, y, z) % 4) ? 15 : 0;
        }
        if (block instanceof BlockRedstoneComparator) {
            if (side == 0 || side == 1)
                return 0;
            int d = Direction.getMovementDirection(FramezUtils.getOffsetX(side), FramezUtils.getOffsetZ(side));
            return d == (world.getBlockMetadata(x, y, z) % 4) ? ((TileEntityComparator) world.getTileEntity(x, y, z)).getOutputSignal() : 0;
        }

        return 0;
    }

    public static int getOutputWeak(IBlockAccess world, int x, int y, int z, int side, int face) {

        Block block = world.getBlock(x, y, z);

        int power = block.isProvidingWeakPower(world, x, y, z, side ^ 1);

        if (block.isNormalCube() && block.isOpaqueCube()) {
            for (int d = 0; d < 6; d++) {
                if (d == side)
                    continue;
                power = Math.max(
                        power,
                        getOutputStrong(world, x + FramezUtils.getOffsetX(d), y + FramezUtils.getOffsetY(d), z + FramezUtils.getOffsetZ(d),
                                d ^ 1, 6));
            }
        }

        return power;
    }

    public static int getOutputStrong(IBlockAccess world, int x, int y, int z, int side, int face) {

        int power = getVanillaSignalStrength(world, x, y, z, side, face);
        if (power > 0)
            return power;

        return world.getBlock(x, y, z).isProvidingStrongPower(world, x, y, z, side ^ 1);
    }

    public static int getOutputWeak(IBlockAccess world, int x, int y, int z, int side) {

        return getOutputWeak(world, x, y, z, side, 6);
    }

    public static int getOutputStrong(IBlockAccess world, int x, int y, int z, int side) {

        return getOutputStrong(world, x, y, z, side, 6);
    }

    public static int getOutput(IBlockAccess world, int x, int y, int z, int side) {

        return Math.max(getOutputWeak(world, x, y, z, side), getOutputStrong(world, x, y, z, side));
    }

    public static int getOutput(IBlockAccess world, int x, int y, int z, int side, int face) {

        return Math.max(getOutputWeak(world, x, y, z, side, face), getOutputStrong(world, x, y, z, side, face));
    }

    public static int getOutput(IBlockAccess world, int x, int y, int z) {

        int power = 0;
        for (int side = 0; side < 6; side++)
            power = Math.max(power, getOutput(world, x, y, z, side));
        return power;
    }

    public static int getInputWeak(IBlockAccess world, int x, int y, int z, int side, int face) {

        return getOutputWeak(world, x + FramezUtils.getOffsetX(side), y + FramezUtils.getOffsetY(side), z + FramezUtils.getOffsetZ(side),
                side ^ 1, face);
    }

    public static int getInputStrong(IBlockAccess world, int x, int y, int z, int side, int face) {

        return getOutputStrong(world, x + FramezUtils.getOffsetX(side), y + FramezUtils.getOffsetY(side), z + FramezUtils.getOffsetZ(side),
                side ^ 1, face);
    }

    public static int getInputWeak(IBlockAccess world, int x, int y, int z, int side) {

        return getOutputWeak(world, x + FramezUtils.getOffsetX(side), y + FramezUtils.getOffsetY(side), z + FramezUtils.getOffsetZ(side),
                side ^ 1);
    }

    public static int getInputStrong(IBlockAccess world, int x, int y, int z, int side) {

        return getOutputStrong(world, x + FramezUtils.getOffsetX(side), y + FramezUtils.getOffsetY(side), z + FramezUtils.getOffsetZ(side),
                side ^ 1);
    }

    public static int getInput(IBlockAccess world, int x, int y, int z, int side) {

        return getOutput(world, x + FramezUtils.getOffsetX(side), y + FramezUtils.getOffsetY(side), z + FramezUtils.getOffsetZ(side),
                side ^ 1);
    }

    public static int getInput(IBlockAccess world, int x, int y, int z, int side, int face) {

        return getOutput(world, x + FramezUtils.getOffsetX(side), y + FramezUtils.getOffsetY(side), z + FramezUtils.getOffsetZ(side),
                side ^ 1, face);
    }

    public static int getInput(IBlockAccess world, int x, int y, int z) {

        int power = 0;
        for (int side = 0; side < 6; side++)
            power = Math.max(power, getInput(world, x, y, z, side));
        return power;
    }

}
