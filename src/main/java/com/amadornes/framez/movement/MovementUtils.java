package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.movement.IFrameMove;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.Utils;

public class MovementUtils {

    public static final List<BlockCoord> getMovedBlocks(TileMotor motor) {

        List<BlockCoord> blocks = new ArrayList<BlockCoord>();
        BlockCoord motorLoc = new BlockCoord(motor);

        addBlockAndNeighbors(motor.getWorldObj(), getRelative(motorLoc, motor.getFace()), blocks, motor.getDirection());
        blocks.remove(motorLoc);

        return blocks;
    }

    private static final void addBlockAndNeighbors(World w, BlockCoord block, List<BlockCoord> blocks, ForgeDirection direction) {

        if (blocks.contains(block))
            return;

        if (!MovementApi.INST.getMovementType(w, block.x, block.y, block.z).isMovable())
            return;

        TileEntity tile = w.getTileEntity(block.x, block.y, block.z);
        if (tile != null && tile instanceof IFrameMove)
            if (!((IFrameMove) tile).canBeMoved())
                return;

        IFrame frame = Utils.getFrame(w, block.x, block.y, block.z);
        if (frame != null) {
            blocks.add(block);
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (frame.isSideBlocked(d))
                    continue;

                TileMultipart tmp = Utils.getMultipartTile(w, block.x, block.y, block.z);
                if (tmp != null && Utils.getMicroblockSize(tmp, d) == 1)
                    continue;

                BlockCoord bl = getRelative(block, d);
                TileMultipart tmp2 = Utils.getMultipartTile(w, bl.x, bl.y, bl.z);
                if (tmp2 != null && Utils.getMicroblockSize(tmp2, d.getOpposite()) == 1)
                    continue;

                addBlockAndNeighbors(w, bl, blocks, direction);
            }
        } else {
            Block b = w.getBlock(block.x, block.y, block.z);
            if (!b.isAir(w, block.x, block.y, block.z) && b != FramezBlocks.block_moving)
                blocks.add(block);
        }
    }

    public static final boolean canMove(List<BlockCoord> blocks, World world, ForgeDirection direction) {

        for (BlockCoord b : blocks) {
            BlockCoord r = getRelative(b, direction);

            for (MovingStructure ms : StructureTickHandler.INST.getStructures())
                if (ms.getBlock(b.x, b.y, b.z) != null || ms.getBlock(r.x, r.y, r.z) != null
                        || ms.getBlock(r.x - ms.getDirection().offsetX, r.y - ms.getDirection().offsetY, r.z - ms.getDirection().offsetZ) != null)
                    return false;

            if (blocks.contains(r))
                continue;
            if (world.isAirBlock(r.x, r.y, r.z))
                continue;
            if (world.getBlock(r.x, r.y, r.z).canBeReplacedByLeaves(world, r.x, r.y, r.z))
                continue;
            return MovementApi.INST.getMovementType(world, r.x, r.y, r.z).isReplaceable();
        }
        return true;
    }

    private static final BlockCoord getRelative(BlockCoord from, ForgeDirection dir) {

        return from.copy().add(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

}
