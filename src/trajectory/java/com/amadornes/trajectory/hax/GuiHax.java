package com.amadornes.trajectory.hax;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHax {

    @SuppressWarnings("unchecked")
    public static void doGuiHax() throws Exception {

        Field fc = NetworkRegistry.class.getDeclaredField("clientGuiHandlers");
        Field fs = NetworkRegistry.class.getDeclaredField("serverGuiHandlers");

        fc.setAccessible(true);
        fs.setAccessible(true);

        Map<ModContainer, IGuiHandler> client = ((Map<ModContainer, IGuiHandler>) fc.get(NetworkRegistry.INSTANCE));
        Map<ModContainer, IGuiHandler> server = ((Map<ModContainer, IGuiHandler>) fs.get(NetworkRegistry.INSTANCE));

        Iterator<Entry<ModContainer, IGuiHandler>> iClient = client.entrySet().iterator();
        List<Entry<ModContainer, IGuiHandler>> lClient = new ArrayList<Entry<ModContainer, IGuiHandler>>();
        while (iClient.hasNext()) {
            Entry<ModContainer, IGuiHandler> h = iClient.next();
            if (h.getValue() instanceof GuiHandlerWrapper)
                continue;
            lClient.add(new ImmutablePair<ModContainer, IGuiHandler>(h.getKey(), new GuiHandlerWrapper(h.getValue())));
            iClient.remove();
        }
        for (Entry<ModContainer, IGuiHandler> e : lClient)
            client.put(e.getKey(), e.getValue());

        Iterator<Entry<ModContainer, IGuiHandler>> iServer = server.entrySet().iterator();
        List<Entry<ModContainer, IGuiHandler>> lServer = new ArrayList<Entry<ModContainer, IGuiHandler>>();
        while (iServer.hasNext()) {
            Entry<ModContainer, IGuiHandler> h = iServer.next();
            if (h.getValue() instanceof GuiHandlerWrapper)
                continue;
            lServer.add(new ImmutablePair<ModContainer, IGuiHandler>(h.getKey(), new GuiHandlerWrapper(h.getValue())));
            iServer.remove();
        }
        for (Entry<ModContainer, IGuiHandler> e : lServer)
            server.put(e.getKey(), e.getValue());
    }
}
