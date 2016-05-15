package com.amadornes.framez.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.CompatRegistryImpl;
import com.amadornes.framez.api.compat.IFramePlacementHandler;
import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.modifier.IModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IModifiableFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.wrench.IFramePart;
import com.amadornes.framez.api.wrench.IFramePartHandler;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketPlayFramePlaceSound;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;

public class FramezUtils {

    public static final int[][] ROTATION_MATRIX = { { 0, 1, 4, 5, 3, 2, 6 }, { 0, 1, 5, 4, 2, 3, 6 }, { 5, 4, 2, 3, 0, 1, 6 },
            { 4, 5, 2, 3, 1, 0, 6 }, { 2, 3, 1, 0, 4, 5, 6 }, { 3, 2, 0, 1, 4, 5, 6 }, { 0, 1, 2, 3, 4, 5, 6 } };
    public static final String[] DIRECTION_NAMES = new String[] { "DOWN", "UP", "NORTH", "SOUTH", "WEST", "EAST" };

    public static BlockCoord getOffset(int side) {

        return new BlockCoord(getOffsetX(side), getOffsetY(side), getOffsetZ(side));
    }

    public static int getOffsetX(int side) {

        if (side == 4)
            return -1;
        else if (side == 5)
            return 1;

        return 0;
    }

    public static int getOffsetY(int side) {

        if (side == 0)
            return -1;
        else if (side == 1)
            return 1;

        return 0;
    }

    public static int getOffsetZ(int side) {

        if (side == 2)
            return -1;
        else if (side == 3)
            return 1;

        return 0;
    }

    public static void spawnItemInWorld(World world, ItemStack itemStack, double x, double y, double z) {

        if (world.isRemote)
            return;
        try {
            float dX = world.rand.nextFloat() * 0.8F + 0.1F;
            float dY = world.rand.nextFloat() * 0.8F + 0.1F;
            float dZ = world.rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.getItem(), itemStack.stackSize,
                    itemStack.getItemDamage()));

