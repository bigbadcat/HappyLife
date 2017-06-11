package org.xuxiang.happylife.tileentity;

import java.util.HashMap;
import java.util.Map;

import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.item.ItemBattery;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

/**
 * 回收台实体。
 * @author XuXiang
 *
 */
public class TileEntityRecycle extends TileEntity implements IInventory {
	/**
	 * 回收信息。
	 * @author XuXiang
	 *
	 */
	public static class RecycleInfo
	{
		private Item _item;
		private Item _target;
		private int _number;
		
		/**
		 * 构造函数。
		 * @param item 要回收的物品。
		 * @param target 回收生成的目标物品。
		 * @param num 分解无损耗时生成的物品数量。
		 */
		public RecycleInfo(Item item, Item target, int num)
		{
			_item = item;
			_target = target;
			_number = num;			
		}
		
		public Item getItem(){return _item;}
		
		public Item getTarget(){return _target;}
		
		public int getNumber(){return _number;}
		
	}
	
	private static Map<Item, RecycleInfo> RecycleItems = new HashMap<Item, RecycleInfo>();
	
	public static void initRecycleItems(){
		//铁-工具
		addRecycleItems(Items.iron_shovel, HappyLife.IronNugget, 9 * 1);		
		addRecycleItems(Items.iron_pickaxe, HappyLife.IronNugget, 9 * 3);
		addRecycleItems(Items.iron_axe, HappyLife.IronNugget, 9 * 3);
		addRecycleItems(Items.iron_hoe, HappyLife.IronNugget, 9 * 2);
		
		//铁-装备
		addRecycleItems(Items.iron_sword, HappyLife.IronNugget, 9 * 2);		
		addRecycleItems(Items.iron_helmet, HappyLife.IronNugget, 9 * 5);
		addRecycleItems(Items.iron_chestplate, HappyLife.IronNugget, 9 * 8);
		addRecycleItems(Items.iron_leggings, HappyLife.IronNugget, 9 * 7);
		addRecycleItems(Items.iron_boots, HappyLife.IronNugget, 9 * 4);
		addRecycleItems(Items.iron_horse_armor, HappyLife.IronNugget, 9 * 6);

		//铁-其他
		addRecycleItems(Items.iron_door, HappyLife.IronNugget, 9 * 2);
		addRecycleItems(Items.flint_and_steel, HappyLife.IronNugget, 9 * 1);
		addRecycleItems(Items.compass, HappyLife.IronNugget, 9 * 4);
		addRecycleItems(Items.bucket, HappyLife.IronNugget, 9 * 3);
		addRecycleItems(Items.shears, HappyLife.IronNugget, 9 * 2);
		addRecycleItems(Items.minecart, HappyLife.IronNugget, 9 * 5);
		addRecycleItems(Item.getItemFromBlock(Blocks.rail), HappyLife.IronNugget, 3);
		addRecycleItems(Item.getItemFromBlock(Blocks.detector_rail), HappyLife.IronNugget, 9);
		addRecycleItems(Item.getItemFromBlock(Blocks.activator_rail), HappyLife.IronNugget, 9);
		addRecycleItems(Item.getItemFromBlock(Blocks.hopper), HappyLife.IronNugget, 9 * 5);
		addRecycleItems(Item.getItemFromBlock(Blocks.iron_trapdoor), HappyLife.IronNugget, 9 * 4);
		addRecycleItems(Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate), HappyLife.IronNugget, 9 * 2);
		addRecycleItems(Item.getItemFromBlock(Blocks.tripwire_hook), HappyLife.IronNugget, 4);
		addRecycleItems(Item.getItemFromBlock(Blocks.iron_bars), HappyLife.IronNugget, 3);
		addRecycleItems(Items.cauldron, HappyLife.IronNugget, 9 * 6);
		
		//黄金-工具
		addRecycleItems(Items.golden_shovel, Items.gold_nugget, 9 * 1);		
		addRecycleItems(Items.golden_pickaxe, Items.gold_nugget, 9 * 3);
		addRecycleItems(Items.golden_axe, Items.gold_nugget, 9 * 3);
		addRecycleItems(Items.golden_hoe, Items.gold_nugget, 9 * 2);
		
		//黄金-装备
		addRecycleItems(Items.golden_sword, Items.gold_nugget, 9 * 2);		
		addRecycleItems(Items.golden_helmet, Items.gold_nugget, 9 * 5);
		addRecycleItems(Items.golden_chestplate, Items.gold_nugget, 9 * 8);
		addRecycleItems(Items.golden_leggings, Items.gold_nugget, 9 * 7);
		addRecycleItems(Items.golden_boots, Items.gold_nugget, 9 * 4);
		addRecycleItems(Items.golden_horse_armor, Items.gold_nugget, 9 * 6);

		//黄金-其他
		addRecycleItems(Items.clock, Items.gold_nugget, 9 * 4);
		addRecycleItems(Item.getItemFromBlock(Blocks.golden_rail), Items.gold_nugget, 9 * 1);
		addRecycleItems(Item.getItemFromBlock(Blocks.light_weighted_pressure_plate), Items.gold_nugget, 9 * 2);
		
