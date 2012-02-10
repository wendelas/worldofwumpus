package WumpusWorld.WumpusForm;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.border.LineBorder;

import WumpusWorld.WumpusGame.WumpusGame;


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
public class WumpusGameForm extends javax.swing.JFrame implements Observer {

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
	private JLabel lblTries;
	private JLabel lblMoves;
	private JLabel lblTitle;
	private JLabel lblWins;
	private JLabel lblKills;
	private JLabel lblGold;
	private GridLayout pnlGameLayout;
	private boolean gameInitialized = false;
	private static final int STATUS_LBL_LENGTH = 9;
	
	private Color agentColor;
	
	public WumpusGameForm(WumpusGame game) {
		super();
		this.game = game;
		initGUI();
		this.addKeyListener(new WumpusKeyHandler(game));
		game.addObserver(this);
		this.update(game, game.getDiscoveredTiles());
	}
	
	private void initBoard()
	{
		if(gameInitialized)
		{
			getContentPane().remove(pnlGame);
		}
		agentColor = Color.white;
		pnlGame = new JPanel();
		pnlGame.setBounds(231, 1, 400, 400);
		
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

		pnlGame.setLayout(pnlGameLayout);
		getContentPane().add(pnlGame);
		pnlGame.setSize(400, 375);
		{
			lblTitle = new JLabel();
			getContentPane().add(lblTitle);
			lblTitle.setText("Wumpus World");
			lblTitle.setFont(new java.awt.Font("Andy",2,28));
			lblTitle.setBounds(11, 3, 180, 48);
			lblTitle.setForeground(new java.awt.Color(255,128,64));
		}

	}
	private void initGUI() {
		try {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setResizable(false);
			getContentPane().setBackground(new java.awt.Color(255,255,255));
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
				getContentPane().add(scrollPane);
				scrollPane.setBounds(231, 388, 400, 150);
			}
			{
				ListModel listKeyModel = 
						new DefaultComboBoxModel(
								new String[] { 
										"Button Key",
										"  Move Buttons:",
										"     Up:        " + WumpusKeyHandler.KEY_UP,
										"     Down:   " + WumpusKeyHandler.KEY_DOWN,
										"     Right:    " + WumpusKeyHandler.KEY_RIGHT,
										"     Left:       " + WumpusKeyHandler.KEY_LEFT,
										"  Action Buttons:",
										"     Grab:     " + WumpusKeyHandler.KEY_GRAB,
										"     Climb:   " + WumpusKeyHandler.KEY_CLIMB,
										"     Fire:       " + WumpusKeyHandler.KEY_FIRE});
				listKey = new JList();
				getContentPane().add(listKey);
				listKey.setModel(listKeyModel);
				listKey.setBounds(0, 47, 179, 251);
				listKey.setEnabled(false);
				listKey.setFont(new java.awt.Font("Segoe UI",0,16));
				listKey.setBackground(new java.awt.Color(255,255,255));
			}
			{
				lblMoves = new JLabel();
				getContentPane().add(lblMoves);
				lblMoves.setText("Moves:");
				lblMoves.setFont(new java.awt.Font("DialogInput",0,20));
				lblMoves.setBounds(6, 448, 213, 48);
			}
			{
				lblGold = new JLabel();
				getContentPane().add(lblGold);
				lblGold.setText("Gold:");
				lblGold.setBounds(6, 295, 180, 48);
				lblGold.setFont(new java.awt.Font("DialogInput",0,20));
			}
			{
				lblDeaths = new JLabel();
				getContentPane().add(lblDeaths);
				lblDeaths.setText("Deaths:");
				lblDeaths.setBounds(6, 333, 225, 48);
				lblDeaths.setFont(new java.awt.Font("DialogInput",0,20));
			}
			{
				lblKills = new JLabel();
				getContentPane().add(lblKills);
				lblKills.setText("Wumpus Kills:");
				lblKills.setFont(new java.awt.Font("DialogInput",0,20));
				lblKills.setBounds(6, 408, 220, 48);
			}
			{
				lblWins = new JLabel();
				getContentPane().add(lblWins);
				lblWins.setText("Wins:");
				lblWins.setFont(new java.awt.Font("DialogInput",0,20));
				lblWins.setBounds(6, 369, 220, 48);
			}
			{
				lblTries = new JLabel();
				getContentPane().add(lblTries);
				lblTries.setText("Tries:");
				lblTries.setFont(new java.awt.Font("DialogInput",0,20));
				lblTries.setBounds(6, 490, 220, 40);
			}

			pack();
			this.setSize(640, 576);
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
			if(isFireing)
			{
				switch(key)
				{
					case KEY_UP:
						game.fireUp();
						break;
					case KEY_DOWN:
						game.fireDown();
						break;
					case KEY_LEFT:
						game.fireLeft();
						break;
					case KEY_RIGHT:
						game.fireRight();
						break;
					default:
						break;
				}
				boolean wumpusDead = WumpusGameForm.this.game.didWumpusDie();
				String message;
				if(wumpusDead)
				{
					message = "You hear a deathlike scream!";
				}
				else
				{
					message = "It would seem you need to work on your aim";
				}
				if(txtArea.getText().length() != 0)
				{
					WumpusGameForm.this.txtArea.setText(txtArea.getText() + "\n" + message);
				}
				else
				{
					WumpusGameForm.this.txtArea.setText(message);
				}
				
				isFireing = false;
			}
			else
			{
				switch(key)
				{
					case KEY_UP:
						game.moveUp();
						break;
					case KEY_DOWN:
						game.moveDown();
						break;
					case KEY_LEFT:
						game.moveLeft();
						break;
					case KEY_RIGHT:
						game.moveRight();
						break;
					case KEY_GRAB:
						game.grabGold();
						if(game.hasGold())
						{
							WumpusGameForm.this.update(game, null);
						}
						break;
					case KEY_CLIMB:
						game.climb();
						break;
					case KEY_FIRE:
						boolean hasFired = WumpusGameForm.this.game.hasFired();
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
							WumpusGameForm.this.txtArea.setText(txtArea.getText() + "\n" + message);
						}
						else
						{
							WumpusGameForm.this.txtArea.setText(message);
						}
						break;
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
		System.out.println("Updating");
		if(game.hasGold())
		{
			agentColor = Color.yellow;
		}
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
			System.out.println("Initializing");
			initBoard();
			System.out.println("Initialized");
		}
	}
	
