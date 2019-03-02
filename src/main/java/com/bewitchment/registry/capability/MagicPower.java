package com.bewitchment.registry.capability;

import com.bewitchment.core.Bewitchment;
import com.bewitchment.registry.block.tile.TileEntityDistillery;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MagicPower
{
	int amount, max_amount, bonus_amount;
	
	public int getAmount()
	{
		return amount;
	}
	
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	
	public int getMaxAmount()
	{
		return max_amount;
	}
	
	public void setMaxAmount(int max_amount)
	{
		this.max_amount = max_amount;
	}
	
	public int getBonusAmount()
	{
		return max_amount;
	}
	
	public void setBonusAmount(int bonus_amount)
	{
		this.bonus_amount = bonus_amount;
	}
	
	public static class Storage implements IStorage<MagicPower>
	{
		@Override
		public NBTBase writeNBT(Capability<MagicPower> capability, MagicPower instance, EnumFacing side)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("amount", instance.getAmount());
			tag.setInteger("max_amount", instance.getMaxAmount());
			tag.setInteger("bonux_amount", instance.getBonusAmount());
			return tag;
		}
		
		@Override
		public void readNBT(Capability<MagicPower> capability, MagicPower instance, EnumFacing side, NBTBase nbt)
		{
			instance.setAmount(((NBTTagCompound)nbt).getInteger("amount"));
			instance.setMaxAmount(((NBTTagCompound)nbt).getInteger("max_amount"));
			instance.setBonusAmount(((NBTTagCompound)nbt).getInteger("bonux_amount"));
		}
	}

	public static class Provider implements ICapabilitySerializable<NBTTagCompound>
	{
		@CapabilityInject(MagicPower.class)
		public static final Capability<MagicPower> CAPABILITY = null;
		
		private MagicPower instance = CAPABILITY.getDefaultInstance();
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == CAPABILITY;
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			return capability == CAPABILITY ? CAPABILITY.<T>cast(instance) : null;
		}
		
		@Override
		public NBTTagCompound serializeNBT()
		{
			return (NBTTagCompound) CAPABILITY.getStorage().writeNBT(CAPABILITY, instance, null);
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			CAPABILITY.getStorage().readNBT(CAPABILITY, instance, null, nbt);
		}
	}

	public static class Handler
	{
		public static final ResourceLocation CAP = new ResourceLocation(Bewitchment.MOD_ID, "magic_power");
		
		@SubscribeEvent
		public void attachCapabilityE(AttachCapabilitiesEvent<Entity> event)
		{
			if (event.getObject() instanceof EntityPlayer) event.addCapability(CAP, new Provider());
		}
		
		@SubscribeEvent
		public void attachCapabilityTE(AttachCapabilitiesEvent<TileEntity> event)
		{
			if (event.getObject() instanceof TileEntityDistillery) event.addCapability(CAP, new Provider());
		}
		
		@SubscribeEvent
		public void livingTick(LivingEvent.LivingUpdateEvent event)
		{
			if (event.getEntityLiving() instanceof EntityPlayer)
			{
				MagicPower cap = event.getEntityLiving().getCapability(Provider.CAPABILITY, null);
				if (cap.getMaxAmount() <= 0)
				{
					cap.setAmount(800);
					cap.setMaxAmount(800);
					cap.setBonusAmount(0);
				}
			}
		}
		
		@SubscribeEvent
		public void clonePlayer(PlayerEvent.Clone event)
		{
			MagicPower oldC = event.getOriginal().getCapability(Provider.CAPABILITY, null), newC = event.getEntityPlayer().getCapability(Provider.CAPABILITY, null);
			newC.setAmount(oldC.getAmount());
			newC.setMaxAmount(oldC.getMaxAmount());
			newC.setBonusAmount(oldC.getBonusAmount());
		}
	}
}