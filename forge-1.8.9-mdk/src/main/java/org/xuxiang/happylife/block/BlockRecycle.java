package org.xuxiang.happylife.block;

import org.xuxiang.happylife.GuiHandler;
import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.tileentity.TileEntityRecycle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 回收台，用于装备或工具的材料回收。
 * @author XuXiang
 *
 */
public class BlockRecycle extends BlockContainer{
	
	/**
	 * 方块名称。
	 */
	public static final String Name = "Recycle";
	
	/**
	 * 构造函数。
	 * @param materialIn 方块材质。
	 */
	public BlockRecycle() {
		super(Material.iron, MapColor.diamondColor);
		setCreativeTab(HappyLife.tabHappyLife);
		this.setUnlocalizedName(Name);
		this.setHardness(5.0F);
		this.setResistance(10.0f);
		this.setStepSound(Block.soundTypeMetal);
		this.setHarvestLevel("pickaxe", 0);
		GameRegistry.registerBlock(this, Name);
	}
	
	/**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public int getRenderType(){
        return 3;
    }
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {        	
        	playerIn.openGui(HappyLife.instance, GuiHandler.ID_RECYCLE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRecycle();
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
		if (!worldIn.isRemote){
			TileEntityRecycle tileentity = (TileEntityRecycle)worldIn.getTileEntity(pos);		
	        if (tileentity != null){
	            if (tileentity.getStackInSlot(0) != null){
	            	Block.spawnAsEntity(worldIn, pos, tileentity.getStackInSlot(0));
	            }
	            worldIn.updateComparatorOutputLevel(pos, this);
	        }
	        super.breakBlock(worldIn, pos, state);
		}
    }
}
