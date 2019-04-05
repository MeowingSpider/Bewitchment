package com.bewitchment.common.world.gen.tree;

import java.util.Random;

import com.bewitchment.common.world.gen.tree.util.WorldGenModTree;
import com.bewitchment.registry.ModObjects;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenElderTree extends WorldGenModTree
{
	public WorldGenElderTree(boolean notify)
	{
		super(notify);
	}
	
	@Override
	public boolean canSaplingGrow(World world, BlockPos pos)
	{
		for (int x = -1; x < 2; x++)
		{
			for (int z = -1; z < 2; z++)
			{
				for (int y = 0; y < 1; y++)
				{
					BlockPos current = pos.up(2).add(x, y, z);
					if (!world.getBlockState(current).getBlock().canBeReplacedByLeaves(world.getBlockState(current), world, current)) return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		int h = generateTrunk(world, ModObjects.log_elder.getDefaultState(), pos, rand, 3, 5);
		for (int x = -2; x < 3; x++)
		{
			for (int z = -2; z < 3; z++)
			{
				for (int y = -2; y < 1; y++)
				{
					BlockPos current = pos.up(h).add(x, y, z);
					if (world.getBlockState(current).getBlock().canBeReplacedByLeaves(world.getBlockState(current), world, current) && (Math.abs(z) != 2 || Math.abs(x) != 2 || rand.nextDouble() < 0.2) && (y < 0 || x < 2 && z < 2 && x > -2 && z > -2)) world.setBlockState(current, ModObjects.leaves_elder.getDefaultState());
				}
			}
		}
		return true;
	}
}