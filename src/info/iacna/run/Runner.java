package info.iacna.run;

import java.util.ArrayList;
import java.util.HashMap;

import info.iacna.actors.*;
import info.iacna.level.*;

import processing.core.*;

//TODO: Quests!

public class Runner extends PApplet {

	public static final int width = 800;
	public static final int height = 800;
	public static final int ROWS = 41;
	public static final int COLS = 41;
	public static final int SIGHT_ROWS = 5;
	public static final int SIGHT_COLS = 5;

	private World newWorld;
	/*private Grid grid1;
	private Grid grid2;
	private Grid grid3;*/
	private Grid reserveGrid;
	private Player iacna;

	private String nameOfGrid;

	public int[] healthAtLevel = {8,17,27,38,51,65,80,96,115,135,158,183,210,241,274,311,352,397};
	public int[] damageAtLevel = {2,2,4,4,4,7,7,7,11,11,11,11,16,16,16,16,22,22,22,22,29,29,37,37,46};

	private ArrayList<String> console;
	public ArrayList<Item> loot;
	public ArrayList<Spell> spells;

	private boolean showInventory;
	private int selectingInvIndex;

	private int spawnMode;

	private PImage heart, backpack, sword, bread, playerFrame, title, satelitical, destiny;
	private ArrayList<PImage> textures;

	private PFont consolas;

	private String commandString;
	private boolean typingInConsole;
	private int gameMode;

	private ArrayList<String> cList = new ArrayList<String>();
	private ArrayList<String> hList = new ArrayList<String>();
	private boolean classicView;

	//private boolean advRender;
	public String[] enemyNames = {"Rat","Mouse","Bear","Rabbit","Dwarf","Bat","Spider","Troll","Ogre"};
	public int[] enemyLevels = {1,1,4,2,7,3,4,5,9};

	public String[] specialEnemyNames = {"Quetaire","Athos","Porthos","Aramis","d'Artagnan","Willzyx","Darius","Xerxes","Julius","Gunther","Navarre","Paul"};

	private HashMap<String, Integer> constants;

	private int flashMode;

	public void setup()
	{
		size(1800, 900);
		//setName("Destiny and Desire");
		frameRate(12);
		//textFont();
		textSize(20);
		noLoop();
		gameMode = 0;

		nameOfGrid = "reserveGrid";


		console = new ArrayList<String>();

		loot = new ArrayList<Item>();
		loot.add(new Item("Rock", "Weapon", new String[]{"Damage/3"}));
		loot.add(new Item("Knife", "Weapon", new String[]{"Damage/7"}));
		loot.add(new Item("Sword", "Weapon", new String[]{"Damage/10"}));
		loot.add(new Item("Glasses", "Glasses", new String[]{"Sight/4"}, "pair of Glasses"));
		loot.add(new Item("Leather Cap", "Head", new String[]{"Armor/20"}));
		loot.add(new Item("Iron Helmet", "Head", new String[]{"Armor/50"}));
		loot.add(new Item("Leather Vest", "Body", new String[]{"Armor/40"}));
		loot.add(new Item("Iron Body Armor", "Body", new String[]{"Armor/100"}));
		loot.add(new Item("Black Pants", "Leg", new String[]{"Armor/10"}, "pair of Black Pants"));
		loot.add(new Item("Iron Pants", "Leg", new String[]{"Armor/50", "pair of Iron Pants"}));
		loot.add(new Consumable("Red Potion", "Potion", new String[]{"Heal/20"}));
		loot.add(new Consumable("Blue Potion", "Potion", new String[]{"Hurt/20"}));

		spells = new ArrayList<Spell>();
		spells.add(new Spell("Blink","Nothing",new String[]{"Blink/2"},"Blink",1));
		spells.add(new Spell("Minor Heal","Nothing",new String[]{"Heal/20"},"Minor Heal",5));

		showInventory = true;
		selectingInvIndex = 0;

		spawnMode = 0;
		heart = loadImage("heart.gif");
		backpack = loadImage("backpack.gif");
		sword = loadImage("sword.gif");
		bread = loadImage("bread.gif");
		playerFrame = loadImage("player.gif");
		title = loadImage("destinyanddesire.gif");
		satelitical = loadImage("satelitical.gif");
		destiny = loadImage("destiny.gif");

		textures = new ArrayList<PImage>();
		/*textures.add(loadImage("texture1.gif"));
		textures.add(loadImage("texture2.gif"));*/
		textures.add(loadImage("texture3.gif"));
		textures.add(loadImage("texture4.gif"));
		textures.add(loadImage("texture5.gif"));
		textures.add(loadImage("texture6.gif"));

		//advRender = true;

		consolas = loadFont("Consolas.vlw");
		textFont(consolas, 20);

		messageToConsole("");
		messageToConsole("DESTINY: Good luck."); //bad oop design, actors should have a say function that writes to console
		//messageToConsole("Collect all 22 keys."); codeday legacy

		cList.add("Use WASD to walk");
		cList.add("Walk into enemies to attack them");
		cList.add("Q to cycle through your inventory");
		cList.add("E to equip a selected item");
		cList.add("R to drop a selected item");
		cList.add("G to pick up loot");
		cList.add("Hold Z and press a number to cast a spell");
		cList.add("X to recombine items");
		cList.add("C to toggle tile and ASCII view");

		hList.add("");

		commandString = "";

		classicView = true;

		constants = new HashMap<String, Integer>();
		//use this to gather up constants around the code

		//redraw();
		flashMode = 0;
	}

