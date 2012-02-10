package main;

import java.util.Random;
import java.util.Scanner;

import javax.swing.*;

public class main {

	
	private static Gameboard board;
	private static Random rand;
	private final static int sizeX = 4;
	private final static int sizeY = 4;
	
	//GUI variables
	private static JPanel buttonPanel;
	private static JFrame guiScreen;
	private static JPanel resultsPanel;
	private static JButton run;
	private static JButton reset;
	private static JTextPane results;
	
	private static final int width = 600;
	private static final int height = 600;
	
	
	public static void main(String args[]){
		
		
		board = new Gameboard(sizeX,sizeY);
		rand = new Random();
		
		
		
		
		
	}
	
	
	
	
	private static void createNewWorld(){
		
		
		
		
	}
	
	
	
	
	private static void createGUI(){
		guiScreen = new JFrame("WumpusWorld");
		buttonPanel = new JPanel();
		resultsPanel = new JPanel();
		run = new JButton("Run");
		reset = new JButton("Reset");
		results = new JTextPane();
		
		
		
		guiScreen.setSize(width, height);
		
	}
	
	
	
	
	
	
	
	
}
