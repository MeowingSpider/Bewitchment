package com.bewitchment.client.block.tile.render;

import com.bewitchment.common.block.tile.entity.TileEntityGemBowl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityGemBowl extends TileEntitySpecialRenderer<TileEntityGemBowl>
{
	@Override
	public void render(TileEntityGemBowl tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if (!tile.inventory.getStackInSlot(0).isEmpty())
		{
			ItemStack stack = tile.inventory.getStackInSlot(0);
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.translate(x + 0.5, y + 0.145, z + 0.5);
			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.translate(0, -0.1, 0);
			GlStateManager.scale(0.8, 0.8, 0.8);
			IBakedModel model = ForgeHooksClient.handleCameraTransforms(Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, tile.getWorld(), null), TransformType.GROUND, false);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
}