package WumpusWorld.WumpusApplet;

import java.awt.Component;

import javax.swing.JApplet;

import WumpusWorld.WumpusGame.WumpusGame;

public class WumpusGUIController {
		
	private JApplet main;
	private WumpusGame game;
	private WumpusGamePanel form;
	private WumpusNewGamePanel newGame;
	
	public WumpusGUIController(JApplet main)
	{
		this.main = main;
		main.setSize(600, 540);
	}
	
	public void handleNewGame(WumpusNewGamePanel newGame, int rows, int cols)
	{
		game = new WumpusGame(rows,cols);
		form = new WumpusGamePanel(game, this);
		newGame.setVisible(false);
		form.setFocusable(true);
		main.getContentPane().add(form);
	}
	
	public void newGame()
	{
		if(form != null)
		{
			form.remove(form);
			form.setVisible(false);
		}
		newGame = new WumpusNewGamePanel(this);
		main.getContentPane().add(newGame);
		newGame.setVisible(true);
	}
}

