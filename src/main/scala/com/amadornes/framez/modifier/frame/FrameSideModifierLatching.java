package com.amadornes.framez.modifier.frame;

import java.util.Collection;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.movement.IModifiableFrame;
import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.ref.References;
import com.amadornes.trajectory.api.vec.BlockPos;

public class FrameSideModifierLatching implements IFrameSideModifier {

    // public FrameSideModifierLatching() {
    //
    // FrameMovementRegistry.instance().registerBlockStickinessHandler(new StickinessHandlerLatching());
    // }

    @Override
    public String getType() {

        return References.Modifier.SIDE_LATCHING;
    }

    @Override
    public boolean isCompatibleWith(IFrameSideModifier mod) {

        return true;
    }

    @Override
    public boolean isCombinationValid(Collection<IFrameSideModifier> combination) {

        return true;
    }

    @Override
    public boolean renderStatic(IBlockAccess world, BlockPos position, IModifiableFrame frame, int side, int pass) {

        if (pass != 0)
            return false;

        RenderHelper renderer = RenderHelper.instance();

        boolean hidden = frame.isSideHidden(side);

        IIcon icon = Blocks.anvil.getIcon(0, 0);
        renderer.addTranslation(-renderer.getCurrentX() - 0.5, -renderer.getCurrentY() - 0.5, -renderer.getCurrentZ() - 0.5);
        renderer.addRotation(side);
        renderer.addTranslation(renderer.getCurrentX() + 0.5, renderer.getCurrentY() + 0.5, renderer.getCurrentZ() + 0.5);

        renderer.render(RenderHelper.latching[hidden ? 1 : 0], icon, icon, icon, icon, icon, icon);

        renderer.addTranslation(-renderer.getCurrentX() - 0.5, -renderer.getCurrentY() - 0.5, -renderer.getCurrentZ() - 0.5);
        renderer.remRotation(side);
        renderer.addTranslation(renderer.getCurrentX() + 0.5, renderer.getCurrentY() + 0.5, renderer.getCurrentZ() + 0.5);

        return true;
    }

    @Override
    public void renderDynamic(IBlockAccess world, BlockPos position, IModifiableFrame frame, int side, int pass, float f) {

    }

    @Override
    public void renderItem(ItemStack stack, int side, ItemRenderType type) {

    }

    @Override
    public boolean canApplyTo(IModifiableFrame frame, int side) {

        return true;
    }

    @Override
    public ItemStack getCraftingItem() {

        return new ItemStack(Items.iron_ingot);
    }

    // public class StickinessHandlerLatching implements IBlockStickinessHandler {
    //
    // private List<Pair<BlockCoord, Integer>> ignored = new ArrayList<Pair<BlockCoord, Integer>>();
    //
    // @Override
    // @Priority(PriorityEnum.OVERRIDE)
    // public boolean isSideSticky(World world, int x, int y, int z, int side, IMovement movement, IMotor motor) {
    //
    // IFrame f = FrameMovementRegistry.instance().findFrame(world, x, y, z);
    // if (f == null)
    // return false;
    //
    // Pair<BlockCoord, Integer> p = new ImmutablePair<BlockCoord, Integer>(new BlockCoord(x, y, z), side);
    // ignored.add(p);
    //
    // if (FramezUtils.hasModifier(f.getSideModifiers(side), FrameSideModifierLatching.this)) {
    // Pair<List<MovingBlock>, List<Pair<BlockCoord, Integer>>> pair = MovementHelper.findMovedBlocks(world, x, y, z, side, movement, motor,
    // Arrays.asList(new BlockCoord(x, y, z)));
    // if (pair != null && pair.getValue().size() > 0) {
    // ignored.remove(p);
    // return false;
    // }
    // }
    //
    // ignored.remove(p);
    //
    // return true;
    // }
    //
    // @Override
    // public boolean canHandle(World world, int x, int y, int z, int side) {
    //
    // for (Pair<BlockCoord, Integer> p : ignored)
    // if (p.getKey().x == x && p.getKey().y == y && p.getKey().z == z && p.getValue() == side)
    // return false;
    //
    // IFrame f = FrameMovementRegistry.instance().findFrame(world, x, y, z);
    // if (f == null)
    // return false;
    // return FramezUtils.hasModifier(f.getSideModifiers(side), FrameSideModifierLatching.this);
    // }
    // }
}
