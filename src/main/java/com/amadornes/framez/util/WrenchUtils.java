package com.amadornes.framez.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import uk.co.qmunity.lib.raytrace.RayTracer;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.IDebuggable;
import com.amadornes.framez.api.IFramezWrench;
import com.amadornes.framez.api.IFramezWrench.WrenchAction;
import com.amadornes.framez.compat.CompatibilityUtils;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WrenchUtils {

    public static WrenchAction getWrenchAction(ItemStack stack) {

        if (stack == null)
            return null;
        if (stack.getItem() == null)
            return null;

        if (stack.getItem() instanceof IFramezWrench)
            return ((IFramezWrench) stack.getItem()).getWrenchAction(stack);

        return CompatibilityUtils.getWrenchAction(stack);
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent event) {

        if (event.action == Action.LEFT_CLICK_BLOCK)
            return;

        if (event.entityPlayer == null)
            return;

        World world = event.world;
        int x = event.x;
        int y = event.y;
        int z = event.z;
        int side = event.face;
        EntityPlayer player = event.entityPlayer;
        ItemStack stack = event.entityPlayer.getHeldItem();
        MovingObjectPosition mop = world.rayTraceBlocks(RayTracer.instance().getStartVector(player).toVec3(), RayTracer.instance()
                .getEndVector(player).toVec3());

        WrenchAction a = getWrenchAction(stack);
        if (a == null)
            return;

        if (a == WrenchAction.ROTATE && mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te != null && te instanceof TileMotor && ((TileMotor) te).rotate(ForgeDirection.getOrientation(side))) {
                player.swingItem();
                event.setCanceled(!world.isRemote);
                return;
            } else if (world.getBlock(x, y, z).rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                event.setCanceled(!world.isRemote);
                return;
            }
        }

        if (a == WrenchAction.DEBUG && mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
            IDebuggable debuggable = findDebuggable(world, x, y, z);
            if (debuggable != null) {
                if (debuggable.debug(world, x, y, z, ForgeDirection.getOrientation(side), player)) {
                    player.swingItem();
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if (a == WrenchAction.CONFIG) {
            if (player.isSneaking() || mop == null || mop.typeOfHit == MovingObjectType.MISS) {
                player.swingItem();
                player.openGui(Framez.instance, 0, world, x, y, z);
                event.setCanceled(true);
                return;
            } else if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                TileEntity te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileMotor) {
                    player.swingItem();
                    player.openGui(Framez.instance, 1, world, x, y, z);
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    private IDebuggable findDebuggable(World world, int x, int y, int z) {

        Block b = world.getBlock(x, y, z);
        if (b instanceof IDebuggable)
            return (IDebuggable) b;

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IDebuggable)
            return (IDebuggable) te;

        return null;
    }
}
