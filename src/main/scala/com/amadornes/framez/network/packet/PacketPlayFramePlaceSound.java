package com.amadornes.framez.network.packet;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.amadornes.framez.network.LocatedPacket;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketPlayFramePlaceSound extends LocatedPacket<PacketPlayFramePlaceSound> {

    public PacketPlayFramePlaceSound(int x, int y, int z) {

        super(x, y, z);
    }

    public PacketPlayFramePlaceSound() {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer player) {

        Block.SoundType sound = Block.soundTypeStone;
        Minecraft
                .getMinecraft()
                .getSoundHandler()
                .playSound(
                        new PositionedSoundRecord(new ResourceLocation(sound.func_150496_b()), (sound.getVolume() + 3)
                                * Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.BLOCKS), sound.getPitch() * 0.85F,
                                x + 0.5F, y + 0.5F, z + 0.5F));
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

}
