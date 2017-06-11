package org.xuxiang.happylife.inventory;

import org.xuxiang.happylife.item.ItemBattery;
import org.xuxiang.happylife.tileentity.TileEntityRecycle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * 回收台工作容器。
 * @author XuXiang
 *
 */
public class ContainerRecycle extends Container{

	protected TileEntityRecycle tileEntity;

	public ContainerRecycle (InventoryPlayer inventoryPlayer, TileEntityRecycle te, final World worldIn){
		this.tileEntity = te;
        this.addSlotToContainer(new Slot(this.tileEntity, 0, 62, 20));
        this.addSlotToContainer(new Slot(this.tileEntity, 1, 98, 20));
        this.addSlotToContainer(new Slot(this.tileEntity, 2, 152, 20)
        {
        	/**
             * 判断是否可放入物品。
             */
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
            
        	/**
             * 判断是否可以拿走槽内的物品。
             */
            public boolean canTakeStack(EntityPlayer playerIn)
            {
            	//需要的电量不超过剩余的电量
            	boolean cm = playerIn.capabilities.isCreativeMode;
                return cm || ContainerRecycle.this.tileEntity.getField(1) <= ContainerRecycle.this.tileEntity.getField(0);
            }
            
            /**
             * 但物品被拿走时。
             */
            public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
            {
            	if (!playerIn.capabilities.isCreativeMode)
                {
            		ContainerRecycle.this.tileEntity.setField(2, ContainerRecycle.this.tileEntity.getField(1));
                }
            	
            	ItemStack itemstack = ContainerRecycle.this.tileEntity.getStackInSlot(1);
            	if (itemstack != null){
            		if (itemstack.stackSize > 1){
                		itemstack.stackSize -= 1;
                		ContainerRecycle.this.tileEntity.setInventorySlotContents(1, itemstack);
                	}else{
                		ContainerRecycle.this.tileEntity.setInventorySlotContents(1, null);
                	}
            	}            	
            }
        });

        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
        return true;
	}


	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		int y = 54;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, y + i * 18));
            }
        }

        y += 58;
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, y));
        }
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {		
		ItemStack ret = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()){
            ItemStack stack = slot.getStack();
            ret = stack.copy();
            if (index == 0 || index == 1 || index == 2){
            	//从输入输出槽移动
                if (!this.mergeItemStack(stack, 3, 39, false)){
                    return null;
                }
            }else if (index >= 3 && index < 39){
            	//从玩家物品槽移动 电池往电池槽 物品往分解槽
            	if (stack.getItem() instanceof ItemBattery){
            		if (!this.mergeItemStack(stack, 0, 1, true)){
                        return null;
                    }
            	}else{
            		if (!this.mergeItemStack(stack, 1, 2, true)){
                        return null;
                    }
            	}                
            }

            if (stack.stackSize == 0){
                slot.putStack((ItemStack)null);
            }else{
                slot.onSlotChanged();
            }

            if (stack.stackSize == ret.stackSize){
                return null;
            }
            slot.onPickupFromSlot(player, stack);
        }

        return ret;
	}

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        ItemStack itemstack = this.tileEntity.getStackInSlot(1);
        if (itemstack != null) {
            playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            this.tileEntity.setInventorySlotContents(1, null);
        }        
        this.tileEntity.setInventorySlotContents(2, null);
    }
}
