package com.amadornes.framez.part;

import com.amadornes.framez.block.BlockMetamorphicStone;
import com.amadornes.framez.block.BlockMetamorphicStone.Type;

import mcmultipart.microblock.IMicroblock;
import mcmultipart.microblock.MicroblockDelegate;
import net.minecraft.block.Block;

public class MicroDelegateMetamorphicStone extends MicroblockDelegate {

    private final Type type;

    public MicroDelegateMetamorphicStone(IMicroblock delegated, Type type) {

        super(delegated);
        this.type = type;
    }

    @Override
    public void onNeighborBlockChange(Block block) {

        BlockMetamorphicStone.convertIfNeeded(delegated.getWorld(), delegated.getPos(), delegated.getSlot().f1, type);
    }

    @Override
    public void onAdded() {

        BlockMetamorphicStone.convertIfNeeded(delegated.getWorld(), delegated.getPos(), delegated.getSlot().f1, type);
    }

}
