package heuristic;

import heuristic.Nodes.Action;

import java.util.Random;


public class randomStart implements Heuristic {
	
	public randomStart(){
		
	}


	@Override
	public int search(GameBoard board) {
		Heuristic mh = new ManhattanDistance();
		int pathCost = mh.search(board);
		
		while(pathCost == -1){
			Random r = new Random();
			
			for(int i=0; i<50; i++){
				while(true){
					boolean move = false;
					int t = r.nextInt(4);
					
					switch(t){
					case 0:
						move = board.movePieceUp();
						break;
					case 1:
						move = board.movePieceDown();
						break;
					case 2:
						move = board.movePieceRight();
						break;
					case 3:
						move = board.movePieceLeft();
						break;
					}
					
					if(move){
						break;
					}
					
					
					
				}
			}
			
			pathCost = mh.search(board);
		}
		
		return pathCost;
	}

}
