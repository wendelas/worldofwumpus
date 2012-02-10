package main;

import java.util.*;

public class Main {
	

	public static void main(String args[]){
		
		int MisplacedTilesSuccess = 0;
		int ManhattanDistanceSuccess = 0;
		int randomStartSuccess = 0;
		
		Heuristic man = new ManhattanDistance();
		Heuristic mis = new MisplacedTiles();
		Heuristic rnd = new randomStart();
		
		for(int a = 50; a>=1; a--){
			Gameboard game = new Gameboard();
			game = randomBoard();
			int manh = man.search(game.clone());
			int misp = mis.search(game.clone());
			int asta = rnd.search(game.clone());
			
			
			if(manh>=0){
				ManhattanDistanceSuccess++;
			}
			if(misp>=0){
				MisplacedTilesSuccess++;
			}
			if(asta>=0){
				randomStartSuccess++;
			}
			
			System.out.println((a+1)+": "+manh+","+misp+","+asta);
			
		}
		
		System.out.println("MisplacedTiles Success: "+ MisplacedTilesSuccess +"\n MisplacedTiles Failed: "+ (50-MisplacedTilesSuccess));
		System.out.println("ManhattanDistance Success: "+ ManhattanDistanceSuccess +"\n ManhattanDistance Failed: "+ (50-ManhattanDistanceSuccess));
		System.out.println("RandomStart Success: "+ randomStartSuccess +"\n RandomStart Failed: "+ (50-randomStartSuccess));
		
		System.out.println("Misplaced Success Rate: "+((double)MisplacedTilesSuccess*100.0/(double)50)+"%");
		System.out.println("ManhattanDistance Success Rate: "+((double)ManhattanDistanceSuccess*100.0/(double)50)+"%");
		System.out.println("RandomStart Success Rate: "+((double)randomStartSuccess*100.0/(double)50)+"%");
		
		
		
		
	}
	
	
	
	
	private static Gameboard randomBoard(){
		
		Random rand = new Random();
		int temp = rand.nextInt(75-1)+1;
		return shuffle(temp);
	}
	
	private static Gameboard shuffle(int moves){
		
		Gameboard a = new Gameboard();
		
		Random rand = new Random();
		
		for(int i = 0; i<moves; i++){
			
			while(true){
				boolean move = false;
				
				int select = rand.nextInt(4);
				switch(select){
				
				case 0:
					move = a.movePieceUp();
					break;
				case 1:
					move = a.movePieceLeft();
					break;
				case 2:
					move = a.movePieceDown();
					break;
				case 3:
					move = a.movePieceRight();
					break;
				}
				if(move){
					break;
				}
			}
		}
		
		return a;		
	}
	
}
