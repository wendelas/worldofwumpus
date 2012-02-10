package main;

import java.util.Random;
import java.util.Scanner;

import board.GameBoard;

public class Driver {

	
	private static GameBoard board;
	private static Scanner scan;
	private static Random rand;
	private static KnowledgeConnector kc;
	private final static int sizeX = 4;
	private final static int sizeY = 4;
	
	
	public static void main(String args[]){
		
		
		board = new GameBoard(sizeX,sizeY);
		kc = new KnowledgeConnector();
		rand = new Random();
		
		
		kc.createkb();
		
	}
}
