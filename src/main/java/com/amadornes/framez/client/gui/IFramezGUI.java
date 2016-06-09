package com.amadornes.framez.client.gui;

public interface IFramezGUI {

    public default boolean isWithinBounds(int x, int y, int width, int height, int mouseX, int mouseY) {

        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

}
