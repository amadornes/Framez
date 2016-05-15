package com.amadornes.trajectory.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import com.amadornes.trajectory.Trajectory;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.IMovingStructure;
import com.amadornes.trajectory.world.FakeWorld;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class MovementScheduler {

    public static MovementScheduler instance = new MovementScheduler();

    public MovingStructure tickingStructure = null;

    private MovementScheduler() {

    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {

        if (Trajectory.proxy.isGamePaused())
            return;

        for (World w : MinecraftServer.getServer().worldServers)
            tick(w, event.phase);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {

        if (Trajectory.proxy.isGamePaused())
            return;

        tick(Trajectory.proxy.getWorld(), event.phase);
    }

    private void tick(World world, Phase phase) {

        if (world == null)
            return;

        List<MovingStructure> invalid = new ArrayList<MovingStructure>();

        for (MovingStructure s : new ArrayList<MovingStructure>(MovementManager.instance.getStructures(world.isRemote))) {
            if (s == null) {
                invalid.add(s);
                continue;
            }
            if (s.getWorld() != world)
                continue;

            tickingStructure = s;
            s.tick(phase);
            tickingStructure = null;

            if (s.getTrajectory().getProgress(s.getTicksMoved()) >= 1)
                invalid.add(s);
        }

        for (IMovingStructure s : invalid)
            MovementManager.instance.getStructures(world.isRemote).remove(s);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {

        if (FakeWorld.isFakeWorld(event.world))
            return;

        FakeWorld.initFakeWorld(event.world);
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {

        if (event.world.isRemote)
            return;

        for (MovingStructure s : new ArrayList<MovingStructure>(MovementManager.instance.getStructures(event.world.isRemote))) {
            if (s.getWorld() == event.world) {
                for (IMovingBlock b : s.getBlocks()) {
                    if ((b.getPosition().x >> 4) == event.getChunk().xPosition && (b.getPosition().z >> 4) == event.getChunk().zPosition) {
                        s.finishMoving();
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {

        if (event.world.isRemote)
            return;

        boolean stopped = false;
        for (MovingStructure s : new ArrayList<MovingStructure>(MovementManager.instance.getStructures(event.world.isRemote))) {
            if (s.getWorld() == event.world) {
                s.finishMoving();
                stopped = true;
            }
        }
        if (stopped && event.world instanceof WorldServer) {
            try {
                ((WorldServer) event.world).saveAllChunks(true, null);
            } catch (MinecraftException e) {
                e.printStackTrace();
            }
        }
    }

}
