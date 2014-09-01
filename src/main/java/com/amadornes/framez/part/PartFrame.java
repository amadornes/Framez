package com.amadornes.framez.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import scala.actors.threadpool.Arrays;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.EntityDigIconFX;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.HollowMicroblock;
import codechicken.multipart.INeighborTileChange;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TNormalOcclusion;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.ModifierRegistry;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.client.IconProvider;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.util.SorterModifier;
import com.amadornes.framez.util.Utils;

public class PartFrame extends TMultiPart implements TNormalOcclusion, INeighborTileChange, IFrame {

    private static final Cuboid6[] subParts = new Cuboid6[] { null, null, null, null, null, null };

    private static int renderType = 0;

    private static PartFrame rendering;

    private static boolean[] renderFace = new boolean[6];

    private static Block blockFake = new BlockStone() {

        @Override
        public IIcon getIcon(IBlockAccess w, int x, int y, int z, int side) {

            return getIcon(side, 0);
        }

        @Override
        public IIcon getIcon(int side, int meta) {

            ForgeDirection face = ForgeDirection.getOrientation(side);

            IIcon tex = null;

            switch (renderType) {
            case 0:
            case 1:
                if (renderType == 1 && (face == ForgeDirection.UP || face == ForgeDirection.DOWN))
                    return IconProvider.iconNothing;
                tex = ModifierRegistry.INST.getBorderTexture(rendering.modifiers, face);
                if (tex == null)
                    tex = IconProvider.iconFrameBorder;
                return tex;
            case 2:
                tex = ModifierRegistry.INST.getCrossTexture(rendering.modifiers, face);
                if (tex == null)
                    tex = IconProvider.iconFrameCross;
                return tex;
            }
            return null;
        }

        @Override
        public boolean shouldSideBeRendered(IBlockAccess w, int x, int y, int z, int side) {

            return renderFace[side];
        };
    };

    private static RenderBlocks rb = new RenderBlocks();

    private Object[] connections = new Object[] { null, null, null, null, null, null };

    private List<IFrameModifier> modifiers = new ArrayList<IFrameModifier>();

    private boolean[] render = new boolean[20];

    private boolean isConnected(ForgeDirection dir) {

        return getConnection(dir) != null && getConnection(dir) instanceof PartFrame;
    }

    private Object getConnection(ForgeDirection dir) {

        return connections[dir.ordinal()];
    }

    private boolean shouldRenderCorner(ForgeDirection face, ForgeDirection a, ForgeDirection b) {

        Object of = face != ForgeDirection.UNKNOWN ? getConnection(face) : null;
        Object oa = getConnection(a);
        Object ob = getConnection(b);

        if (face == ForgeDirection.UNKNOWN) {
            return !(oa != null || ob != null) || ((oa == null || !(oa instanceof PartFrame)) && (ob == null || !(ob instanceof PartFrame)));
        } else {
            if (oa != null && ob != null && oa instanceof PartFrame && ob instanceof PartFrame && ((PartFrame) oa).isConnected(b))
                return false;

            if (oa != null && of != null && oa instanceof PartFrame && of instanceof PartFrame && ((PartFrame) oa).isConnected(face))
                return false;

            if (ob != null && of != null && ob instanceof PartFrame && of instanceof PartFrame && ((PartFrame) ob).isConnected(face))
                return false;
        }

        return true;
    }

    public PartFrame() {

    }

