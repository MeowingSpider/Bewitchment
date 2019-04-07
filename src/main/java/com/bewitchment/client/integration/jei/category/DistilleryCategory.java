package com.bewitchment.client.integration.jei.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.registry.DistilleryRecipe;
import com.bewitchment.client.integration.jei.category.DistilleryCategory.DistilleryWrapper;
import com.bewitchment.registry.ModObjects;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class DistilleryCategory implements IRecipeCategory<DistilleryWrapper>
{
	public static final String UID = ModObjects.distillery.getTranslationKey() + ".name";
	
	private IDrawable bg;
	
	public DistilleryCategory(IGuiHelper helper)
	{
		bg = helper.drawableBuilder(new ResourceLocation(Bewitchment.MOD_ID, "textures/gui/jei_distillery.png"), 0, 0, 93, 54).setTextureSize(93, 54).build();
	}
	
	@Override
	public String getUid()
	{
		return UID;
	}
	
	@Override
	public String getTitle()
	{
		return I18n.format(UID);
	}
	
	@Override
	public String getModName()
	{
		return Bewitchment.MOD_NAME;
	}
	
	@Override
	public IDrawable getBackground()
	{
		return bg;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, DistilleryWrapper recipeWrapper, IIngredients ingredients)
	{
		for (int i = 0; i < recipeWrapper.input.size(); i++)
		{
			recipeLayout.getItemStacks().init(i, true, (i % 2) * 18, (i / 2) * 18);
			recipeLayout.getItemStacks().set(i, Arrays.asList(recipeWrapper.input.get(i).getMatchingStacks()));
		}
		for (int i = 0; i < recipeWrapper.output.size(); i++)
		{
			recipeLayout.getItemStacks().init(recipeWrapper.input.size() + i, false, 57 + (i % 2) * 18, (i / 2) * 18);
			recipeLayout.getItemStacks().set(recipeWrapper.input.size() + i, recipeWrapper.output.get(i));
		}
	}

	public static class DistilleryWrapper implements IRecipeWrapper
	{
		private List<Ingredient> input;
		private List<ItemStack> output;
		
		public DistilleryWrapper(DistilleryRecipe recipe)
		{
			input = recipe.getInput();
			output = recipe.getOutput();
		}
		
		@Override
		public void getIngredients(IIngredients ingredients)
		{
			List<List<ItemStack>> lists = new ArrayList<List<ItemStack>>();
			for (Ingredient ing : input) lists.add(Arrays.asList(ing.getMatchingStacks()));
			ingredients.setInputLists(VanillaTypes.ITEM, lists);
			ingredients.setOutputs(VanillaTypes.ITEM, output);
		}
	}
}