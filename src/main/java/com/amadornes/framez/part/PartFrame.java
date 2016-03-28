package com.amadornes.framez.part;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.util.PropertyMaterial;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.Multipart;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties.PropertyAdapter;

@SuppressWarnings("unchecked")
public class PartFrame extends Multipart {

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

    public PartFrame() {

        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.materials.values().iterator();
        for (int i = 0; i < materials.length; i++)
            materials[i] = it.next();
    }

    public PartFrame(IFrameMaterial[] materials) {
        this.materials = materials;
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {

        list.add(AxisAlignedBB.fromBounds(0 / 16D, 14 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 2 / 16D));
        list.add(AxisAlignedBB.fromBounds(0 / 16D, 14 / 16D, 14 / 16D, 16 / 16D, 16 / 16D, 16 / 16D));
        list.add(AxisAlignedBB.fromBounds(0 / 16D, 14 / 16D, 0 / 16D, 2 / 16D, 16 / 16D, 16 / 16D));
        list.add(AxisAlignedBB.fromBounds(14 / 16D, 14 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 16 / 16D));

        list.add(AxisAlignedBB.fromBounds(0 / 16D, 0 / 16D, 0 / 16D, 2 / 16D, 16 / 16D, 2 / 16D));
        list.add(AxisAlignedBB.fromBounds(14 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 16 / 16D, 2 / 16D));
        list.add(AxisAlignedBB.fromBounds(0 / 16D, 0 / 16D, 14 / 16D, 2 / 16D, 16 / 16D, 16 / 16D));
        list.add(AxisAlignedBB.fromBounds(14 / 16D, 0 / 16D, 14 / 16D, 16 / 16D, 16 / 16D, 16 / 16D));

        list.add(AxisAlignedBB.fromBounds(0 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 2 / 16D, 2 / 16D));
        list.add(AxisAlignedBB.fromBounds(0 / 16D, 0 / 16D, 14 / 16D, 16 / 16D, 2 / 16D, 16 / 16D));
        list.add(AxisAlignedBB.fromBounds(0 / 16D, 0 / 16D, 0 / 16D, 2 / 16D, 2 / 16D, 16 / 16D));
        list.add(AxisAlignedBB.fromBounds(14 / 16D, 0 / 16D, 0 / 16D, 16 / 16D, 2 / 16D, 16 / 16D));

        list.add(AxisAlignedBB.fromBounds(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D));
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

        AxisAlignedBB bb = AxisAlignedBB.fromBounds(0, 0, 0, 1, 1, 1);
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
        tag.setString("border", materials[0].getType());
        tag.setString("cross", materials[1].getType());
        tag.setString("binding", materials[2].getType());
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
    public boolean canRenderInLayer(EnumWorldBlockLayer layer) {

        return layer == EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public BlockState createBlockState() {

        return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[0], Arrays.copyOf(PROPERTIES, PROPERTIES.length));
    }

    @Override
    public boolean onActivated(EntityPlayer player, ItemStack stack, PartMOP hit) {

        if (!player.isSneaking() && stack != null) return false;

        int i = hit.sideHit.ordinal();
        properties.flip(i + (player.isSneaking() ? 6 : 0));
        markRenderUpdate();

        return super.onActivated(player, stack, hit);
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

}
