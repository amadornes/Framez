package com.amadornes.framez;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.api.IMotorRegistry;
import com.amadornes.framez.client.render.IRenderMotorSpecial;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MotorRegistry implements IMotorRegistry {

    public static final MotorRegistry INST = new MotorRegistry();

    private List<IMotorProvider> providers = new ArrayList<IMotorProvider>();
    @SideOnly(Side.CLIENT)
    private List<IRenderMotorSpecial> renderers = new ArrayList<IRenderMotorSpecial>();

    private MotorRegistry() {

    }

    @Override
    public void registerMotor(IMotorProvider provider) {

        if (provider == null)
            return;
        if (providers.contains(provider))
            return;

        providers.add(provider);
    }

    @Override
    public IMotorProvider[] getRegisteredMotors() {

        return providers.toArray(new IMotorProvider[0]);
    }

    @Override
    public void registerSpecialRenderer(IRenderMotorSpecial renderer) {

        if (renderer == null)
            return;
        if (renderers.contains(renderer))
            return;

        renderers.add(renderer);
    }

    @Override
    public IRenderMotorSpecial[] getRenderers(TileMotor tile, ForgeDirection face) {

        List<IRenderMotorSpecial> l = new ArrayList<IRenderMotorSpecial>();

        for (IRenderMotorSpecial r : renderers)
            if (r.shouldRender(tile, face))
                l.add(r);

        IRenderMotorSpecial[] renderers = l.toArray(new IRenderMotorSpecial[0]);
        l.clear();
        return renderers;
    }

}
