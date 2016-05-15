package com.amadornes.framez.compat.botania;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.compat.Dependencies;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.jtraits.JTrait;

import cpw.mods.fml.common.registry.GameRegistry;

public class MotorModifierMana implements IMotorModifierPower {

    @Override
    public String getType() {

        return "mana";
    }

    @Override
    public boolean isCompatibleWith(IMotorModifier mod) {

        return true;
    }

    @Override
    public boolean isCombinationValid(Collection<IMotorModifier> combination) {

        return true;
    }

    @Override
    public void registerRecipes(ItemStack unmodified, ItemStack modified) {

        Item rune = GameRegistry.findItem(Dependencies.BOTANIA, "rune");
        Item tablet = GameRegistry.findItem(Dependencies.BOTANIA, "manaTablet");
        Item mana_resource = GameRegistry.findItem(Dependencies.BOTANIA, "manaResource");
        Block piston_relay = GameRegistry.findBlock(Dependencies.BOTANIA, "pistonRelay");

        if (rune != null && tablet != null && mana_resource != null && piston_relay != null)
            GameRegistry.addShapedRecipe(modified, "iti", "rur", "ifi", 'i', new ItemStack(mana_resource, 1, 0), 't', new ItemStack(tablet,
                    1, OreDictionary.WILDCARD_VALUE), 'r', new ItemStack(rune, 1, 8), 'u', unmodified, 'f', new ItemStack(piston_relay, 1,
                    0));
    }

    @Override
    public Class<? extends JTrait<? extends IMotor>> getTraitClass() {

        return TMotorMana.class;
    }

    public static class TMotorMana extends JTrait<IMotor> implements IManaReceiver, ISparkAttachable {

        @Override
        public int getCurrentMana() {

            return (int) (_super.getEnergyBuffer() / FramezCompatConfig.ratio_mana);
        }

        @Override
        public boolean areIncomingTranfersDone() {

            return isFull();
        }

        @Override
        public void attachSpark(ISparkEntity arg0) {

        }

        @Override
        public boolean canAttachSpark(ItemStack arg0) {

            return true;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public ISparkEntity getAttachedSpark() {

            List sparks = _super.getWorld().getEntitiesWithinAABB(
                    ISparkEntity.class,
                    AxisAlignedBB.getBoundingBox(_super.getX(), _super.getY() + 1, _super.getZ(), _super.getX() + 1, _super.getY() + 2,
                            _super.getZ() + 1));
            if (sparks.size() == 1)
                return (ISparkEntity) sparks.get(0);
            return null;
        }

        @Override
        public int getAvailableSpaceForMana() {

            return (int) ((_super.getEnergyBufferSize() - _super.getEnergyBuffer()) / FramezCompatConfig.ratio_mana);
        }

        @Override
        public boolean canRecieveManaFromBursts() {

            return true;
        }

        @Override
        public boolean isFull() {

            return _super.getEnergyBufferSize() == _super.getEnergyBuffer();
        }

        @Override
        public void recieveMana(int amt) {

            _super.injectPower(amt * FramezCompatConfig.ratio_mana, false);
        }

    }

}
