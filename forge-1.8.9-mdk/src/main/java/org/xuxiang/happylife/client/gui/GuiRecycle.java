package org.xuxiang.happylife.client.gui;

import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.inventory.ContainerRecycle;
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
 * 回收台工作界面。
 * @author XuXiang
 *
 */
@SideOnly(Side.CLIENT)
public class GuiRecycle extends GuiContainer{
	
	/** GUI资源。*/
	private static final ResourceLocation recycleResource = new ResourceLocation(HappyLife.MODID + ":textures/gui/container/Recycle.png");
	
	private IInventory tileRecycle;
	
	/**
	 * 构造函数。
	 * @param inventoryPlayer 玩家仓库。
	 * @param worldIn 所在世界对象。
	 */
	public GuiRecycle (InventoryPlayer inventoryPlayer, TileEntityRecycle te, final World worldIn) {
	    super(new ContainerRecycle(inventoryPlayer, te, worldIn));
	    this.ySize = 146;
	    this.tileRecycle = te;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(recycleResource);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}
	
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();

        int need = tileRecycle.getField(1);
        if (need > 0){
        	boolean cm = this.mc.thePlayer.capabilities.isCreativeMode;
        	int have = tileRecycle.getField(0);        	
        	String text = "E-" + need; //16736352 8453920
        	int dx = this.xSize - 22;
        	int dy = 38;
        	int color = (cm || need <= have) ? 8453920 : 16736352;
        	this.fontRendererObj.drawString(text, dx, dy, color);
        }
        GlStateManager.enableLighting();
    }
}
