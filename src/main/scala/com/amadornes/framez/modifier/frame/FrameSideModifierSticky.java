package com.amadornes.framez.modifier.frame;

import java.util.Collection;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Translation;

import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.movement.IModifiableFrame;
import com.amadornes.framez.client.IconSupplier;
import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.ref.References;
import com.amadornes.trajectory.api.vec.BlockPos;

public class FrameSideModifierSticky implements IFrameSideModifier {

    public FrameSideModifierSticky() {

        // FrameMovementRegistry.instance().registerBlockStickinessHandler(new StickinessHandlerSticky());
    }

    @Override
    public String getType() {

        return References.Modifier.SIDE_STICKY;
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

        if (pass != 1)
            return false;

        RenderHelper h = RenderHelper.instance();

        boolean hidden = frame.isSideHidden(side);

        IIcon icon = IconSupplier.sidemod_sticky;
        // h.addRotation(side);

        BlockCoord bc = new BlockCoord(h.getCurrentX(), h.getCurrentY(), h.getCurrentZ()).offset(side);
        if (!hidden && !h.getCurrentBlockAccess().isSideSolid(bc.x, bc.y, bc.z, ForgeDirection.getOrientation(side ^ 1), false)) {
            CCRenderState.hasBrightness = false;
            BlockCoord current = new BlockCoord(h.getCurrentX(), h.getCurrentY(), h.getCurrentZ()).add(0, 1, 0);// BlockCoord.sideOffsets[side]);
            h.tmpOverrideLighting(h.getCurrentBlockAccess(), current.x, current.y, current.z);
            h.render(RenderHelper.sticky[0].copy().apply(new Translation(0, -1, 0)).computeLightCoords().computeNormals(), icon, icon,
                    icon, icon, icon, icon);
            h.resetLighting();
            CCRenderState.hasBrightness = true;
        }
        h.render(RenderHelper.sticky[1], icon, icon, icon, icon, icon, icon);

        // h.remRotation(side);

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

        return new ItemStack(Items.slime_ball);
    }

    // public class StickinessHandlerSticky implements IBlockStickinessHandler {
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
    // if (FramezUtils.hasModifier(f.getSideModifiers(side), FrameSideModifierSticky.this)) {
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
    // return FramezUtils.hasModifier(f.getSideModifiers(side), FrameSideModifierSticky.this);
    // }
    // }
}
