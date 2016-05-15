package com.amadornes.framez.client;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.CCRenderState.IVertexOperation;

public class LightingNB implements IVertexOperation {

    public static final int operationIndex = CCRenderState.registerOperation();

    @Override
    public boolean load() {

        if (!CCRenderState.computeLighting)
            return false;

        CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
        CCRenderState.pipeline.addDependency(CCRenderState.lightCoordAttrib);
        return true;
    }

    @Override
    public void operate() {

        CCRenderState.setBrightness(0xE000E0);
    }

    @Override
    public int operationID() {

        return operationIndex;
    }

}
