package org.xuxiang.happylife.block;

import org.xuxiang.happylife.HappyLife;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockSpringBed extends Block {

	/**
	 * 方块名称。
	 */
	public static final String Name = "SpringBed";
	
	public BlockSpringBed() {
		super(Material.cloth, MapColor.woodColor);
		setCreativeTab(HappyLife.tabHappyLife);
		this.setUnlocalizedName(Name);
		this.setHardness(1.0F);
//		this.setResistance(10.0f);
		this.setStepSound(Block.soundTypeCloth);
		this.setHarvestLevel("axe", 0);
		GameRegistry.registerBlock(this, Name);
	}

	/**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (entityIn.isSneaking())
        {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
        else
        {
            entityIn.fall(fallDistance, 0.0F);
        }
    }
    
    /**
     * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
     * on its own
     */
    public void onLanded(World worldIn, Entity entityIn)
    {
        if (entityIn.isSneaking())
        {
            super.onLanded(worldIn, entityIn);
        }
        else if (entityIn.motionY < 0.0D)
        {
            entityIn.motionY = -entityIn.motionY * 0.2f;
        }
    }
}
