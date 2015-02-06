package info.iacna.actors;

import info.iacna.level.Grid;

public class DroppedItem extends Actor {

	private Item item;
	
	public DroppedItem(Grid grid, Item item) {
		super(grid);
		this.item = item;
		r = 255;
		g = 0;
		b = 255;
	}
		
	public Item give(Player player)
	{
		Item temp = new Item(item);
		player.give(temp);
		removeSelfFromWorld();
		return temp;
	}
	
	public String getName()
	{
		return item.getFormalName();
	}
	
	public void act()
	{
		
	}
	
}
