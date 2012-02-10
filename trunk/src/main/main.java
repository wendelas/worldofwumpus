package main;

import java.util.Random;
import java.util.Scanner;

public class main {

	
	private static Gameboard board;
	private static Scanner scan;
	private static Random rand;
	private static KnowledgeConnector kc;
	private final static int sizeX = 4;
	private final static int sizeY = 4;
	
	
	public static void main(String args[]){
		
		
		board = new Gameboard(sizeX,sizeY);
		kc = new KnowledgeConnector();
		rand = new Random();
		
		
		kc.createkb();
		
		
		
	}
	
	
	
	
	private static void createNewWorld(){
		
		board.setGameSpot(rand.nextInt(5), rand.nextInt(5), "G ");
		board.setGameSpot(rand.nextInt(5), rand.nextInt(5), "W ");
		
		int temp = rand.nextInt(10);
		for(int i=0;i<=temp;i++){
			int x = rand.nextInt(5);
			int y = rand.nextInt(5);
			boolean add = true;
			
			scan = new Scanner(board.getGameSpot(x, y));
			
			while(scan.hasNext()){
				if(scan.next()=="P")
					add = false;
			}
			if(add){
				board.setGameSpot(x, y, "P ");
			}
		}
		
		for(int x=0;x<5;x++){
			for(int y=0;y<5;y++){
				String spot = board.getGameSpot(x, y);
				scan = new Scanner(spot);
				while(scan.hasNext()){
					String code = scan.next();
					if(code=="W"){
						board.setGameSpot(x+1, y, "S ");
						board.setGameSpot(x-1, y, "S ");
						board.setGameSpot(x, y+1, "S ");
						board.setGameSpot(x, y-1, "S ");
					}else if(code=="P"){
						board.setGameSpot(x+1, y, "B ");
						board.setGameSpot(x-1, y, "B ");
						board.setGameSpot(x, y+1, "B ");
						board.setGameSpot(x, y-1, "B ");
					}else if(code=="G"){
						board.setGameSpot(x, y, "GL ");
					}
					
					
				}
				
				
				
				
				
			}
		}
		
		
		
	}
	
	
	
	
	private static void createGUI(){
		
	}
	
	
	
	
	
	
	
	
}
