package com.amadornes.framez.init;

public class FramezConfig {

    public static class MetamorphicStone {

        public static boolean generate = true;

        public static int minVeinSize = 20;
        public static int maxVeinSize = 40;

        public static int minGenHeight = 0;
        public static int maxGenHeight = 100;

        public static int minVeinsPerChunk = 0;
        public static int maxVeinsPerChunk = 20;

        public static double rarity = 0.375D;
    }

    public static class Client {

        public static boolean connectContiguousFrames = false;
        public static boolean clickThroughFrames = false;

    }

}
