package org.xuxiang.happylife.item;

import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.entity.EntityWitherBomb;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 凋零炸弹道具。
 * @author XuXiang
 *
 */
public class ItemWitherBomb extends Item {
	
	/**
	 * 物品名称。
	 */
	public static final String Name = "WitherBomb";
	
	/**
	 * 构造函数。
	 */
	public ItemWitherBomb()
	{
		setUnlocalizedName(Name);
		setCreativeTab(HappyLife.tabHappyLife);
		GameRegistry.registerItem(this, Name);
	}
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (!playerIn.capabilities.isCreativeMode)
        {
            --itemStackIn.stackSize;
        }

        worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            worldIn.spawnEntityInWorld(new EntityWitherBomb(worldIn, playerIn));
        }

        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}
