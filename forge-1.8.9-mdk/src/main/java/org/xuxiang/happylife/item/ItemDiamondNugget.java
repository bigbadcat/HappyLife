package org.xuxiang.happylife.item;

import org.xuxiang.happylife.HappyLife;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 钻石粒道具。
 * @author XuXiang
 *
 */
public class ItemDiamondNugget extends Item {
	
	/**
	 * 物品名称。
	 */
	public static final String Name = "DiamondNugget";
	
	/**
	 * 构造函数。
	 */
	public ItemDiamondNugget()
	{
		setUnlocalizedName(Name);
		setCreativeTab(HappyLife.tabHappyLife);
		GameRegistry.registerItem(this, Name);
	}
}
