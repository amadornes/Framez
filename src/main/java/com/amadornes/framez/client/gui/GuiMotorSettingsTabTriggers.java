package com.amadornes.framez.client.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotorAction;
import com.amadornes.framez.api.motor.IMotorTrigger;
import com.amadornes.framez.api.motor.MotorTriggerCategory;
import com.amadornes.framez.api.motor.MotorTriggerType;
import com.amadornes.framez.motor.EnumTriggerOperation;
import com.amadornes.framez.motor.MotorTrigger;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.PacketUpdateTrigger;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiMotorSettingsTabTriggers extends GuiMotorSettingsTab implements IFramezGUI {

    private Map<MotorTriggerCategory, Map<MotorTriggerType, IMotorTrigger>> allTriggers//
            = new HashMap<MotorTriggerCategory, Map<MotorTriggerType, IMotorTrigger>>();

    private IMotorAction currentAction;
    private MotorTrigger currentTrigger;
    private MotorTrigger modifiedTrigger;

    public GuiMotorSettingsTabTriggers(DynamicReference<TileMotor> motor, int xSize, int ySize) {

        super(motor, xSize, ySize);
        for (IMotorTrigger t : motor.get().availableTriggers.values()) {
            Map<MotorTriggerType, IMotorTrigger> map = allTriggers.get(t.getTriggerType().getCategory());
            if (map == null) allTriggers.put(t.getTriggerType().getCategory(), map = new HashMap<MotorTriggerType, IMotorTrigger>());
            map.put(t.getTriggerType(), t);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks);

        MotorTrigger trigger = getTrigger(mouseX, mouseY);

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        drawString(fontRendererObj, I18n.format("gui.framez:motor.triggers") + ":", left + 8, top + 8, 0xFFFFFF);

        // Left panel
        mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_tab_triggers_2.png"));
        drawTexturedModalRect(left - 50, top + 7, 0, 0, 50, 140);

        // Main GUI body
        mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_tab_triggers.png"));
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

        // Action selector background
        Iterator<IMotorAction> it = motor.get().triggers.keySet().iterator();
        for (int i = 0; i < 5; i++) {
            boolean exists = it.hasNext();
            boolean active = exists && it.next() == currentAction;
            boolean mouseOver = exists && isWithinBounds(left + 8 + 1, top + 28 + 22 * i + 1, 18, 18, mouseX, mouseY);
            drawTexturedModalRect(left + 8, top + 28 + 22 * i, 8, ySize + 21 + (active ? 40 : mouseOver ? 20 : 0), 20, 20);
        }

        // Action selector icons
        it = motor.get().triggers.keySet().iterator();
        for (int i = 0; i < 5 && it.hasNext(); i++) {
            IMotorAction action = it.next();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.translate(0.0F, 0.0F, 32.0F);
            this.zLevel = 100.0F;
            this.itemRender.zLevel = 100.0F;
            this.itemRender.renderItemAndEffectIntoGUI(action.getIconStack(), left + 10, top + 30 + 22 * i);
            this.zLevel = 0.0F;
            this.itemRender.zLevel = 0.0F;

            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }

        // Save button
        {
            int x = left + 8, y = top + ySize - 16;
            String text = I18n.format("gui.framez:button.save");
            boolean trig = mouseX >= x && mouseX < x + 103 && mouseY >= y && mouseY < y + 10;

            mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_tab_triggers.png"));
            drawTexturedModalRect(x, y, 8, ySize + 1 + (trig ? 10 : 0), 103, 10);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + (103 - fontRendererObj.getStringWidth(text) * 0.75) / 2D, y + 2, 0);
            GlStateManager.scale(0.75, 0.75, 0.75);
            drawString(fontRendererObj, text, 0, 0, 0xBBBBBB);
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1);
        }

        // Cancel button
        {
            int x = left + 8 + 103 + 4, y = top + ySize - 16;
            String text = I18n.format("gui.framez:button.cancel");
            boolean trig = mouseX >= x && mouseX < x + 103 && mouseY >= y && mouseY < y + 10;

            mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_tab_triggers.png"));
            drawTexturedModalRect(x, y, 8, ySize + 1 + (trig ? 10 : 0), 103, 10);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + (103 - fontRendererObj.getStringWidth(text) * 0.75) / 2D, y + 2, 0);
            GlStateManager.scale(0.75, 0.75, 0.75);
            drawString(fontRendererObj, text, 0, 0, 0xBBBBBB);
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1);
        }

        // Logic button
        if (trigger != null) {
            int x = left + 8 + 103 + 4 + 80, y = top + 22;
            String text = trigger.getOperation().name();
            boolean trig = (trigger == currentTrigger && modifiedTrigger.getOperation() != currentTrigger.getOperation())
                    || (mouseX >= x && mouseX < x + 23 && mouseY >= y && mouseY < y + 10);

            mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_tab_triggers.png"));
            drawTexturedModalRect(x, y, 8 + 103, ySize + 1 + (trig ? 10 : 0), 23, 10);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + (23 - fontRendererObj.getStringWidth(text) * 0.75) / 2D, y + 2, 0);
            GlStateManager.scale(0.75, 0.75, 0.75);
            drawString(fontRendererObj, text, 0, 0, 0xBBBBBB);
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(left + 30, top + 25, 0);
        GlStateManager.scale(0.75, 0.75, 0.75);
        drawString(fontRendererObj,
                I18n.format("gui.framez:action") + ": " + (trigger != null ? I18n.format(currentAction.getUnlocalizedName()) : "---"), 0, 0,
                0xBBBBBB);
        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();

        // Trigger list
        if (trigger != null) {
            int i = 0;
            for (Entry<IMotorTrigger, Boolean> e : trigger.getTriggers().entrySet()) {
                int x = left + 31, y = top + 35 + 19 * i;
                boolean trig = mouseX >= x && mouseX < x + 186 && mouseY >= y && mouseY < y + 19;

                mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_tab_triggers.png"));
                drawTexturedModalRect(x, y, 31, 177 + (trig ? 19 : 0), 186, 19);
                drawTexturedModalRect(x + 1, y + 1, 28, 177 + (e.getKey().isActive() == e.getValue() ? 3 : 0), 3, 3);

                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 19 + 3, y + 6, 0);
                GlStateManager.scale(0.75, 0.75, 0.75);
                drawString(fontRendererObj, I18n.format(e.getKey().getUnlocalizedName(e.getValue()))
                        + (e.getKey().requiresInvertedOverlay() && e.getValue() ? " [" + I18n.format("trigger.framez:inverted") + "]" : ""),
                        0, 0, 0xBBBBBB);
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableRescaleNormal();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                GlStateManager.translate(0.0F, 0.0F, 32.0F);
                this.zLevel = 100.0F;
                this.itemRender.zLevel = 100.0F;
                this.itemRender.renderItemAndEffectIntoGUI(e.getKey().getIconStack(e.getValue()), x + 1, y + 1);
                this.zLevel = 0.0F;
                this.itemRender.zLevel = 0.0F;

                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();

                i++;
                if (i > 6) break;
            }
        }

        // Trigger panel
        {
            int i = 0;
            for (IMotorTrigger t : motor.get().availableTriggers.values()) {
                int x = left - 43 + (21 * (i % 2)), y = top + 27 + 21 * ((i - (i % 2)) / 2);
                boolean trig = modifiedTrigger != null && mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18;

                if (trig) {
                    for (IMotorTrigger t2 : modifiedTrigger.getTriggers().keySet()) {
                        if (t2.getTriggerType().getCategory() == t.getTriggerType().getCategory()) {
                            trig = false;
                            break;
                        }
                    }
                    if (trig) this.drawGradientRect(x, y, x + 18, y + 18, 0x70C0C0FF, 0x80C0C0FF);
                    else this.drawGradientRect(x, y, x + 18, y + 18, 0x70FFC0C0, 0x80FFC0C0);
                }

                GlStateManager.pushMatrix();
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableRescaleNormal();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                GlStateManager.translate(0.0F, 0.0F, 32.0F);
                this.zLevel = 100.0F;
                this.itemRender.zLevel = 100.0F;
                this.itemRender.renderItemAndEffectIntoGUI(t.getIconStack(false), x + 1, y + 1);
                this.zLevel = 0.0F;
                this.itemRender.zLevel = 0.0F;

                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();

                i++;
                if (i >= 10) break;
            }
        }
    }

    public MotorTrigger getTrigger(int mouseX, int mouseY) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        int x = left + 8 + 103 + 4, y = top + ySize - 16;
        return mouseX >= x && mouseX < x + 103 && mouseY >= y && mouseY < y + 10 ? currentTrigger : modifiedTrigger;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);

        MotorTrigger trigger = getTrigger(mouseX, mouseY);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        if (mouseButton == 0) {
            // Action selector
            {
                Iterator<IMotorAction> it = motor.get().triggers.keySet().iterator();
                IMotorAction action;
                for (int i = 0; i < 5; i++) {
                    if (it.hasNext() && (action = it.next()) != currentAction
                            && isWithinBounds(left + 8 + 1, top + 28 + 22 * i + 1, 18, 18, mouseX, mouseY)) {
                        currentAction = action;
                        currentTrigger = motor.get().triggers.get(action);
                        modifiedTrigger = new MotorTrigger(currentTrigger);
                        playClickSound();
                        return;
                    }
                }
            }

            // Save button
            if (trigger != null && currentTrigger.hashCode() != modifiedTrigger.hashCode()) {
                int x = left + 8 + 4, y = top + ySize - 16;
                if (mouseX >= x && mouseX < x + 103 && mouseY >= y && mouseY < y + 10) {
                    motor.get().triggers.put(currentAction, currentTrigger = new MotorTrigger(modifiedTrigger));
                    NetworkHandler.instance
                            .sendToServer(new PacketUpdateTrigger(motor.get(), motor.get().actionIdMap.indexOf(currentAction)));
                    playClickSound();
                    return;
                }
            }

            // Cancel button
            if (trigger != null) {
                int x = left + 8 + 103 + 4, y = top + ySize - 16;
                if (mouseX >= x && mouseX < x + 103 && mouseY >= y && mouseY < y + 10) {
                    modifiedTrigger = new MotorTrigger(currentTrigger);
                    playClickSound();
                    return;
                }
            }

            // Logic button
            if (trigger != null) {
                int x = left + 8 + 103 + 4 + 80, y = top + 22;
                if (mouseX >= x && mouseX < x + 23 && mouseY >= y && mouseY < y + 10) {
                    trigger.setOperation(
                            EnumTriggerOperation.values()[(trigger.getOperation().ordinal() + 1) % EnumTriggerOperation.values().length]);
                    playClickSound();
                    return;
                }
            }

            // Trigger list
            if (trigger != null) {
                int i = 0;
                for (Entry<IMotorTrigger, Boolean> e : trigger.getTriggers().entrySet()) {
                    int x = left + 31, y = top + 35 + 19 * i;
                    if (mouseX >= x && mouseX < x + 186 && mouseY >= y && mouseY < y + 19) {
                        if (mouseX >= x + 178 && mouseX < x + 178 + 7 && mouseY >= y + 1 && mouseY < y + 1 + 7) {
                            modifiedTrigger.removeTrigger(e.getKey());
                            playClickSound();
                            return;
                        } else {
                            if (e.getKey().canBeInverted()) {
                                modifiedTrigger.addTrigger(e.getKey(), !modifiedTrigger.getTriggers().get(e.getKey()));
                                playClickSound();
                            }
                            return;
                        }
                    }

                    i++;
                    if (i > 6) break;
                }
            }

            // Trigger panel
            if (modifiedTrigger != null) {
                int i = 0;
                for (IMotorTrigger t : motor.get().availableTriggers.values()) {
                    int x = left - 43 + (21 * (i % 2)), y = top + 27 + 21 * ((i - (i % 2)) / 2);
                    boolean trig = mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18;
                    if (trig) {
                        for (IMotorTrigger t2 : modifiedTrigger.getTriggers().keySet()) {
                            if (t2.getTriggerType().getCategory() == t.getTriggerType().getCategory()) {
                                trig = false;
                                break;
                            }
                        }
                        if (trig) {
                            modifiedTrigger.addTrigger(t, false);
                            playClickSound();
                        }
                        return;
                    }
                    i++;
                    if (i >= 10) break;
                }
            }
        }
    }

    private void playClickSound() {

        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

}
