package org.xuxiang.happylife.block;

import java.util.Random;

import org.xuxiang.happylife.GuiHandler;
import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.tileentity.TileEntityGenerator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 发电机，用于给蓄电池充电。
 * @author XuXiang
 *
 */
public class BlockGenerator extends BlockContainer{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private boolean isBurning;
	private static boolean keepInventory;
	
	/**
	 * 构造函数。
	 * @param burn 是否为燃烧状态。
	 */
	public BlockGenerator(boolean burn) {
		super(Material.iron);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.isBurning = burn;		
		this.setUnlocalizedName(getName());
		if (isBurning)
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
		return isBurning ? "GeneratorBurn" : "Generator";
	}
	
	/**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public int getRenderType(){
        return 3;
    }
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
		if (!worldIn.isRemote){
			if (!keepInventory){
				TileEntityGenerator tileentity = (TileEntityGenerator)worldIn.getTileEntity(pos);		
		        if (tileentity != null){
		            InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
		            worldIn.updateComparatorOutputLevel(pos, this);
		        }
		        super.breakBlock(worldIn, pos, state);
			}
		}
    }

	public static void setState(boolean active, World worldIn, BlockPos pos)
    {
		IBlockState iblockstate = worldIn.getBlockState(pos);
        keepInventory = true;
        if (active){
        	worldIn.setBlockState(pos, HappyLife.GeneratorBurn.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }else{
        	worldIn.setBlockState(pos, HappyLife.Generator.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        keepInventory = false;
    }
	
	/**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityFurnace)
            {
                ((TileEntityFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }
	
	/**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }
    
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {        	
        	playerIn.openGui(HappyLife.instance, GuiHandler.ID_GENERATOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityGenerator();
	}
    
    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(HappyLife.Generator);
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand){
    	//燃烧火焰特效
        if (this.isBurning){
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            switch (enumfacing){
                case WEST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case EAST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case NORTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case SOUTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
				default:
					break;
            }
        }
    }
}
