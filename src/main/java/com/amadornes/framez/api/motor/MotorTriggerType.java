package com.amadornes.framez.api.motor;

public final class MotorTriggerType {

    public static final MotorTriggerType TYPE_CONSTANT = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE, "constant",
            "trigtype:framez.constant");
    public static final MotorTriggerType TYPE_REDSTONE = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE, "redstone",
            "trigtype:framez.redstone");

    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_WHITE = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.white", "trigtype:framez.redstone.white");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_ORANGE = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.orange", "trigtype:framez.redstone.orange");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_MAGENTA = new MotorTriggerType(
            MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED, "redstone.magenta", "trigtype:framez.redstone.magenta");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_LIGHT_BLUE = new MotorTriggerType(
            MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED, "redstone.light_blue", "trigtype:framez.redstone.light_blue");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_YELLOW = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.yellow", "trigtype:framez.redstone.yellow");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_LIME = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.lime", "trigtype:framez.redstone.lime");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_PINK = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.pink", "trigtype:framez.redstone.pink");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_GRAY = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.gray", "trigtype:framez.redstone.gray");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_SILVER = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.silver", "trigtype:framez.redstone.silver");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_CYAN = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.cyan", "trigtype:framez.redstone.cyan");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_PURPLE = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.purple", "trigtype:framez.redstone.purple");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_BLUE = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.blue", "trigtype:framez.redstone.blue");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_BROWN = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.brown", "trigtype:framez.redstone.brown");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_GREEN = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.green", "trigtype:framez.redstone.green");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_RED = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.red", "trigtype:framez.redstone.red");
    public static final MotorTriggerType TYPE_REDSTONE_BUNLDED_BLACK = new MotorTriggerType(MotorTriggerCategory.CATEGORY_REDSTONE_BUNDLED,
            "redstone.black", "trigtype:framez.redstone.black");

    public static final MotorTriggerType[] TYPE_REDSTONE_BUNLDED = { //
            TYPE_REDSTONE_BUNLDED_WHITE, //
            TYPE_REDSTONE_BUNLDED_ORANGE, //
            TYPE_REDSTONE_BUNLDED_MAGENTA, //
            TYPE_REDSTONE_BUNLDED_LIGHT_BLUE, //
            TYPE_REDSTONE_BUNLDED_YELLOW, //
            TYPE_REDSTONE_BUNLDED_LIME, //
            TYPE_REDSTONE_BUNLDED_PINK, //
            TYPE_REDSTONE_BUNLDED_GRAY, //
            TYPE_REDSTONE_BUNLDED_SILVER, //
            TYPE_REDSTONE_BUNLDED_CYAN, //
            TYPE_REDSTONE_BUNLDED_PURPLE, //
            TYPE_REDSTONE_BUNLDED_BLUE, //
            TYPE_REDSTONE_BUNLDED_BROWN, //
            TYPE_REDSTONE_BUNLDED_GREEN, //
            TYPE_REDSTONE_BUNLDED_RED, //
            TYPE_REDSTONE_BUNLDED_BLACK//
    };

    private final MotorTriggerCategory category;
    private final String type, unlocalizedName;

    public MotorTriggerType(MotorTriggerCategory category, String type, String unlocalizedName) {

        this.category = category;
        this.type = type;
        this.unlocalizedName = unlocalizedName;
    }

    public MotorTriggerCategory getCategory() {

        return category;
    }

    public String getType() {

        return type;
    }

    public String getUnlocalizedName() {

        return unlocalizedName;
    }

}
