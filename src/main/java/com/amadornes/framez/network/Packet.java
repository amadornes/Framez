package com.amadornes.framez.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Packet<REQ extends Packet<REQ>> implements IMessage, IMessageHandler<REQ, REQ> {

    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {

        if (synchronize()) {
            if (ctx.side == Side.SERVER) syncServer(message, ctx.getServerHandler().playerEntity);
            else syncClient(message, getPlayerClient());
        } else {
            if (ctx.side == Side.SERVER) message.handleServerSide(ctx.getServerHandler().playerEntity);
            else message.handleClientSide(getPlayerClient());
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private final void syncClient(final REQ packet, final EntityPlayer player) {

        Minecraft.getMinecraft().addScheduledTask(new Runnable() {

            @Override
            public void run() {

                packet.handleClientSide(player);
            }
        });
    }

    private final void syncServer(final REQ packet, final EntityPlayer player) {

        MinecraftServer.getServer().addScheduledTask(new Runnable() {

            @Override
            public void run() {

                packet.handleServerSide(player);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public EntityPlayer getPlayerClient() {

        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void toBytes(ByteBuf buf) {

        toBytes(new PacketBuffer(buf));
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        fromBytes(new PacketBuffer(buf));
    }

    protected boolean synchronize() {

        return true;
    }

    @SideOnly(Side.CLIENT)
    public abstract void handleClientSide(EntityPlayer player);

    public abstract void handleServerSide(EntityPlayer player);

    public abstract void toBytes(PacketBuffer buf);

    public abstract void fromBytes(PacketBuffer buf);

}
