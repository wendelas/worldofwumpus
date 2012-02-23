package z.wumpus;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

/**
 * @author ebertb, Schmidbauerk
 * @date 2/17/12
 * This class keeps track of the wumpus world
 */
public class GameBoard implements Cloneable {

	public Dimension boardSize;
	
	public int NUMBER_OF_WUMPUS = 1;
	public int NUMBER_OF_GOLD = 1;
	public int NUMBER_OF_PITS = 0;
	
	public static final int DEATH_COST = -1000;
	public static final int STOP_COST = -1;
	public static final int ARROW_COST = -10;
	public static final int GOLD_VALUE = 1000;
	public static final double CRUMB_COST = 0.5;
	
	private Tile[][] board;

	private Point wumpus;
	private Point gold;
	
	
	public GameBoard(){
		this(4,4);
	}
	
	/**
	 * Default constructor.
	 * Initializes the Wumpus World
	 */
	public GameBoard(int width, int height) {
		boardSize = new Dimension(width, height);
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
		gold = new Point(x, y);
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
				if(((gen.nextInt(4)+1)%4)==2 && !((x==0&&y==0) || (wumpus.x==x&&wumpus.y==y) || (board[x][y].gold))){
					//Add pit
					board[x][y].pit = true;
					//Add breeze
					if(x!=board.length-1) board[x+1][y].breeze = true;
					if(x!=0) board[x-1][y].breeze = true;
					if(y!=board[x].length-1) board[x][y+1].breeze = true;
					if(y!=0) board[x][y-1].breeze = true;
					NUMBER_OF_PITS++;
				}
			}
		}
	}
	
	public Object clone() {
		GameBoard newWorld = new GameBoard(boardSize.width, boardSize.height);
		for (int x = 0; x < boardSize.width; x++) {
			for (int y = 0; y < boardSize.height; y++) {
				newWorld.board[x][y] = ((Tile)board[x][y].clone());
			}
		}
		return newWorld;
	}
	
	public boolean inBounds(int x, int y) {
		if (x < 0 || x >= boardSize.width) return false;
		if (y < 0 || y >= boardSize.height) return false;
		return true;
	}
	
	public boolean isVisited(int x, int y) {
		if (!inBounds(x,y)) return false;
		return (board[x][y].visited);
	}
	
	public boolean markVisited(int x, int y) {
		if (!inBounds(x,y)) return false;
		board[x][y].visited = true;
		return true;
	}
	
	public boolean hasWumpus(int x, int y) {
		if(wumpus==null) return false;
		return (wumpus.x==x && wumpus.y==y);
	}
	
	public boolean killWumpus(int x, int y) {
		if (!hasWumpus(x,y)) return false;
		board[x][y].wumpusDead = true;
		wumpus = null;
		return true;
	}
	
	public boolean hasPit(int x, int y) {
		if (!inBounds(x,y)) return false;
		return board[x][y].pit;
	}
	
	public boolean isStart(int x, int y) {
		return (x == 0 && y == 0);
	}
	
	public boolean hasGold(int x, int y) {
		if(gold==null) return false;
		return (gold.x==x && gold.y==y);
	}
	
	public boolean grabGold(int x, int y) {
		if (!inBounds(x,y) || !hasGold(x,y) || gold==null) return false;
		board[x][y].gold = false;
		gold = null;
		return true;
	}
	
	public boolean hasStench(int x, int y) {
		if (!inBounds(x,y)) return false;
		return board[x][y].stench;
	}
	
	public boolean hasBreeze(int x, int y) {
		if (!inBounds(x,y)) return false;
		return board[x][y].breeze;
	}
	
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

	public boolean placeGold(int x, int y) {
		board[x][y].gold = true;
		gold = new Point(x, y);
		return true;
	}

	public void dropMileMarker(int x, int y) {
		board[x][y].mileMarker++;
	}

	public boolean hasMileMarker(int x, int y) {
		return (board[x][y].mileMarker>0);
	}
	
	public Tile getTile(int x, int y){
		if(board[x][y].visited) return board[x][y];
		return null;
	}
}
