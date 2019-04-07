package com.bewitchment.client.integration.jei.category;

import java.util.Arrays;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.registry.OvenRecipe;
import com.bewitchment.client.integration.jei.category.OvenCategory.OvenWrapper;
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
import net.minecraft.util.ResourceLocation;

public class OvenCategory implements IRecipeCategory<OvenWrapper>
{
	public static final String UID = ModObjects.oven.getTranslationKey() + ".name";
	
	private IDrawable bg;
	
	public OvenCategory(IGuiHelper helper)
	{
		bg = helper.drawableBuilder(new ResourceLocation(Bewitchment.MOD_ID, "textures/gui/jei_oven.png"), 0, 0, 82, 54).setTextureSize(82, 54).build();
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
	public void setRecipe(IRecipeLayout recipeLayout, OvenWrapper recipeWrapper, IIngredients ingredients)
	{
		recipeLayout.getItemStacks().init(0, true, 0, 0);
		recipeLayout.getItemStacks().set(0, recipeWrapper.input);
		recipeLayout.getItemStacks().init(1, false, 60, 4);
		recipeLayout.getItemStacks().set(1, recipeWrapper.output);
		recipeLayout.getItemStacks().init(2, false, 60, 36);
		recipeLayout.getItemStacks().set(2, recipeWrapper.byproduct);
	}

	public static class OvenWrapper implements IRecipeWrapper
	{
		private ItemStack input, output, byproduct;
		
		public OvenWrapper(OvenRecipe recipe)
		{
			input = recipe.getInput();
			output = recipe.getOutput();
			byproduct = recipe.getByproduct();
		}
		
		@Override
		public void getIngredients(IIngredients ingredients)
		{
			ingredients.setInput(VanillaTypes.ITEM, input);
			ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(output, byproduct));
		}
	}
}