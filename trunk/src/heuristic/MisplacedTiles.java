package heuristic;

import heuristic.Nodes.Action;
import board.GameBoard;


public class MisplacedTiles implements Heuristic {

	public MisplacedTiles(){
		
	}
	
/*
	@Override
	public int search(GameBoard board) {
		 MisplacedTiles mt = new MisplacedTiles();
		 
		 Nodes currentNode = mt.createNode(null, board.clone(), 0, Action.NO_ACTION);
		 
		 int misplacedTiles = board.getNumMisplacedTiles();
		 
		 while(!board.isSolved()){
			 
			 //Create a node when the top piece is moved down
			 Nodes upNode = mt.createNode(currentNode, board.clone(), currentNode.getPATHCOST()+1, Action.UP);
			 upNode.getState().movePieceUp();
			 
			 //Create a node when the bottom piece is moved up
			 Nodes downNode = mt.createNode(currentNode, board.clone(), currentNode.getPATHCOST()+1, Action.DOWN);
			 downNode.getState().movePieceDown();
			 
			 //Create a node when the left piece is moved right
			 Nodes leftNode = mt.createNode(currentNode, board.clone(), currentNode.getPATHCOST()+1, Action.LEFT);
			 leftNode.getState().movePieceLeft();
			 
			 //Create a node when the right piece is moved left
			 Nodes rightNode = mt.createNode(currentNode, board.clone(), currentNode.getPATHCOST()+1, Action.RIGHT);
			 rightNode.getState().movePieceRight();
			 
			 
			 
			 if(upNode.getState().getNumMisplacedTiles()<misplacedTiles){
				 board.movePieceUp();
				 currentNode = upNode;
				 misplacedTiles = upNode.getState().getNumMisplacedTiles();
			 }else if(downNode.getState().getNumMisplacedTiles()<misplacedTiles){
				 board.movePieceDown();
				 currentNode = downNode;
				 misplacedTiles = downNode.getState().getNumMisplacedTiles();
			 }else if(leftNode.getState().getNumMisplacedTiles()<misplacedTiles){
				 board.movePieceLeft();
				 currentNode = leftNode;
				 misplacedTiles = leftNode.getState().getNumMisplacedTiles();
			 }else if(rightNode.getState().getNumMisplacedTiles()<misplacedTiles){
				 board.movePieceRight();
				 currentNode = rightNode;
				 misplacedTiles = rightNode.getState().getNumMisplacedTiles();
			 }else{
				 return -1;
			 }
			 
		 }
		 
		 return currentNode.getPATHCOST();
	}
	
*/
	private Nodes createNode(Nodes parent,GameBoard state, int PATHCOST,Action action){
		return new Nodes(parent,state,PATHCOST,action);
	}

	@Override
	public int nextMove(GameBoard board) {
		// TODO Auto-generated method stub
		return 0;
	}


}
