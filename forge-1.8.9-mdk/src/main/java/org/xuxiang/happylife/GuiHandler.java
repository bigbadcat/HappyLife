package org.xuxiang.happylife;

import org.xuxiang.happylife.client.gui.*;
import org.xuxiang.happylife.inventory.*;
import org.xuxiang.happylife.tileentity.TileEntityDigger;
import org.xuxiang.happylife.tileentity.TileEntityFiller;
import org.xuxiang.happylife.tileentity.TileEntityGenerator;
import org.xuxiang.happylife.tileentity.TileEntityRecycle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	static public final int ID_RECYCLE = 1;
	static public final int ID_FILLER = 2;
	static public final int ID_DIGGER = 3;
	static public final int ID_GENERATOR = 4;
	
    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if(id == ID_RECYCLE){
            return new ContainerRecycle(player.inventory, (TileEntityRecycle)tileEntity, world);
        }else if(id == ID_FILLER){
        	return new ContainerFiller(player.inventory, (TileEntityFiller)tileEntity, world);
        }else if(id == ID_DIGGER){
        	return new ContainerDigger(player.inventory, (TileEntityDigger)tileEntity, world);
        }else if(id == ID_GENERATOR){
        	return new ContainerGenerator(player.inventory, (TileEntityGenerator)tileEntity, world);
        }
        return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if(id == ID_RECYCLE){
        	return new GuiRecycle(player.inventory, (TileEntityRecycle)tileEntity, world);
        }else if(id == ID_FILLER){
        	return new GuiFiller(player.inventory, (TileEntityFiller)tileEntity, world);
        }else if(id == ID_DIGGER){
        	return new GuiDigger(player.inventory, (TileEntityDigger)tileEntity, world);
        }else if(id == ID_GENERATOR){
        	return new GuiGenerator(player.inventory, (TileEntityGenerator)tileEntity, world);
        }
        return null;

    }
}
