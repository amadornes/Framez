package com.amadornes.framez.api.modifier;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.jtraits.ITrait;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFrameSideModifier extends IFrameModifier {

    @Override
    /**
     * UNUSED. This is a side modifier, sides cannot be modified individually
     */
    public Class<? extends ITrait> getTraitClass();

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(IFrame frame, ForgeDirection side, RenderHelper renderer, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(IFrame frame, ForgeDirection side, Vec3d pos, int pass, double partial_tick_time);

}
