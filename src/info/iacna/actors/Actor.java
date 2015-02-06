package info.iacna.actors;

import info.iacna.level.Grid;
import info.iacna.level.World;

public abstract class Actor {
	
	protected int x,y;
	protected Grid grid;
	protected World world;
	protected int r,g,b;
	protected int imageId;
	//protected boolean dead;
	protected int health;
	protected int poisoned;
	public int element, aR, fR, eR, wR, aD, fD, eD, wD;
	protected String name;
	protected boolean printedDead;

	public Actor(Grid grid)
	{
		this.grid = grid;
		grid.getOccupants().add(this);
		world = grid.getWorld();
		r = 255;
		g = 0;
		b = 0;
		element = (int)(Math.random()*4);
		setElement(0,0,0,0,0,0,0,0);
		printedDead = false;
		imageId = (int) (Math.random()*4);
		poisoned = 0;
	}

	public void act()
	{

	}

	public void waddle()
	{
		move((int)(Math.random()*3) - 1, (int)(Math.random()*3) - 1);
	}

	public void setElement(int aR, int fR, int eR, int wR, int aD, int fD, int eD, int wD)
	{
		this.aR = aR;
		this.fR = fR;
		this.eR = eR;
		this.wR = wR;
		this.aD = aD;
		this.fD = fD;
		this.eD = eD;
		this.wD = wD;
	}
	
	public void removeSelfFromWorld()
	{
		for (int i = 0; i < grid.getOccupants().size(); i++)
		{
			if (grid.getOccupants().get(i) == this)
			{
				grid.getOccupants().remove(i);
				grid = null;
				world = null;
				return;
			}
		}
	}
	
	public void setPoisoned(int poisoned)
	{
		this.poisoned = poisoned;
	}

	public String getName()
	{
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public int XD(Actor actor)
	{
		return actor.getX() - x;
	}

	public int YD(Actor actor)
	{
		return actor.getY() - y;
	}
	
	public int XD(int x)
	{
		return x - this.x;
	}
	
	public int YD(int y)
	{
		return y - this.y;
	}
	
	public String rafs(String string)
	{
		for (int i = 0; i < string.length(); i++)
		{
			if (string.charAt(i) == '/') return string.substring(i+1);
		}
		return null;
	}
	
	public String rufs(String string)
	{
		for (int i = 0; i < string.length(); i++)
		{
			if (string.charAt(i) == '/') return string.substring(0,i);
		}
		return null;
	}
	
	public void moveTo(int x, int y)
	{
		this.x = x;
		this.y = y;
		wrap();
	}

	public boolean move(int x, int y)
	{
		int newX = this.x + x;
		int newY = this.y + y;
		if (grid.findActor(newX, newY) == null)
		{
			if (grid.findActor(newX, newY) instanceof Door)
			{
				if (!((Door) grid.findActor(newX, newY)).isOpen())
				{
					return true;
				}
			}
			this.x += x;
			this.y += y;
			wrap();
			return false;
		}
		if (grid.findActor(newX, newY) instanceof DroppedItem)
		{
			/*this.x += x;
			this.y += y;*/
			//wrap();
			return true;
		}
		return true;
	}

	public void wrap()
	{
		if (x < 0) x = 0;
		if (x > grid.getRows()) x = grid.getRows();
		if (y < 0) y = 0;
		if (y > grid.getCols()) y = grid.getCols();
	}

	public void setColor(int r, int g, int b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int getHealth() {
		return health;
	}
	
	public boolean inBounds(int r, int c)
	{
		return r > 0 && c > 0 && r < grid.getRows() && c < grid.getCols();
	}

	public boolean decrementHealth(int health) {
		this.health -= health;
		if (health != 0 && !printedDead)
			world.getRunner().messageToConsole(getName() + " took " + health + " damage.");
		if (this.health <= 0) 
		{
			if (!printedDead)
			{
				printedDead = true;
				world.getRunner().messageToConsole(getName() + " died!");
			}
			else 
			{
				try {removeSelfFromWorld();} catch (NullPointerException e) {}
			}
			return true;
		}
		return false;
	}
	
	public int getImageId() {
		// TODO Auto-generated method stub
		return imageId;
	}

}
