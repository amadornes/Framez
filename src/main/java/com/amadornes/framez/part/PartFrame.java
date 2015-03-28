package com.amadornes.framez.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.CommonMicroblock;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.HollowMicroblock;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TNormalOcclusion;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.client.RenderFrame;
import com.amadornes.framez.config.Config;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.FrameModifierRegistry;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.util.FramezUtils;

public class PartFrame extends TMultiPart implements TNormalOcclusion, IFrame {

    private List<IFrameModifier> modifiers;
    private List<IFrameSideModifier>[] sideModifiers;
    private boolean[] hidden = new boolean[6];

    public PartFrame() {

    }

    @Override
    public String getType() {

        return FrameFactory.getIdentifier(References.FRAME_PART_ID, getModifiers());
    }

    @Override
    public World getWorld() {

        if (tile() == null)
            return null;

        return world();
    }

    @Override
    public int getX() {

        if (tile() == null)
            return 0;

        return x();
    }

    @Override
    public int getY() {

        if (tile() == null)
            return 0;

        return y();
    }

    @Override
    public int getZ() {

        if (tile() == null)
            return 0;

        return z();
    }

    @Override
    public boolean doesTick() {

        return false;
    }

    public ItemStack getItem() {

        NBTTagList tagList = new NBTTagList();
        for (IFrameModifier mod : getModifiers())
            tagList.appendTag(new NBTTagString(mod.getType()));

        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("modifiers", tagList);

        ItemStack is = new ItemStack(FramezItems.frame);
        is.setTagCompound(tag);

        return is;
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit) {

        return getItem();
    }

