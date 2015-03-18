package com.amadornes.framez.network;

import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.relauncher.Side;

public class NetworkHandler extends uk.co.qmunity.lib.network.NetworkHandler {

    private static final NetworkHandler instance = new NetworkHandler();

    public static NetworkHandler instance() {

        return instance;
    }

    private NetworkHandler() {

        super(ModInfo.MODID);
    }

    public void init() {

        registerPacket(PacketStartMoving.class, Side.CLIENT);
        registerPacket(PacketBlockSync.class, Side.CLIENT);
        registerPacket(PacketSingleBlockSync.class, Side.CLIENT);
        registerPacket(PacketWrenchMode.class, Side.SERVER);
        registerPacket(PacketMotorSetting.class, Side.SERVER);
    }

}
