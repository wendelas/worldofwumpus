package z.propositional_logic;

import z.WumpusWorld.WumpusForm.WumpusGameForm;
import z.WumpusWorld.WumpusGame.WumpusGame;
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
