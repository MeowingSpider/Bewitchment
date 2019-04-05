package com.bewitchment.common.handler;

import java.util.Random;

import com.bewitchment.registry.ModObjects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockDropHandler
{
	@SubscribeEvent
	public void harvestDrops(HarvestDropsEvent event)
	{
		replaceDrop(event, ModObjects.ore_salt, new ItemStack(ModObjects.salt, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_alexandrite, new ItemStack(ModObjects.gem_alexandrite, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_amethyst, new ItemStack(ModObjects.gem_amethyst, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_bloodstone, new ItemStack(ModObjects.gem_bloodstone, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_garnet, new ItemStack(ModObjects.gem_garnet, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_jasper, new ItemStack(ModObjects.gem_jasper, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_malachite, new ItemStack(ModObjects.gem_malachite, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_nuummite, new ItemStack(ModObjects.gem_nuummite, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_tigers_eye, new ItemStack(ModObjects.gem_tigers_eye, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		replaceDrop(event, ModObjects.ore_tourmaline, new ItemStack(ModObjects.gem_tourmaline, getFortuneDropAmount(event.getWorld().rand, event.getFortuneLevel())), 100, true, false);
		
		replaceDrop(event, ModObjects.leaves_juniper, new ItemStack(ModObjects.juniper_berries), 15, false, false);
		replaceDrop(event, ModObjects.leaves_yew, new ItemStack(ModObjects.yew_aril), 15, false, false);
	}
	
	private static int getFortuneDropAmount(Random rand, int fortuneLevel)
	{
		return fortuneLevel > 0 ? Math.max(0, rand.nextInt(fortuneLevel + 2)) : 1;
	}
	
	private static void replaceDrop(HarvestDropsEvent event, Block block, ItemStack out, int chance, boolean replace, boolean ignoreSilkTouch)
	{
		if (event.getState().getBlock() == block && (ignoreSilkTouch ? true : !event.isSilkTouching()))
		{
			if (replace) event.getDrops().clear();
			if (event.getWorld().rand.nextInt(100) <= chance) event.getDrops().add(out);
		}
	}
}