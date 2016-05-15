package com.amadornes.framez.compat.floco;

import codechicken.multipart.handler.MultipartProxy;

import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.Dependencies;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.movement.MovementRegistry;
import com.rwtema.funkylocomotion.proxydelegates.ProxyRegistry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import framesapi.IStickyBlock;

public class CompatModuleFLoco extends CompatModule {

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

        if (!FramezCompatConfig.floco_cross_stickiness)
            return;

        MovementRegistry.instance.registerStickinessHandler(new StickinesHandlerFLoco());

        FunkyLocomotionFramezProxy proxy = new FunkyLocomotionFramezProxy();
        if (Loader.isModLoaded(Dependencies.FMP))
            loadFMPSupport(proxy);
        for (BlockFrame b : FramezBlocks.frames.values())
            ProxyRegistry.register(b, proxy, IStickyBlock.class);
    }

    @Optional.Method(modid = Dependencies.FMP)
    private void loadFMPSupport(FunkyLocomotionFramezProxy proxy) {

        FunkyLocomotionFramezProxy.oldFMP = ProxyRegistry.getInterface(MultipartProxy.block(), IStickyBlock.class);
        ProxyRegistry.register(MultipartProxy.block(), proxy, IStickyBlock.class);
    }

}
