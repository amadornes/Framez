package com.amadornes.framez.compat.fmp;

import java.util.BitSet;

import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroblockClass;
import codechicken.microblock.MicroblockGenerator.IGeneratedMaterial;

import com.amadornes.framez.init.FramezBlocks;

public class MicroMaterialStopper extends BlockMicroMaterial implements IGeneratedMaterial {

    public MicroMaterialStopper() {

        super(FramezBlocks.stopper, 0);
    }

    @Override
    public void addTraits(BitSet traits, MicroblockClass microClass, boolean client) {

        // if (!client && (microClass.getName().equals("mcr_face") || microClass.getName().equals("mcr_hllw")))
        // traits.set(CompatModuleFMP.traitIDStopper);
    }

}
