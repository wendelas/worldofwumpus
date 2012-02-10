package z.WumpusWorld.WumpusForm;

import z.WumpusWorld.WumpusGame.WumpusGame;

public class WumpusMain {

	public static void main(String[] args)
	{
		WumpusGame game = new WumpusGame(5,5);
		WumpusGameForm form = new WumpusGameForm(game);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}
