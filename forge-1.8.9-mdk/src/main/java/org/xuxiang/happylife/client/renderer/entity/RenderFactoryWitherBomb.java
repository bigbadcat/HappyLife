package org.xuxiang.happylife.client.renderer.entity;

import org.xuxiang.happylife.HappyLife;
import org.xuxiang.happylife.entity.EntityWitherBomb;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFactoryWitherBomb implements IRenderFactory<EntityWitherBomb> {

	@Override
	public Render<? super EntityWitherBomb> createRenderFor(RenderManager manager) {
		return new RenderWitherBomb<EntityWitherBomb>(manager, HappyLife.WitherBomb, Minecraft.getMinecraft().getRenderItem());
	}

	public class RenderWitherBomb<T extends Entity> extends Render<T> {
		protected final Item item;
	    private final RenderItem renderItem;

	    public RenderWitherBomb(RenderManager renderManagerIn, Item p_i46137_2_, RenderItem p_i46137_3_)
	    {
	        super(renderManagerIn);
	        this.item = p_i46137_2_;
	        this.renderItem = p_i46137_3_;
	    }

	    /**
	     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
	     * double d2, float f, float f1). But JAD is pre 1.5 so doe
	     */
		public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	    {
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float)x, (float)y, (float)z);
	        GlStateManager.enableRescaleNormal();
	        GlStateManager.scale(0.5F, 0.5F, 0.5F);
	        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	        this.bindTexture(TextureMap.locationBlocksTexture);
	        ItemStack itemstack = this.func_177082_d(entity);
	        IBakedModel ibakedmodel = this.renderItem.getItemModelMesher().getItemModel(itemstack);
	        this.renderItem.renderItem(this.func_177082_d(entity), ibakedmodel);
	        
	        
	        GlStateManager.disableRescaleNormal();
	        GlStateManager.popMatrix();
	        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	    }

	    public ItemStack func_177082_d(T entityIn)
	    {
	        return new ItemStack(this.item, 1, 0);
	    }

	    /**
	     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	     */
	    protected ResourceLocation getEntityTexture(Entity entity)
	    {
	        return TextureMap.locationBlocksTexture;
	    }
	}
}
