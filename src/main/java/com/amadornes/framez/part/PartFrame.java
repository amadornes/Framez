package com.amadornes.framez.part;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import com.amadornes.framez.api.frame.IFrame;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.api.frame.IStickable;
import com.amadornes.framez.api.frame.ISticky;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.util.PropertyMaterial;

import mcmultipart.MCMultiPartMod;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.Multipart;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties.PropertyAdapter;

@SuppressWarnings("unchecked")
public class PartFrame extends Multipart implements IFrame {

    public static final IUnlistedProperty<Boolean>[] PROPERTIES_BOOL = new IUnlistedProperty[12];
    public static final IUnlistedProperty<IFrameMaterial>[] PROPERTIES_MATERIAL = new IUnlistedProperty[3];
    public static final IUnlistedProperty<?>[] PROPERTIES = new IUnlistedProperty[PROPERTIES_BOOL.length + PROPERTIES_MATERIAL.length];

    static {
        for (int i = 0; i < PROPERTIES_BOOL.length; i++)
            PROPERTIES[i] = PROPERTIES_BOOL[i] = new PropertyAdapter<Boolean>(PropertyBool.create("bool_" + i));
        PROPERTIES[PROPERTIES_BOOL.length + 0] = PROPERTIES_MATERIAL[0] = new PropertyMaterial("material_border");
        PROPERTIES[PROPERTIES_BOOL.length + 1] = PROPERTIES_MATERIAL[1] = new PropertyMaterial("material_cross");
        PROPERTIES[PROPERTIES_BOOL.length + 2] = PROPERTIES_MATERIAL[2] = new PropertyMaterial("material_binding");
    }

    private BitSet properties = new BitSet(PROPERTIES_BOOL.length);
    private IFrameMaterial[] materials = new IFrameMaterial[PROPERTIES_MATERIAL.length];
    private ISticky[] stickySides = new ISticky[6];
    private IStickable[] stickableSides = new IStickable[6];

    public PartFrame() {

        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.materials.values().iterator();
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

        list.add(new AxisAlignedBB(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D));
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
        tag.setString("border", getBorderMaterial().getType());
        tag.setString("cross", getCrossMaterial().getType());
        tag.setString("binding", getBindingMaterial().getType());
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
    public boolean canRenderInLayer(BlockRenderLayer layer) {

        return layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockStateContainer createBlockState() {

        return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[0], Arrays.copyOf(PROPERTIES, PROPERTIES.length));
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack stack, PartMOP hit) {

        if (!player.isSneaking() && stack != null) return false;

        int i = hit.sideHit.ordinal();
        properties.flip(i + (player.isSneaking() ? 6 : 0));
        markRenderUpdate();

        return super.onActivated(player, hand, stack, hit);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state) {

        IExtendedBlockState s = (IExtendedBlockState) state;
        for (int i = 0; i < PROPERTIES_BOOL.length; i++)
            s = s.withProperty(PROPERTIES_BOOL[i], properties.get(i));
        for (int i = 0; i < PROPERTIES_MATERIAL.length; i++)
            s = s.withProperty(PROPERTIES_MATERIAL[i], materials[i]);
        return s;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setString("mat_border", materials[0].getType());
        tag.setString("mat_cross", materials[1].getType());
        tag.setString("mat_binding", materials[2].getType());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        if (tag.hasKey("mat_border")) {
            materials[0] = FrameRegistry.INSTANCE.materials.get(tag.getString("mat_border"));
            materials[1] = FrameRegistry.INSTANCE.materials.get(tag.getString("mat_cross"));
            materials[2] = FrameRegistry.INSTANCE.materials.get(tag.getString("mat_binding"));
        }
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {

        super.writeUpdatePacket(buf);
        for (int i = 0; i < materials.length; i++)
            buf.writeString(materials[i].getType());
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {

        super.readUpdatePacket(buf);
        for (int i = 0; i < materials.length; i++)
            materials[i] = FrameRegistry.INSTANCE.materials.get(buf.readStringFromBuffer(512));
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

}
