package com.amadornes.framez.util;

import java.util.UUID;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import com.mojang.authlib.GameProfile;

public class FramezFakePlayer {

    private static GameProfile FRAMEZ_PROFILE = new GameProfile(UUID.randomUUID(), "[Framez]");

    public static FakePlayer get(WorldServer world) {

        return FakePlayerFactory.get(world, FRAMEZ_PROFILE);
    }

}
