package com.amadornes.framez.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import com.amadornes.framez.config.ConfigurationHandler;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class GuiConfigFramez extends GuiConfig {

    public GuiConfigFramez(GuiScreen parent) {

        super(parent, GuiConfigFramez.getConfigElements(), ModInfo.MODID, false, true, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.cfg
                .toString()));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<IConfigElement> getConfigElements() {

        List<IConfigElement> list = new ArrayList<IConfigElement>();

        List<GuiConfigFramez> motors = new ConfigElement(ConfigurationHandler.cfg.getCategory(ConfigurationHandler.CATEGORY_MOTORS))
        .getChildElements();
        List<GuiConfigFramez> power = new ConfigElement(ConfigurationHandler.cfg.getCategory(ConfigurationHandler.CATEGORY_POWER)).getChildElements();

        list.add(new DummyConfigElement.DummyCategoryElement("Motors", ModInfo.MODID + ".cfg." + ConfigurationHandler.CATEGORY_MOTORS, motors));
        list.add(new DummyConfigElement.DummyCategoryElement("Power", ModInfo.MODID + ".cfg." + ConfigurationHandler.CATEGORY_POWER, power));

        return list;
    }
}