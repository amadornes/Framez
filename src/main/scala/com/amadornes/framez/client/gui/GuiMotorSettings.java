package com.amadornes.framez.client.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.amadornes.framez.api.modifier.IMotorUpgrade.IConfigurableMotorUpgrade;
import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.api.movement.IMovement.IMovementBlink;
import com.amadornes.framez.api.movement.IMovement.IMovementRotation;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.movement.MotorSetting;
import com.amadornes.framez.movement.RedstoneMode;
import com.amadornes.framez.movement.SpeedUnit;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotor;

public class GuiMotorSettings extends GuiScreen {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "textures/gui/motor.png");
    private static SpeedUnit slidingUnit = SpeedUnit.SPM;
    private static SpeedUnit rotationUnit = SpeedUnit.SPM;

    private TileMotor motor;
    private int xSize, ySize;

    private int clickX = -1;
    private GuiButton slider;

    private GuiTextField speed;
    private double lastTpm;
    private double tpm;
    private GuiButton unitSelector;
    private SpeedUnit unit;

    public GuiMotorSettings(TileMotor motor) {

        xSize = 228;
        ySize = 123;
        this.motor = motor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {

        super.initGui();

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        buttonList.add(new GuiButton16(0, x_ + 8, y_ + 8));
        buttonList.add(new GuiButton16(1, x_ + 8, y_ + 8 + 20));

        buttonList.add(slider = new GuiButton16(2, x_ + 8, y_ + 8 + 20 + 20, 7));

        speed = new GuiTextField(fontRendererObj, x_ + 8 + 1, y_ + 8 + 20 + 20 + 20 + 1, xSize - 8 - 1 - 8 - 1 - 32 - 2, 14);
        buttonList.add(unitSelector = new GuiButton16(3, x_ + xSize - 8 - 32, y_ + 8 + 20 + 20 + 20, 32));

        if (motor.getMovement() instanceof IMovementRotation)
            unitSelector.displayString = (unit = rotationUnit).getUnit();
        else if (motor.getMovement() instanceof IMovementSlide || motor.getMovement() instanceof IMovementBlink)
            unitSelector.displayString = (unit = slidingUnit).getUnit();

        if (unit != null)
            speed.setText(String.format("%.4f", unit.convertFromTPM(tpm = lastTpm = motor.getSetting(MotorSetting.MOVEMENT_DURATION)))
                    + " " + unit.getUnit());

        if (tpm < 0 || unit == null) {
            speed.setEnabled(false);
            slider.enabled = false;
            unitSelector.enabled = false;
        } else {
            double d2 = getPercFromTicks(tpm) * (xSize - 8 - slider.width + 1);
            slider.xPosition = (int) (x_ + 8 + d2);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    private <T> GuiScreen getConfigGui(IConfigurableMotorUpgrade<T> upgrade, int id) {

        return upgrade.getConfigGUI(motor, id, this);
    }

    @Override
    public void drawScreen(int x, int y, float partialTick) {

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        mc.renderEngine.bindTexture(resLoc);
        drawTexturedModalRect(x_, y_, 0, 0, xSize, ySize);

        for (int i = 0; i < 7; i++) {
            IMotorUpgradeData data = motor.getUpgrades()[i];
            if (data == null)
                continue;
            if (!(data.getUpgrade() instanceof IConfigurableMotorUpgrade<?>)
                    || getConfigGui((IConfigurableMotorUpgrade<?>) data.getUpgrade(), i) == null)
                continue;
            this.zLevel = 100;
            drawTexturedModalRect(x_ + 27 + 31 * i, y_ + 90, xSize, 28, 6, 6);
            this.zLevel = 0;
        }

        Tessellator t = Tessellator.instance;

        GL11.glPushMatrix();
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            GL11.glTranslated(x_, y_ + 8 + 20 + 20, 0);

            t.startDrawingQuads();

            t.setColorOpaque_I(0x111111);
            t.addVertex(8, 3, 0);
            t.addVertex(8, 13, 0);
            t.addVertex(xSize - 8, 13, 0);
            t.addVertex(xSize - 8, 3, 0);

            t.setColorOpaque_I(0x333333);
            t.addVertex(8 + 1, 3 + 1, 0);
            t.addVertex(8 + 1, 13 - 1, 0);
            t.addVertex(xSize - 8 - 1, 13 - 1, 0);
            t.addVertex(xSize - 8 - 1, 3 + 1, 0);

            t.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();

        super.drawScreen(x, y, partialTick);

        mc.renderEngine.bindTexture(resLoc);

        drawTexturedModalRect(x_ + 8 + 1, y_ + 8 + 1, 242, motor.getSetting(MotorSetting.REDSTONE_PULSE) ? 14 : 0, 14, 14);
        drawTexturedModalRect(
                x_ + 8 + 1,
                y_ + 8 + 1 + 16 + 4,
                242,
                28 + (motor.getSetting(MotorSetting.REDSTONE_MODE) == RedstoneMode.INVERTED ? 14 : motor
                        .getSetting(MotorSetting.REDSTONE_MODE) == RedstoneMode.NONE ? 28 : 0), 14, 14);

        drawString(fontRendererObj,
                I18n.format("gui." + ModInfo.MODID + ":motor.redstone_pulse." + ((motor.getSetting(MotorSetting.REDSTONE_PULSE) ? 1 : 0))),
                x_ + 26, y_ + 8 + 4, 0xFFFFFF);
        drawString(
                fontRendererObj,
                I18n.format("gui." + ModInfo.MODID + ":motor.redstone_inverted."
                        + motor.getSetting(MotorSetting.REDSTONE_MODE).name().toLowerCase(Locale.ENGLISH)), x_ + 26, y_ + 8 + 4 + 20,
                0xFFFFFF);

        speed.drawTextBox();

        if (x >= unitSelector.xPosition && y >= unitSelector.yPosition && x < unitSelector.xPosition + unitSelector.width
                && y < unitSelector.yPosition + unitSelector.height) {
            List<String> l = null;
            if (motor.getMovement() instanceof IMovementRotation)
                l = Arrays.asList(rotationUnit.getDescription());
            else if (motor.getMovement() instanceof IMovementSlide)
                l = Arrays.asList(slidingUnit.getDescription());
            GuiHelper.drawHoveringText(l, x, y, fontRendererObj);
        }

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 0; i < 7; i++) {
            IMotorUpgradeData data = motor.getUpgrades()[i];
            if (data == null)
                continue;
            ItemStack is = data.getStack();

            int ix = x_ + 13 + 31 * i;
            int iy = y_ + 94;

            itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), is, ix, iy);
        }
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        for (int i = 0; i < 7; i++) {
            if (x >= x_ + 9 + i * 31 && y >= y_ + 90 && x < x_ + 9 + 24 + i * 31 && y < y_ + 90 + 25) {
                IMotorUpgradeData data = motor.getUpgrades()[i];
                if (x >= x_ + 9 + i * 31 + 18 && y >= y_ + 90 && x < x_ + 9 + 24 + i * 31 && y < y_ + 90 + 7) {
                    if (data != null && data.getUpgrade() instanceof IConfigurableMotorUpgrade<?>) {
                        GuiScreen gui = getConfigGui((IConfigurableMotorUpgrade<?>) data.getUpgrade(), i);
                        if (gui != null) {
                            GuiHelper.drawHoveringText(Arrays.asList("Settings"), x, y, fontRendererObj);
                            continue;
                        }
                    }
                }
                if (data == null) {
                    GuiHelper.drawHoveringText(Arrays.asList("Upgrade slot " + (i + 1), EnumChatFormatting.GRAY.toString()
                            + EnumChatFormatting.ITALIC.toString() + "Empty", EnumChatFormatting.YELLOW + "Click to select an upgrade",
                            EnumChatFormatting.YELLOW + "from your inventory."), x, y, fontRendererObj);
                } else {
                    GuiHelper.drawHoveringText(Arrays.asList("Upgrade slot " + (i + 1), EnumChatFormatting.GREEN
                            + data.getStack().getDisplayName(), EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC.toString()
                            + I18n.format("upgrade.framez:" + data.getUpgrade().getType()), EnumChatFormatting.YELLOW
                            + "Click to select an upgrade", EnumChatFormatting.YELLOW + "from your inventory."), x, y, fontRendererObj);
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        if (!button.enabled)
            return;

        super.actionPerformed(button);

        if (button.id == 0) {
            motor.setSetting(MotorSetting.REDSTONE_PULSE, !motor.getSetting(MotorSetting.REDSTONE_PULSE));
        } else if (button.id == 1) {
            motor.setSetting(MotorSetting.REDSTONE_MODE, RedstoneMode.values()[(motor.getSetting(MotorSetting.REDSTONE_MODE).ordinal() + 1)
                    % RedstoneMode.values().length]);
        } else if (button.id == 3) {
            if (motor.getMovement() instanceof IMovementRotation) {
                List<SpeedUnit> units = SpeedUnit.getRotationUnits();
                int i = ((units.indexOf(rotationUnit) + 1) % units.size());
                unit = rotationUnit = units.get(i);
                unitSelector.displayString = rotationUnit.getUnit();
            } else if (motor.getMovement() instanceof IMovementSlide) {
                List<SpeedUnit> units = SpeedUnit.getSlidingUnits();
                int i = ((units.indexOf(slidingUnit) + 1) % units.size());
                unit = slidingUnit = units.get(i);
                unitSelector.displayString = slidingUnit.getUnit();
            } else {
                mc.thePlayer.addChatMessage(new ChatComponentText(
                        "ERROR, attempting to configure a motor that doesn't support the config screen!"));
            }
            double val = unit.convertFromTPM(tpm);
            speed.setText(String.format("%.4f", val) + " " + unit.getUnit());
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

        super.mouseClicked(x, y, button);

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        if (tpm >= 0) {
            boolean wasSelected = speed.isFocused();
            speed.mouseClicked(x, y, button);
            speed.setCursorPosition(Math.min(speed.getCursorPosition(), speed.getText().length() - unit.getUnit().length() - 1));

            if (wasSelected && !speed.isFocused()) {
                try {
                    double val = Double.parseDouble(speed.getText().substring(0, speed.getText().length() - unit.getUnit().length() - 1)
                            + (speed.getText().substring(0, speed.getText().length() - unit.getUnit().length() - 1).contains(".") ? "0"
                                    : ""));
                    tpm = unit.convertToTPM(val);
                    if (tpm != lastTpm) {
                        motor.setSetting(MotorSetting.MOVEMENT_DURATION, tpm);
                        lastTpm = tpm;
                        double d = getPercFromTicks(tpm) * (xSize - 8 - slider.width + 1);
                        slider.xPosition = (int) (x_ + 8 + d);
                    }
                } catch (Exception ex) {
                    speed.setFocused(true);
                }
            }

            if (slider.enabled) {
                if (slider.mousePressed(mc, x, y)) {
                    clickX = x;
                } else {
                    if (x > x_ + 8 + 1 && y > y_ + 8 + 20 + 20 + 3 + 1 && x < x_ + xSize - 8 - 1 && y < y_ + 8 + 20 + 20 + 13 - 1) {
                        clickX = x;
                        slider.xPosition = x - 2;
                        double perc = (slider.xPosition - x_ - 8) / (double) (xSize - 8 - slider.width + 1);
                        tpm = getSliderTicks(perc);
                        double val = unit.convertFromTPM(tpm);
                        speed.setText(String.format("%.4f", val) + " " + unit.getUnit());
                    } else {
                        clickX = -1;
                    }
                }
            }
        }

        for (int i = 0; i <= 7; i++) {
            if (x >= x_ + 9 + i * 31 + 18 && y >= y_ + 90 && x < x_ + 9 + 24 + i * 31 && y < y_ + 90 + 7) {
                IMotorUpgradeData data = motor.getUpgrades()[i];
                if (data != null) {
                    GuiScreen gui = getConfigGui((IConfigurableMotorUpgrade<?>) data.getUpgrade(), i);
                    if (gui != null) {
                        mc.displayGuiScreen(gui);
                        break;
                    }
                }
            }
            if (x >= x_ + 9 + i * 31 && y >= y_ + 90 && x < x_ + 9 + 24 + i * 31 && y < y_ + 90 + 25) {
                mc.displayGuiScreen(new GuiUpgrade(motor, i, this));
                break;
            }
        }
    }

    @Override
    protected void mouseClickMove(int x, int y, int b, long t) {

        int x_ = (width - xSize) / 2;

        if (x < x_ + 8 || x > x_ + xSize - 8)
            return;
        if (clickX < 0)
            return;
        if (!slider.enabled)
            return;

        double d = x - clickX;
        clickX = x;

        slider.xPosition += d;

        slider.xPosition = Math.max(x_ + 8, Math.min(slider.xPosition, x_ + xSize - 8 - slider.width + 1));
        double perc = (slider.xPosition - x_ - 8) / (double) (xSize - 8 - slider.width + 1);
        tpm = getSliderTicks(perc);
        double val = unit.convertFromTPM(tpm);
        speed.setText(String.format("%.4f", val) + " " + unit.getUnit());
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int t) {

        super.mouseMovedOrUp(x, y, t);

        if (tpm < 0)
            return;
        if (t < 0)
            return;

        if (tpm != lastTpm) {
            motor.setSetting(MotorSetting.MOVEMENT_DURATION, tpm);
            lastTpm = tpm;
        }
    }

    @Override
    protected void keyTyped(char c, int k) {

        super.keyTyped(c, k);

        // int x_ = (width - xSize) / 2;
        //
        // if (k == 14 || (k == Keyboard.KEY_DELETE && speed.getCursorPosition() < speed.getText().length() - unit.getUnit().length() - 1)
        // || Character.isDigit(c) || k == Keyboard.KEY_LEFT
        // || (k == Keyboard.KEY_RIGHT && speed.getCursorPosition() < speed.getText().length() - unit.getUnit().length() - 1)
        // || (c == '.' && !speed.getText().substring(0, speed.getText().length() - unit.getUnit().length() - 1).contains("."))) {
        // if (!Character.isDigit(c) || speed.getText().length() < speed.getMaxStringLength() - (unit.getUnit()).length())
        // speed.textboxKeyTyped(c, k);
        // }
        // try {
        // double val = Double.parseDouble(speed.getText().substring(0, speed.getText().length() - unit.getUnit().length() - 1)
        // + (speed.getText().substring(0, speed.getText().length() - unit.getUnit().length() - 1).contains(".") ? "0" : ""));
        // if (k == Keyboard.KEY_RETURN) {
        // tpm = unit.convertToTPM(val);
        //
        // if (tpm != lastTpm) {
        // NetworkHandler.instance().sendToServer(new PacketUpdateMotorSpeed(motor, tpm));
        // lastTpm = tpm;
        // double d2 = getPercFromTicks(tpm) * (xSize - 8 - slider.width + 1);
        // slider.xPosition = (int) (x_ + 8 + d2);
        // }
        // }
        // } catch (Exception ex) {
        // speed.setText("0 " + unit.getUnit());
        // speed.setCursorPosition(1);
        // }
    }

    private double xmax = -1;

    private double getSliderTicks(double perc) {

        if (xmax == -1)
            xmax = Math.log10(FramezConfig.max_movement_ticks - getMinTicksPerMovement() + 1) / Math.log10(2);

        double x = perc * xmax;
        return Math.pow(2, x) - 1 + getMinTicksPerMovement();
    }

    private double getPercFromTicks(double ticks) {

        if (xmax == -1)
            xmax = Math.log10(FramezConfig.max_movement_ticks - getMinTicksPerMovement() + 1) / Math.log10(2);

        double x = Math.log10(ticks - getMinTicksPerMovement() + 1) / Math.log10(2);
        return x / xmax;
    }

    private double getMinTicksPerMovement() {

        return motor.minMovementTicks;
    }

}