    @Override
    public Iterable<ItemStack> getDrops() {

        return Arrays.asList(getItem());
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {

        return Arrays.asList(new Cuboid6(0, 0, 0, 1, 1, 1));
    }

    @Override
    public Iterable<IndexedCuboid6> getSubParts() {

        List<IndexedCuboid6> l = new ArrayList<IndexedCuboid6>();

        Vec3dCube c1 = new Vec3dCube(0, 0, 0, 2 / 16D, 2 / 16D, 1);
        Vec3dCube c2 = new Vec3dCube(0, 2 / 16D, 0, 2 / 16D, 14 / 16D, 2 / 16D);
        Vec3dCube c3 = new Vec3dCube(0, 14 / 16D, 0, 2 / 16D, 1, 1);
        for (int i = 0; i < 4; i++) {
            l.add(new IndexedCuboid6(0, new Cuboid6(c1.clone().rotate(0, i * 90, 0, Vec3d.center).toAABB())));
            l.add(new IndexedCuboid6(0, new Cuboid6(c2.clone().rotate(0, i * 90, 0, Vec3d.center).toAABB())));
            l.add(new IndexedCuboid6(0, new Cuboid6(c3.clone().rotate(0, i * 90, 0, Vec3d.center).toAABB())));
        }
        if (getWorld() != null && (!getWorld().isRemote || !Config.click_through_frames))
            if (is2D())
                l.add(new IndexedCuboid6(0, new Cuboid6(0, 0, 0, 1, 1, 1).expand(-0.001)));
            else
                l.add(new IndexedCuboid6(0, new Cuboid6(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D)));

        return l;
    }

    @Override
    public ExtendedMOP collisionRayTrace(Vec3 start, Vec3 end) {

        ExtendedMOP mop = super.collisionRayTrace(start, end);

        double d = 0.001;

        if (mop != null) {
            if (mop.hitVec.xCoord % 1 == 0)
                mop.hitVec.xCoord += ForgeDirection.getOrientation(mop.sideHit).offsetX * d;
            if (mop.hitVec.yCoord % 1 == 0)
                mop.hitVec.yCoord += ForgeDirection.getOrientation(mop.sideHit).offsetY * d;
            if (mop.hitVec.zCoord % 1 == 0)
                mop.hitVec.zCoord += ForgeDirection.getOrientation(mop.sideHit).offsetZ * d;

            // mop.blockX = (int) Math.floor(mop.hitVec.xCoord);
            // mop.blockY = (int) Math.floor(mop.hitVec.yCoord);
            // mop.blockZ = (int) Math.floor(mop.hitVec.zCoord);
        }

        return mop;
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {

        return new ArrayList<Cuboid6>();
    }

    @Override
    public boolean occlusionTest(TMultiPart part) {

        if (part instanceof CommonMicroblock) {
            if (!(part instanceof FaceMicroblock) && !(part instanceof HollowMicroblock))
                return false;
            if (((CommonMicroblock) part).getSize() > 2)
                return false;
        }

        return !(part instanceof PartFrame) && super.occlusionTest(part);
    }

    @Override
    public Collection<IFrameModifier> getModifiers() {

        if (modifiers == null)
            return modifiers = new ArrayList<IFrameModifier>();

        return modifiers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<IFrameSideModifier> getSideModifiers(ForgeDirection side) {

        if (sideModifiers == null) {
            sideModifiers = new ArrayList[6];
            for (int i = 0; i < 6; i++)
                sideModifiers[i] = new ArrayList<IFrameSideModifier>();
        }

        return sideModifiers[side.ordinal()];
    }

    @Override
    public boolean addSideModifier(ForgeDirection side, String modifier) {

        IFrameModifier mod = FrameModifierRegistry.instance().findModifier(modifier);
        if (mod == null)
            return false;
        if (!(mod instanceof IFrameSideModifier))
            return false;

        Collection<IFrameSideModifier> l = getSideModifiers(side);
        if (FramezUtils.hasModifier(l, mod))
            return false;

        boolean result = l.add((IFrameSideModifier) mod);
        sendDescUpdate();
        tile().markDirty();
        return result;
    }

    @Override
    public boolean removeSideModifier(ForgeDirection side, String modifier) {

        IFrameModifier mod = FrameModifierRegistry.instance().findModifier(modifier);
        if (mod == null)
            return false;
        if (!(mod instanceof IFrameSideModifier))
            return false;

        Collection<IFrameSideModifier> l = getSideModifiers(side);
        if (!FramezUtils.hasModifier(l, mod))
            return false;

        boolean result = l.remove(mod);
        sendDescUpdate();
        tile().markDirty();
        return result;
    }

    @Override
    public int getMaxMovedBlocks() {

        return 6;
    }

    @Override
    public int getMaxMultiparts() {

        return 2;
    }

    @Override
    public int getMultiparts() {

        return tile().jPartList().size() - 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {

        return is2D() ? pass == 0 || Config.click_through_frames : true;
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
        {
            double d = 0.002;

            if (!is2D()) {
                RenderUtils.drawCuboidOutline(new Cuboid6(2 / 16D + d, isSideHidden(ForgeDirection.DOWN) ? d * 2 : -d * 2, 2 / 16D + d,
                        14 / 16D - d, 1 + (isSideHidden(ForgeDirection.UP) ? -d * 2 : d * 2), 14 / 16D - d));
                RenderUtils.drawCuboidOutline(new Cuboid6(isSideHidden(ForgeDirection.WEST) ? d * 2 : -d * 2, 2 / 16D + d, 2 / 16D + d,
                        1 + (isSideHidden(ForgeDirection.EAST) ? -d * 2 : d * 2), 14 / 16D - d, 14 / 16D - d));
                RenderUtils.drawCuboidOutline(new Cuboid6(2 / 16D + d, 2 / 16D + d, isSideHidden(ForgeDirection.NORTH) ? d : -d,
                        14 / 16D - d, 14 / 16D - d, 1 + (isSideHidden(ForgeDirection.SOUTH) ? -d * 2 : d * 2)));
            }

            RenderUtils.drawCuboidOutline(new Cuboid6(0, 0, 0, 1, 1, 1).expand(d));
        }
        GL11.glPopMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        return true;
    }

    @Override
    public boolean renderStatic(RenderHelper renderer, int pass) {

        IIcon borderT = getBorderIcon();
        IIcon borderPanelT = getBorderPanelIcon();
        IIcon crossT = getCrossIcon();
        IIcon simpleT = getSimpleIcon();

        IIcon[] border = new IIcon[] { borderT, borderT, borderT, borderT, borderT, borderT };
        IIcon[] cross = new IIcon[] { crossT, crossT, crossT, crossT, crossT, crossT };
        IIcon[] simple = new IIcon[] { simpleT, simpleT, simpleT, simpleT, simpleT, simpleT };

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            int mb = getMicroblock(d);
            if (mb != 0) {
                cross[d.ordinal()] = null;
                if (mb == 2) {
                    border[d.ordinal()] = borderPanelT;
                    simple[d.ordinal()] = borderPanelT;
                } else {
                    simple[d.ordinal()] = borderT;
                }
            }
        }

        if (hidden == null)
            hidden = new boolean[6];

        boolean renderedModifier = false;
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            for (IFrameSideModifier m : getSideModifiers(d))
                renderedModifier |= m.renderStatic(this, d, renderer, pass);

        if (!is2D())
            return RenderFrame.renderFrame3D(renderer, border, borderT, cross, simple, hidden, pass) || renderedModifier;
        else
            return RenderFrame.renderFrame2D(renderer, simple, hidden) || renderedModifier;
    }

    @Override
    public boolean renderStatic(Vector3 pos, int pass) {

        if (!canRenderInPass(pass))
            return false;

        RenderHelper renderer = RenderHelper.instance;
        renderer.reset();
        renderer.setRenderCoords(getWorld(), (int) pos.x, (int) pos.y, (int) pos.z);

        boolean result = renderStatic(renderer, pass);

        renderer.fullReset();

        return result;
    }

    @Override
    public void renderDynamic(Vec3d pos, int pass, double frame) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            for (IFrameSideModifier m : getSideModifiers(d))
                m.renderDynamic(this, d, pos, pass, frame);
    }

    @Override
    public void renderDynamic(Vector3 pos, float frame, int pass) {

        renderDynamic(new Vec3d(pos.x, pos.y, pos.z), pass, frame);
    }

    @Override
    public void renderItem(ItemStack item, ItemRenderType type) {

        Tessellator t = Tessellator.instance;
        RenderHelper renderer = RenderHelper.instance;
        renderer.fullReset();

        GL11.glPushMatrix();
        {
            t.startDrawingQuads();
            if (canRenderInPass(0))
                renderStatic(renderer, 0);
            if (canRenderInPass(1))
                renderStatic(renderer, 1);
            t.draw();

            if (canRenderInPass(0))
                renderDynamic(new Vec3d(0, 0, 0), 0, Framez.proxy.getFrame());
            if (canRenderInPass(1))
                renderDynamic(new Vec3d(0, 0, 0), 1, Framez.proxy.getFrame());
        }
        GL11.glPopMatrix();

        renderer.fullReset();
    }

    @Override
    public void drawBreaking(RenderBlocks renderBlocks) {

        RenderHelper.instance.setOverrideTexture(renderBlocks.overrideBlockTexture);
        renderStatic(new Vector3(getX(), getY(), getZ()), 0);
        RenderHelper.instance.fullReset();
    }

    @Override
    public boolean isSideSticky(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        return true;
    }

    @Override
    public int getMicroblock(ForgeDirection face) {

        if (tile() != null) {
            for (TMultiPart p : tile().jPartList()) {
                if (p instanceof FaceMicroblock || p instanceof HollowMicroblock) {
                    int pos = ((CommonMicroblock) p).getShape();
                    if (pos != face.ordinal())
                        continue;
                    if (((CommonMicroblock) p).getSize() == 2)
                        return 2;
                    else
                        return 1;
                }
            }
        }

        return 0;
    }

    @Override
    public IIcon getBorderIcon() {

        return null;
    }

    @Override
    public IIcon getBorderPanelIcon() {

        return null;
    }

    @Override
    public IIcon getCrossIcon() {

        return null;
    }

    @Override
    public IIcon getSimpleIcon() {

        return null;
    }

    @Override
    public boolean is2D() {

        return Config.simple_frames;
    }

    @Override
    public boolean isSideHidden(ForgeDirection side) {

        if (hidden == null)
            hidden = new boolean[6];

        return hidden[side.ordinal()];
    }

    @Override
    public void setSideHidden(ForgeDirection side, boolean hidden) {

        if (this.hidden == null)
            this.hidden = new boolean[6];

        this.hidden[side.ordinal()] = hidden;

        sendDescUpdate();
    }

    @Override
    public void save(NBTTagCompound tag) {

        if (hidden == null)
            hidden = new boolean[6];

        super.save(tag);

        for (int i = 0; i < 6; i++) {
            tag.setBoolean("hidden_" + i, hidden[i]);
            NBTTagList l = new NBTTagList();
            for (IFrameSideModifier m : getSideModifiers(ForgeDirection.getOrientation(i)))
                l.appendTag(new NBTTagString(m.getType()));
            tag.setTag("sidemods_" + i, l);
        }
    }

    @Override
    public void load(NBTTagCompound tag) {

        if (hidden == null)
            hidden = new boolean[6];

        super.load(tag);

        for (int i = 0; i < 6; i++) {
            hidden[i] = tag.getBoolean("hidden_" + i);
            NBTTagList l = tag.getTagList("sidemods_" + i, new NBTTagString().getId());
            Collection<IFrameSideModifier> c = getSideModifiers(ForgeDirection.getOrientation(i));
            c.clear();
            for (int j = 0; j < l.tagCount(); j++)
                c.add((IFrameSideModifier) FrameModifierRegistry.instance().findModifier(l.getStringTagAt(j)));
        }
    }

    @Override
    public void writeDesc(MCDataOutput packet) {

        if (hidden == null)
            hidden = new boolean[6];

        super.writeDesc(packet);

        for (int i = 0; i < 6; i++)
            packet.writeBoolean(hidden[i]);

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            Collection<IFrameSideModifier> c = getSideModifiers(d);
            packet.writeInt(c.size());
            for (IFrameModifier m : c)
                packet.writeString(m.getType());
        }
    }

    @Override
    public void readDesc(MCDataInput packet) {

        if (hidden == null)
            hidden = new boolean[6];

        super.readDesc(packet);

        for (int i = 0; i < 6; i++)
            hidden[i] = packet.readBoolean();

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            Collection<IFrameSideModifier> c = getSideModifiers(d);
            c.clear();
            int amt = packet.readInt();
            for (int i = 0; i < amt; i++)
                c.add((IFrameSideModifier) FrameModifierRegistry.instance().findModifier(packet.readString()));
        }
    }

}
