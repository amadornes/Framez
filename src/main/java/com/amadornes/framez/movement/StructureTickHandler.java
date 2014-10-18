package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import com.amadornes.framez.Framez;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class StructureTickHandler {

    public static StructureTickHandler INST = new StructureTickHandler();

    private List<MovingStructure> structures = new ArrayList<MovingStructure>();

    public MovingStructure tickingStructure = null;

    private StructureTickHandler() {

    }

    public void addStructure(MovingStructure structure) {

        structures.add(structure);
    }

    public List<MovingStructure> getStructures() {

        return structures;
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {

        for (World w : MinecraftServer.getServer().worldServers)
            tick(w, event.phase);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {

        tick(Framez.proxy.getWorld(), event.phase);
    }

    private void tick(World world, TickEvent.Phase phase) {

        if (world == null)
            return;

        List<MovingStructure> invalid = new ArrayList<MovingStructure>();

        for (MovingStructure s : new ArrayList<MovingStructure>(structures)) {
            if (s.getWorld() != world)
                continue;

            tickingStructure = s;
            s.tick(phase);
            tickingStructure = null;

            if (s.getMoved() >= 1)
                invalid.add(s);
        }

        for (MovingStructure s : invalid)
            structures.remove(s);
    }

}
