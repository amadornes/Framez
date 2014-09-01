package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class StructureTickHandler {

    public static StructureTickHandler inst = new StructureTickHandler();

    private List<MovingStructure> structures = new ArrayList<MovingStructure>();

    private StructureTickHandler() {

    }

    public void addStructure(MovingStructure structure) {

        structures.add(structure);
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {

        if (event.phase == Phase.START) {
            List<MovingStructure> invalid = new ArrayList<MovingStructure>();

            for (MovingStructure s : new ArrayList<MovingStructure>(structures)) {
                s.tick();
                if (!s.isValid())
                    invalid.add(s);
            }

            for (MovingStructure s : invalid) {
                structures.remove(s);
            }
        }
    }

}
