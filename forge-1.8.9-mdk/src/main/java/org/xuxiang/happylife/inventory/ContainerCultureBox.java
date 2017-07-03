package org.xuxiang.happylife.inventory;

import org.xuxiang.happylife.item.ItemBattery;
import org.xuxiang.happylife.tileentity.TileEntityCultureBox;
import org.xuxiang.happylife.tileentity.TileEntityRecycle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

/**
 * 培养箱工作容器。
 * @author XuXiang
 *
 */
public class ContainerCultureBox extends Container {
	
	protected TileEntityCultureBox tileEntity;
	
	public ContainerCultureBox(InventoryPlayer inventoryPlayer, TileEntityCultureBox te, final World worldIn){
		this.tileEntity = te;
        this.addSlotToContainer(new Slot(this.tileEntity, 0, 62, 20)
        {
        	/**
             * 判断是否可放入物品。
             */
            public boolean isItemValid(ItemStack stack)
            {
                return TileEntityCultureBox.isManure(stack);
            }
        });
        this.addSlotToContainer(new Slot(this.tileEntity, 1, 98, 20)
        {
        	/**
             * 判断是否可放入物品。
             */
            public boolean isItemValid(ItemStack stack)
            {
                return TileEntityCultureBox.isCulturable(stack.getItem());
            }
            
            /**
             * 当物品被拿走时。
             */
            public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
            {
            	ContainerCultureBox.this.tileEntity.stopCulture();
            }
        });
        
        this.addSlotToContainer(new Slot(this.tileEntity, 2, 152, 20)
        {
        	/**
             * 判断是否可放入物品。
             */
            public boolean isItemValid(ItemStack stack)
            {
                return false;
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
            if (index <3){
            	//从培养箱移动            	
                if (!this.mergeItemStack(stack, 3, 39, false)){
                    return null;
                }
            }else if (index >= 3 && index <39){            	
            	//金克拉移往对应的槽
            	if (TileEntityCultureBox.isManure(stack)){
            		if (!this.mergeItemStack(stack, 0, 1, false)){
	                    return null;
	                }
            	}else{
                	if (stack.stackSize > 0){
                		if (!this.mergeItemStack(stack, 1, 2, false)){
                            return null;
                        }
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
}
