package com.amadornes.framez.movement;

import java.util.ArrayDeque;
import java.util.EnumSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import com.amadornes.framez.api.movement.IMovingStructure;
import com.amadornes.framez.api.movement.MovementIssue;
import com.amadornes.framez.util.AdvancedMutableBlockPos;
import com.amadornes.framez.util.Graph;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MovingStructure implements IMovingStructure {

    private final Map<MovingBlock, BlockPos> blocks;
    private final Set<MovementIssue> issues;

    private MovingStructure(Map<MovingBlock, BlockPos> blocks, Set<MovementIssue> issues) {

        this.blocks = blocks;
        this.issues = issues;
    }

    @Override
    public Map<MovingBlock, BlockPos> getBlocks() {

        return blocks;
    }

    @Override
    public Set<MovementIssue> getMovementIssues() {

        return issues;
    }

    public NBTTagCompound toNBT() {

        NBTTagCompound tag = new NBTTagCompound();

        NBTTagList blocks = new NBTTagList();
        for (MovingBlock b : getBlocks().keySet()) {

        }

        return new NBTTagCompound();// TODO: Serialize to NBT
    }

    public static MovingStructure fromNBT(NBTTagCompound tag) {

        return null;// TODO: Deserialize
    }

    @SafeVarargs
    public static MovingStructure discover(BlockPos pos, BiPredicate<IBlockAccess, BlockPos> ignored, EnumSet<EnumFacing> directions,
            boolean isMotorSticky, Function<Set<MovingBlock>, IMovement> movementSupplier, Supplier<World>... worlds) {

        Graph<MovingBlock> graph = new Graph<MovingBlock>();
        Queue<MovingBlock> nodes = new ArrayDeque<MovingBlock>();
        AdvancedMutableBlockPos mutablePos = new AdvancedMutableBlockPos();

        MovingBlock start = new MovingBlock(worlds[0], pos, directions, isMotorSticky);
        nodes.add(start);
        graph.addVertex(start);

        MovingBlock node;
        while (!nodes.isEmpty()) {
            node = nodes.poll();
            for (EnumFacing face : EnumFacing.VALUES) {
                if (node.isSticky(face) && !graph.getVertices().contains(mutablePos.set(node.pos.offset(face)))) {
                    for (Supplier<World> world : worlds) {
                        if (!ignored.test(world.get(), mutablePos)) {
                            MovingBlock neighbor = new MovingBlock(world, mutablePos.getImmutable());
                            if (neighbor.canStickTo(face.getOpposite())) {
                                graph.addEdge(node, neighbor);
                                nodes.add(neighbor);
                            }
                            break;
                        }
                    }
                }
            }
        }

        Graph<MovingBlock> optimalPath = graph.getMST(start);

        return null;// TODO: Create MovingStructure from graphs
    }

}
