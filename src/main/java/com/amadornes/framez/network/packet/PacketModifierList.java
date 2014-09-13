package com.amadornes.framez.network.packet;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.network.Packet;

public class PacketModifierList extends Packet<PacketModifierList> {

    private boolean extra = false;
    private boolean kick = false;
    private List<String> l = new ArrayList<String>();

    @Override
    public void handleClientSide(PacketModifierList message, EntityPlayer player) {

    }

    @Override
    public void handleServerSide(PacketModifierList message, EntityPlayer player) {

        for (String s : l) {
            if (ModifierRegistry.INST.getModifierProvider(s) == null) {
                kick = true;
                extra = true;
                break;
            }
        }
        if (!kick) {
            for (IFrameModifierProvider m : ModifierRegistry.INST.getProviders()) {
                if (!l.contains(m.getIdentifier())) {
                    kick = true;
                    break;
                }
            }
        }

        if (kick) {
            if (extra) {
                ((EntityPlayerMP) player).playerNetServerHandler
                        .kickPlayerFromServer("You have some frame modifiers that aren't available on the server!");
            } else {
                ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("You're missing some frame modifiers!");
            }
        }
    }

    @Override
    public void read(NBTTagCompound tag) {

        NBTTagList list = tag.getTagList("modifiers", new NBTTagString().getId());
        for (int i = 0; i < list.tagCount(); i++)
            l.add(list.getStringTagAt(i));

    }

    @Override
    public void write(NBTTagCompound tag) {

        NBTTagList list = new NBTTagList();
        for (IFrameModifierProvider p : ModifierRegistry.INST.getProviders())
            list.appendTag(new NBTTagString(p.getIdentifier()));

        tag.setTag("modifiers", list);
    }

}
