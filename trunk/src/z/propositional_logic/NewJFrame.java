package z.propositional_logic;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

public class NewJFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3761689795708152783L;

	/**
	* Auto-generated main method to display this JFrame
	*/
	
	public NewJFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			pack();
			setSize(400, 300);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
