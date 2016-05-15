package com.amadornes.trajectory.movement;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.trajectory.api.IBlockDescriptionProvider.IPrioritisedBlockDescriptionProvider;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.block.TileMoving;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public abstract class BlockDescriptionProviderBase implements IPrioritisedBlockDescriptionProvider {

    @Override
    public void writeBlockData(IMovingBlock block, NBTTagCompound tag) {

        UniqueIdentifier uuid = GameRegistry.findUniqueIdentifierFor(block.getBlock());
        tag.setString("blockMod", uuid.modId);
        tag.setString("blockName", uuid.name);
        tag.setInteger("meta", block.getMetadata());
    }

    @Override
    public void readBlockData(IMovingBlock block, NBTTagCompound tag) {

        block.setBlock(GameRegistry.findBlock(tag.getString("blockMod"), tag.getString("blockName")));
        block.setMetadata(tag.getInteger("meta"));
        TileEntity te = block.getTileEntity();
        if ((te == null || te instanceof TileMoving) && block.getBlock().hasTileEntity(block.getMetadata()))
            block.setTileEntity(te = block.getBlock().createTileEntity(block.getFakeWorld(), block.getMetadata()));
        else if (te != null && te instanceof TileMoving)
            block.setTileEntity(te = null);

        if (te != null) {
            te.setWorldObj(block.getFakeWorld());
            te.xCoord = block.getPosition().x;
            te.yCoord = block.getPosition().y;
            te.zCoord = block.getPosition().z;

            te.blockType = block.getBlock();
            te.blockMetadata = block.getMetadata();
        }
    }
}
