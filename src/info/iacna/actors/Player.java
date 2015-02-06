package info.iacna.actors;

import java.util.ArrayList;
import java.util.HashMap;

import info.iacna.level.*;

public class Player extends Actor {

	private ArrayList<Item> inventory;
	private ArrayList<Spell> spells;
	private int maxHealth;
	private int damage;
	private int keys;
	//private HashMap<String, Integer> stats;
	private int ticksWithoutDamage, previousHealth;
	private int sight;
	private int numKills, recordNumKills;
	private int food, maxFood;
	public int previousX, previousY;
	private Merchant lastMerchant;
	private String[] actions = {"slash at","hack at","swing at","stab","lunge at","jab","charge towards","smash"};
	private String[] godActions = {
			"favors",
	};
	private String[] godSpeech = {

	};

	public Player(Grid grid) {
		super(grid);
		// TODO Auto-generated constructor stub
		r = 0;
		g = 255;
		b = 0;
		health = 200;
		maxHealth = 200;
		inventory = new ArrayList<Item>();
		spells = new ArrayList<Spell>();
		damage = 12;
		/*stats = new HashMap<String, Integer>();
		stats.put("Damage", 0);*/
		name = "You";
		keys = 0;
		ticksWithoutDamage = 0;
		previousHealth = health;
		sight = 3;
		numKills = 5;
		recordNumKills = 5;
		food = 500;
		maxFood = food;
		previousX = x;
		previousY = y;
		lastMerchant = null;
	}

	public void act()
	{
		food--;
		if (food <= 0)
		{
			food = 0;
			ticksWithoutDamage = 0;
			health--;
			return;
		}

		if (health >= previousHealth) 
			ticksWithoutDamage++;
		else 
			ticksWithoutDamage = 0;
		if (ticksWithoutDamage > 25) 
			health++;
		if (health > maxHealth) 
			health = maxHealth;
		previousHealth = health;

		for (int i = inventory.size() - 1; i >= 0; i--)
		{
			if (inventory.size() == 0) break;
			if (inventory.get(i).getName().equals("Corpse"))
			{
				if (Math.random() < 0.05)
				{
					//world.getRunner().messageToConsole("A corpse rots in your bag.");
					inventory.remove(i);
					give(new Consumable("Rotten Corpse", "Potion", new String[]{"Feed/150","Hurt/10"}));
				}
			}
			else if (inventory.get(i).getName().equals("Rotten Corpse"))
			{
				if (Math.random() < 0.025)
				{
					//world.getRunner().messageToConsole("A rotten corpse turns into bones.");
					inventory.remove(i);
					give(new Consumable("Bones", "Potion", new String[]{"Feed/50"}));
				}
			}
			else if (inventory.get(i).getName().equals("Bones"))
			{
				if (Math.random() < 0.025)
				{
					inventory.remove(i);
				}
			}
		}
	}

	public Item give(Item item)
	{
		inventory.add(new Item(item));
		//System.out.println(inventory);
		return item;
	}

	public Spell giveSpell(Spell spell)
	{
		spells.add(spell);
		return spell;
	}

	public ArrayList<Spell> getSpells()
	{
		return spells;
	}

	public ArrayList<Item> getInventory()
	{
		return inventory;
	}

	public void evaluateEffects()
	{
		damage = 5;
		maxHealth = 100;
		sight = 3;
		for (int i = 0; i < inventory.size(); i++)
		{
			if (inventory.get(i).isEquipped())
			{
				for (int j = 0; j < inventory.get(i).getEffects().size(); j++)
				{
					String effectText = inventory.get(i).getEffects().get(j);
					//System.out.println(effectText);
					if (effectText.contains("Damage"))
					{
						damage += Integer.parseInt(rafs(effectText));
						//System.out.println(damage);
					}
					else if (effectText.contains("Armor"))
					{
						maxHealth += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("aR"))
					{
						aR += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("fR"))
					{
						fR += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("eR"))
					{
						eR += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("wR"))
					{
						wR += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("aD"))
					{
						aD += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("fD"))
					{
						fD += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("eD"))
					{
						eD += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("wD"))
					{
						wD += Integer.parseInt(rafs(effectText));
					}
					else if (effectText.contains("Sight"))
					{
						sight += Integer.parseInt(rafs(effectText));
					}
				}
			}
		}
	}

