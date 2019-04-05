package com.bewitchment.api.registry;

import java.util.ArrayList;
import java.util.List;

import com.bewitchment.Bewitchment;
import com.bewitchment.common.block.tile.entity.util.ModTileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DistilleryRecipe extends IForgeRegistryEntry.Impl<DistilleryRecipe>
{
	private final List<Ingredient> input;
	private final List<ItemStack> output;
	private final int runningPower, time;
	
	public DistilleryRecipe(String modid, String name, List<Ingredient> input, List<ItemStack> output, int runningPower, int time)
	{
		this.setRegistryName(new ResourceLocation(modid, name));
		this.input = input;
		this.output = output;
		this.runningPower = runningPower;
		this.time = time;
	}
	
	/**
	 * @return the list of ItemStacks to be used as an input.
	 */
	public List<Ingredient> getInput()
	{
		return input;
	}
	
	/**
	 * @return the list of ItemStacks to be given as an output.
	 */
	public List<ItemStack> getOutput()
	{
		return output;
	}
	
	/**
	 * @return how much power is required to keep the recipe going.
	 */
	public int getRunningPower()
	{
		return runningPower;
	}
	
	/**
	 * @return how long the recipe should take to finish.
	 */
	public int getTime()
	{
		return time;
	}
	
	public boolean matches(ItemStackHandler handler)
	{
		List<ItemStack> checklist = new ArrayList<>();
		for (int i = 0; i < handler.getSlots(); i++) if (!handler.getStackInSlot(i).isEmpty()) checklist.add(handler.extractItem(i, 1, true));
		return Bewitchment.proxy.areISListsEqual(getInput(), checklist);
	}
	
	public boolean isValid(ItemStackHandler input, ItemStackHandler output)
	{
		for (ItemStack stack : getOutput()) if (ModTileEntity.getFirstValidSlot(output, stack) < 0) return false;
		return ModTileEntity.isEmpty(input);
	}
	
	public void giveOutput(ItemStackHandler input, ItemStackHandler output)
	{
		for (int i = 0; i < input.getSlots(); i++) input.extractItem(i, 1, false);
		for (ItemStack stack : getOutput()) output.insertItem(ModTileEntity.getFirstValidSlot(output, stack), stack.copy(), false);
	}
}