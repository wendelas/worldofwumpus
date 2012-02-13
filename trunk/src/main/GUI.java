package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class GUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -693036000167528691L;

	private static int HEIGHT = 600, WIDTH = 600;
	
	private final boolean RESIZABLE = false;
	
	//GUI variables
	//private static JPanel buttonPanel;
	//private static JPanel resultsPanel;
	//private static JButton run;
	//private static JButton reset;
	private JTextArea results;
	private JScrollPane scrollable;
	
	
	public GUI(){
		//Setting Window Properties
		super("World Of Wumpus");
		setSize(WIDTH,HEIGHT);
		setResizable(RESIZABLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowEventListener());
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-WIDTH)/2,
					(Toolkit.getDefaultToolkit().getScreenSize().height-HEIGHT)/2);
		
		//Set layout to null
		setLayout(null);
		
		
		
		//Add elements
		addTextPanel();
		addButtonPanel();
		
		//Sets Visible
		setVisible(true);
		
		
	}
	
	
	
	
	private void addTextPanel(){
		//Sets up the result
		results = new JTextArea();
		results.setBackground(Color.white);
		results.setForeground(Color.black);
		results.setEditable(false);
		
		//Sets up the scroll pane
		scrollable = new JScrollPane(results);
		scrollable.setAutoscrolls(true);
		scrollable.setFocusTraversalKeysEnabled(false);
		scrollable.setFocusable(true);
		scrollable.setLocation(10, 10);
		scrollable.setSize(WIDTH-25, 450);
		
		//Adds it to the frame
		this.add(scrollable);
	}
	
	
	
	
	
	private void addButtonPanel(){
		JPanel buttonPanel = new JPanel(new GridLayout(1,2));
		buttonPanel.setSize(WIDTH-25, 100);
		buttonPanel.setLocation(10,465);
		
		//Generate Buttons
		buttonPanel.add(createButtons("Run"));
		buttonPanel.add(createButtons("Reset"));
		
		//Add to main Panel
		this.add(buttonPanel);
		
	}
	
	private JButton createButtons(String name){
		JButton button = new JButton(name);
		button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 60));
		button.setToolTipText(name);
		button.setActionCommand(name);
		button.addActionListener(new ButtonListener());
		
		return button;
	}



	
	
	
	
	
	
	//Listens to any of the Buttons Pressed
	 private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	
	}
	
	 
	 
	 
	 
	 
	 //Listens for any window state changes like closing
	private class WindowEventListener implements WindowListener{

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
	
}
