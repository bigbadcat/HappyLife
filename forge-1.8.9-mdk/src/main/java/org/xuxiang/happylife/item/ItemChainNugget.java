package org.xuxiang.happylife.item;

import org.xuxiang.happylife.HappyLife;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 链扣道具。
 * @author XuXiang
 *
 */
public class ItemChainNugget extends Item {
	
	/**
	 * 物品名称。
	 */
	public static final String Name = "ChainNugget";
	
	/**
	 * 构造函数。
	 */
	public ItemChainNugget()
	{
		setUnlocalizedName(Name);
		setCreativeTab(HappyLife.tabHappyLife);
		GameRegistry.registerItem(this, Name);
	}
}
