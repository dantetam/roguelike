package info.iacna.actors;

import info.iacna.level.Grid;

public class Enemy extends Actor {

	private boolean passive;
	private int attackLevel;
	private int damage;
	private int lootId;
	private boolean special;
	public String displayLetter;

	public Enemy(Grid grid, int attackLevel, String name) {
		super(grid);
		// TODO Auto-generated constructor stub
		passive = false;
		this.attackLevel = attackLevel;
		health = world.getRunner().healthAtLevel[attackLevel];
		damage = world.getRunner().damageAtLevel[attackLevel];
		this.name = name;
		displayLetter = name.substring(0,1);
		lootId = -1;
		special = false;
		if (Math.random() < 0.05)
		{
			//System.out.println("lalala");
			special = true;
			this.name = world.getRunner().specialEnemyNames[(int) (Math.random()*world.getRunner().specialEnemyNames.length)] + " the " + this.name;
			attackLevel += (double)(attackLevel*Math.random());
		}
	}
	
	public void removeSelfFromWorldClean()
	{
		super.removeSelfFromWorld();
	}

	public void removeSelfFromWorld()
	{
		/*if (lootId == -1)
		{
			Item temp = world.getRunner().loot.get((int)(Math.random()*world.getRunner().loot.size()));
			for (int i = 0; i < temp.getEffects().size(); i++)
			{
				String replace = rufs(temp.getEffects().get(i)) + "/" + Integer.parseInt(rafs(temp.getEffects().get(i)))*((attackLevel+3)/3);
				temp.getEffects().set(i, replace);
				System.out.println(replace);
			}
			world.getPlayer().give(temp);
		}
		else
			world.getPlayer().give(world.getRunner().loot.get(lootId));

		world.getPlayer().feed(100);*/

		Item temp = world.getRunner().loot.get((int)(Math.random()*world.getRunner().loot.size()));
		Consumable corpse = new Consumable("Corpse", "Potion", new String[]{"Feed/200"});
		for (int i = 0; i < temp.getEffects().size(); i++)
		{
			try
			{
				String replace = rufs(temp.getEffects().get(i)) + "/" + Integer.parseInt(rafs(temp.getEffects().get(i)))*((attackLevel+3)/3);
				temp.getEffects().set(i, replace);
			}
			catch (NumberFormatException e)
			{
				//e.printStackTrace();
				temp.getEffects().set(i, rufs(temp.getEffects().get(i)) + "/1");
			}
			//System.out.println(replace);
		}
		new DroppedItem(grid,temp).moveTo(x,y);
		new DroppedItem(grid,corpse).moveTo(x,y);
		
		world.getPlayer().incrementNumKills();
		super.removeSelfFromWorld();
	}

	public boolean attack = false;
	public void act()
	{
		if (attack && !passive && (world.getPlayer().previousX != world.getPlayer().getX() || world.getPlayer().previousY != world.getPlayer().getY()))
		{
			moveTo(world.getPlayer().previousX,world.getPlayer().previousY);
			return;
		}
		if (grid.findActor(x + 1, y) != null || 
				grid.findActor(x - 1, y) != null || 
				grid.findActor(x, y + 1) != null || 
				grid.findActor(x, y - 1) != null)
		{
			if (grid.findActor(x + 1, y) instanceof Player || 
					grid.findActor(x - 1, y) instanceof Player || 
					grid.findActor(x, y + 1) instanceof Player ||
					grid.findActor(x, y - 1) instanceof Player)
			{
				attack(world.getPlayer());
				attack = true;
				return;
			}
		}
		//moveTowardsPlayer(world.getPlayer());
		attack = false;
		waddle();
	}

	@Override
	public boolean decrementHealth(int health) 
	{
		this.health -= health;
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

	public void moveTowardsPlayer(Player player)
	{
		if (XD(player) == 2 && YD(player) == 0) move(1,0);
		else if (XD(player) == -2 && YD(player) == 0) move(-1,0);
		else if (XD(player) == 0 && YD(player) == 2) move(0,1);
		else if (XD(player) == 0 && YD(player) == -2) move(0,-1);

		else if (XD(player) == -2 || XD(player) == -1) move(-1,0);
		else if (XD(player) == 1 || XD(player) == 2) move(1,0);

		/*else if (XD(player) == 2 && YD(player) == -1) move(1,-1);
		else if (XD(player) == 2 && YD(player) == -2) move(1,-1);
		else if (XD(player) == 1 && YD(player) == -2) move(1,-1);

		else if (XD(player) == -1 && YD(player) == -2) move(-1,-1);
		else if (XD(player) == -2 && YD(player) == -2) move(-1,-1);
		else if (XD(player) == -2 && YD(player) == -1) move(-1,-1);

		else if (XD(player) == 2 && YD(player) == -1) move(-1,1);
		else if (XD(player) == 2 && YD(player) == -2) move(-1,1);
		else if (XD(player) == 1 && YD(player) == -2) move(-1,1);*/

		else 
		{
			waddle();
		}
	}

	public void attack(Player player)
	{
		if (printedDead) return;
		player.decrementHealth(damage + (int)(Math.random()*attackLevel) - attackLevel/2);

		player.decrementHealth(aD*(100-player.aR)/100);
		player.decrementHealth(fD*(100-player.fR)/100);
		player.decrementHealth(eD*(100-player.eR)/100);
		player.decrementHealth(wD*(100-player.wR)/100);

		//world.getRunner().messageToConsole("Attacked player!");
		//System.out.println(player.getHealth());
	}
	
	public boolean isSpecial()
	{
		return special;
	}

}
