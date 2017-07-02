package org.xuxiang.happylife.block;

import org.xuxiang.happylife.HappyLife;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 链块，用于建筑和存储链网。
 * @author XuXiang
 *
 */
public class BlockChain extends Block{
	
	/**
	 * 方块名称。
	 */
	public static final String Name = "Chain";
	
	/**
	 * 构造函数。
	 * @param materialIn 方块材质。
	 */
	public BlockChain() {
		super(Material.iron, MapColor.grayColor);
		setCreativeTab(HappyLife.tabHappyLife);
		this.setUnlocalizedName(Name);
//		this.setHardness(5.0F);
//		this.setResistance(10.0f);
		this.setStepSound(Block.soundTypeCloth);
		this.setHarvestLevel("pickaxe", 0);
		GameRegistry.registerBlock(this, Name);
	}
}
