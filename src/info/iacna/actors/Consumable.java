package info.iacna.actors;

import java.util.ArrayList;

public class Consumable extends Item {

	//public int state; 0 solid 1 liquid 2 gas???
	
	public Consumable(String name, String type, String[] effects) {
		super(name, type, effects);
		//state = 0;
		// TODO Auto-generated constructor stub
	}

	public Consumable(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public Consumable(Item item) {
		super(item);
		// TODO Auto-generated constructor stub
	}
	
	public String getName(boolean diff)
	{
		return "Consumable";
	}

}
