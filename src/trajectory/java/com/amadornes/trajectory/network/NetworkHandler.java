package com.amadornes.trajectory.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.amadornes.trajectory.ModInfo;
import com.amadornes.trajectory.network.packet.PacketSingleBlockSync;
import com.amadornes.trajectory.network.packet.PacketStartMoving;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler {

    private static final NetworkHandler INSTANCE = new NetworkHandler(ModInfo.MODID);

    public static final NetworkHandler instance() {

        return INSTANCE;
    }

    public final SimpleNetworkWrapper wrapper;
    private int lastDiscriminator = 0;

    public NetworkHandler(String modid) {

        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(modid);
    }

    public static void init() {

        instance().registerPacket(PacketStartMoving.class, Side.CLIENT);
        instance().registerPacket(PacketSingleBlockSync.class, Side.CLIENT);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void registerPacket(Class packetHandler, Class packetType, Side side) {

        wrapper.registerMessage(packetHandler, packetType, lastDiscriminator++, side);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void registerPacket(Class packetType, Side side) {

        wrapper.registerMessage(packetType, packetType, lastDiscriminator++, side);
    }

    public void sendToAll(IMessage packet) {

        wrapper.sendToAll(packet);
    }

    public void sendTo(IMessage packet, EntityPlayerMP player) {

        wrapper.sendTo(packet, player);
    }

    @SuppressWarnings("rawtypes")
    public void sendToAllAround(LocatedPacket packet, World world, double range) {

        sendToAllAround(packet, packet.getTargetPoint(world, range));
    }

    @SuppressWarnings("rawtypes")
    public void sendToAllAround(LocatedPacket packet, World world) {

        sendToAllAround(packet, packet.getTargetPoint(world, 64));
    }

    public void sendToAllAround(IMessage packet, NetworkRegistry.TargetPoint point) {

        wrapper.sendToAllAround(packet, point);
    }

    public void sendToDimension(IMessage packet, int dimensionId) {

        wrapper.sendToDimension(packet, dimensionId);
    }

    public void sendToServer(IMessage packet) {

        wrapper.sendToServer(packet);
    }

}
