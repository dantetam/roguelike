package info.iacna.level;

import java.util.ArrayList;
import java.util.HashMap;

import info.iacna.actors.*;

public class MazeGrid extends Grid {

	private ArrayList<Node> nodes;

	//Mazes must be square and have an odd number of rows and cols
	public MazeGrid(World world, int rows, int cols) {
		super(world, rows, cols);
		nodes = new ArrayList<Node>();
		// TODO Auto-generated constructor stub
		generateCells();

		carvePath(nodes.get((int) (Math.random()*nodes.size())));
		Node spawn = clearRandomPatches(true,5,3,3);
		setSpawn(spawn.x, spawn.y);
		//fillInWalls();
		//clearRandomPatches(true,5,1,10);

		clearRandomPatches(true,rows/5,0,(int)(Math.random()*10));
		clearRandomPatches(true,rows/5,0,(int)(Math.random()*10));
		//fillPatch(1,10,6,16);
		fillRandomPatches(true,rows/5,3,3);

		cleanStandaloneBlocks();

		int r,c;
		for (int i = 0; i < rows/10; i++)
		{
			r = (int)(Math.random()*rows);
			c = (int)(Math.random()*cols);
			clearPatch(r,0,r,cols);
			clearPatch(0,c,rows,c);
		}

		fillInWalls();
		placeDoors(rows*cols/100);
		spawnRandomEnemies(rows*cols/25);

		Merchant temp = new Merchant(this);
		temp.moveTo(5, 5);
		temp.addStoreItem(new Consumable("Blue Potion", "Potion", new String[]{"Hurt/20"}), 5);
		new Portal(this,999).moveTo(7, 7);

		nodes.clear();
	}

	private int step = -1;
	public void nextStep()
	{
		step++;
		if (step == 0)
		{
			generateCells();
		}
		if (step == 1)
		{
			carvePath(nodes.get((int) (Math.random()*nodes.size())));
			//fillInWalls();
			//clearRandomPatches(true,5,1,10);
		}
		else if (step == 2)
		{
			Node spawn = clearRandomPatches(true,5,3,3);
			setSpawn(spawn.x, spawn.y);
			clearRandomPatches(true,5,0,(int)(Math.random()*10));
			clearRandomPatches(true,5,0,(int)(Math.random()*10));
			//fillPatch(1,10,6,16);
			fillRandomPatches(true,5,3,3);
		}
		else if (step == 3)
		{
			cleanStandaloneBlocks();
		}
		else if (step == 4)
		{
			int r = (int)(Math.random()*rows);
			int c = (int)(Math.random()*cols);
			clearPatch(r,0,r,cols);
			clearPatch(0,c,rows,c);
			fillInWalls();
			spawnRandomEnemies(rows);
			placeDoors(10);
		}
		else if (step == 5)
		{
			nodes.clear();
			step = -1;
		}
	}

	public class Node
	{
		public int x,y;
		public boolean visited;
		private ArrayList<Node> container;

		public Node(int x, int y, ArrayList<Node> container)
		{
			this.x = x;
			this.y = y;
			this.container = container;
			container.add(this);
			visited = false;
		}

		public Node(int r, int c) {
			x = c;
			y = r;
			visited = false;
			container = null;
		}

		public boolean hasBeenVisited()
		{
			return visited;
		}

		public int checkPathTo(Node node)
		{
			if (Math.abs(node.x - x) != 2 && Math.abs(node.y - y) != 2) return -1;
			int cX = (node.x + x)/2;
			int cY = (node.y + y)/2;
			if (findActor(cX,cY) == null) return 1;
			return 0;
		}

		public void connectNode(Node node)
		{
			int cX = (node.x + x)/2;
			int cY = (node.y + y)/2;
			if (findActor(cX,cY) != null) findActor(cX,cY).removeSelfFromWorld();
			//System.out.println("Connected nodes " + "(" + x + "," + y + "),(" + node.x + "," + node.y + ")");
		}

		public ArrayList<Node> findNewNeighboringNodes()
		{
			ArrayList<Node> temp = new ArrayList<Node>();
			//temp.add(findNodeInContainer(x-2,y-2));
			temp.add(findNodeInContainer(x-2,y));
			//temp.add(findNodeInContainer(x-2,y+2));
			temp.add(findNodeInContainer(x,y+2));
			//temp.add(findNodeInContainer(x+2,y+2));
			temp.add(findNodeInContainer(x+2,y));
			//temp.add(findNodeInContainer(x+2,y-2));
			temp.add(findNodeInContainer(x,y-2));
			for (int i = temp.size() - 1; i >= 0; i--)
			{
				if (temp.get(i) == null) temp.remove(i);
				else if (temp.get(i).hasBeenVisited()) temp.remove(i);
			}
			return temp;
		}

