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

		createWorld();
	}


	private void createWorld(){
		board = new Tile[boardSize.width][boardSize.height];
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
		for(int y = board.length-1; y>=0;y--){
			for(int x = 0; x<board[y].length; x++){
				output+="|"+board[y][x]+"\t|";
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

	public boolean wumpusDead() {
		return (wumpus==null);
	}


	private boolean hasGold;
	private boolean wumpusDead;
	private boolean agentDead;
	private boolean agentFinished;

	private Point wumpusLocation;
	private Point goldLocation;
	private Point agentLocation;
	private Point startingLocation;

	private static final char WUMPUS ='W';
	private static final char GOLD = 'G';
	private static final char BREEZE = 'B';
	private static final char PIT = 'P';
	private static final char STENCH = 'S';


	private String[][] status;


	private void resetWorld(){
		//Tries++
		this.agentDead=false;
		this.agentFinished=false;
		this.hasGold=false;
		this.wumpusDead=false;
		this.agentLocation=new Point(startingLocation.x, startingLocation.y);
		this.discoveredTiles=new boolean[boardSize.width][boardSize.height];

		createWorld();
	}

	private boolean[][] discoveredTiles;
	public boolean[][] getDiscoveredTiles(){
		return discoveredTiles;
	}

	public String[][] getStatus(){
		return status;
	}


	public boolean grabGold(){
		boolean gotten = false;
		if(hasGold){
			gotten = false;
		}else if(agentLocation.x == goldLocation.x && agentLocation.y == goldLocation.y){
			status[goldLocation.x][goldLocation.y] = status[goldLocation.x][goldLocation.y].replace(""+GOLD, "");
			hasGold=true;
			gotten=true;
		}
		return gotten;
	}



	public boolean climb(String string){
		boolean climbed = false;
		if(agentLocation.x == startingLocation.x && agentLocation.y == startingLocation.y){
			agentFinished = true;
			if(hasGold){
				//gold += 1000;
				//wins++;
			}
			resetWorld();
			climbed = true;
			System.out.println("Notifying");
			System.out.println("Notified");
		}
		return climbed;
	}

	private void killWumpus(){
		status[wumpusLocation.x][wumpusLocation.y] = status[wumpusLocation.x][wumpusLocation.y].replace(""+WUMPUS, "");
//		gold+=100;
		wumpusDead = true;
//		wumpusKills++;
	}

	private void move(){
		discoveredTiles[agentLocation.x][agentLocation.y] = true;
		String agentStats = getStatusAtLocation(agentLocation);
		boolean dead = false;
//		for(int i = 0; i<agentStats.length; i++){
//			if(agentStats[i] == PIT || agentStats[i] == WUMPUS){
//				dead = true;
//				break;
//			}
//		}
		if(dead){
			this.agentDead = true;
			//gold -= 1000;
			//deaths++;
			resetWorld();
		}else{
			//gold--;
		}
		//numMoves++;
	}

	public String getStatusAtLocation(Point p){
		return board[p.x][p.y].toString();
	}

	public Point getWumpusLocation(){
		return wumpusLocation;
	}

	public Point getGoldLocation(){
		return goldLocation;
	}

	public Point getAgentLocation(){
		return agentLocation;
	}

	public Point getStartLocation(){
		return startingLocation;
	}

	public boolean didAgentDie(){
		return agentDead;
	}

	public boolean didAgentFinish(){
		return agentFinished;
	}
}
