package heuristic;

import heuristic.Nodes.Action;

public class ManhattanDistance implements Heuristic{

	public ManhattanDistance(){
		
	}

	@Override
	public int search(GameBoard board) {
		
		ManhattanDistance md = new ManhattanDistance();
		
		Nodes currentNode = md.createNode(null, board.clone(), 0, Action.NO_ACTION);

		int manhattanDistance = board.getManHattanDistance();
		
		while(!board.isSolved()){
			
			//Create a node when the top piece is moved down
			Nodes upNode = md.createNode(currentNode, board.clone(), currentNode.getPATHCOST()+1, Action.UP);
			upNode.getState().movePieceUp();
			int upManhattanDistance = upNode.getState().getManHattanDistance();
			
			//Create a node when the bottom piece is moved up
			Nodes downNode = md.createNode(currentNode, board.clone(), currentNode.getPATHCOST()+1, Action.DOWN);
			downNode.getState().movePieceDown();
			int downManhattanDistance = downNode.getState().getManHattanDistance();
			
			//Create a node when the left piece is moved right
			Nodes leftNode = md.createNode(currentNode, board.clone(), currentNode.getPATHCOST()+1, Action.LEFT);
			leftNode.getState().movePieceLeft();
			int leftManhattanDistance = leftNode.getState().getManHattanDistance();
			
			//Create a node when the Right piece is moved left
			Nodes rightNode = md.createNode(currentNode, board.clone(), currentNode.getPATHCOST()+1, Action.RIGHT);
			rightNode.getState().movePieceRight();
			int rightManhattanDistance = rightNode.getState().getManHattanDistance();
			
			
			Action nextAction = Action.NO_ACTION;
			if(upManhattanDistance < manhattanDistance){
				nextAction = Action.UP;
				manhattanDistance = upManhattanDistance;
			}else if(downManhattanDistance < manhattanDistance){
				nextAction = Action.DOWN;
				manhattanDistance = downManhattanDistance;
			}else if(leftManhattanDistance < manhattanDistance){
				nextAction = Action.LEFT;
				manhattanDistance = leftManhattanDistance;
			}else if(rightManhattanDistance < manhattanDistance){
				nextAction = Action.RIGHT;
				manhattanDistance = rightManhattanDistance;
			}
			
			switch(nextAction){
			
			case RIGHT:
				board.movePieceRight();
				currentNode = rightNode;
				break;
			case LEFT:
				board.movePieceLeft();
				currentNode = leftNode;
				break;
			case UP:
				board.movePieceUp();
				currentNode = upNode;
				break;
			case DOWN:
				board.movePieceDown();
				currentNode = downNode;
				break;
			default:
				return -1;
			}
			
			
			
		}
		
		return currentNode.getPATHCOST();
	}
	
	
	private Nodes createNode(Nodes parrent, GameBoard state, int pathCost, Action action){
		return new Nodes(parrent,state,pathCost,action);
	}

}