		public ArrayList<Actor> findImmediateNeighbors()
		{
			ArrayList<Actor> temp = new ArrayList<Actor>();
			//temp.add(findActor(x-1,y-1));
			temp.add(findActor(x-1,y));
			//temp.add(findActor(x-1,y+1));
			temp.add(findActor(x,y+1));
			//temp.add(findActor(x+1,y+1));
			temp.add(findActor(x+1,y));
			//temp.add(findActor(x+1,y-1));
			temp.add(findActor(x,y-1));
			for (int i = temp.size() - 1; i >= 0; i--)
			{
				if (temp.get(i) == null) temp.remove(i);
			}
			return temp;
		}

		public Node findFirstVisitedNeighboringNode()
		{
			ArrayList<Node> temp = new ArrayList<Node>();
			//temp.add(findNodeInContainer(x-2,y-2));
			temp.add(findNodeInContainer(x-2,y));
			//temp.add(findNodeInContainer(x-2,y+2));
			temp.add(findNodeInContainer(x,y+2));
			//temp.add(findNodeInContainer(x+2,y+2));
			temp.add(findNodeInContainer(x+2,y));
			//temp.add(findNodeInContainer(x+2,y-2));
			temp.add(findNodeInContainer(x,y-2));
			for (int i = temp.size() - 1; i >= 0; i--)
			{
				if (temp.get(i) == null) temp.remove(i);
				else if (temp.get(i).hasBeenVisited()) return temp.get(i);
			}
			return null;
		}

		private Node findNodeInContainer(int x, int y)
		{
			for (int i = 0; i < container.size(); i++)
			{
				if (container.get(i).x == x && container.get(i).y == y)
					return container.get(i);
			}
			return null;
		}
	}

	//this should have been used years ago
	public class Location
	{
		public int r,c;

		public Location(int r, int c)
		{
			this.r = r;
			this.c = c;
		}

		public boolean equals(Location a)
		{
			return r == a.r && c == a.c;
		}
	}

	private ArrayList<Location> nodesChecked = new ArrayList<Location>();
	public void clearNodes() {nodesChecked.clear();}
	public int findAreaOfRoom(int r, int c)
	{
		for (int i = 0; i < nodesChecked.size(); i++)
		{
			if (nodesChecked.get(i).equals(new Location(r,c)))
			{
				nodesChecked.add(new Location(r,c));
				return 0;
			}
		}
		if (findActor(r,c) instanceof Obstacle)
		{
			nodesChecked.add(new Location(r,c));
			return 0;
		}
		if (r >= rows || c >= cols || r == 0 || c == 0)
		{
			nodesChecked.add(new Location(r,c));
			return 0;
		}
		nodesChecked.add(new Location(r,c));
		return findAreaOfRoom(r+1,c) + findAreaOfRoom(r-1,c) + findAreaOfRoom(r,c+1) + findAreaOfRoom(r,c-1) + 1;
	}