	public void throwSomething(int xd, int yd, boolean swap)
	{
		if (swap)
		{
			int temp = xd;
			xd = yd;
			yd = temp;
		}
		//System.out.println(xd+","+yd);
		for (int i = inventory.size() - 1; i >= 0; i--)
		{
			if (inventory.get(i).getName().equals("Blue Potion"))
			{
				inventory.remove(i);
				for (int j = 0; j < grid.getOccupants().size(); j++)
				{
					if (Math.abs(grid.getOccupants().get(j).getX() - xd) < 3 
							&& Math.abs(grid.getOccupants().get(j).getY() - yd) < 3
							&& grid.getOccupants().get(j) instanceof Enemy)
					{
						grid.getOccupants().get(j).decrementHealth((int) (Math.random()*grid.getOccupants().get(j).getHealth()));
					}
				}
				return;
			}
		}
	}

	public boolean checkIfMoved()
	{
		return !(previousX == x && previousY == y);
	}

	public void consume(Item item)
	{
		for (int j = 0; j < item.getEffects().size(); j++)
		{
			String effectText = item.getEffects().get(j);
			//System.out.println(effectText);
			if (effectText.contains("Heal"))
			{
				health += Integer.parseInt(rafs(effectText));
				//System.out.println(damage);
			}
			else if (effectText.contains("Hurt"))
			{
				health -= Integer.parseInt(rafs(effectText));
				//System.out.println(damage);
			}
			else if (effectText.contains("Feed"))
			{
				feed(Integer.parseInt(rafs(effectText)));
				//System.out.println(damage);
			}
		}
	}

	public void cast(Spell spell)
	{
		for (int i = 0; i < spell.getEffects().size(); i++)
		{
			String effectText = spell.getEffects().get(i);
			//System.out.println(effectText);
			if (effectText.contains("Heal"))
			{
				health += Integer.parseInt(rafs(effectText));
				//System.out.println(damage);
			}
			else if (effectText.contains("Hurt"))
			{
				health -= Integer.parseInt(rafs(effectText));
				//System.out.println(damage);
			}
			else if (effectText.contains("Feed"))
			{
				feed(Integer.parseInt(rafs(effectText)));
				//System.out.println(damage);
			}
			else if (effectText.contains("Blink"))
			{
				do
				{
					moveTo(previousX,previousY);
					move((int)(Math.random()*5) - 3, (int)(Math.random()*5) - 3);
				} while (grid.findActor(x, y) != null);
			}
		}
	}

	public void removeSelfFromWorld()
	{
		world.getRunner().messageToConsole("Desire kills " + getName() + ".");
		world.getRunner().showConsole();
		for (int i = inventory.size() - 1; i >= 0; i--)
		{
			if (i <= -1) break;
			new DroppedItem(grid,inventory.get(i)).moveTo(x, y);
		}
		super.removeSelfFromWorld();

		//try {Thread.sleep(5000);} catch (InterruptedException e) {}
		//System.exit(0);
	}

	public ArrayList<DroppedItem> findLoot(int x, int y)
	{
		ArrayList<DroppedItem> loot = new ArrayList<DroppedItem>();
		for (int i = 0; i < grid.getOccupants().size(); i++)
		{
			if (x == grid.getOccupants().get(i).getX() && y == grid.getOccupants().get(i).getY())
			{
				if (grid.getOccupants().get(i).getClass().getSimpleName().equals("DroppedItem"))
					loot.add((DroppedItem) grid.getOccupants().get(i));
			}
		}
		return loot;
	}

