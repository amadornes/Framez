package com.amadornes.framez.frame;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.api.frame.IFrameRegistry;

public enum FrameRegistry implements IFrameRegistry {

    INSTANCE;

    public Map<String, IFrameMaterial> materials = new LinkedHashMap<String, IFrameMaterial>();
    public Map<String, IFrameMaterial> internalMaterials = new LinkedHashMap<String, IFrameMaterial>();

    @Override
    public void registerMaterial(IFrameMaterial material) {

        materials.put(material.getType(), material);
    }

    public void registerMaterialInternal(IFrameMaterial material) {

        registerMaterial(material);
        internalMaterials.put(material.getType(), material);
    }

}
