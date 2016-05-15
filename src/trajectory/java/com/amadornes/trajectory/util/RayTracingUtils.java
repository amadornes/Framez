package com.amadornes.trajectory.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;

import com.amadornes.trajectory.api.vec.Vector3;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RayTracingUtils {

    private static double getBlockReachDistance_server(EntityPlayerMP player) {

        return player.theItemInWorldManager.getBlockReachDistance();
    }

    @SideOnly(Side.CLIENT)
    private static double getBlockReachDistance_client() {

        return Minecraft.getMinecraft().playerController.getBlockReachDistance();
    }

    public static double getBlockReachDistance(EntityPlayer player) {

        return player.worldObj.isRemote ? getBlockReachDistance_client()
                : player instanceof EntityPlayerMP ? getBlockReachDistance_server((EntityPlayerMP) player) : 5D;
    }

    public static Vec3 getStartVector(EntityPlayer player) {

        Vector3 v = new Vector3(player.posX, player.posY, player.posZ);
        if (player.worldObj.isRemote) {
            v.add(0, player.getEyeHeight() - player.getDefaultEyeHeight(), 0);// compatibility with eye height changing mods
        } else {
            v.add(0, player.getEyeHeight(), 0);
            if (player instanceof EntityPlayerMP && player.isSneaking())
                v.sub(0, 0.08, 0);
        }
        return Vec3.createVectorHelper(v.x, v.y, v.z);
    }

    public static Vec3 getEndVector(EntityPlayer player) {

        return getEndVector(player, getBlockReachDistance(player));
    }

    public static Vec3 getEndVector(EntityPlayer player, double reach) {

        Vec3 headVec = getStartVector(player);
        Vec3 lookVec = player.getLook(1.0F);
        return headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
    }

}
