package character;

import character.abilities.*;

/**
 * Build a new character
 * 
 * @author supreetuniversity
 */
public class Character {
	private String characterClass;
	private String name;
	private int level;
	private int strength;
	private int dexterity;
	private int constitution;	
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setCharacterClass(String characterClass){
		this.characterClass = characterClass;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void setStrength(int strength){
		this.strength = strength;
	}
	
	public void setDexterity(int dexterity){
		this.dexterity = dexterity;
	}

	public void setConstitution(int constitution){
		this.constitution = constitution;
	}
}