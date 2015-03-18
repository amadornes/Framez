package com.amadornes.framez.movement.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;
import codechicken.lib.vec.BlockCoord;
import codechicken.microblock.CommonMicroblock;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.HollowMicroblock;
import codechicken.multipart.PartMap;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;
import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.api.movement.IStickyProvider;

public class MovementHandlerFMP implements IMovementHandler, IStickyProvider {

    @Override
    @Priority(PriorityEnum.OVERRIDE)
    public BlockMovementType getMovementType(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return null;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof TSlottedPart) {
                int slot = ((TSlottedPart) p).getSlotMask();
                if (slot == PartMap.face(side.ordinal()).mask && (p instanceof FaceMicroblock || p instanceof HollowMicroblock)) {
                    if (((CommonMicroblock) p).getSize() == 1)
                        return BlockMovementType.UNMOVABLE;
                }
            }
        }

        return null;
    }

    @Override
    public boolean startMoving(IMovingBlock block) {

        TileMultipart tmp = (TileMultipart) block.getTileEntity();
        if (!block.getWorld().isRemote)
            for (TMultiPart p : tmp.jPartList())
                p.onWorldSeparate();

        block.startMoving(true, false);

        if (!block.getWorld().isRemote) {
            for (TMultiPart p : tmp.jPartList()) {
                p.onWorldJoin();
                p.onMoved();
            }
        }

        return true;
    }

    @Override
    public boolean finishMoving(IMovingBlock block) {

        TileMultipart tmp = (TileMultipart) block.getTileEntity();
        if (!block.getWorld().isRemote)
            for (TMultiPart p : tmp.jPartList())
                p.onWorldSeparate();

        if (!block.getWorld().isRemote) {
            List<TMultiPart> l = new ArrayList<TMultiPart>(tmp.jPartList());
            if (tmp != null)
                tmp.clearParts();
            Vec3i v = block.getStructure().getMovement().transform(new Vec3i(block));
            for (TMultiPart p : l) {
                p.onWorldSeparate();
                TileMultipart.addPart(block.getWorld(), new BlockCoord(v.getX(), v.getY(), v.getZ()), p);
                p.onMoved();
            }
        }

        return true;
    }

    @Override
    public boolean canHandle(World world, int x, int y, int z) {

        return TileMultipart.getTile(world, new BlockCoord(x, y, z)) != null;
    }

    @Override
    public ISticky getStickyAt(World world, int x, int y, int z) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp != null)
            return StickyHandlerFMP.instance();
        return null;
    }

    @Override
    public IFrame getFrameAt(World world, int x, int y, int z) {

        // TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        // if (tmp != null)
        // return StickyHandlerFMP.instance();
        return null;
    }

}
