package org.xuxiang.happylife.tileentity;

import java.util.HashSet;
import java.util.Set;

import org.xuxiang.happylife.block.BlockCultureBox;
import org.xuxiang.happylife.block.BlockGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 培养箱实体。
 * @author XuXiang
 *
 */
public class TileEntityCultureBox extends TileEntity implements ITickable, IInventory {
	private ItemStack[] inv;	//0金克拉槽 1培养源 2培养目标
	private int cultureCount;	//培养计数 变为0则增加一个培养目标
	
	private static HashSet<Item> CulturableItems;		//可培养的物品
	
	public static void registerCulturableItems(){
		CulturableItems = new HashSet<Item>();
		CulturableItems.add(Item.getItemFromBlock(Blocks.waterlily));
		CulturableItems.add(Item.getItemFromBlock(Blocks.sapling));
	}
	
	public static int CultureTime = 20 * 60 * 2;
	
	
	//判断是否可培养
	public static boolean isCulturable(Item item){
		return CulturableItems.contains(item);
	}
	
	//判断是否肥料
	public static boolean isManure(ItemStack stack){
		return stack.getItem() == Items.dye && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.WHITE;
	}
	
	public TileEntityCultureBox(){
		inv = new ItemStack[3];
		cultureCount = 0;
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
		
		cultureCount = compound.getInteger("CultureCount");

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
		
		compound.setInteger("CultureCount", cultureCount);

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
		boolean dirty = false;
		if (cultureCount > 0){
			--cultureCount;
			if (cultureCount <= 0){
				cultureCount = 0;
				addCultureItem();
				if (!tryCulture()){
					BlockCultureBox.setState(false, this.worldObj, pos);
				}
				dirty = true;
			}						
		}else{
			dirty = tryCulture();
			if (dirty){
				BlockCultureBox.setState(true, this.worldObj, pos);
			}			
		}
		
		if (dirty){
			this.markDirty();
		}
	}
	
	public void stopCulture(){
		cultureCount = 0;
		BlockCultureBox.setState(false, this.worldObj, pos);
		this.markDirty();
	}
	
	public boolean tryCulture(){
		//没有肥料
		if (inv[0] == null || inv[0].stackSize <= 0){
			return false;
		}
		//没有被培养的目标
		if (inv[1] == null || inv[1].stackSize <= 0){
			return false;
		}
		//输出槽物品已满或者与培养目标不是同类型
		if (inv[2] != null)
		{
			if (inv[2].stackSize >= inv[2].getMaxStackSize()){
				return false;
			}
			boolean eq = inv[2].getItem() == inv[1].getItem() && (!inv[1].getHasSubtypes() || inv[1].getMetadata() == inv[2].getMetadata()) && ItemStack.areItemStackTagsEqual(inv[1], inv[2]);
			if (!eq){
				return false;
			}
		}
		
		//消耗一个肥料，进入培养计时
		inv[0].stackSize = inv[0].stackSize - 1;
		if (inv[0].stackSize <= 0){
			inv[0] = null;
		}
		cultureCount = CultureTime;
		return true;
	}
	
	//添加培养物品
	public void addCultureItem(){
		if (inv[2] != null)
		{
			if (inv[2].stackSize >= inv[2].getMaxStackSize()){
				return ;
			}
			boolean eq = inv[2].getItem() == inv[1].getItem() && (!inv[1].getHasSubtypes() || inv[1].getMetadata() == inv[2].getMetadata()) && ItemStack.areItemStackTagsEqual(inv[1], inv[2]);
			if (!eq){
				return ;
			}
		}
		
		if (inv[2] != null){
			inv[2].stackSize = inv[2].stackSize + 1;
		}else{
			inv[2] = inv[1].copy();
			inv[2].stackSize = 1;
		}
	}
	
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock().getClass() != newSate.getBlock().getClass();
    }
	
	@Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.cultureCount;
            default:
                return 0;
        }
    }

	@Override
    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.cultureCount = value;
                break;
            default:
            	break;
        }
    }
	
	@Override
	public int getFieldCount() {
		return 1;
	}
	
	@SideOnly(Side.CLIENT)
    public static boolean isRunning(IInventory inv)
    {
        return inv.getField(0) > 0;
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
