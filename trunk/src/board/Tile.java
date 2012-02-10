package board;

/**
 * @author amethyr
 *
 */
public class Tile {
	
	/**
	 * 
	 */
	public boolean gold;
	/**
	 * 
	 */
	public boolean wumpus;
	/**
	 * 
	 */
	public boolean pit;
	/**
	 * 
	 */
	public boolean breeze;
	/**
	 * 
	 */
	public boolean stench;
	/**
	 * 
	 */
	public boolean glitter;
	
	public String toString(){
		String output = "";
		if(gold) output+="A";
		if(wumpus) output+="W";
		if(pit) output+="P";
		if(breeze) output+="B";
		if(stench) output+="S";
		if(glitter) output+="G";
		return output;
	}
}

