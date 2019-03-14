package com.bewitchment.common.item;

import com.bewitchment.api.capability.magicpower.MagicPower;
import com.bewitchment.common.item.util.ModItem;
import com.bewitchment.registry.ModObjects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemUndyingSalve extends ModItem
{
	public ItemUndyingSalve()
	{
		super("undying_salve");
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ)
	{
		player.setActiveHand(hand);
		player.getCapability(MagicPower.CAPABILITY, null).addBonus(getRegistryName().toString(), 75);
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
	{
		entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 1000, 1));
		entity.addPotionEffect(new PotionEffect(MobEffects.WITHER, 1000, 1));
		entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1000, 1));
		return new ItemStack(ModObjects.glass_jar);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 60;
	}
}