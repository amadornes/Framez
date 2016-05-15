package com.amadornes.framez.client;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.CCRenderState.IVertexOperation;

public class RenderColor implements IVertexOperation {

    public static final int operationIndex = CCRenderState.registerOperation();
    private double r, g, b, a;

    public RenderColor(double r, double g, double b, double a) {

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public boolean load() {

        CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
        return true;
    }

    @Override
    public void operate() {

        CCRenderState.setColour(ColourRGBA.multiply(CCRenderState.colour, new ColourRGBA(r, g, b, a).rgba()));
    }

    @Override
    public int operationID() {

        return operationIndex;
    }

}
