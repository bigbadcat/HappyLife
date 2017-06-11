package org.xuxiang.happylife.inventory;

import org.xuxiang.happylife.item.ItemBattery;
import org.xuxiang.happylife.tileentity.TileEntityGenerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * 发电机工作容器。
 * @author XuXiang
 *
 */
public class ContainerGenerator extends Container{

	protected TileEntityGenerator tileEntity;
	
	public ContainerGenerator (InventoryPlayer inventoryPlayer, TileEntityGenerator te, final World worldIn){
        tileEntity = te;
        bindGeneratorInventory();
        bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileEntity.isUseableByPlayer(playerIn);
	}
	
	protected void bindGeneratorInventory(){
        addSlotToContainer(new Slot(tileEntity, 0, 62, 20));
        addSlotToContainer(new Slot(tileEntity, 1, 134, 20));
        
        int y = 54;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(tileEntity, 2 + j + i * 9, 8 + j * 18, y + i * 18));
            }
        }
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
            if (index < 29){
            	//从输入输出槽移动
                if (!this.mergeItemStack(stack, 29, 65, false)){
                    return null;
                }
            }else if (index >= 29 && index < 65){
            	//从玩家物品槽移动 蓄电池往电池槽 其它往燃料槽 如果燃料槽不为空则往燃料备用槽
            	if (stack.getItem() instanceof ItemBattery){
            		if (!this.mergeItemStack(stack, 1, 2, true)){
                        return null;
                    }
            	}else{
            		if (this.inventorySlots.get(0).getStack() == null){
            			if (!this.mergeItemStack(stack, 0, 1, true)){
                            return null;
                        }
            		}else{
            			if (!this.mergeItemStack(stack, 2, 29, false)){
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
