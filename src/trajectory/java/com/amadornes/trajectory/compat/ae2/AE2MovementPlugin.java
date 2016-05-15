package com.amadornes.trajectory.compat.ae2;

import java.lang.reflect.Method;

import net.minecraftforge.common.util.ForgeDirection;
import appeng.api.parts.IFacadePart;
import appeng.api.parts.IPart;
import appeng.block.AEBaseBlock;
import appeng.parts.CableBusContainer;
import appeng.parts.CableBusStorage;
import appeng.tile.networking.TileCableBus;

import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.util.MiscUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class AE2MovementPlugin {

    private static Method mGetSide = ReflectionHelper.findMethod(CableBusStorage.class, null, new String[] { "getSide" },
            ForgeDirection.class);
    private static Method mSetSide = ReflectionHelper.findMethod(CableBusStorage.class, null, new String[] { "setSide" },
            ForgeDirection.class, IPart.class);
    private static Method mGetFacade = ReflectionHelper.findMethod(CableBusStorage.class, null, new String[] { "getFacade" }, int.class);
    private static Method mSetFacade = ReflectionHelper.findMethod(CableBusStorage.class, null, new String[] { "setFacade" }, int.class,
            IFacadePart.class);

    static {
        mGetSide.setAccessible(true);
        mSetSide.setAccessible(true);
        mGetFacade.setAccessible(true);
        mSetFacade.setAccessible(true);
    }

    @SubscribeEvent
    public void onMoveFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        if (event.block.getBlock() instanceof AEBaseBlock && event.trajectory instanceof ITrajectoryRotation) {
            ITrajectoryRotation t = (ITrajectoryRotation) event.trajectory;
            for (int i = 0; i < t.getRotations() * 2; i++)
                event.block.getBlock().rotateBlock(event.block.getFakeWorld(), event.block.getPosition().x, event.block.getPosition().y,
                        event.block.getPosition().z, ForgeDirection.getOrientation(t.getAxis()));
        }

        if (event.block.getTileEntity() == null || !(event.block.getTileEntity() instanceof TileCableBus))
            return;

        if (event.trajectory instanceof ITrajectoryRotation)
            rotate(((TileCableBus) event.block.getTileEntity()).cb, ((ITrajectoryRotation) event.trajectory).getAxis());
    }

    public static void rotate(CableBusContainer cb, int rotAxis) {

        try {
            IPart[] newSides = new IPart[6];
            IFacadePart[] newFacades = new IFacadePart[6];

            for (int i = 0; i < 6; i++) {
                newSides[i] = (IPart) mGetSide.invoke(cb, ForgeDirection.getOrientation(MiscUtils.rotate(i, rotAxis)));
                if (newSides[i] != null)
                    newSides[i].setPartHostInfo(ForgeDirection.getOrientation(i), cb.tcb, cb.getTile());
                newFacades[i] = (IFacadePart) mGetFacade.invoke(cb, MiscUtils.rotate(i, rotAxis));
            }
            for (int i = 0; i < 6; i++) {
                mSetSide.invoke(cb, ForgeDirection.getOrientation(i), newSides[i]);
                mSetFacade.invoke(cb, i, newFacades[i]);
            }

            cb.updateConnections();
            cb.markForUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
