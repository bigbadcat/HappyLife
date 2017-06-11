package org.xuxiang.happylife.client.gui;

import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.inventory.ContainerGenerator;
import org.xuxiang.happylife.tileentity.TileEntityGenerator;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 充电器工作界面。
 * @author XuXiang
 *
 */
@SideOnly(Side.CLIENT)
public class GuiGenerator extends GuiContainer{
	
	/** GUI资源。*/
	private static final ResourceLocation generatorResource = new ResourceLocation(HappyLife.MODID + ":textures/gui/container/Generator.png");
	
	private IInventory tileGenerator;
	
	/**
	 * 构造函数。
	 * @param inventoryPlayer 玩家仓库。
	 * @param tileEntity 发电机实体。
	 * @param worldIn 所在世界对象。
	 */
	public GuiGenerator (InventoryPlayer inventoryPlayer, TileEntityGenerator tileEntity, final World worldIn) {
	    super(new ContainerGenerator(inventoryPlayer, tileEntity, worldIn));
	    this.ySize = 204;
	    this.tileGenerator = tileEntity;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(generatorResource);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        
        if (TileEntityGenerator.isBurning(this.tileGenerator)){
        	int k = this.getBurnLeftScaled(14);
            this.drawTexturedModalRect(i + 81, j + 22 + 13 - k, 176, 14 - k, 14, k + 1);
        }
        
        if (TileEntityGenerator.isChargeing(this.tileGenerator)){
	        int l = this.getChargeScaled(19);
	        this.drawTexturedModalRect(i + 105, j + 24, 176, 14, l + 1, 9);
        }
	}
	
	private int getBurnLeftScaled(int pixels){
        int i = this.tileGenerator.getField(2);
        if (i == 0){
            i = 200;
        }

        return this.tileGenerator.getField(1) * pixels / i;
    }
	
	private int getChargeScaled(int pixels){
        int i = this.tileGenerator.getField(0);
        int j = 20;
        return i * pixels / j;
    }
}
