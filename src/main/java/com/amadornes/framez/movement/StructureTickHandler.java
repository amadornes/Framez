package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.framez.Framez;
import com.amadornes.framez.util.BlockUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
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
    public void onWorldTick(ServerTickEvent event) {

        if (event.phase == Phase.END)
            for (World w : MinecraftServer.getServer().worldServers)
                tick(w);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {

        if (event.phase == Phase.END)
            tick(Framez.proxy.getWorld());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void tick(World world) {

        if (world == null)
            return;

        List<MovingStructure> invalid = new ArrayList<MovingStructure>();

        for (MovingStructure s : new ArrayList<MovingStructure>(structures)) {
            if (s.getWorld() != world)
                continue;
            tickingStructure = s;

            s.tick();
            if (s.getMoved() >= 1)
                invalid.add(s);

            tickingStructure = null;
        }

        for (MovingStructure s : invalid) {
            structures.remove(s);
        }

        List list = world.loadedTileEntityList;
        for (TileEntity te : BlockUtils.removedTEs) {
            if (te.getWorldObj() == world) {
                synchronized (list) {
                    list.remove(te);
                }
            }
        }
        BlockUtils.removedTEs.clear();

        for (TileEntity te : BlockUtils.addedTEs) {
            if (te.getWorldObj() == world) {
                synchronized (list) {
                    list.add(te);
                }
            }
        }
        BlockUtils.removedTEs.clear();
    }

}
