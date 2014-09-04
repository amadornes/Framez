package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class StructureTickHandler {

    public static StructureTickHandler INST = new StructureTickHandler();

    private List<MovingStructure> structures = new ArrayList<MovingStructure>();

    private StructureTickHandler() {

    }

    public void addStructure(MovingStructure structure) {

        structures.add(structure);
    }

    public List<MovingStructure> getStructures() {

        return structures;
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {

        if (event.phase == Phase.END) {
            List<MovingStructure> invalid = new ArrayList<MovingStructure>();

            for (MovingStructure s : new ArrayList<MovingStructure>(structures)) {
                s.tick();
                if (s.getMoved() >= 1)
                    invalid.add(s);
            }

            for (MovingStructure s : invalid) {
                structures.remove(s);
            }
        }
    }

}
