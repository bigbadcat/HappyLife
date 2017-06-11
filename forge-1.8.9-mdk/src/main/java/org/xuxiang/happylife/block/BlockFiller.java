package org.xuxiang.happylife.block;

import java.util.Random;

import org.xuxiang.happylife.GuiHandler;
import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.tileentity.TileEntityFiller;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 填充器，用于将方块填满某个区域。
 * @author XuXiang
 *
 */
public class BlockFiller extends BlockContainer {
	
	private boolean isOn;
	private static boolean keepInventory;
	
	/**
	 * 构造函数。
	 * @param on 是否为运行状态。
	 */
	public BlockFiller(boolean on) {
		super(Material.iron);
		this.isOn = on;		
		this.setUnlocalizedName(getName());
		if (isOn)
        {
            this.setLightLevel(1.0F);
        }
		else
		{
			this.setCreativeTab(HappyLife.tabHappyLife);
		}
		this.setHardness(5.0F);
		this.setResistance(10.0f);
		this.setStepSound(Block.soundTypeMetal);
		this.setHarvestLevel("pickaxe", 0);
		GameRegistry.registerBlock(this, getName());
	}

	public String getName(){
		return isOn ? "FillerOn" : "Filler";
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {        	
        	playerIn.openGui(HappyLife.instance, GuiHandler.ID_FILLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
	
	/**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public int getRenderType()
    {
        return 3;
    }
    
    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock){
    	checkPowered(worldIn, pos, state);
    }
    
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state){
    	checkPowered(worldIn, pos, state);
    }
    
    public void checkPowered(World worldIn, BlockPos pos, IBlockState state){
    	if (!worldIn.isRemote){
        	TileEntityFiller entity = (TileEntityFiller)worldIn.getTileEntity(pos);
        	entity.setPowered(worldIn.isBlockPowered(pos));        	
        	if (this.isOn && !worldIn.isBlockPowered(pos)){
        		BlockFiller.setState(false, worldIn, pos);
            }else if (!this.isOn && worldIn.isBlockPowered(pos)){
                BlockFiller.setState(true, worldIn, pos);
            }
        }
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand){
        if (!worldIn.isRemote){
            if (this.isOn && !worldIn.isBlockPowered(pos)){
                BlockFiller.setState(false, worldIn, pos);
            }
        }
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFiller();
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
		if (!worldIn.isRemote){
			if (!keepInventory){
				TileEntityFiller tileentity = (TileEntityFiller)worldIn.getTileEntity(pos);		
		        if (tileentity != null){
		            InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
		            worldIn.updateComparatorOutputLevel(pos, this);
		        }
		        super.breakBlock(worldIn, pos, state);
			}
		}
    }
	
	/**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(HappyLife.Filler);
    }
    
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos){
        return Item.getItemFromBlock(HappyLife.Filler);
    }

    protected ItemStack createStackedBlock(IBlockState state){
        return new ItemStack(HappyLife.Filler);
    }
    
    public static void setState(boolean active, World worldIn, BlockPos pos){
        keepInventory = true;
        if (active){
        	worldIn.setBlockState(pos, HappyLife.FillerOn.getDefaultState(), 3);
        }else{
        	worldIn.setBlockState(pos, HappyLife.Filler.getDefaultState(), 3);
        }
        keepInventory = false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube()
    {
        return false;
    }
    
}
