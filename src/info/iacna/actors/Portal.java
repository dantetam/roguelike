package info.iacna.actors;

import info.iacna.level.Grid;

public class Portal extends Actor {

	private int destination;
	
	public Portal(Grid grid, int destination) {
		super(grid);
		// TODO Auto-generated constructor stub
		r = 50;
		g = 50;
		b = 150;
		this.destination = destination;
	}

	public void removeSelfFromWorld()
	{
		world.getRunner().startGame(false);
		super.removeSelfFromWorld();
	}

	public int getDestination() {
		return destination;
	}
	
}
