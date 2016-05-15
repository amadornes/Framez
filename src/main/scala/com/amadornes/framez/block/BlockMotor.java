package com.amadornes.framez.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Cuboid6;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.modifier.IMotorUpgrade;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementRotation;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.api.wrench.IFramezWrench.WrenchAction;
import com.amadornes.framez.client.IconSupplier;
import com.amadornes.framez.client.render.RenderMotor;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.init.FramezCreativeTab;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.modifier.MotorFactory;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.tile.TileMotorBlinkDrive;
import com.amadornes.framez.tile.TileMotorLinearActuator;
import com.amadornes.framez.tile.TileMotorRotator;
import com.amadornes.framez.tile.TileMotorSlider;
import com.amadornes.trajectory.api.vec.Vector3;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMotor extends BlockContainer {

    public final String id;
    public TileMotor[] tiles = new TileMotor[16];

    public BlockMotor(String id) {

        super(Material.iron);

        setHardness(2.5F);
        setResistance(30);

        this.id = id;
        for (int i = 0; i < 16; i++)
            this.tiles[i] = createNewTileEntity(null, i);

        setBlockName("motor");
        setHarvestLevel("wrench", 0);

        setCreativeTab(FramezCreativeTab.tab);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public TileMotor createNewTileEntity(World world, int meta) {

        if (meta == 0 && FramezConfig.enable_frame_sliders)
            return MotorFactory.createMotor(TileMotorSlider.class, "motor_" + id);
        else if (meta == 1 && FramezConfig.enable_frame_rotators)
            return MotorFactory.createMotor(TileMotorRotator.class, "motor_" + id);
        else if (meta == 2 && FramezConfig.enable_frame_linear_actuators)
            return MotorFactory.createMotor(TileMotorLinearActuator.class, "motor_" + id);
        else if (meta == 3 && FramezConfig.enable_frame_blink_drives)
            return MotorFactory.createMotor(TileMotorBlinkDrive.class, "motor_" + id);

        return null;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        onBlockAdded(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return;
        ((TileMotor) tile).onNeighborChange();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderMotor.RENDER_ID;
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {

        if (world.isRemote)
            return true;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return false;

        return ((TileMotor) tile).rotate(axis.ordinal());
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {

        return world.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean isOpaqueCube() {

        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {

        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {

        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {

        return IconSupplier.motor_center;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float x_, float y_, float z_) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return false;
        TileMotor te = (TileMotor) tile;

        if (te.onActivated(player, side, new Vector3(x_, y_, z_)))
            return true;

        ItemStack stack = player.getCurrentEquippedItem();
        IFramezWrench wrench = FramezApi.instance().getWrench(stack);

        if (wrench != null) {
            WrenchAction action = wrench.getWrenchAction(stack);

            if (action == WrenchAction.DEFAULT) {
                if (player.isSneaking()) {
                    if (te.rotate(te.getFace())) {
                        wrench.onUsed(stack, player);
                        return true;
                    }
                    return false;
                } else {
                    int oldFace = te.getFace(), newFace = (oldFace + 1) % 6;
                    te.setFace(newFace);
                    IMovement m = te.getMovement();
                    if (m instanceof IMovementSlide) {
                        int dir = 0;
                        for (int i = 0; i < 6; i++) {
                            if (i != newFace && i != (newFace ^ 1)) {
                                dir = i;
                                break;
                            }
                        }
                        ((IMovementSlide) m).setDirection(dir);
                        wrench.onUsed(stack, player);
                        return true;
                    } else if (m instanceof IMovementRotation) {
                        int rotDir = ((IMovementRotation) m).getAxis() % 2;
                        ((IMovementRotation) m).setAxis(Math.min(newFace, newFace ^ 1) + rotDir);
                        wrench.onUsed(stack, player);
                        return true;
                    }
                }
                return false;
            } else if (action == WrenchAction.ROTATE) {
                if (te.rotate(side)) {
                    wrench.onUsed(stack, player);
                    return true;
                }
                return false;
            } else if (action == WrenchAction.CONFIG) {
                player.openGui(Framez.instance, 1, world, x, y, z);
                wrench.onUsed(stack, player);
                return true;
            }
        } else {
            if (stack != null) {
                IMotorUpgrade upgrade = null;
                for (IMotorUpgrade u : ModifierRegistry.instance.motorUpgrades) {
                    if (u.isUpgradeStack(stack)) {
                        upgrade = u;
                        break;
                    }
                }
                if (upgrade != null && upgrade.canApply(te, stack, player) && upgrade.canApplyDirectly(te, stack, player)) {
                    int slot = -1;
                    for (int i = 0; i < 7; i++) {
                        if (te.getUpgrades()[i] == null) {
                            slot = i;
                            break;
                        }
                    }
                    if (slot != -1) {
                        te.setUpgrade(slot, upgrade, stack.copy());
                        stack.stackSize--;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderHighlight(DrawBlockHighlightEvent event) {

        TileEntity te = event.player.worldObj.getTileEntity(event.target.blockX, event.target.blockY, event.target.blockZ);
        if (te == null || !(te instanceof TileMotor))
            return;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0, 0, 0, 0.4F);
        GL11.glLineWidth(2);
        GL11.glDepthMask(true);
        GL11.glPushMatrix();

        double px = event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * event.partialTicks;
        double py = event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * event.partialTicks;
        double pz = event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * event.partialTicks;
        GL11.glTranslated(event.target.blockX - px, event.target.blockY - py, event.target.blockZ - pz);
        {
            double d = 0.002;
            double din = 0.001;

            RenderUtils.drawCuboidOutline(new Cuboid6(2 / 16D + din, -d, 2 / 16D + din, 14 / 16D - din, 1 + d, 14 / 16D - din));
            RenderUtils.drawCuboidOutline(new Cuboid6(-d, 2 / 16D + din, 2 / 16D + din, 1 + d, 14 / 16D - din, 14 / 16D - din));
            RenderUtils.drawCuboidOutline(new Cuboid6(2 / 16D + din, 2 / 16D + din, -d, 14 / 16D - din, 14 / 16D - din, 1 + d));

            RenderUtils.drawCuboidOutline(new Cuboid6(0, 0, 0, 1, 1, 1).expand(d));
        }
        GL11.glPopMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        event.setCanceled(true);
    }
}
