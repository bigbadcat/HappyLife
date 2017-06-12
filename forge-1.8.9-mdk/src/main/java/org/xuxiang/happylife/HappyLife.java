package org.xuxiang.happylife;

import org.xuxiang.happylife.block.*;
import org.xuxiang.happylife.client.renderer.entity.RenderFactoryWitherBomb;
import org.xuxiang.happylife.client.renderer.entity.TileEntityRendererDigger;
import org.xuxiang.happylife.client.renderer.entity.TileEntityRendererFiller;
import org.xuxiang.happylife.entity.EntityWitherBomb;
import org.xuxiang.happylife.item.*;
import org.xuxiang.happylife.tileentity.TileEntityDigger;
import org.xuxiang.happylife.tileentity.TileEntityFiller;
import org.xuxiang.happylife.tileentity.TileEntityGenerator;
import org.xuxiang.happylife.tileentity.TileEntityRecycle;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Mod类。
 * @author XuXiang
 *
 */
@Mod(modid = HappyLife.MODID, name = HappyLife.NAME, version = HappyLife.VERSION)
public class HappyLife {

	//MOD信息
	public static final String MODID = "happylife";
	public static final String NAME = "Happy Life";
    public static final String VERSION = "1.0.3";

    @Instance(HappyLife.MODID)
    static public HappyLife instance;

    static public CreativeTabs tabHappyLife;

    //方块
  	static public Block Recycle;
  	static public Block Chain;
  	static public Block Filler;
  	static public Block FillerOn;
  	static public Block Digger;
  	static public Block DiggerOn;
  	static public Block Generator;
  	static public Block GeneratorBurn;
  	static public Block SpringBed;

	//物品
	static public Item IronNugget;
	static public Item LeatherNugget;
	static public Item DiamondNugget;
	static public Item ChainNet;
	static public Item ChainNugget;
	static public Item WitherBomb;
	static public Item Battery;
	static public Item BatteryV2;

