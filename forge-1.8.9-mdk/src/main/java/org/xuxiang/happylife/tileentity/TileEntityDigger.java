package org.xuxiang.happylife.tileentity;

import java.util.List;

import org.xuxiang.happylife.block.BlockDigger;
import org.xuxiang.happylife.item.ItemBattery;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
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
 * 挖掘机实体。
 * @author XuXiang
 *
 */
public class TileEntityDigger extends TileEntity implements ITickable, ISidedInventory {
	private static final int[] slots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
	
	private ItemStack[] inv;			//0-26物品栏  27-30工具栏 31蓄电池栏
	private int pickCount;
	private int fortune;				//工具时运
	private boolean powered;
	
	private int minx;
	private int miny;
	private int minz;
	private int maxx;
	private int maxy;
	private int maxz;
	
	private int pickx;
	private int picky;
	private int pickz;
	
	public TileEntityDigger(){
		inv = new ItemStack[32];
		pickCount = 0;
		fortune = 0;
		powered = false;
	}
	
	@Override
	public void setPos(BlockPos posIn){
		super.setPos(posIn);
		resetPosition();
    }
	
	public int getPickX(){
		return pickx;
	}
	
	public int getPickY(){
		return picky;
	}
	
	public int getPickZ(){
		return pickz;
	}
	
	private void resetPosition(){
		//挖掘机上方的长17 宽17 高20个方块
		minx = pos.getX() - 8;
		miny = pos.getY();
		minz = pos.getZ() - 8;
		maxx = pos.getX() + 8;
		maxy = pos.getY() + 19;
		maxz = pos.getZ() + 8;
		pickx = minx;
		picky = miny;
		pickz = minz;
	}
	
	/**
	 * 设置是否充能。
	 * @param pow 是否充能。
	 */
	public void setPowered(boolean pow){
		if (powered != pow){
			powered = pow;
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
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
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
		
		pickCount = compound.getInteger("PickCount");
		fortune = compound.getInteger("Fortune");
		powered = compound.getBoolean("Powered");
		minx = compound.getInteger("MinX");
		miny = compound.getInteger("MinY");
		minz = compound.getInteger("MinZ");
		maxx = compound.getInteger("MaxX");
		maxy = compound.getInteger("MaxY");
		maxz = compound.getInteger("MaxZ");
		pickx = compound.getInteger("PickX");
		picky = compound.getInteger("PickY");
		pickz = compound.getInteger("PickZ");
		
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
		
		compound.setInteger("PickCount", pickCount);
		compound.setInteger("Fortune", fortune);
		compound.setBoolean("Powered", powered);
		compound.setInteger("MinX", minx);
		compound.setInteger("MinY", miny);
		compound.setInteger("MinZ", minz);
		compound.setInteger("MaxX", maxx);
		compound.setInteger("MaxY", maxy);
		compound.setInteger("MaxZ", maxz);
		compound.setInteger("PickX", pickx);
		compound.setInteger("PickY", picky);
		compound.setInteger("PickZ", pickz);

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
			if (pickCount > 0){
				--pickCount;
				if (pickCount == 0){
					if (!this.worldObj.isRemote){
						pickBlock();
					}
					tryFindCanPickBlock();
				}
			}else{
				tryFindCanPickBlock();
			}
		}        	
	}
	
	private void tryFindCanPickBlock(){
		int i = 0;
		while (!isCanpickBlock() && ++i <= 10){
			moveNextPostition();
		}
		//如果可挖取则尝试挖取 如果尝试挖取失败则移动下一个位置继续
		if (getElectricity() >= 1 && isCanpickBlock()){
			if (tryPickBlock()){
				consumeElectricity(1);
			}else{
				moveNextPostition();
			}
		}
	}
	
	public int getPickCount(){
		return pickCount;
	}
	
	/**
	 * 获取电力。
	 * @return 当前蓄电池的电力。
	 */
	private int getElectricity(){
		int ret = 0;
		ItemStack stack = inv[31];
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
		ItemStack stack = inv[31];
		if (stack != null && stack.getItem() instanceof ItemBattery){
			stack.attemptDamageItem(n, this.worldObj.rand);
		}
	}
	
	/**
	 * 尝试挖取方块。
	 * @return 是否可挖取。
	 */
	private boolean tryPickBlock(){
		BlockPos pickpos = new BlockPos(pickx, picky, pickz);
		IBlockState iblockstate = worldObj.getBlockState(pickpos);
		Block block = iblockstate.getBlock();
		Material mat = block.getMaterial();
		if (mat == Material.air){
			return false;
		}
		
		ItemStack toolstack = null;
		int a = 50;		//某个系数 参考代码应该是100或30 但一个太快一个太慢 50是最接近的	
		float hardness = iblockstate.getBlock().getBlockHardness(worldObj, pos);
		if (mat == Material.iron || mat == Material.rock){
			toolstack = getToolPickaxe(iblockstate);
			a = 50;
		}else if (mat == Material.wood || mat == Material.gourd){
			toolstack = getToolAxe();
			a = 50;
		}else if (mat == Material.sand || mat == Material.snow || mat == Material.craftedSnow || mat == Material.grass || mat == Material.ground){
			toolstack = getToolSpade();
			a = 100;
		}else if (mat == Material.leaves || mat == Material.cloth || mat == Material.glass || mat == Material.sponge){
			pickCount = 20;
			return true;
		}
		
		if (toolstack != null){
	        float speed = (toolstack == null ? 1.0F : toolstack.getItem().getDigSpeed(toolstack, iblockstate));
	        if (speed > 1.0F){
	            int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, toolstack);
	            if (i > 0){
	            	speed += (float)(i * i + 1);
	            }
	        }
	        
	        float pt = speed / hardness;		 
			float tick = a / pt;						
			pickCount = Math.max(5, (int)tick);
			toolstack.attemptDamageItem(1, this.worldObj.rand);
			return true;
		}