	private void updateStatusTexts()
	{	
		int tries = game.getTries();
		int wins = game.getWins();
		int deaths = game.getDeaths();
		int kills = game.getWumpusKills();
		
		double winPerc = ((double)wins)/((double)tries)*100.00;
		double deathPerc = ((double)deaths)/((double)tries)*100.00;
		double killsPerc = ((double)kills)/((double)tries)*100.00;
		
		String winString = "" + winPerc;
		String deathString = "" + deathPerc;
		String killsString = "" + killsPerc;
		
		if(winString.length() > 5)
		{
			winString = winString.substring(0,5);
		}
		
		if(deathString.length() > 5)
		{
			deathString = deathString.substring(0,5);
		}
		
		if(killsString.length() > 5)
		{
			killsString = killsString.substring(0,5);
		}
		
		if(tries == 0)
		{
			winString = "00.00";
			deathString = "00.00";
			killsString = "00.00";
		}
		lblGold.setText(  "Gold:   " + game.getGold());
		lblDeaths.setText("Deaths: " + deaths + "(" + deathString + "%)");
		lblWins.setText(  "Wins:   " + wins + "(" + winString + "%)");
		lblKills.setText( "Kills:  " + kills + "(" + killsString + "%)");
		lblMoves.setText( "Moves:  " + game.getNumMoves());
		lblTries.setText( "Tries:  " + tries);
	}
	

}
