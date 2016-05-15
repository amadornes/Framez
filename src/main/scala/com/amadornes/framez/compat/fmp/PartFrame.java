package com.amadornes.framez.compat.fmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.CommonMicroClass;
import codechicken.microblock.CommonMicroblock;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.HollowMicroblock;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.Microblock;
import codechicken.microblock.MicroblockGenerator;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.IconHitEffects;
import codechicken.multipart.JIconHitEffects;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IModifiableFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.client.render.RenderFrame;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.IFramezFrame;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PartFrame extends TMultiPart implements IFramezFrame, IRedstonePart, JIconHitEffects {

    @Override
    public String getType() {

        return FrameFactory.getIdentifier(ModInfo.MODID + ":frame", material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vector3 pos, int pass) {

        RenderHelper helper = RenderHelper.instance();

        helper.reset();
        helper.start(world(), x(), y(), z(), pass);
        boolean result = RenderFrame.renderFrame(world(), new BlockPos(x(), y(), z()), this, pass);
        helper.reset();

        return result;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean drawHighlight(MovingObjectPosition hit, EntityPlayer player, float frame) {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0, 0, 0, 0.4F);
        GL11.glLineWidth(2);
        GL11.glDepthMask(true);
        GL11.glPushMatrix();

        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * frame;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * frame;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * frame;
        GL11.glTranslated(x() - px, y() - py, z() - pz);
        {
            double d = 0.005;
            double din = 0.001;

            RenderUtils.drawCuboidOutline(new Cuboid6(2 / 16D + din, isSideHidden(0) ? d : -d, 2 / 16D + din, 14 / 16D - din,
                    1 + (isSideHidden(1) ? -d : d), 14 / 16D - din));
            RenderUtils.drawCuboidOutline(new Cuboid6(isSideHidden(4) ? d : -d, 2 / 16D + din, 2 / 16D + din,
                    1 + (isSideHidden(5) ? -d : d), 14 / 16D - din, 14 / 16D - din));
            RenderUtils.drawCuboidOutline(new Cuboid6(2 / 16D + din, 2 / 16D + din, isSideHidden(2) ? d : -d, 14 / 16D - din,
                    14 / 16D - din, 1 + (isSideHidden(3) ? -d : d)));

            RenderUtils.drawCuboidOutline(new Cuboid6(isSideHidden(4) ? d : 0, isSideHidden(0) ? d : 0, isSideHidden(2) ? d : 0,
                    1 - (isSideHidden(5) ? d : 0), 1 - (isSideHidden(1) ? d : 0), 1 - (isSideHidden(3) ? d : 0)).expand(d));
        }
        GL11.glPopMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawBreaking(RenderBlocks renderBlocks) {

        RenderHelper helper = RenderHelper.instance();
        BlockPos position = new BlockPos(x(), y(), z());

        helper.reset();
        helper.start(world(), x(), y(), z(), 0);
        helper.setOverrideTexture(renderBlocks.overrideBlockTexture);
        RenderFrame.renderFrame(world(), position, this, 0);
        RenderFrame.renderFrame(world(), position, this, 1);
        helper.setOverrideTexture(null);
        helper.reset();
    }

    @Override
    public Cuboid6 getBounds() {

        return Cuboid6.full;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getBreakingIcon(Object obj, int side) {

        return getMaterial().getTexture(null, side, 0).full();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getBrokenIcon(int side) {

        return getMaterial().getTexture(null, side, 0).full();
    }

    @Override
    public void addDestroyEffects(EffectRenderer effectRenderer) {

        IconHitEffects.addDestroyEffects(this, effectRenderer);
    }

    @Override
    public void addHitEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {

        IconHitEffects.addHitEffects(this, hit, effectRenderer);
    }

    @Override
    public Iterable<IndexedCuboid6> getSubParts() {

        List<IndexedCuboid6> l = new ArrayList<IndexedCuboid6>();

        Cuboid6 center = new Cuboid6(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D);
        if (!FramezConfig.click_through_frames)
            for (int i = 0; i < 6; i++)
                if (shouldRenderCross(i))
                    l.add(new IndexedCuboid6(i + 1, center.copy().setSide(i ^ 1, center.getSide(i))));

        l.add(new IndexedCuboid6(0, new Cuboid6(0 / 16D, 0 / 16D, 0 / 16D, 2 / 16D, 2 / 16D, 16 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(14 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 2 / 16D, 16 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(0 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 2 / 16D, 2 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(0 / 16D, 0 / 16D, 14 / 16D, 16 / 16D, 2 / 16D, 16 / 16D)));

        l.add(new IndexedCuboid6(0, new Cuboid6(0 / 16D, 14 / 16D, 0 / 16D, 2 / 16D, 16 / 16D, 16 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(14 / 16D, 14 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 16 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(0 / 16D, 14 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 2 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(0 / 16D, 14 / 16D, 14 / 16D, 16 / 16D, 16 / 16D, 16 / 16D)));

        l.add(new IndexedCuboid6(0, new Cuboid6(0 / 16D, 2 / 16D, 0 / 16D, 2 / 16D, 14 / 16D, 2 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(14 / 16D, 2 / 16D, 0 / 16D, 16 / 16D, 14 / 16D, 2 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(14 / 16D, 2 / 16D, 14 / 16D, 16 / 16D, 14 / 16D, 16 / 16D)));
        l.add(new IndexedCuboid6(0, new Cuboid6(0 / 16D, 2 / 16D, 14 / 16D, 2 / 16D, 14 / 16D, 16 / 16D)));

        return l;
    }

    @Override
    public ExtendedMOP collisionRayTrace(Vec3 start, Vec3 end) {

        ExtendedMOP mop = super.collisionRayTrace(start, end);

        if (mop != null) {
            if (((Integer) mop.data) > 0)
                mop.sideHit = ((Integer) mop.data) - 1;

            if (mop.hitVec.xCoord == mop.blockX || mop.hitVec.xCoord == mop.blockX + 1 || mop.hitVec.yCoord == mop.blockY
                    || mop.hitVec.yCoord == mop.blockY + 1 || mop.hitVec.zCoord == mop.blockZ || mop.hitVec.zCoord == mop.blockZ + 1)
                mop.dist = -1;
            else
                mop.dist *= 1.001;
        }

        return mop;
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {

        return Arrays.asList(new Cuboid6(0, 0, 0, 1, 1, 1));
    }

    @Override
    public boolean occlusionTest(TMultiPart part) {

        if (part instanceof CommonMicroblock) {
            if (!(part instanceof FaceMicroblock) && !(part instanceof HollowMicroblock))
                return false;
            if (((Microblock) part).getSize() > 2)
                return false;
            if (((Microblock) part).getIMaterial() instanceof MicroMaterialFrame)
                return false;
        }

        return !(part instanceof PartFrame) && super.occlusionTest(part);
    }

    @Override
    public Iterable<ItemStack> getDrops() {

        return Arrays.asList(pickItem(null));
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit) {

        try {
            BlockPos position = new BlockPos(x(), y(), z());
            if (Framez.proxy.getPlayer() != null && Framez.proxy.getPlayer().isSneaking()) {
                ItemStack item = Framez.proxy.getPlayer().getCurrentEquippedItem();
                if ((item != null && item.getItem() instanceof IFramezWrench && ((IFramezWrench) item.getItem()).isSilky(item))
                        || Framez.proxy.getPlayer().capabilities.isCreativeMode) {
                    return FramezUtils.silkHarvest(world(), position, true);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ItemStack(FramezBlocks.frames.get(FrameFactory.getIdentifier("frame0", material)));
    }

    @Override
    public boolean doesTick() {

        return false;
    }

    @Override
    public boolean activate(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {

        if (onActivated(player, mop.sideHit,
                new com.amadornes.trajectory.api.vec.Vector3(mop.hitVec).sub(mop.blockX, mop.blockY, mop.blockZ)))
            return true;

        ItemStack stack = player.getCurrentEquippedItem();
        IFramezWrench wrench = FramezApi.instance().getWrench(stack);

        if (wrench != null) {
            if (player.isSneaking()) {
                if (setSideHidden(mop.sideHit, !isSideHidden(mop.sideHit))) {
                    wrench.onUsed(stack, player);
                    return true;
                }
            } else {
                if (setSideBlocked(mop.sideHit, !isSideBlocked(mop.sideHit))) {
                    wrench.onUsed(stack, player);
                    return true;
                }
            }
            return false;
        }

        // BlockPos position = new BlockPos(x(), y(), z());
        // for (IFrameSideModifier mod : ModifierRegistry.instance.frameSideModifiers) {
        // if (!FramezUtils.contains(getSideModifiers(world(), position, mop.sideHit), mod)
        // && mod.canApplyTo(world(), position, this, mop.sideHit)) {
        // addSideModifier(mop.sideHit, mod);
        // if (!world().isRemote && !player.capabilities.isCreativeMode)
        // stack.stackSize--;
        // return true;
        // }
        // }

        return false;
    }

    @Override
    public float getStrength(MovingObjectPosition hit, EntityPlayer player) {

        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null)
            return 0.2F;
        return stack.getItem().getHarvestLevel(stack, "wrench") >= 0 || stack.getItem().getHarvestLevel(stack, "pickaxe") >= 0 ? 15F : 0.2F;
    }

    @Override
    public void onPartChanged(TMultiPart part) {

        super.onPartChanged(part);
        onNeighborChanged();
    }

    @Override
    public void onConverted() {

        super.onConverted();
        onNeighborChanged();
    }

    @Override
    public void onNeighborChanged() {

        super.onNeighborChanged();

        boolean updated = false;
        for (int i = 0; i < 6; i++) {
            if (blocked[i] && (canPlaceCover(i) || tile().partMap(i) != null)) {
                blocked[i] = false;
                updated = true;
            }
        }
        if (updated)
            notifyChange();
    }

    public void markRender() {

        if (world() != null)
            world().func_147479_m(x(), y(), z());
    }

    public void notifyNeighbours() {

        if (world() != null && !world().isRemote)
            world().notifyBlockChange(x(), y(), z(), tile().getBlockType());
    }

    @Override
    public void notifyChange() {

        tile().markDirty();
        markRender();
        notifyNeighbours();
        sendUpdate();
    }

    @Override
    public void sendUpdate() {

        if (world() != null && !world().isRemote)
            sendDescUpdate();
    }

    // Frame logic

    private IFrameMaterial material = ModifierRegistry.instance.findFrameMaterial("wood");
    private IFrameSideModifier[][] sideModifiers = new IFrameSideModifier[6][0];

    public boolean[] hidden = new boolean[6];
    public boolean[] blocked = new boolean[6];

    @Override
    public World getWorld() {

        return world();
    }

    @Override
    public int getX() {

        return x();
    }

    @Override
    public int getY() {

        return y();
    }

    @Override
    public int getZ() {

        return z();
    }

    @Override
    public void init(IFrameMaterial material) {

        this.material = material;
    }

    @Override
    public IFrameMaterial getMaterial() {

        return material;
    }

    @Override
    public IFrameSideModifier[] getSideModifiers(int side) {

        return sideModifiers[side];
    }

    @Override
    public int getMultipartCount() {

        return 0;
    }

    @Override
    public void addSideModifier(int side, IFrameSideModifier modifier) {

        sideModifiers[side] = FramezUtils.addElement(sideModifiers[side], modifier);
    }

    @Override
    public void removeSideModifier(int side, IFrameSideModifier modifier) {

        sideModifiers[side] = FramezUtils.removeElement(sideModifiers[side], modifier);
    }

    @Override
    public boolean canHaveCovers() {

        return true;
    }

    @Override
    public boolean hasPanel(int side) {

        TMultiPart p = tile().partMap(side);
        return p != null && p instanceof Microblock && ((Microblock) p).getSize() == 2;
    }

    @Override
    public boolean shouldRenderCross(int side) {

        TMultiPart p = tile().partMap(side);
        return p == null || !(p instanceof Microblock);
    }

    protected boolean canPlaceCover(int side) {

        if (tile() == null || world() == null)
            return false;

        Microblock cover = MicroblockGenerator.create(CommonMicroClass.classes()[0],
                MicroMaterialRegistry.materialID(BlockMicroMaterial.materialKey(Blocks.stone)), false);
        cover.setShape(1, side);
        return TileMultipart.canPlacePart(getWorld(), new BlockCoord(x(), y(), z()), cover);
    }

    @Override
    public boolean isSideBlocked(int side) {

        if (blocked[side])
            return true;

        TMultiPart p = tile().partMap(side);
        return p != null && p instanceof Microblock && ((Microblock) p).getSize() == 1
                && !FMPCompatRegistry.materials.contains(((Microblock) p).getIMaterial());
    }

    public boolean setSideBlocked(int side, boolean blocked) {

        TMultiPart p = tile().partMap(side);
        if (p != null && p instanceof Microblock && ((Microblock) p).getSize() == 1
                && !FMPCompatRegistry.materials.contains(((Microblock) p).getIMaterial()))
            return false;

        if (!blocked || (!canPlaceCover(side) && tile().partMap(side) == null)) {
            this.blocked[side] = blocked;
            notifyChange();
            return true;
        }

        return false;
    }

    @Override
    public boolean isSideHidden(int side) {

        return hidden[side];
    }

    public boolean setSideHidden(int side, boolean hidden) {

        if (this.hidden[side] == hidden)
            return false;

        this.hidden[side] = hidden;
        notifyChange();
        return true;
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        return !isSideBlocked(side);
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        return isSideSticky(world, position, side, movement);
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return false;
    }

    @Override
    public boolean onActivated(EntityPlayer player, int side, com.amadornes.trajectory.api.vec.Vector3 hit) {

        return false;
    }

    @Override
    public void cloneFrame(IFrame frame) {

        for (int side = 0; side < 6; side++) {
            if (frame instanceof IModifiableFrame) {
                // Copy side modifiers (re-instantiate instantiable ones and just copy the rest)
                IFrameSideModifier[] frameSideModifiers = ((IModifiableFrame) frame).getSideModifiers(side);
                if (frameSideModifiers == null)
                    continue;
                sideModifiers[side] = new IFrameSideModifier[frameSideModifiers.length];
                for (int i = 0; i < frameSideModifiers.length; i++) {
                    sideModifiers[side][i] = frameSideModifiers[i];
                }
            }

            // Copy blocked sides
            hidden[side] = frame.isSideHidden(side);
            blocked[side] = frame.isSideBlocked(side);
        }
    }

    @Override
    public void harvest() {

        tile().remPart(this);
    }

    @Override
    public boolean canConnectRedstone(int side) {

        return false;
    }

    @Override
    public int strongPowerLevel(int side) {

        return 0;
    }

    @Override
    public int weakPowerLevel(int side) {

        return 0;
    }

    @Override
    public void save(NBTTagCompound tag) {

        super.save(tag);
        writeFrame(tag);
    }

    @Override
    public void load(NBTTagCompound tag) {

        super.load(tag);
        readFrame(tag);
    }

    @Override
    public void writeDesc(MCDataOutput packet) {

        super.writeDesc(packet);
        NBTTagCompound tag = new NBTTagCompound();
        writeFrame(tag);
        packet.writeNBTTagCompound(tag);
    }

    @Override
    public void readDesc(MCDataInput packet) {

        super.readDesc(packet);
        readFrame(packet.readNBTTagCompound());
    }

    @Override
    public void writeFrame(NBTTagCompound tag) {

        tag.setString("material", material.getType());

        NBTTagList sideMods = new NBTTagList();
        for (IFrameSideModifier[] sMods : sideModifiers) {
            NBTTagList side = new NBTTagList();
            for (IFrameSideModifier m : sMods)
                side.appendTag(new NBTTagString(m.getType()));
            NBTTagCompound t = new NBTTagCompound();
            t.setTag("l", side);
            sideMods.appendTag(t);
        }
        tag.setTag("sideModifiers", sideMods);

        tag.setIntArray("hidden", new int[] { hidden[0] ? 1 : 0, hidden[1] ? 1 : 0, hidden[2] ? 1 : 0, hidden[3] ? 1 : 0,
                hidden[4] ? 1 : 0, hidden[5] ? 1 : 0 });
        tag.setIntArray("blocked", new int[] { blocked[0] ? 1 : 0, blocked[1] ? 1 : 0, blocked[2] ? 1 : 0, blocked[3] ? 1 : 0,
                blocked[4] ? 1 : 0, blocked[5] ? 1 : 0 });
    }

    @Override
    public void readFrame(NBTTagCompound tag) {

        material = ModifierRegistry.instance.findFrameMaterial(tag.getString("material"));

        NBTTagList sideMods = tag.getTagList("sideModifiers", new NBTTagCompound().getId());
        for (int i = 0; i < sideMods.tagCount(); i++) {
            NBTTagList side = sideMods.getCompoundTagAt(i).getTagList("l", new NBTTagString().getId());
            sideModifiers[i] = new IFrameSideModifier[side.tagCount()];
            for (int j = 0; j < side.tagCount(); j++)
                sideModifiers[i][j] = ModifierRegistry.instance.findFrameSideModifier(side.getStringTagAt(j));
        }

        int[] hiddenSides = tag.getIntArray("hidden");
        for (int i = 0; i < hiddenSides.length; i++)
            hidden[i] = (hiddenSides[i] == 1);

        int[] blockedSides = tag.getIntArray("blocked");
        for (int i = 0; i < blockedSides.length; i++)
            blocked[i] = (blockedSides[i] == 1);
    }

}
