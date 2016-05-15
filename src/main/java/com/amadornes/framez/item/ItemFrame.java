package com.amadornes.framez.item;

import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.part.PartFrame;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemFrame extends ItemMultiPart implements IFramezItem {

    @Override
    public String getName() {

        return "frame";
    }

    @Override
    public boolean isBlock() {

        return false;
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return new PartFrame();
        IFrameMaterial[] materials = new IFrameMaterial[3];
        if (tag.hasKey("border")) materials[0] = FrameRegistry.INSTANCE.materials.get(tag.getString("border"));
        if (tag.hasKey("cross")) materials[1] = FrameRegistry.INSTANCE.materials.get(tag.getString("cross"));
        if (tag.hasKey("binding")) materials[2] = FrameRegistry.INSTANCE.materials.get(tag.getString("binding"));
        return new PartFrame(materials);
    }

}
