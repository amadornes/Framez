package com.amadornes.framez.block;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMoving;

public class BlockMoving extends BlockContainer {

    public BlockMoving() {

        super(Material.rock);

        setBlockName(References.BLOCK_MOVING_NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World w, int meta) {

        return new TileMoving();// A TE will be assigned automatically
    }

    private TileMoving get(World w, int x, int y, int z) {

        TileEntity te = w.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMoving)
            return (TileMoving) te;

        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB aabb, List l, Entity e) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return;

        te.addCollisionBoxesToList(aabb, l, e);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

        return te.getSelectedBoundingBox();
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 start, Vec3 end) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return null;

        return te.rayTrace(start, end);
    }

    @Override
    public int getRenderType() {

        return -1;
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {

        return false;
    }

}
