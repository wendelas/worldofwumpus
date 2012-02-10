package propositional_logic;

import WumpusWorld.WumpusGame.WumpusGame;
import WumpusWorld.WumpusForm.WumpusGameForm;
public class Main {

	public static void main(String[] args)
	{
		WumpusGame game = new WumpusGame(4,4);
		final WumpusGameForm form = new WumpusGameForm(game);
		Thread t = new Thread(){
			@Override
			public void run()
			{
				form.setVisible(true);
			}
		};
		
		t.start();
		WumpusWorldSolver.CHICKEN_LITTLE_SEARCH(game);
	}
}
