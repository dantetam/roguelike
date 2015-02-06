package info.iacna.actors;

import java.util.ArrayList;

import info.iacna.level.Grid;

public class Merchant extends Actor {

	private ArrayList<Item> items;
	public ArrayList<Integer> resPrices;
	private ArrayList<Item> acceptedItems;
	
	public Merchant(Grid grid) {
		super(grid);
		r = 46;
		g = 139;
		b = 87;
		items = new ArrayList<Item>();
		acceptedItems = new ArrayList<Item>();
		
		resPrices = new ArrayList<Integer>();
	}
	
	public ArrayList<Item> getItems()
	{
		return items;
	}
	
	public void addStoreItem(Item item, int price)
	{
		items.add(item);
		resPrices.add(price);
	}
	
	public void removeStoreItem(Item item)
	{
		for (int i = 0; i < items.size(); i++)
		{
			if (items.get(i).equals(item))
			{
				resPrices.remove(i);
				items.remove(i);
				return;
			}
		}
	}
	
	public void removeStoreItem(int index)
	{
		resPrices.remove(index);
		items.remove(index);
	}
	
	public boolean accepts(Item item)
	{
		for (int i = 0; i < acceptedItems.size(); i++)
		{
			if (acceptedItems.get(i).getName().equals(item.getName()))
				return true;
		}
		return false;
	}
	
	public int getPriceOfIndex(int index)
	{
		return resPrices.get(index);
	}
	
}