	public void draw()
	{
		smooth(2);
		background(0);
		if (gameMode == 2)
		{
			if (!classicView)
			{
				if (newWorld.getAG().isRevealed())
				{
					for (int i = newWorld.getAG().getOccupants().size() - 1; i >= 0; i--)
					{
						Actor actor = newWorld.getAG().getOccupants().get(i);
						stroke(actor.getR(),actor.getG(),actor.getB());
						fill(actor.getR(),actor.getG(),actor.getB());
						if (actor instanceof Key)
							rect(actor.getX()*width/newWorld.getAG().getRows() + 5, actor.getY()*height/newWorld.getAG().getCols() + 5, width/newWorld.getAG().getRows()*2/3, height/newWorld.getAG().getCols()*2/3);
						else if (actor instanceof Portal)
							ellipse(actor.getX()*width/newWorld.getAG().getRows() + width/40, actor.getY()*height/newWorld.getAG().getCols() + height/40, width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
						else if (actor instanceof Door)
						{
							if (((Door)(actor)).isOpen()) 
							{
								//rect(actor.getX()*width/newWorld.getAG().getRows(), actor.getY()*height/newWorld.getAG().getCols(), width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
							}
							else
							{
								rect(actor.getX()*width/newWorld.getAG().getRows(), actor.getY()*height/newWorld.getAG().getCols(), width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
							}
						}
						else
							rect(actor.getX()*width/newWorld.getAG().getRows(), actor.getY()*height/newWorld.getAG().getCols(), width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
					}
				}
				else 
				{
					for (int i = newWorld.getAG().getOccupants().size() - 1; i >= 0; i--)
					{
						Actor actor = newWorld.getAG().getOccupants().get(i);
						if (Math.abs(actor.XD(iacna)) <= iacna.getSight() && Math.abs(actor.YD(iacna)) <= iacna.getSight())
						{
							stroke(actor.getR(),actor.getG(),actor.getB());
							fill(actor.getR(),actor.getG(),actor.getB());
							if (actor instanceof Key)
								rect(actor.getX()*width/newWorld.getAG().getRows() + 5, actor.getY()*height/newWorld.getAG().getCols() + 5, width/newWorld.getAG().getRows()*2/3, height/newWorld.getAG().getCols()*2/3);
							else if (actor instanceof Portal)
								ellipse(actor.getX()*width/newWorld.getAG().getRows() + width/40, actor.getY()*height/newWorld.getAG().getCols() + height/40, width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
							/*else if (actor.getClass().getSimpleName().equals("Obstacle"))
					{
						PImage rTemp = textures.get(((Obstacle)actor).getImageId());
						image(rTemp,actor.getX()*width/newWorld.getAG().getRows(), actor.getY()*height/newWorld.getAG().getCols(), width/newWorld.getAG().getCols(), height/newWorld.getAG().getRows());
					}*/
							else if (actor.getClass().getSimpleName().equals("Door"))
							{
								if (((Door)(actor)).isOpen()) 
								{
									//rect(actor.getX()*width/newWorld.getAG().getRows(), actor.getY()*height/newWorld.getAG().getCols(), width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
								}
								else
								{
									rect(actor.getX()*width/newWorld.getAG().getRows(), actor.getY()*height/newWorld.getAG().getCols(), width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
								}
							}
							else
								rect(actor.getX()*width/newWorld.getAG().getRows(), actor.getY()*height/newWorld.getAG().getCols(), width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
						}
					}
					for (int i = 0; i <= newWorld.getAG().getRows(); i++)
					{
						for (int j = 0; j <= newWorld.getAG().getCols(); j++)
						{
							if (Math.abs(iacna.XD(j)) > iacna.getSight() || Math.abs(iacna.YD(i)) > iacna.getSight())
							{
								stroke(100,100,100);
								fill(100,100,100);
								rect(j*width/newWorld.getAG().getRows(), i*height/newWorld.getAG().getCols(), width/newWorld.getAG().getRows(), height/newWorld.getAG().getCols());
							}
						}
					}
				}
			}
			else
			{	
				double showSize = 1 + 2*SIGHT_ROWS;
				textSize((float) (width/showSize));

				/*for (int i = newWorld.getAG().getOccupants().size() - 1; i >= 0; i--)
				{
					Actor actor = newWorld.getAG().getOccupants().get(i);
					int x = actor.getX()*width/newWorld.getAG().getRows();
					int y = actor.getY()*height/newWorld.getAG().getCols();

					if (Math.abs(actor.XD(iacna)) <= iacna.getSight() && Math.abs(actor.YD(iacna)) <= iacna.getSight())
					{
						fill(255); 
						stroke(255);
						//stroke(actor.getR(),actor.getG(),actor.getB());
						//fill(actor.getR(),actor.getG(),actor.getB());
						if (actor instanceof Key)
						{
							fill(255,255,0);
							text("K",x,y);
						}
						else if (actor instanceof Portal)
						{
							text("<",x,y);
						}
						else if (actor.getClass().getSimpleName().equals("Door"))
						{
							fill(255,140,0);
							if (((Door)(actor)).isOpen()) 
							{
								text("+",x,y);
							}
							else
							{
								text("+",x,y);
							}
						}
						else if (actor instanceof Player)
						{
							fill(0,255,255);
							text("@",x,y);
						}
						else if (actor instanceof Enemy)
						{
							fill(255,0,0);
							text(actor.getName().substring(0,1).toUpperCase(),x,y);
						}
						else if (actor instanceof DroppedItem)
						{
							text("!",x,y);
						}
						else if (actor instanceof Obstacle)
						{
							text("#",x,y);
						}
						else if (actor instanceof Merchant)
						{
							fill(0,0,255);
							text("M",x,y);
						}
						else
						{
							text(".",x,y);
						}
					}
				}
				stroke(100);
				fill(100);
				for (int i = 0; i <= newWorld.getAG().getRows(); i++)
				{
					for (int j = 0; j <= newWorld.getAG().getCols(); j++)
					{
						if (Math.abs(iacna.XD(j)) > iacna.getSight() || Math.abs(iacna.YD(i)) > iacna.getSight())
						{
							text((char)127,j*width/newWorld.getAG().getRows(), i*height/newWorld.getAG().getCols());
						}
					}
				}*/
				int rr = 0; int cc = 0;
				for (int r = iacna.getX() - SIGHT_ROWS; r <= iacna.getX() + SIGHT_ROWS; r++)
				{
					for (int c = iacna.getY() - SIGHT_COLS; c <= iacna.getY() + SIGHT_COLS; c++)
					{
						int x = (int)(rr*(width/showSize));
						int y = (int)(cc*(width/showSize));
						if (findNumActors(newWorld.getAG(),r,c).size() == 1)
						{
							Actor actor = newWorld.getAG().findActor(r, c);
							fill(255); 
							stroke(255);
							//stroke(actor.getR(),actor.getG(),actor.getB());
							//fill(actor.getR(),actor.getG(),actor.getB());
							if (actor instanceof Key)
							{
								fill(255,255,0);
								text("K",x,y);
							}
							else if (actor instanceof Portal)
							{
								text("<",x,y);
							}
							else if (actor.getClass().getSimpleName().equals("Door"))
							{
								fill(255,140,0);
								if (((Door)(actor)).isOpen()) 
								{
									text("|",x,y);
								}
								else
								{
									text("+",x,y);
								}
							}
							else if (actor instanceof Player)
							{
								fill(0,255,255);
								text("@",x,y);
							}
							else if (actor instanceof Enemy)
							{
								fill(255,0,0);
								if (((Enemy)actor).isSpecial())
								{
									fill(255,255,0);
									textSize(10);
									text("!",(float) (x+(width/showSize)*0.5),y);
									textSize((float) (width/showSize));
									//println("special on the screen");
									fill(255,0,0);
									text(((Enemy)actor).displayLetter,x,y);
								}
								else
									text(actor.getName().substring(0,1),x,y);
							}
							else if (actor instanceof DroppedItem)
							{
								text("!",x,y);
							}
							else if (actor instanceof Obstacle)
							{
								text("#",x,y);
							}
							else if (actor instanceof Merchant)
							{
								fill(0,0,255);
								text("M",x,y);
							}
							else
							{
								text("?",x,y);
							}
						}
						else if (findNumActors(newWorld.getAG(),r,c).size() > 1)
						{
							if (findPriorityActor(newWorld.getAG(),r,c).equals("Player"))
							{
								fill(0,255,255);
								text("@",x,y);
							}
							else if (findPriorityActor(newWorld.getAG(),r,c).equals("Enemy"))
							{
								fill(255,0,0);
								text("!",x,y);
							}
							else
							{
								fill(255,255,255);
								text("!",x,y);
							}
						}
						else
						{
							//fill(100,100,100);
							//text(".",x,y);
						}
						cc++;
					}
					rr++;
					cc = 0;
				}
			}
			showConsole();
			showInventoryAndSpells();
			showHealthAndFood();
			showKills();
			//showPlayerFrame();

			fill(255);
			stroke(255);
			//rect(width*((float)(newWorld.getAG().getRows()+1)/(newWorld.getAG().getRows())),0,10,height);
			//rect(width*(float)13.2/10,0,10,height);
			if (iacna.getPercentageHP() < 0.25)
			{
				tint(200,0,0,(float) ((0.25-iacna.getPercentageHP())*4*255));
				image(destiny,0,0,width,height);
				tint(255,255,255,255);
			}
			else if (iacna.getPercentageFood() < 0.15)
			{
				if (Math.random() < 0.1) messageToConsole("You are starving.");
				tint(255,255,255,(float) ((0.15-iacna.getPercentageFood())*(100/15)*255));
				image(destiny,0,0,width,height);
				tint(255,255,255,255);
			}
			else if (flashMode > 0) 
			{
				flashMode--;
				tint(255,150/(10-flashMode));
				image(destiny,0,0,width,height);
				tint(255,255);
			}
			else if (flashMode == 0)
			{
				if (Math.random() < 0.001)
					flashMode = 10;
			}
		}
		else if (gameMode == 1)
		{
			image(destiny,0,0,width,height);
			text("Press N to continue",(float) (width*0.4),height);
			text((ROWS*COLS) + " tiles cannot contain Desire...",(float) (width*0.35),height+20);
		}
		else if (gameMode == 0)
		{
			image(title,0,0,1000,200);
			textSize(15);
			text("Press N to start a new game",100,height/2);
			text("Press H for a description",100,height/2 + 20);
			for (int i = 0; i < cList.size(); i++)
			{
				text(cList.get(i),500,height/2 + 20*i);
			}
		}
		//image(satelitical,width+50,500,100,100);
		//fill(255);
		textSize(15);
		//text("#Satelitical",width+50,625);
		//text("Loading " + (ROWS*COLS) + " tiles.",width+50,645);
		//text("Labs",width+50,685);
		fill(0);
		text("Hmmm...this isn't right.",1000,700);
		fill(255);
	}

	private ArrayList<Actor> findNumActors(Grid ag, int r, int c) {
		ArrayList<Actor> returnThis = new ArrayList<Actor>();
		for (int i = 0; i < ag.getOccupants().size(); i++)
		{
			if (ag.getOccupants().get(i).getX() == r && ag.getOccupants().get(i).getY() == c)
				returnThis.add(ag.getOccupants().get(i));
		}
		/*if (iacna.getX() == r && iacna.getY() == c) 
		{
			return new ArrayList<Actor>().add(iacna);
		}*/
		return returnThis;
	}

	public String findPriorityActor(Grid ag, int r, int c)
	{
		String[] priorityList = {"Obstacle","Player","Enemy","Merchant","DroppedItem","Door","Portal","Key"};
		Actor actor = ag.findActor(r,c);
		if (actor == null) return "Nothing";
		for (int i = 0; i < priorityList.length; i++)
		{
			if (actor.getClass().getSimpleName().equals(priorityList[i])) return priorityList[i];
		}
		return "Nothing";
	}

	public void startGame(boolean first)
	{
		if (first)
		{
			newWorld = new World(0);
			newWorld.setRunner(this);
			reserveGrid = null;
			reserveGrid = newWorld.addMazeGrid(ROWS,COLS);
			((MazeGrid) reserveGrid).findASpawn(true);
			iacna = new Player(reserveGrid);
			newWorld.setPlayer(iacna);

			iacna.give(loot.get(3));
			iacna.give(loot.get(10));
			iacna.give(loot.get(10));
			iacna.give(loot.get(10));
			iacna.give(loot.get(11));

			iacna.giveSpell(spells.get(1));

			iacna.moveTo(reserveGrid.getSpawnX(), reserveGrid.getSpawnY());
			gameMode = 2;
		}
		else
		{
			reserveGrid = null;
			reserveGrid = newWorld.addMazeGrid(ROWS,COLS);
			((MazeGrid) reserveGrid).findASpawn(true);
			iacna.moveTo(reserveGrid.getSpawnX(), reserveGrid.getSpawnY());

			changeLevel(newWorld.getLastGrid());
			reserveGrid.getWorld().setActiveGrid(reserveGrid.getWorld().getLastGrid());
			//((MazeGrid) reserveGrid).movePlayerToRandomSpawn();
		}
		redraw();
	}

	private boolean zPressed = false;
	private boolean qPressed = false;
	private boolean shifting = false;
	
	public void keyReleased()
	{
		if (key == 'z')
		{
			zPressed = false;
		}
		else if (key == 'v')
		{
			shifting = false;
		}
	}

	public void keyPressed()
	{
		if (key == 'v')
		{
			shifting = true;
			return;
		}
		if (shifting)
			executeActionOf(Character.toUpperCase(key));
		else
			executeActionOf(Character.toLowerCase(key));
	}

	public void executeActionOf(char key)
	{		
		//key = Character.toLowerCase(key);
		redraw();
		if (gameMode == 0)
		{
			if (key == 'n')
			{
				//System.out.println("What is the hell is going on?");
				gameMode = 1;
			}
			else if (key == 'h')
			{

			}
		}
		else if (gameMode == 1 && key == 'n')
		{
			startGame(true);
			redraw();
			return;
		}
		else if (gameMode == 2)
		{
			/*if (key == 'C') //obsolete
			{
				typingInConsole = !typingInConsole;
				return;
			}
			if (typingInConsole)
			{
				if (key == 10 && !commandString.equals(""))
				{
					doCommand(commandString);
					commandString = "";
				}
				else if (key >= 32 && key <= 122)
					commandString += key;
				else if (key == BACKSPACE && commandString.length() > 0)
					commandString = commandString.substring(0,commandString.length() - 1);
				redraw();
				return;
			}*/
			if (iacna.getHealth() < 0) return;
			try
			{
				if (key == 'w') 
				{
					iacna.move(0,-1,true);
					step();
				}
				else if (key == 'a') 
				{
					iacna.move(-1,0,true);
					step();
				}
				else if (key == 's') 
				{
					iacna.move(0,1,true);
					step();
				}
				else if (key == 'd') 
				{
					iacna.move(1,0,true);
					step();
				}
			} 
			catch (NullPointerException e)
			{
				messageToConsole("Reload to start again.");
			}

			if (key == 'z')
			{
				zPressed = true;
			}
			/*if (key == 'z')
			{
				messageToConsole("Resting.");
				step();
			}*/
			else if (key == 'Q')
			{
				System.out.println("show quests");
			}
			else if (key == 'q' && showInventory)
			{
				selectingInvIndex++;
				if (selectingInvIndex >= iacna.getInventory().size()) selectingInvIndex = 0;
			}
			else if (key == 'e' && showInventory)
			{
				if (selectingInvIndex >= iacna.getInventory().size()) selectingInvIndex = 0;
				if (iacna.getInventory().get(selectingInvIndex).getType().contains("Potion"))
				{
					//println(Math.random() + "consumed");
					for (int j = 0; j < iacna.getInventory().get(selectingInvIndex).getEffects().size(); j++)
					{
						iacna.consume(iacna.getInventory().get(selectingInvIndex));
					}
					messageToConsole("You consume a " + iacna.getInventory().get(selectingInvIndex).getFormalName() + ".");
					iacna.getInventory().remove(selectingInvIndex);
					//selectingInvIndex = 0;
					showInventoryAndSpells();
					redraw();
					step();
					return;
				}
				for (int i = iacna.getInventory().size() - 1; i >= 0; i--)
				{
					if (iacna.getInventory().get(i).getType().equals(iacna.getInventory().get(selectingInvIndex).getType()))
						iacna.getInventory().get(i).setEquipped(false);
				}
				try {
					iacna.getInventory().get(selectingInvIndex).setEquipped(true);
					messageToConsole("You equip a " + iacna.getInventory().get(selectingInvIndex).getFormalName() + ".");
				} catch (IndexOutOfBoundsException e) {}
				//println("consumed" + Math.random());
				//println(iacna.getInventory().get(selectingInvIndex).getClass().getSimpleName());
				step();
			}
			else if (key == 'x')
			{
				Item firstItem = null;
				Item secondItem = iacna.getInventory().get(selectingInvIndex);
				if (!secondItem.isEquipped())
				{
					for (int i = 0; i < iacna.getInventory().size(); i++)
					{
						if (iacna.getInventory().get(i).equals(secondItem) && i != selectingInvIndex)
							firstItem = iacna.getInventory().get(i);
					}
					if (firstItem != null)
					{
						System.out.println(firstItem + " " + secondItem);
					}
				}
			}
			else if (key == 'r' && showInventory && iacna.getInventory().size() > 0)
			{
				DroppedItem temp = new DroppedItem(newWorld.getAG(),iacna.getInventory().get(selectingInvIndex));
				temp.moveTo(iacna.getX(),iacna.getY());
				messageToConsole("You drop a " + temp.getName() + ".");
				iacna.getInventory().remove(selectingInvIndex);
				//selectingInvIndex = 0;
				showInventoryAndSpells();
				step();
			}
			else if (key == 'g')
			{
				if (newWorld.getAG().findDroppedItem(iacna.getX(),iacna.getY()) != null)
				{
					Item temp = newWorld.getAG().findDroppedItem(iacna.getX(),iacna.getY()).give(iacna);
					messageToConsole("You pick up a " + temp.getFormalName() + ".");
				}
				step();
			}
			else if (key == 'y')
			{

			}
			else if (key == 'u')
			{

			}
			else if (key == 'i')
			{
				spawnMode = 0;
			}
			else if (key == 'o')
			{
				spawnMode = 1;
			}
			else if (key == 'p')
			{
				spawnMode = 2;
			}
			else if (key == 'l')
			{
				newWorld.getAG().setRevealed(!newWorld.getAG().isRevealed());
			}
			else if (key == 'c')
			{
				classicView = !classicView;
				if (classicView) frameRate(20);
				else frameRate(12);
			}
			else if (key == 't')
			{
				if (iacna.checkIfMoved())
				{
					int xd = iacna.getX() - iacna.previousX;
					int yd = iacna.getY() - iacna.previousY;
					System.out.println(xd + "," + yd);
					if (xd == 1 && yd == 0) iacna.throwSomething(iacna.getY(),iacna.getX()+3,true);
					else if (xd == -1 && yd == 0) iacna.throwSomething(iacna.getY(),iacna.getX()-3,true);
					else if (xd == 0 && yd == 1) iacna.throwSomething(iacna.getY()+3,iacna.getX(),true);
					else if (xd == 0 && yd == -1) iacna.throwSomething(iacna.getY()-3,iacna.getX(),true);
				}
			}
			else if (Character.isDigit(key) && zPressed)
			{
				int spellNumber = Integer.parseInt(Character.toString(key));
				if (iacna.getSpells().size() - 1 >= spellNumber)
				{
					if (iacna.getSpells().get(spellNumber).cost <= iacna.getNumKills())
					{
						messageToConsole("You cast " + iacna.getSpells().get(spellNumber).getFormalName() + ".");
						iacna.changeNumKills(-iacna.getSpells().get(spellNumber).cost);
						iacna.cast(iacna.getSpells().get(spellNumber));
						step();
					}
					else
					{
						messageToConsole("You cannot cast " + iacna.getSpells().get(spellNumber).getFormalName() + ".");
					}
				}
			}
		}
		redraw();
	}

	private void doCommand(String string) {
		redraw();
		if (string.equals("clearall"))
		{
			newWorld.getAG().getOccupants().clear();
			messageToConsole(">" + string);
			return;
		}
		else if (string.equals("rl"))
		{
			iacna.give(new Item(loot.get((int)(Math.random()*loot.size()))));
			messageToConsole(">" + string);
			return;
		}
		else if (string.contains("randomloot"))
		{
			Item temp = new Item(loot.get((int)(Math.random()*loot.size())));
			for (int i = 0; i < temp.getEffects().size(); i++)
			{
				String replace = iacna.rufs(temp.getEffects().get(i)) + "/" + Integer.parseInt(iacna.rafs(temp.getEffects().get(i))) * Integer.parseInt(string.substring(string.length() - 1));
				temp.getEffects().set(i, replace);
				System.out.println(replace);
			}
			iacna.give(temp);
			messageToConsole(">" + string);
			return;
		}
		else if (string.contains("newgrid"))
		{
			reserveGrid = null;
			reserveGrid = newWorld.addMazeGrid(Integer.parseInt(string.substring(7,9)),Integer.parseInt(string.substring(9)));
			reserveGrid.setSpawn(1,1);
			changeLevel(newWorld.getLastGrid());
			messageToConsole(">" + string);
			return;
		}
		else if (string.contains("setnameofgrid"))
		{
			nameOfGrid = string.substring(13);
			messageToConsole(">" + string);
			return;
		}
		else if (string.equals("exit"))
		{
			showConsole();
			messageToConsole(">" + string);
			System.exit(0);
			return;
		}
		else if (string.equals("step"))
		{
			((MazeGrid) newWorld.getAG()).nextStep();
			messageToConsole(">" + string);
			return;
		}
		else if (string.contains("buy"))
		{
			if (iacna.getLastMerchant() == null) return;
			int index = -1;
			try {index = Integer.parseInt(string.substring(3));} catch (NumberFormatException e) {return;}
			if (index < iacna.getLastMerchant().getItems().size() && iacna.getNumKills() >= iacna.getLastMerchant().getPriceOfIndex(index))
			{
				iacna.changeNumKills(-iacna.getLastMerchant().getPriceOfIndex(index));
				iacna.give(iacna.getLastMerchant().getItems().get(index));
				iacna.getLastMerchant().removeStoreItem(index);
			}
			messageToConsole(">" + string);
			return;
		}
		messageToConsole(string + " is not a valid command");
		//showConsole();
	}

	public void mousePressed()
	{
		redraw();
		if (gameMode != 2) return;
		double row = newWorld.getAG().getRows()*(mouseX/(double)width);
		double col = newWorld.getAG().getCols()*(mouseY/(double)height);
		if (mouseX < width && mouseY < height)
		{
			if (spawnMode == 0)
			{
				new Enemy(newWorld.getAG(),0,"MadeByLevelEditor").moveTo((int)row,(int)col);
				println("new Enemy("+nameOfGrid+",0).moveTo(" + (int)row + "," + (int)col + ");");
			}
			else if (spawnMode == 1)
			{
				new Obstacle(newWorld.getAG()).moveTo((int)row,(int)col);
				println("new Obstacle("+nameOfGrid+").moveTo(" + (int)row + "," + (int)col + ");");
			}
			else if (spawnMode == 2)
			{
				new Key(newWorld.getAG()).moveTo((int)row,(int)col);
				println("new Key("+nameOfGrid+").moveTo(" + (int)row + "," + (int)col + ");");
			}
		}

		System.out.println(((MazeGrid)newWorld.getAG()).findAreaOfRoom((int)row, (int)col));
		((MazeGrid)newWorld.getAG()).clearNodes();

		if (mouseX > width*0.6 + 450 && mouseX < width*0.6 + 650 && mouseY > height*9/10 && mouseY < height*9/10 + 50)
			typingInConsole = !typingInConsole;
	}

	public void changeLevel(int newGridNumber)
	{
		try
		{
			newWorld.setActiveGrid(newGridNumber);
			iacna.setGrid(newWorld.getAG()).addOccupant(iacna);
			((MazeGrid)newWorld.getAG()).findASpawn(true);
			iacna.moveTo(newWorld.getAG().getSpawnX(),newWorld.getAG().getSpawnY());
		} catch (IndexOutOfBoundsException e)
		{
			newWorld = new World(0);
			newWorld.setRunner(this);

			reserveGrid = null;
			reserveGrid = newWorld.addMazeGrid(ROWS,COLS);
			changeLevel(0);
			//reserveGrid.setSpawn(1,1);
		}
	}

	public void showInventoryAndSpells()
	{
		image(backpack,(float) (width*0.6 + 850),height/20);
		/*stroke(0);
		fill(0);
		rect(0,0,width/5,height/3);*/
		int posY = height/5;
		for (int i = 0; i < iacna.getInventory().size(); i++)
		{
			//println(iacna.getInventory().get(i).getName());
			//textSize(32);
			//println(iacna.getInventory().get(i).isEquipped());
			textSize(12);
			stroke(255,255,255);
			fill(255,255,255);
			if (iacna.getInventory().get(i).isEquipped())
			{
				stroke(255,0,0);
				fill(255,0,0);
			}
			text(iacna.getInventory().get(i).getName(), (float)width*6/10 + 850, (float)posY);
			if (i == selectingInvIndex)
			{
				stroke(255,0,0);
				line(width*6/10 + 825,posY,width*6/10 + 925,posY);
			}
			posY += 20;
		}
		posY = height*4/5;
		for (int i = 0; i < iacna.getSpells().size(); i++)
		{
			//println(iacna.getInventory().get(i).getName());
			//textSize(32);
			//println(iacna.getInventory().get(i).isEquipped());
			textSize(12);
			stroke(255,255,255);
			fill(255,255,255);
			text(i + ". " + iacna.getSpells().get(i).getName(), (float)width*6/10 + 850, (float)posY);
			posY += 20;
		}
	}

	public void step()
	{
		for (int i = newWorld.getAG().getOccupants().size() - 1; i >= 0; i--)
		{
			newWorld.getAG().getOccupants().get(i).act();
			if (newWorld.getAG().getOccupants().get(i).getHealth() < 0)
			{
				newWorld.getAG().getOccupants().get(i).removeSelfFromWorld();
			}
		}
	}

	public void showConsole()
	{
		String lastCommand = "";
		int posY = 10;
		int rep = 0;

		int tempSize;
		if (console.size() > 15) 
			tempSize = 15;
		else
			tempSize = console.size();

		textSize(12);

		for (int i = 0; i < tempSize; i++)
		{
			if (!console.get(i).equals(lastCommand))
			{
				if (rep == 0)
				{
					fill(255,255,255);
					text(lastCommand,(float)(width*0.6 + 450),(float)posY);
					//println("a");
					rep = 0;
				}
				else if (rep > 0)
				{
					fill(255,255,255);
					text(lastCommand + " x" + (rep+1),(float)(width*0.6 + 450),(float)posY);
					//println("b");
					rep = 0;
				}
				lastCommand = console.get(i);
				posY += 15;
			}
			else
			{
				rep++;
			}
		}

		/*fill(255);
		stroke(255);
		rect((float)(width*0.6 + 450),(float)height*9/10,200,50);
		fill(0);
		if (typingInConsole && Math.random() < 0.5)
			text(commandString + "|",(float)(width*0.6 + 450),(float)height*9/10 + 30);
		text(commandString,(float)(width*0.6 + 450),(float)height*9/10 + 30);*/
	}

	public void showHealthAndFood()
	{
		textSize(32);
		if (iacna.getPercentageHP() < 0.25)
			fill(255,0,0);
		else if (iacna.getPercentageHP() < 0.5)
			fill(255,140,0);
		else if (iacna.getPercentageHP() < 0.75)
			fill(255,255,0);
		else
			fill(255,255,255);
		image(heart,width*11/10,height/10 + 13);
		int health = iacna.getHealth();
		if (health < 0) health = 0;
		text(health,width*12/10,height/10 + 50);

		if (iacna.getPercentageFood() < 0.25)
			fill(255,0,0);
		else if (iacna.getPercentageFood() < 0.5)
			fill(255,140,0);
		else if (iacna.getPercentageFood() < 0.75)
			fill(255,255,0);
		else
			fill(255,255,255);
		image(bread,width*11/10,height/10 + 73);
		text(iacna.getFood()/10,width*12/10,height/10 + 110);
	}

	public void showKills()
	{
		fill(255,255,255);
		image(sword,width*11/10,height/10 - 50);
		text(iacna.getNumKills(),width*12/10,height/10 - 13);
	}

	public void showPlayerFrame()
	{
		image(playerFrame,width*11/10 + 7,height/10 + 150);
		boolean h = false; boolean b = false; boolean l = false; boolean w = false;
		for (int i = iacna.getInventory().size() - 1; i >= 0; i--)
		{
			if (iacna.getInventory().get(i).isEquipped())
			{
				if (iacna.getInventory().get(i).getType().equals("Head")) h = true;
				else if (iacna.getInventory().get(i).getType().equals("Body")) b = true;
				else if (iacna.getInventory().get(i).getType().equals("Leg")) l = true;
				else if (iacna.getInventory().get(i).getType().equals("Weapon")) w = true;
			}
		}
		fill(255,0,0);
		stroke(255,0,0);
		if (h) rect(width*11/10 + 47,height/10 + 163,20,20);
		if (b) rect(width*11/10 + 47,height/10 + 210,20,20);
		if (l) rect(width*11/10 + 47,height/10 + 290,20,20);
		if (w) rect(width*11/10 + 10,height/10 + 240,20,20);
	}

	public void messageToConsole(String actionCommand) {
		/*if (actionCommand.length() > 30)
		{
			for (int i = actionCommand.length() - 1; i >= 0; i--)
			{
				if (actionCommand.charAt(i) == ' ')
				{
					messageToConsole(actionCommand.substring(0,i));
					messageToConsole(actionCommand.substring(i));
					return;
				}
			}
		}*/
		if (actionCommand.length() > 30)
		{
			messageToConsole(actionCommand.substring(30));
			messageToConsole(actionCommand.substring(0,30));
			return;
		}
		console.add(0, actionCommand);
	}

}
