package com.bewitchment.common.block.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.bewitchment.Bewitchment;
import com.bewitchment.registry.IOreDictionaryContainer;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ModBlockLeaves extends BlockLeaves implements IOreDictionaryContainer
{
	private final List<String> ore_dictionary_names = new ArrayList<String>();
	
	private final ItemStack drop;
	
	public ModBlockLeaves(String name, ItemStack drop, String... ore_dictionary_names)
	{
		super();
		Bewitchment.proxy.registerValues(this, name, Material.LEAVES, SoundType.PLANT, 0.2f, 0, "shears", 0, ore_dictionary_names);
		setDefaultState(this.getBlockState().getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
		this.drop = drop;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DECAYABLE, CHECK_DECAY);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return drop.getItem();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = 0;
		meta += (state.getValue(DECAYABLE) ? 1 : 0);
		meta += (state.getValue(CHECK_DECAY) ? 2 : 1);
		return meta;
	}
	
	@Override
	public List<String> getOreDictionaryNames()
	{
		return ore_dictionary_names;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DECAYABLE, ((meta) & 1) == 1).withProperty(CHECK_DECAY, ((meta) & 2) > 0);
	}
	
	@Override
	public EnumType getWoodType(int meta)
	{
		return null;
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tile, ItemStack stack)
	{
		if (!world.isRemote && stack.getItem() instanceof ItemShears)
		{
			player.addStat(StatList.getBlockStats(this));
			spawnAsEntity(world, pos, new ItemStack(Item.getItemFromBlock(this)));
		}
		else super.harvestBlock(world, player, pos, state, tile, stack);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		this.leavesFancy = Bewitchment.proxy.isFancyGraphicsEnabled();
		return !this.leavesFancy;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return NonNullList.withSize(1, new ItemStack(this));
	}
}