package com.amadornes.framez.modifier.frame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.oredict.OreDictionary;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;
import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IStickinessHandler;
import com.amadornes.framez.movement.FrameMovementRegistry;
import com.amadornes.framez.movement.MovementHelper;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.jtraits.ITrait;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FrameSideModifierLatching implements IFrameSideModifier {

    public FrameSideModifierLatching() {

        FrameMovementRegistry.instance().registerStickinessHandler(new StickinessHandlerLatching());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getType() {

        return References.Modifier.SIDE_LATCHING;
    }

    @Override
    public boolean isCompatibleWith(IFrameModifier mod) {

        return true;
    }

    @Override
    public boolean isValidCombination(Collection<IFrameModifier> combination) {

        return false;
    }

    @Override
    public Class<? extends ITrait> getTraitClass() {

        return null;
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent event) {

        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return;

        List<IFrame> frames = FrameMovementRegistry.instance().findFrames(event.world, event.x, event.y, event.z);
        ForgeDirection d = ForgeDirection.getOrientation(event.face);

        Iterator<IFrame> iterator = frames.iterator();
        while (iterator.hasNext()) {
            IFrame f = iterator.next();
            if (f.getSideModifiers(d).contains(this))
                iterator.remove();
        }

        if (frames.size() == 0)
            return;

        ItemStack is = event.entityPlayer.getCurrentEquippedItem();
        if (is == null)
            return;
        int iron = OreDictionary.getOreID("ingotIron");
        boolean isIron = false;
        for (int i : OreDictionary.getOreIDs(is)) {
            if (i == iron) {
                isIron = true;
                break;
            }
        }
        if (!isIron)
            return;

        if (is.stackSize < frames.size())
            return;

        if (event.world.isRemote) {
            event.entityPlayer.swingItem();
            return;
        }

        if (!event.entityPlayer.capabilities.isCreativeMode)
            is.stackSize -= frames.size();

        for (IFrame f : frames)
            f.addSideModifier(d, getType());
    }

    @Override
    public boolean renderStatic(IFrame frame, ForgeDirection side, RenderHelper renderer, int pass) {

        if (pass != 0)
            return false;

        boolean hidden = frame.isSideHidden(side);

        Vec3dCube cube = new Vec3dCube(3 / 32D, hidden ? 0.001 : -1 / 64D, 3 / 32D, 2 / 16D + 1 / 64D, 1 / 32D, 29 / 32D);
        Vec3dCube cube2 = new Vec3dCube(-1 / 256D, 3 / 256D, -1 / 256D, 1 / 32D, 13 / 256D, 1);
        for (int i = 0; i < 4; i++) {
            renderer.renderBox(cube.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(side, Vec3d.center), Blocks.anvil.getIcon(0, 0));
            renderer.renderBox(cube2.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(side, Vec3d.center), Blocks.anvil.getIcon(0, 0));
        }

        return true;
    }

    @Override
    public void renderDynamic(IFrame frame, ForgeDirection side, Vec3d pos, int pass, double partial_tick_time) {

    }

    public class StickinessHandlerLatching implements IStickinessHandler {

        private List<Pair<Vec3i, ForgeDirection>> ignored = new ArrayList<Pair<Vec3i, ForgeDirection>>();

        @Override
        @Priority(PriorityEnum.OVERRIDE)
        public boolean isSideSticky(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

            Collection<IFrame> frames = FrameMovementRegistry.instance().findFrames(world, x, y, z);
            if (frames == null || frames.size() == 0)
                return false;

            Pair<Vec3i, ForgeDirection> p = new Pair<Vec3i, ForgeDirection>(new Vec3i(x, y, z, world), side);
            ignored.add(p);

            for (IFrame f : frames) {
                if (FramezUtils.hasModifier(f.getSideModifiers(side), FrameSideModifierLatching.this)) {
                    Pair<List<MovingBlock>, List<Vec3i>> pair = MovementHelper.findMovedBlocks(world, x, y, z, side, movement,
                            Arrays.asList(new Vec3i(x, y, z, world)));
                    if (pair != null && pair.getValue().size() > 0) {
                        ignored.remove(p);
                        return false;
                    }
                }
            }

            ignored.remove(p);

            return true;
        }

        @Override
        public boolean canHandle(World world, int x, int y, int z, ForgeDirection side) {

            for (Pair<Vec3i, ForgeDirection> p : ignored)
                if (p.getKey().getX() == x && p.getKey().getY() == y && p.getKey().getZ() == z && p.getKey().getWorld().equals(world)
                        && p.getValue() == side)
                    return false;

            Collection<IFrame> frames = FrameMovementRegistry.instance().findFrames(world, x, y, z);
            if (frames == null || frames.size() == 0)
                return false;

            for (IFrame f : frames)
                if (FramezUtils.hasModifier(f.getSideModifiers(side), FrameSideModifierLatching.this))
                    return true;

            return false;
        }
    }
}
