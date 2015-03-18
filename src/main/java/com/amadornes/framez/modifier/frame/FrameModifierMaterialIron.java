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

public class FrameModifierMaterialIron implements IFrameModifierMaterial {

    @Override
    public String getType() {

        return References.Modifier.MATERIAL_IRON;
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

        return TFrameIron.class;
    }

    public static abstract class TFrameIron extends JTrait<IFrame> implements IFrame {

        @Override
        public IIcon getBorderIcon() {

            return IconSupplier.iron_border;
        }

        @Override
        public IIcon getBorderPanelIcon() {

            return IconSupplier.iron_border_panel;
        }

        @Override
        public IIcon getCrossIcon() {

            return IconSupplier.iron_cross;
        }

        @Override
        public IIcon getSimpleIcon() {

            return IconSupplier.iron_simple;
        }

        @Override
        public int getMaxMovedBlocks() {

            return 4;
        }

    }

}
