package com.amadornes.framez.client;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.CCRenderState.IVertexOperation;

public class LightingNBNormals implements IVertexOperation {

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

        float ao = LightMatrix.sideao[CCRenderState.lc.side];

        CCRenderState.setBrightness(0xE000E0);
        CCRenderState.setColour(ColourRGBA.multiplyC(CCRenderState.colour, ao * (1 - (1 - ao) / 2)));
    }

    @Override
    public int operationID() {

        return operationIndex;
    }

}
