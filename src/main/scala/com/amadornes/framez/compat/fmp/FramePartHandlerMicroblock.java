package com.amadornes.framez.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.CommonMicroClass;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.HollowMicroblock;
import codechicken.microblock.ItemMicroPart;
import codechicken.microblock.Microblock;
import codechicken.microblock.MicroblockClass;
import codechicken.microblock.MicroblockClient;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.CommonProxy;
import com.amadornes.framez.Framez;
import com.amadornes.framez.api.movement.FrameFeature;
import com.amadornes.framez.api.wrench.IFramePart;
import com.amadornes.framez.api.wrench.IFramePart.IFeatureDependantFramePart;
import com.amadornes.framez.api.wrench.IFramePart.IFramePartFace;
import com.amadornes.framez.api.wrench.IFramePartHandler;
import com.amadornes.trajectory.api.vec.BlockPos;

public class FramePartHandlerMicroblock implements IFramePartHandler {

    @Override
    public IFramePart[] silkHarvest(World world, BlockPos position, boolean simulated) {

        List<IFramePart> parts = new ArrayList<IFramePart>();

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(position.x, position.y, position.z));
        if (tmp != null) {
            for (TMultiPart p : new ArrayList<TMultiPart>(tmp.jPartList())) {
                if (p instanceof Microblock) {
                    if (p instanceof FaceMicroblock || p instanceof HollowMicroblock)
                        parts.add(new FramePartMicroblockFace((Microblock) p));
                    else
                        parts.add(new FramePartMicroblock((Microblock) p));
                    if (!simulated)
                        tmp.remPart(p);
                }
            }
        }

        return parts.toArray(new IFramePart[parts.size()]);
    }

    @Override
    public IFramePart createLocated(int relX, int relY, ItemStack stack) {

        int face = -1;
        if (relX == 0 && relY == 1)
            face = 0;
        else if (relX == 0 && relY == -1)
            face = 1;
        else if (relX == 1 && relY == 0)
            face = 2;
        else if (relX == -1 && relY == 0)
            face = 3;
        else if ((relX == -1 || relX == 1) && relY == -1)
            face = 4;
        else if ((relX == -1 || relX == 1) && relY == 1)
            face = 5;

        if (face == -1)
            return null;

        if (stack.getItem() instanceof ItemMicroPart) {
            MicroblockClass microClass = CommonMicroClass.getMicroClass(stack.getItemDamage());
            Microblock mb = microClass.create(false, ItemMicroPart.getMaterialID(stack));
            int size = stack.getItemDamage() & 0xFF;
            if (mb instanceof FaceMicroblock || mb instanceof HollowMicroblock) {
                if (size > 2)
                    return null;
                if (mb.getIMaterial() instanceof MicroMaterialFrame)
                    return null;
                mb.setShape(size, face);
                return new FramePartMicroblockFace(mb);
            }
            // else {
            // mb.setShape(size, 0);
            // return new FramePartMicroblock(mb);
            // }
        }

        return null;
    }

    @Override
    public IFramePart createPart(String type, boolean client) {

        if (type.equals("fmp_mcr"))
            return new FramePartMicroblock();
        if (type.equals("fmp_mcr_face"))
            return new FramePartMicroblockFace();

        return null;
    }

    public static class FramePartMicroblock implements IFramePart, IFeatureDependantFramePart {

        public Microblock mb;

        public FramePartMicroblock(Microblock mb) {

            this.mb = mb;
        }

        public FramePartMicroblock() {

        }

        @Override
        public String getType() {

            return "fmp_mcr";
        }

        @Override
        public void writePickedToNBT(NBTTagCompound tag) {

            tag.setInteger("_size", mb.getSize());
            tag.setInteger("_slot", mb.getShape());
            tag.setInteger("_mat", mb.getMaterial());
            mb.save(tag);
            tag.setString("microclass", mb.microClass().getName());
        }

        @Override
        public void readPickedFromNBT(NBTTagCompound tag) {

            String name = tag.getString("microclass");
            MicroblockClass mc = null;
            for (MicroblockClass c : CommonMicroClass.classes()) {
                if (c.getName().equals(name)) {
                    mc = c;
                    break;
                }
            }
            if (mc == null)
                throw new IllegalStateException("Microclass not found! D:");
            mb = mc.create(Framez.proxy.getClass() != CommonProxy.class, tag.getInteger("_material"));
            mb.setShape(tag.getInteger("_size"), tag.getInteger("_slot"));
            mb.load(tag);
        }

        @Override
        public int getPlacementPriority() {

            return 0;
        }

        @Override
        public boolean canPlace(World world, BlockPos position) {

            return TileMultipart.canPlacePart(world, new BlockCoord(position.x, position.y, position.z), mb);
        }

        @Override
        public boolean canPlaceWith(IFramePart[] parts) {

            return true;
        }

        @Override
        public void place(World world, BlockPos position) {

            TileMultipart.addPart(world, new BlockCoord(position.x, position.y, position.z), mb);
        }

        @Override
        public void renderItem(ItemRenderType type) {

            GL11.glPushMatrix();

            CCRenderState.setDynamic();
            Tessellator.instance.startDrawingQuads();
            ((MicroblockClient) mb).render(Vector3.zero, -1);
            Tessellator.instance.draw();

            GL11.glPopMatrix();
        }

        @Override
        public String[] getRequiredFeatures() {

            return new String[] {};
        }

        @Override
        public String[] getIncompatibleFeatures() {

            return new String[] { FrameFeature.BLOCK_SIDES.id };
        }

        @Override
        public String getDisplayName() {

            return I18n.format(mb.microClass().getName() + "." + mb.getSize() + ".name", mb.getIMaterial().getLocalizedName());
        }

    }

    public static class FramePartMicroblockFace extends FramePartMicroblock implements IFramePartFace {

        public FramePartMicroblockFace(Microblock mb) {

            this.mb = mb;
        }

        public FramePartMicroblockFace() {

        }

        @Override
        public String getType() {

            return "fmp_mcr_face";
        }

        @Override
        public int getFace() {

            return mb.getShape();
        }

        @Override
        public void renderItem(ItemRenderType type) {

            GL11.glPushMatrix();

            int f = getFace();
            double t = 0.005;
            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glScaled(f == 4 || f == 5 ? 1 : 1 - t, f == 0 || f == 1 ? 1 : 1 - t, f == 2 || f == 3 ? 1 : 1 - t);
            GL11.glTranslated(-0.5, -0.5, -0.5);

            CCRenderState.setDynamic();
            Tessellator.instance.startDrawingQuads();
            ((MicroblockClient) mb).render(Vector3.zero, -1);
            Tessellator.instance.draw();

            GL11.glPopMatrix();
        }

    }

}
