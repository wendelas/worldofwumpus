package z.WumpusWorld.WumpusApplet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;



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
public class WumpusNewGamePanel extends javax.swing.JPanel {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 9065892428095947447L;
	
	private JTextField txtRows;
	private JTextField txtCols;
	private JLabel lblTitle;
	private JRadioButton rdoHuman;
	private JTextField txtWumpuses;
	private JLabel jLabel1;
	private JTextField txtGold;
	private JButton btnNewGame;
	private JLabel jLabel2;
	private JCheckBox chkRopes;
	private JCheckBox chkWings;
	private JCheckBox chkFlashlight;
	private JCheckBox chkRubbish;
	private JCheckBox chkBats;
	private JCheckBox chkAmmo;
	private JRadioButton rdoCustomRules;
	private JRadioButton rdoOfficialRules;
	private JRadioButton rdoAI;
	private JLabel lblCols;
	private JLabel lblRows;
	private WumpusGUIController controller;
	public WumpusNewGamePanel(WumpusGUIController controller) {
		super();
		this.controller = controller;
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(227, 235));
			this.setBackground(new java.awt.Color(255,255,255));
			this.setLayout(null);
			this.setSize(600, 500);
			{
				txtRows = new JTextField();
				this.add(txtRows);
				txtRows.setBounds(300, 158, 50, 23);
				txtRows.setText("5");
			}
			{
				txtCols = new JTextField();
				this.add(txtCols);
				txtCols.setBounds(300, 187, 50, 23);
				txtCols.setText("5");
			}
			{
				lblRows = new JLabel();
				this.add(lblRows);
				lblRows.setText("Rows:");
				lblRows.setBounds(215, 194, 43, 16);
			}
			{
				lblCols = new JLabel();
				this.add(lblCols);
				lblCols.setText("Cols:");
				lblCols.setBounds(215, 165, 43, 16);
			}
			{
				lblTitle = new JLabel();
				this.add(lblTitle);
				lblTitle.setText("Wumpus World");
				lblTitle.setForeground(new java.awt.Color(255,128,64));
				lblTitle.setFont(new java.awt.Font("Andy",2,28));
				lblTitle.setBounds(194, 62, 181, 40);
			}
			ButtonGroup group = new ButtonGroup();
			{
				rdoHuman = new JRadioButton();
				this.add(rdoHuman);
				rdoHuman.setText("Human");
				rdoHuman.setBounds(283, 356, 67, 20);
				rdoHuman.setBackground(new java.awt.Color(255,255,255));
				group.add(rdoHuman);
				rdoHuman.setSelected(true);
			}
			{
				rdoAI = new JRadioButton();
				this.add(rdoAI);
				rdoAI.setText("AI");
				rdoAI.setBounds(225, 356, 58, 20);
				rdoAI.setBackground(new java.awt.Color(255,255,255));
				group.add(rdoAI);
				rdoAI.setEnabled(false);
			}
			{
				btnNewGame = new JButton();
				this.add(btnNewGame);
				btnNewGame.setText("New Game");
				btnNewGame.setBounds(237, 390, 99, 23);
				btnNewGame.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try{
							WumpusNewGamePanel.this.btnNewGame.setEnabled(false);
							WumpusNewGamePanel.this.txtCols.setEnabled(false);
							WumpusNewGamePanel.this.txtRows.setEnabled(false);
							WumpusNewGamePanel.this.lblCols.setEnabled(false);
							WumpusNewGamePanel.this.lblRows.setEnabled(false);
							WumpusNewGamePanel.this.rdoAI.setEnabled(false);
							WumpusNewGamePanel.this.rdoHuman.setEnabled(false);
							controller.handleNewGame(WumpusNewGamePanel.this, getRows(), getCols());
						}
						catch (Exception e)
						{
							JOptionPane.showMessageDialog(null, "Invalid input");
						}
					}
					
				});
			}
			{
				txtWumpuses = new JTextField();
				this.add(txtWumpuses);
				txtWumpuses.setBounds(300, 216, 50, 23);
				txtWumpuses.setText("1");
				txtWumpuses.setEnabled(false);
			}
			{
				txtGold = new JTextField();
				this.add(txtGold);
				txtGold.setBounds(300, 245, 50, 23);
				txtGold.setText("1");
				txtGold.setEnabled(false);
			}
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Wumpuses:");
				jLabel1.setBounds(215, 222, 78, 16);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("Gold:");
				jLabel2.setBounds(215, 250, 43, 16);
			}
			ButtonGroup rules = new ButtonGroup();
			{
				rdoOfficialRules = new JRadioButton();
				this.add(rdoOfficialRules);
				rdoOfficialRules.setText("Official Rules");
				rdoOfficialRules.setBounds(227, 100, 109, 20);
				rdoOfficialRules.setBackground(new java.awt.Color(255,255,255));
				rdoOfficialRules.setSelected(true);
				rdoOfficialRules.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						txtWumpuses.setEnabled(false);
						txtGold.setEnabled(false);
						chkRopes.setEnabled(false);
						chkAmmo.setEnabled(false);
						chkFlashlight.setEnabled(false);
						chkBats.setEnabled(false);
						chkRubbish.setEnabled(false);
						chkWings.setEnabled(false);
					}
					
				});
				rules.add(rdoOfficialRules);
			}
			{
				rdoCustomRules = new JRadioButton();
				this.add(rdoCustomRules);
				rdoCustomRules.setText("Custom Rules");
				rdoCustomRules.setBounds(227, 127, 128, 20);
				rdoCustomRules.setBackground(new java.awt.Color(255,255,255));
				rdoCustomRules.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						txtWumpuses.setEnabled(true);
						txtGold.setEnabled(true);
						chkRopes.setEnabled(true);
						chkAmmo.setEnabled(true);
						chkFlashlight.setEnabled(true);
						chkBats.setEnabled(true);
						chkRubbish.setEnabled(true);
						chkWings.setEnabled(true);
					}
					
				});
				rules.add(rdoCustomRules);
				rdoCustomRules.setEnabled(false);
			}
			{
				chkRopes = new JCheckBox();
				this.add(chkRopes);
				chkRopes.setText("Ropes");
				chkRopes.setBounds(185, 284, 61, 20);
				chkRopes.setBackground(new java.awt.Color(255,255,255));
				chkRopes.setEnabled(false);
				chkRopes.setToolTipText("Ropes are a one time use to get you out of a hole");
			}
			{
				chkAmmo = new JCheckBox();
				this.add(chkAmmo);
				chkAmmo.setText("Ammo");
				chkAmmo.setBounds(246, 284, 69, 20);
				chkAmmo.setBackground(new java.awt.Color(255,255,255));
				chkAmmo.setEnabled(false);
				chkAmmo.setToolTipText("Replenish your ammo capacity");
			}
			{
				chkBats = new JCheckBox();
				this.add(chkBats);
				chkBats.setText("Bats");
				chkBats.setBounds(185, 313, 56, 20);
				chkBats.setBackground(new java.awt.Color(255,255,255));
				chkBats.setEnabled(false);
				chkBats.setToolTipText("Bats always come in pairs on random locations around the map.  If a bat senses someone below it, it will grab you and fly you over to the other bat.  (Bats can reside over a hole)");
			}
			{
				chkRubbish = new JCheckBox();
				this.add(chkRubbish);
				chkRubbish.setText("Rubbish");
				chkRubbish.setBounds(246, 313, 74, 20);
				chkRubbish.setBackground(new java.awt.Color(255,255,255));
				chkRubbish.setEnabled(false);
				chkRubbish.setToolTipText("Random objects you can grab and pick up for very small amounts of extra gold");
			}
			{
				chkFlashlight = new JCheckBox();
				this.add(chkFlashlight);
				chkFlashlight.setText("Flashlight");
				chkFlashlight.setBounds(320, 284, 86, 20);
				chkFlashlight.setBackground(new java.awt.Color(255,255,255));
				chkFlashlight.setEnabled(false);
				chkFlashlight.setToolTipText("Flashlights light the way allowing you to see an additional square in every direction");
			}
			{
				chkWings = new JCheckBox();
				this.add(chkWings);
				chkWings.setText("Wings");
				chkWings.setBounds(320, 313, 67, 20);
				chkWings.setBackground(new java.awt.Color(255,255,255));
				chkWings.setEnabled(false);
				chkWings.setToolTipText("Finding wings will allow you to fly to any square on the map.  Wings have a one time use.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getRows()
	{
		return Integer.parseInt(txtRows.getText());
	}
	
	public int getCols()
	{
		return Integer.parseInt(txtCols.getText());
	}

}