		//钻石-工具
		addRecycleItems(Items.diamond_shovel, HappyLife.DiamondNugget, 9 * 1);		
		addRecycleItems(Items.diamond_pickaxe, HappyLife.DiamondNugget, 9 * 3);
		addRecycleItems(Items.diamond_axe, HappyLife.DiamondNugget, 9 * 3);
		addRecycleItems(Items.diamond_hoe, HappyLife.DiamondNugget, 9 * 2);
		
		//钻石-装备
		addRecycleItems(Items.diamond_sword, HappyLife.DiamondNugget, 9 * 2);		
		addRecycleItems(Items.diamond_helmet, HappyLife.DiamondNugget, 9 * 5);
		addRecycleItems(Items.diamond_chestplate, HappyLife.DiamondNugget, 9 * 8);
		addRecycleItems(Items.diamond_leggings, HappyLife.DiamondNugget, 9 * 7);
		addRecycleItems(Items.diamond_boots, HappyLife.DiamondNugget, 9 * 4);
		addRecycleItems(Items.diamond_horse_armor, HappyLife.DiamondNugget, 9 * 6);
		
		//皮革-装备		
		addRecycleItems(Items.leather_helmet, HappyLife.LeatherNugget, 9 * 5);
		addRecycleItems(Items.leather_chestplate, HappyLife.LeatherNugget, 9 * 8);
		addRecycleItems(Items.leather_leggings, HappyLife.LeatherNugget, 9 * 7);
		addRecycleItems(Items.leather_boots, HappyLife.LeatherNugget, 9 * 4);
				
		//链-装备	
		addRecycleItems(Items.chainmail_helmet, HappyLife.ChainNugget, 9 * 5);
		addRecycleItems(Items.chainmail_chestplate, HappyLife.ChainNugget, 9 * 8);
		addRecycleItems(Items.chainmail_leggings, HappyLife.ChainNugget, 9 * 7);
		addRecycleItems(Items.chainmail_boots, HappyLife.ChainNugget, 9 * 4);
	}
	
	public static void addRecycleItems(Item item, Item target, int num){
		RecycleItems.put(item, new RecycleInfo(item, target, num));
	}
	
	private ItemStack[] inv;	//0电池槽 1分解槽 2回收槽
	private int cost;			//消耗电量

	public TileEntityRecycle(){
		inv = new ItemStack[3];
		cost = 0;
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
        if (index == 1){
        	this.updateRecycleOutput();
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
		
		compound.setInteger("Cost", cost);
		
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

		cost = compound.getInteger("Cost");
		
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

	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock().getClass() != newSate.getBlock().getClass();
    }
	
	/**
	 * 获取电力。
	 * @return 当前蓄电池的电力。
	 */
	private int getElectricity(){
		int ret = 0;
		ItemStack stack = inv[0];
		if (stack != null && stack.getItem() instanceof ItemBattery){
			ret = stack.getMaxDamage() - stack.getItemDamage();
		}
		return ret;
	}
	
	/**
	 * 获取需要的电力。
	 * @return 当前蓄电需要池的电力。
	 */
	private int getNeedElectricity(){
		int ret = 0;
		ItemStack stack = inv[2];
		if (stack != null){
			ret = Math.max(1, stack.stackSize / 3);
			Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(inv[1]);
			if (map.size() > 0){
				ret *= 2;
			}
		}
		return ret;
	}
	
	/**
	 * 消耗电力。
	 * @param n 消耗的电量。
	 */
	private void consumeElectricity(int n){
		ItemStack stack = inv[0];
		if (stack != null && stack.getItem() instanceof ItemBattery){
			stack.attemptDamageItem(n, this.worldObj.rand);
		}
	}
	
	@Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.getElectricity();
            case 1:
                return this.cost;
            default:
                return 0;
        }
    }

	@Override
    public void setField(int id, int value)
    {	switch (id)
        {
	        case 2:
	            this.consumeElectricity(value);
	            break;
	        default:
	            break;
        }
    }
	
	@Override
	public int getFieldCount() {
		return 3;
	}

	/**
	 * 更新回收输出。
	 */
	public void updateRecycleOutput(){
		ItemStack itemstack = inv[1];
		if (itemstack == null){
			this.setInventorySlotContents(2, null);
			this.cost = 0;
            return;
        }
		
		Item item = itemstack.getItem();
		RecycleInfo info = RecycleItems.get(item);
		if (info != null){
			int dmg = itemstack.getItemDamage();			//当前损耗值
			int maxdmg = itemstack.getMaxDamage();			//最大可损耗值
			int num = maxdmg <= 0 ? info.getNumber() : (info.getNumber() * (maxdmg - dmg) / maxdmg);
			num = Math.max(1, Math.min(64, num));
			this.setInventorySlotContents(2, new ItemStack(info.getTarget(), num));
		}else{
			this.setInventorySlotContents(2, null);
		}
		this.cost = this.getNeedElectricity();
	}
}
