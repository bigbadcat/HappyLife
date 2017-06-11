package org.xuxiang.happylife.tileentity;

import org.xuxiang.happylife.item.ItemBattery;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

/**
 * 填充器实体。
 * @author XuXiang
 *
 */
public class TileEntityFiller extends TileEntity implements ITickable, ISidedInventory {
	private static final int[] slots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};
	
	private ItemStack[] inv;				//0-26物品栏 27蓄电池栏
	private int fillCount;
	private boolean powered;
	
	private int minx;
	private int miny;
	private int minz;
	private int maxx;
	private int maxy;
	private int maxz;
	
	private int putx;
	private int puty;
	private int putz;
	
	public TileEntityFiller(){
		inv = new ItemStack[28];
		fillCount = 0;
		powered = false;
	}
	
	@Override
	public void setPos(BlockPos posIn)
    {
		super.setPos(posIn);
		resetPosition();		
    }
	
	public int getPutX(){
		return putx;
	}
	
	public int getPutY(){
		return puty;
	}
	
	public int getPutZ(){
		return putz;
	}
	
	private void resetPosition(){
		//填充器下方的长33 宽33 高7个方块
		minx = pos.getX() - 16;
		miny = pos.getY() - 7;
		minz = pos.getZ() - 16;
		maxx = pos.getX() + 16;
		maxy = pos.getY() - 1;
		maxz = pos.getZ() + 16;
		putx = minx;
		puty = miny;
		putz = minz;
	}
	
	/**
	 * 设置是否充能。
	 * @param pow 是否充能。
	 */
	public void setPowered(boolean pow){
		if (powered != pow){
			powered = pow;
			fillCount = 0;
			if (powered){
				resetPosition();
			}
		}
	}
	
	public boolean isPowered(){
		return powered;
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inv[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = getStackInSlot(index);
        if (stack != null) {
            if (stack.stackSize <= count) {
                setInventorySlotContents(index, null);
            } else {
                stack = stack.splitStack(count);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(index, null);
                }
            }
        }
        return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inv[index] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.add(0.5, 0.5, 0.5)) < 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id)
    {
		return 0;
    }

	@Override
    public void setField(int id, int value)
    {
    }

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = getStackInSlot(index);
        if (stack != null) {
            setInventorySlotContents(index, null);
        }
        return stack;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		fillCount = compound.getInteger("FillCount");
		powered = compound.getBoolean("Powered");
		minx = compound.getInteger("MinX");
		miny = compound.getInteger("MinY");
		minz = compound.getInteger("MinZ");
		maxx = compound.getInteger("MaxX");
		maxy = compound.getInteger("MaxY");
		maxz = compound.getInteger("MaxZ");
		putx = compound.getInteger("PutX");
		puty = compound.getInteger("PutY");
		putz = compound.getInteger("PutZ");
		
		NBTTagList nbttaglist = compound.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");

            if (j >= 0 && j < this.inv.length)
            {
                this.inv[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("FillCount", fillCount);
		compound.setBoolean("Powered", powered);
		compound.setInteger("MinX", minx);
		compound.setInteger("MinY", miny);
		compound.setInteger("MinZ", minz);
		compound.setInteger("MaxX", maxx);
		compound.setInteger("MaxY", maxy);
		compound.setInteger("MaxZ", maxz);
		compound.setInteger("PutX", putx);
		compound.setInteger("PutY", puty);
		compound.setInteger("PutZ", putz);

		NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.inv.length; ++i)
        {
            if (this.inv[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.inv[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag("Items", nbttaglist);
	}

	@Override
	public void update() {
		if (powered){
			if (fillCount > 0){
				--fillCount;
				if (fillCount <= 0){
					if (!this.worldObj.isRemote){
						putBlock();
			        }
					tryFindCanPutBlock();
				}
			}else{
				tryFindCanPutBlock();
			}
		}
	}
	
	/**
	 * 查找可放置的方块。
	 */
	private void tryFindCanPutBlock(){
		int i = 0;
		while (!isCanPutBlock() && ++i <= 10){
			moveNextPostition();
		}
		//当前位置可放置方块时
		if (getElectricity() >= 1 && isCanPutBlock()){
			fillCount = 20;
		}
	}
	
	public int getFillCount(){
		return fillCount;
	}
	
	/**
	 * 获取电力。
	 * @return 当前蓄电池的电力。
	 */
	private int getElectricity(){
		int ret = 0;
		ItemStack stack = inv[27];
		if (stack != null && stack.getItem() instanceof ItemBattery){
			ret = stack.getMaxDamage() - stack.getItemDamage();
		}
		return ret;
	}
	
	/**
	 * 消耗电力。
	 * @param n 消耗的电量。
	 */
	private void consumeElectricity(int n){
		ItemStack stack = inv[27];
		if (stack != null && stack.getItem() instanceof ItemBattery){
			stack.attemptDamageItem(n, this.worldObj.rand);
		}
	}
	
	/**
	 * 放置一个方块到目前移动的位置。
	 * @return 是否放置了方块。
	 */
	private boolean putBlock(){
		int index = -1;
		for (int i=0; i<inv.length; ++i){
			ItemStack stack = inv[i];
			if (stack != null){
				Block block = Block.getBlockFromItem(stack.getItem());
				if (block != null && block != Blocks.chest){
					Material mat = block.getMaterial();
					boolean can = mat == Material.iron || mat == Material.rock || mat == Material.wood || mat == Material.gourd;
					can = can || mat == Material.sand || mat == Material.grass || mat == Material.ground || mat == Material.leaves;
					can = can || mat == Material.glass || mat == Material.cloth || mat == Material.craftedSnow;
					if (can){
						index = i;
						break;
					}
				}
			}
		}
		
		if (index != -1){
			ItemStack stack = inv[index].copy();
			BlockPos pos = new BlockPos(putx, puty, putz);
			BlockState state = Block.getBlockFromItem(stack.getItem()).getBlockState();
			IBlockState istate = state.getValidStates().get(stack.getItemDamage());
			
			//植物的话会掉落
			Block block = this.worldObj.getBlockState(new BlockPos(putx, puty, putz)).getBlock();
			if (block.getMaterial() == Material.plants)
			{
				this.worldObj.destroyBlock(pos, true);
			}
			if (this.worldObj.setBlockState(pos, istate, 3)){
				consumeElectricity(1);
				--stack.stackSize;
				this.setInventorySlotContents(index, stack.stackSize <= 0 ? null : stack);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 移动到下一个位置，也有可能移动到的方块依然不可放置。
	 */
	private void moveNextPostition(){
		++putx;
		if (putx > maxx){
			putx = minx;
			++putz;
			if (putz > maxz){
				putz = minz;
				++puty;
				if (puty > maxy){
					puty = miny;
				}
			}
		}
	}
	
	/**
	 * 判断当前位置是否可放置方块。
	 * @return 是否放置方块。
	 */
	public boolean isCanPutBlock(){
		Block block = this.worldObj.getBlockState(new BlockPos(putx, puty, putz)).getBlock();
		Material mat = block.getMaterial();
		boolean ret = mat == Material.air || mat == Material.plants || mat == Material.water || mat == Material.lava;
		ret = ret || mat == Material.vine || mat == Material.fire;
		return ret;
	}
	
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock().getClass() != newSate.getBlock().getClass();
    }	
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return slots;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		//蓄电池只会放入电池槽
		if (itemStackIn != null && itemStackIn.getItem() instanceof ItemBattery){
			return index == 27;
		}
		//电池槽只能放蓄电池
		if (index == 27){
			return itemStackIn != null && itemStackIn.getItem() instanceof ItemBattery;
		}
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		//电池槽只有在不是蓄电池或蓄电池没电时才拿出
		if (index == 27 && stack != null && stack.getItem() instanceof ItemBattery && stack.getItemDamage() < stack.getMaxDamage()){
			return false;
		}
		//非方块类物品可以拿出
		return Block.getBlockFromItem(stack.getItem()) == null;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public Packet<?> getDescriptionPacket() {
	    NBTTagCompound tag = new NBTTagCompound();
	    writeToNBT(tag);
	    return new S35PacketUpdateTileEntity(this.pos, 0, tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
	    readFromNBT(pkt.getNbtCompound());
	}
}
