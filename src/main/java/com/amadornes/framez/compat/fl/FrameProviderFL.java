package com.amadornes.framez.compat.fl;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.HollowMicroblock;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.Microblock;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameProvider;
import com.rwtema.funkylocomotion.FunkyLocomotion;

import framesapi.IStickyBlock;

public class FrameProviderFL implements IFrameProvider {

    @Override
    public IFrame getFrameAt(World world, int x, int y, int z) {

        Block b = world.getBlock(x, y, z);

        if (b instanceof IStickyBlock)
            return new FunkyLocomotionFrame(world, x, y, z, (IStickyBlock) b);

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMultipart) {
            TileMultipart tmp = (TileMultipart) te;
            boolean hasFrame = false;
            for (int i = 0; i < 6; i++) {
                if (isFLMicroblock(tmp, i)) {
                    hasFrame = true;
                    break;
                }
            }
            if (hasFrame)
                return new FunkyLocomotionFrameMicroblock(tmp);
        }

        return null;
    }

    public static boolean isFLMicroblock(TileMultipart tmp, int side) {

        TMultiPart p = tmp.partMap(side);
        if (p != null
                && (p instanceof FaceMicroblock || p instanceof HollowMicroblock)
                && MicroMaterialRegistry.materialName(((Microblock) p).getMaterial()).equals(
                        BlockMicroMaterial.materialKey(FunkyLocomotion.frame[0], 0))) {
            return true;
        }
        return false;
    }
}