    @Override
    public String getType() {

        return References.FRAME_NAME;
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {

        List<Cuboid6> boxes = new ArrayList<Cuboid6>();

        return boxes;
    }

    @Override
    public boolean occlusionTest(TMultiPart npart) {

        if (npart instanceof PartFrame)
            return false;

        return true;
    }

    @Override
    public boolean doesTick() {

        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {

        return Arrays.asList(new Cuboid6[] { new Cuboid6(0, 0, 0, 1, 1, 1) });
    }

    @Override
    public Iterable<IndexedCuboid6> getSubParts() {

        List<IndexedCuboid6> boxes = new ArrayList<IndexedCuboid6>();

        double translation = 0.0001;
        double t = 0.001;

        subParts[0] = new Cuboid6(0, 0 + t, 0, 1, translation + t, 1); // DOWN (-Y)
        subParts[1] = new Cuboid6(0, 1 - translation - t, 0, 1, 1 - t, 1); // UP (+Y)
        subParts[2] = new Cuboid6(0, 0, 0 + t, 1, 1, translation + t); // WEST (-Z)
        subParts[3] = new Cuboid6(0, 0, 1 - translation - t, 1, 1, 1 - t); // EAST (+Z)
        subParts[4] = new Cuboid6(0 + t, 0, 0, translation + t, 1, 1); // NORTH (-X)
        subParts[5] = new Cuboid6(1 - translation - t, 0, 0, 1 - t, 1, 1); // SOUTH (+X)

        if (tile() != null) {
            List<TMultiPart> parts = tile().jPartList();
            for (int i = 0; i < 6; i++) {
                IndexedCuboid6 c = new IndexedCuboid6(0, subParts[i]);
                boolean skip = false;
                for (TMultiPart p : parts) {
                    if (p instanceof HollowMicroblock && ((HollowMicroblock) p).getSlot() == i) {
                        skip = true;
                        break;
                    }
                }
                if (skip)
                    continue;
                boxes.add(c);
            }
        }

        return boxes;
    }

    private void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz, boolean up, boolean down, boolean east, boolean west,
            boolean south, boolean north) {

        double sep = 0.001;

        rb.setRenderFromInside(false);
        rb.setRenderBounds(minx / 16D + (minx == 0 ? sep : 0), miny / 16D + (miny == 0 ? sep : 0), minz / 16D + (minz == 0 ? sep : 0), maxx / 16D
                - (maxx == 16 ? sep : 0), maxy / 16D - (maxy == 16 ? sep : 0), maxz / 16D - (maxz == 16 ? sep : 0));

        renderFace[ForgeDirection.UP.ordinal()] = up;
        renderFace[ForgeDirection.DOWN.ordinal()] = down;
        renderFace[ForgeDirection.NORTH.ordinal()] = north;
        renderFace[ForgeDirection.SOUTH.ordinal()] = south;
        renderFace[ForgeDirection.EAST.ordinal()] = east;
        renderFace[ForgeDirection.WEST.ordinal()] = west;

        rb.renderStandardBlock(blockFake, x(), y(), z());
    }

    @Override
    public boolean renderStatic(Vector3 pos, int pass) {

        if (pass != 0)
            return false;

        rendering = this;

        rb.blockAccess = Minecraft.getMinecraft().theWorld;

        // Border
        {
            renderType = 0;

            boolean renderDown = connections[ForgeDirection.DOWN.ordinal()] == null;
            boolean renderUp = connections[ForgeDirection.UP.ordinal()] == null;
            boolean renderEast = connections[ForgeDirection.EAST.ordinal()] == null;
            boolean renderWest = connections[ForgeDirection.WEST.ordinal()] == null;
            boolean renderSouth = connections[ForgeDirection.SOUTH.ordinal()] == null;
            boolean renderNorth = connections[ForgeDirection.NORTH.ordinal()] == null;

            // Bottom
            {
                // North-west
                if (render[0])
                    renderBox(0, 0, 0, 1, 1, 1, !render[16], renderDown, !render[1], renderWest, !render[7], renderNorth);
                // North
                if (render[1])
                    renderBox(1, 0, 0, 15, 1, 1, true, renderDown, false, false, true, renderNorth);
                // North-east
                if (render[2])
                    renderBox(15, 0, 0, 16, 1, 1, !render[17], renderDown, renderEast, !render[1], !render[3], renderNorth);
                // East
                if (render[3])
                    renderBox(15, 0, 1, 16, 1, 15, true, renderDown, renderEast, true, false, false);
                // South-east
                if (render[4])
                    renderBox(15, 0, 15, 16, 1, 16, !render[18], renderDown, renderEast, !render[5], renderSouth, !render[7]);
                // South
                if (render[5])
                    renderBox(1, 0, 15, 15, 1, 16, true, renderDown, false, false, renderSouth, true);
                // South-west
                if (render[6])
                    renderBox(0, 0, 15, 1, 1, 16, !render[19], renderDown, !render[5], renderWest, renderSouth, !render[7]);
                // West
                if (render[7])
                    renderBox(0, 0, 1, 1, 1, 15, true, renderDown, true, renderWest, false, false);
            }

            // Top
            {
                // North-west
                if (render[8])
                    renderBox(0, 15, 0, 1, 16, 1, renderUp, !render[16], !render[9], renderWest, !render[15], renderNorth);
                // North
                if (render[9])
                    renderBox(1, 15, 0, 15, 16, 1, renderUp, true, false, false, true, renderNorth);
                // North-east
                if (render[10])
                    renderBox(15, 15, 0, 16, 16, 1, renderUp, !render[17], renderEast, !render[9], !render[11], renderNorth);
                // East
                if (render[11])
                    renderBox(15, 15, 1, 16, 16, 15, renderUp, true, renderEast, true, false, false);
                // South-east
                if (render[12])
                    renderBox(15, 15, 15, 16, 16, 16, renderUp, !render[18], renderEast, !render[13], renderSouth, !render[15]);
                // South
                if (render[13])
                    renderBox(1, 15, 15, 15, 16, 16, renderUp, true, false, false, renderSouth, true);
                // South-west
                if (render[14])
                    renderBox(0, 15, 15, 1, 16, 16, renderUp, !render[19], !render[13], renderWest, renderSouth, !render[15]);
                // West
                if (render[15])
                    renderBox(0, 15, 1, 1, 16, 15, renderUp, true, true, renderWest, false, false);
            }

            // North-west
            if (render[16])
                renderBox(0, 1, 0, 1, 15, 1, false, false, true, true, true, true);

            // North-east
            if (render[17])
                renderBox(15, 1, 0, 16, 15, 1, false, false, true, true, true, true);

            // South-east
            if (render[18])
                renderBox(15, 1, 15, 16, 15, 16, false, false, true, true, true, true);

            // South-west
            if (render[19])
                renderBox(0, 1, 15, 1, 15, 16, false, false, true, true, true, true);
        }

        // Crosses
        if (true) {
            renderType = 2;

            double sep = 1 / 32D;

            rb.setRenderFromInside(false);

            Arrays.fill(renderFace, false);
            renderFace[ForgeDirection.UP.ordinal()] = true;
            renderFace[ForgeDirection.DOWN.ordinal()] = true;

            rb.setRenderBounds(0, 1 - sep, 0, 1, 1 - sep, 1);
            if (getConnection(ForgeDirection.UP) == null) {
                rb.renderStandardBlock(blockFake, x(), y(), z());
            }
            rb.setRenderBounds(0, 0 + sep, 0, 1, 0 + sep, 1);
            if (getConnection(ForgeDirection.DOWN) == null) {
                rb.renderStandardBlock(blockFake, x(), y(), z());
            }

            Arrays.fill(renderFace, false);
            renderFace[ForgeDirection.EAST.ordinal()] = true;
            renderFace[ForgeDirection.WEST.ordinal()] = true;

            rb.setRenderBounds(1 - sep, 0, 0, 1 - sep, 1, 1);
            if (getConnection(ForgeDirection.EAST) == null) {
                rb.renderStandardBlock(blockFake, x(), y(), z());
            }
            rb.setRenderBounds(0 + sep, 0, 0, 0 + sep, 1, 1);
            if (getConnection(ForgeDirection.WEST) == null) {
                rb.renderStandardBlock(blockFake, x(), y(), z());
            }

            Arrays.fill(renderFace, false);
            renderFace[ForgeDirection.SOUTH.ordinal()] = true;
            renderFace[ForgeDirection.NORTH.ordinal()] = true;

            rb.setRenderBounds(0, 0, 1 - sep, 1, 1, 1 - sep);
            if (getConnection(ForgeDirection.SOUTH) == null) {
                rb.renderStandardBlock(blockFake, x(), y(), z());
            }
            rb.setRenderBounds(0, 0, 0 + sep, 1, 1, 0 + sep);
            if (getConnection(ForgeDirection.NORTH) == null) {
                rb.renderStandardBlock(blockFake, x(), y(), z());
            }
        }

        return true;
    }

    @Override
    public void addDestroyEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {

        IIcon icon = Blocks.planks.getIcon(0, 0);
        EntityDigIconFX.addBlockDestroyEffects(world(), Cuboid6.full.copy().add(Vector3.fromTileEntity(tile())), new IIcon[] { icon, icon, icon,
                icon, icon, icon }, effectRenderer);

    }

    @Override
    public void addHitEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {

        IIcon icon = Blocks.planks.getIcon(0, 0);
        EntityDigIconFX.addBlockHitEffects(world(), Cuboid6.full.copy().add(Vector3.fromTileEntity(tile())), hit.sideHit, icon, effectRenderer);
    }

    @Override
    public void drawBreaking(RenderBlocks renderBlocks) {

        RenderBlocks rb2 = rb;
        rb = renderBlocks;
        renderStatic(new Vector3(), 0);
        rb = rb2;
    }

    @Override
    public boolean drawHighlight(MovingObjectPosition hit, EntityPlayer player, float frame) {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0, 0, 0, 0.4F);
        GL11.glLineWidth(2);
        GL11.glDepthMask(true);
        GL11.glPushMatrix();
        RenderUtils.translateToWorldCoords(player, frame);
        GL11.glTranslated(x(), y(), z());

        RenderUtils.drawCuboidOutline(new Cuboid6(0, 0, 0, 1, 1, 1).expand(0.001));

        GL11.glPopMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        return true;
    }

