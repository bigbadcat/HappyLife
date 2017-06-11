package org.xuxiang.happylife.item;

import org.xuxiang.happylife.HappyLife;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 革片道具。
 * @author XuXiang
 *
 */
public class ItemLeatherNugget extends Item {
	
	/**
	 * 物品名称。
	 */
	public static final String Name = "LeatherNugget";
	
	/**
	 * 构造函数。
	 */
	public ItemLeatherNugget()
	{
		setUnlocalizedName(Name);
		setCreativeTab(HappyLife.tabHappyLife);
		GameRegistry.registerItem(this, Name);
	}
}