            if (itemStack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
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
     * This method takes one very long string, and cuts it into lines which have a maxCharPerLine and returns it in a String list. it also preserves
     * color formats. \n can be used to force a carriage return.
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
                    if (lineLen > 0)
                        output.append(" ");
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

    public static <T> boolean contains(T[] array, T object) {

        for (T obj : array)
            if (object.equals(obj))
                return true;

        return false;
    }

    public static <T> T[] addElement(T[] array, T object) {

        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = object;
        return array;
    }

    public static <T> T[] removeElement(T[] array, T object) {

        T[] nArray = Arrays.copyOf(array, array.length - 1);
        int i = 0, j = -1;
        for (T obj : array) {
            j++;
            if (obj == object)
                continue;
            nArray[i] = array[j];
        }
        return nArray;
    }

    public static boolean hasModifier(IModifier<?>[] mods, String modifier) {

        for (IModifier<?> m : mods)
            if (m.getType().equals(modifier))
                return true;
        return false;
    }

    public static MovingObjectPosition rayTraceFrame(World world, int x, int y, int z, Vec3 start, Vec3 end) {

        Block block = Blocks.stone;

        List<MovingObjectPosition> l = new ArrayList<MovingObjectPosition>();

        if (!FramezConfig.click_through_frames) {
            block.setBlockBounds(1 / 16F, 1 / 16F, 1 / 16F, 15 / 16F, 15 / 16F, 15 / 16F);
            l.add(block.collisionRayTrace(world, x, y, z, start, end));
        }

        block.setBlockBounds(0 / 16F, 0 / 16F, 0 / 16F, 2 / 16F, 2 / 16F, 16 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(14 / 16F, 0 / 16F, 0 / 16F, 16 / 16F, 2 / 16F, 16 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(0 / 16F, 0 / 16F, 0 / 16F, 16 / 16F, 2 / 16F, 2 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(0 / 16F, 0 / 16F, 14 / 16F, 16 / 16F, 2 / 16F, 16 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));

        block.setBlockBounds(0 / 16F, 14 / 16F, 0 / 16F, 2 / 16F, 16 / 16F, 16 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(14 / 16F, 14 / 16F, 0 / 16F, 16 / 16F, 16 / 16F, 16 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(0 / 16F, 14 / 16F, 0 / 16F, 16 / 16F, 16 / 16F, 2 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(0 / 16F, 14 / 16F, 14 / 16F, 16 / 16F, 16 / 16F, 16 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));

        block.setBlockBounds(0 / 16F, 2 / 16F, 0 / 16F, 2 / 16F, 14 / 16F, 2 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(14 / 16F, 2 / 16F, 0 / 16F, 16 / 16F, 14 / 16F, 2 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(14 / 16F, 2 / 16F, 14 / 16F, 16 / 16F, 14 / 16F, 16 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));
        block.setBlockBounds(0 / 16F, 2 / 16F, 14 / 16F, 2 / 16F, 14 / 16F, 16 / 16F);
        l.add(block.collisionRayTrace(world, x, y, z, start, end));

        while (l.contains(null))
            l.remove(null);

        double dist = Integer.MAX_VALUE;
        MovingObjectPosition closest = null;

        for (MovingObjectPosition m : l) {
            double d = m.hitVec.distanceTo(start);
            if (d < dist) {
                dist = d;
                closest = m;
            }
        }

        block.setBlockBounds(0, 0, 0, 1, 1, 1);

        return closest;
    }

    public static ItemStack getItem(World world, BlockPos position) {

        IFrame frame = MovementRegistry.instance.getFrameAt(world, position);
        if (frame == null)
            return null;

        return new ItemStack(FramezBlocks.frames.get(FrameFactory.getIdentifier("frame0", frame.getMaterial())));
    }

    public static ItemStack silkHarvest(World world, BlockPos position, boolean simulated) {

        // Try to find a frame at the specified location
        IFrame frame = MovementRegistry.instance.getFrameAt(world, position);
        if (frame == null)
            return null;

        // Create an item of the material of the frame and with the specified modifiers
        ItemStack stack = new ItemStack(FramezBlocks.frames.get(FrameFactory.getIdentifier("frame0", frame.getMaterial())));

        // Write the frame's data to the stack's tag
        NBTTagCompound tag = stack.stackTagCompound = new NBTTagCompound();
        tag.setInteger("hidden", ((frame.isSideHidden(0) ? 1 : 0) << 0) + ((frame.isSideHidden(1) ? 1 : 0) << 1)
                + ((frame.isSideHidden(2) ? 1 : 0) << 2) + ((frame.isSideHidden(3) ? 1 : 0) << 3) + ((frame.isSideHidden(4) ? 1 : 0) << 4)
                + ((frame.isSideHidden(5) ? 1 : 0) << 5));
        tag.setInteger("blocked",
                ((frame.isSideBlocked(0) ? 1 : 0) << 0) + ((frame.isSideBlocked(1) ? 1 : 0) << 1) + ((frame.isSideBlocked(2) ? 1 : 0) << 2)
                        + ((frame.isSideBlocked(3) ? 1 : 0) << 3) + ((frame.isSideBlocked(4) ? 1 : 0) << 4)
                        + ((frame.isSideBlocked(5) ? 1 : 0) << 5));

        // Write the frame's side modifiers to the stack's tag
        for (int i = 0; i < 6; i++) {
            NBTTagList modifiers = new NBTTagList();
            if (frame instanceof IModifiableFrame) {
                for (IFrameSideModifier modifier : ((IModifiableFrame) frame).getSideModifiers(i)) {
                    NBTTagCompound t = new NBTTagCompound();
                    t.setString("type", modifier.getType());
                    modifiers.appendTag(t);
                }
            }
            tag.setTag("side_" + i, modifiers);
        }

        // Write the frame frame parts in this block to the stack's tag
        NBTTagList parts = new NBTTagList();
        for (IFramePartHandler handler : ModifierRegistry.instance.framePartHandlers) {
            for (IFramePart part : handler.silkHarvest(world, position, simulated)) {
                NBTTagCompound t = new NBTTagCompound();
                t.setString("type", part.getType());
                part.writePickedToNBT(t);
                parts.appendTag(t);
            }
        }
        tag.setTag("parts", parts);

        // If the frame is not that special, return the normal stack
        boolean isSpecial = parts.tagCount() > 0;
        if (!isSpecial) {
            for (int i = 0; i < 6; i++) {
                if (frame instanceof IModifiableFrame) {
                    if (((IModifiableFrame) frame).getSideModifiers(i).length > 0) {
                        isSpecial = true;
                        break;
                    }
                }
                if (frame.isSideBlocked(i) || frame.isSideHidden(i)) {
                    isSpecial = true;
                    break;
                }
            }
            if (!isSpecial) {
                // This is a normal frame, don't even bother adding an NBT tag
                stack.stackTagCompound = null;

                // Harvest the frame if not simulating
                if (!simulated)
                    frame.harvest();

                return stack;
            }
        }

        // Harvest the frame if not simulating
        if (!simulated)
            frame.harvest();

        // Return the stack with silky data
        return stack;
    }

    public static List<IFramePart> loadSilkyData(World world, BlockPos position, IFrame frame, ItemStack stack) {

        // Find out what the material and the modifiers are
        IFrameMaterial material = BlockFrame.getMaterial(stack);

        // Get the NBT tag from the stack
        NBTTagCompound tag = stack.stackTagCompound;

        boolean[] hidden = new boolean[6];
        boolean[] blocked = new boolean[6];
        IFrameSideModifier[][] sideModifiers = new IFrameSideModifier[6][0];
        List<IFramePart> frameParts = new ArrayList<IFramePart>();

        // If the tag isn't null, read its data
        if (tag != null) {
            // Read the frame data
            int iHidden = tag.getInteger("hidden");
            hidden = new boolean[] { (iHidden & (1 << 0)) != 0, (iHidden & (1 << 1)) != 0, (iHidden & (1 << 2)) != 0,
                    (iHidden & (1 << 3)) != 0, (iHidden & (1 << 4)) != 0, (iHidden & (1 << 5)) != 0 };
            int iBlocked = tag.getInteger("blocked");
            blocked = new boolean[] { (iBlocked & (1 << 0)) != 0, (iBlocked & (1 << 1)) != 0, (iBlocked & (1 << 2)) != 0,
                    (iBlocked & (1 << 3)) != 0, (iBlocked & (1 << 4)) != 0, (iBlocked & (1 << 5)) != 0 };

            // Read the side modifiers
            for (int i = 0; i < 6; i++) {
                NBTTagList smodifiers = tag.getTagList("side_" + i, new NBTTagCompound().getId());
                for (int j = 0; j < smodifiers.tagCount(); j++) {
                    NBTTagCompound t = smodifiers.getCompoundTagAt(j);
                    sideModifiers[i] = addElement(sideModifiers[i], ModifierRegistry.instance.findFrameSideModifier(t.getString("type")));
                }
            }

            // Read the parts
            NBTTagList parts = tag.getTagList("parts", new NBTTagCompound().getId());
            for (int j = 0; j < parts.tagCount(); j++) {
                NBTTagCompound t = parts.getCompoundTagAt(j);
                String type = t.getString("type");
                IFramePart part = null;
                for (IFramePartHandler handler : ModifierRegistry.instance.framePartHandlers) {
                    part = handler.createPart(type, world != null ? world.isRemote : false);
                    if (part != null)
                        break;
                }
                part.readPickedFromNBT(t);
                frameParts.add(part);
            }
        }

        // Load the settings into the temporary frame
        placementFrame.loadSettings(material, sideModifiers, hidden, blocked);

        // Copy the data over
        if (frame != null && frame != placementFrame)
            frame.cloneFrame(placementFrame);

        // Return the frame parts, which aren't included with the frame itself
        return frameParts;
    }

    public static boolean placeFrame(World world, BlockPos position, ItemStack stack) {

        // If there's a frame there, don't place another one
        if (MovementRegistry.instance.getFrameAt(world, position) != null)
            return false;

        // Find out what the material and the modifiers are
        IFrameMaterial material = BlockFrame.getMaterial(stack);

        // Get the NBT tag from the stack
        NBTTagCompound tag = stack.stackTagCompound;

        boolean[] hidden = new boolean[6];
        boolean[] blocked = new boolean[6];
        IFrameSideModifier[][] sideModifiers = new IFrameSideModifier[6][0];
        List<IFramePart> frameParts = new ArrayList<IFramePart>();

        // If the tag isn't null, read its data
        if (tag != null) {
            // Read the frame data
            int iHidden = tag.getInteger("hidden");
            hidden = new boolean[] { (iHidden & (1 << 0)) != 0, (iHidden & (1 << 1)) != 0, (iHidden & (1 << 2)) != 0,
                    (iHidden & (1 << 3)) != 0, (iHidden & (1 << 4)) != 0, (iHidden & (1 << 5)) != 0 };
            int iBlocked = tag.getInteger("blocked");
            blocked = new boolean[] { (iBlocked & (1 << 0)) != 0, (iBlocked & (1 << 1)) != 0, (iBlocked & (1 << 2)) != 0,
                    (iBlocked & (1 << 3)) != 0, (iBlocked & (1 << 4)) != 0, (iBlocked & (1 << 5)) != 0 };

            // Read the side modifiers
            for (int i = 0; i < 6; i++) {
                NBTTagList smodifiers = tag.getTagList("side_" + i, new NBTTagCompound().getId());
                for (int j = 0; j < smodifiers.tagCount(); j++) {
                    NBTTagCompound t = smodifiers.getCompoundTagAt(j);

                    sideModifiers[i] = addElement(sideModifiers[i], ModifierRegistry.instance.findFrameSideModifier(t.getString("type")));
                }
            }

            // Read the parts
            NBTTagList parts = tag.getTagList("parts", new NBTTagCompound().getId());
            for (int j = 0; j < parts.tagCount(); j++) {
                NBTTagCompound t = parts.getCompoundTagAt(j);
                String type = t.getString("type");
                IFramePart part = null;
                for (IFramePartHandler handler : ModifierRegistry.instance.framePartHandlers) {
                    part = handler.createPart(type, world.isRemote);
                    if (part != null)
                        break;
                }
                part.readPickedFromNBT(t);
                frameParts.add(part);
            }
        }

        // Load the settings into the frame
        placementFrame.loadSettings(material, sideModifiers, hidden, blocked);

        // If the frame can't be placed here, return false
        boolean canPlace = false;
        for (IFramePlacementHandler handler : CompatRegistryImpl.framePlacementHandlers) {
            if (handler.canPlaceFrame(world, position, placementFrame)) {
                canPlace = true;
                break;
            }
        }
        if (!canPlace)
            return false;

        // If any of the frame parts can't be placed here, return false
        for (IFramePart part : frameParts)
            if (!part.canPlace(world, position))
                return false;

        // Place the parts. This will determine how the frame gets placed
        if (!world.isRemote)
            for (IFramePart part : frameParts)
                part.place(world, position);

        // Attempt to place the frame
        for (IFramePlacementHandler handler : CompatRegistryImpl.framePlacementHandlers) {
            if (handler.placeFrame(world, position, placementFrame)) {
                if (!world.isRemote)
                    NetworkHandler.instance().sendToAllAround(new PacketPlayFramePlaceSound(position.x, position.y, position.z), world);
                return true;
            }
        }

        return false;
    }

    public static final PlacementFrame placementFrame = new PlacementFrame();

    private static class PlacementFrame implements IModifiableFrame {

        private IFrameMaterial material;
        private IFrameSideModifier[][] sideModifiers;

        private boolean[] hidden;
        private boolean[] blocked;

        public void loadSettings(IFrameMaterial material, IFrameSideModifier[][] sideModifiers, boolean[] hidden, boolean[] blocked) {

            this.material = material;
            this.sideModifiers = sideModifiers;

            this.hidden = hidden;
            this.blocked = blocked;
        }

        @Override
        public World getWorld() {

            return null;
        }

        @Override
        public int getX() {

            return 0;
        }

        @Override
        public int getY() {

            return 0;
        }

        @Override
        public int getZ() {

            return 0;
        }

        @Override
        public IFrameMaterial getMaterial() {

            return material;
        }

        @Override
        public IFrameSideModifier[] getSideModifiers(int side) {

            if (sideModifiers == null)
                return new IFrameSideModifier[0];
            return sideModifiers[side];
        }

        @Override
        public void addSideModifier(int side, IFrameSideModifier modifier) {

        }

        @Override
        public void removeSideModifier(int side, IFrameSideModifier modifier) {

        }

        @Override
        public int getMultipartCount() {

            return 0;
        }

        @Override
        public boolean canHaveCovers() {

            return true;
        }

        @Override
        public boolean hasPanel(int side) {

            return false;
        }

        @Override
        public boolean shouldRenderCross(int side) {

            return true;
        }

        @Override
        public boolean isSideBlocked(int side) {

            return blocked[side];
        }

        @Override
        public boolean isSideHidden(int side) {

            return hidden[side];
        }

        @Override
        public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

            return false;
        }

        @Override
        public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

            return false;
        }

        @Override
        public boolean canBeOverriden(World world, BlockPos position) {

            return false;
        }

        @Override
        public void notifyChange() {

        }

        @Override
        public void sendUpdate() {

        }

        @Override
        public void cloneFrame(IFrame frame) {

        }

        @Override
        public void harvest() {

        }

        @Override
        public void writeFrame(NBTTagCompound tag) {

        }

        @Override
        public void readFrame(NBTTagCompound tag) {

        }

        @Override
        public void update() {

        }

        @Override
        public void onNeighborChanged() {

        }

        @Override
        public boolean onActivated(EntityPlayer player, int side, Vector3 hit) {

            return false;
        }

    }
}
