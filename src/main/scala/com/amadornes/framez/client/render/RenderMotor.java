package com.amadornes.framez.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.CCRenderState;

import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementRotation;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.client.IconSupplier;
import com.amadornes.framez.client.LightingNB;
import com.amadornes.framez.client.LightingNBNormals;
import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.tile.TileMotorBlinkDrive;
import com.amadornes.framez.tile.TileMotorLinearActuator;
import com.amadornes.framez.tile.TileMotorRotator;
import com.amadornes.framez.tile.TileMotorSlider;
import com.amadornes.framez.upgrade.MotorUpgradeCamouflage;
import com.amadornes.framez.upgrade.MotorUpgradeCamouflage.MotorUpgradeCamouflageData;
import com.amadornes.framez.util.FramezUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMotor implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    // Item rendering

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        CCRenderState.reset();
        CCRenderState.useNormals = true;
        CCRenderState.pullLightmap();
        GL11.glPushMatrix();
        {
            CCRenderState.changeTexture(TextureMap.locationBlocksTexture);
            if (type == ItemRenderType.INVENTORY)
                GL11.glTranslated(0, -0.09375, 0);
            if (type == ItemRenderType.ENTITY)
                GL11.glTranslated(-0.5, -0.5, -0.5);
            CCRenderState.startDrawing();

            RenderHelper renderer = RenderHelper.instance();
            renderer.start();
            renderMotor(((BlockMotor) Block.getBlockFromItem(item.getItem())).tiles[item.getItemDamage()], type);
            renderer.reset();

            CCRenderState.draw();
        }
        GL11.glPopMatrix();
        CCRenderState.reset();

        if (!alpha)
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    // Static rendering

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rb) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return false;
        TileMotor te = (TileMotor) tile;

        RenderHelper renderer = RenderHelper.instance();
        renderer.reset();
        renderer.start(world, x, y, z, 0);
        renderer.setOverrideTexture(rb.overrideBlockTexture);
        renderMotor(te, null);
        renderer.setOverrideTexture(null);
        renderer.reset();

        return true;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

    private void renderMotor(TileMotor te, ItemRenderType type) {

        if (te == null)
            return;

        RenderHelper renderer = RenderHelper.instance();

        IIcon border = IconSupplier.motor_border;
        IIcon center = IconSupplier.motor_center;
        IIcon arrow_base = IconSupplier.arrow_base;
        IIcon[] border_outer = new IIcon[] { border, border, border, border, border, border };

        for (IMotorUpgradeData d : te.getUpgrades()) {
            if (d != null && d.getUpgrade() instanceof MotorUpgradeCamouflage) {
                MotorUpgradeCamouflageData data = (MotorUpgradeCamouflageData) d.getData();

                for (int i = 0; i < 6; i++)
                    if (data.getCamo(i) != null)
                        border_outer[i] = ((ItemBlock) data.getCamo(i).getItem()).field_150939_a.getIcon(data.getCamoFace(i),
                                data.getCamo(i).getItemDamage());

                break;
            }
        }

        if (te.getWorld() == null)
            renderer.start();
        else
            renderer.start(te.getWorld(), te.getX(), te.getY(), te.getZ(), 0);

        renderer.render(RenderHelper.frame3DBorderIn, border, border, border, border, border, border);
        renderer.render(RenderHelper.frame3DBorderOut, border_outer[0], border_outer[1], border_outer[2], border_outer[3], border_outer[4],
                border_outer[5]);
        renderer.render(RenderHelper.frame3DInsideOut, center, center, center, center, center, center);

        if (type == null) {
            renderer.reset();
            if (te.getWorld() == null)
                renderer.start();
            else
                renderer.start(te.getWorld(), te.getX(), te.getY(), te.getZ(), -1);
        }

        if (type == null)
            renderer.builder.add(new LightingNBNormals());
        if (type != ItemRenderType.INVENTORY)
            renderer.builder.add(new LightingNB());

        // Render arrows
        {
            IMovement movement = te.getMovement();

            if (movement != null) {
                int face = te.getFace();

                renderer.render(RenderHelper.motorLineFace[face], arrow_base, arrow_base, arrow_base, arrow_base, arrow_base, arrow_base);

                if (te instanceof TileMotorSlider) {
                    int mdir = ((IMovementSlide) movement).getDirection();
                    int start = face == 0 || face == 4 || face == 5 ? 3 : (face == 1 ? 2 : (face == 3 ? 1 : 0));
                    int rotY = 0;
                    int tmpRot = start;
                    for (; rotY < 4; rotY++)
                        if ((tmpRot = FramezUtils.ROTATION_MATRIX[face][tmpRot]) == (mdir ^ 1))
                            break;

                    boolean hasBouncyUpgrade = false;
                    for (int i = 0; i < 7; i++) {
                        IMotorUpgradeData d = te.getUpgrades()[i];
                        if (d != null && d.getUpgrade().getType().equals("bouncy")) {
                            hasBouncyUpgrade = true;
                            break;
                        }
                    }

                    if (!hasBouncyUpgrade) {
                        renderer.render(RenderHelper.motorArrowSlider[face][rotY], arrow_base, arrow_base, arrow_base, arrow_base,
                                arrow_base, arrow_base);
                    } else {
                        renderer.render(RenderHelper.motorArrowSliderBouncy[0][face][rotY], arrow_base, arrow_base, arrow_base, arrow_base,
                                arrow_base, arrow_base);

                        renderer.render(RenderHelper.motorArrowSliderBouncy[1][face][rotY], arrow_base, arrow_base, arrow_base, arrow_base,
                                arrow_base, arrow_base);

                        renderer.setColor(0.625D, 0.625D, 0.625D, 1D);
                        renderer.render(RenderHelper.motorArrowSliderBouncy[2][face][rotY], arrow_base, arrow_base, arrow_base, arrow_base,
                                arrow_base, arrow_base);
                        renderer.setColor(1D, 1D, 1D, 1D);
                    }
                } else if (te instanceof TileMotorRotator) {
                    int dir = ((IMovementRotation) movement).getAxis();

                    renderer.render(RenderHelper.motorArrowRotation[face][dir == face ? 1 : 0], arrow_base, arrow_base, arrow_base,
                            arrow_base, arrow_base, arrow_base);
                } else if (te instanceof TileMotorLinearActuator) {
                    renderer.render(RenderHelper.motorArrowLinearActuator[face][0], arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base, arrow_base);
                    renderer.render(RenderHelper.motorArrowLinearActuator[face][1], arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base, arrow_base);
                } else if (te instanceof TileMotorBlinkDrive) {
                    renderer.render(RenderHelper.motorArrowBlinkDrive[face][0], arrow_base, arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base);
                    renderer.render(RenderHelper.motorArrowBlinkDrive[face][1], arrow_base, arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base);
                }
            }
        }

        renderer.reset();
    }
}
