package com.amadornes.framez.util;

public class PowerHelper {

    public double convert(double value, PowerUnit from, PowerUnit to) {

        return (value / from.powerMultiplier) * to.powerMultiplier;
    }

    public static enum PowerUnit {
        HC_PRESSURE(5), EU(2.5), PC_PRESSURE(0.005), RF(1);

        private double powerMultiplier = 0;

        private PowerUnit(double powerMultiplier) {

            this.powerMultiplier = powerMultiplier;
        }

        public double getPowerMultiplier() {

            return powerMultiplier;
        }
    }

}
