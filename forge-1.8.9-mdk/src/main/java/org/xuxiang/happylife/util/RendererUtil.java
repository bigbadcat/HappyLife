package org.xuxiang.happylife.util;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;

/**
 * 渲染工具集。
 * @author XuXiang
 *
 */
public class RendererUtil {
	
	private static ArrayList<BlockPos> Points = new ArrayList<BlockPos>();
	
	public static void drawPolyline(int x1, int y1, int z1, int x2, int y2, int z2, float rate){
		//要绘制的点列表
		Points.clear();
		BlockPos curpos = new BlockPos(x1, y1, z1);
		if (curpos.getX() != x2){
			drawLineX(curpos.getX(), curpos.getY(), curpos.getZ(), x2, 1, rate);
			curpos = new BlockPos(x2, curpos.getY(), curpos.getZ());
			Points.add(curpos);
		}
		if (curpos.getZ() != z2){
			drawLineZ(curpos.getX(), curpos.getY(), curpos.getZ(), z2, 1, rate);
			curpos = new BlockPos(curpos.getX(), curpos.getY(), z2);
			Points.add(curpos);
		}
		if (curpos.getY() != y2){
			drawLineY(curpos.getX(), curpos.getY(), curpos.getZ(), y2, 1, rate);
			curpos = new BlockPos(curpos.getX(), y2, curpos.getZ());
			Points.add(curpos);
		}
		
		
		for (int i=0; i<Points.size(); ++i){
			BlockPos pos = Points.get(i);
			float s = i == Points.size() - 1 ? 1.5f : 1;
			drawPoint(pos.getX(), pos.getY(), pos.getZ(), s, rate);
		}
	}
	
	public static void drawPoint(int x, int y, int z, float scale, float rate){
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        //方块中间
        float mx = x + 0.5f;
        float my = y + 0.5f;
        float mz = z + 0.5f;
        float offset = 0.5f * scale / 2;
        float minx = mx - offset;
        float maxx = mx + offset;
        float miny = my - offset;
        float maxy = my + offset;
        float minz = mz - offset;
        float maxz = mz + offset;
        float tu = rate;
        float td = 1 + rate;
        
        //绘制6个面(逆时针旋转为正)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        //下
		worldrenderer.pos(minx, miny, minz).tex(0.5, 0).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, miny, minz).tex(1, 0).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, miny, maxz).tex(1, 1).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, miny, maxz).tex(0.5, 1).color(1, 1, 1, 1.0F).endVertex();
		
		//上
		worldrenderer.pos(minx, maxy, minz).tex(0.5, 0).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, maxy, maxz).tex(0.5, 1).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, maxz).tex(1, 1).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, minz).tex(1, 0).color(1, 1, 1, 1.0F).endVertex();
		
		//左
		worldrenderer.pos(minx, miny, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, miny, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, maxy, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, maxy, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
		
		//右
		worldrenderer.pos(maxx, miny, minz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, minz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, maxz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, miny, maxz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
		
		//前
		worldrenderer.pos(minx, miny, maxz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, miny, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, maxy, maxz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
		
		//后
		worldrenderer.pos(minx, miny, minz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, maxy, minz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, miny, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
		
        tessellator.draw();
	}
	
	public static void drawLineX(int x, int y, int z, int x2, float scale, float rate){
		if (x == x2){
			return;
		}
		
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        //方块中间
        float mx = x + 0.5f;
        float mx2 = x2 + 0.5f;
        float my = y + 0.5f;        
        float mz = z + 0.5f;
        float offset = 0.2f * scale / 2;
        float minx = Math.min(mx, mx2) - offset;
        float maxx = Math.max(mx, mx2) + offset;
        float miny = my - offset;
        float maxy = my + offset;
        float minz = mz - offset;
        float maxz = mz + offset;
        float tu = rate;
        float td = 1 + rate + Math.abs(x - x2);
        if (x > x2){
        	float temp = tu;
        	tu = td;
        	td = temp;
        }
        
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        //下
  		worldrenderer.pos(minx, miny, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, miny, minz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, miny, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, miny, maxz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
  		
  		//上
  		worldrenderer.pos(minx, maxy, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, maxy, maxz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, maxy, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, maxy, minz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();

  		//前
  		worldrenderer.pos(minx, miny, maxz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, miny, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, maxy, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, maxy, maxz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
  		
  		//后
  		worldrenderer.pos(minx, miny, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, maxy, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, maxy, minz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, miny, minz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
        
        tessellator.draw();
	}
	
	public static void drawLineY(int x, int y, int z, int y2, float scale, float rate){
		if (y == y2){
			return;
		}
		
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        //方块中间
        float mx = x + 0.5f;
        float my = y + 0.5f;
        float my2 = y2 + 0.5f;
        float mz = z + 0.5f;
        float offset = 0.2f * scale / 2;
        float minx = mx - offset;
        float maxx = mx + offset;
        float miny = Math.min(my, my2) - offset;
        float maxy = Math.max(my, my2) + offset;
        float minz = mz - offset;
        float maxz = mz + offset;
        float tu = rate;
        float td = 1 + rate + Math.abs(y - y2);
        if (y > y2){
        	float temp = tu;
        	tu = td;
        	td = temp;
        }
        
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        //左
  		worldrenderer.pos(minx, miny, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, miny, maxz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, maxy, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, maxy, minz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
  		
  		//右
  		worldrenderer.pos(maxx, miny, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, maxy, minz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, maxy, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, miny, maxz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
  		
  		//前
  		worldrenderer.pos(minx, miny, maxz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, miny, maxz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, maxy, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, maxy, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
  		
  		//后
  		worldrenderer.pos(minx, miny, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(minx, maxy, minz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, maxy, minz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
  		worldrenderer.pos(maxx, miny, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
        
        tessellator.draw();
	}
	
	public static void drawLineZ(int x, int y, int z, int z2, float scale, float rate){
		if (z == z2){
			return;
		}
		
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        //方块中间
        float mx = x + 0.5f;        
        float my = y + 0.5f;        
        float mz = z + 0.5f;
        float mz2 = z2 + 0.5f;
        float offset = 0.2f * scale / 2;
        float minx = mx - offset;
        float maxx = mx + offset;
        float miny = my - offset;
        float maxy = my + offset;
        float minz = Math.min(mz, mz2) - offset;
        float maxz = Math.max(mz, mz2) + offset;
        float tu = rate;
        float td = 1 + rate + Math.abs(z - z2);
        if (z > z2){
        	float temp = tu;
        	tu = td;
        	td = temp;
        }
        
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        //下
		worldrenderer.pos(minx, miny, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, miny, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, miny, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, miny, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
		
		//上
		worldrenderer.pos(minx, maxy, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, maxy, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
		
		//左
		worldrenderer.pos(minx, miny, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, miny, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, maxy, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(minx, maxy, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
		
		//右
		worldrenderer.pos(maxx, miny, minz).tex(0, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, minz).tex(0.5, td).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, maxy, maxz).tex(0.5, tu).color(1, 1, 1, 1.0F).endVertex();
		worldrenderer.pos(maxx, miny, maxz).tex(0, tu).color(1, 1, 1, 1.0F).endVertex();

        tessellator.draw();
	}
}
