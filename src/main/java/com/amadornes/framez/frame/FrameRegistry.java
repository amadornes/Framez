package com.amadornes.framez.frame;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.frame.IFrame;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.api.frame.IFrameRegistry;
import com.amadornes.framez.api.frame.IStickable;
import com.amadornes.framez.api.frame.IStickinessProvider;
import com.amadornes.framez.api.frame.ISticky;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public enum FrameRegistry implements IFrameRegistry {

    INSTANCE;

    private Map<ResourceLocation, IFrameMaterial> materials = new LinkedHashMap<ResourceLocation, IFrameMaterial>();
    private Map<ResourceLocation, IFrameMaterial> internalMaterials = new LinkedHashMap<ResourceLocation, IFrameMaterial>();

    private Map<Integer, IStickinessProvider> stickinessProviders = new TreeMap<Integer, IStickinessProvider>(
            (a, b) -> Integer.compare(b, a));

    @Override
    public void registerMaterial(IFrameMaterial material) {

        materials.put(material.getType(), material);
    }

    public void registerMaterialInternal(IFrameMaterial material) {

        registerMaterial(material);
        internalMaterials.put(material.getType(), material);
    }

    @Override
    public void registerStickinessProvider(IStickinessProvider provider) {

        registerStickinessProvider(provider, 0);
    }

    @Override
    public void registerStickinessProvider(IStickinessProvider provider, int priority) {

        stickinessProviders.put(priority, provider);
    }

    @Override
    public ISticky getSticky(IBlockAccess world, BlockPos pos, EnumFacing face) {

        for (IStickinessProvider provider : stickinessProviders.values()) {
            ISticky sticky = provider.getSticky(world, pos, face);
            if (sticky != null) return sticky;
        }

        TileEntity te = world.getTileEntity(pos);
        return te == null || !te.hasCapability(ISticky.CAPABILITY_STICKY, face) ? null : te.getCapability(ISticky.CAPABILITY_STICKY, face);
    }

    public ISticky getSticky(IBlockAccess world, BlockPos pos, EnumFacing face, TileEntity te) {

        for (IStickinessProvider provider : stickinessProviders.values()) {
            ISticky sticky = provider.getSticky(world, pos, face);
            if (sticky != null) return sticky;
        }

        return te == null || !te.hasCapability(ISticky.CAPABILITY_STICKY, face) ? null : te.getCapability(ISticky.CAPABILITY_STICKY, face);
    }

    @Override
    public IStickable getStickable(IBlockAccess world, BlockPos pos, EnumFacing face) {

        for (IStickinessProvider provider : stickinessProviders.values()) {
            IStickable stickable = provider.getStickable(world, pos, face);
            if (stickable != null) return stickable;
        }

        TileEntity te = world.getTileEntity(pos);
        return te == null || !te.hasCapability(IStickable.CAPABILITY_STICKABLE, face) ? null
                : te.getCapability(IStickable.CAPABILITY_STICKABLE, face);
    }

    public IStickable getStickable(IBlockAccess world, BlockPos pos, EnumFacing face, TileEntity te) {

        for (IStickinessProvider provider : stickinessProviders.values()) {
            IStickable stickable = provider.getStickable(world, pos, face);
            if (stickable != null) return stickable;
        }

        return te == null || !te.hasCapability(IStickable.CAPABILITY_STICKABLE, face) ? null
                : te.getCapability(IStickable.CAPABILITY_STICKABLE, face);
    }

    @Override
    public IFrame getFrame(IBlockAccess world, BlockPos pos) {

        return getFrame(world.getTileEntity(pos));
    }

    public IFrame getFrame(TileEntity te) {

        return te == null || !te.hasCapability(IFrame.CAPABILITY_FRAME, null) ? null : te.getCapability(IFrame.CAPABILITY_FRAME, null);
    }

    public IFrameMaterial getMaterial(ResourceLocation name) {

        if (name.getResourceDomain().equals("minecraft")) name = new ResourceLocation(ModInfo.MODID, name.getResourcePath());
        return materials.get(name);
    }

    public Map<ResourceLocation, IFrameMaterial> getMaterials() {

        return materials;
    }

}
