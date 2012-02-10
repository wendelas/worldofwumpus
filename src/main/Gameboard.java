package main;

import java.awt.Point;

public class Gameboard {
	
	private int[][] currentBoard;
	
	private Point zeroIndex;
	
	public static final int[][] SOLVEDBOARD ={{0,3,6},{1,4,7},{2,5,8}};
	
	public static final Point SOLVEDZEROPOINT = new Point(0,0);
	
	
	public Gameboard(){
		
		currentBoard = new int[3][3];
		for(int i = 0; i<SOLVEDBOARD.length; i++){
			for(int j = 0; j<SOLVEDBOARD[i].length; j++){
				currentBoard[i][j]=SOLVEDBOARD[i][j];
			}
		}
		
		zeroIndex = new Point(SOLVEDZEROPOINT.x,SOLVEDZEROPOINT.y);
		
	}
	
	
	
	
	private Gameboard(int[][] board, Point zeroIndex){
		this.currentBoard=board;
		this.zeroIndex=zeroIndex;
	}
	
	
	private void movePiece(Point newZeroIndex){
		int pieceToMove = currentBoard[newZeroIndex.x][newZeroIndex.y];
		currentBoard[zeroIndex.x][zeroIndex.y] = pieceToMove;
		currentBoard[newZeroIndex.x][newZeroIndex.y] = 0;
		zeroIndex = newZeroIndex;
	}
	
	
	public boolean movePieceLeft(){
		if(zeroIndex.x == 0){
			return false;
		}
		
		movePiece(new Point(zeroIndex.x-1,zeroIndex.y));
		return true;
	}
	
	public boolean movePieceRight(){
		if(zeroIndex.x == 2){
			return false;
		}
		
		movePiece(new Point(zeroIndex.x+1,zeroIndex.y));
		return true;
	}
	
	public boolean movePieceDown(){
		if(zeroIndex.y == 2){
			return false;
		}
		
		movePiece(new Point(zeroIndex.x,zeroIndex.y+1));
		return true;
	}
	
	public boolean movePieceUp(){
		if(zeroIndex.y == 0){
			return false;
		}
		
		movePiece(new Point(zeroIndex.x,zeroIndex.y-1));
		return true;
	}
	
	
	
	public int getNumMisplacedTiles(){
		
		int num = 0;
		for(int i = 0; i<currentBoard.length; i++){
			for(int j = 0; j<currentBoard[i].length; j++){
				if(currentBoard[i][j]!=SOLVEDBOARD[i][j]){
					num++;
				}
			}
		}
		return num;
	}
	
	
	
	
	public int getManHattanDistance(){
		
		int dist = 0;
		for(int i = 1; i<= 8; i++){
			
			Point p1 = getPosition(i);
			Point p2 = getSolvedPosition(i);
			Point temp = new Point(Math.abs(p1.x - p2.x),Math.abs(p1.y - p2.y));
			dist += temp.x;
			dist += temp.y;
				
		}
		
		return dist;
	}
	
	
	public Point getPosition(int value){
		
		if(value > 9 || value < 0){
			return null;
		}
		
		Point Post = null;
		for(int x = 0; x < currentBoard.length; x++){
			for(int y = 0; y < currentBoard[x].length; y++){
				if(currentBoard[x][y] == value){
					Post = new Point(x,y);
					x = currentBoard.length;
					break;
				}
			}
		}
		return Post;
	}
	
	
	public Point getSolvedPosition(int value){
		
		if(value > 9 || value < 0){
			return null;
		}
		
		Point Post = null;
		for(int x = 0; x < SOLVEDBOARD.length; x++){
			for(int y = 0; y < SOLVEDBOARD[x].length; y++){
				if(SOLVEDBOARD[x][y] == value){
					Post = new Point(x,y);
					x = SOLVEDBOARD.length;
					break;
				}
			}
		}
		return Post;
		
	}
	
	
	
	public boolean isSolved(){
		
		for(int x = 0; x < currentBoard.length; x++)
		{
			for(int y = 0; y < currentBoard[x].length; y++)
			{
				if(SOLVEDBOARD[x][y] != currentBoard[x][y])
				{
					return false;
				}
			}
		}
		return true;
	}
	
	
	@Override
	public Gameboard clone(){
		int[][] currentBoardCopy = new int[3][3];
		for(int x = 0; x<currentBoard.length; x++){
			for(int y = 0; y<currentBoard[x].length; y++){
				currentBoardCopy[x][y] = currentBoard[x][y];
			}
		}
		Point zeroIndexCopy = new Point(zeroIndex.x,zeroIndex.y);
		return new Gameboard(currentBoardCopy,zeroIndexCopy);
		
	}
	
	
}
