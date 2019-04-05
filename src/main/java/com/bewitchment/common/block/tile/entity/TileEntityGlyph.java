package com.bewitchment.common.block.tile.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bewitchment.api.BewitchmentAPI;
import com.bewitchment.api.capability.magicpower.MagicPower;
import com.bewitchment.api.registry.Ritual;
import com.bewitchment.common.block.tile.entity.util.TileEntityAltarStorage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGlyph extends TileEntityAltarStorage implements ITickable
{
	private Ritual ritual;
	private BlockPos effectivePos = getPos();
	private UUID caster;
	private int cooldown = -1, effectiveDim = 0;
	
	public final ItemStackHandler inventory = new ItemStackHandler(Byte.MAX_VALUE)
	{
		@Override
	    public int getSlotLimit(int slot)
	    {
	        return Integer.MAX_VALUE;
	    }
	};
	
	@Override
	public void update()
	{
		if (ritual != null && caster != null)
		{
			EntityPlayer player = world.getPlayerEntityByUUID(caster);
			if (!world.isRemote)
			{
				if (MagicPower.attemptDrain(world, player, getAltarPosition(), ritual.getRunningPower() * (getEffectivePos() == getPos() ? 1 : MathHelper.ceil(getEffectivePos().distanceSq(getPos()) / 400))))
				{
					ritual.onUpdate(this, getWorld(), player, getEffectivePos(), getEffectiveDim(), cooldown);
					cooldown--;
				}
				else if (ritual.onLowPower(this, getWorld(), player, getEffectivePos(), getEffectiveDim(), cooldown)) stopRitual(player, false);
				if (cooldown <= 0 && ritual.getTime() >= 0) stopRitual(player, true);
			}
			else ritual.onRandomDisplayTick(this, getWorld(), player, getEffectivePos(), getEffectiveDim(), cooldown);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setString("ritual", ritual == null ? "" : ritual.getRegistryName().toString());
		tag.setLong("effectivePos", getEffectivePos().toLong());
		tag.setInteger("effectiveDim", getEffectiveDim());
		tag.setString("caster", caster == null ? "" : caster.toString());
		tag.setInteger("cooldown", cooldown);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		ritual = tag.getString("ritual").isEmpty() ? null : BewitchmentAPI.REGISTRY_RITUAL.getValue(new ResourceLocation(tag.getString("ritual")));
		setEffectivePos(BlockPos.fromLong(tag.getLong("effectivePos")));
		setEffectiveDim(tag.getInteger("effectiveDim"));
		caster = tag.getString("caster").isEmpty() ? null : UUID.fromString(tag.getString("caster"));
		cooldown = tag.getInteger("cooldown");
	}
	
	@Override
	public ItemStackHandler[] getInventories()
	{
		return new ItemStackHandler[] {inventory};
	}
	
	public void startRitual(EntityPlayer player)
	{
		if (!world.isRemote && cooldown < 0)
		{
			List<ItemStack> items_on_ground = new ArrayList<>();
			List<EntityItem> entityItemsOnGround = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos()).grow(3, 0, 3));
			for (EntityItem item : entityItemsOnGround) 
			{
				for (int i = 0; i < item.getItem().getCount(); i++)
				{
					ItemStack copy = item.getItem().copy();
					copy.setCount(1);
					items_on_ground.add(copy);
				}
			}
			List<EntityLivingBase> living_on_ground = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos()).grow(3, 0, 3));
			ritual = BewitchmentAPI.REGISTRY_RITUAL.getValuesCollection().parallelStream().filter(p -> p.matches(world, getPos(), items_on_ground, living_on_ground)).findFirst().orElse(null);
			if (ritual != null)
			{
				setEffectiveDim(world.provider.getDimension());
				if (ritual.isValid(this, getWorld(), player, getEffectivePos(), getEffectiveDim(), cooldown))
				{
					if (MagicPower.attemptDrain(world, player, getAltarPosition(), ritual.getStartingPower() * (getEffectivePos() == getPos() ? 1 : MathHelper.ceil(getEffectivePos().distanceSq(getPos()) / 400))))
					{
						world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
						caster = player.getPersistentID();
						cooldown = ritual.getTime();
						ritual.onStarted(this, getWorld(), player, getEffectivePos(), getEffectiveDim(), cooldown);
						world.playSound(null, getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.7f, 0.7f);
						player.sendStatusMessage(new TextComponentTranslation(ritual.getRegistryName().toString().replace(":", ".")), true);
						for (EntityItem item : entityItemsOnGround) inventory.insertItem(getFirstEmptySlot(inventory), item.getItem().splitStack(item.getItem().getCount()), false);
						if (!ritual.getInputEntities().isEmpty())
						{
							for (EntityLivingBase entity : living_on_ground)
							{
								if (ritual.getInputEntities().parallelStream().anyMatch(p -> p.getEntityClass().equals(entity.getClass())))
								{
									entity.attackEntityFrom(DamageSource.MAGIC, Integer.MAX_VALUE);
									break;
								}
							}
						}
						return;
					}
					else player.sendStatusMessage(new TextComponentTranslation("magic.no_power"), true);
				}
				else player.sendStatusMessage(new TextComponentTranslation("ritual.precondition"), true);
			}
			else player.sendStatusMessage(new TextComponentTranslation("ritual.invalid_input"), true);
			world.playSound(null, getPos(), SoundEvents.BLOCK_NOTE_SNARE, SoundCategory.BLOCKS, 1, 1);
		}
	}
	
	public void stopRitual(EntityPlayer player, boolean finished)
	{
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
		if (ritual != null)
		{
			if (finished)
			{
				ritual.onFinished(this, getWorld(), player, getEffectivePos(), getEffectiveDim(), cooldown);
				world.playSound(null, getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.7f, 0.7f);
			}
			else
			{
				ritual.onStopped(this, getWorld(), player, getEffectivePos(), getEffectiveDim(), cooldown);
				world.playSound(null, getPos(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 0.7f, 0.7f);
			}
		}
		clear(inventory);
		setEffectivePos(getPos());
		setEffectiveDim(getWorld().provider.getDimension());
		ritual = null;
		caster = null;
		cooldown = -1;
	}
	
	public Ritual getRitual()
	{
		return ritual;
	}
	
	public UUID getCaster()
	{
		return caster;
	}
	
	public BlockPos getEffectivePos()
	{
		return effectivePos;
	}
	
	public void setEffectivePos(BlockPos pos)
	{
		effectivePos = pos;
	}
	
	public int getEffectiveDim()
	{
		return effectiveDim;
	}
	
	public void setEffectiveDim(int dim)
	{
		effectiveDim = dim;
	}
}