package info.iacna.actors;

import info.iacna.level.Grid;

public class Door extends Actor {

	private boolean open;
	private boolean horizontal;
	
	public Door(Grid grid) {
		super(grid);
		// TODO Auto-generated constructor stub
	 	r = 140;
	 	g = 70;
	 	b = 20;
	 	open = false;
	 	horizontal = false;
	}
	
	public void toggleOpen()
	{
		open = !open;
	}
	
	public boolean isOpen()
	{
		return open;
	}
	
	public void setHorizontal()
	{
		horizontal = true;
	}

}
