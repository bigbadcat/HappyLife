package org.xuxiang.happylife.client.gui;

import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.inventory.ContainerDigger;
import org.xuxiang.happylife.tileentity.TileEntityDigger;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 挖掘机工作界面。
 * @author XuXiang
 *
 */
@SideOnly(Side.CLIENT)
public class GuiDigger extends GuiContainer {
	
	/** GUI资源。*/
	private static final ResourceLocation fillerResource = new ResourceLocation(HappyLife.MODID + ":textures/gui/container/Digger.png");
	
	/**
	 * 构造函数。
	 * @param inventoryPlayer 玩家仓库。
	 * @param tileEntity 挖掘机实体。
	 * @param worldIn 所在世界对象。
	 */
	public GuiDigger (InventoryPlayer inventoryPlayer, TileEntityDigger tileEntity, final World worldIn) {
	    super(new ContainerDigger(inventoryPlayer, tileEntity, worldIn));
	    this.ySize = 204;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(fillerResource);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}	
}
