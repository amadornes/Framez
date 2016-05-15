package com.amadornes.trajectory.api.vec;

/**
 * Represents block coordinates in 3 dimensions. Includes some useful operations to those coordinates.
 */
public class BlockPos {

    public static final BlockPos[] sideOffsets = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 1, 0), new BlockPos(0, 0, -1),
            new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0) };

    public int x;
    public int y;
    public int z;

    public BlockPos(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(BlockPos v) {

        this(v.x, v.y, v.z);
    }

    public BlockPos(int[] arr) {

        this(arr[0], arr[1], arr[2]);
    }

    public BlockPos() {

    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof BlockPos))
            return false;
        BlockPos o2 = (BlockPos) obj;
        return x == o2.x && y == o2.y && z == o2.z;
    }

    @Override
    public int hashCode() {

        return (x ^ z) * 31 + y;
    }

    public Vector3 center() {

        return new Vector3(x + 0.5, y + 0.5, z + 0.5);
    }

    public BlockPos add(int x, int y, int z) {

        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public BlockPos add(BlockPos vec) {

        return add(vec.x, vec.y, vec.z);
    }

    public BlockPos sub(int x, int y, int z) {

        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public BlockPos sub(BlockPos vec) {

        return sub(vec.x, vec.y, vec.z);
    }

    public BlockPos mul(int f) {

        return mul(f, f, f);
    }

    public BlockPos mul(int fx, int fy, int fz) {

        x *= fx;
        y *= fy;
        z *= fz;
        return this;
    }

    public double mag() {

        return Math.sqrt(x * x + y * y + z * z);
    }

    public int magSq() {

        return x * x + y * y + z * z;
    }

    public boolean isZero() {

        return x == 0 && y == 0 && z == 0;
    }

    public boolean isAxial() {

        return x == 0 ? (y == 0 || z == 0) : (y == 0 && z == 0);
    }

    public BlockPos offset(int side) {

        return offset(side, 1);
    }

    public BlockPos offset(int side, int amount) {

        BlockPos offset = sideOffsets[side];
        x += offset.x * amount;
        y += offset.y * amount;
        z += offset.z * amount;
        return this;
    }

    public int getSide(int side) {

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

    public BlockPos setSide(int s, int v) {

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

    public int[] intArray() {

        return new int[] { x, y, z };
    }

    public BlockPos copy() {

        return new BlockPos(x, y, z);
    }

    public BlockPos set(int i, int j, int k) {

        x = i;
        y = j;
        z = k;
        return this;
    }

    public BlockPos set(BlockPos coord) {

        return set(coord.x, coord.y, coord.z);
    }

    public BlockPos set(int[] ia) {

        return set(ia[0], ia[1], ia[2]);
    }

    public int toSide() {

        if (!isAxial())
            return -1;
        if (y < 0)
            return 0;
        if (y > 0)
            return 1;
        if (z < 0)
            return 2;
        if (z > 0)
            return 3;
        if (x < 0)
            return 4;
        if (x > 0)
            return 5;

        return -1;
    }

    public int absSum() {

        return (x < 0 ? -x : x) + (y < 0 ? -y : y) + (z < 0 ? -z : z);
    }

    @Override
    public String toString() {

        return "(" + x + ", " + y + ", " + z + ")";
    }

    public int[] toIntArray() {

        return new int[] { x, y, z };
    }
}
