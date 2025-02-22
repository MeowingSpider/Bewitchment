package com.bewitchment.registry;

import com.bewitchment.Bewitchment;
import com.bewitchment.Util;
import com.bewitchment.api.BewitchmentAPI;
import com.bewitchment.api.capability.extendedplayer.ExtendedPlayer;
import com.bewitchment.api.registry.*;
import com.bewitchment.common.block.BlockCandle;
import com.bewitchment.common.block.BlockGlyph;
import com.bewitchment.common.block.BlockLantern;
import com.bewitchment.common.entity.living.*;
import com.bewitchment.common.entity.spirit.demon.EntityDemon;
import com.bewitchment.common.entity.spirit.demon.EntityHellhound;
import com.bewitchment.common.entity.spirit.demon.EntitySerpent;
import com.bewitchment.common.entity.spirit.ghost.EntityBlackDog;
import com.bewitchment.common.fortune.*;
import com.bewitchment.common.ritual.*;
import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamilyVanilla;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.RecipeTippedArrow;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.misc.BlockNitor;
import vazkii.botania.common.item.ModItems;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class ModRecipes {
	public static void init() {
		furnaceInit();
		altarInit();
		ritualInit();
		cauldronInit();
		ovenInit();
		distilleryInit();
		spinningWheelInit();
		frostfireInit();
		fortuneInit();
		tarotInit();
		
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityOcelot.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityWolf.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityHorse.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityDonkey.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityMule.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityLlama.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityParrot.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityOwl.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityRaven.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntitySnake.class));
		BewitchmentAPI.VALID_PETS.add(EntityRegistry.getEntry(EntityToad.class));
		
		ModObjects.TOOL_COLD_IRON.setRepairItem(new ItemStack(ModObjects.cold_iron_ingot));
		ModObjects.TOOL_SILVER.setRepairItem(new ItemStack(ModObjects.silver_ingot));
		ModObjects.ARMOR_COLD_IRON.setRepairItem(new ItemStack(ModObjects.cold_iron_ingot));
		ModObjects.ARMOR_SILVER.setRepairItem(new ItemStack(ModObjects.silver_ingot));
		
		ForgeRegistries.RECIPES.register(new RecipeTippedArrow() {
			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv) {
				ItemStack stack = super.getCraftingResult(inv);
				if (PotionUtils.getPotionFromItem(stack) == PotionTypes.EMPTY) stack.getTagCompound().setInteger("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(PotionUtils.getFullEffectsFromItem(stack)));
				return stack;
			}
		}.setRegistryName("minecraft", "tippedarrow"));
	}
	
	public static void postInit() {
		athamePostInit();
		furnacePostInit();
	}
	
	private static void altarInit() {
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof BlockFlowerPot && s.getTileEntity() instanceof TileEntityFlowerPot && ((TileEntityFlowerPot) s.getTileEntity()).getFlowerItemStack().isEmpty(), new AltarUpgrade(AltarUpgrade.Type.CUP, 0, 1.05));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof BlockFlowerPot && s.getTileEntity() instanceof TileEntityFlowerPot && !((TileEntityFlowerPot) s.getTileEntity()).getFlowerItemStack().isEmpty(), new AltarUpgrade(AltarUpgrade.Type.CUP, 1, 1.1));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() == ModObjects.goblet, new AltarUpgrade(AltarUpgrade.Type.CUP, 0, 1.075));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() == ModObjects.filled_goblet, new AltarUpgrade(AltarUpgrade.Type.CUP, 1, 1.2));
		Util.registerAltarUpgradeItem(Items.GLASS_BOTTLE, new AltarUpgrade(AltarUpgrade.Type.CUP, 0, 1.05));
		Util.registerAltarUpgradeItem(ModObjects.flying_ointment, new AltarUpgrade(AltarUpgrade.Type.CUP, 1, 1.17));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() == Blocks.SKULL && s.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull) s.getTileEntity()).getSkullType() == 5, new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 6, 0));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() == Blocks.SKULL && s.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull) s.getTileEntity()).getSkullType() == 1, new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() == Blocks.SKULL && s.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull) s.getTileEntity()).getSkullType() != 5 && ((TileEntitySkull) s.getTileEntity()).getSkullType() != 1, new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeItem(ModObjects.pentacle, new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 3, 0));
		Util.registerAltarUpgradeOreDict("fossil", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemDiamond", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemEmerald", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemPsi", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemPrismarine", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemLifeCrystal", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemQuartz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemLapis", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("dustRedstone", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemBloodstone", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemNuummite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemGarnet", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemTourmaline", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemJasper", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemTigersEye", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemMalachite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemAmethyst", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemAlexandrite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemAquamarine", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemRuby", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemSapphire", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemPeridot", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAmber", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemApatite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemTopaz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemJet", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemTanzanite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemPearl", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemOpal", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemZanite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCrimsonMiddleGem", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAquaMiddleGem", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemGreenMiddleGem", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemZircon", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAzurite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemEudialyte", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemRime", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAgate", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemJade", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemOnyx", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemEnderBiotite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemBurnium", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemEndimium", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemHephaestite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemScarlite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAether", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemSerpentine", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemPetoskeyStone", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemValonite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemRhodochrosite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemBoronNitride", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemFluorite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemVilliaumite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCarobbiite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemMoon", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemRedstone", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemVinteum", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemQuartzite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemGlass", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemQuartzBlock", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAlmandine", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemBlueTopaz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCinnabar", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemGreenSapphire", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemRutile", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemLazurite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemSodalite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCertusQuartz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemOlivine", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemLignite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemGarnetRed", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemGarnetYellow", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemMonazite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemDominicanAmber", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemScarabBlue", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemScarab", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemMoldavite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		Util.registerAltarUpgradeOreDict("gemVioletSapphire", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCatsEye", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAmmolite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemSpinel", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemMoonstone", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemSunstone", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemPyrope", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemRoseQuartz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemKunzite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemKyanite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChrysoprase", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemBlackDiamond", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemTektite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemMorganite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemLepidolite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCoral", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCarnelian", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemGoldenBeryl", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemHeliodor", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemIndicolite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemIolite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemTurquoise", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("materialCoraliumPearl", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemShadow", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemBlackTourmaline", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCrystalFlux", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemLava", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemEnderEssence", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemPhoenixite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemBoronArsenide", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemQuartzBlack", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemMoon", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChaos", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedCinnabar", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedAlmandine", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedAmethyst", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedApatite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedBlueTopaz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedCertusQuartz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedCoal", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedCoke", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedDiamond", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedEmerald", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedGarnetRed", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedGarnetYellow", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedGlass", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedGreenSapphire", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedJasper", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedLapis", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedLazurite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedLignite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedMonazite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedNetherQuartz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedOlivine", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedOpal", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedQuartzite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedRuby", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedRutile", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedSapphire", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedSodalite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedTanzanite", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedTopaz", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemChippedVinteum", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAmetrine", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemAbalone", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCowry", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeOreDict("gemCowrie", new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 1, 0));
		Util.registerAltarUpgradeItem(ModObjects.demon_heart, new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 4, 0));
		Util.registerAltarUpgradeItem(ModObjects.grimoire_magia, new AltarUpgrade(AltarUpgrade.Type.PENTACLE, 2, 0));
		//Addendum 2000-2: While making repairs to SRA units in Sector 3382 on
		//??/??/????.2, Technician [DATA EXPUNGED] reported the discovery of
		//human remains in an advanced state of decay. Analysis of clothing
		//fragments discovered with the remains indicates the remains are 450 -
		//700 yrs old. Valid Foundation security credentials for Dr. Alto Clef
		//were discovered nearby, although a genetic match could not be
		//established. The following note was recovered from a hermetically
		//sealed plastic document sleeve.
		
		//Why did we have to build this thing?
		//When did we do it?
		//How long have we been doing it?
		//Do we even know?!
		
		Util.registerAltarUpgradeItem(ModObjects.athame, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.325));
		Util.registerAltarUpgradeItem(ModObjects.silver_sword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.3));
		Util.registerAltarUpgradeItem(ModObjects.cold_iron_sword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.35));
		Util.registerAltarUpgradeItem(Items.WOODEN_SWORD, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.075));
		Util.registerAltarUpgradeItem(Items.STONE_SWORD, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.15));
		Util.registerAltarUpgradeItem(Items.IRON_SWORD, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.225));
		Util.registerAltarUpgradeItem(Items.GOLDEN_SWORD, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.3));
		Util.registerAltarUpgradeItem(Items.DIAMOND_SWORD, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.325));
		
		//Botania stuff
		if (Loader.isModLoaded("botania")) {
			Util.registerAltarUpgradeItem(ModItems.twigWand, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.1));
			Util.registerAltarUpgradeItem(ModItems.manasteelSword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.23));
			Util.registerAltarUpgradeItem(ModItems.starSword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.375));
			Util.registerAltarUpgradeItem(ModItems.thunderSword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.375));
			Util.registerAltarUpgradeItem(ModItems.enderDagger, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.23));
			Util.registerAltarUpgradeItem(ModItems.elementiumSword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.3));
			Util.registerAltarUpgradeItem(ModItems.terraSword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.35));
			Util.registerAltarUpgradeItem(ModItems.cobbleRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.2));
			Util.registerAltarUpgradeItem(ModItems.dirtRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.2));
			Util.registerAltarUpgradeItem(ModItems.diviningRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.25));
			Util.registerAltarUpgradeItem(ModItems.exchangeRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.35));
			Util.registerAltarUpgradeItem(ModItems.fireRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.3));
			Util.registerAltarUpgradeItem(ModItems.gravityRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.3));
			Util.registerAltarUpgradeItem(ModItems.missileRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.4));
			Util.registerAltarUpgradeItem(ModItems.rainbowRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.4));
			Util.registerAltarUpgradeItem(ModItems.skyDirtRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.225));
			Util.registerAltarUpgradeItem(ModItems.smeltRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.225));
			Util.registerAltarUpgradeItem(ModItems.terraformRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.3));
			Util.registerAltarUpgradeItem(ModItems.tornadoRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.25));
			Util.registerAltarUpgradeItem(ModItems.waterRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.2));
		}
		
		//Thaumcraft stuff
		if (Loader.isModLoaded("thaumcraft")) {
			Util.registerAltarUpgradeItem(ItemsTC.thaumiumSword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.25));
			Util.registerAltarUpgradeItem(ItemsTC.voidSword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.3));
			Util.registerAltarUpgradeItem(ItemsTC.elementalSword, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.3));
			Util.registerAltarUpgradeItem(ItemsTC.crimsonBlade, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.325));
			Util.registerAltarUpgradeItem(ItemsTC.primalCrusher, new AltarUpgrade(AltarUpgrade.Type.SWORD, 0, 1.35));
			BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof thaumcraft.common.blocks.basic.BlockCandle, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.1));
			BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof BlockNitor, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.225));
			Util.registerAltarUpgradeItem(ItemsTC.pechWand, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.35));
		}
		
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof BlockTorch, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.05));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof BlockRedstoneTorch, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.1));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof BlockCandle, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.125));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof BlockLantern, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.25));
		BewitchmentAPI.ALTAR_UPGRADES.put(s -> s.getBlockState().getBlock() instanceof BlockEndRod, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.325));
		Util.registerAltarUpgradeItem(Items.BLAZE_ROD, new AltarUpgrade(AltarUpgrade.Type.WAND, 0, 1.2));
	}
	
	private static void ritualInit() {
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualSolarGlory());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualHighMoon());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualSandsOfTime());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualDeluge());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualPerception());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualHungryFlames());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualFrenziedGrowth());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualRevealing());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualCallOfTheWild());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualLesserHellMouth());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualHellmouth());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualGreaterHellmouth());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualConjureWitch());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualConjureWither());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualConjureDemon());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualConjureImp());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualSpiritualRift());
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualDrawing(new ResourceLocation(Bewitchment.MODID, "draw_small"), Collections.singletonList(Util.get(Items.CLAY_BALL)), 150, BlockGlyph.ANY, -1, -1, Ritual.small));
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualDrawing(new ResourceLocation(Bewitchment.MODID, "draw_medium"), Arrays.asList(Util.get(Items.CLAY_BALL), Util.get(ModObjects.wood_ash)), 300, BlockGlyph.ANY, BlockGlyph.ANY, -1, Ritual.medium));
		BewitchmentAPI.REGISTRY_RITUAL.register(new RitualDrawing(new ResourceLocation(Bewitchment.MODID, "draw_large"), Arrays.asList(Util.get(Items.CLAY_BALL), Util.get(Items.CLAY_BALL), Util.get(ModObjects.wood_ash), Util.get(ModObjects.wood_ash)), 450, BlockGlyph.ANY, BlockGlyph.ANY, BlockGlyph.ANY, Ritual.large));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "crystal_ball"), Arrays.asList(Util.get("blockGlass"), Util.get("gemQuartz"), Util.get("gemQuartz"), Util.get("gemQuartz"), Util.get(ModObjects.droplet_of_wisdom)), null, Collections.singletonList(new ItemStack(ModObjects.crystal_ball)), 5, 500, 50, BlockGlyph.NORMAL, BlockGlyph.ENDER, -1));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "tarot_table"), Arrays.asList(Util.get(Blocks.STONEBRICK), Util.get(ModObjects.liquid_witchcraft), Util.get(ModObjects.juniper_planks), Util.get(new ItemStack(Blocks.WOOL, 1, Short.MAX_VALUE))), null, Collections.singletonList(new ItemStack(ModObjects.tarot_table)), 7, 750, 60, BlockGlyph.NORMAL, BlockGlyph.NORMAL, -1));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "tarot_cards"), Arrays.asList(Util.get("dye"), Util.get("dye"), Util.get("dye"), Util.get("paper"), Util.get("materialWax", "materialBeeswax", "wax", "tallow", "materialPressedWax", "itemBeeswax", "clumpWax", "beeswax", "itemWax")), null, Collections.singletonList(new ItemStack(ModObjects.tarot_cards)), 4, 500, 25, BlockGlyph.NORMAL, -1, -1));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "grimoire_magia"), Arrays.asList(Util.get("leather"), Util.get("leather"), Util.get("paper"), Util.get("paper"), Util.get("paper"), Util.get(ModObjects.liquid_witchcraft), Util.get(ModObjects.moonstone)), null, Collections.singletonList(new ItemStack(ModObjects.grimoire_magia)), 5, 150, 20, BlockGlyph.NORMAL, -1, -1));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "purifying_earth"), Arrays.asList(Util.get("dirt"), Util.get("dirt"), Util.get("dirt"), Util.get("dirt"), Util.get("cropWhiteSage"), Util.get("cropWhiteSage"), Util.get("salt"), Util.get("salt")), null, Collections.singletonList(new ItemStack(ModObjects.purifying_earth, 16)), 2, 200, 20, BlockGlyph.NORMAL, BlockGlyph.NORMAL, -1));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "cypress_broom"), Arrays.asList(Util.get(ModObjects.broom), Util.get(ModObjects.cypress_wood), Util.get(ModObjects.cypress_sapling), Util.get(ModObjects.flying_ointment), Util.get(ModObjects.ebb_of_death)), null, Collections.singletonList(new ItemStack(ModObjects.cypress_broom)), 10, 1250, 60, BlockGlyph.NORMAL, BlockGlyph.NORMAL, BlockGlyph.ENDER));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "elder_broom"), Arrays.asList(Util.get(ModObjects.broom), Util.get(ModObjects.elder_wood), Util.get(ModObjects.elder_sapling), Util.get(ModObjects.flying_ointment), Util.get(ModObjects.droplet_of_wisdom)), null, Collections.singletonList(new ItemStack(ModObjects.elder_broom)), 10, 1250, 60, BlockGlyph.NORMAL, BlockGlyph.NORMAL, BlockGlyph.ENDER));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "juniper_broom"), Arrays.asList(Util.get(ModObjects.broom), Util.get(ModObjects.juniper_wood), Util.get(ModObjects.juniper_sapling), Util.get(ModObjects.flying_ointment), Util.get(ModObjects.liquid_witchcraft)), null, Collections.singletonList(new ItemStack(ModObjects.juniper_broom)), 10, 1250, 60, BlockGlyph.NORMAL, BlockGlyph.NORMAL, BlockGlyph.ENDER));
		BewitchmentAPI.REGISTRY_RITUAL.register(new Ritual(new ResourceLocation(Bewitchment.MODID, "yew_broom"), Arrays.asList(Util.get(ModObjects.broom), Util.get(ModObjects.yew_wood), Util.get(ModObjects.yew_sapling), Util.get(ModObjects.flying_ointment), Util.get(ModObjects.essence_of_vitality)), null, Collections.singletonList(new ItemStack(ModObjects.yew_broom)), 10, 1250, 60, BlockGlyph.NORMAL, BlockGlyph.NORMAL, BlockGlyph.ENDER));
		if (Bewitchment.config.wednesday) BewitchmentAPI.REGISTRY_RITUAL.register(new RitualWednesday());
	}
	
	private static void cauldronInit() {
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "focal_chalk"), Arrays.asList(Util.get(ModObjects.ritual_chalk), Util.get(ModObjects.liquid_witchcraft), Util.get("nuggetGold")), Arrays.asList(new ItemStack(ModObjects.focal_chalk), new ItemStack(ModObjects.empty_jar))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "fiery_chalk"), Arrays.asList(Util.get(ModObjects.ritual_chalk), Util.get(Items.BLAZE_POWDER), Util.get("netherrack")), Collections.singletonList(new ItemStack(ModObjects.fiery_chalk))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "phasing_chalk"), Arrays.asList(Util.get(ModObjects.ritual_chalk), Util.get(ModObjects.dimensional_sand), Util.get("dustGlowstone")), Collections.singletonList(new ItemStack(ModObjects.phasing_chalk))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "filled_goblet"), Arrays.asList(Util.get(ModObjects.goblet), Util.get(ModObjects.cloudy_oil), Util.get(Items.GHAST_TEAR), Util.get("dustRedstone"), Util.get("dustRedstone"), Util.get("dustRedstone")), Collections.singletonList(new ItemStack(ModObjects.filled_goblet))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "embergrass"), Arrays.asList(Util.get(new ItemStack(Blocks.TALLGRASS, 1, 1)), Util.get(ModObjects.liquid_witchcraft), Util.get(Items.BLAZE_POWDER), Util.get(new ItemStack(Blocks.YELLOW_FLOWER, 1, Short.MAX_VALUE), new ItemStack(Blocks.RED_FLOWER, 1, Short.MAX_VALUE))), Arrays.asList(new ItemStack(ModObjects.embergrass), new ItemStack(ModObjects.empty_jar))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "torchwood"), Arrays.asList(Util.get(new ItemStack(Blocks.TALLGRASS, 1, 1)), Util.get(ModObjects.liquid_witchcraft), Util.get("logWood"), Util.get("glowstone")), Arrays.asList(new ItemStack(ModObjects.torchwood), new ItemStack(ModObjects.empty_jar))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "tallow"), Arrays.asList(Util.get(Items.ROTTEN_FLESH), Util.get(Items.ROTTEN_FLESH)), Collections.singletonList(new ItemStack(ModObjects.tallow))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "slimeball"), Collections.singletonList(Util.get(ModObjects.hoof)), Collections.singletonList(new ItemStack(Items.SLIME_BALL))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "iron_gall_ink"), Arrays.asList(Util.get(ModObjects.oak_apple_gall), Util.get(ModObjects.oak_apple_gall), Util.get("nuggetIron")), Collections.singletonList(new ItemStack(ModObjects.iron_gall_ink, 3))));
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "catechu_brown"), Collections.singletonList(Util.get("logWood")), Collections.singletonList(new ItemStack(ModObjects.catechu_brown, 4))));
		
		BewitchmentAPI.REGISTRY_CAULDRON.register(new CauldronRecipe(new ResourceLocation(Bewitchment.MODID, "banner_pattern_removal"), Collections.singletonList(Util.get(new ItemStack(Items.BANNER, 1, Short.MAX_VALUE))), Collections.singletonList(new ItemStack(Items.BANNER, 1, EnumDyeColor.WHITE.getDyeDamage()))));
		
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "instant_health"), Util.get(Items.APPLE), new PotionEffect(MobEffects.INSTANT_HEALTH, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "instant_damage"), Util.get(Items.SPIDER_EYE), new PotionEffect(MobEffects.INSTANT_DAMAGE, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "regeneration"), Util.get(Items.SPECKLED_MELON), new PotionEffect(MobEffects.REGENERATION, (20 * 20))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "poison"), Util.get(ModObjects.snake_venom, ModObjects.belladonna), s -> s.getItem() == ModObjects.snake_venom, new ItemStack(Items.GLASS_BOTTLE), new PotionEffect(MobEffects.POISON, (20 * 20))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "wither"), Util.get(ModObjects.liquid_wroth), new ItemStack(Items.GLASS_BOTTLE), new PotionEffect(MobEffects.WITHER, (20 * 15))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "night_vision"), Util.get(Items.CARROT, ModObjects.eye_of_old), new PotionEffect(MobEffects.NIGHT_VISION, (20 * 120))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "blindness"), Util.get(ModObjects.iron_gall_ink, new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage())), new PotionEffect(MobEffects.BLINDNESS, (20 * 10))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "strength"), Util.get(Items.PORKCHOP, Items.BEEF, Items.MUTTON, Items.CHICKEN, Items.RABBIT), new PotionEffect(MobEffects.STRENGTH, (20 * 65))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "weakness"), Util.get(ModObjects.hellebore), new PotionEffect(MobEffects.WEAKNESS, (20 * 45))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "resistance"), Util.get(Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_MUTTON, Items.COOKED_CHICKEN, Items.COOKED_RABBIT), new PotionEffect(MobEffects.RESISTANCE, (20 * 30))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "fire_resistance"), Util.get(ModObjects.embergrass), new PotionEffect(MobEffects.FIRE_RESISTANCE, (20 * 150))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "absorption"), Util.get("treeSapling"), new PotionEffect(MobEffects.ABSORPTION, (20 * 35))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "jump_boost"), Util.get(Items.RABBIT_HIDE, ModObjects.hoof), new PotionEffect(MobEffects.JUMP_BOOST, (20 * 45))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "speed"), Util.get(Items.SUGAR), new PotionEffect(MobEffects.SPEED, (20 * 45))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "slowness"), Util.get(Blocks.BROWN_MUSHROOM, "slimeball"), new PotionEffect(MobEffects.SLOWNESS, (20 * 40))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "haste"), Util.get("sugarcane"), new PotionEffect(MobEffects.HASTE, (20 * 30))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "mining_fatigue"), Util.get(ModObjects.lizard_leg), new PotionEffect(MobEffects.MINING_FATIGUE, (20 * 30))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "luck"), Util.get(Items.RABBIT_FOOT), new PotionEffect(MobEffects.LUCK, (20 * 120))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "unluck"), Util.get(ModObjects.adders_fork), new PotionEffect(MobEffects.UNLUCK, (20 * 120))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "water_breathing"), Util.get(Items.FISH, ModObjects.eye_of_old), new PotionEffect(MobEffects.WATER_BREATHING, (20 * 150))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "invisibility"), Util.get(ModObjects.ectoplasm), new PotionEffect(MobEffects.INVISIBILITY, (20 * 25))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "hunger"), Util.get(ModObjects.tongue_of_dog), new PotionEffect(MobEffects.HUNGER, (20 * 30))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "nausea"), Util.get(Blocks.RED_MUSHROOM), new PotionEffect(MobEffects.NAUSEA, (20 * 10))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "glowing"), Util.get(ModObjects.spectral_dust), new PotionEffect(MobEffects.GLOWING, (20 * 40))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "levitation"), Util.get(Items.CHORUS_FRUIT), new PotionEffect(MobEffects.LEVITATION, (20 * 4))));
		
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "absence"), Util.get(Items.MILK_BUCKET), new PotionEffect(ModPotions.absence, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "purification"), Util.get(Items.MELON), new PotionEffect(ModPotions.purification, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "corruption"), Util.get(Items.FERMENTED_SPIDER_EYE), new PotionEffect(ModPotions.corruption, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "magic_resistance"), Util.get(ModObjects.yew_aril), new PotionEffect(ModPotions.magic_resistance, (20 * 60))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "magic_weakness"), Util.get(ModObjects.juniper_berries), new PotionEffect(ModPotions.magic_weakness, (20 * 60))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "hellworld"), Util.get(ModObjects.hellhound_horn), new PotionEffect(ModPotions.hellworld, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "iceworld"), Util.get(Blocks.ICE, Blocks.PACKED_ICE, Blocks.SNOW), new PotionEffect(ModPotions.iceworld, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "rubedo"), Util.get(ModObjects.acacia_resin), new ItemStack(ModObjects.empty_jar), new PotionEffect(ModPotions.rubedo, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "citrinitas"), Util.get(ModObjects.birch_soul), new ItemStack(ModObjects.empty_jar), new PotionEffect(ModPotions.citrinitas, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "desertification"), Util.get(new ItemStack(Items.COAL, 1, Short.MAX_VALUE)), new PotionEffect(ModPotions.desertification, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "fertility"), Util.get(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta())), new PotionEffect(ModPotions.fertility, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "harvest"), Util.get(Blocks.PUMPKIN), new PotionEffect(ModPotions.harvest, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "spore_cloud"), Util.get(Items.MUSHROOM_STEW), new ItemStack(Items.BOWL), new PotionEffect(ModPotions.spore_cloud, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "blight"), Util.get(Items.ROTTEN_FLESH), new PotionEffect(ModPotions.blight, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "ruin"), Util.get(Blocks.CACTUS, Blocks.DEADBUSH), new PotionEffect(ModPotions.ruin, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "pesticide"), Util.get("cropWormwood"), new PotionEffect(ModPotions.pesticide, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "arachnophobia"), Util.get(Blocks.WEB), new PotionEffect(ModPotions.arachnophobia, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "mending"), Util.get(Items.GOLDEN_APPLE, new ItemStack(Items.GOLDEN_APPLE, 1, 1)), new PotionEffect(ModPotions.mending, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "holy_water"), Util.get("cropWhiteSage", "cropGarlic", new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.ALLIUM.getMeta())), new PotionEffect(ModPotions.holy_water, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "wolfsbane"), Util.get("cropAconitum"), new PotionEffect(ModPotions.wolfsbane, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "deviants_decomposure"), Util.get(ModObjects.elderberries), new PotionEffect(ModPotions.deviants_decomposure, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "sleeping"), Util.get(Blocks.RED_FLOWER), new PotionEffect(ModPotions.sleeping, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "volatility"), Util.get(Items.FIRE_CHARGE), new PotionEffect(ModPotions.volatility, (20 * 45))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "grace"), Util.get("feather"), new PotionEffect(ModPotions.grace, (20 * 30))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "shell_armor"), Util.get("coquina"), new PotionEffect(ModPotions.shell_armor, (20 * 30))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "deflection"), Util.get(Items.CHORUS_FRUIT_POPPED), new PotionEffect(ModPotions.deflection, (20 * 30))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "enderference"), Util.get(ModObjects.dimensional_sand), new PotionEffect(ModPotions.enderference, (20 * 30))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "revealing"), Util.get(ModObjects.eye_of_newt), new PotionEffect(ModPotions.revealing, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "disrobing"), Util.get(ModObjects.tallow), new PotionEffect(ModPotions.disrobing, 1)));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "sinking"), Util.get("nuggetGold", "nuggetIron", "nuggetColdIron", "nuggetCopper", "nuggetTin", "nuggetBronze", "nuggetLead"), new PotionEffect(ModPotions.sinking, (20 * 20))));
		BewitchmentAPI.REGISTRY_BREW.register(new Brew(new ResourceLocation(Bewitchment.MODID, "cursed_leaps"), Util.get(ModObjects.toe_of_frog), new PotionEffect(ModPotions.cursed_leaps, 1)));
	}
	
	private static void athamePostInit() {
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityPlayer, Sets.newHashSet(new ItemStack(ModObjects.heart), new ItemStack(Items.SKULL, 1, 3)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityVillager || e instanceof EntityWitch || e instanceof AbstractIllager, Sets.newHashSet(new ItemStack(ModObjects.heart)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityZombieVillager, Sets.newHashSet(new ItemStack(ModObjects.spectral_dust)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityWither, Sets.newHashSet(new ItemStack(ModObjects.spectral_dust, 6)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityElderGuardian, Sets.newHashSet(new ItemStack(ModObjects.eye_of_old, 3)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityZombie && !(e instanceof EntityPigZombie), Sets.newHashSet(new ItemStack(Items.SKULL, 1, 2), new ItemStack(ModObjects.spectral_dust)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityPigZombie, Sets.newHashSet(new ItemStack(ModObjects.spectral_dust, 3), new ItemStack(ModObjects.hoof, 2)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof AbstractSkeleton && !(e instanceof EntityWitherSkeleton), Sets.newHashSet(new ItemStack(Items.SKULL), new ItemStack(ModObjects.spectral_dust)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityWitherSkeleton, Sets.newHashSet(new ItemStack(Items.SKULL, 1, 1), new ItemStack(ModObjects.spectral_dust, 2)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityCreeper, Sets.newHashSet(new ItemStack(Items.SKULL, 1, 4)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityBlaze, Sets.newHashSet(new ItemStack(ModObjects.ectoplasm, 2)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityGhast, Sets.newHashSet(new ItemStack(ModObjects.ectoplasm, 8)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntitySilverfish, Sets.newHashSet(new ItemStack(ModObjects.silver_nugget, 2)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityEndermite, Sets.newHashSet(new ItemStack(ModObjects.dimensional_sand, 2)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityEnderman, Sets.newHashSet(new ItemStack(ModObjects.ectoplasm, 2)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityVex, Sets.newHashSet(new ItemStack(ModObjects.ectoplasm, 4)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityGuardian, Sets.newHashSet(new ItemStack(ModObjects.eye_of_old)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityPig, Sets.newHashSet(new ItemStack(ModObjects.hoof, 4)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityCow, Sets.newHashSet(new ItemStack(ModObjects.hoof, 4)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntitySheep, Sets.newHashSet(new ItemStack(ModObjects.hoof, 4)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityWolf, Sets.newHashSet(new ItemStack(ModObjects.tongue_of_dog)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityRabbit, Sets.newHashSet(new ItemStack(Items.RABBIT_FOOT)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof AbstractHorse, Sets.newHashSet(new ItemStack(ModObjects.hoof, 4)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof AbstractHorse && e.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD, Sets.newHashSet(new ItemStack(ModObjects.spectral_dust, 2)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityLizard, Sets.newHashSet(new ItemStack(ModObjects.lizard_leg, 4)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityNewt, Sets.newHashSet(new ItemStack(ModObjects.eye_of_newt, 2)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityOwl, Sets.newHashSet(new ItemStack(ModObjects.owlets_wing, 2)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityRaven && !(e instanceof EntityOwl), Sets.newHashSet(new ItemStack(ModObjects.ravens_feather, 4)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityToad, Sets.newHashSet(new ItemStack(ModObjects.toe_of_frog, 4)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntitySnake, Sets.newHashSet(new ItemStack(ModObjects.adders_fork, 3)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityBlackDog, Sets.newHashSet(new ItemStack(ModObjects.tongue_of_dog), new ItemStack(ModObjects.ectoplasm, 4), new ItemStack(ModObjects.spectral_dust)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityHellhound, Sets.newHashSet(new ItemStack(ModObjects.tongue_of_dog), new ItemStack(ModObjects.hellhound_horn, 2), new ItemStack(Items.BLAZE_POWDER, 4)));
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntitySerpent, Sets.newHashSet(new ItemStack(ModObjects.adders_fork, 3)));
		
		BewitchmentAPI.ATHAME_LOOT.put(e -> e instanceof EntityDemon, Sets.newHashSet(new ItemStack(ModObjects.demon_heart)));
	}
	
	private static void furnaceInit() {
		GameRegistry.addSmelting(ModObjects.amethyst_ore, new ItemStack(ModObjects.amethyst), 0.85f);
		GameRegistry.addSmelting(ModObjects.garnet_ore, new ItemStack(ModObjects.garnet), 0.85f);
		GameRegistry.addSmelting(ModObjects.moonstone_ore, new ItemStack(ModObjects.moonstone), 0.85f);
		GameRegistry.addSmelting(ModObjects.silver_ore, new ItemStack(ModObjects.silver_ingot), 0.65f);
		GameRegistry.addSmelting(ModObjects.salt_ore, new ItemStack(ModObjects.salt), 0.35f);
		
		GameRegistry.addSmelting(ModObjects.embittered_bricks, new ItemStack(ModObjects.cracked_embittered_bricks), 0.1f);
		GameRegistry.addSmelting(ModObjects.scorned_bricks[0], new ItemStack(ModObjects.cracked_scorned_bricks), 0.1f);
		
		GameRegistry.addSmelting(ModObjects.cypress_wood, new ItemStack(Items.COAL, 1, 1), 0.15f);
		GameRegistry.addSmelting(ModObjects.elder_wood, new ItemStack(Items.COAL, 1, 1), 0.15f);
		GameRegistry.addSmelting(ModObjects.juniper_wood, new ItemStack(Items.COAL, 1, 1), 0.15f);
		GameRegistry.addSmelting(ModObjects.yew_wood, new ItemStack(Items.COAL, 1, 1), 0.15f);
		
		GameRegistry.addSmelting(ModObjects.unfired_jar, new ItemStack(ModObjects.empty_jar), 0.15f);
	}
	
	private static void furnacePostInit() {
		for (Block block : ForgeRegistries.BLOCKS) {
			if (block instanceof BlockSapling && FurnaceRecipes.instance().getSmeltingResult(new ItemStack(block)).isEmpty()) GameRegistry.addSmelting(block, new ItemStack(ModObjects.wood_ash, 4), 0.15f);
		}
		if (Loader.isModLoaded("dynamictrees")) for (Item item : ForgeRegistries.ITEMS)
			if (item instanceof Seed && !item.getRegistryName().toString().toLowerCase().contains("cactus") && FurnaceRecipes.instance().getSmeltingResult(new ItemStack(item)).isEmpty())
				GameRegistry.addSmelting(item, new ItemStack(ModObjects.wood_ash, 4), 0.15f);
	}
	
	private static void ovenInit() {
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "oak_spirit"), new ItemStack(Blocks.SAPLING, 1), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.oak_spirit), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "oak_spirit_alt"), new ItemStack(Blocks.SAPLING, 1, 5), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.oak_spirit), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "spruce_heart"), new ItemStack(Blocks.SAPLING, 1, 1), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.spruce_heart), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "birch_soul"), new ItemStack(Blocks.SAPLING, 1, 2), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.birch_soul), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "cloudy_oil"), new ItemStack(Blocks.SAPLING, 1, 3), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.cloudy_oil), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "acacia_resin"), new ItemStack(Blocks.SAPLING, 1, 4), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.acacia_resin), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "ebb_of_death"), new ItemStack(ModObjects.cypress_sapling), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.ebb_of_death), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "droplet_of_wisdom"), new ItemStack(ModObjects.elder_sapling), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.droplet_of_wisdom), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "liquid_witchcraft"), new ItemStack(ModObjects.juniper_sapling), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.liquid_witchcraft), 0.75f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "essence_of_vitality"), new ItemStack(ModObjects.yew_sapling), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(ModObjects.essence_of_vitality), 0.75f));
		
		if (Loader.isModLoaded("dynamictrees")) {
			for (TreeFamilyVanilla family : ModTrees.baseFamilies) {
				Species species = family.getCommonSpecies();
				String name = species.getSaplingName().toString().toLowerCase();
				name = name.substring(name.indexOf(":") + 1);
				BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, name), species.getSeedStack(1), new ItemStack(ModObjects.wood_ash, 4), new ItemStack(name.contains("oak") ? ModObjects.oak_spirit : name.contains("spruce") ? ModObjects.spruce_heart : name.contains("birch") ? ModObjects.birch_soul : name.contains("acacia") ? ModObjects.acacia_resin : ModObjects.cloudy_oil), 0.75f));
			}
		}
		
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "cloudy_oil_alt0"), new ItemStack(Items.WHEAT), new ItemStack(Items.BREAD), new ItemStack(ModObjects.cloudy_oil), 0.55f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "cloudy_oil_alt1"), new ItemStack(Blocks.CACTUS), new ItemStack(Items.DYE, 1, 2), new ItemStack(ModObjects.cloudy_oil), 0.55f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "cloudy_oil_alt2"), new ItemStack(ModObjects.mandrake_root), new ItemStack(ModObjects.wood_ash), new ItemStack(ModObjects.cloudy_oil), 0.85f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "ectoplasm"), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.LEATHER), new ItemStack(ModObjects.ectoplasm, 3), 0.65f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "ectoplasm_alt"), new ItemStack(Items.BONE), new ItemStack(Items.DYE, 1, 15), new ItemStack(ModObjects.ectoplasm), 0.65f));
		
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "dimensional_sand"), new ItemStack(Items.ENDER_EYE), new ItemStack(Items.BLAZE_POWDER, 1, 0), new ItemStack(ModObjects.dimensional_sand, 4, 0), 0.35f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "dimensional_sand_alt0"), new ItemStack(Items.SHULKER_SHELL), new ItemStack(Items.CHORUS_FRUIT_POPPED, 1, 0), new ItemStack(ModObjects.dimensional_sand, 2, 0), 0.45f));
		BewitchmentAPI.REGISTRY_OVEN.register(new OvenRecipe(new ResourceLocation(Bewitchment.MODID, "dimensional_sand_alt1"), new ItemStack(Items.CHORUS_FRUIT), new ItemStack(Items.CHORUS_FRUIT_POPPED), new ItemStack(ModObjects.dimensional_sand, 2), 0.75f));
	}
	
	private static void distilleryInit() {
		BewitchmentAPI.REGISTRY_DISTILLERY.register(new DistilleryRecipe(new ResourceLocation(Bewitchment.MODID, "bottled_frostfire"), Arrays.asList(Util.get(Items.GLASS_BOTTLE), Util.get(ModObjects.perpetual_ice), Util.get(ModObjects.cleansing_balm), Util.get(ModObjects.fiery_unguent)), Collections.singletonList(new ItemStack(ModObjects.bottled_frostfire))));
		BewitchmentAPI.REGISTRY_DISTILLERY.register(new DistilleryRecipe(new ResourceLocation(Bewitchment.MODID, "cleansing_balm"), Arrays.asList(Util.get(ModObjects.acacia_resin), Util.get("cropWhiteSage"), Util.get("salt"), Util.get("cropGarlic")), Arrays.asList(new ItemStack(ModObjects.cleansing_balm), new ItemStack(ModObjects.wood_ash))));
		BewitchmentAPI.REGISTRY_DISTILLERY.register(new DistilleryRecipe(new ResourceLocation(Bewitchment.MODID, "demonic_elixir"), Arrays.asList(Util.get(ModObjects.demon_heart), Util.get(ModObjects.fiery_unguent), Util.get(ModObjects.empty_jar), Util.get(ModObjects.empty_jar)), Collections.singletonList(new ItemStack(ModObjects.demonic_elixir, 3))));
		BewitchmentAPI.REGISTRY_DISTILLERY.register(new DistilleryRecipe(new ResourceLocation(Bewitchment.MODID, "fiery_unguent"), Arrays.asList(Util.get(Items.BLAZE_POWDER), Util.get(ModObjects.cloudy_oil), Util.get("wax")), Collections.singletonList(new ItemStack(ModObjects.fiery_unguent))));
	}
	
	private static void spinningWheelInit() {
		BewitchmentAPI.REGISTRY_SPINNING_WHEEL.register(new SpinningWheelRecipe(new ResourceLocation(Bewitchment.MODID, "cobweb"), Arrays.asList(Util.get("string"), Util.get("string"), Util.get("string")), new ItemStack(Blocks.WEB)));
		BewitchmentAPI.REGISTRY_SPINNING_WHEEL.register(new SpinningWheelRecipe(new ResourceLocation(Bewitchment.MODID, "witches_stitching"), Arrays.asList(Util.get("string"), Util.get("string"), Util.get(ModObjects.liquid_witchcraft), Util.get(ModObjects.liquid_witchcraft)), new ItemStack(ModObjects.witches_stitching, 2)));
		BewitchmentAPI.REGISTRY_SPINNING_WHEEL.register(new SpinningWheelRecipe(new ResourceLocation(Bewitchment.MODID, "diabolical_vein"), Arrays.asList(Util.get(ModObjects.witches_stitching), Util.get(ModObjects.witches_stitching), Util.get(ModObjects.fiery_unguent), Util.get(ModObjects.heart)), new ItemStack(ModObjects.diabolical_vein, 2)));
		BewitchmentAPI.REGISTRY_SPINNING_WHEEL.register(new SpinningWheelRecipe(new ResourceLocation(Bewitchment.MODID, "pure_filament"), Arrays.asList(Util.get(ModObjects.witches_stitching), Util.get(ModObjects.witches_stitching), Util.get(ModObjects.acacia_resin), Util.get("cropWhiteSage")), new ItemStack(ModObjects.pure_filament, 2)));
	}
	
	private static void frostfireInit() {
		BewitchmentAPI.REGISTRY_FROSTFIRE.register(new FrostfireRecipe(new ResourceLocation(Bewitchment.MODID, "cold_iron_ingot"), Util.get("oreIron"), new ItemStack(ModObjects.cold_iron_ingot)));
		if (!Arrays.asList(Util.get("clusterIron").getMatchingStacks()).isEmpty())
			BewitchmentAPI.REGISTRY_FROSTFIRE.register(new FrostfireRecipe(new ResourceLocation(Bewitchment.MODID, "cold_iron_cluster"), Util.get("clusterIron"), new ItemStack(ModObjects.cold_iron_nugget, 18)));
		if (!Arrays.asList(Util.get("dustIron", "gritIron").getMatchingStacks()).isEmpty())
			BewitchmentAPI.REGISTRY_FROSTFIRE.register(new FrostfireRecipe(new ResourceLocation(Bewitchment.MODID, "cold_iron_ingot_alt"), Util.get("dustIron", "gritIron"), new ItemStack(ModObjects.cold_iron_ingot)));
		if (!Arrays.asList(Util.get("dustTinyIron").getMatchingStacks()).isEmpty())
			BewitchmentAPI.REGISTRY_FROSTFIRE.register(new FrostfireRecipe(new ResourceLocation(Bewitchment.MODID, "cold_iron_nugget"), Util.get("dustTinyIron"), new ItemStack(ModObjects.cold_iron_nugget)));
	}
	
	private static void fortuneInit() {
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneBadLuck());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneGoodLuck());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneIllness());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneVitality());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetPet());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetMerchant());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetDemon());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetSerpent());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetBlaze());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetDireWolf());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetSilverfish());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetWitch());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneMeetZombie());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneDeath());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneDropItem());
		BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneTreasure());
		if (Bewitchment.config.enableCatsAndDogsFortune) BewitchmentAPI.REGISTRY_FORTUNE.register(new FortuneCatsAndDogs());
	}
	
	private static void tarotInit() {
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "player"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/00player.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return BewitchmentAPI.isWitch(player);
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "witch"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/01witch.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when spells are added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "enderman"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/02enderman.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when end infusion is added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "cat"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/03cat.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when infusions are added
			}
			
			@Override
			public boolean isReversed(EntityPlayer player) {
				return false; //change when infusions are added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "guardian"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/04guardian.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return getNumber(player) > 0;
			}
			
			@Override
			public int getNumber(EntityPlayer player) {
				return player.getCapability(ExtendedPlayer.CAPABILITY, null).uniqueDefeatedBosses.tagCount();
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "illusioner"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/05illusioner.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return !player.getActivePotionEffects().isEmpty();
			}
			
			@Override
			public int getNumber(EntityPlayer player) {
				int max = 0;
				for (PotionEffect effect : player.getActivePotionEffects()) if (effect.getAmplifier() > max) max = effect.getAmplifier();
				return max;
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "companions"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/06companions.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when familiars are added
			}
			
			@Override
			public boolean isReversed(EntityPlayer player) {
				return false; //change when familiars are added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "mounts"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/07mounts.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when familiars are added, and add compat for respawnable pets
			}
			
			@Override
			public int getNumber(EntityPlayer player) {
				return 0; //change when familiars are added, and add compat for respawnable pets
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "silver_sword"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/08silver_sword.png")) {
			@Override
			public boolean isReversed(EntityPlayer player) {
				return BewitchmentAPI.isWitchHunter(player);
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "evoker"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/09evoker.png")) {
			@Override
			public boolean isReversed(EntityPlayer player) {
				return BewitchmentAPI.isSpectre(player);
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "diamonds"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/10diamonds.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return player.getCapability(ExtendedPlayer.CAPABILITY, null).fortune != null;
			}
			
			@Override
			public boolean isReversed(EntityPlayer player) {
				return isCounted(player) && player.getCapability(ExtendedPlayer.CAPABILITY, null).fortune.isNegative;
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "iron_golem"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/11iron_golem.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when overworld infusion is added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "zombie"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/12zombie.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when protection poppets are added
			}
			
			@Override
			public int getNumber(EntityPlayer player) {
				return 0; //change when protection poppets are added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "wither_skeleton"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/13wither_skeleton.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when nether infusion is added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "villager"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/14villager.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when covens are added
			}
			
			@Override
			public int getNumber(EntityPlayer player) {
				return 0; //change when covens are added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "wither"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/15wither.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when pacts are added
			}
			
			@Override
			public int getNumber(EntityPlayer player) {
				return 0; //change when pacts are added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "ender_dragon"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/16ender_dragon.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return getNumber(player) > 0;
			}
			
			@Override
			public int getNumber(EntityPlayer player) {
				return player.getCapability(ExtendedPlayer.CAPABILITY, null).mobsKilled / 50;
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "star"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/17star.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when dream infusion is added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "moon"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/18moon.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return BewitchmentAPI.isVampire(player) || BewitchmentAPI.isWerewolf(player);
			}
			
			@Override
			public boolean isReversed(EntityPlayer player) {
				return BewitchmentAPI.isWerewolf(player);
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "sun"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/19sun.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //change when curses are added
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "stronghold"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/20stronghold.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return getNumber(player) > 0;
			}
			
			@Override
			public int getNumber(EntityPlayer player) {
				return player.getCapability(ExtendedPlayer.CAPABILITY, null).ritualsCast;
			}
		});
		BewitchmentAPI.REGISTRY_TAROT.register(new Tarot(new ResourceLocation(Bewitchment.MODID, "world"), new ResourceLocation(Bewitchment.MODID, "textures/gui/tarot/21world.png")) {
			@Override
			public boolean isCounted(EntityPlayer player) {
				return false; //find something for this
			}
		});
	}
}
