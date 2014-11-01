package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import com.amadornes.framez.Framez;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class StructureTickHandler {

    public static StructureTickHandler INST = new StructureTickHandler();

    private List<MovingStructure> structures = new ArrayList<MovingStructure>();

    public MovingStructure tickingStructure = null;

    private StructureTickHandler() {

    }

    public void addStructure(MovingStructure structure) {

        structures.add(structure);
    }

    public void removeStructure(MovingStructure structure) {

        structures.remove(structure);
    }

    public List<MovingStructure> getStructures() {

        return structures;
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {

        if (Framez.proxy.isGamePaused())
            return;

        for (World w : MinecraftServer.getServer().worldServers)
            tick(w, event.phase);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {

        if (Framez.proxy.isGamePaused())
            return;

        tick(Framez.proxy.getWorld(), event.phase);
    }

    private void tick(World world, TickEvent.Phase phase) {

        if (world == null)
            return;

        // Tick the moved block handler
        if (phase == Phase.START)
            MovedBlockHandler.worldTick(world);

        // Tick structures

        List<MovingStructure> invalid = new ArrayList<MovingStructure>();

        for (MovingStructure s : new ArrayList<MovingStructure>(structures)) {
            if (s.getWorld() != world)
                continue;

            tickingStructure = s;
            s.tick(phase);
            tickingStructure = null;

            if (s.getMoved() >= 1)
                invalid.add(s);
        }

        for (MovingStructure s : invalid)
            structures.remove(s);
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {

        if (event.world.isRemote)
            return;

        for (Object o : event.getChunk().chunkTileEntityMap.values()) {
            TileEntity te = (TileEntity) o;
            if (te instanceof TileMotor) {
                ((TileMotor) te).onUnload();
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {

        if (event.world.isRemote)
            return;
        if (!(event.world instanceof WorldServer))
            return;
        WorldServer world = (WorldServer) event.world;

        boolean found = false;

        List<TileMotor> unloaded = new ArrayList<TileMotor>();

        for (Object o : new ArrayList(world.loadedTileEntityList)) {
            TileEntity te = (TileEntity) o;
            if (te instanceof TileMotor && !unloaded.contains(te)) {
                ((TileMotor) te).onUnload();
                found = true;
                unloaded.add((TileMotor) te);
            }
        }

        if (found) {
            Framez.log.info("Saving moving structures in dimension " + world.provider.dimensionId + " ("
                    + world.provider.getDimensionName() + ")");
            try {
                world.saveAllChunks(true, new IProgressUpdate() {

                    @Override
                    public void setLoadingProgress(int p_73718_1_) {

                    }

                    @Override
                    public void resetProgressAndMessage(String p_73721_1_) {

                    }

                    @Override
                    public void resetProgresAndWorkingMessage(String p_73719_1_) {

                    }

                    @Override
                    public void func_146586_a() {

                    }

                    @Override
                    public void displayProgressMessage(String p_73720_1_) {

                    }
                });
                Framez.log.info("Saved moving structures in dimension " + world.provider.dimensionId + "!");
            } catch (MinecraftException e) {
                Framez.log.info("Could not save moving structures in dimension " + world.provider.dimensionId + "!");
                e.printStackTrace();
            }
        }
    }

}
