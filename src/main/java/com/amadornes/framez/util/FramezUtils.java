package com.amadornes.framez.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.amadornes.framez.api.motor.IMotorVariable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class FramezUtils {

    public static void spawnItemInWorld(World world, ItemStack itemStack, double x, double y, double z) {

        if (world.isRemote) {
            return;
        }
        try {
            float dX = world.rand.nextFloat() * 0.8F + 0.1F;
            float dY = world.rand.nextFloat() * 0.8F + 0.1F;
            float dZ = world.rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ,
                    new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));

            if (itemStack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound(itemStack.getTagCompound().copy());
            }

            float factor = 0.05F;
            entityItem.motionX = world.rand.nextGaussian() * factor;
            entityItem.motionY = world.rand.nextGaussian() * factor + 0.2F;
            entityItem.motionZ = world.rand.nextGaussian() * factor;
            world.spawnEntityInWorld(entityItem);
            itemStack.stackSize = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method takes one very long string, and cuts it into lines which have a maxCharPerLine and returns it in a String list. it also
     * preserves color formats. \n can be used to force a carriage return.
     *
     * @author MineMaarten (PneumaticCraft)
     */
    public static List<String> split(String text, int maxCharsPerLine) {

        StringTokenizer tok = new StringTokenizer(text, " ");
        StringBuilder output = new StringBuilder(text.length());
        List<String> textList = new ArrayList<String>();
        String color = "";
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();
            boolean doAppend = true;
            if (word.contains("\u00a7")) {
                for (int i = 0; i < word.length() - 1; i++) {
                    if (word.substring(i, i + 2).contains("\u00a7")) {
                        color = word.substring(i, i + 2);
                        lineLen -= 2;
                    }
                }
            }
            if (lineLen + word.length() > maxCharsPerLine || word.contains("\\n")) {
                if (word.contains("\\n")) {
                    if (lineLen > 0) {
                        output.append(" ");
                    }
                    for (int i = 0; i < word.length(); i++) {
                        char c = word.charAt(i);
                        if (c == '\\' && i + 1 < word.length() && Character.toLowerCase(word.charAt(i + 1)) == 'n') {
                            textList.add(output.toString());
                            output.delete(0, output.length());
                            output.append(color);
                            i++;
                            lineLen = 0;
                        } else {
                            output.append(c);
                            doAppend = false;
                            lineLen++;
                        }
                    }
                } else {
                    textList.add(output.toString());
                    output.delete(0, output.length());
                    output.append(color);
                    lineLen = 0;
                }
            } else if (lineLen > 0) {
                output.append(" ");
                lineLen++;
            }
            if (doAppend) {
                output.append(word);
                lineLen += word.length();
            }
        }
        textList.add(output.toString());
        return textList;
    }

    @SuppressWarnings("unchecked")
    public static <T> String valueToString(IMotorVariable<T> var, Object value) {

        return var.valueToString((T) value);
    }

    private static final int[][] ROTATION_MATRIX = { //
            //
            { 0, 1, 4, 5, 3, 2, 6 }, //
            { 0, 1, 5, 4, 2, 3, 6 }, //
            { 5, 4, 2, 3, 0, 1, 6 }, //
            { 4, 5, 2, 3, 1, 0, 6 }, //
            { 2, 3, 1, 0, 4, 5, 6 }, //
            { 3, 2, 0, 1, 4, 5, 6 }, //
            { 0, 1, 2, 3, 4, 5, 6 },//
    };

    public static final EnumFacing rotate(EnumFacing dir, EnumFacing axis) {

        return EnumFacing.getFront(ROTATION_MATRIX[axis.ordinal()][dir.ordinal()]);
    }

}