	public void findASpawn(boolean clearSpawn)
	{
		ArrayList<Location> possibleSpawns = new ArrayList<Location>();		
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				if (r % 2 == 0 && c % 2 == 0)
				{
					int area = findAreaOfRoom(r,c);
					clearNodes();
					//System.out.println(area);
					if (area < 5) 
					{
						//new Obstacle(this).moveTo(r,c);
					}
					else if (area > 50)
					{
						//System.out.println(r + "," + c);
						possibleSpawns.add(new Location(r,c));
					}
				}
			}
		}
		int randomIndex = (int) (Math.random()*possibleSpawns.size());
		setSpawn(possibleSpawns.get(randomIndex));
		if (clearSpawn)
		{
			int sr = possibleSpawns.get(randomIndex).r;
			int sc = possibleSpawns.get(randomIndex).c;
			for (int r = sr - 3; r <= sr + 3; r++)
			{
				for (int c = sc - 3; c < sc + 3; c++)
				{
					if (findActor(r,c) != null)
					{
						if (findActor(r,c) instanceof Enemy)
						{
							((Enemy)findActor(r,c)).removeSelfFromWorldClean();
						}
					}
				}
			}
		}
	}

	public void setSpawn(Location location) {
		setSpawn(location.r, location.c);
	}

	public void movePlayerToRandomSpawn()
	{
		world.getPlayer().moveTo(spawnX,spawnY);
	}

	public Node findNodeInContainer(int x, int y)
	{
		for (int i = 0; i < nodes.size(); i++)
		{
			if (nodes.get(i).x == x && nodes.get(i).y == y)
				return nodes.get(i);
		}
		return null;
	}

	public void generateCells()
	{
		for (int r = 0; r < rows; r++)
		{
			if (r % 2 == 0)
			{
				for (int c = 0; c < cols; c++)
					new Obstacle(this).moveTo(r, c);
			}
			else
			{
				for (int c = 0; c < cols; c += 2)
					new Obstacle(this).moveTo(r, c);
				for (int c = 1; c < cols; c += 2)
					new Node(r,c,nodes);
			}
		}
	}

	public void spawnRandomEnemies(int times)
	{
		for (int i = 0; i < times; i++)
		{
			int random = (int)(Math.random()*world.getRunner().enemyNames.length);
			Enemy temp = new Enemy(this,world.getRunner().enemyLevels[random],world.getRunner().enemyNames[random]);
			boolean moved = false;
			do
			{
				int r = (int)(Math.random()*rows);
				int c = (int)(Math.random()*cols);
				if (findActor(r, c) == null)
				{
					temp.moveTo(r, c);
					moved = true;
				}
			} while (!moved);
		}
	}

	public void clearPatch(int x1, int y1, int x2, int y2)
	{
		for (int i = occupants.size() - 1; i >= 0; i--)
		{
			if (occupants.get(i).getX() >= x1 && occupants.get(i).getX() <= x2 && occupants.get(i).getY() >= y1 && occupants.get(i).getY() <= y2)
			{
				occupants.remove(i);
			}
		}
	}

	public void fillPatch(int x1, int y1, int x2, int y2)
	{
		for (int r = y1; r <= y2; r++)
		{
			for (int c = x1; c <= x2; c++)
			{
				if (findActor(r,c) == null) new Obstacle(this).moveTo(r, c);
			}
		}
	}

	public Node clearRandomPatches(boolean walls, int times, int w, int h) {
		//HashMap<Integer,Integer> usedRows = new HashMap<Integer,Integer>();\
		int r = 1; int c = 1;
		for (int i = 0; i < times; i++)
		{
			r = (int)(Math.random()*rows);
			c = (int)(Math.random()*cols);
			clearPatch(r,c,r+w,c+h);
			if (walls)
			{

			}
		}
		return new Node(r,c);
	}

	public void fillRandomPatches(boolean walls, int times, int w, int h) {
		int r = 1; int c = 1;
		for (int i = 0; i < times; i++)
		{
			r = (int)(Math.random()*rows);
			c = (int)(Math.random()*cols);
			fillPatch(r,c,r+w,c+h);
			if (walls)
			{

			}
		}
	}

	private void cleanStandaloneBlocks() {
		/*for (int r = 0; r < rows; r += 2)
		{
			for (int c = 0; c < cols; c += 2)
			{
				if (findNodeInContainer(c,r+1) == null && findNodeInContainer(c,r-1) == null && findNodeInContainer(c+1,r) == null && findNodeInContainer(c-1,r) == null)
				{
					int numRandomNodes = 1;
					ArrayList<Node> temp = new ArrayList<Node>();
					new Node(c-1,r-1,temp);
					new Node(c-1,r,temp);
					new Node(c-1,r+1,temp);
					new Node(c,r+1,temp);
					new Node(c+1,r+1,temp);
					new Node(c+1,r,temp);
					new Node(c+1,r-1,temp);
					new Node(c,r-1,temp);
					for (int i = 0; i < numRandomNodes; i++)
					{
						Node n = temp.get((int)(Math.random()*temp.size()));
						new Obstacle(this).moveTo(n.x, n.y);
					}
					temp.clear();
				}
			}
		}*/
		int changed = 0;
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				if (findActor(c,r) != null && new Node(r,c).findImmediateNeighbors().size() == 0)
				{
					//System.out.println(r + "," + c);
					findActor(c,r).removeSelfFromWorld();
					changed++;
				}
				if (new Node(c,r).findImmediateNeighbors().size() >= 7)
				{
					new Obstacle(this).moveTo(r,c);
					changed++;
				}
			}
		}
		//if (changed > 0) cleanStandaloneBlocks();
	}

	public void placeDoors(int times) 
	{
		int i = 0;
		do
		{
			int r = (int)(Math.random()*rows);
			int c = (int)(Math.random()*cols);
			if (((findActor(r-1,c) != null && findActor(r+1,c) != null) || (findActor(r,c+1) != null && findActor(r,c-1) != null)) && findActor(r,c) == null)
			{
				new Door(this).moveTo(r,c);
				i++;
			}
		} while (i < times);
	}

	public void fillInWalls() {
		for (int r = 0; r <= rows; r++)
		{
			if (findActor(r,0) == null) new Obstacle(this).moveTo(r,0);
			if (findActor(r,cols) == null) new Obstacle(this).moveTo(r,cols);
		}
		for (int c = 0; c <= cols; c++)
		{
			if (findActor(0,c) == null) new Obstacle(this).moveTo(0,c);
			if (findActor(rows,c) == null) new Obstacle(this).moveTo(rows,c);
		}
	}

	public void carvePath(Node start)
	{
		start.visited = true;
		ArrayList<Node> neighboringNodes = start.findNewNeighboringNodes();
		if (neighboringNodes.size() == 0) 
		{
			for (int r = 1; r < rows; r += 2)
			{
				for (int c = 1; c < cols; c += 2)
				{
					//System.out.println(r + "," + c);
					Node newStart = nodes.get(0).findNodeInContainer(r, c);
					if (newStart != null)
					{
						if (!newStart.visited && newStart.findFirstVisitedNeighboringNode() != null)
						{
							newStart.connectNode(newStart.findFirstVisitedNeighboringNode());
							carvePath(newStart);
						}
					}
				}
			}
			return;
		}
		Node random = neighboringNodes.get((int)(Math.random()*neighboringNodes.size()));
		start.connectNode(random);
		//try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		carvePath(random);
	}

}
