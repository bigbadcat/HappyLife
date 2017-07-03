package org.xuxiang.happylife.client.gui;

import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.inventory.ContainerCultureBox;
import org.xuxiang.happylife.inventory.ContainerRecycle;
import org.xuxiang.happylife.tileentity.TileEntityCultureBox;
import org.xuxiang.happylife.tileentity.TileEntityGenerator;
import org.xuxiang.happylife.tileentity.TileEntityRecycle;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 培养箱工作界面。
 * @author XuXiang
 *
 */
@SideOnly(Side.CLIENT)
public class GuiCultureBox  extends GuiContainer{

	/** GUI资源。*/
	private static final ResourceLocation Resource = new ResourceLocation(HappyLife.MODID + ":textures/gui/container/CultureBox.png");
	
	private IInventory tileCultureBox;
	
	/**
	 * 构造函数。
	 * @param inventoryPlayer 玩家仓库。
	 * @param worldIn 所在世界对象。
	 */
	public GuiCultureBox (InventoryPlayer inventoryPlayer, TileEntityCultureBox te, final World worldIn) {
	    super(new ContainerCultureBox(inventoryPlayer, te, worldIn));
	    this.ySize = 146;
	    this.tileCultureBox = te;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resource);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        
        if (TileEntityCultureBox.isRunning(this.tileCultureBox)){
        	int k = this.getCultureScaled1(20);
        	this.drawTexturedModalRect(i + 123, j + 22, 176, 0, k, 3); //上
        	
        	int l = this.getCultureScaled2(20);
        	this.drawTexturedModalRect(i + 123, j + 30, 176, 0, l, 3);	//下
        }
	}
	
	private int getCultureScaled1(int pixels){
		int j = TileEntityCultureBox.CultureTime/20;
        int i = this.tileCultureBox.getField(0) % j;        
        return (j-i) * pixels / j;
    }
	
	private int getCultureScaled2(int pixels){
        int i = this.tileCultureBox.getField(0) * 20 / TileEntityCultureBox.CultureTime;
        int j = 20;
        return (j-i) * pixels / j;
    }
}
