package zz.agent;

import zz.agent.Agent.direction;
import zz.board.GameBoard;

public class MemoryNode {
	private GameBoard state;
	private int pathCost;
	private direction direction;
	private MemoryNode parent;

	public MemoryNode(MemoryNode currentNode, GameBoard board, int pathCost, direction direction) {
		this.parent=currentNode;
		this.state=board;
		this.pathCost=pathCost;
		this.direction=direction;
	}

	public MemoryNode getParent() {
		return parent;
	}
	
	public GameBoard getBoard() {
		return state;
	}

	public int getPathCost() {
		return pathCost;
	}
	
	public direction getDirection() {
		return direction;
	}
	
	
	
	
	
}
