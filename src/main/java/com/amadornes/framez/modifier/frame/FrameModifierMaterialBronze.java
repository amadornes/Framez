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

public class FrameModifierMaterialBronze implements IFrameModifierMaterial {

    @Override
    public String getType() {

        return References.Modifier.MATERIAL_BRONZE;
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

        return TFrameBronze.class;
    }

    public static abstract class TFrameBronze extends JTrait<IFrame> implements IFrame {

        @Override
        public IIcon getBorderIcon() {

            return IconSupplier.bronze_border;
        }

        @Override
        public IIcon getBorderPanelIcon() {

            return IconSupplier.bronze_border_panel;
        }

        @Override
        public IIcon getCrossIcon() {

            return IconSupplier.bronze_cross;
        }

        @Override
        public IIcon getSimpleIcon() {

            return IconSupplier.bronze_simple;
        }

        @Override
        public int getMaxMovedBlocks() {

            return 4;
        }

    }

}
