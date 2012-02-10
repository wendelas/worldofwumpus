package main;

import java.awt.Point;


/**
 * This is the game board class where it will hold
 * all the info about the board.
 * 
 * 
 * 
 * @author ebertb
 * @Date 1/20/12
 *
 */
public class Gameboard {
	
	private final Point startingPoint = new Point(0,0);
	
	private String[][] board;
	private String[][] posBoard;
	
	private Point currentPos;
	
	
	private int sizeX;
	private int sizeY;
	
	/**
	 * Constructor of the Game board
	 * @param sizeX How big the game board is horizontally
	 * @param sizeY How big the game board is Vertically
	 */
	public Gameboard(int sizeX, int sizeY){
		board = new String[sizeX][sizeY];
		posBoard = new String[sizeX][sizeY];
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		currentPos = new Point(0,0);
		
		for(int x=0;x<sizeX;x++){
			for(int y=0;y<sizeY;y++){
				board[x][y]="";
			}
		}
		
		
		
	}
	
	
	public Gameboard(int sizeX, int sizeY, String[][] posBoard, String[][] board, Point currentPos){
		this.board=board;
		this.posBoard=posBoard;
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		this.currentPos=currentPos;
	}
	
	
	
	
	public Point getPosition(){
		return currentPos;
		
	}
	
	
	public void setGameSpot(int x, int y, String o){
		if(x>=0 && x<sizeX && y>=0 && y<sizeY){
			board[x][y] +=o;
		}
	}
	
	
	public String getGameSpot(int x, int y){
		return board[x][y];
	}
	
	
	public Gameboard clone(){
		String[][] boardCopy = new String[sizeX][sizeY];
		String[][] posBoardCopy = new String[sizeX][sizeY];
		Point tempPos = currentPos;
		
		for(int x = 0; x<board.length; x++){
			for(int y = 0; y<board[x].length; y++){
				boardCopy[x][y] = board[x][y];
				posBoardCopy[x][y] = posBoard[x][y];
			}
		}
		return new Gameboard(sizeX,sizeY,boardCopy,posBoardCopy, tempPos);
		
	}
	
	
}
