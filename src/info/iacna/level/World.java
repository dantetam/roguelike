package info.iacna.level;

import info.iacna.run.*;
import info.iacna.actors.*;

import java.util.ArrayList;

public class World {
	protected ArrayList<Grid> grids;
	protected int activeGrid;
	
	private Runner runner;
	private Player player;
	
	public World(int numLevels)
	{
		grids = new ArrayList<Grid>();
		
		for (int i = 0; i < numLevels; i++)
		{
			addGrid(10,10);
		}
		
		activeGrid = 0;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	/**
	 * Adds one more level to the list of levels.
	 */
	public Grid addGrid(int rows, int cols)
	{
		Grid newGrid = new Grid(this,rows,cols);
		grids.add(newGrid);
		return newGrid;
	}
	
	/**
	 * Gets a level at a certain height.
	 * 0 is the top level.
	 * 1 is the level below it.
	 * @param height the desired height to look at
	 * @return the level at the height
	 */
	public Grid getGrid(int height)
	{
		return grids.get(height);
	}

	public int getLastGrid()
	{
		return grids.size() - 1;
	}
	
	public Grid getAG() {
		return getGrid(activeGrid);
	}

	public void setActiveGrid(int activeGrid) {
		this.activeGrid = activeGrid;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	public Grid addMazeGrid(int rows, int cols) {
		MazeGrid newGrid = new MazeGrid(this,rows,cols);
		grids.add(newGrid);
		return newGrid;
	}
	
}

