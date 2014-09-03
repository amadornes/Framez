package com.amadornes.framez.world;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;

public class WorldWrapper extends World {

    private MovingStructure structure;

    public WorldWrapper(MovingStructure structure) {

        super(structure.getWorld().getSaveHandler(), structure.getWorld().getWorldInfo().getWorldName(), structure.getWorld().provider,
                new WorldSettings(structure.getWorld().getWorldInfo()), structure.getWorld().theProfiler);

        this.structure = structure;
    }

    @Override
    protected IChunkProvider createChunkProvider() {

        return null;
    }

    @Override
    protected int func_152379_p() {

        return Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
    }

    private MovingBlock get(int x, int y, int z) {

        if (structure.getMoved() >= 1)
            return null;

        return structure.getBlock(x, y, z);
    }

    @Override
    public Block getBlock(int x, int y, int z) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            return b.getBlock();

        return Blocks.air;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            return b.getTileEntity();

        return null;
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int unknown) {

        return structure.getWorld().getLightBrightnessForSkyBlocks(x, y, z, unknown);
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            return b.getMetadata();

        return 0;
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int face) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            return b.getBlock().isProvidingStrongPower(this, x, y, z, face);

        return 0;
    }

    @Override
    public boolean isAirBlock(int x, int y, int z) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            return false;

        return true;
    }

    @Override
    public Entity getEntityByID(int id) {

        return structure.getWorld().getEntityByID(id);
    }

    @Override
    public boolean spawnEntityInWorld(Entity entity) {

        entity.posX += structure.getDirection().offsetX * structure.getMoved();
        entity.posY += structure.getDirection().offsetY * structure.getMoved();
        entity.posZ += structure.getDirection().offsetZ * structure.getMoved();

        return structure.getWorld().spawnEntityInWorld(entity);
    }

    @Override
    public void spawnParticle(String type, double x, double y, double z, double r, double g, double b) {

        structure.getWorld()
                .spawnParticle(type, x + (structure.getDirection().offsetX * structure.getMoved()),
                        y + (structure.getDirection().offsetY * structure.getMoved()), z + (structure.getDirection().offsetZ * structure.getMoved()),
                        r, g, b);
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setBlock(block);
        return true;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setBlock(block);
        b.setMeta(meta);
        return true;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int notify) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setMeta(meta);
        return true;
    }

    @Override
    public boolean setBlockToAir(int x, int y, int z) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setBlock(Blocks.air);
        return true;
    }

    @Override
    public void setTileEntity(int x, int y, int z, TileEntity te) {

    }

    @Override
    public void removeTileEntity(int x, int y, int z) {

    }

    @Override
    public void tick() {

    }

    @Override
    public void updateEntities() {

    }

    @Override
    public void playSound(double x, double y, double z, String sound, float volume, float pitch, boolean unknown) {

        structure.getWorld().playSound(x, y, z, sound, volume, pitch, unknown);
    }

    @Override
    public void playSoundAtEntity(Entity entity, String sound, float volume, float pitch) {

        structure.getWorld().playSoundAtEntity(entity, sound, volume, pitch);
    }

    @Override
    public void playSoundEffect(double x, double y, double z, String sound, float volume, float pitch) {

        structure.getWorld().playSoundEffect(x, y, z, sound, volume, pitch);
    }

    @Override
    public void playAuxSFX(int x, int y, int z, int a, int b) {

        structure.getWorld().playAuxSFX(x, y, z, a, b);
    }

    @Override
    public void playAuxSFXAtEntity(EntityPlayer player, int x, int y, int z, int a, int b) {

        structure.getWorld().playAuxSFXAtEntity(player, x, y, z, a, b);
    }

    @Override
    public void playSoundToNearExcept(EntityPlayer player, String sound, float volume, float pitch) {

        structure.getWorld().playSoundToNearExcept(player, sound, volume, pitch);
    }

    @Override
    public boolean updateLightByType(EnumSkyBlock p_147463_1_, int p_147463_2_, int p_147463_3_, int p_147463_4_) {

        return false;
    }

    @Override
    public Chunk getChunkFromBlockCoords(int x, int z) {

        return structure.getWorld().getChunkFromBlockCoords(x, z);
    }

    @Override
    public Chunk getChunkFromChunkCoords(int x, int z) {

        return structure.getWorld().getChunkFromChunkCoords(x, z);
    }

    @Override
    public IChunkProvider getChunkProvider() {

        return structure.getWorld().getChunkProvider();
    }

    @Override
    protected boolean chunkExists(int p_72916_1_, int p_72916_2_) {

        return getChunkProvider().chunkExists(p_72916_1_, p_72916_2_);
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            return b.getBlock().isSideSolid(this, x, y, z, side);

        return false;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            return b.getBlock().isSideSolid(this, x, y, z, side);

        return _default;
    }

    @Override
    public void notifyBlockChange(int p_147444_1_, int p_147444_2_, int p_147444_3_, Block p_147444_4_) {

    }

    @Override
    public void notifyBlockOfNeighborChange(int p_147460_1_, int p_147460_2_, int p_147460_3_, Block p_147460_4_) {

    }

    @Override
    public void notifyBlocksOfNeighborChange(int p_147441_1_, int p_147441_2_, int p_147441_3_, Block p_147441_4_, int p_147441_5_) {

    }

    @Override
    public void notifyBlocksOfNeighborChange(int p_147459_1_, int p_147459_2_, int p_147459_3_, Block p_147459_4_) {

    }

}
