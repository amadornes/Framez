package com.amadornes.framez.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("rawtypes")
public abstract class Packet<REQ extends Packet> implements IMessage, IMessageHandler<REQ, REQ> {

    @SuppressWarnings("unchecked")
    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {

        if (ctx.side == Side.SERVER) {
            if (message.getClass() == getClass())
                message.handleServerSide(message, ctx.getServerHandler().playerEntity);
            else
                handleServerSide(message, ctx.getServerHandler().playerEntity);
        } else {
            if (message.getClass() == getClass())
                message.handleClientSide(message, getPlayerClient());
            else
                handleClientSide(message, getPlayerClient());
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public EntityPlayer getPlayerClient() {

        return Minecraft.getMinecraft().thePlayer;
    }

    @SideOnly(Side.CLIENT)
    public abstract void handleClientSide(REQ message, EntityPlayer player);

    @SideOnly(Side.SERVER)
    public abstract void handleServerSide(REQ message, EntityPlayer player);

    @Override
    public void fromBytes(ByteBuf buf) {

        read(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {

        NBTTagCompound tag = new NBTTagCompound();
        write(tag);
        ByteBufUtils.writeTag(buf, tag);
    }

    public abstract void read(NBTTagCompound tag);

    public abstract void write(NBTTagCompound tag);
}
