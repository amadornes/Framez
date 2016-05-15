package com.amadornes.framez.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class BlockRendererDispatcher {

    private static final List<Integer> lists = new ArrayList<Integer>();

    public static int getNewRenderList() {

        // if (lists.size() == 0)
        return GL11.glGenLists(1);
        //
        // Integer l = lists.get(0);
        // lists.remove(0);
        // return l;
    }

    public static void invalidate(int list) {

        lists.add(list);
    }

}
