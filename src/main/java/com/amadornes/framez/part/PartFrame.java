package com.amadornes.framez.part;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrame;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.api.frame.IStickable;
import com.amadornes.framez.api.frame.ISticky;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.util.PropertyMaterial;

import mcmultipart.MCMultiPartMod;
import mcmultipart.client.multipart.AdvancedParticleManager;
import mcmultipart.client.multipart.ICustomHighlightPart;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.microblock.IMicroblock.IFaceMicroblock;
import mcmultipart.multipart.ICenterConnectablePart;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.Multipart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties.PropertyAdapter;

@SuppressWarnings("unchecked")
public class PartFrame extends Multipart implements IFrame, ICustomHighlightPart {

    public static final IUnlistedProperty<EnumFrameSideState>[] PROPERTIES_SIDE_STATE = new IUnlistedProperty[6];
    public static final IUnlistedProperty<IFrameMaterial>[] PROPERTIES_MATERIAL = new IUnlistedProperty[3];
    public static final IUnlistedProperty<?>[] PROPERTIES = new IUnlistedProperty[PROPERTIES_SIDE_STATE.length
            + PROPERTIES_MATERIAL.length];

    static {
        for (int i = 0; i < 6; i++)
            PROPERTIES[i] = PROPERTIES_SIDE_STATE[i] = new PropertyAdapter<EnumFrameSideState>(
                    PropertyEnum.create("side_" + i, EnumFrameSideState.class));
        PROPERTIES[6] = PROPERTIES_MATERIAL[0] = new PropertyMaterial("material_border");
        PROPERTIES[7] = PROPERTIES_MATERIAL[1] = new PropertyMaterial("material_cross");
        PROPERTIES[8] = PROPERTIES_MATERIAL[2] = new PropertyMaterial("material_binding");
    }

    private IFrameMaterial[] materials = new IFrameMaterial[PROPERTIES_MATERIAL.length];
    private ISticky[] stickySides = new ISticky[6];
    private IStickable[] stickableSides = new IStickable[6];

    public PartFrame() {

        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.getMaterials().values().iterator();
        for (int i = 0; i < materials.length; i++)
            materials[i] = it.next();
        initStickiness();
    }

    public PartFrame(IFrameMaterial[] materials) {

        this.materials = materials;
        initStickiness();
    }

    private void initStickiness() {

        for (int i = 0; i < 6; i++) {
            EnumFacing facing = EnumFacing.getFront(i);
            stickySides[i] = () -> checkStickiness(facing);
            stickableSides[i] = () -> checkStickiness(facing);
        }
    }

    private boolean checkStickiness(EnumFacing facing) {

        if (getContainer() != null) {
            IMultipart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(facing));
            if (part != null && part instanceof IMicroblock) return ((IMicroblock) part).getSize() != 1;
            return true;
        }
        return false;
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {

        list.add(new AxisAlignedBB(0 / 16D, 14 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 2 / 16D));
        list.add(new AxisAlignedBB(0 / 16D, 14 / 16D, 14 / 16D, 16 / 16D, 16 / 16D, 16 / 16D));
        list.add(new AxisAlignedBB(0 / 16D, 14 / 16D, 0 / 16D, 2 / 16D, 16 / 16D, 16 / 16D));
        list.add(new AxisAlignedBB(14 / 16D, 14 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 16 / 16D));

        list.add(new AxisAlignedBB(0 / 16D, 0 / 16D, 0 / 16D, 2 / 16D, 16 / 16D, 2 / 16D));
        list.add(new AxisAlignedBB(14 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 2 / 16D));
        list.add(new AxisAlignedBB(0 / 16D, 0 / 16D, 14 / 16D, 2 / 16D, 16 / 16D, 16 / 16D));
        list.add(new AxisAlignedBB(14 / 16D, 0 / 16D, 14 / 16D, 16 / 16D, 16 / 16D, 16 / 16D));

        list.add(new AxisAlignedBB(0 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 2 / 16D, 2 / 16D));
        list.add(new AxisAlignedBB(0 / 16D, 0 / 16D, 14 / 16D, 16 / 16D, 2 / 16D, 16 / 16D));
        list.add(new AxisAlignedBB(0 / 16D, 0 / 16D, 0 / 16D, 2 / 16D, 2 / 16D, 16 / 16D));
        list.add(new AxisAlignedBB(14 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 2 / 16D, 16 / 16D));

        if (!FramezConfig.Client.clickThroughFrames) {
            list.add(new AxisAlignedBB(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D));
        }
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

        AxisAlignedBB bb = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
        if (bb.intersectsWith(mask)) list.add(bb);
    }

    @Override
    public boolean occlusionTest(IMultipart part) {

        if (part instanceof PartFrame) return false;
        return super.occlusionTest(part);
    }