	/**
	 * Mod预加载。从1.7开始,砖块和物品的初始化必须在这个阶段进行!
	 * @param event 加载参数。
	 */
	@EventHandler
	public void preLoad(FMLPreInitializationEvent event)
	{
		//初始化创造栏
		tabHappyLife = new CreativeTabs(12, MODID){
	        @SideOnly(Side.CLIENT)
	        public Item getTabIconItem(){
	            return Item.getItemFromBlock(Recycle);
	        }
	    };

		//初始化方块
		Recycle = new BlockRecycle();
		Chain = new BlockChain();
		Filler = new BlockFiller(false);
		FillerOn = new BlockFiller(true);
		Digger = new BlockDigger(false);
		DiggerOn = new BlockDigger(true);
		Generator = new BlockGenerator(false);
		GeneratorBurn = new BlockGenerator(true);
		SpringBed = new BlockSpringBed();

		//初始化物品
		IronNugget = new ItemIronNugget();
		LeatherNugget = new ItemLeatherNugget();
		DiamondNugget = new ItemDiamondNugget();
		ChainNet = new ItemChainNet();
		ChainNugget = new ItemChainNugget();
		WitherBomb = new ItemWitherBomb();
		Battery = new ItemBattery();	
		BatteryV2 = new ItemBattery(2);

		//界面和实体注册
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		GameRegistry.registerTileEntity(TileEntityFiller.class, "TileEntityFiller");
		GameRegistry.registerTileEntity(TileEntityDigger.class, "TileEntityDigger");
		GameRegistry.registerTileEntity(TileEntityGenerator.class, "TileEntityGenerator");
		GameRegistry.registerTileEntity(TileEntityRecycle.class, "TileEntityRecycle");
		EntityRegistry.registerModEntity(EntityWitherBomb.class, "EntityWitherBomb", 1, this, 64, 3, true);

    	if(event.getSide() == Side.CLIENT){
    		//实体
    		RenderingRegistry.registerEntityRenderingHandler(EntityWitherBomb.class, new RenderFactoryWitherBomb());
    		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFiller.class, new TileEntityRendererFiller());
    		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDigger.class, new TileEntityRendererDigger());
    	}
	}

	/**
	 * Mod加载。配置Mod设置，添加合成表。Mod间通讯(通过FMLInterModComms类完成)在此时进行。
	 * @param event 加载参数。
	 */
	@EventHandler
	public void load(FMLInitializationEvent event){
		//合成表
		GameRegistry.addRecipe(new ItemStack(Items.iron_ingot), new Object[] {"AAA", "AAA", "AAA", 'A', IronNugget});
		GameRegistry.addRecipe(new ItemStack(IronNugget, 9), new Object[] {"A", 'A', Items.iron_ingot});
		GameRegistry.addRecipe(new ItemStack(Items.leather), new Object[] {"AAA", "AAA", "AAA", 'A', LeatherNugget});
		GameRegistry.addRecipe(new ItemStack(LeatherNugget, 9), new Object[] {"A", 'A', Items.leather});
		GameRegistry.addRecipe(new ItemStack(Items.diamond), new Object[] {"AAA", "AAA", "AAA", 'A', DiamondNugget});
		GameRegistry.addRecipe(new ItemStack(DiamondNugget, 9), new Object[] {"A", 'A', Items.diamond});
		GameRegistry.addRecipe(new ItemStack(ChainNet), new Object[] {"AAA", "AAA", "AAA", 'A', ChainNugget});
		GameRegistry.addRecipe(new ItemStack(ChainNugget, 9), new Object[] {"A", 'A', ChainNet});
		GameRegistry.addRecipe(new ItemStack(Chain), new Object[] {"AAA", "AAA", "AAA", 'A', ChainNet});
		GameRegistry.addRecipe(new ItemStack(ChainNet, 9), new Object[] {"A", 'A', Chain});
		GameRegistry.addRecipe(new ItemStack(WitherBomb), new Object[] {"ABA", "BCB", "ABA", 'A', Items.iron_ingot, 'B', Items.gunpowder, 'C', new ItemStack(Items.skull, 1, 1)});
		GameRegistry.addRecipe(new ItemStack(Items.chainmail_helmet), new Object[] {"AAA", "A A", 'A', ChainNet});
		GameRegistry.addRecipe(new ItemStack(Items.chainmail_chestplate), new Object[] {"A A", "AAA", "AAA", 'A', ChainNet});
		GameRegistry.addRecipe(new ItemStack(Items.chainmail_leggings), new Object[] {"AAA", "A A", "A A", 'A', ChainNet});
		GameRegistry.addRecipe(new ItemStack(Items.chainmail_boots), new Object[] {"A A", "A A", 'A', ChainNet});
		GameRegistry.addRecipe(new ItemStack(Recycle), new Object[] {" A ", "BCB", "DED", 'A', Items.diamond, 'B', Items.redstone, 'C', Blocks.crafting_table, 'D', new ItemStack(Items.dye, 1, 4), 'E', Items.iron_ingot});
		GameRegistry.addRecipe(new ItemStack(Filler), new Object[] {"ABA", "CDC", "EEE", 'A', Items.quartz, 'B', Items.diamond, 'C', Items.redstone, 'D', Blocks.chest, 'E', Items.iron_ingot});
		GameRegistry.addRecipe(new ItemStack(Digger), new Object[] {"ABA", "CDC", "EEE", 'A', Items.diamond, 'B', Items.quartz, 'C', Items.redstone, 'D', Blocks.chest, 'E', Items.iron_ingot});
		GameRegistry.addRecipe(new ItemStack(Battery), new Object[] {"A B", "CDE", "FFF", 'A', Items.iron_ingot, 'B', Items.gold_ingot, 'C', Items.redstone, 'D', Items.diamond, 'E', Items.quartz, 'F', Blocks.stone_pressure_plate});
		GameRegistry.addRecipe(new ItemStack(BatteryV2), new Object[] {"ABC", "BDB", "EBF", 'A', Items.iron_ingot, 'B', Battery, 'C', Items.gold_ingot, 'D', Items.diamond, 'E', Items.redstone, 'F', Items.quartz});
		GameRegistry.addRecipe(new ItemStack(Generator), new Object[] {"A B", "CDE", "AFA", 'A', Items.iron_ingot, 'B', Items.gold_ingot, 'C', Items.redstone, 'D', Items.diamond, 'E', Items.quartz, 'F', Blocks.furnace});
		GameRegistry.addRecipe(new ItemStack(SpringBed), new Object[] {"AAA", "BBB", "CDC", 'A', Blocks.wool, 'B', Items.wheat, 'C', Blocks.planks, 'D', Items.iron_ingot});
		TileEntityRecycle.initRecycleItems();

		//注册渲染
    	if(event.getSide() == Side.CLIENT){
    		//方块
    		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    		mesher.register(Item.getItemFromBlock(Recycle), 0, new ModelResourceLocation(MODID + ":" + BlockRecycle.Name, "inventory"));
    		mesher.register(Item.getItemFromBlock(Chain), 0, new ModelResourceLocation(MODID + ":" + BlockChain.Name, "inventory"));
    		mesher.register(Item.getItemFromBlock(Filler), 0, new ModelResourceLocation(MODID + ":" + ((BlockFiller) Filler).getName(), "inventory"));
    		mesher.register(Item.getItemFromBlock(Digger), 0, new ModelResourceLocation(MODID + ":" + ((BlockDigger)Digger).getName(), "inventory"));
    		mesher.register(Item.getItemFromBlock(Generator), 0, new ModelResourceLocation(MODID + ":" + ((BlockGenerator)Generator).getName(), "inventory"));
    		mesher.register(Item.getItemFromBlock(SpringBed), 0, new ModelResourceLocation(MODID + ":" + BlockSpringBed.Name, "inventory"));

    		//物品
    		mesher.register(IronNugget, 0, new ModelResourceLocation(MODID + ":" + ItemIronNugget.Name, "inventory"));
    		mesher.register(LeatherNugget, 0, new ModelResourceLocation(MODID + ":" + ItemLeatherNugget.Name, "inventory"));
    		mesher.register(DiamondNugget, 0, new ModelResourceLocation(MODID + ":" + ItemDiamondNugget.Name, "inventory"));
    		mesher.register(ChainNet, 0, new ModelResourceLocation(MODID + ":" + ItemChainNet.Name, "inventory"));
    		mesher.register(ChainNugget, 0, new ModelResourceLocation(MODID + ":" + ItemChainNugget.Name, "inventory"));
    		mesher.register(WitherBomb, 0, new ModelResourceLocation(MODID + ":" + ItemWitherBomb.Name, "inventory"));
    		mesher.register(Battery, 0, new ModelResourceLocation(MODID + ":" + ItemBattery.Name, "inventory"));
    		mesher.register(BatteryV2, 0, new ModelResourceLocation(MODID + ":" + ItemBattery.NameV2, "inventory"));
    	}
	}

	/**
	 * Mod加载完毕后。该载入的Mod都已经载入，为实现Mod间联动的操作做准备。
	 * @param event 加载参数。
	 */
	@EventHandler
	public void postLoad(FMLPostInitializationEvent event){
	}
}
