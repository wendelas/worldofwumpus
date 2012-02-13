/*****************************************************************************
 * FILE: GameBoard.java
 * DATE: 02/08/12
 * AUTHOR: 	Karl Schmidbauer <schmidbauerk@msoe.edu>
 * 			Ben Ebert <ebertb@msoe.edu>
 * 
 * PURPOSE: Creates and stores the game board.
 * 
 ****************************************************************************/
package board;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

/**
 * Generates the game board for a wumpus world implementation
 * 
 * @author Karl Schmidbauer <schmidbauerk@msoe.edu>
 *
 */
public class GameBoard {
	
	/**
	 * The point where the wupus is. If null the wumpus is dead.
	 */
	private Point wumpus;
	/**
	 * The game board stored as a two dimensional array of tiles
	 */
	private Tile[][] board;
	/**
	 * Stores the board size
	 */
	private Dimension boardSize;
	
	
	/**
	 * Default constructor makes a game board of a default size
	 */
	public GameBoard(){
		this(4, 4);
	}
	
	/**
	 * Public constructor takes in parameters the size of the game board
	 */
	public GameBoard(int x, int y){
		boardSize = new Dimension(x, y);
		
		board = new Tile[x][y];
		for(int i = 0; i<board.length; i++){
			for(int j = 0; j<board[i].length; j++){
				board[i][j] = new Tile();
			}
		}
		placeGold();
		placeWumpus();
		placePits();
	}
	
	
	/**
	 * Places the gold on a random tile on the board
	 */
	private void placeGold(){
		Random gen = new Random();
		int x =gen.nextInt(4), y = gen.nextInt(4); 
		//Place gold
		board[x][y].gold = true;
		//Glitter and gold are the same
	}

	/**
	 * Places the wumpus on a random tile on the board
	 */
	private void placeWumpus(){
		Random gen = new Random();
		int x=0, y=0;
		while((x==0 && y==0) || (board[x][y].gold)){
			x =gen.nextInt(4);
			y = gen.nextInt(4); 
		}
		
		//Place Wumpus
		wumpus = new Point(x, y);
		board[x][y].wumpus = true;
		//Add stench
		if(x!=board.length-1) board[x+1][y].stench=true;
		if(x!=0) board[x-1][y].stench = true;
		if(y!=board[x].length-1) board[x][y+1].stench = true;
		if(y!=0) board[x][y-1].stench = true;
	}
	
	
	/**
	 * Places pits with a 20% chance on tiles
	 */
	private void placePits(){
		Random gen = new Random();
		for(int x = 0; x<board.length;x++){
			for(int y = 0; y<board[x].length; y++){
				//Determines if the tile has a pit
				if(((gen.nextInt(4)+1)%4)==2){
					if((x==0&&y==0) || (wumpus.x==x&&wumpus.y==y) || (board[x][y].gold)) break;
					//Add pit
					board[x][y].pit = true;
					//Add breeze
					if(x!=board.length-1) board[x+1][y].breeze = true;
					if(x!=0) board[x-1][y].breeze = true;
					if(y!=board[x].length-1) board[x][y+1].breeze = true;
					if(y!=0) board[x][y-1].breeze = true;
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String output = "";
		for(int x = 0; x<board.length;x++){
			for(int y = 0; y<board[x].length; y++){
				output+="|"+board[x][y]+"\t|";
			}
			output+="\n";
		}
		return output;
	}
	
	/**
	 * Get the size of the game board
	 * @return - dimension of the game board
	 */
	public Dimension getBoardSize(){
		return boardSize;
	}

	public Point getAgentLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void grabGold() {
		// TODO Auto-generated method stub
		
	}

	public void climb() {
		// TODO Auto-generated method stub
		
	}

	public char[] getStatusAtLocation(Point agentLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean wumpusDead() {
		return (wumpus==null);
	}
}
