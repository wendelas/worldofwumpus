package WumpusWorld.WumpusApplet;

import javax.swing.JApplet;

public class WumpusApplet extends JApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4610862701254571101L;

	public void init()
	{
		WumpusGUIController controller = new WumpusGUIController(this);
		controller.newGame();
	}
}
