package com.amadornes.framez.ref;

public class References {

    public static final String getTextureName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    public static class Modifiers {

        public static final String CONNECTED = "connected";
        public static final String IRON = "iron";
        public static final String GLASS = "glass";
        public static final String CLEAR_GLASS = "glass_clear";

    }

    public static class Names {

        public static class Unlocalized {

            public static final String MOTOR = ModInfo.MODID + ":motor";
            public static final String FRAME = ModInfo.MODID + ":frame";
            public static final String WRENCH = ModInfo.MODID + ":wrench";
            public static final String IRON_STICK = ModInfo.MODID + ":stickiron";

            private static final String FRAME_PART = ModInfo.MODID + ":framepart";
            public static final String FRAME_PART_CROSS = FRAME_PART + ".cross";
            public static final String FRAME_PART_FRAME = FRAME_PART + ".frame";
            public static final String FRAME_PART_CROSS_IRON = FRAME_PART_CROSS + ".iron";
            public static final String FRAME_PART_FRAME_IRON = FRAME_PART_FRAME + ".iron";
        }

        public static class Registry {

            public static final String FRAME = "frame";
            public static final String MOTOR = "motor";
            public static final String MOVING = "moving";
            public static final String WRENCH = "wrench";
            public static final String FRAME_PART = "framepart";
            public static final String IRON_STICK = "stickIron";
        }
    }

    public static class Textures {

        public static final String MOTOR = ModInfo.MODID + ":motor";
        public static final String WRENCH = ModInfo.MODID + ":wrench";

        public static final String FRAME_PART_CROSS = ModInfo.MODID + ":frameCross";
        public static final String FRAME_PART_FRAME = ModInfo.MODID + ":frame";
        public static final String FRAME_PART_CROSS_IRON = ModInfo.MODID + ":frameCrossIron";
        public static final String FRAME_PART_FRAME_IRON = ModInfo.MODID + ":frameIron";

        public static final String IRON_STICK = ModInfo.MODID + ":stickIron";

    }

}
