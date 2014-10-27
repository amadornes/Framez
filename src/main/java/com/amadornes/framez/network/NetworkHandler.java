package com.amadornes.framez.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.amadornes.framez.network.packet.PacketBlockSide;
import com.amadornes.framez.network.packet.PacketModifierList;
import com.amadornes.framez.network.packet.PacketRequestModifierList;
import com.amadornes.framez.network.packet.PacketStartMoving;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler {

    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);
    private static int lastDiscriminator = 0;

    public static void init() {

        registerPacket(PacketStartMoving.class, Side.CLIENT);
        registerPacket(PacketModifierList.class, Side.SERVER);
        registerPacket(PacketRequestModifierList.class, Side.CLIENT);
        registerPacket(PacketBlockSide.class, Side.SERVER);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void registerPacket(Class packetHandler, Class packetType, Side side) {

        NETWORK_WRAPPER.registerMessage(packetHandler, packetType, lastDiscriminator++, side);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void registerPacket(Class packetType, Side side) {

        NETWORK_WRAPPER.registerMessage(packetType, packetType, lastDiscriminator++, side);
    }

    public static void sendToAll(IMessage packet) {

        NETWORK_WRAPPER.sendToAll(packet);
    }

    public static void sendTo(IMessage packet, EntityPlayerMP player) {

        NETWORK_WRAPPER.sendTo(packet, player);
    }

    @SuppressWarnings("rawtypes")
    public static void sendToAllAround(LocatedPacket packet, World world, double range) {

        sendToAllAround(packet, packet.getTargetPoint(world, range));
    }

    public static void sendToAllAround(IMessage packet, NetworkRegistry.TargetPoint point) {

        NETWORK_WRAPPER.sendToAllAround(packet, point);
    }

    public static void sendToDimension(IMessage packet, int dimensionId) {

        NETWORK_WRAPPER.sendToDimension(packet, dimensionId);
    }

    public static void sendToServer(IMessage packet) {

        NETWORK_WRAPPER.sendToServer(packet);
    }

}
