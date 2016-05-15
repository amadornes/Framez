package com.amadornes.framez.compat.fmp;

import java.util.BitSet;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import codechicken.lib.render.uv.MultiIconTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MaterialRenderHelper;
import codechicken.microblock.MicroblockClass;
import codechicken.microblock.MicroblockGenerator.IGeneratedMaterial;

public class MicroMaterialFrame extends BlockMicroMaterial implements IGeneratedMaterial {

    public MicroMaterialFrame(Block block, int meta) {

        super(block, meta);
    }

    @Override
    public void renderMicroFace(Vector3 pos, int pass, Cuboid6 bounds) {

        IIcon icon = block().getIcon(0, 0);
        MultiIconTransformation icont = new MultiIconTransformation(icon, icon, icon, icon, icon, icon);
        MaterialRenderHelper.start(pos, pass, icont).blockColour(getColour(pass)).lighting().render();

        // IIcon icon = block().getIcon(0, 0);
        //
        // if (bounds.getSide(0) == 0 / 16D && bounds.getSide(1) == 2 / 16D) {
        // RenderHelper rh = (RenderHelper) FramezApi.instance().client().renderer();
        // rh.start(pass);
        // rh.builder.add(new Translation(pos.x, pos.y, pos.z));
        // rh.render(RenderHelper.frameModels[0], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[1], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[2], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[3], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[8], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[9], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[10], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[11], icon, icon, icon, icon, icon, icon);
        // rh.renderFaceY(0, 0, 1, 1, 1 / 16D, icon, false);
        // rh.renderFaceY(0, 0, 1, 1, 1 / 16D, icon, true);
        // } else if (bounds.getSide(0) == 14 / 16D && bounds.getSide(1) == 16 / 16D) {
        // RenderHelper rh = (RenderHelper) FramezApi.instance().client().renderer();
        // rh.start(pass);
        // rh.builder.add(new Translation(pos.x, pos.y, pos.z));
        // rh.render(RenderHelper.frameModels[4], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[5], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[6], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[7], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[12], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[13], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[14], icon, icon, icon, icon, icon, icon);
        // rh.render(RenderHelper.frameModels[15], icon, icon, icon, icon, icon, icon);
        // rh.renderFaceY(0, 0, 1, 1, 15 / 16D, icon, false);
        // rh.renderFaceY(0, 0, 1, 1, 15 / 16D, icon, true);
        // }
        //
        // if (false) {
        // MultiIconTransformation icont = new MultiIconTransformation(icon, icon, icon, icon, icon, icon);
        // MaterialRenderHelper.start(pos, pass, icont).blockColour(getColour(pass)).lighting().render();
        // } else {
        // super.renderMicroFace(pos, pass, bounds);
        // }
    }

    @Override
    public void addTraits(BitSet traits, MicroblockClass microClass, boolean client) {

        if (microClass.getName().equals("mcr_face") || microClass.getName().equals("mcr_hllw"))
            traits.set(CompatModuleFMP.traitIDFrame);
    }

}
