package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;

import com.amadornes.framez.util.BlockUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {

        if (event.phase == Phase.END) {
            List<MovingStructure> invalid = new ArrayList<MovingStructure>();

            for (MovingStructure s : new ArrayList<MovingStructure>(structures)) {
                tickingStructure = s;

                s.tick();
                if (s.getMoved() >= 1)
                    invalid.add(s);

                tickingStructure = null;
            }

            for (MovingStructure s : invalid) {
                structures.remove(s);
            }

            List list = event.world.loadedTileEntityList;
            for (TileEntity te : BlockUtils.removedTEs) {
                if (te.getWorldObj() == event.world) {
                    synchronized (list) {
                        list.remove(te);
                    }
                }
            }
            BlockUtils.removedTEs.clear();

            for (TileEntity te : BlockUtils.addedTEs) {
                if (te.getWorldObj() == event.world) {
                    synchronized (list) {
                        list.add(te);
                    }
                }
            }
            BlockUtils.removedTEs.clear();
        }
    }

}
