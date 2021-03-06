package org.xuxiang.happylife.client.renderer.entity;

import org.lwjgl.opengl.GL11;
import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.tileentity.TileEntityFiller;
import org.xuxiang.happylife.util.RendererUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityRendererFiller extends TileEntitySpecialRenderer<TileEntityFiller>{
	
	private static final ResourceLocation linetex = new ResourceLocation(HappyLife.MODID + ":textures/entity/conveyor.png");
	
	
	/**
	 * 构造函数。
	 */
	public TileEntityRendererFiller(){	
	}
	
	@Override
	public void renderTileEntityAt(TileEntityFiller te, double x, double y, double z, float partialTicks, int destroyStage) {
		this.bindTexture(linetex);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableLighting();
		
		if (te.isPowered() && te.getFillCount() > 0){
			double d0 = (double)te.getWorld().getTotalWorldTime() + (double)partialTicks;
	        double d1 = MathHelper.func_181162_h(-d0 * 0.2D - (double)MathHelper.floor_double(-d0 * 0.1D));
	        float r = 1 - (float)d1;	        
	        int px = te.getPutX() - te.getPos().getX();
	        int py = te.getPutY() - te.getPos().getY();
	        int pz = te.getPutZ() - te.getPos().getZ();	        
	        RendererUtil.drawPolyline(0, 0, 0, px, py, pz, r);
		}
		
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();

    }
	
	@Override
	public boolean func_181055_a()
    {
        return true;
    }
}
