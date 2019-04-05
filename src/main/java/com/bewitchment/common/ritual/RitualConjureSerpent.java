package com.bewitchment.common.ritual;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.registry.Ritual;
import com.bewitchment.common.block.BlockGlyph;
import com.bewitchment.common.block.tile.entity.TileEntityGlyph;
import com.bewitchment.common.entity.spirits.demons.EntitySerpent;
import com.bewitchment.registry.ModObjects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 4/5/2019.
 */
public class RitualConjureSerpent extends Ritual

{
	public RitualConjureSerpent() {
		super(Bewitchment.MOD_ID, "conjure_serpent", ofi(), ofe(), ofs(), 313, 2500, 6, BlockGlyph.GlyphType.NETHER, BlockGlyph.GlyphType.NETHER, null);
	}

	@Override
	public ItemStack[] getOutput(TileEntityGlyph tile) {
		List<ItemStack> out = new ArrayList<>();
		for (ItemStack stack : super.getOutput(tile)) out.add(stack);
		for (int i = 0; i < tile.inventory.getSlots(); i++) {
			ItemStack stack = tile.inventory.getStackInSlot(i);
			if (stack.getItem() == ModObjects.athame) {
				stack.setItemDamage(stack.getItemDamage() + 50);
				if (stack.getItemDamage() >= stack.getMaxDamage()) stack.shrink(1);
				if (!stack.isEmpty()) out.add(stack);
			}
		}
		return out.toArray(new ItemStack[out.size()]);
	}

	@Override
	public void onFinished(TileEntityGlyph tile, EntityPlayer caster) {
		if (!tile.getWorld().isRemote) {
			EntitySerpent entity = new EntitySerpent(tile.getWorld());
			entity.setLocationAndAngles(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), tile.getWorld().rand.nextInt(360), 0);
			for (EntityPlayerMP player : tile.getWorld().getEntitiesWithinAABB(EntityPlayerMP.class, entity.getEntityBoundingBox().grow(50)))
				CriteriaTriggers.SUMMONED_ENTITY.trigger(player, entity);
			tile.getWorld().spawnEntity(entity);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRandomDisplayTick(TileEntityGlyph tile) {
		for (int i = 0; i < 20; i++) {
			double cx = tile.getPos().getX() + 0.5, cy = tile.getPos().getY() + 0.5, cz = tile.getPos().getZ() + 0.5;
			double sx = cx + tile.getWorld().rand.nextGaussian() * 0.5, sy = cy + tile.getWorld().rand.nextGaussian() * 0.5, sz = cz + tile.getWorld().rand.nextGaussian() * 0.5;
			tile.getWorld().spawnParticle(EnumParticleTypes.FLAME, sx, sy, sz, 0.6 * (sx - cx), 0.6 * (sy - cy), 0.6 * (sz - cz));
		}
	}
}