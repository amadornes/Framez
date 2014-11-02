package com.amadornes.framez.config;

public class Config {

    public static class Motors {

        public static boolean isRedstoneMotorEnabled = false;
        public static boolean isHydCraftMotorEnabled = true;
        public static boolean isIC2MotorEnabled = true;
        public static boolean isPneumaticCraftMotorEnabled = true;
        public static boolean isRedstoneFluxMotorEnabled = true;
        public static boolean isBloodMagicMotorEnabled = true;
        public static boolean isAEMotorEnabled = true;
    }

    /**
     * In FPU
     */
    public static class PowerUsage {

        public static double getPowerUsedPerMove = 1000;
        public static double getPowerUsedPerBlock = 100;
        public static double getPowerUsedPerTileEntity = 250; // 100 + 150 = 250
    }

    /**
     * Ratio of OriginalPower/FPU
     */
    public static class PowerRatios {

        public static double bmLP = 1.75;
        public static double hcPressure = 50;
        public static double eu = 2.5;
        public static double pcPressure = 0.001;
        public static double rf = 1;
    }

}
