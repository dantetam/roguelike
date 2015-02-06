package info.iacna.level;

import java.util.ArrayList;
import info.iacna.actors.*;

public class Grid {

	protected World world;
	//private Actor[][] grid;
	protected int rows, cols;
	protected int spawnX;
	protected int spawnY;
	protected ArrayList<Actor> occupants;
	
	private boolean revealed;
	
	public Grid(World world, int rows, int cols)
	{
		this.world = world;
		//grid = new Actor[rows][cols];
		occupants = new ArrayList<Actor>();
		this.rows = rows;
		this.cols = cols;
		revealed = true;
	}
	
	public Actor findActor(int x, int y)
	{
		ArrayList<Actor> actors = new ArrayList<Actor>();
		for (int i = 0; i < occupants.size(); i++)
		{
			if (x == occupants.get(i).getX() && y == occupants.get(i).getY())
			{
				actors.add(occupants.get(i));
				if (occupants.get(i) instanceof Player) return occupants.get(i);
			}
		}
		if (actors.size() > 0) return actors.get(0);
		return null;
	}
	
	public DroppedItem findDroppedItem(int x, int y) {
		for (int i = 0; i < occupants.size(); i++)
		{
			if (x == occupants.get(i).getX() && y == occupants.get(i).getY() && occupants.get(i) instanceof DroppedItem)
			{
				return (DroppedItem) occupants.get(i);
			}
		}
		return null;
	}
	
	public void setSpawn(int spawnX, int spawnY)
	{
		this.spawnX = spawnX;
		this.spawnY = spawnY; 
		//Wow, I can't believe I can finally type without looking at the keyboard. This is amazing. Did I have this ability all along?
	}
	
	public int getSpawnX()
	{
		return spawnX;
	}
	
	public int getSpawnY()
	{
		return spawnY;
	}
	
	public ArrayList<Actor> getOccupants()
	{
		return occupants;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public boolean isRevealed() {
		return revealed;
	}

	public void setRevealed(boolean revealed) {
		this.revealed = revealed;
	}

	public int getRows()
	{
		return rows;
	}
	
	public int getCols()
	{
		return cols;
	}

	public void addOccupant(Actor actor) {
		occupants.add(actor);
	}
	
}
