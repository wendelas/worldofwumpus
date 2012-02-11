package heuristic;

import java.util.Random;

import agent.Agent.direction;
import board.GameBoard;


public class randomStart implements Heuristic {
	
	public randomStart(){
		
	}


	@Override
	public direction nextMove(GameBoard board) {
		Heuristic mh = new ManhattanDistance();
		direction pathCost = mh.nextMove(board);
		
		while(pathCost == -1){
			Random r = new Random();
			
			for(int i=0; i<50; i++){
				boolean move = false;
				while(!move){
					int t = r.nextInt(4);
					
					switch(t){
					case 0:
//						move = board.movePieceUp();
						break;
					case 1:
//						move = board.movePieceDown();
						break;
					case 2:
//						move = board.movePieceRight();
						break;
					case 3:
//						move = board.movePieceLeft();
						break;
					}
				}
			}
			
			pathCost = mh.nextMove(board);
		}
		
		return pathCost;
	}

}
