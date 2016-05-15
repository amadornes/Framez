package com.amadornes.trajectory.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.MultipartHelper;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.trajectory.api.IBlockMovementHandler;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.movement.MovingBlock;
import com.amadornes.trajectory.world.FakeWorldServer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockMovementHandlerMultipart implements IBlockMovementHandler {

    @Override
    public boolean canHandle(IMovingBlock block, ITrajectory trajectory) {

        return block.getTileEntity() instanceof TileMultipart;
    }

    @Override
    public boolean canBeMoved(IMovingBlock block, ITrajectory trajectory) {

        return true;
    }

    @Override
    public void startMoving(IMovingBlock block, ITrajectory trajectory) {

        TileMultipart tmp = (TileMultipart) block.getTileEntity();
        for (TMultiPart p : tmp.jPartList())
            p.onWorldSeparate();
        TrajectoryAPI.instance().defaultStartMoving(block, false, false, true);
    }

    @Override
    public void finishMoving(IMovingBlock block, ITrajectory trajectory) {

        TileMultipart tmp = (TileMultipart) block.getTileEntity();
        List<TMultiPart> parts = new ArrayList<TMultiPart>(tmp.jPartList());
        FakeWorldServer w = FakeWorldServer.instance(block.getWorld());
        w.setStructure(null);
        tmp.clearParts();
        w.setStructure(((MovingBlock) block).getStructure());
        TrajectoryAPI.instance().defaultFinishMoving(block, false, false);
        for (TMultiPart p : parts) {
            tmp.addPart_do(p);
            tmp.writeAddPart(p);
            p.tile_$eq(tmp);
        }
    }

    @SubscribeEvent
    public void onMoveFinishPreUpdate(TrajectoryEventMove.Structure.Finish.PreUpdate event) {

        for (IMovingBlock b : event.structure.getBlocks()) {
            TileEntity te = b.getTileEntity();
            if (te != null && te instanceof TileMultipart) {
                for (TMultiPart p : ((TileMultipart) te).jPartList())
                    p.onMoved();
                MultipartHelper.sendDescPacket(event.structure.getFakeWorld(), te);
            }
        }
    }

}
