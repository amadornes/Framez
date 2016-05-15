package com.amadornes.trajectory.api.vec;

/**
 * Math helper class. Includes a couple of useful methods used mostly in the {@link Vector3} class.
 */
public class MathHelper {

    public static final double phi = 1.618033988749894;
    public static final double pi = Math.PI;
    public static final double todeg = 57.29577951308232;
    public static final double torad = 0.017453292519943;

    public static double[] SIN_TABLE = new double[65536];
    static {
        for (int i = 0; i < 65536; ++i)
            SIN_TABLE[i] = Math.sin(i / 65536D * 2 * Math.PI);

        SIN_TABLE[0] = 0;
        SIN_TABLE[16384] = 1;
        SIN_TABLE[32768] = 0;
        SIN_TABLE[49152] = 1;
    }

    public static double sin(double d) {

        return SIN_TABLE[(int) ((float) d * 10430.378F) & 65535];
    }

    public static double cos(double d) {

        return SIN_TABLE[(int) ((float) d * 10430.378F + 16384.0F) & 65535];
    }

    public static boolean isBetween(double a, double x, double b) {

        return a <= x && x <= b;
    }

    public static int floor(double d) {

        return (int) Math.floor(d);
    }

    public static int roundAway(double d) {

        return (int) (d < 0 ? Math.floor(d) : Math.ceil(d));
    }
}
