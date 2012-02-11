/*****************************************************************************
 * FILE: Tile.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Single tile object serves to store the state of each tile 
 * 			or room on the game board.
 * 
 ****************************************************************************/
package board;

/**
 * Storage for the configuration of each tile or room on the game board
 * 
 * @author Karl Schmidbauer <schmidbauerk@msoe.edu>
 *
 */
public class Tile {
	
	/**
	 * Stores if the gold in in this tile
	 */
	public boolean gold;
	/**
	 * Stores if the wumpus is in this tile
	 */
	public boolean wumpus;
	/**
	 * Stores if there is a pit on this tile
	 */
	public boolean pit;
	/**
	 * Stores if there is a breeze on this tile
	 */
	public boolean breeze;
	/**
	 * Stores if there is a stench on this tile
	 */
	public boolean stench;
	
	
	/* (non-Javadoc)
	 * Outputs the tile to a string to be viewed on a console.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String output = "";
		if(gold) output+="A";
		if(wumpus) output+="W";
		if(pit) output+="P";
		if(breeze) output+="B";
		if(stench) output+="S";
		return output;
	}
}

