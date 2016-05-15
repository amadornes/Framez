package com.amadornes.framez.compat.fmp;

import java.util.BitSet;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.uv.MultiIconTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MaterialRenderHelper;
import codechicken.microblock.MicroblockClass;
import codechicken.microblock.MicroblockGenerator.IGeneratedMaterial;

import com.amadornes.framez.init.FramezBlocks;

public class MicroMaterialMetamorphicStone extends BlockMicroMaterial implements IGeneratedMaterial {

    public MicroMaterialMetamorphicStone(int meta) {

        super(FramezBlocks.metamorphic_stone, meta);
    }

    @Override
    public void renderMicroFace(Vector3 pos, int pass, Cuboid6 bounds) {

        if (meta() >= 3) {
            IIcon icon2 = meta() == 3 ? Blocks.water.getIcon(0, 0) : (meta() == 4 ? Blocks.lava.getIcon(0, 0) : block().getIcon(0, -1));
            MultiIconTransformation icont2 = new MultiIconTransformation(icon2, icon2, icon2, icon2, icon2, icon2);
            LightMatrix lm = CCRenderState.lightMatrix;
            CCRenderState.lightMatrix = RenderLightMatrix.inst;
            MaterialRenderHelper.start(pos, pass, icont2).blockColour(getColour(pass)).lighting().render();
            CCRenderState.lightMatrix = lm;
        }

        IIcon icon1 = block().getIcon(null, meta(), 0, 0, 0);
        MultiIconTransformation icont1 = new MultiIconTransformation(icon1, icon1, icon1, icon1, icon1, icon1);
        MaterialRenderHelper.start(pos, pass, icont1).blockColour(getColour(pass)).lighting().render();
    }

    @Override
    public void addTraits(BitSet traits, MicroblockClass microClass, boolean client) {

        if (microClass.getName().equals("mcr_face") || microClass.getName().equals("mcr_hllw"))
            traits.set(CompatModuleFMP.traitIDMetamorphicStone);
    }
}
