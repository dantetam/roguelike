package info.iacna.actors;

public class Spell extends Item {

	public int cost;
	
	public Spell(String name, String type, String[] effects, String formalName, int cost) {
		super(name, type, effects, formalName);
		// TODO Auto-generated constructor stub
		this.cost = cost;
	}

	public Spell(Spell spell) {
		super(spell);
		// TODO Auto-generated constructor stub
		cost = spell.cost;
	}
	
}
