package com.amadornes.trajectory.util;

import net.minecraftforge.common.MinecraftForge;

import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.IMovingStructure;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.TrajectoryEvent;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventRender;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventHelper {

    public static void fireBlockMovementStartEvent(Phase phase, IMovingBlock block, ITrajectory trajectory) {

        MinecraftForge.EVENT_BUS.post(phase == Phase.START ? new TrajectoryEventMove.Block.Start.Pre(block, trajectory)
                : new TrajectoryEventMove.Block.Start.Post(block, trajectory));
    }

    public static boolean fireBlockMovementFinishEvent(Phase phase, IMovingBlock block, ITrajectory trajectory) {

        if (phase == Phase.START) {
            TrajectoryEventMove.Block.Finish.Pre ev = new TrajectoryEventMove.Block.Finish.Pre(block, trajectory);
            MinecraftForge.EVENT_BUS.post(ev);
            return ev.shouldApplyTransformation;
        } else {
            MinecraftForge.EVENT_BUS.post(new TrajectoryEventMove.Block.Finish.Post(block, trajectory));
            return false;
        }
    }

    public static void fireStructureMovementStartEvent(Phase phase, IMovingStructure structure, ITrajectory trajectory) {

        MinecraftForge.EVENT_BUS.post(phase == Phase.START ? new TrajectoryEventMove.Structure.Start.Pre(structure, trajectory)
                : new TrajectoryEventMove.Structure.Start.Post(structure, trajectory));
    }

    public static void fireStructureMovementPreUpdateEvent(IMovingStructure structure, ITrajectory trajectory) {

        MinecraftForge.EVENT_BUS.post(new TrajectoryEventMove.Structure.Finish.PreUpdate(structure, trajectory));
    }

    public static void fireStructureMovementFinishEvent(Phase phase, IMovingStructure structure, ITrajectory trajectory) {

        MinecraftForge.EVENT_BUS.post(phase == Phase.START ? new TrajectoryEventMove.Structure.Finish.Pre(structure, trajectory)
                : new TrajectoryEventMove.Structure.Finish.Post(structure, trajectory));

    }

    @SideOnly(Side.CLIENT)
    public static boolean fireRenderStaticEvent(IMovingBlock block, ITrajectory trajectory, int pass) {

        TrajectoryEvent.TrajectoryEventRender ev = new TrajectoryEventRender.Static(block, trajectory, pass);
        MinecraftForge.EVENT_BUS.post(ev);
        return ev.isCanceled();
    }

    @SideOnly(Side.CLIENT)
    public static boolean fireRenderDynamicEvent(IMovingBlock block, ITrajectory trajectory, int pass, float frame) {

        TrajectoryEvent.TrajectoryEventRender ev = new TrajectoryEventRender.Dynamic(block, trajectory, pass, frame);
        MinecraftForge.EVENT_BUS.post(ev);
        return ev.isCanceled();
    }

}
