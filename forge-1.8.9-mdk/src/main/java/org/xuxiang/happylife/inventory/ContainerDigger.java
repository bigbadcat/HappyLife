package org.xuxiang.happylife.inventory;

import org.xuxiang.happylife.item.ItemBattery;
import org.xuxiang.happylife.tileentity.TileEntityDigger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

/**
 * 填充器工作容器。
 * @author XuXiang
 *
 */
public class ContainerDigger extends Container{
	
	protected TileEntityDigger tileEntity;
	
	public ContainerDigger (InventoryPlayer inventoryPlayer, TileEntityDigger te, final World worldIn){
        tileEntity = te;
        bindDiggerInventory();
        bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileEntity.isUseableByPlayer(playerIn);
	}
	
	protected void bindDiggerInventory(){
		int y = 54;
		int x = 8;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(tileEntity, j + i * 9, 8 + j * 18, y + i * 18));
            }
        }
        
        x = 98;
        y = 20;
        for (int i = 0; i < 4; ++i){
        	addSlotToContainer(new Slot(tileEntity, 27 + i, x + i * 18, y));
        }
        
        addSlotToContainer(new Slot(tileEntity, 31, 62, 20));
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
            if (index <32){
            	//从挖掘机移动            	
                if (!this.mergeItemStack(stack, 32, 68, false)){
                    return null;
                }
            }else if (index >= 32 && index <68){            	
            	//从玩家槽 移动区分蓄电池
            	if (stack.getItem() instanceof ItemBattery){
            		if (!this.mergeItemStack(stack, 31, 32, false)){
	                    return null;
	                }
            	}else{
            		//工具优先工具槽
                	if (stack.getItem() instanceof ItemTool){
                		if (!this.mergeItemStack(stack, 27, 31, false)){
                            return null;
                        }
                	}

                	if (stack.stackSize > 0){
                		if (!this.mergeItemStack(stack, 0, 27, false)){
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
