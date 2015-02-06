package info.iacna.actors;

import info.iacna.level.Grid;

public class Key extends Actor {

	public Key(Grid grid) {
		super(grid);
		// TODO Auto-generated constructor stub
		r = 255;
		g = 255;
		b = 0;
	}

	public void removeSelfFromWorld()
	{
		world.getPlayer().incrementKeys();
		super.removeSelfFromWorld();
	}
	
}