    @Override
    public ExtendedMOP collisionRayTrace(Vec3 start, Vec3 end) {

        ExtendedMOP mop = super.collisionRayTrace(start, end);

        if (mop != null) {
            Vec3 click = mop.hitVec.addVector(-mop.blockX, -mop.blockY, -mop.blockZ);
            ForgeDirection face = ForgeDirection.getOrientation(mop.sideHit);
            if (face == ForgeDirection.EAST || face == ForgeDirection.WEST) {
                click.yCoord = 0.5;
                click.zCoord = 0.5;
                click.xCoord = (click.xCoord * 0.5) + 0.25;
            }
            if (face == ForgeDirection.UP || face == ForgeDirection.DOWN) {
                click.xCoord = 0.5;
                click.zCoord = 0.5;
                click.yCoord = (click.yCoord * 0.5) + 0.25;
            }
            if (face == ForgeDirection.NORTH || face == ForgeDirection.SOUTH) {
                click.xCoord = 0.5;
                click.yCoord = 0.5;
                click.zCoord = (click.zCoord * 0.5) + 0.25;
            }

            mop.hitVec = click.addVector(mop.blockX, mop.blockY, mop.blockZ);
        }

        return mop;
    }

    private ItemStack getItem() {

        List<String> modifiers = new ArrayList<String>();

        for (IFrameModifier m : this.modifiers)
            modifiers.add(m.getIdentifier());

        return FramezApi.inst().getModifierRegistry().getFrameStack(modifiers.toArray(new String[0]));
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit) {

        return getItem();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<ItemStack> getDrops() {

        onUpdate(1);

        return Arrays.asList(new ItemStack[] { getItem() });
    }

    @Override
    public void save(NBTTagCompound tag) {

        super.save(tag);

        writeModifiersToNBT(tag);
    }

    @Override
    public void load(NBTTagCompound tag) {

        super.load(tag);

        try {
            readModifiersFromNBT(tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void writeDesc(MCDataOutput packet) {

        super.writeDesc(packet);

        NBTTagCompound tag = new NBTTagCompound();
        writeModifiersToNBT(tag);
        packet.writeNBTTagCompound(tag);
    }

    @Override
    public void readDesc(MCDataInput packet) {

        super.readDesc(packet);

        NBTTagCompound tag = packet.readNBTTagCompound();
        try {
            readModifiersFromNBT(tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public float getStrength(MovingObjectPosition hit, EntityPlayer player) {

        ItemStack item = player.getCurrentEquippedItem();
        if (item != null)
            return item.getItem().getDigSpeed(item, Blocks.planks, 0);
        return 1;
    }

    @Override
    public void onAdded() {

        super.onAdded();

        onUpdate(0);
    }

    @Override
    public void onWorldJoin() {

        super.onWorldJoin();

        onUpdate(0);
    }

    @Override
    public void onRemoved() {

        super.onRemoved();

        onUpdate(1);
    }

    @Override
    public void onNeighborChanged() {

        super.onNeighborChanged();

        onUpdate(2);
    }

    @Override
    public void onPartChanged(TMultiPart part) {

        super.onPartChanged(part);

        onUpdate(3);
    }

    @Override
    public boolean weakTileChanges() {

        return false;
    }

    @Override
    public void onNeighborTileChanged(int arg0, boolean arg1) {

        onUpdate(0);
    }

    private void onUpdate(int mode) {

        for (int i = 0; i < 6; i++) {
            ForgeDirection face = ForgeDirection.getOrientation(i);
            if (mode != 1)
                connections[i] = canConnect(this, face);

            if (mode != 2)
                if (connections[i] != null && connections[i] instanceof PartFrame) {
                    ((PartFrame) connections[i]).onUpdate(2);
                    for (Object o : ((PartFrame) connections[i]).connections) {
                        if (o != null && o instanceof PartFrame) {
                            ((PartFrame) o).onUpdate(2);
                        }
                    }
                }
        }

        updateRenderer();
    }

    private void updateRenderer() {

        render[0] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.WEST));
        render[1] = !hasModifier("connected")
                || (hasModifier("connected") && !isConnected(ForgeDirection.NORTH) && !isConnected(ForgeDirection.DOWN));
        render[2] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.EAST));
        render[3] = !hasModifier("connected") || (hasModifier("connected") && !isConnected(ForgeDirection.EAST) && !isConnected(ForgeDirection.DOWN));
        render[4] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.DOWN, ForgeDirection.SOUTH, ForgeDirection.EAST));
        render[5] = !hasModifier("connected")
                || (hasModifier("connected") && !isConnected(ForgeDirection.SOUTH) && !isConnected(ForgeDirection.DOWN));
        render[6] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.DOWN, ForgeDirection.SOUTH, ForgeDirection.WEST));
        render[7] = !hasModifier("connected") || (hasModifier("connected") && !isConnected(ForgeDirection.WEST) && !isConnected(ForgeDirection.DOWN));
        render[8] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.WEST));
        render[9] = !hasModifier("connected") || (hasModifier("connected") && !isConnected(ForgeDirection.NORTH) && !isConnected(ForgeDirection.UP));
        render[10] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.EAST));
        render[11] = !hasModifier("connected") || (hasModifier("connected") && !isConnected(ForgeDirection.EAST) && !isConnected(ForgeDirection.UP));
        render[12] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.EAST));
        render[13] = !hasModifier("connected") || (hasModifier("connected") && !isConnected(ForgeDirection.SOUTH) && !isConnected(ForgeDirection.UP));
        render[14] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.WEST));
        render[15] = !hasModifier("connected") || (hasModifier("connected") && !isConnected(ForgeDirection.WEST) && !isConnected(ForgeDirection.UP));
        render[16] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.UNKNOWN, ForgeDirection.NORTH, ForgeDirection.WEST));
        render[17] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.UNKNOWN, ForgeDirection.NORTH, ForgeDirection.EAST));
        render[18] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.UNKNOWN, ForgeDirection.SOUTH, ForgeDirection.EAST));
        render[19] = !hasModifier("connected")
                || (hasModifier("connected") && shouldRenderCorner(ForgeDirection.UNKNOWN, ForgeDirection.SOUTH, ForgeDirection.WEST));
    }

    public static Object canConnect(PartFrame frame, ForgeDirection face) {

        if (frame.world().isSideSolid(frame.x() + face.offsetX, frame.y() + face.offsetY, frame.z() + face.offsetZ, face.getOpposite()))
            return true;

        if (!Utils.occlusionTest(frame.tile(), face))
            return true;

        TileEntity te = frame.world().getTileEntity(frame.x() + face.offsetX, frame.y() + face.offsetY, frame.z() + face.offsetZ);
        if (te != null && te instanceof TileMultipart) {
            PartFrame fr = Utils.getFrame((TileMultipart) te);
            if (fr != null) {
                boolean isConnected = false;
                for (IFrameModifier m : fr.modifiers) {
                    if (m.getIdentifier().equals(References.Modifiers.CONNECTED)) {
                        isConnected = true;
                        break;
                    }
                }
                if (isConnected) {
                    Collections.sort(frame.modifiers, new SorterModifier());
                    Collections.sort(fr.modifiers, new SorterModifier());
                    if (frame.modifiers.size() == fr.modifiers.size()) {
                        for (int i = 0; i < frame.modifiers.size(); i++) {
                            if (!frame.modifiers.get(i).getIdentifier().equals(fr.modifiers.get(i).getIdentifier())) {
                                isConnected = false;
                                break;
                            }
                        }
                    } else {
                        isConnected = false;
                    }
                }
                if (isConnected && Utils.occlusionTest((TileMultipart) te, face.getOpposite()))
                    return fr;
            }

            if (!Utils.occlusionTest((TileMultipart) te, face.getOpposite()))
                return true;
        }

        return null;
    }

    @Override
    public IFrameModifier[] getModifiers() {

        return modifiers.toArray(new IFrameModifier[0]);
    }

    @Override
    public void addModifier(IFrameModifier modifier) {

        if (modifier == null)
            return;
        for (IFrameModifier m : getModifiers())
            if (m.getIdentifier().equals(modifier.getIdentifier()))
                return;

        modifiers.add(modifier);
    }

    @Override
    public void addModifiers(IFrameModifier... modifiers) {

        for (IFrameModifier m : modifiers)
            addModifier(m);
    }

    @Override
    public void removeModifier(String modifier) {

        IFrameModifier mod = null;

        for (IFrameModifier m : getModifiers())
            if (m.getIdentifier().equals(modifier)) {
                mod = m;
                break;
            }

        if (mod != null)
            modifiers.remove(mod);
    }

    @Override
    public boolean hasModifier(String modifier) {

        for (IFrameModifier m : getModifiers())
            if (m.getIdentifier().equals(modifier))
                return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readModifiersFromNBT(NBTTagCompound tag) {

        List<IFrameModifier> unedited = new ArrayList<IFrameModifier>();
        unedited.addAll(Arrays.asList(getConnections()));

        NBTTagList list = tag.getTagList("modifiers", 10);// List of tag compounds

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound t = list.getCompoundTagAt(i);
            String type = t.getString("__type");
            IFrameModifier modifier = null;
            for (IFrameModifier m : unedited) {
                if (m == null)
                    continue;
                if (m.getIdentifier().equals(type)) {
                    modifier = m;
                    break;
                }
            }

            if (modifier == null) {
                for (IFrameModifierProvider p : FramezApi.inst().getModifierRegistry().getProviders()) {
                    if (p.getIdentifier().equals(type)) {
                        modifier = p.instantiate(this);
                        break;
                    }
                }
                addModifier(modifier);
            } else {
                unedited.remove(modifier);
            }

            modifier.readFromNBT(t);
        }

        modifiers.removeAll(unedited);
        unedited.clear();
    }

    @Override
    public void writeModifiersToNBT(NBTTagCompound tag) {

        NBTTagList list = new NBTTagList();

        for (IFrameModifier m : getModifiers()) {
            NBTTagCompound t = new NBTTagCompound();
            m.writeToNBT(t);
            t.setString("__type", m.getIdentifier());
            list.appendTag(t);
        }

        tag.setTag("modifiers", list);
    }

    @Override
    public Object[] getConnections() {

        return connections;
    }
}
