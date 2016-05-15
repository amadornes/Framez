package com.amadornes.trajectory.world;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
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
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.trajectory.api.vec.Vector3;
import com.amadornes.trajectory.movement.MovingBlock;
import com.amadornes.trajectory.movement.MovingStructure;
import com.amadornes.trajectory.network.NetworkHandler;
import com.amadornes.trajectory.network.packet.PacketSingleBlockSync;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class FakeWorldServer extends WorldServer {

    private static Map<World, FakeWorldServer> instances = new HashMap<World, FakeWorldServer>();

    public static FakeWorldServer instance(MovingStructure structure) {

        FakeWorldServer instance = instance(structure.getWorld());
        instance.setStructure(structure);
        return instance;
    }

    public static FakeWorldServer instance(World world) {

        if (instances.containsKey(world))
            return instances.get(world);

        FakeWorldServer instance = new FakeWorldServer(world);
        instances.put(world, instance);
        return instance;
    }

    private MovingStructure structure;

    private FakeWorldServer(World world) {

        super(MinecraftServer.getServer(), new NonSavingHandler(), "Trajectory_" + world.getWorldInfo().getWorldName(),
                world.provider.dimensionId, new WorldSettings(world.getWorldInfo()), world.theProfiler);
        DimensionManager.setWorld(world.provider.dimensionId, (WorldServer) world);

        chunkProvider = world.getChunkProvider();
    }

    public void setStructure(MovingStructure structure) {

        this.structure = structure;
    }

    @Override
    protected void initialize(WorldSettings p_72963_1_) {

    }

    @Override
    protected IChunkProvider createChunkProvider() {

        EmptyChunkLoader chunkloader = new EmptyChunkLoader();
        return this.theChunkProviderServer = new EmptyChunkProvider(this, chunkloader, chunkloader);
    }

    @Override
    protected int func_152379_p() {

        return Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
    }

    private MovingBlock get(int x, int y, int z) {

        return structure == null ? null : (MovingBlock) structure.getBlocks().find(x, y, z);
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

        Vector3 pos = structure.getTrajectory().transformVec(new Vector3(entity.posX, entity.posY, entity.posZ), structure.getTicksMoved());

        entity.lastTickPosX -= entity.posX - pos.x;
        entity.lastTickPosY -= entity.posY - pos.y;
        entity.lastTickPosZ -= entity.posZ - pos.z;

        entity.prevPosX -= entity.posX - pos.x;
        entity.prevPosY -= entity.posY - pos.y;
        entity.prevPosZ -= entity.posZ - pos.z;

        entity.posX = pos.x;
        entity.posY = pos.y;
        entity.posZ = pos.z;

        return structure.getWorld().spawnEntityInWorld(entity);
    }

    @Override
    public void spawnParticle(String type, double x, double y, double z, double r, double g, double b) {

        Vector3 pos = structure.getTrajectory().transformVec(new Vector3(x, y, z), structure.getTicksMoved());
        structure.getWorld().spawnParticle(type, pos.x, pos.y, pos.z, r, g, b);
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block) {

        return setBlock(x, y, z, block, 0, 3);
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setBlock(block);
        b.setMetadata(meta);

        if ((notify & 1) != 0)
            notifyBlockChange(x, y, z, block);
        if ((notify & 2) != 0)
            NetworkHandler.instance().sendToAllAround(new PacketSingleBlockSync(b),
                    new TargetPoint(structure.getWorld().provider.dimensionId, x, y, z, 64));

        return true;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int notify) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return false;

        b.setMetadata(meta);

        if ((notify & 1) != 0)
            notifyBlockChange(x, y, z, b.getBlock());
        if ((notify & 2) != 0)
            NetworkHandler.instance().sendToAllAround(new PacketSingleBlockSync(b),
                    new TargetPoint(structure.getWorld().provider.dimensionId, x, y, z, 64));

        return true;
    }

    @Override
    public boolean setBlockToAir(int x, int y, int z) {

        return setBlock(x, y, z, Blocks.air);
    }

    @Override
    public void setTileEntity(int x, int y, int z, TileEntity te) {

        MovingBlock b = get(x, y, z);
        if (b != null) {
            b.setTileEntity(te);

            NetworkHandler.instance().sendToAllAround(new PacketSingleBlockSync(b),
                    new TargetPoint(structure.getWorld().provider.dimensionId, x, y, z, 64));
        }
    }

    @Override
    public void removeTileEntity(int x, int y, int z) {

        MovingBlock b = get(x, y, z);
        if (b != null) {
            b.setTileEntity(null);

            NetworkHandler.instance().sendToAllAround(new PacketSingleBlockSync(b),
                    new TargetPoint(structure.getWorld().provider.dimensionId, x, y, z, 64));
        }
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
    public void playBroadcastSound(int x, int y, int z, int p_82739_4_, int p_82739_5_) {

        structure.getWorld().playBroadcastSound(x, y, z, p_82739_4_, p_82739_5_);
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
    protected boolean chunkExists(int x, int z) {

        return getChunkProvider().chunkExists(x, z);
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
    public void notifyBlockChange(int x, int y, int z, Block block) {

        super.notifyBlockChange(x, y, z, block);
    }

    @Override
    public void notifyBlockOfNeighborChange(int x, int y, int z, Block block) {

        super.notifyBlockOfNeighborChange(x, y, z, block);
    }

    @Override
    public void notifyBlocksOfNeighborChange(int x, int y, int z, Block block, int exceptSide) {

        super.notifyBlocksOfNeighborChange(x, y, z, block, exceptSide);
    }

    @Override
    public void notifyBlocksOfNeighborChange(int x, int y, int z, Block block) {

        super.notifyBlocksOfNeighborChange(x, y, z, block);
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

        structure.getWorld().joinEntityInSurroundings(entity);
    }

    @Override
    public void removeEntity(Entity p_72900_1_) {

        structure.getWorld().removeEntity(p_72900_1_);
    }

    @Override
    public File getChunkSaveLocation() {

        return null;
    }

    @Override
    public void markBlockForUpdate(int x, int y, int z) {

    }

    @Override
    public long getWorldTime() {

        return structure.getWorld().getWorldTime();
    }

    @Override
    public long getTotalWorldTime() {

        return structure.getWorld().getTotalWorldTime();
    }

    @Override
    public boolean isBlockNormalCubeDefault(int x, int y, int z, boolean whatever) {

        return getBlock(x, y, z).isNormalCube(this, x, y, z);
    }

    @Override
    public void scheduleBlockUpdate(int x, int y, int z, Block block, int time) {

        scheduleBlockUpdateWithPriority(x, y, z, block, time, 0);
    }

    @Override
    public void scheduleBlockUpdateWithPriority(int x, int y, int z, Block block, int time, int priority) {

        MovingBlock b = get(x, y, z);
        if (b == null)
            return;
        b.scheduleTick(priority, time);
    }

    @Override
    public void markTileEntityChunkModified(int p_147476_1_, int p_147476_2_, int p_147476_3_, TileEntity p_147476_4_) {

    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getEntitiesWithinAABB(Class p_72872_1_, AxisAlignedBB p_72872_2_) {

        return structure.getWorld().getEntitiesWithinAABB(p_72872_1_,
                structure.getTrajectory().transformAABB(p_72872_2_, structure.getTicksMoved())[0]);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getEntitiesWithinAABBExcludingEntity(Entity p_72839_1_, AxisAlignedBB p_72839_2_) {

        return structure.getWorld().getEntitiesWithinAABBExcludingEntity(p_72839_1_,
                structure.getTrajectory().transformAABB(p_72839_2_, structure.getTicksMoved())[0]);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getEntitiesWithinAABBExcludingEntity(Entity p_94576_1_, AxisAlignedBB p_94576_2_, IEntitySelector p_94576_3_) {

        return structure.getWorld().getEntitiesWithinAABBExcludingEntity(p_94576_1_,
                structure.getTrajectory().transformAABB(p_94576_2_, structure.getTicksMoved())[0], p_94576_3_);
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

    private static class EmptyChunkLoader extends AnvilChunkLoader implements IChunkProvider {

        public EmptyChunkLoader() {

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

    private static class EmptyChunkProvider extends ChunkProviderServer {

        public EmptyChunkProvider(WorldServer world, IChunkLoader loader, IChunkProvider provider) {

            super(world, loader, provider);
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