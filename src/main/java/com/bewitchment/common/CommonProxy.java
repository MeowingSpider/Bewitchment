package com.bewitchment.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bewitchment.Bewitchment;
import com.bewitchment.ModConfig;
import com.bewitchment.api.BewitchmentAPI;
import com.bewitchment.api.capability.extendedplayer.ExtendedPlayer;
import com.bewitchment.api.capability.extendedplayer.ExtendedPlayerHandler;
import com.bewitchment.api.capability.magicpower.MagicPower;
import com.bewitchment.api.capability.magicpower.MagicPowerHandler;
import com.bewitchment.api.capability.magicpower.MagicPowerMessage;
import com.bewitchment.api.registry.DistilleryRecipe;
import com.bewitchment.api.registry.LoomRecipe;
import com.bewitchment.api.registry.OvenRecipe;
import com.bewitchment.api.registry.Ritual;
import com.bewitchment.common.block.BlockGlyph.GlyphType;
import com.bewitchment.common.block.tile.entity.TileEntityGlyph;
import com.bewitchment.common.compat.thaumcraft.ThaumcraftCompat;
import com.bewitchment.common.entity.living.EntityBlindworm;
import com.bewitchment.common.entity.living.EntityLizard;
import com.bewitchment.common.entity.living.EntityNewt;
import com.bewitchment.common.entity.living.EntityOwl;
import com.bewitchment.common.entity.living.EntityRaven;
import com.bewitchment.common.entity.living.EntitySnake;
import com.bewitchment.common.entity.living.EntityToad;
import com.bewitchment.common.entity.spirits.demons.EntityAlphaHellhound;
import com.bewitchment.common.entity.spirits.demons.EntityDemon;
import com.bewitchment.common.entity.spirits.demons.EntityDemoness;
import com.bewitchment.common.entity.spirits.demons.EntityHellhound;
import com.bewitchment.common.entity.spirits.ghosts.EntityBlackDog;
import com.bewitchment.common.fortune.FortuneBadLuck;
import com.bewitchment.common.fortune.FortuneDeath;
import com.bewitchment.common.fortune.FortuneDropItem;
import com.bewitchment.common.fortune.FortuneGoodLuck;
import com.bewitchment.common.fortune.FortuneIllness;
import com.bewitchment.common.handler.BlockDropHandler;
import com.bewitchment.common.handler.EventHandler;
import com.bewitchment.common.handler.GuiHandler;
import com.bewitchment.common.ritual.*;
import com.bewitchment.common.world.gen.WorldGenBeehive;
import com.bewitchment.common.world.gen.WorldGenCoquina;
import com.bewitchment.common.world.gen.WorldGenOres;
import com.bewitchment.registry.ModObjects;
import com.bewitchment.registry.ModParticles;
import com.bewitchment.registry.util.IOreDictionaryContainer;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy
{
	public enum ModGui
	{
		APIARY, DISTILLERY, LOOM, OVEN, TAROT;
	}
	
	public static final SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(Bewitchment.MOD_ID);
	
	public ModConfig config;
	
	public final CreativeTabs tab = new CreativeTabs(Bewitchment.MOD_ID)
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ModObjects.witches_altar_red);
		}
	};
	
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new ModConfig(event.getSuggestedConfigurationFile());
		registerCapabilities();
	}
	
	public void init(FMLInitializationEvent event)
	{
		for (Block block : ForgeRegistries.BLOCKS) if (block instanceof IOreDictionaryContainer) for (String ore : ((IOreDictionaryContainer) block).getOreDictionaryNames()) OreDictionary.registerOre(ore, block);
		for (Item item : ForgeRegistries.ITEMS) if (item instanceof IOreDictionaryContainer) for (String ore : ((IOreDictionaryContainer) item).getOreDictionaryNames()) OreDictionary.registerOre(ore, item);
		NetworkRegistry.INSTANCE.registerGuiHandler(Bewitchment.instance, new GuiHandler());
		registerEventHandlers();
		registerRecipes();
		registerWorldGenerators();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		// Oven Post
		for (ItemStack stack : FurnaceRecipes.instance().getSmeltingList().keySet())
		{
			ItemStack output = FurnaceRecipes.instance().getSmeltingResult(stack);
			if (!BewitchmentAPI.REGISTRY_OVEN.getValuesCollection().parallelStream().anyMatch(r -> stack.getItem() == r.getInput().getItem() && (stack.getMetadata() == r.getInput().getMetadata() || r.getInput().getMetadata() == Short.MAX_VALUE)))
			{
				String name = stack.getItem().getRegistryName().toString();
				BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, name.substring(name.indexOf(":") + 1) + stack.getMetadata(),
						stack,
						output,
						stack.getItem() instanceof ItemFood ? new ItemStack(ModObjects.cloudy_oil) : ItemStack.EMPTY,
						0.85f));
			}
		}
		registerAltarValues();
		registerAthameValues();
		ThaumcraftCompat.loadThaumcraftCompat();
	}
	
	@SuppressWarnings("deprecation")
	public void registerValues(Block block, String name, Block base, String... oreDictionaryNames)
	{
		registerValues(block, name, base.getDefaultState().getMaterial(), base.getSoundType(), base.getBlockHardness(null, null, null), base.getExplosionResistance(null) * 5, base.getHarvestTool(base.getDefaultState()), base.getHarvestLevel(base.getDefaultState()), oreDictionaryNames);
	}
	
	@SuppressWarnings("deprecation")
	public void registerValues(Block block, String name, Material mat, SoundType sound, float hardness, float resistance, String tool, int level, String... oreDictionaryNames)
	{
		block.setRegistryName(new ResourceLocation(Bewitchment.MOD_ID, name));
		block.setTranslationKey(block.getRegistryName().toString().replace(":", "."));
		block.setCreativeTab(tab);
		ObfuscationReflectionHelper.setPrivateValue(Block.class, block, sound, 16);
		block.setHardness(hardness);
		block.setResistance(resistance);
		block.setHarvestLevel(tool, level);
		if (mat == Material.CARPET) Blocks.FIRE.setFireInfo(block, 60, 20);
		if (mat == Material.CLOTH || mat == Material.LEAVES) Blocks.FIRE.setFireInfo(block, 30, 60);
		if (mat == Material.PLANTS) Blocks.FIRE.setFireInfo(block, 60, 100);
		if (mat == Material.TNT || mat == Material.VINE) Blocks.FIRE.setFireInfo(block, 15, 100);
		if (mat == Material.WOOD) Blocks.FIRE.setFireInfo(block, 5, 20);
		if (mat == Material.ICE) block.setDefaultSlipperiness(0.98f);
		if (block instanceof IOreDictionaryContainer) for (String ore : oreDictionaryNames) ((IOreDictionaryContainer) block).getOreDictionaryNames().add(ore);
		ModObjects.REGISTRY.add(block);
	}
	
	public void registerValues(Item item, String name, String... oreDictionaryNames)
	{
		item.setRegistryName(new ResourceLocation(Bewitchment.MOD_ID, name));
		item.setTranslationKey(item.getRegistryName().toString().replace(":", "."));
		item.setCreativeTab(tab);
		if (item instanceof IOreDictionaryContainer) for (String ore : oreDictionaryNames) ((IOreDictionaryContainer) item).getOreDictionaryNames().add(ore);
		ModObjects.REGISTRY.add(item);
	}
	
	public boolean areStacksEqual(ItemStack stack0, ItemStack stack1)
	{
		return stack0.getItem() == stack1.getItem() && (stack0.getMetadata() == stack1.getMetadata() || stack1.getMetadata() == Short.MAX_VALUE);
	}
	
	public boolean isFancyGraphicsEnabled()
	{
		return false;
	}
	
	public void ignoreProperty(Block block, IProperty<?>... properties)
	{
	}
	
	public void registerTexture(Item item)
	{
	}
	
	public void registerTexture(Fluid fluid)
	{
	}
	
	public void registerTextureEyeOfOld()
	{
	}
	
	public boolean areISListsEqual(List<Ingredient> ings, List<ItemStack> stacks)
	{
		if (ings.size() == stacks.size())
		{
			List<Ingredient> foundList = new ArrayList<>(ings);
			for (int i = 0; i < stacks.size(); i++)
			{
				for (Ingredient ingredient : ings)
				{
					if (ingredient.apply(stacks.get(i)))
					{
						foundList.set(i, Ingredient.EMPTY);
						break;
					}
				}
			}
			for (Ingredient ing : foundList) if (ing != Ingredient.EMPTY) return false;
			return true;
		}
		return false;
	}
	
	public void spawnParticle(ModParticles particle, double x, double y, double z)
	{
	}
	
	protected void registerCapabilities()
	{
		byte id = 0;
		CapabilityManager.INSTANCE.register(ExtendedPlayer.class, new ExtendedPlayer(), ExtendedPlayer::new);
		MinecraftForge.EVENT_BUS.register(new ExtendedPlayerHandler());
		WRAPPER.registerMessage(MagicPowerMessage.Handler.class, MagicPowerMessage.class, id++, Side.CLIENT);
		
		CapabilityManager.INSTANCE.register(MagicPower.class, new MagicPower(), MagicPower::new);
		MinecraftForge.EVENT_BUS.register(new MagicPowerHandler());
	}
	
	protected void registerEventHandlers()
	{
		MinecraftForge.EVENT_BUS.register(new BlockDropHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
	
	private void registerAltarValues()
	{
		for (final Block block : ForgeRegistries.BLOCKS)
		{
			if (!(block instanceof BlockGrass) && (block instanceof IPlantable || block instanceof IGrowable || block instanceof BlockMelon || block instanceof BlockPumpkin)) BewitchmentAPI.registerAltarScanValue(block, 30);
			if (block instanceof BlockLog) BewitchmentAPI.registerAltarScanValue(block, 15);
			if (block instanceof BlockLeaves) BewitchmentAPI.registerAltarScanValue(block, 8);
		}
		BewitchmentAPI.registerAltarSwordRadius(ModObjects.boline, 2);
		BewitchmentAPI.registerAltarSwordMultiplier(ModObjects.sword_silver, 1.275);
		BewitchmentAPI.registerAltarSwordRadius(ModObjects.sword_cold_iron, -1);
		BewitchmentAPI.registerAltarSwordMultiplier(ModObjects.sword_cold_iron, 1.425);
		BewitchmentAPI.registerAltarSwordRadius(ModObjects.sword_cold_iron, -1);
		BewitchmentAPI.registerAltarSwordMultiplier(ModObjects.athame, 1.625);
		BewitchmentAPI.registerAltarSwordRadius(ModObjects.athame, 1);
		BewitchmentAPI.registerAltarSwordMultiplier(Items.IRON_SWORD, 1.275);
		BewitchmentAPI.registerAltarSwordRadius(Items.IRON_SWORD, -1);
		BewitchmentAPI.registerAltarSwordMultiplier(Items.GOLDEN_SWORD, 1.325);
		BewitchmentAPI.registerAltarSwordRadius(Items.GOLDEN_SWORD, 2);
		BewitchmentAPI.registerAltarSwordMultiplier(Items.DIAMOND_SWORD, 1.575);
		BewitchmentAPI.registerAltarSwordRadius(Items.DIAMOND_SWORD, -2);
	}
	
	private void registerAthameValues()
	{
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityPlayer.class), Sets.newHashSet(new ItemStack(ModObjects.heart), new ItemStack(Items.SKULL, 1, 3)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityVillager.class), Sets.newHashSet(new ItemStack(ModObjects.heart)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityZombieVillager.class), Sets.newHashSet(new ItemStack(ModObjects.spectral_dust)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityWither.class), Sets.newHashSet(new ItemStack(ModObjects.spectral_dust, 6)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityElderGuardian.class), Sets.newHashSet(new ItemStack(ModObjects.carnivorous_tooth, 4)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityZombie.class), Sets.newHashSet(new ItemStack(Items.SKULL, 1, 2), new ItemStack(ModObjects.spectral_dust)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityPigZombie.class), Sets.newHashSet(new ItemStack(ModObjects.spectral_dust, 3), new ItemStack(ModObjects.hoof, 2)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityHusk.class), Sets.newHashSet(new ItemStack(ModObjects.spectral_dust)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntitySkeleton.class), Sets.newHashSet(new ItemStack(Items.SKULL), new ItemStack(ModObjects.spectral_dust)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityWitherSkeleton.class), Sets.newHashSet(new ItemStack(Items.SKULL, 1, 1), new ItemStack(ModObjects.spectral_dust, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityStray.class), Sets.newHashSet(new ItemStack(ModObjects.spectral_dust)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntitySpider.class), Sets.newHashSet(new ItemStack(ModObjects.envenomed_fang, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityCaveSpider.class), Sets.newHashSet(new ItemStack(ModObjects.envenomed_fang, 2)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityCreeper.class), Sets.newHashSet(new ItemStack(Items.SKULL, 1, 4)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityBlaze.class), Sets.newHashSet(new ItemStack(ModObjects.ectoplasm)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityGhast.class), Sets.newHashSet(new ItemStack(ModObjects.ectoplasm, 2)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntitySilverfish.class), Sets.newHashSet(new ItemStack(ModObjects.silver_scales, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityEndermite.class), Sets.newHashSet(new ItemStack(ModObjects.dimensional_sand, 2)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityEnderman.class), Sets.newHashSet(new ItemStack(ModObjects.ectoplasm)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityVex.class), Sets.newHashSet(new ItemStack(ModObjects.ectoplasm, 4)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityGuardian.class), Sets.newHashSet(new ItemStack(ModObjects.eye_of_old)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityPig.class), Sets.newHashSet(new ItemStack(ModObjects.hoof, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityCow.class), Sets.newHashSet(new ItemStack(ModObjects.hoof, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntitySheep.class), Sets.newHashSet(new ItemStack(ModObjects.hoof, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityWolf.class), Sets.newHashSet(new ItemStack(ModObjects.tongue_of_dog), new ItemStack(ModObjects.carnivorous_tooth, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityOcelot.class), Sets.newHashSet(new ItemStack(ModObjects.carnivorous_tooth, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityParrot.class), Sets.newHashSet(new ItemStack(ModObjects.chromatic_quill, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityHorse.class), Sets.newHashSet(new ItemStack(ModObjects.hoof, 4), new ItemStack(ModObjects.equine_tail)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityDonkey.class), Sets.newHashSet(new ItemStack(ModObjects.hoof, 4), new ItemStack(ModObjects.equine_tail)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityMule.class), Sets.newHashSet(new ItemStack(ModObjects.hoof, 4), new ItemStack(ModObjects.equine_tail)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntitySkeletonHorse.class), Sets.newHashSet(new ItemStack(ModObjects.spectral_dust, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityLlama.class), Sets.newHashSet(new ItemStack(ModObjects.hoof, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityZombieHorse.class), Sets.newHashSet(new ItemStack(ModObjects.spectral_dust)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityPolarBear.class), Sets.newHashSet(new ItemStack(ModObjects.carnivorous_tooth, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityBat.class), Sets.newHashSet(new ItemStack(ModObjects.wool_of_bat, 3)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityBlindworm.class), Sets.newHashSet(new ItemStack(ModObjects.blindworms_sting, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityLizard.class), Sets.newHashSet(new ItemStack(ModObjects.lizard_leg, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityNewt.class), Sets.newHashSet(new ItemStack(ModObjects.eye_of_newt, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityOwl.class), Sets.newHashSet(new ItemStack(ModObjects.owlets_wing, 2)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityRaven.class), Sets.newHashSet(new ItemStack(ModObjects.ravens_feather, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityToad.class), Sets.newHashSet(new ItemStack(ModObjects.toe_of_frog, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntitySnake.class), Sets.newHashSet(new ItemStack(ModObjects.fillet_of_fenny_snake, 3), new ItemStack(ModObjects.adders_fork, 3)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityBlackDog.class), Sets.newHashSet(new ItemStack(ModObjects.tongue_of_dog), new ItemStack(ModObjects.ectoplasm, 4), new ItemStack(ModObjects.spectral_dust)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityHellhound.class), Sets.newHashSet(new ItemStack(ModObjects.tongue_of_dog), new ItemStack(ModObjects.hellhound_horn, 2), new ItemStack(Items.BLAZE_POWDER, 4)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityAlphaHellhound.class), Sets.newHashSet(new ItemStack(ModObjects.tongue_of_dog), new ItemStack(ModObjects.hellhound_horn, 4), new ItemStack(Items.BLAZE_POWDER, 8)));
		
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityDemon.class), Sets.newHashSet(new ItemStack(ModObjects.demonic_heart)));
		BewitchmentAPI.registerAthameLoot(EntityRegistry.getEntry(EntityDemoness.class), Sets.newHashSet(new ItemStack(ModObjects.demonic_heart)));
	}
	
	private void registerRecipes()
	{
		// Furnace
		GameRegistry.addSmelting(ModObjects.ore_silver, new ItemStack(ModObjects.ingot_silver), 0.35f);
		GameRegistry.addSmelting(Blocks.SAPLING, new ItemStack(ModObjects.wood_ash, 4), 0.15f);
		GameRegistry.addSmelting(Items.MELON, new ItemStack(ModObjects.grilled_watermelon), 0.45f);
		GameRegistry.addSmelting(ModObjects.ore_alexandrite, new ItemStack(ModObjects.gem_alexandrite), 0.35f);
		GameRegistry.addSmelting(ModObjects.ore_amethyst, new ItemStack(ModObjects.gem_amethyst), 0.35f);
		GameRegistry.addSmelting(ModObjects.ore_bloodstone, new ItemStack(ModObjects.gem_bloodstone), 0.35f);
		GameRegistry.addSmelting(ModObjects.ore_garnet, new ItemStack(ModObjects.gem_garnet), 0.35f);
		GameRegistry.addSmelting(ModObjects.ore_jasper, new ItemStack(ModObjects.gem_jasper), 0.35f);
		GameRegistry.addSmelting(ModObjects.ore_malachite, new ItemStack(ModObjects.gem_malachite), 0.35f);
		GameRegistry.addSmelting(ModObjects.ore_nuummite, new ItemStack(ModObjects.gem_nuummite), 0.35f);
		GameRegistry.addSmelting(ModObjects.ore_tigers_eye, new ItemStack(ModObjects.gem_tigers_eye), 0.35f);
		GameRegistry.addSmelting(ModObjects.ore_tourmaline, new ItemStack(ModObjects.gem_tourmaline), 0.35f);
		GameRegistry.addSmelting(ModObjects.golden_thread, new ItemStack(Items.GOLD_NUGGET), 1);
		GameRegistry.addSmelting(ModObjects.unfired_jar, new ItemStack(ModObjects.empty_jar), 0.45f);
		
		// Fortune
		BewitchmentAPI.registerFortune(new FortuneBadLuck());
		BewitchmentAPI.registerFortune(new FortuneDeath());
		BewitchmentAPI.registerFortune(new FortuneDropItem());
		BewitchmentAPI.registerFortune(new FortuneGoodLuck());
		BewitchmentAPI.registerFortune(new FortuneIllness());
		
		// Ritual
		BewitchmentAPI.registerRitual(new RitualHighMoon());
		BewitchmentAPI.registerRitual(new RitualSolarGlory());
		BewitchmentAPI.registerRitual(new RitualSandsTime());
		BewitchmentAPI.registerRitual(new RitualPerception());
		BewitchmentAPI.registerRitual(new RitualConjureWitch());
		BewitchmentAPI.registerRitual(new RitualConjureMagmaCube());
		BewitchmentAPI.registerRitual(new RitualConjureVex());
		BewitchmentAPI.registerRitual(new RitualConjureBlaze());
		BewitchmentAPI.registerRitual(new RitualConjureGhast());
		BewitchmentAPI.registerRitual(new RitualConjureWither());
		BewitchmentAPI.registerRitual(new RitualConjureSerpent());
		BewitchmentAPI.registerRitual(new RitualDrawing("draw_small", 0, 0, 0, GlyphType.ANY, null, null, TileEntityGlyph.small));
		BewitchmentAPI.registerRitual(new RitualDrawing("draw_medium", 0, 0, 0, GlyphType.ANY, GlyphType.ANY, null, TileEntityGlyph.medium));
		BewitchmentAPI.registerRitual(new RitualDrawing("draw_large", 0, 0, 0, GlyphType.ANY, GlyphType.ANY, GlyphType.ANY, TileEntityGlyph.large));
		BewitchmentAPI.registerRitual(new Ritual(Bewitchment.MOD_ID, "sanctuary", Ritual.ofi(), Ritual.ofe(), Ritual.ofs(), 250, 500, 4, GlyphType.NORMAL, GlyphType.NORMAL, null));
		
		// Distillery
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "cleansing_balm",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.acacia_resin)), Ingredient.fromStacks(new ItemStack(ModObjects.sagebrush)), Ingredient.fromStacks(new ItemStack(ModObjects.tulsi)), Ingredient.fromStacks(new ItemStack(ModObjects.white_sage))),
				Arrays.asList(new ItemStack(ModObjects.cleansing_balm), new ItemStack(ModObjects.wood_ash)),
				0, 300));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "demonic_elixir",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(ModObjects.cloudy_oil)), Ingredient.fromStacks(new ItemStack(ModObjects.demonic_heart)), Ingredient.fromStacks(new ItemStack(ModObjects.graveyard_dust))),
				Arrays.asList(new ItemStack(ModObjects.demonic_elixir)),
				0, 300));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "everchanging_dew",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, Short.MAX_VALUE)), Ingredient.fromStacks(new ItemStack(ModObjects.essence_of_vitality)), Ingredient.fromStacks(new ItemStack(Items.PAPER))),
				Arrays.asList(new ItemStack(ModObjects.empty_jar), new ItemStack(ModObjects.everchanging_dew)),
				0, 300));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "fiery_unguent",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(ModObjects.cloudy_oil)), Ingredient.fromStacks(new ItemStack(Blocks.OBSIDIAN)), Ingredient.fromStacks(new ItemStack(ModObjects.wood_ash))),
				Arrays.asList(new ItemStack(ModObjects.diabolical_vein, 2), new ItemStack(ModObjects.fiery_unguent)),
				0, 900));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "heaven_extract",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.birch_soul)), Ingredient.fromStacks(new ItemStack(Items.GLOWSTONE_DUST)), Ingredient.fromStacks(new ItemStack(ModObjects.gem_jasper)), Ingredient.fromStacks(new ItemStack(ModObjects.quartz_powder))),
				Arrays.asList(new ItemStack(ModObjects.heaven_extract)),
				0, 900));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "otherworldly_tears",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.birch_soul)), Ingredient.fromStacks(new ItemStack(Items.ENDER_PEARL)), Ingredient.fromStacks(new ItemStack(ModObjects.lapis_powder))),
				Arrays.asList(new ItemStack(ModObjects.dimensional_sand, 2), new ItemStack(ModObjects.otherworldly_tears)),
				0, 600));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "philter_of_dishonesty",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(ModObjects.graveyard_dust)), Ingredient.fromStacks(new ItemStack(ModObjects.liquid_witchcraft)), Ingredient.fromStacks(new ItemStack(ModObjects.oak_apple_gall))),
				Arrays.asList(new ItemStack(ModObjects.philter_of_dishonesty)),
				0, 300));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "stone_ichor",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.coquina)), Ingredient.fromStacks(new ItemStack(ModObjects.liquid_witchcraft)), Ingredient.fromStacks(new ItemStack(Blocks.OBSIDIAN)), Ingredient.fromStacks(new ItemStack(Blocks.STONE))),
				Arrays.asList(new ItemStack(ModObjects.stone_ichor)),
				0, 900));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "swirl_of_the_depths",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.coquina)), Ingredient.fromStacks(new ItemStack(ModObjects.kelp)), Ingredient.fromStacks(new ItemStack(ModObjects.lapis_powder)), Ingredient.fromStacks(new ItemStack(ModObjects.otherworldly_tears))),
				Arrays.asList(new ItemStack(Items.SLIME_BALL, 2), new ItemStack(ModObjects.swirl_of_the_depths)),
				0, 900));
		BewitchmentAPI.registerDistilleryRecipe(new DistilleryRecipe(Bewitchment.MOD_ID, "undying_salve",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.ectoplasm)), Ingredient.fromStacks(new ItemStack(ModObjects.ebb_of_death)), Ingredient.fromStacks(new ItemStack(ModObjects.essence_of_vitality))),
				Arrays.asList(new ItemStack(ModObjects.ectoplasm, 2), new ItemStack(ModObjects.undying_salve)),
				0, 300));
		
		// Loom
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "spider_web",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(Items.STRING)), Ingredient.fromStacks(new ItemStack(Items.STRING)), Ingredient.fromStacks(new ItemStack(Items.STRING))),
				new ItemStack(Blocks.WEB)));
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "regal_silk",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.chromatic_quill)), Ingredient.fromStacks(new ItemStack(Blocks.WEB)), Ingredient.fromStacks(new ItemStack(Blocks.WEB)), Ingredient.fromStacks(new ItemStack(ModObjects.everchanging_dew))),
				new ItemStack(ModObjects.regal_silk, 12)));
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "golden_thread",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(Items.WHEAT)), Ingredient.fromStacks(new ItemStack(Items.WHEAT)), Ingredient.fromStacks(new ItemStack(Blocks.HAY_BLOCK)), Ingredient.fromStacks(new ItemStack(ModObjects.everchanging_dew))),
				new ItemStack(ModObjects.golden_thread, 3)));
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "witches_stitching",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(Items.STRING)), Ingredient.fromStacks(new ItemStack(Items.STRING)), Ingredient.fromStacks(new ItemStack(ModObjects.oak_spirit)), Ingredient.fromStacks(new ItemStack(ModObjects.oak_spirit))),
				new ItemStack(ModObjects.witches_stitching, 4)));
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "diabolical_vein",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.soul_string)), Ingredient.fromStacks(new ItemStack(ModObjects.demonic_heart)), Ingredient.fromStacks(new ItemStack(ModObjects.philter_of_dishonesty)), Ingredient.fromStacks(new ItemStack(ModObjects.stone_ichor))),
				new ItemStack(ModObjects.diabolical_vein, 4)));
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "pure_filament",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.witches_stitching)), Ingredient.fromStacks(new ItemStack(ModObjects.witches_stitching)), Ingredient.fromStacks(new ItemStack(ModObjects.cleansing_balm)), Ingredient.fromStacks(new ItemStack(ModObjects.cleansing_balm))),
				new ItemStack(ModObjects.pure_filament, 4)));
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "soul_string",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.witches_stitching)), Ingredient.fromStacks(new ItemStack(ModObjects.witches_stitching)), Ingredient.fromStacks(new ItemStack(ModObjects.undying_salve)), Ingredient.fromStacks(new ItemStack(ModObjects.ectoplasm))),
				new ItemStack(ModObjects.soul_string, 2)));
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "string",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.kenaf)), Ingredient.fromStacks(new ItemStack(ModObjects.kenaf)), Ingredient.fromStacks(new ItemStack(ModObjects.kenaf)), Ingredient.fromStacks(new ItemStack(ModObjects.kenaf))),
				new ItemStack(Items.STRING, 12)));
		BewitchmentAPI.registerLoomRecipe(new LoomRecipe(Bewitchment.MOD_ID, "sanguine_fabric",
				Arrays.asList(Ingredient.fromStacks(new ItemStack(ModObjects.diabolical_vein)), Ingredient.fromStacks(new ItemStack(ModObjects.diabolical_vein)), Ingredient.fromStacks(new ItemStack(ModObjects.diabolical_vein)), Ingredient.fromStacks(new ItemStack(ModObjects.diabolical_vein))),
				new ItemStack(ModObjects.sanguine_fabric, 4)));
		
		// Oven
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "wheat",
				new ItemStack(Items.WHEAT),
				new ItemStack(Items.BREAD),
				new ItemStack(ModObjects.cloudy_oil),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "cactus",
				new ItemStack(Blocks.CACTUS),
				new ItemStack(Items.DYE, 1, 2),
				new ItemStack(ModObjects.cloudy_oil),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "chorus_fruit",
				new ItemStack(Items.CHORUS_FRUIT),
				new ItemStack(Items.CHORUS_FRUIT_POPPED),
				new ItemStack(ModObjects.dimensional_sand, 2),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "mandrake_root",
				new ItemStack(ModObjects.mandrake_root),
				new ItemStack(ModObjects.wood_ash),
				new ItemStack(ModObjects.cloudy_oil),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_oak",
				new ItemStack(Blocks.SAPLING),
				new ItemStack(ModObjects.wood_ash, 4),
				new ItemStack(ModObjects.oak_spirit),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_spruce",
				new ItemStack(Blocks.SAPLING, 1, 1),
				new ItemStack(ModObjects.wood_ash, 4),
				new ItemStack(ModObjects.spruce_heart),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_birch",
				new ItemStack(Blocks.SAPLING, 1, 2),
				new ItemStack(ModObjects.wood_ash, 4),
				new ItemStack(ModObjects.birch_soul),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_jungle",
				new ItemStack(Blocks.SAPLING, 1, 3),
				new ItemStack(ModObjects.wood_ash, 4),
				new ItemStack(ModObjects.cloudy_oil),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_acacia",
				new ItemStack(Blocks.SAPLING, 1, 4),
				new ItemStack(ModObjects.wood_ash, 4),
				new ItemStack(ModObjects.acacia_resin),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_dark_oak",
				new ItemStack(Blocks.SAPLING, 1, 5),
				new ItemStack(ModObjects.wood_ash, 4),
				new ItemStack(ModObjects.oak_spirit),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_cypress",
				new ItemStack(ModObjects.sapling_cypress),
				new ItemStack(ModObjects.wood_ash),
				new ItemStack(ModObjects.ebb_of_death),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_elder",
				new ItemStack(ModObjects.sapling_elder),
				new ItemStack(ModObjects.wood_ash),
				new ItemStack(ModObjects.droplet_of_wisdom),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_juniper",
				new ItemStack(ModObjects.sapling_juniper),
				new ItemStack(ModObjects.wood_ash),
				new ItemStack(ModObjects.liquid_witchcraft),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "sapling_yew",
				new ItemStack(ModObjects.sapling_yew),
				new ItemStack(ModObjects.wood_ash),
				new ItemStack(ModObjects.essence_of_vitality),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "rotten_flesh",
				new ItemStack(Items.ROTTEN_FLESH),
				new ItemStack(Items.LEATHER),
				new ItemStack(ModObjects.ectoplasm, 3),
				0.85f));
		BewitchmentAPI.registerOvenRecipe(new OvenRecipe(Bewitchment.MOD_ID, "bone",
				new ItemStack(Items.BONE),
				new ItemStack(Items.DYE, 1, 15),
				new ItemStack(ModObjects.ectoplasm),
				0.85f));
		
		// Tool Repair
		ModObjects.TOOL_COLD_IRON.setRepairItem(new ItemStack(ModObjects.ingot_cold_iron));
		ModObjects.TOOL_SILVER.setRepairItem(new ItemStack(ModObjects.ingot_silver));
		ModObjects.ARMOR_COLD_IRON.setRepairItem(new ItemStack(ModObjects.ingot_cold_iron));
		ModObjects.ARMOR_SILVER.setRepairItem(new ItemStack(ModObjects.ingot_silver));
		ModObjects.TOOL_RITUAL.setRepairItem(new ItemStack(ModObjects.ingot_silver));
		ModObjects.ARMOR_BEWITCHED_LEATHER.setRepairItem(new ItemStack(ModObjects.witches_stitching));
		ModObjects.ARMOR_VAMPIRE.setRepairItem(new ItemStack(ModObjects.sanguine_fabric));
	}
	
	private void registerWorldGenerators()
	{
		GameRegistry.registerWorldGenerator(new WorldGenOres(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenCoquina(), 0);
		GameRegistry.registerWorldGenerator(new WorldGenBeehive(), 0);
		
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_aconitum), 6);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_asphodel), 6);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_belladonna), 8);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_chrysanthemum), 6);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_ginger), 3);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_hellebore), 3);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_kenaf), 3);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_lavender), 4);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_mandrake), 4);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_mint), 6);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_thistle), 8);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_tulsi), 3);
		MinecraftForge.addGrassSeed(new ItemStack(ModObjects.seed_wormwood), 8);
		
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "chests/books"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "chests/materials"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "chests/saplings"));
		
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/blindworm"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/lizard"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/newt"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/owl"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/snake"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/raven"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/toad"));
		
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/black_dog"));
		
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/hellhound"));
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/alpha_hellhound"));
		
		LootTableList.register(new ResourceLocation(Bewitchment.MOD_ID, "entities/demon"));
	}
}