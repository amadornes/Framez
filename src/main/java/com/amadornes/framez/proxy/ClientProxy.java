package com.amadornes.framez.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.amadornes.framez.client.IconProvider;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {

        MinecraftForge.EVENT_BUS.register(new IconProvider());
    }

}
