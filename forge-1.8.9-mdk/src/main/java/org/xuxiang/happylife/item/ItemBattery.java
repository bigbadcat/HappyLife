package org.xuxiang.happylife.item;

import org.xuxiang.happylife.HappyLife;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 蓄电池道具。
 * @author XuXiang
 *
 */
public class ItemBattery extends Item {
	
	/**
	 * 物品名称。
	 */
	public static final String Name = "Battery";
	
	/**
	 * 物品名称。
	 */
	public static final String NameV2 = "BatteryV2";
	
	/**
	 * 电池版本。
	 */
	private int version;
	
	/**
	 * 构造函数。
	 */
	public ItemBattery(){
		this(1);
	}
	
	/**
	 * 构造函数。
	 */
	public ItemBattery(int v){
		version = v;
		String name = version == 1 ? Name : NameV2;
		setUnlocalizedName(name);
		setCreativeTab(HappyLife.tabHappyLife);
		setMaxDamage(version == 1 ? 2500 : 10000);
		setMaxStackSize(1);
		GameRegistry.registerItem(this, name);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack) + " " + getDamageProgress(stack);
	}
	
	public String getDamageProgress(ItemStack stack){
		int m = this.getMaxDamage(stack);
		int n = this.getDamage(stack);
		return (m - n) + "/" + m;
	}
}
