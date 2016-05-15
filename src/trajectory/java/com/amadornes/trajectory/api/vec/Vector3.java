package com.amadornes.trajectory.api.vec;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import net.minecraft.util.Vec3;

/**
 * Represents coordinates in 3 dimensions. Includes some useful operations to those coordinates.
 */
public class Vector3 {

    public static Vector3 zero = new Vector3();
    public static Vector3 one = new Vector3(1, 1, 1);
    public static Vector3 center = new Vector3(0.5, 0.5, 0.5);

    public double x;
    public double y;
    public double z;

    public Vector3() {

    }

    public Vector3(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 vec) {

        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    public Vector3(BlockPos translation) {

        x = translation.x;
        y = translation.y;
        z = translation.z;
    }

    public Vector3(Vec3 vec) {

        x = vec.xCoord;
        y = vec.yCoord;
        z = vec.zCoord;
    }

    public Vector3 copy() {

        return new Vector3(this);
    }

    public Vector3 set(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 set(Vector3 vec) {

        x = vec.x;
        y = vec.y;
        z = vec.z;
        return this;
    }

    public double getSide(int side) {

        switch (side) {
        case 0:
        case 1:
            return y;
        case 2:
        case 3:
            return z;
        case 4:
        case 5:
            return x;
        }
        throw new IndexOutOfBoundsException("Switch Falloff");
    }

    public Vector3 setSide(int s, double v) {

        switch (s) {
        case 0:
        case 1:
            y = v;
            break;
        case 2:
        case 3:
            z = v;
            break;
        case 4:
        case 5:
            x = v;
            break;
        default:
            throw new IndexOutOfBoundsException("Switch Falloff");
        }
        return this;
    }

    public double dot(Vector3 vec) {

        double d = vec.x * x + vec.y * y + vec.z * z;

        if (d > 1 && d < 1.00001)
            d = 1;
        else if (d < -1 && d > -1.00001)
            d = -1;
        return d;
    }

    public double dot(double d, double d1, double d2) {

        return d * x + d1 * y + d2 * z;
    }

    public Vector3 add(double x, double y, double z) {

        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3 add(Vector3 vec) {

        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    public Vector3 sub(double x, double y, double z) {

        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3 sub(Vector3 vec) {

        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    public Vector3 mul(double f) {

        x *= f;
        y *= f;
        z *= f;
        return this;
    }

    public Vector3 mul(double fx, double fy, double fz) {

        x *= fx;
        y *= fy;
        z *= fz;
        return this;
    }

    public Vector3 mul(Vector3 f) {

        x *= f.x;
        y *= f.y;
        z *= f.z;
        return this;
    }

    public Vector3 div(double f) {

        x /= f;
        y /= f;
        z /= f;
        return this;
    }

    public Vector3 div(double fx, double fy, double fz) {

        x /= fx;
        y /= fy;
        z /= fz;
        return this;
    }

    public Vector3 div(Vector3 f) {

        x /= f.x;
        y /= f.y;
        z /= f.z;
        return this;
    }

    public double mag() {

        return Math.sqrt(x * x + y * y + z * z);
    }

    public double magSq() {

        return x * x + y * y + z * z;
    }

    public Vector3 normalize() {

        double d = mag();
        if (d != 0) {
            mul(1 / d);
        }
        return this;
    }

    @Override
    public String toString() {

        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector3(" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ")";
    }

    public Vector3 rotate(double angle, double x, double y, double z) {

        return rotate(angle, new Vector3(x, y, z));
    }

    public Vector3 rotate(double angle, Vector3 axis) {

        return rotate(Quat.aroundAxis(axis.copy().normalize(), angle));
    }

    public Vector3 rotate(Quat rotator) {

        rotator.rotate(this);
        return this;
    }

    public double angle(Vector3 vec) {

        return Math.acos(copy().normalize().dot(vec.copy().normalize())) * MathHelper.todeg;
    }

    public boolean isZero() {

        return x == 0 && y == 0 && z == 0;
    }

    public boolean isAxial() {

        return x == 0 ? (y == 0 || z == 0) : (y == 0 && z == 0);
    }

    public Vector3 inverse() {

        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vector3 negate(Vector3 vec) {

        x = 2 * vec.x - x;
        y = 2 * vec.y - y;
        z = 2 * vec.z - z;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Vector3))
            return false;
        Vector3 v = (Vector3) o;
        return x == v.x && y == v.y && z == v.z;
    }

    /**
     * Equals method with tolerance
     *
     * @return true if this is equal to v within +-1E-5
     */
    public boolean equalsT(Vector3 v) {

        return MathHelper.isBetween(x - 1E-5, v.x, x + 1E-5) && MathHelper.isBetween(y - 1E-5, v.y, y + 1E-5)
                && MathHelper.isBetween(z - 1E-5, v.z, z + 1E-5);
    }

    public BlockPos toBlockPos() {

        return new BlockPos((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
    }

}
