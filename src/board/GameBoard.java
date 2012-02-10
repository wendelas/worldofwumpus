package board;

import java.util.Random;

public class GameBoard {
	
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
		board[x][y].addWumpus();
		//Add stench
		if(x!=board.length-1) board[x+1][y].addStench();
		if(x!=0) board[x-1][y].addStench();
		if(y!=board[x].length-1) board[x][y+1].addStench();
		if(y!=0) board[x][y-1].addStench();
	}
	
	
	/**
	 * 
	 */
	private void placePits(){
		Random gen = new Random();
		for(int x = 0; x<board.length;x++){
			for(int y = 0; y<board[x].length; y++){
				//Determines if the tile has a pit
				if(((gen.nextInt(4)+1)%4)==0){
					if(x==0&&y==0) continue;
					//Add pit
					board[x][y].addPit();
					//Add breeze
					if(x!=board.length-1) board[x+1][y].addBreeze();
					if(x!=0) board[x-1][y].addBreeze();
					if(y!=board[x].length-1) board[x][y+1].addBreeze();
					if(y!=0) board[x][y-1].addBreeze();
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
		board[x][y].addGold();
		//Add glitter
		board[x][y].addGlitter();
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