	public boolean move(int x, int y, boolean diff)
	{
		feed(-1);
		//System.out.println(food);
		evaluateEffects();
		int newX = this.x + x;
		int newY = this.y + y;
		previousX = this.x;
		previousY = this.y;
		//System.out.println("hi "+previousX+","+previousY);
		if (super.move(x, y))
		{
			if (grid.findActor(newX, newY) instanceof Enemy)
			{
				Actor v = grid.findActor(newX, newY);
				v.decrementHealth((int)(damage*Math.random()));
				v.decrementHealth(aD*(100-v.aR)/100);
				v.decrementHealth(fD*(100-v.fR)/100);
				v.decrementHealth(eD*(100-v.eR)/100);
				v.decrementHealth(wD*(100-v.wR)/100);

				try {world.getRunner().messageToConsole("You " + actions[(int) (Math.random()*actions.length)] + " the " + ((Enemy)grid.findActor(newX, newY)).getName() + ".");}
				catch (ClassCastException e) {}
			}
			else if (grid.findActor(newX, newY).getClass().getSimpleName().equals("DroppedItem"))
			{
				ArrayList<DroppedItem> loot = findLoot(newX, newY);
				if (loot.size() == 1)
					world.getRunner().messageToConsole("You see a " + ((DroppedItem) grid.findActor(newX, newY)).getName() + " on the ground.");
				else if (loot.size() > 1)
				{
					String showThis = "You see ";
					for (int i = 0; i < loot.size() - 1; i++)
					{
						showThis += "a " + loot.get(i).getName() + ", ";
					}
					showThis += "and a " + loot.get(loot.size() - 1).getName() + " on the ground.";
					world.getRunner().messageToConsole(showThis);
				}
				this.x += x;
				this.y += y;
				return false;
			}
			else if (grid.findActor(newX, newY) instanceof Portal)
			{
				world.getRunner().messageToConsole("Moved to level " + ((Portal)grid.findActor(newX, newY)).getDestination());
				((Portal)grid.findActor(newX, newY)).removeSelfFromWorld();
			}
			else if (grid.findActor(newX, newY) instanceof Key)
			{
				world.getRunner().messageToConsole("Collected key #" + (keys+1));
				((Key)grid.findActor(newX, newY)).removeSelfFromWorld();
			}
			else if (grid.findActor(newX, newY) instanceof Door)
			{
				if (!((Door) grid.findActor(newX, newY)).isOpen())
				{
					if (Math.random() < 0.3) 
					{
						world.getRunner().messageToConsole("You kick the door open.");
						((Door) grid.findActor(newX, newY)).toggleOpen();
						moveTo(newX, newY);
					}
					else
					{
						world.getRunner().messageToConsole("The door remains shut.");
					}
				}
				else
				{
					super.moveTo(newX, newY);
				}
			}
			else if (grid.findActor(newX, newY) instanceof Merchant)
			{
				Merchant m = (Merchant) grid.findActor(newX, newY);
				lastMerchant = m;
				if (m.getItems().size() == 0)
				{
					world.getRunner().messageToConsole("I can't sell anything to you.");
					world.getRunner().messageToConsole("MERCHANT: ");
					return true;
				}
				world.getRunner().messageToConsole("------------------------------");
				for (int i = m.getItems().size() - 1; i >= 0; i--)
				{
					world.getRunner().messageToConsole(i + ". " + m.getItems().get(i).getName() + " for " + m.getPriceOfIndex(i));
				}
				world.getRunner().messageToConsole("Here's what I've got.");
				world.getRunner().messageToConsole("MERCHANT: ");
			}
			return true;
		}
		return false;
	}

	public void incrementKeys() {
		keys++;
	}

	public void incrementNumKills() {
		numKills++;
		recordNumKills++;
	}

	public Grid setGrid(Grid grid) {
		this.grid = grid;
		return grid;
	}

	public int getSight() {
		return sight;
	}

	public void setSight(int sight) {
		this.sight = sight;
	}

	public double getPercentageHP()
	{
		return (double)health/maxHealth;
	}

	public double getPercentageFood()
	{
		return (double)food/maxFood;
	}

	public int getNumKills() {
		return numKills;
	}

	public void changeNumKills(int numKills)
	{
		this.numKills += numKills;
	}

	public void feed(int food) {
		this.food += food;
		if (this.food > maxFood) this.food = maxFood;
	}

	public int getFood() {
		return food;
	}

	public Merchant getLastMerchant()
	{
		return lastMerchant;
	}

	/*public boolean decrementHealth(int health)
	{
		boolean temp = super.decrementHealth(health);
		if (health < 0) health = 0;
		return temp;
	}*/

}
