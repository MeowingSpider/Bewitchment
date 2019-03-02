package com.bewitchment.registry.block.tile;

import java.util.Random;

import com.bewitchment.core.Bewitchment;
import com.bewitchment.core.Bewitchment.API.OvenRecipe;
import com.bewitchment.registry.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityOven extends ModTileEntity
{
	private final Random rand = new Random();
	
	public String current_recipe = "";
	
	public int burn_time, fuel_burn_time, progress;
	
	public TileEntityOven()
	{
		super(5);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setString("current_recipe", current_recipe);
		tag.setInteger("burn_time", burn_time);
		tag.setInteger("fuel_burn_time", fuel_burn_time);
		tag.setInteger("progress", progress);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		current_recipe = tag.getString("current_recipe");
		burn_time = tag.getInteger("burn_time");
		fuel_burn_time = tag.getInteger("fuel_burn_time");
		progress = tag.getInteger("progress");
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing face)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, face);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing face)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this) : super.getCapability(capability, face);
	}
	
	@Override
	public boolean isItemValid(int index, ItemStack stack)
	{
		return index == 0 ? TileEntityFurnace.isItemFuel(stack) : index == 1 ? stack.getItem() == ModItems.empty_jar : index == 2;
	}
	
	@Override
	public void update()
	{
		OvenRecipe recipe = Bewitchment.API.REGISTRY_OVEN.getValuesCollection().parallelStream().filter(dr -> dr.matches(getStackInSlot(2))).findFirst().orElse(null);
		if (recipe == null)
		{
			current_recipe = "";
			progress = 0;
		}
		else if (!current_recipe.equals(recipe.getRegistryName().toString()) && (getStackInSlot(3).isEmpty() || (getStackInSlot(3).isItemEqual(recipe.getOutput()) && getStackInSlot(3).getCount() < getSlotLimit(3))) && (getStackInSlot(4).isEmpty() || (getStackInSlot(4).isItemEqual(recipe.getOutput()) && getStackInSlot(4).getCount() < getSlotLimit(4)))) current_recipe = recipe.getRegistryName().toString();
		if (burn_time > 0) burn_time--;
		if (!current_recipe.isEmpty())
		{
			if (burn_time == 0 && !getStackInSlot(0).isEmpty())
			{
				burn_time = TileEntityFurnace.getItemBurnTime(getStackInSlot(0));
				fuel_burn_time = burn_time;
				extractItem(0, 1, false);
			}
			else if (burn_time > 0)
			{
				progress++;
				if (progress >= 100)
				{
					progress = 0;
					extractItem(2, 1, false);
					ItemStack output = getStackInSlot(3);
					if (output.isItemEqual(recipe.getByproduct())) output.grow(1);
					else output = recipe.getOutput();
					insertItem(3, output, false);
					if (rand.nextFloat() <= recipe.getByproductChance())
					{
						extractItem(1, 1, false);
						ItemStack byproduct = getStackInSlot(4);
						if (byproduct.isItemEqual(recipe.getByproduct())) byproduct.grow(1);
						else byproduct = recipe.getByproduct();
						insertItem(4, byproduct, false);
					}
				}
			}
		}
		markDirty();
	}
}