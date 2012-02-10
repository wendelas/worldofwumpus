package board;

/**
 * @author amethyr
 *
 */
public class Tile {
	
	/**
	 * 
	 */
	private boolean gold;
	/**
	 * 
	 */
	private boolean wumpus;
	/**
	 * 
	 */
	private boolean pit;
	/**
	 * 
	 */
	private boolean breeze;
	/**
	 * 
	 */
	private boolean stench;
	/**
	 * 
	 */
	private boolean glitter;
	
	/**
	 * @return
	 */
	public boolean addWumpus(){
		if(!pit){
			wumpus = true;
			return true;
		}
		return false;
	}
	
	/**
	 * @return
	 */
	public boolean addPit() {
		if(!wumpus){
			pit = true;
			return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean addGold() {
		if(!pit && !wumpus){
			gold = true;
			return true;
		}
		return false;
	}
	
	/**
	 * @return
	 */
	public boolean addBreeze() {
		this.breeze = true;
		return true;
	}

	/**
	 * @return
	 */
	public boolean addStench() {
		this.stench = true;
		return true;
	}

	/**
	 * @return
	 */
	public boolean addGlitter() {
		glitter = true;
		return true;
	}

	/**
	 * @return
	 */
	public boolean hasWumpus() {
		return this.wumpus;
	}
	
	/**
	 * @return
	 */
	public boolean hasPit() {
		return this.pit;
	}
	
	/**
	 * @return
	 */
	public boolean hasGold() {
		return this.gold;
	}
	
	/**
	 * @return
	 */
	public boolean hasBreeze() {
		return this.breeze;
	}
	
	/**
	 * @return
	 */
	public boolean hasStench() {
		return this.stench;
	}

	/**
	 * @return
	 */
	public boolean hasGlitter(){
		return this.glitter;
	}
	
	
	public String toString(){
		String output = "";
		if(gold) output+="G";
		if(wumpus) output+="W";
		if(pit) output+="P";
		if(breeze) output+="B";
		if(stench) output+="S";
		if(glitter) output+="F";
		return output;
	}
}

