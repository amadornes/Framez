package com.amadornes.framez.util;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.framez.tile.TileMotor;

public class ThreadBlockChecking extends Thread {

    private static final ThreadBlockChecking instance = new ThreadBlockChecking();

    public static ThreadBlockChecking instance() {

        return instance;
    }

    private List<TileMotor> toCheck = new ArrayList<TileMotor>();
    private boolean inWorld = false;

    public void addMotor(TileMotor te) {

        if (te == null)
            return;
        if (toCheck.contains(te))
            return;

        toCheck.add(te);
    }

    public void removeMotor(TileMotor te) {

        toCheck.remove(te);
    }

    public void onJoinWorld() {

        inWorld = true;
    }

    public void onLeaveWorld() {

        inWorld = false;
    }

    @Override
    public void run() {

        while (true) {
            if (!inWorld) {
                toCheck.clear();
                sleep();
                continue;
            }

            List<TileMotor> removed = new ArrayList<TileMotor>();
            for (TileMotor m : new ArrayList<TileMotor>(toCheck)) {
                if (m.isInvalid() || m.move())
                    removed.add(m);
            }
            toCheck.removeAll(removed);
            removed.clear();

            sleep();
        }
    }

    private void sleep() {

        try {
            Thread.sleep(500);
        } catch (Exception ex) {
        }
    }

}
