package com.amadornes.framez.event;

import net.minecraft.entity.player.EntityPlayerMP;

import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketRequestModifierList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler {

    @SubscribeEvent
    public void onLogin(PlayerLoggedInEvent event) {

        NetworkHandler.sendTo(new PacketRequestModifierList(), (EntityPlayerMP) event.player);
    }

}
