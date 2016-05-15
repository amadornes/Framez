package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.tile.TileMotorSlider;
import com.amadornes.framez.util.MotorPlacement;

public class ItemBlockMotor extends ItemBlockFramez {

    public BlockMotor b;

    public ItemBlockMotor(Block b) {

        super(b);
        this.b = (BlockMotor) b;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        String type = "Frame Motor";
        if (stack.getItemDamage() == 0)
            type = "Frame Slider";
        else if (stack.getItemDamage() == 1)
            type = "Frame Rotator";
        else if (stack.getItemDamage() == 2)
            type = "Frame Linear Actuator";
        else if (stack.getItemDamage() == 3)
            type = "Frame Blink Drive";
        return type;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        String type = null;
        if (stack.getItemDamage() == 0)
            type = "slider";
        else if (stack.getItemDamage() == 1)
            type = "rotator";
        else if (stack.getItemDamage() == 2)
            type = "linearactuator";
        else if (stack.getItemDamage() == 3)
            type = "blinkdrive";

        String modStr = StatCollector.translateToLocal("modifier." + ModInfo.MODID + ":" + b.id);
        if (modStr.equals("modifier." + ModInfo.MODID + ":" + b.id)) {
            modStr = "";
            for (String mod : b.id.split("$"))
                modStr += StatCollector.translateToLocal("modifier." + ModInfo.MODID + ":" + mod) + " ";
            modStr = modStr.trim();
        }

        return StatCollector.translateToLocalFormatted("tile." + ModInfo.MODID + ":motor" + (type == null ? "" : "_" + type) + ".name",
                modStr);
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List l) {

        if (FramezConfig.enable_frame_sliders)
            l.add(new ItemStack(item, 1, 0));
        if (FramezConfig.enable_frame_rotators)
            l.add(new ItemStack(item, 1, 1));
        if (FramezConfig.enable_frame_linear_actuators)
            l.add(new ItemStack(item, 1, 2));
        if (FramezConfig.enable_frame_blink_drives)
            l.add(new ItemStack(item, 1, 3));
    }

    @Override
    public int getMetadata(int meta) {

        return meta;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ, int metadata) {

        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
            return false;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return false;
        TileMotor te = (TileMotor) tile;
        te.setFace(side ^ 1);
        if (te instanceof TileMotorSlider)
            ((IMovementSlide) te.getMovement()).setDirection(MotorPlacement.getPlacementDirection(side, hitX, hitY, hitZ));
        return true;
    }
}