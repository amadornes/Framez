package com.amadornes.framez.config;

public class Config {

    public static class Motors {

        public static boolean isRedstoneMotorEnabled = false;
        public static boolean isHydCraftMotorEnabled = true;
        public static boolean isIC2MotorEnabled = true;
        public static boolean isPneumaticCraftMotorEnabled = true;
        public static boolean isRedstoneFluxMotorEnabled = true;
    }

    /**
     * In RF
     */
    public static class Power {

        public static double getPowerUsedPerMove = 1000;
        public static double getPowerUsedPerBlock = 100;
        public static double getPowerUsedPerTileEntity = 150; // 100 + 150 = 250
    }

}
