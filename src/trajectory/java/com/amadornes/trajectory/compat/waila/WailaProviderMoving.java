package com.amadornes.trajectory.compat.waila;

import java.util.ArrayList;
import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.MetaDataProvider;
import mcp.mobius.waila.cbcore.Layout;
import mcp.mobius.waila.client.KeyEvent;
import mcp.mobius.waila.overlay.RayTracing;
import mcp.mobius.waila.overlay.WailaTickHandler;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import org.lwjgl.input.Keyboard;

import com.amadornes.trajectory.block.BlockMoving;
import com.amadornes.trajectory.block.TileMoving;
import com.amadornes.trajectory.movement.MovingBlock;
import com.amadornes.trajectory.world.FakeWorld;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WailaProviderMoving implements IWailaDataProvider {

    public static final WailaProviderMoving INST = new WailaProviderMoving();

    public ItemStack identifiedHighlight = new ItemStack(Blocks.dirt);
    private List<String> currenttipHead = new ArrayList<String>();
    private List<String> currenttipBody = new ArrayList<String>();
    private List<String> currenttipTail = new ArrayList<String>();
    public MetaDataProvider handler = WailaTickHandler.instance().handler;
    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void tickClient(TickEvent.ClientTickEvent event) {

        if (!Keyboard.isKeyDown(KeyEvent.key_show.getKeyCode())
                && !ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_MODE, false)) {
            ConfigHandler.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, false);
        }

        World world = mc.theWorld;
        EntityPlayer player = mc.thePlayer;
        if (world != null && player != null) {
            RayTracing.instance().fire();
            MovingObjectPosition target = RayTracing.instance().getTarget();

            if (target != null && target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                if (world.getBlock(target.blockX, target.blockY, target.blockZ) instanceof BlockMoving) {
                    TileMoving tile = (TileMoving) world.getTileEntity(target.blockX, target.blockY, target.blockZ);
                    MovingObjectPosition mop = tile.rayTrace(player);
                    MovingBlock b = tile.getSelected(mop);

                    if (b != null) {
                        target = mop;
                        world = FakeWorld.getFakeWorld(b);
                        ReflectionHelper.setPrivateValue(RayTracing.class, RayTracing.instance(), mop, "target");

                        WorldClient w = Minecraft.getMinecraft().theWorld;
                        Minecraft.getMinecraft().thePlayer.worldObj = Minecraft.getMinecraft().theWorld = (WorldClient) world;

                        DataAccessorCommon accessor = DataAccessorCommon.instance;
                        accessor.set(world, player, target);
                        ItemStack targetStack = RayTracing.instance().getTargetStack(); // Here we get either the proper stack or the override

                        if (targetStack != null) {
                            currenttipHead.clear();
                            currenttipBody.clear();
                            currenttipTail.clear();

                            currenttipHead = handler.handleBlockTextData(targetStack, world, player, target, accessor, currenttipHead,
                                    Layout.HEADER);
                            currenttipBody = handler.handleBlockTextData(targetStack, world, player, target, accessor, currenttipBody,
                                    Layout.BODY);
                            currenttipTail = handler.handleBlockTextData(targetStack, world, player, target, accessor, currenttipTail,
                                    Layout.FOOTER);

                            if (ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHIFTBLOCK, false)
                                    && currenttipBody.size() > 0 && !accessor.getPlayer().isSneaking()) {
                                currenttipBody.clear();
                                currenttipBody.add(EnumChatFormatting.ITALIC + "Press shift for more data");
                            }

                            identifiedHighlight = targetStack;
                        }

                        Minecraft.getMinecraft().thePlayer.worldObj = Minecraft.getMinecraft().theWorld = w;
                    } else {
                        currenttipHead.clear();
                        currenttipBody.clear();
                        currenttipTail.clear();

                        currenttipHead.add("ERROR");
                        currenttipBody.add("ERROR");
                        currenttipTail.add("ERROR");
                    }
                }
            }
        }

    }

    @Override
    public List<String> getWailaHead(ItemStack is, List<String> l2, IWailaDataAccessor acc, IWailaConfigHandler cfg) {

        return new UnmodifiableArrayList<String>(currenttipHead);
    }

    @Override
    public List<String> getWailaBody(ItemStack is, List<String> l2, IWailaDataAccessor acc, IWailaConfigHandler cfg) {

        return new UnmodifiableArrayList<String>(currenttipBody);
    }

    @Override
    public List<String> getWailaTail(ItemStack is, List<String> l2, IWailaDataAccessor acc, IWailaConfigHandler cfg) {

        List<String> l = new ArrayList<String>();
        l.addAll(currenttipTail);
        l.add("Moving");
        return new UnmodifiableArrayList<String>(l);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor acc, IWailaConfigHandler cfg) {

        return identifiedHighlight;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity arg1, NBTTagCompound arg2, World arg3, int arg4, int arg5, int arg6) {

        return null;
    }

}