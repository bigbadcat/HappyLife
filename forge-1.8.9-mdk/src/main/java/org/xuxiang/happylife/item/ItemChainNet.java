package org.xuxiang.happylife.item;

import org.xuxiang.happylife.HappyLife;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 链网道具。
 * @author XuXiang
 *
 */
public class ItemChainNet extends Item {
	
	/**
	 * 物品名称。
	 */
	public static final String Name = "ChainNet";
	
	/**
	 * 构造函数。
	 */
	public ItemChainNet()
	{
		setUnlocalizedName(Name);
		setCreativeTab(HappyLife.tabHappyLife);
		GameRegistry.registerItem(this, Name);
	}
}