		return false;
	}
	
	/**
	 * 获取斧头工具。
	 * @return 斧头工具。
	 */
	private ItemStack getToolAxe(){
		for (int i=27; i<31; ++i){
			ItemStack stack = inv[i];
			if (stack != null && (stack.getItem() instanceof ItemAxe) && stack.getItemDamage() < stack.getMaxDamage()){
				return stack;
			}
		}
		return null;
	}
	
	/**
	 * 获取铲子工具。
	 * @return 铲子工具。
	 */
	private ItemStack getToolSpade(){
		for (int i=27; i<31; ++i){
			ItemStack stack = inv[i];
			if (stack != null && (stack.getItem() instanceof ItemSpade) && stack.getItemDamage() < stack.getMaxDamage()){
				return stack;
			}
		}
		return null;
	}
		
	/**
	 * 获取稿子工具。
	 * @param iblockstate 可采集的目标方块。
	 * @return 稿子工具。
	 */
	
	private ItemStack getToolPickaxe(IBlockState iblockstate){
		for (int i=27; i<31; ++i){
			ItemStack stack = inv[i];
			if (stack != null && (stack.getItem() instanceof ItemPickaxe) && stack.getItemDamage() < stack.getMaxDamage()){
				ItemPickaxe pickaxe = (ItemPickaxe)stack.getItem();
				if (pickaxe.canHarvestBlock(iblockstate.getBlock())){
					fortune = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
					return stack;
				}
			}
		}
		return null;
	}
	
	/**
	 * 挖取方块。
	 */
	private void pickBlock(){
		BlockPos pickpos = new BlockPos(pickx, picky, pickz);
		IBlockState iblockstate = worldObj.getBlockState(pickpos);
		Block block = iblockstate.getBlock();
		if (block.getMaterial() == Material.air){
			return;
		}

		worldObj.playAuxSFX(2001, pickpos, Block.getStateId(iblockstate));		
		List<ItemStack> items = block.getDrops(worldObj, pickpos, iblockstate, fortune);
        for (ItemStack item : items){
        	mergeItemStack(item);
        	if (item.stackSize > 0){
        		Block.spawnAsEntity(worldObj, this.pos.add(0, 1, 0), item);
        	}
        }
		worldObj.setBlockState(pickpos, Blocks.air.getDefaultState(), 3);
	}
	
	protected void mergeItemStack(ItemStack stack){
        //先把可堆叠的堆满先
        if (stack.isStackable()){
            for (int i=0; stack.stackSize > 0 && i < 27; ++i){
                ItemStack itemstack = inv[i];
                boolean same = itemstack != null && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata());
                if (same && ItemStack.areItemStackTagsEqual(stack, itemstack)){
                    int j = itemstack.stackSize + stack.stackSize;
                    if (j <= stack.getMaxStackSize()){
                        stack.stackSize = 0;
                        itemstack.stackSize = j;
                    }else if (itemstack.stackSize < stack.getMaxStackSize()){
                        stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
                        itemstack.stackSize = stack.getMaxStackSize();
                    }
                }
            }
        }

        //找空位放入
        if (stack.stackSize > 0){
            for (int i=0; i < 27; ++i){
                ItemStack itemstack1 = inv[i];
                if (itemstack1 == null){
                	inv[i] = stack.copy();
                    stack.stackSize = 0;
                    break;
                }
            }
        }
    }
	
	/**
	 * 移动到下一个位置，也有可能移动到的方块依然不可挖取。
	 */
	private void moveNextPostition(){
		++pickx;
		if (pickx > maxx){
			pickx = minx;
			++pickz;
			if (pickz > maxz){
				pickz = minz;
				++picky;
				if (picky > maxy){
					picky = miny;
				}
			}
		}
	}
	
	/**
	 * 判断当前位置是否可放置方块。
	 * @return 是否放置方块。
	 */
	private boolean isCanpickBlock(){
		Block block = this.worldObj.getBlockState(new BlockPos(pickx, picky, pickz)).getBlock();
		if (block instanceof BlockDigger){
			return false;
		}else if (block instanceof BlockSnow){
			return true;
		}
		Material mat = block.getMaterial();
		boolean ret = mat == Material.grass || mat == Material.ground || mat == Material.wood || mat == Material.rock || mat == Material.sponge || mat == Material.craftedSnow;
		ret = ret || mat == Material.iron || mat == Material.leaves || mat == Material.sand || mat == Material.glass || mat == Material.snow || mat == Material.cloth || mat == Material.gourd;
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
			return index == 31;
		}
		//电池槽只能放蓄电池
		if (index == 31){
			return itemStackIn != null && itemStackIn.getItem() instanceof ItemBattery;
		}
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		//电池槽只有在不是蓄电池或蓄电池没电时才拿出
		if (index == 31 && stack != null && stack.getItem() instanceof ItemBattery && stack.getItemDamage() < stack.getMaxDamage()){
			return false;
		}
		return true;
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
