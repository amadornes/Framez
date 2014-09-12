package com.amadornes.framez.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.movement.StructureTickHandler;
import com.amadornes.framez.tile.TileMoving.UpdateType;

public class WorldWrapperServer extends WorldServer {

    private List<MovingStructure> structures = new ArrayList<MovingStructure>();
    private World world;

    private List<BlockCoord> toUpdate = new ArrayList<BlockCoord>();

    public WorldWrapperServer(World world) {

        super(MinecraftServer.getServer(), new NonSavingHandler(), world.getWorldInfo().getWorldName(), world.provider.dimensionId,
                new WorldSettings(world.getWorldInfo()), world.theProfiler);
        DimensionManager.setWorld(world.provider.dimensionId, (WorldServer) world);

        chunkProvider = world.getChunkProvider();

        this.world = world;
    }

    public void addStructure(MovingStructure structure) {

        structures.add(structure);
    }

    public void removeStructure(MovingStructure structure) {

        structures.remove(structure);
    }

    @Override
    protected void initialize(WorldSettings p_72963_1_) {

    }

    @Override
    protected IChunkProvider createChunkProvider() {

        return new EmptyChunkProvider();
    }

    @Override
    protected int func_152379_p() {

        return Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
    }

    private MovingBlock get(int x, int y, int z) {

        for (MovingStructure structure : structures) {
            if (structure.getMoved() >= 1)
                continue;

            MovingBlock b = structure.getBlock(x, y, z);
            if (b != null && !b.isStored())
                continue;
            return b;
        }
        return null;
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

        return world.getLightBrightnessForSkyBlocks(x, y, z, unknown);
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

        return world.getEntityByID(id);
    }

    @Override
    public boolean spawnEntityInWorld(Entity entity) {

        MovingStructure structure = StructureTickHandler.INST.tickingStructure;
        if (structure != null) {
            entity.posX += structure.getDirection().offsetX * structure.getMoved();
            entity.posY += structure.getDirection().offsetY * structure.getMoved();
            entity.posZ += structure.getDirection().offsetZ * structure.getMoved();
        }

        return world.spawnEntityInWorld(entity);
    }

    @Override
    public void spawnParticle(String type, double x, double y, double z, double r, double g, double b) {

        MovingStructure structure = StructureTickHandler.INST.tickingStructure;
        if (structure != null) {
            world.spawnParticle(type, x + (structure.getDirection().offsetX * structure.getMoved()), y
                    + (structure.getDirection().offsetY * structure.getMoved()), z + (structure.getDirection().offsetZ * structure.getMoved()), r, g,
                    b);
        } else {
            world.spawnParticle(type, x, y, z, r, g, b);
        }
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setBlock(block);

        if (b.getPlaceholder() != null)
            b.getPlaceholder().sendUpdatePacket(UpdateType.BLOCK);

        return true;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setBlock(block);
        b.setMetadata(meta);

        if (b.getPlaceholder() != null)
            b.getPlaceholder().sendUpdatePacket(UpdateType.BLOCK);

        return true;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int notify) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setMetadata(meta);

        if (b.getPlaceholder() != null)
            b.getPlaceholder().sendUpdatePacket(UpdateType.BLOCK);

