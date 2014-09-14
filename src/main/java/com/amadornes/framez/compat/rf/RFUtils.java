package com.amadornes.framez.compat.rf;

public class RFUtils {

    public static final boolean isRFApiLoaded() {

        try {
            return Class.forName("cofh.api.energy.IEnergyHandler") != null;
        } catch (Exception ex) {
        }
        return false;
    }

}