    private ItemStack getItem() {

        ItemStack stack = new ItemStack(FramezItems.frame);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("border", getBorderMaterial().getType().toString());
        tag.setString("cross", getCrossMaterial().getType().toString());
        tag.setString("binding", getBindingMaterial().getType().toString());
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {

        return getItem();
    }

    @Override
    public List<ItemStack> getDrops() {

        return Arrays.asList(getItem());
    }

    @Override
    public float getHardness(PartMOP hit) {

        return 1;
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {

        return layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockStateContainer createBlockState() {

        return new BlockStateContainer.Builder(MCMultiPartMod.multipart).add(PROPERTIES).build();
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack stack, PartMOP hit) {

        return false;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state) {

        IExtendedBlockState s = (IExtendedBlockState) state;
        for (int i = 0; i < PROPERTIES_SIDE_STATE.length; i++)
            s = s.withProperty(PROPERTIES_SIDE_STATE[i], getSideState(EnumFacing.getFront(i)));
        for (int i = 0; i < PROPERTIES_MATERIAL.length; i++)
            s = s.withProperty(PROPERTIES_MATERIAL[i], materials[i]);
        return s;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {

        tag = super.writeToNBT(tag);
        tag.setString("mat_border", materials[0].getType().toString());
        tag.setString("mat_cross", materials[1].getType().toString());
        tag.setString("mat_binding", materials[2].getType().toString());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        if (tag.hasKey("mat_border")) {
            materials[0] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("mat_border")));
            materials[1] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("mat_cross")));
            materials[2] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("mat_binding")));
        }
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {

        super.writeUpdatePacket(buf);
        for (int i = 0; i < materials.length; i++)
            buf.writeString(materials[i].getType().toString());
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {

        super.readUpdatePacket(buf);
        for (int i = 0; i < materials.length; i++)
            materials[i] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(buf.readStringFromBuffer(128)));
    }

    @Override
    public IFrameMaterial getBorderMaterial() {

        return materials[0];
    }

    @Override
    public IFrameMaterial getCrossMaterial() {

        return materials[1];
    }

    @Override
    public IFrameMaterial getBindingMaterial() {

        return materials[2];
    }

    public EnumFrameSideState getSideState(EnumFacing face) {

        ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(face));
        if (part != null && part instanceof IFaceMicroblock) {
            if (((IFaceMicroblock) part).getSize() == 2) {
                return EnumFrameSideState.PANEL;
            } else {
                return EnumFrameSideState.HOLLOW;
            }
        } else {
            part = getContainer().getPartInSlot(PartSlot.CENTER);
            if (part != null && part instanceof ICenterConnectablePart) {
                int radius = ((ICenterConnectablePart) part).getHoleRadius(face);
                if (radius > 0) {
                    if (radius <= 8) return EnumFrameSideState.PIPE;
                    else return EnumFrameSideState.HOLLOW;
                }
            }
            if (FramezConfig.Client.connectContiguousFrames) {
                IMultipartContainer container = MultipartHelper.getPartContainer(getWorld(), getPos().offset(face));
                if (container != null) {
                    for (IMultipart p : container.getParts()) {
                        if (p instanceof PartFrame) return EnumFrameSideState.HOLLOW;
                    }
                }
            }
            if (FramezConfig.Client.clickThroughFrames) return EnumFrameSideState.PIPE;
            return EnumFrameSideState.NORMAL;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

        if (capability == IFrame.CAPABILITY_FRAME && facing == null) return true;
        if (capability == ISticky.CAPABILITY_STICKY && facing != null) return true;
        if (capability == IStickable.CAPABILITY_STICKABLE && facing != null) return true;
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        if (capability == IFrame.CAPABILITY_FRAME && facing == null) return (T) this;
        if (capability == ISticky.CAPABILITY_STICKY && facing != null) return (T) stickySides[facing.ordinal()];
        if (capability == IStickable.CAPABILITY_STICKABLE && facing != null) return (T) stickableSides[facing.ordinal()];
        return null;
    }

    @Override
    public boolean addHitEffects(PartMOP hit, AdvancedParticleManager particleManager) {

        particleManager.addBlockHitEffects(getPos(), hit, Block.FULL_BLOCK_AABB,
                Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(materials[1].getTexture(EnumFrameTexture.CROSS).toString()));
        return true;
    }

    @Override
    public boolean addDestroyEffects(AdvancedParticleManager particleManager) {

        particleManager.addBlockDestroyEffects(getPos(),
                Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(materials[1].getTexture(EnumFrameTexture.CROSS).toString()));
        return true;
    }

    @Override
    public boolean drawHighlight(PartMOP hit, EntityPlayer player, float partialTicks) {

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        double d = 0.002D;
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).expandXyz(d));
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(0, 2 / 16D, 2 / 16D, 1, 14 / 16D, 14 / 16D).expand(d, -d, -d));
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(2 / 16D, 0, 2 / 16D, 14 / 16D, 1, 14 / 16D).expand(-d, d, -d));
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(2 / 16D, 2 / 16D, 0, 14 / 16D, 14 / 16D, 1).expand(-d, -d, d));

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        return true;
    }

}
