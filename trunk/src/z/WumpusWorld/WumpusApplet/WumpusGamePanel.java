package z.WumpusWorld.WumpusApplet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import z.WumpusWorld.WumpusGame.WumpusGame;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class WumpusGamePanel extends javax.swing.JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -990778910616011830L;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private WumpusGame game;
	private JButton[][] buttons;
	private JPanel pnlGame;
	private JTextArea txtArea;
	private JScrollPane scrollPane;
	private JList listKey;
	private JLabel lblDeaths;
	private JButton btnNewGame;
	private JLabel lblMoves;
	private JLabel lblTitle;
	private JLabel lblWins;
	private JLabel lblKills;
	private JLabel lblGold;
	private GridLayout pnlGameLayout;
	private boolean gameInitialized = false;
	private KeyListener keyHandler;
	private boolean showingBoard;
	private Color agentColor;
	private WumpusGUIController controller;
	private boolean hadGold;
	
	public WumpusGamePanel(WumpusGame game, WumpusGUIController controller) {
		super();
		this.game = game;
		this.controller = controller;
		initGUI();
		keyHandler = new WumpusKeyHandler(game);
		//this.addKeyListener(keyHandler);
		game.addObserver(this);
		this.update(game,null);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				txtArea.requestFocus();
			}
		});
		
		this.txtArea.setText("Welcome to Wumpus World!");
	}

	private void initBoard()
	{
		if(gameInitialized)
		{
			remove(pnlGame);
		}
		agentColor = Color.white;
		pnlGame = new JPanel();
		pnlGame.setBounds(191, 1, 400, 400);
		
		pnlGameLayout = new GridLayout(game.getHeight(),game.getWidth());
		
		buttons = new JButton[game.getWidth()][game.getHeight()];
		for(int x = 0; x < buttons.length; x++)
		{
			for(int y = 0; y < buttons[x].length; y++)
			{
				buttons[x][y] = new JButton();
				buttons[x][y].setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
				buttons[x][y].setFont(new Font("sansserif", Font.BOLD, 18));
				buttons[x][y].setBackground(Color.gray);
				buttons[x][y].setEnabled(false);
			}
		}
		
		JButton[][] temp = new JButton[game.getHeight()][game.getWidth()];
		
		for(int x = 0; x < buttons.length; x++)
		{
			for(int y = 0; y < buttons[x].length; y++)
			{
				temp[y][x] = buttons[x][y];
			}
		}

		for(int x = 0; x < temp.length; x++){
			for(int y = 0; y < temp[x].length; y++)
			{
				pnlGame.add(temp[x][y]);
			}
		}
		
		pnlGameLayout.setHgap(0);
		pnlGameLayout.setVgap(0);
		
		gameInitialized = true;
		hadGold = false;
		pnlGame.setLayout(pnlGameLayout);
		
		add(pnlGame);
		pnlGame.setSize(400, 375);
		{
			lblTitle = new JLabel();
			add(lblTitle);
			lblTitle.setText("Wumpus World");
			lblTitle.setFont(new java.awt.Font("Andy",2,28));
			lblTitle.setBounds(11, 3, 180, 48);
			lblTitle.setForeground(new java.awt.Color(255,128,64));
		}

	}
	private void initGUI() {
		try {
			setLayout(null);
			setBackground(new java.awt.Color(255,255,255));
			{
				initBoard();
			}
			{
				txtArea =  new JTextArea("", 5, 50);
				txtArea.setLineWrap(true);
				txtArea.setEditable(false);
				txtArea.addKeyListener(new WumpusKeyHandler(game));
			}
			{
				scrollPane = new JScrollPane(txtArea);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				add(scrollPane);
				scrollPane.setBounds(191, 388, 400, 150);
			}
			{
				ListModel listKeyModel = 
						new DefaultComboBoxModel(
								new String[] { 
										"Button Key",
										" Move Buttons:",
										"  Up:     " + WumpusKeyHandler.KEY_UP,
										"  Down:   " + WumpusKeyHandler.KEY_DOWN,
										"  Right:  " + WumpusKeyHandler.KEY_RIGHT,
										"  Left:   " + WumpusKeyHandler.KEY_LEFT,
										" Action Buttons:",
										"  Grab:   " + WumpusKeyHandler.KEY_GRAB,
										"  Climb:  " + WumpusKeyHandler.KEY_CLIMB,
										"  Fire:   " + WumpusKeyHandler.KEY_FIRE
										});
				listKey = new JList();
				add(listKey);
				listKey.setModel(listKeyModel);
				listKey.setBounds(0, 47, 179, 251);
				listKey.setEnabled(false);
				listKey.setFont(new java.awt.Font("DialogInput",0,16));
				listKey.setBackground(new java.awt.Color(255,255,255));
			}
			{
				lblMoves = new JLabel();
				add(lblMoves);
				lblMoves.setText("Moves:");
				lblMoves.setFont(new java.awt.Font("DialogInput",0,20));
				lblMoves.setBounds(6, 448, 180, 48);
			}
			{
				lblGold = new JLabel();
				add(lblGold);
				lblGold.setText("Gold:");
				lblGold.setBounds(6, 295, 180, 48);
				lblGold.setFont(new java.awt.Font("DialogInput",0,20));
			}
			{
				lblDeaths = new JLabel();
				add(lblDeaths);
				lblDeaths.setText("Deaths:");
				lblDeaths.setBounds(6, 333, 180, 48);
				lblDeaths.setFont(new java.awt.Font("DialogInput",0,20));
			}
			{
				lblKills = new JLabel();
				add(lblKills);
				lblKills.setText("Wumpus Kills:");
				lblKills.setFont(new java.awt.Font("DialogInput",0,20));
				lblKills.setBounds(6, 408, 180, 48);
			}
			{
				lblWins = new JLabel();
				add(lblWins);
				lblWins.setText("Wins:");
				lblWins.setFont(new java.awt.Font("DialogInput",0,20));
				lblWins.setBounds(6, 369, 180, 48);
			}
			{
				btnNewGame = new JButton();
				this.add(btnNewGame);
				btnNewGame.setText("NewGame");
				btnNewGame.setBounds(33, 512, 92, 26);
				btnNewGame.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						controller.newGame();
					}
					
				});
			}

			this.setSize(610, 576);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	private class WumpusKeyHandler implements KeyListener {

		private static final char KEY_UP = 'w';
		private static final char KEY_DOWN = 's';
		private static final char KEY_LEFT = 'a';
		private static final char KEY_RIGHT = 'd';
		private static final char KEY_GRAB = 'g';
		private static final char KEY_CLIMB = 'c';
		private static final char KEY_FIRE = 'f';
		
		private WumpusGame game;
		
		private boolean isFireing;
		public WumpusKeyHandler(WumpusGame game)
		{
			this.game = game;
			isFireing = false;
			hadGold = false;
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
			char key = e.getKeyChar();
			if(showingBoard)
			{
				initBoard();
				showingBoard = false;
				update(game,null);
			}
			else
			{
				if(isFireing)
				{
					boolean fired = false;
					switch(key)
					{
						case KEY_UP:
							game.fireUp();
							fired = true;
							break;
						case KEY_DOWN:
							game.fireDown();
							fired = true;
							break;
						case KEY_LEFT:
							game.fireLeft();
							fired = true;
							break;
						case KEY_RIGHT:
							game.fireRight();
							fired = true;
							break;
						default:
							break;
					}
					boolean wumpusDead = WumpusGamePanel.this.game.didWumpusDie();
					String message;
					if(wumpusDead)
					{
						message = "You hear a deathlike scream!";
					}
					else if(fired)
					{
						message = "It would seem you need to work on your aim";
					}
					else
					{
						message = "Fireing sequence canceled";
					}
					if(txtArea.getText().length() != 0)
					{
						WumpusGamePanel.this.txtArea.setText(txtArea.getText() + "\n" + message);
					}
					else
					{
						WumpusGamePanel.this.txtArea.setText(message);
					}
					
					isFireing = false;
				}
				else
				{
					boolean move = true;
					switch(key)
					{
						case KEY_UP:
							move = game.moveUp();
							break;
						case KEY_DOWN:
							move = game.moveDown();
							break;
						case KEY_LEFT:
							move = game.moveLeft();
							break;
						case KEY_RIGHT:
							move = game.moveRight();
							break;
						case KEY_GRAB:
							game.grabGold();
							if(game.hasGold())
							{
								agentColor = Color.yellow;
								if(hadGold == false)
								{
									hadGold = true;
									WumpusGamePanel.this.update(game, null);
								}
							}
							
							break;
						case KEY_CLIMB:
							game.climb();
							break;
						case KEY_FIRE:
							boolean hasFired = WumpusGamePanel.this.game.hasFired();
							String message;
							if(hasFired)
							{
								message = "You already fired! You has no more ammo!";
							}
							else
							{
								message = "Which Direction? (use WASD or any other key to cancel)";
								isFireing = true;
							}
							if(txtArea.getText().length() != 0)
							{
								WumpusGamePanel.this.txtArea.setText(txtArea.getText() + "\n" + message);
							}
							else
							{
								WumpusGamePanel.this.txtArea.setText(message);
							}
							break;
					}
					
					if(!move)
					{
						String message = "Bump! Sorry you can't walk through walls...";
						if(txtArea.getText().length() != 0)
						{
							WumpusGamePanel.this.txtArea.setText(txtArea.getText() + "\n" + message);
						}
						else
						{
							WumpusGamePanel.this.txtArea.setText(message);
						}
					}
				}
			}
			updateStatusTexts();
		}
		
	}
	
	private void updateTextArea()
	{
		char[] status = game.getStatusAtLocation(game.getAgentLocation());
		
		String message = "";
	
		for(int i = 0; i < status.length; i++)
		{
			switch(status[i])
			{
				case WumpusGame.STATUS_BREEZE:
					if(i > 0)
					{
						message += "\n";
					}
					message += "It sure is windy here";
					break;
				case WumpusGame.STATUS_GOLD:
					if(i > 0)
					{
						message += "\n";
					}
					message += "Ooooo! Shinny!";
					break;
				case WumpusGame.STATUS_HOLE:
					message = "Oops! There is no ground there, you fell to your death =(";
					i = status.length;
					break;
				case WumpusGame.STATUS_STENCH:
					if(i > 0)
					{
						message += "\n";
					}
					message += "Ewww it sure smells in here";
					break;
				case WumpusGame.STATUS_WUMPUS:
					i = status.length;
					message = "The boogy-man? No wait! Ahh! You were eaten by the Wumpus =(";
					break;
			}
		}
		if(message.length() == 0)
		{
			return;
		}
		if(txtArea.getText().length() != 0)
		{
			this.txtArea.setText(txtArea.getText() + "\n" + message);
		}
		else
		{
			this.txtArea.setText(message);
		}
	}
	
	@Override
	public void update(Observable o, Object object) {
		
		Thread t = new Thread()
		{
			public void run()
			{
				System.out.println("Updating");
				boolean[][] discoveredTiles = game.getDiscoveredTiles();
				for(int x = 0; x < discoveredTiles.length; x++)
				{
					for(int y = 0; y < discoveredTiles[x].length; y++)
					{
						if(discoveredTiles[x][y])
						{
							buttons[x][y].setText(game.getStatus()[x][y]);
							buttons[x][y].setBackground(Color.LIGHT_GRAY);
							buttons[x][y].repaint();
						}
					}
				}
				
				buttons[game.getAgentLocation().x][game.getAgentLocation().y].setBackground(agentColor);
				buttons[game.getAgentLocation().x][game.getAgentLocation().y].repaint();
				
				updateTextArea();
				updateStatusTexts();
				
				if(game.didAgentDie() || game.didAgentFinish())
				{
					agentColor = Color.LIGHT_GRAY;

					Point start = game.getStartLocation();
					String text = buttons[start.x][start.y].getText();
					for(int x = 0; x < discoveredTiles.length; x++)
					{
						for(int y = 0; y < discoveredTiles[x].length; y++)
						{
							buttons[x][y].setText(game.getStatus()[x][y]);
							buttons[x][y].setBackground(Color.LIGHT_GRAY);
							buttons[x][y].repaint();
						}
					}
					buttons[start.x][start.y].setText(text);
					String message = "Press any key to continue";
					if(txtArea.getText().length() != 0)
					{
						WumpusGamePanel.this.txtArea.setText(txtArea.getText() + "\n" + message);
					}
					else
					{
						WumpusGamePanel.this.txtArea.setText(message);
					}
					
					showingBoard = true;
				}
			}
		};
		
		t.run();
	}
	
	private void updateStatusTexts()
	{		
		lblGold.setText(  "Gold:   " + game.getGold());
		lblDeaths.setText("Deaths: " + game.getDeaths());
		lblWins.setText(  "Wins:   " + game.getWins());
		lblKills.setText( "Kills:  " + game.getWumpusKills());
		lblMoves.setText( "Moves:  " + game.getNumMoves());
	}
	

}
