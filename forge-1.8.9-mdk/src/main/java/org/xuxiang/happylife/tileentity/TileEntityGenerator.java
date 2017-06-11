package org.xuxiang.happylife.tileentity;

import org.xuxiang.happylife.block.BlockGenerator;
import org.xuxiang.happylife.item.ItemBattery;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 发电机实体。
 * @author XuXiang
 *
 */
public class TileEntityGenerator extends TileEntity implements ITickable, IInventory {
	private ItemStack[] inv;	//0燃料槽 1电池槽 2-28燃料备用槽
	private int burnTime;
	private int burnCount;
	private int chargeCount;

	public TileEntityGenerator(){
		inv = new ItemStack[29];
		burnTime = 0;
		chargeCount = 0;
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
		
		burnTime = compound.getInteger("BurnTime");
		burnCount = compound.getInteger("BurnCount");
		chargeCount = compound.getInteger("ChargeCount");

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
		
		compound.setInteger("BurnTime", burnTime);
		compound.setInteger("BurnCount", burnCount);
		compound.setInteger("ChargeCount", chargeCount);

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
		if (burnCount > 0){
			++chargeCount;
			if (chargeCount >= 20){
				addElectricity(1);
				chargeCount = 0;
			}				
			--burnCount;
			if (burnCount <= 0){				
				if (getNeedElectricity() > 0){
					//继续烧物品 没物品可烧了则切换为熄灭状态
					tryBurnItem();
					if (burnCount <= 0){
						BlockGenerator.setState(false, this.worldObj, pos);
						dirty = true;
					}						
				}else{
					//停止烧物品 切换方块为熄灭状态
					BlockGenerator.setState(false, this.worldObj, pos);
					dirty = true;
					chargeCount = 0;
				}
			}else if (getNeedElectricity() <= 0){
				chargeCount = 0;
			}			
		}else{
			if (getNeedElectricity() > 0){
				//开始烧物品 切换方块为燃烧状态
				tryBurnItem();
				if (burnCount > 0){
					BlockGenerator.setState(true, this.worldObj, pos);
					dirty = true;
				}
			}else{
				chargeCount = 0;
			}
		}
		
		if (dirty){
			this.markDirty();
		}
	}

	/**
	 * 获取需要的电力。
	 * @return 当前蓄电需要池的电力。
	 */
	private int getNeedElectricity(){
		int ret = 0;
		ItemStack stack = inv[1];
		if (stack != null && stack.getItem() instanceof ItemBattery){
			ret = stack.getItemDamage();
		}
		return ret;
	}
	
	/**
	 * 增加电量。
	 * @param n 增加的电量。
	 */
	private void addElectricity(int n){
		ItemStack stack = inv[1];
		if (stack != null && stack.getItem() instanceof ItemBattery){
			stack.setItemDamage(stack.getItemDamage() - n);
		}
	}
	
	private void tryBurnItem(){
		//看是否需要从备用区拿取燃料
		if (inv[0] == null){
			for (int i=2; i<29; ++i){
				if (inv[i] != null && TileEntityFurnace.getItemBurnTime(inv[i]) > 0){
					inv[0] = inv[i];
					inv[i] = null;
					break;
				}
			}
		}		
		if (inv[0] == null){
			burnTime = 0;
			burnCount = 0;
			return;
		}

		burnTime = TileEntityFurnace.getItemBurnTime(inv[0]);
		burnCount = burnTime;
		if (burnTime > 0){
			--inv[0].stackSize;
			if (inv[0].stackSize <= 0){
				//将铁剩余东西放到备用区的空格里 从后面开放
				inv[0] = inv[0].getItem().getContainerItem(inv[0]);
				for (int i=28; i>=2; --i){
					if (inv[i] == null){
						inv[i] = inv[0];
						inv[0] = null;
						break;
					}
				}
			}
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
                return this.chargeCount;
            case 1:
                return this.burnCount;
            case 2:
                return this.burnTime;
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
                this.chargeCount = value;
                break;
            case 1:
                this.burnCount = value;
                break;
            case 2:
                this.burnTime = value;
                break;
            default:
            	break;
        }
    }
	
	@Override
	public int getFieldCount() {
		return 3;
	}
	
	@SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inv)
    {
        return inv.getField(1) > 0;
    }
	
	@SideOnly(Side.CLIENT)
    public static boolean isChargeing(IInventory inv)
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