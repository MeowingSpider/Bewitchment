package com.bewitchment.common.handler;

import com.bewitchment.client.block.tile.gui.GuiDistillery;
import com.bewitchment.client.block.tile.gui.GuiOven;
import com.bewitchment.common.CommonProxy.ModGui;
import com.bewitchment.common.block.tile.container.ContainerDistillery;
import com.bewitchment.common.block.tile.container.ContainerOven;
import com.bewitchment.common.block.tile.entity.TileEntityDistillery;
import com.bewitchment.common.block.tile.entity.TileEntityOven;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile instanceof TileEntityDistillery) return new GuiDistillery((ContainerDistillery) getServerGuiElement(ModGui.DISTILLERY.ordinal(), player, world, x, y, z));
		if (tile instanceof TileEntityOven) return new GuiOven((ContainerOven) getServerGuiElement(ModGui.OVEN.ordinal(), player, world, x, y, z), player.inventory);
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile instanceof TileEntityDistillery) return new ContainerDistillery(player.inventory, (TileEntityDistillery) tile);
		if (tile instanceof TileEntityOven) return new ContainerOven(player.inventory, (TileEntityOven) tile);
		return null;
	}
}