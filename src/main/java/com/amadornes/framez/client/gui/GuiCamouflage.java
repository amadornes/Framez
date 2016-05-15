package com.amadornes.framez.client.gui;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.container.ContainerCamouflage;
import com.amadornes.framez.motor.upgrade.UpgradeCamouflage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class GuiCamouflage extends SubGuiContainer {

    private UpgradeCamouflage upgrade;

    private double angleX, angleY;
    private boolean tracking = false, autoRotate = false;
    private int lastX = 0, lastY = 0;
    private int ticks = 0;

    public GuiCamouflage(GuiScreen parent, UpgradeCamouflage upgrade, DynamicReference<? extends IMotor> motor) {

        super(parent, upgrade == null ? null : new ContainerCamouflage(motor, upgrade, Minecraft.getMinecraft().thePlayer.inventory));
        this.upgrade = upgrade;

        this.xSize = 176;
        this.ySize = 238;

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void initGui() {

        super.initGui();

        angleX = -mc.thePlayer.rotationPitch;
        angleY = 180 - mc.thePlayer.rotationYaw;
    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    private static final FloatBuffer buf = BufferUtils.createFloatBuffer(16);

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_camouflage.png"));
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

        drawCenteredString(fontRendererObj, "Camouflage", (left + 79 + left + 79 + 90) / 2, top + 22, 0xFFFFFFFF);

        for (EnumFacing face : EnumFacing.VALUES)
            drawString(fontRendererObj, StringUtils.capitalize(face.getName()), left + 28, top + 16 + face.ordinal() * 24, 0xFFFFFFFF);

        double angleX = this.angleX, angleY = this.angleY;
        if (autoRotate) {
            angleX = Math.sin(Math.toRadians(ticks + partialTicks)) * 45;
            angleY -= 2.0 * partialTicks;
        }
        Matrix4f matrix = new Matrix4f().rotate((float) Math.toRadians(angleY), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(angleX), new Vector3f(1, 0, 0));

        buf.rewind();
        matrix.storeTranspose(buf);
        buf.flip();

        GlStateManager.pushMatrix();
        {
            // GL11.glViewport(vp.x, vp.y, vp.width, vp.height);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            // RenderUtil.loadMatrix(camera.getTransposeProjectionMatrix());
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            // RenderUtil.loadMatrix(camera.getTransposeViewMatrix());
            // GL11.glTranslatef(-(float) eye.x, -(float) eye.y, -(float) eye.z);

            GlStateManager.pushMatrix();
            {
                GlStateManager.enableCull();
                GlStateManager.translate(254, 111, 50);
                GlStateManager.scale(45, -45, 1);

                GlStateManager.translate(0.5, 0.5, 0.5);
                GlStateManager.multMatrix(buf);
                GlStateManager.translate(-0.5, -0.5, -0.5);

                // TODO: Re-enable camouflage GUI
                // mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                // mc.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                // IMotor motor = upgrade.motor.get();
                // VertexBuffer wr = Tessellator.getInstance().getBuffer();
                // wr.begin(7, DefaultVertexFormats.BLOCK);
                // wr.setTranslation(-motor.getMotorPos().getX(), -motor.getMotorPos().getY(), -motor.getMotorPos().getZ());
                // IBlockState state = FramezBlocks.motor.getActualState(motor.getMotorWorld().getBlockState(motor.getMotorPos()),
                // motor.getMotorWorld(), motor.getMotorPos());
                // IBakedModel model;
                //
                // state = state.withProperty(BlockMotor.PROPERTY_PART_TYPE, 0);
                // model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
                // model = model instanceof ISmartBlockModel ? ((ISmartBlockModel) model)
                // .handleBlockState(FramezBlocks.motor.getExtendedState(state, motor.getMotorWorld(), motor.getMotorPos())) : model;
                //
                // mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                // IsolatedWorld.getWorld(motor.getMotorWorld(), motor.getMotorPos()), model, state, motor.getMotorPos(), wr);
                //
                // state = state.withProperty(BlockMotor.PROPERTY_PART_TYPE, 1);
                // model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
                // model = model instanceof ISmartBlockModel ? ((ISmartBlockModel) model).handleBlockState(state) : model;
                //
                // mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                // IsolatedWorld.getWorld(motor.getMotorWorld(), motor.getMotorPos()), model, state, motor.getMotorPos(), wr);
                //
                // wr.setTranslation(0, 0, 0);
                // Tessellator.getInstance().draw();
                // mc.renderEngine.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(240, 56, 50);
                GlStateManager.scale(8, -8, 1);

                GlStateManager.multMatrix(buf);

                GlStateManager.disableTexture2D();
                GL11.glLineWidth(2);
                VertexBuffer wr = Tessellator.getInstance().getBuffer();
                wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
                {
                    wr.pos(0, 0, 0).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
                    wr.pos(0, 1, 0).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
                    wr.pos(0, 0, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
                    wr.pos(1, 0, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
                    wr.pos(0, 0, 0).color(0.0F, 0.0F, 1.0F, 1.0F).endVertex();
                    wr.pos(0, 0, 1).color(0.0F, 0.0F, 1.0F, 1.0F).endVertex();
                }
                Tessellator.getInstance().draw();
                GlStateManager.enableTexture2D();
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        int x = 80, y = 37;

        if (mouseX >= left + x && mouseX < left + x + 90 && mouseY >= top + y && mouseY < top + y + 90) {
            if (mouseButton == 3) {
                autoRotate = !autoRotate;
                ticks = (int) Math.toDegrees(Math.asin(Math.min(Math.max(-1, angleX / 45D), 1)));
            } else if (mouseButton == 0 && !autoRotate) {
                tracking = true;
                lastX = mouseX;
                lastY = mouseY;
            } else if (mouseButton == 2 && !autoRotate) {
                angleX = -mc.thePlayer.rotationPitch;
                angleY = 180 - mc.thePlayer.rotationYaw;
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (tracking) {
            int dx = mouseX - lastX, dy = mouseY - lastY;

            angleX = Math.min(Math.max(-90, angleX - dy), 90);
            angleY -= dx;

            lastX = mouseX;
            lastY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {

        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) tracking = false;
    }

    @SubscribeEvent
    public void onGameTick(ClientTickEvent event) {

        if (event.phase == Phase.START && autoRotate) {
            angleX = Math.sin(Math.toRadians(ticks)) * 45;
            angleY -= 2.0;
            ticks++;
        }
    }

}
