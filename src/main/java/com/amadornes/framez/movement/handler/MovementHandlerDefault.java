package com.amadornes.framez.movement.handler;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;
import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovable;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;

@Priority(PriorityEnum.VERY_LOW)
public class MovementHandlerDefault implements IMovementHandler {

    private IMovable findMovable(World world, int x, int y, int z) {

        Block b = world.getBlock(x, y, z);
        if (b instanceof IMovable)
            return (IMovable) b;

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IMovable)
            return (IMovable) te;

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp != null)
            for (TMultiPart p : tmp.jPartList())
                if (p instanceof IMovable)
                    return (IMovable) p;// FIXME actual multipart handling

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, x, y, z);
        if (holder != null)
            for (IPart p : holder.getParts())
                if (p instanceof IMovable)
                    return (IMovable) p;// FIXME actual multipart handling

        return null;
    }

    @Override
    public boolean canHandle(World world, int x, int y, int z) {

        return true;
    }

    @Override
    public BlockMovementType getMovementType(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        IMovable movable = findMovable(world, x, y, z);
        if (movable != null)
            return movable.getMovementType(world, x, y, z, side, movement);

        return !world.getBlock(x, y, z).getMaterial().isReplaceable() ? BlockMovementType.MOVABLE : BlockMovementType.REPLACEABLE;
    }

    @Override
    public boolean startMoving(IMovingBlock block) {

        IMovable movable = findMovable(block.getWorld(), block.getX(), block.getY(), block.getZ());
        if (movable != null)
            return movable.startMoving(block);

        return false;
    }

    @Override
    public boolean finishMoving(IMovingBlock block) {

        IMovable movable = findMovable(block.getWorld(), block.getX(), block.getY(), block.getZ());
        if (movable != null)
            return movable.finishMoving(block);

        return false;
    }

}
