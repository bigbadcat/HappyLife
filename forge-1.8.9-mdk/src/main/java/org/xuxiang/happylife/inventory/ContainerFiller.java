package org.xuxiang.happylife.inventory;

import org.xuxiang.happylife.item.ItemBattery;
import org.xuxiang.happylife.tileentity.TileEntityFiller;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * 填充器工作容器。
 * @author XuXiang
 *
 */
public class ContainerFiller extends Container{
	
	protected TileEntityFiller tileEntity;
	
	public ContainerFiller (InventoryPlayer inventoryPlayer, TileEntityFiller te, final World worldIn){
        tileEntity = te;
        bindFillerInventory();
        bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileEntity.isUseableByPlayer(playerIn);
	}
	
	protected void bindFillerInventory(){
		int y = 54;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(tileEntity, j + i * 9, 8 + j * 18, y + i * 18));
            }
        }
        addSlotToContainer(new Slot(tileEntity, 27, 62, 20));
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		int y = 122;
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
            if (index <28){
            	//从填充器物品槽和电池槽移动
            	if (!this.mergeItemStack(stack, 28, 64, false)){
                    return null;
                }
            }else if (index >= 28 && index <64){
                //从玩家槽 移动区分蓄电池
            	if (stack.getItem() instanceof ItemBattery){
            		if (!this.mergeItemStack(stack, 27, 28, false)){
	                    return null;
	                }
            	}else{
	        		if (!this.mergeItemStack(stack, 0, 27, false)){
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
}
