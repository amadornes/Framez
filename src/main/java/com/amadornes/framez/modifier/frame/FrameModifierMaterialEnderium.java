package com.amadornes.framez.modifier.frame;

import java.util.Collection;

import net.minecraft.util.IIcon;

import com.amadornes.framez.api.modifier.IFrameModifierMaterial;
import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.client.IconSupplier;
import com.amadornes.framez.ref.References;
import com.amadornes.jtraits.ITrait;
import com.amadornes.jtraits.JTrait;

public class FrameModifierMaterialEnderium implements IFrameModifierMaterial {

    @Override
    public String getType() {

        return References.Modifier.MATERIAL_ENDERIUM;
    }

    @Override
    public boolean isCompatibleWith(IFrameModifier mod) {

        return !(mod instanceof IFrameModifierMaterial);
    }

    @Override
    public boolean isValidCombination(Collection<IFrameModifier> combination) {

        return false;
    }

    @Override
    public Class<? extends ITrait> getTraitClass() {

        return TFrameEnderium.class;
    }

    public static abstract class TFrameEnderium extends JTrait<IFrame> implements IFrame {

        @Override
        public IIcon getBorderIcon() {

            return IconSupplier.enderium_border;
        }

        @Override
        public IIcon getBorderPanelIcon() {

            return IconSupplier.enderium_border_panel;
        }

        @Override
        public IIcon getCrossIcon() {

            return IconSupplier.enderium_cross;
        }

        @Override
        public IIcon getSimpleIcon() {

            return IconSupplier.enderium_simple;
        }

        @Override
        public int getMaxMovedBlocks() {

            return 4;
        }

    }

}
