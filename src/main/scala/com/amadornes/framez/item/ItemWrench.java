package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWrench extends ItemFramez implements IFramezWrench {

    @SideOnly(Side.CLIENT)
    private IIcon normal, rotate, debug, config, inhand;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

        normal = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_NORMAL);
        rotate = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_ROTATE);
        debug = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_DEBUG);
        config = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_CONFIG);
        inhand = reg.registerIcon(ModInfo.MODID + ":" + References.Texture.WRENCH_INHAND);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {

        return inhand;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {

        if (meta == 0)
            return normal;
        if (meta == 1)
            return rotate;
        if (meta == 2)
            return debug;
        if (meta == 3)
            return config;

        return normal;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        boolean isSilky = isSilky(stack);

        return "item."
                + ModInfo.MODID
                + ":wrench"
                + (isSilky ? ".silky" : "")
                + (stack.getItemDamage() == 0 ? ".normal" : (stack.getItemDamage() == 1 ? ".rotate"
                        : (stack.getItemDamage() == 2 ? ".debug" : ".config")));
    }

    @Override
    protected String getUnlocalizedTip(ItemStack stack, EntityPlayer player) {

        boolean isSilky = isSilky(stack);

        return "tooltip." + ModInfo.MODID + ":wrench" + (isSilky ? ".silky" : "");
    }

    @Override
    public WrenchAction getWrenchAction(ItemStack stack) {

        if (stack.getItemDamage() == 0)
            return WrenchAction.DEFAULT;
        else if (stack.getItemDamage() == 1)
            return WrenchAction.ROTATE;
        else if (stack.getItemDamage() == 2)
            return WrenchAction.DEBUG;
        else if (stack.getItemDamage() == 3)
            return WrenchAction.CONFIG;

        return WrenchAction.NONE;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (getWrenchAction(stack) == WrenchAction.CONFIG && world.isRemote) {
            player.openGui(Framez.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            player.swingItem();
        }

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World w, int x, int y, int z, int s, float p_77648_8_,
            float p_77648_9_, float p_77648_10_) {

        return false;
    }

    @Override
    public void onUsed(ItemStack stack, EntityPlayer player) {

    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {

        NBTTagList ench = stack.getEnchantmentTagList();
        if (ench != null) {
            for (int id = 0; id < ench.tagCount(); ++id) {
                short eid = ench.getCompoundTagAt(id).getShort("id");
                if (Enchantment.enchantmentsList[eid] != null && Enchantment.enchantmentsList[eid] == Enchantment.silkTouch)
                    return false;
            }
        }

        NBTTagList l = Items.enchanted_book.func_92110_g(book);

        boolean hasSilkTouch = false;
        int count = 0;

        if (l != null) {
            for (int i = 0; i < l.tagCount(); ++i) {
                short id = l.getCompoundTagAt(i).getShort("id");
                if (Enchantment.enchantmentsList[id] != null) {
                    if (Enchantment.enchantmentsList[id] == Enchantment.silkTouch)
                        hasSilkTouch = true;
                    count++;
                }
            }
        }

        return hasSilkTouch && count == 1;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World w, Block b, int x, int y, int z, EntityLivingBase e) {

        // Stats!
        return true;
    }

    @Override
    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {

        if (block.isToolEffective("wrench", metadata))
            return 5;
        return 0;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {

        if (player.worldObj.isRemote)
            return false;

        if (isSilky(itemstack) && player.isSneaking()) {
            FramezUtils.spawnItemInWorld(player.worldObj, silkPick(player.worldObj, new BlockPos(x, y, z), false), x, y, z);
            return true;
        }

        return false;
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List l) {

        l.add(new ItemStack(this));
        ItemStack is = new ItemStack(this);
        NBTTagList ench = new NBTTagList();
        NBTTagCompound silktouch = new NBTTagCompound();
        silktouch.setShort("id", (short) Enchantment.silkTouch.effectId);
        silktouch.setShort("lvl", (short) 1);
        ench.appendTag(silktouch);
        is.setTagInfo("ench", ench);
        l.add(is);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {

        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {

        return stack.copy();
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack) {

        return false;
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {

        return true;
    }

    @Override
    public boolean isSilky(ItemStack stack) {

        NBTTagList ench = stack.getEnchantmentTagList();
        if (ench != null) {
            for (int id = 0; id < ench.tagCount(); ++id) {
                short eid = ench.getCompoundTagAt(id).getShort("id");
                if (Enchantment.enchantmentsList[eid] != null && Enchantment.enchantmentsList[eid] == Enchantment.silkTouch)
                    return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack silkPick(World world, BlockPos position, boolean simulated) {

        return FramezUtils.silkHarvest(world, position, false);
    }
}
