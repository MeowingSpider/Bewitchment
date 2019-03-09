package com.bewitchment.api.capability.magicpower;

public class MagicPowerCapability
{
	int amount = 0, max_amount = 0;
	
	public int getAmount()
	{
		return amount;
	}
	
	public MagicPowerCapability setAmount(int amount)
	{
		this.amount = amount;
		return this;
	}
	
	public int getMaxAmount()
	{
		return max_amount;
	}
	
	public MagicPowerCapability setMaxAmount(int max_amount)
	{
		this.max_amount = max_amount;
		return this;
	}
	
	public boolean fill(int amount)
	{
		if (getAmount() < getMaxAmount())
		{
			setAmount(Math.min(getAmount() + amount, getMaxAmount()));
			return true;
		}
		return false;
	}
	
	public boolean drain(int amount)
	{
		if (getAmount() > 0)
		{
			setAmount(Math.max(0, getAmount()) - amount);
			return true;
		}
		return false;
	}
}