        return true;
    }

    @Override
    public boolean setBlockToAir(int x, int y, int z) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setBlock(Blocks.air);

        if (b.getPlaceholder() != null)
            b.getPlaceholder().sendUpdatePacket(UpdateType.BLOCK);

        return true;
    }

    @Override
    public void setTileEntity(int x, int y, int z, TileEntity te) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            b.setTileEntity(te);
    }

    @Override
    public void removeTileEntity(int x, int y, int z) {

        MovingBlock b = get(x, y, z);
        if (b != null)
            b.setTileEntity(null);
    }

    @Override
    public void tick() {

        List<BlockCoord> toUpdateCurrent = new ArrayList<BlockCoord>(toUpdate);
        toUpdate.clear();
        for (BlockCoord b : toUpdateCurrent) {
            Block bl = getBlock(b.x, b.y, b.z);
            if (bl != null)
                bl.updateTick(this, b.x, b.y, b.z, rand);
        }
        toUpdateCurrent.clear();
    }

    @Override
    public void updateEntities() {

    }

    @Override
    public void playSound(double x, double y, double z, String sound, float volume, float pitch, boolean unknown) {

        world.playSound(x, y, z, sound, volume, pitch, unknown);
    }

    @Override
    public void playSoundAtEntity(Entity entity, String sound, float volume, float pitch) {

        world.playSoundAtEntity(entity, sound, volume, pitch);
    }

    @Override
    public void playSoundEffect(double x, double y, double z, String sound, float volume, float pitch) {

        world.playSoundEffect(x, y, z, sound, volume, pitch);
    }

    @Override
    public void playAuxSFX(int x, int y, int z, int a, int b) {

        world.playAuxSFX(x, y, z, a, b);
    }

    @Override
    public void playAuxSFXAtEntity(EntityPlayer player, int x, int y, int z, int a, int b) {

        world.playAuxSFXAtEntity(player, x, y, z, a, b);
    }

    @Override
    public void playSoundToNearExcept(EntityPlayer player, String sound, float volume, float pitch) {

        world.playSoundToNearExcept(player, sound, volume, pitch);
    }

    @Override
    public void playBroadcastSound(int x, int y, int z, int p_82739_4_, int p_82739_5_) {

        world.playBroadcastSound(x, y, z, p_82739_4_, p_82739_5_);
    }

    @Override
    public boolean updateLightByType(EnumSkyBlock p_147463_1_, int p_147463_2_, int p_147463_3_, int p_147463_4_) {

        return false;
    }

    @Override
    public Chunk getChunkFromBlockCoords(int x, int z) {

        return world.getChunkFromBlockCoords(x, z);
    }

    @Override
    public Chunk getChunkFromChunkCoords(int x, int z) {

        return world.getChunkFromChunkCoords(x, z);
    }

    @Override
    public IChunkProvider getChunkProvider() {

        return world.getChunkProvider();
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

    @Override
    public void saveAllChunks(boolean p_73044_1_, IProgressUpdate p_73044_2_) throws MinecraftException {

    }

    @Override
    public void saveChunkData() {

    }

    @Override
    protected void saveLevel() throws MinecraftException {

    }

    @Override
    public void joinEntityInSurroundings(Entity entity) {

        world.joinEntityInSurroundings(entity);
    }

    @Override
    public void removeEntity(Entity p_72900_1_) {

        world.removeEntity(p_72900_1_);
    }

    @Override
    public File getChunkSaveLocation() {

        return null;
    }

    @Override
    public void markBlockForUpdate(int x, int y, int z) {

        toUpdate.add(new BlockCoord(x, y, z));

        MovingBlock b = get(x, y, z);
        if (b != null && b.getPlaceholder() != null)
            b.getPlaceholder().sendUpdatePacket(UpdateType.ALL);
    }

    public World getRealWorld() {

        return world;
    }

    private static class NonSavingHandler implements ISaveHandler {

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {

        }

        @Override
        public void saveWorldInfo(WorldInfo p_75761_1_) {

        }

        @Override
        public WorldInfo loadWorldInfo() {

            return null;
        }

        @Override
        public String getWorldDirectoryName() {

            return null;
        }

        @Override
        public File getWorldDirectory() {

            return null;
        }

        @Override
        public IPlayerFileData getSaveHandler() {

            return null;
        }

        @Override
        public File getMapFileFromName(String p_75758_1_) {

            return null;
        }

        @Override
        public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {

            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public void checkSessionLock() {

        }
    }

    private static class EmptyChunkProvider extends AnvilChunkLoader implements IChunkProvider {

        public EmptyChunkProvider() {

            super(null);
        }

        @Override
        public boolean chunkExists(World world, int i, int j) {

            return true;
        }

        @Override
        public void chunkTick() {

        }

        @Override
        public Chunk loadChunk(World p_75815_1_, int p_75815_2_, int p_75815_3_) {

            return null;
        }

        @Override
        public Object[] loadChunk__Async(World p_75815_1_, int p_75815_2_, int p_75815_3_) {

            return null;
        }

        @Override
        public void loadEntities(World p_75823_1_, NBTTagCompound p_75823_2_, Chunk chunk) {

        }

        @Override
        public void saveChunk(World p_75816_1_, Chunk p_75816_2_) {

        }

        @Override
        public void saveExtraChunkData(World p_75819_1_, Chunk p_75819_2_) {

        }

        @Override
        public void saveExtraData() {

        }

        @Override
        protected void addChunkToPending(ChunkCoordIntPair p_75824_1_, NBTTagCompound p_75824_2_) {

        }

        @Override
        protected Chunk checkedReadChunkFromNBT(World p_75822_1_, int p_75822_2_, int p_75822_3_, NBTTagCompound p_75822_4_) {

            return null;
        }

        @Override
        protected Object[] checkedReadChunkFromNBT__Async(World p_75822_1_, int p_75822_2_, int p_75822_3_, NBTTagCompound p_75822_4_) {

            return null;
        }

        @Override
        public boolean writeNextIO() {

            return true;
        }

        @Override
        public boolean chunkExists(int p_73149_1_, int p_73149_2_) {

            return false;
        }

        @Override
        public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {

            return null;
        }

        @Override
        public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {

            return null;
        }

        @Override
        public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {

        }

        @Override
        public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {

            return true;
        }

        @Override
        public boolean unloadQueuedChunks() {

            return true;
        }

        @Override
        public boolean canSave() {

            return true;
        }

        @Override
        public String makeString() {

            return "";
        }

        @SuppressWarnings("rawtypes")
        @Override
        public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {

            return new ArrayList();
        }

        @Override
        public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {

            return null;
        }

        @Override
        public int getLoadedChunkCount() {

            return 0;
        }

        @Override
        public void recreateStructures(int p_82695_1_, int p_82695_2_) {

        }

    }

}
