package com.amadornes.framez.movement;

import java.util.ArrayDeque;
import java.util.EnumSet;
import java.util.Queue;

import com.amadornes.framez.api.frame.IFrame;
import com.amadornes.framez.api.frame.IStickable;
import com.amadornes.framez.api.frame.ISticky;
import com.amadornes.framez.frame.FrameHelper;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.util.Graph;
import com.amadornes.framez.util.Graph.INode;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public final class Structure {

    public static Structure discover(IBlockAccess world, BlockPos pos, EnumSet<EnumFacing> directions, boolean isMotorSticky) {

        Graph<StructureNode> graph = new Graph<StructureNode>();
        Queue<StructureNode> nodes = new ArrayDeque<StructureNode>();
        AdvancedMutableBlockPos mutablePos = new AdvancedMutableBlockPos();

        StructureNode start = new StructureNode(world, pos, directions, isMotorSticky);
        nodes.add(start);
        graph.addVertex(start);

        StructureNode node;
        while (!nodes.isEmpty()) {
            node = nodes.poll();
            for (EnumFacing face : EnumFacing.VALUES) {
                if (node.isSticky(face) && !graph.getVertices().contains(mutablePos.set(node.pos.offset(face)))) {
                    StructureNode neighbor = new StructureNode(world, mutablePos.getImmutable());
                    if (neighbor.canStickTo(face.getOpposite())) {
                        graph.addEdge(node, neighbor);
                        nodes.add(neighbor);
                    }
                }
            }
        }

        System.out.println("Nodes: " + graph.getVertices().size());
        Graph<StructureNode> optimalPath = graph.getMST(start);
        System.out.println("Optimal: " + optimalPath.getVertices().size());

        return null;
    }

    public static class StructureNode implements INode {

        public final IBlockAccess world;
        public final BlockPos pos;
        public final IBlockState state;
        public final TileEntity tile;
        public final IFrame frame;

        private EnumSet<EnumFacing> directions = null;
        private boolean canStickTo = true;

        private ISticky[] stickySides = null;
        private IStickable[] stickableSides = null;

        public StructureNode(IBlockAccess world, BlockPos pos) {

            this(world, pos, null, true);
        }

        public StructureNode(IBlockAccess world, BlockPos pos, EnumSet<EnumFacing> directions, boolean canStickTo) {

            this(world, pos, world.getBlockState(pos), world.getTileEntity(pos), directions);
            this.canStickTo = canStickTo;
        }

        public StructureNode(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, EnumSet<EnumFacing> directions) {

            this.world = world;
            this.pos = pos;
            this.state = state;
            this.tile = tile;
            this.frame = FrameRegistry.INSTANCE.getFrame(this.tile);
            this.directions = directions;
        }

        @Override
        public int getMaxNeighbors() {

            return directions != null ? directions.size() : FrameHelper.getMaxCarriedBlocks(frame);
        }

        public boolean isSticky(EnumFacing face) {

            if (directions != null) return directions.contains(face);

            if (stickySides == null) {
                stickySides = new ISticky[6];
                for (int i = 0; i < 6; i++)
                    stickySides[i] = FrameRegistry.INSTANCE.getSticky(world, pos, face, tile);
            }
            return stickySides[face.ordinal()] != null && stickySides[face.ordinal()].isSticky();
        }

        public boolean canStickTo(EnumFacing face) {

            if (!canStickTo) return false;
            if (directions != null) return directions.contains(face);

            if (stickableSides == null) {
                stickableSides = new IStickable[6];
                for (int i = 0; i < 6; i++)
                    stickableSides[i] = FrameRegistry.INSTANCE.getStickable(world, pos, face, tile);
            }
            if (stickableSides[face.ordinal()] != null) {
                return stickableSides[face.ordinal()].canStickTo();
            } else {
                return !state.getBlock().isAir(world, pos);
            }
        }

        @Override
        public int hashCode() {

            return pos.hashCode();
        }

    }

    private static final class AdvancedMutableBlockPos extends BlockPos {

        private int x;
        private int y;
        private int z;

        public AdvancedMutableBlockPos() {

            super(0, 0, 0);
        }

        @Override
        public int getX() {

            return this.x;
        }

        @Override
        public int getY() {

            return this.y;
        }

        @Override
        public int getZ() {

            return this.z;
        }

        public AdvancedMutableBlockPos set(BlockPos pos) {

            return set(pos.getX(), pos.getY(), pos.getZ());
        }

        public AdvancedMutableBlockPos set(int x, int y, int z) {

            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        @Override
        public BlockPos getImmutable() {

            return new BlockPos(this);
        }

        @Override
        public boolean equals(Object o) {

            return super.equals(((StructureNode) o).pos);
        }

    }

}
