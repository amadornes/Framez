package com.amadornes.framez.network;

import com.amadornes.framez.ModInfo;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    public static final NetworkHandler instance = new NetworkHandler(ModInfo.MODID);

    public final SimpleNetworkWrapper wrapper;
    private int lastDiscriminator = 0;

    public NetworkHandler(String modid) {

        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(modid);
    }

    public static void init() {

        instance.registerPacket(PacketShowGUI.class, Side.SERVER);
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
