package com.bewitchment.client.jei.components;

import java.util.ArrayList;
import java.util.Arrays;

import com.bewitchment.api.cauldron.IBrewModifier;
import com.bewitchment.common.cauldron.BrewData;
import com.bewitchment.common.cauldron.BrewData.BrewEntry;
import com.bewitchment.common.cauldron.BrewModifierListImpl;
import com.bewitchment.common.crafting.cauldron.CauldronRegistry;
import com.bewitchment.common.item.ModItems;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class BrewModifierWrapper implements IRecipeWrapper {
	
	ItemStack[] in;
	ArrayList<ItemStack> validBrews = new ArrayList<ItemStack>(CauldronRegistry.BREW_POTION_MAP.size());
	String name;
	
	public BrewModifierWrapper(IBrewModifier modifier) {
		in = modifier.getJEIStackRepresentative().getMatchingStacks();
		name = "modifier.description." + modifier.getRegistryName().toString().replace(':', '.');
		CauldronRegistry.BREW_POTION_MAP.forEach((brew, potion) -> addStackFor(potion));
	}
	
	private void addStackFor(Potion potion) {
		ItemStack stack = new ItemStack(ModItems.brew_phial_drink);
		BrewData bd = new BrewData();
		bd.addEntry(new BrewEntry(potion, new BrewModifierListImpl()));
		bd.saveToStack(stack);
		validBrews.add(stack);
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, Arrays.asList(Arrays.asList(in)));
		ingredients.setOutputLists(ItemStack.class, Arrays.asList(validBrews));
	}
	
	@Override
	public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		FontRenderer fr = mc.fontRenderer;
		String res = I18n.format(name);
		fr.drawString(res, (recipeWidth - fr.getStringWidth(res)) / 2, recipeHeight - fr.FONT_HEIGHT, 0, false);
	}
	
}
