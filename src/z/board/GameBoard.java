package z.board;

import java.awt.Point;
import java.util.Random;

public class GameBoard {
	
	private Point wumpus;
	private Tile[][] board;
	
	
	
	/**
	 * 
	 */
	public GameBoard(){
		board = new Tile[4][4];
		for(int x = 0; x<board.length;x++){
			for(int y = 0; y<board[x].length; y++){
				board[x][y] = new Tile();
			}
		}
		placePits();
		placeWumpus();
		placeGold();
	}
	
	
	/**
	 * 
	 */
	private void placeWumpus(){
		Random gen = new Random();
		int x=0, y=0;
		while(x==0 && y==0){
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
	 * 
	 */
	private void placePits(){
		Random gen = new Random();
		for(int x = 0; x<board.length;x++){
			for(int y = 0; y<board[x].length; y++){
				//Determines if the tile has a pit
				if(((gen.nextInt(4)+1)%4)==2){
					if((x==0&&y==0) || (wumpus.x==x&&wumpus.y==y)) break;
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
	
	/**
	 * 
	 */
	private void placeGold(){
		Random gen = new Random();
		int x =gen.nextInt(4), y = gen.nextInt(4); 
		//Place gold
		board[x][y].gold = true;
		//Add glitter
		board[x][y].glitter = true;
	}
	
	
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
}
