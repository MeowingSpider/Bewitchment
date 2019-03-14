package com.bewitchment.client.entity.render;

import com.bewitchment.Bewitchment;
import com.bewitchment.client.entity.model.ModelNewt;
import com.bewitchment.common.entity.EntityNewt;
import com.bewitchment.common.entity.util.FLEntityAnimal;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNewt extends RenderLiving<EntityNewt>
{
	private static final ResourceLocation[] TEX = {
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/newt_0.png"),
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/newt_1.png"),
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/newt_2.png"),
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/newt_3.png")};
	
	public RenderNewt(RenderManager manager)
	{
		super(manager, new ModelNewt(), 0.1f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityNewt entity)
	{
		return TEX[entity.getDataManager().get(FLEntityAnimal.SKIN)];
	}
	
	@Override
	protected void preRenderCallback(EntityNewt entity, float partialTickTime)
	{
		super.preRenderCallback(entity, partialTickTime);
		if (entity.isChild()) GlStateManager.scale(0.4, 0.4, 0.4);
		else GlStateManager.scale(0.6, 0.6, 0.6);
	}
}