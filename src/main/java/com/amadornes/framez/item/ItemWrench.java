package com.amadornes.framez.item;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IFramezWrench;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementRotation;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.movement.FrameMovementRegistry;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.PacketWrenchMode;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWrench extends Item implements IFramezWrench {

    @SideOnly(Side.CLIENT)
    private IIcon normal, rotate, debug, config;

    public ItemWrench() {

        setUnlocalizedName(ModInfo.MODID + ":" + References.Item.WRENCH);

        setFull3D();
        setMaxStackSize(1);
        setHarvestLevel("wrench", 0);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

        normal = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_NORMAL);
        rotate = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_ROTATE);
        debug = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_DEBUG);
        config = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_CONFIG);
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {

        return normal;
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {

        if (stack.getItemDamage() == 0)
            return normal;
        if (stack.getItemDamage() == 1)
            return rotate;
        if (stack.getItemDamage() == 2)
            return debug;
        if (stack.getItemDamage() == 3)
            return config;

        return super.getIconIndex(stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return super.getUnlocalizedName(stack)
                + (stack.getItemDamage() == 0 ? ".normal" : (stack.getItemDamage() == 1 ? ".rotate"
                        : (stack.getItemDamage() == 2 ? ".debug" : ".config")));
    }

    @Override
    public WrenchAction getWrenchAction(ItemStack stack) {

        if (stack.getItemDamage() == 1)
            return WrenchAction.ROTATE;
        else if (stack.getItemDamage() == 2)
            return WrenchAction.DEBUG;
        else if (stack.getItemDamage() == 3)
            return WrenchAction.CONFIG;

        return null;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        if (stack.getItemDamage() == 0) {
            Collection<IFrame> frames = FrameMovementRegistry.instance().findFrames(world, x, y, z);
            if (frames != null && frames.size() > 0) {
                if (!world.isRemote) {
                    ForgeDirection d = ForgeDirection.getOrientation(side);
                    for (IFrame f : frames)
                        f.setSideHidden(d, !f.isSideHidden(d));
                }
                player.swingItem();
                return !world.isRemote;
            }

            TileEntity te = world.getTileEntity(x, y, z);

            if (te != null && te instanceof TileMotor) {
                if (!world.isRemote) {
                    if (!player.isSneaking()) {
                        ForgeDirection face = ForgeDirection.getOrientation((((TileMotor) te).getFace().ordinal() + 1) % 6);
                        ((TileMotor) te).setFace(face);
                        IMovement m = ((TileMotor) te).getMovement();
                        if (m instanceof IMovementSlide) {
                            ForgeDirection d = null;
                            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                                if (dir != face && dir != face.getOpposite()) {
                                    d = dir;
                                    break;
                                }
                            }
                            ((IMovementSlide) m).setDirection(d);
                        } else if (m instanceof IMovementRotation)
                            ((IMovementRotation) m).setAxis(face);
                    } else {
                        IMovement m = ((TileMotor) te).getMovement();
                        if (m instanceof IMovementSlide)
                            ((IMovementSlide) m).setDirection(((IMovementSlide) m).getDirection().getRotation(((TileMotor) te).getFace()));
                        else if (m instanceof IMovementRotation)
                            ((IMovementRotation) m).setAxis(((IMovementRotation) m).getAxis().getOpposite());
                    }
                    world.markBlockForUpdate(x, y, z);// FIXME Replace with proper update method
                }
                player.swingItem();
                return !world.isRemote;
            } else if (world.getBlock(x, y, z).rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                return !world.isRemote;
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {

        // We only want to process wheel events
        if (event.button < 0) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
            if (entityPlayer.isSneaking()) {
                ItemStack itemStack = entityPlayer.getHeldItem();
                if (itemStack != null && itemStack.getItem() == this) {
                    if (event.dwheel != 0)
                        NetworkHandler.instance().sendToServer(new PacketWrenchMode(entityPlayer.inventory.currentItem, event.dwheel < 0));
                    event.setCanceled(true);
                }
            }
        }
    }

}
