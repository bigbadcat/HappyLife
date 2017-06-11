package org.xuxiang.happylife.item;

import org.xuxiang.happylife.HappyLife;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 铁粒道具。
 * @author XuXiang
 *
 */
public class ItemIronNugget extends Item {
	
	/**
	 * 物品名称。
	 */
	public static final String Name = "IronNugget";
	
	/**
	 * 构造函数。
	 */
	public ItemIronNugget()
	{
		setUnlocalizedName(Name);
		setCreativeTab(HappyLife.tabHappyLife);
		GameRegistry.registerItem(this, Name);
	}
}
