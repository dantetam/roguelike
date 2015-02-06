package info.iacna.actors;

import java.util.ArrayList;

public class Item {

	private String name;
	private String formalName;
	private String type;
	private ArrayList<String> effects;
	private boolean equipped;
	private ArrayList<Item> inventory;
	
	public Item(String name)
	{
		effects = new ArrayList<String>();
		equipped = false;
		this.name = name;
		formalName = name;
	}
	
	public Item(String name, String type, String[] effects)
	{
		equipped = false;
		this.effects = new ArrayList<String>();
		this.name = name;
		this.type = type;
		addEffects(effects);
	}
	
	public Item(String name, String type, String[] effects, String formalName)
	{
		equipped = false;
		this.effects = new ArrayList<String>();
		this.name = name;
		this.type = type;
		this.formalName = formalName;
		addEffects(effects);
	}
	
	public Item(Item item)
	{
		effects = new ArrayList<String>();
		equipped = false;
		
		name = item.getName();
		type = item.getType();
		formalName = item.getFormalName();
		for (int i = 0; i < item.getEffects().size(); i++)
		{
			effects.add(item.getEffects().get(i));
		}
	}
	
	public boolean equals(Item item)
	{
		return name == item.getName() && type == item.getType();
	}
	
	public String getFormalName() {
		if (formalName == null)
			return name;
		return formalName;
	}

	public void addEffect(String effect)
	{
		effects.add(effect);
	}
	
	public void addEffects(String[] effects)
	{
		for (int i = 0; i < effects.length; i++)
		{
			this.effects.add(effects[i]);
		}
	}
	
	public ArrayList<String> getEffects()
	{
		return effects;
	}
	
	public ArrayList<Item> getInventory()
	{
		return inventory;
	}
	
	public void setInventory(ArrayList<Item> inventory)
	{
		this.inventory = inventory;
	}

	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}

	public boolean isEquipped()
	{
		return equipped;
	}
	
	public void setEquipped(boolean equipped)
	{
		this.equipped = equipped;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getName(boolean diff)
	{
		return "Item";
	}
	
}
