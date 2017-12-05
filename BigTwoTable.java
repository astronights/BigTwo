import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Public class to represent a BigTwoTable object containing all variables and member functions of the GUI.
 * @author shubh31
 *
 */
public class BigTwoTable implements CardGameTable
{
	private BigTwoClient game; //The BigTwo game client created.
	private boolean[] selected; //The array with boolean values true for selected cards.
	private int activePlayer; //The index of the activePlayer.
	private JFrame frame; //The main frame for the GUI
	private JPanel bigTwoPanel; //The panel representing the green area of the Table.
	private JButton playButton; //The Play Button
	private JButton passButton; //The Pass Button
	private JTextArea msgArea; //The message area where text is displayed.
	private JTextArea chat; //The chat area.	
	private Image[][] cardImages; //The array storing all images of the cards.
	private Image cardBackImage; //The variable storing the back image of the card.
	private Image[] avatars; //The array storing the images of the avatars.
	private BigTwoPanel player0, player1, player2, player3, lasthand; //Five panels to display the five parts of the bigTwoPanel
	private JTextField typein;
	Border blackline; //The border object.
	private JScrollPane textarea; //The scroll pane for the message area.
	
	/**
	 * Public constructor to initialise the game and set it up.
	 * @param game
	 * 		The BigTwo game.
	 */
	public BigTwoTable(BigTwoClient game)
	{
		this.game = game; 
		this.setUp();
		blackline = BorderFactory.createLineBorder(Color.black);
		selected = new boolean[13];
		this.reset();
		this.setActivePlayer(game.getCurrentIdx());
	}
	/**
	 * Public function to set up all elements of the GUI.
	 */
	public void setUp()
	{
		//Adding avatars
		avatars = new Image[4];
		avatars[0] = new ImageIcon("src/avatars/batman_128.png").getImage();
		avatars[1] = new ImageIcon("src/avatars/flash_128.png").getImage();
		avatars[2] = new ImageIcon("src/avatars/superman_128.png").getImage();
		avatars[3] = new ImageIcon("src/avatars/wonder_woman_128.png").getImage();
		
		//Adding cards
		char[] suits = { 'd', 'c', 'h', 's'}; //Array to store card suits.
		char[] ranks = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' }; //Array to store card ranks.
		cardImages = new Image[4][13]; //String to store file path.
		String location = new String();
		cardBackImage = new ImageIcon("src/cards/b.gif").getImage();
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 13; j++)
			{
				location = "src/cards/"+ ranks[j] + suits[i] + ".gif";
				cardImages[i][j] = new ImageIcon(location).getImage();
			}
		}
		
		//Creating the frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		//Making the menubar
		JMenuBar menubar = new JMenuBar(); //The menubar item.
		JMenu gamemenu = new JMenu("Game"); //The Game menu item.
		JMenuItem connect = new JMenuItem("Connect"); //The menu option Restart.
		JMenuItem quit = new JMenuItem("Quit"); //The menu option Quit.
		connect.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());
		gamemenu.add(connect);
		gamemenu.add(quit);
		JMenu messagemenu = new JMenu("Message"); //The message menu item.
		JMenuItem clear = new JMenuItem("Clear"); //The clear chat area menu item.
		clear.addActionListener(new ClearMenuItemListener());
		messagemenu.add(clear);
		menubar.add(gamemenu);
		menubar.add(messagemenu);
		frame.setJMenuBar(menubar);
		
		//Buttons Panel at the bottom
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		JPanel buttons = new JPanel(); //Panel for the two buttons.
		JPanel lowerpanel = new JPanel();
		lowerpanel.setLayout(new BorderLayout());
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttons.add(playButton);
		buttons.add(new JSeparator());
		buttons.add(passButton);
		buttons.setBackground(Color.gray.brighter().brighter());
		JLabel messagelabel = new JLabel("Message: ");
		messagelabel.setFont(new Font( null, Font.PLAIN, 17));
		typein = new JTextField(39);
		typein.addActionListener(new TextFieldListener());
		typein.setPreferredSize(new Dimension(30, 27));
		typein.setFont(new Font(null, Font.PLAIN, 17));
		typein.setFocusable(true);
		JPanel messagearea = new JPanel();
		messagearea.setLayout(new FlowLayout(FlowLayout.CENTER));
		messagearea.add(messagelabel);
		messagearea.add(typein);
		messagearea.setBackground(Color.gray.brighter().brighter());
		lowerpanel.setBackground(Color.gray.brighter().brighter());
		lowerpanel.add(buttons, BorderLayout.CENTER);
		lowerpanel.add(messagearea, BorderLayout.EAST);		
		
		//Making the Green area
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));
		player0 = new BigTwoPanel();
		player1 = new BigTwoPanel();
		player2 = new BigTwoPanel();
		player3 = new BigTwoPanel();
		lasthand = new BigTwoPanel();
		bigTwoPanel.add(player0);
		bigTwoPanel.add(player1);
		bigTwoPanel.add(player2);
		bigTwoPanel.add(player3);
		bigTwoPanel.add(lasthand);
		frame.add(BorderLayout.SOUTH, lowerpanel);
		frame.add(bigTwoPanel);
		
		//The text area on the right side.
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		msgArea = new JTextArea();
		msgArea.setFont(new Font("Courier", Font.PLAIN, 20));
		msgArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret)msgArea.getCaret(); //Setting the scrollbar to always remain down.
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textarea = new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textarea.setPreferredSize(new Dimension(600, 450));
		chat = new JTextArea();
		chat.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		chat.setEditable(false);
		DefaultCaret caret1 = (DefaultCaret)chat.getCaret(); //Setting the scrollbar to always remain down.
		caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane chatarea = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		chatarea.setPreferredSize(new Dimension(600, 450));
		right.add(textarea);
		right.add(chatarea);
		frame.getContentPane().add(BorderLayout.EAST, right);
		
		//Displaying frame at the end.
		frame.setTitle("BigTwo");
		frame.setSize(1500, 950);
		frame.setVisible(true);
	}
	/**
	 * Public setter function to set the value of the private variable activePlayer.
	 * @see CardGameTable#setActivePlayer(int)
	 */
	public void setActivePlayer(int activePlayer)
	{
		this.activePlayer = activePlayer;
	}
	/**
	 * Public getter function to return an array with the selected indices from a boolean array containing truth values of selected cards.
	 * @see CardGameTable#getSelected()
	 */
	public int[] getSelected()
	{
		int size = 0; //Number of selected cards.
		for(int i = 0; i < 13; i++)
		{
			if(selected[i])
			{
				size += 1;
			}
		}
		int[] cards = new int[size]; //Array to store indices.
		int count = 0;
		for(int i = 0; i < 13; i++)
		{
			if(selected[i])
			{
				cards[count] = i;
				count += 1;
			}
		}
		if(size == 0)
		{
			return null;
		}
		else
		{
			return cards;
		}
	}
	/**
	 * Public function to repaint the entire GUI.
	 * @see CardGameTable#repaint()
	 */
	public void repaint()
	{
		if(game.getPlayerID() != game.getCurrentIdx())
		{
			this.disable();
		}
		else
		{
			this.enable();
		}
		this.resetSelected();
		bigTwoPanel.repaint();
	}
	/**
	 * Public function to print a message on the text area.
	 * @see CardGameTable#printMsg(java.lang.String)
	 */
	public void printMsg(String msg)
	{
		msgArea.append(msg);
	}
	/**
	 * Public function to print a message on the chat area.
	 * @see CardGameTable#printMsg(java.lang.String)
	 */
	public void chatMsg(String msg)
	{
		chat.append(msg + "\n");
	}
	/**
	 * Public function to clear text from the msgArea.
	 * @see CardGameTable#clearMsgArea()
	 */
	public void clearMsgArea()
	{
		msgArea.setText("");
	}
	/**
	 * Public function to reset the GUI.
	 * @see CardGameTable#reset()
	 */
	public void reset()
	{
		this.resetSelected();
		this.clearMsgArea();
	}
	/**
	 * Public function to reset the list of selected cards.
	 * @see CardGameTable#resetSelected()
	 */
	public void resetSelected()
	{
		for(int i = 0; i < 13; i++)
		{
			selected[i] = false;
		}
	}
	/**
	 * Public function to enable functionality of the buttons and the panel.
	 * @see CardGameTable#enable()
	 */
	public void enable()
	{
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}
	/**
	 * Public function to disable functionality of the buttons and the panel.
	 * @see CardGameTable#disable()
	 */
	public void disable()
	{
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * This class is used to represent an object of BigTwoPanel and contains all functions to access components of the panel.
	 * @author shubh31
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		/**
		 * Public constructor for the panel to add a MouseListener.
		 */
		public BigTwoPanel() 
		{
			this.addMouseListener(this);
		}
		/**
		 * Public function to figure where the mouse if clicked and if it is used to select or deselect a card.
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			int xc = e.getX(); //X-coordinate of mouse click.
			int yc = e.getY(); //Y-coordinate of mouse click.
			if(e.getComponent().getY() == 1 + (player1.getY()-player0.getY())*activePlayer)
			{
				if(xc >= 200 && xc <= 200+ 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-1)+cardBackImage.getWidth(this))
				{
					if(xc >= 200+ 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-1) && xc < 200+ 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-1) + cardBackImage.getWidth(this))
					{
						if(game.getPlayerList().get(activePlayer).getCardsInHand().size() >= 2)
						{
							if(selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == true && selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-2] == false && xc < 200+ 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-2)+cardBackImage.getWidth(this) && yc >= this.getHeight() - avatars[0].getHeight(this)+cardBackImage.getHeight(this)+10 && yc < this.getHeight() - avatars[0].getHeight(this)+20 +cardBackImage.getHeight(this)) 
							{
								selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-2] = true;//Lower condition
							}
							else if(selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == false && selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-2] == true && xc < 200+ 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-2)+cardBackImage.getWidth(this) && yc >= this.getHeight() - avatars[0].getHeight(this) + 10 && yc < this.getHeight() - avatars[0].getHeight(this)+20)
							{
								selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-2] = false;//Upper condition
							}
							else
							{
								if(selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == true && yc>= this.getHeight() - avatars[0].getHeight(this) + 10 && yc < this.getHeight() + 10 - avatars[0].getHeight(this)+cardBackImage.getHeight(this))
								{
									selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] = false;
								}
								else if(selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == false && yc>= this.getHeight() - avatars[0].getHeight(this) + 20 && yc < this.getHeight() - avatars[0].getHeight(this) + 20+cardBackImage.getHeight(this))
								{
									selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] = true;
								}
							}
						}
						else if(game.getPlayerList().get(activePlayer).getCardsInHand().size() == 1)
						{
							if(selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == true && yc>= this.getHeight() - avatars[0].getHeight(this) + 10 && yc < this.getHeight() + 10 - avatars[0].getHeight(this)+cardBackImage.getHeight(this))
							{
								selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] = false;
							}
							else if(selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == false && yc>= this.getHeight() - avatars[0].getHeight(this) + 20 && yc < this.getHeight() - avatars[0].getHeight(this) + 20+cardBackImage.getHeight(this))
							{
								selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] = true;
							}
						}
					}
					else
					{
						for(int i = 0; i < 12; i++)
						{
							if(xc >= 200+(40*i) && xc < 200+(40*(i+1)))
							{
								if(selected[i] == true && yc>= this.getHeight() - avatars[0].getHeight(this) + 10 && yc < this.getHeight() +10 - avatars[0].getHeight(this)+cardBackImage.getHeight(this))
								{
									selected[i] = false;
								}
								else if(selected[i] == false && yc>= this.getHeight() - avatars[0].getHeight(this) + 20 && yc < this.getHeight() - avatars[0].getHeight(this) + 20+cardBackImage.getHeight(this))
								{
									selected[i] = true;
								}
	 						}
							else if(xc >= 200+(40*i) && xc < 200+(40*i)+cardBackImage.getWidth(this) && selected[i] == true && selected[i+1] == false && yc >= this.getHeight() - avatars[0].getHeight(this) + 10 && yc < this.getHeight() - avatars[0].getHeight(this)+20)
							{
								selected[i] = false;
							}
							else if(xc >= 200+(40*i) && xc < 200+(40*i)+cardBackImage.getWidth(this) && selected[i] == false && selected[i+1] == true && yc >= this.getHeight() - avatars[0].getHeight(this)+cardBackImage.getHeight(this)+10 && yc < this.getHeight() - avatars[0].getHeight(this)+20 +cardBackImage.getHeight(this))
							{
								selected[i] = true;
							}
						}
					}
				}
			}
			player0.repaint();
			player1.repaint();
			player2.repaint();
			player3.repaint();
			lasthand.repaint();
		}

		/**
		 * Public function to implement functionality when a mouse is pressed down.
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) 
		{
			// TODO Auto-generated method stub
			
		}

		/**
		 * Public function to implement functionality when a mouse is released.
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) 
		{
			// TODO Auto-generated method stub
			
		}

		/**
		 * Public function to implement functionality when a mouse enters a region.
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			// TODO Auto-generated method stub
			
		}

		/**
		 * Public function to implement functionality when a mouse exits a region.
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) 
		{
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * Public function to paint the panel by adding images of player and cards.
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			this.setBackground(Color.green.darker().darker());
			this.setBorder(blackline);
			Font bigFont = new Font("sans-serif", Font.BOLD, 20); //Creating the font.
			if(this == player0)
			{
				int x = this.getHeight() - avatars[0].getHeight(this);
				if(activePlayer == 0)
				{
					g.setColor(Color.blue);
				}
				if(game.getPlayerID() == 0)
				{
					g.setColor(Color.red);
				}
				g.setFont(bigFont);
				g.drawString(game.getPlayerList().get(0).getName(), 60, x-10);
				g.drawImage(avatars[0], 30, x, this);
				if(game.getPlayerID() == 0)
				{
					for(int i = 0; i < game.getPlayerList().get(0).getNumOfCards(); i++)
					{
						if(selected[i] == true)
						{
							g.drawImage(cardImages[game.getPlayerList().get(0).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(0).getCardsInHand().getCard(i).getRank()], 200+ (40*i), x+10, this);	
						}
						else
						{
							g.drawImage(cardImages[game.getPlayerList().get(0).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(0).getCardsInHand().getCard(i).getRank()], 200+ (40*i), x+20, this);
						}
					}
				}
				else
				{
					for(int i = 0; i < game.getPlayerList().get(0).getNumOfCards(); i++)
					{
						g.drawImage(cardBackImage, 200+ (40*i), x+20, this);
					}
				}
				
			}
			else if(this == player1)
			{
				int x = this.getHeight() - avatars[1].getHeight(this);
				g.setFont(bigFont);
				if(activePlayer == 1)
				{
					g.setColor(Color.blue);
				}
				if(game.getPlayerID() == 1)
				{
					g.setColor(Color.red);
				}
				g.drawString(game.getPlayerList().get(1).getName(), 60, x-10);
				g.drawImage(avatars[1], 30, x, this);
				if(game.getPlayerID() == 1)
				{
					for(int i = 0; i < game.getPlayerList().get(1).getNumOfCards(); i++)
					{
						if(selected[i] == true)
						{
							g.drawImage(cardImages[game.getPlayerList().get(1).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(1).getCardsInHand().getCard(i).getRank()], 200+ (40*i), x+10, this);	
						}
						else
						{
							g.drawImage(cardImages[game.getPlayerList().get(1).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(1).getCardsInHand().getCard(i).getRank()], 200+ (40*i), x+20, this);
						}
					}
				}
				else
				{
					for(int i = 0; i < game.getPlayerList().get(1).getNumOfCards(); i++)
					{
						g.drawImage(cardBackImage, 200+ (40*i), x+20, this);
					}
				}
			}
			else if(this == player2)
			{
				int x = this.getHeight() - avatars[2].getHeight(this);
				g.setFont(bigFont);
				if(activePlayer == 2)
				{
					g.setColor(Color.blue);
				}
				if(game.getPlayerID() == 2)
				{
					g.setColor(Color.red);
				}
				g.drawString(game.getPlayerList().get(2).getName(), 60, x-10);
				g.drawImage(avatars[2], 30, x, this);
				if(game.getPlayerID() == 2)
				{
					for(int i = 0; i < game.getPlayerList().get(2).getNumOfCards(); i++)
					{
						if(selected[i] == true)
						{
							g.drawImage(cardImages[game.getPlayerList().get(2).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(2).getCardsInHand().getCard(i).getRank()], 200+ (40*i), x+10, this);	
						}
						else
						{
							g.drawImage(cardImages[game.getPlayerList().get(2).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(2).getCardsInHand().getCard(i).getRank()], 200+ (40*i), x+20, this);
						}
					}
				}
				else
				{
					for(int i = 0; i < game.getPlayerList().get(2).getNumOfCards(); i++)
					{
						g.drawImage(cardBackImage, 200+ (40*i), x+20, this);
					}
				}
				
			}
			else if(this == player3)
			{
				int x = this.getHeight() - avatars[3].getHeight(this);
				g.setFont(bigFont);
				if(activePlayer == 3)
				{
					g.setColor(Color.blue);
				}
				if(game.getPlayerID() == 3)
				{
					g.setColor(Color.red);
				}
				g.drawString(game.getPlayerList().get(3).getName(), 60, x-10);
				g.drawImage(avatars[3], 30, x, this);
				if(game.getPlayerID() == 3)
				{
					for(int i = 0; i < game.getPlayerList().get(3).getNumOfCards(); i++)
					{
						if(selected[i] == true)
						{
							g.drawImage(cardImages[game.getPlayerList().get(3).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(3).getCardsInHand().getCard(i).getRank()], 200+ (40*i), x+10, this);	
						}
						else
						{
							g.drawImage(cardImages[game.getPlayerList().get(3).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(3).getCardsInHand().getCard(i).getRank()], 200+ (40*i), x+20, this);
						}
					}
				}
				else
				{
					for(int i = 0; i < game.getPlayerList().get(3).getNumOfCards(); i++)
					{
						g.drawImage(cardBackImage, 200+ (40*i), x+20, this);
					}
				}
				
			}
			else if(this == lasthand)
			{
				if(game.getHandsOnTable().size() != 0)
				{
					String pp = game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName();
					int p = -1;
					for(int i = 0; i < 4; i++)
					{
						if(game.getPlayerList().get(i).getName() == pp)
						{
							p = i;
						}
					}
					int x = this.getHeight() - avatars[0].getHeight(this);
					g.setFont(bigFont);
					for(int i = 0; i < game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).size(); i++)
					{
						g.drawString("Played by " + pp, 30, x-10);
						g.drawImage(avatars[p], 30, x, this);
						g.drawImage(cardImages[game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(i).getSuit()][game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(i).getRank()], 200+ (40*i), x+20, this);
					}
				}
			}
			
		}
	}
	/**
	 * Class to create a listener for the message input field.
	 * @author shubh31
	 *
	 */
	class TextFieldListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			game.sendMessage(new CardGameMessage(7, -1, typein.getText()));
			typein.setText(null);
		}
	}
	/**
	 * Class to create a listener for the Play Button.
	 * @author shubh31
	 *
	 */
	class PlayButtonListener implements ActionListener
	{

		/**
		 * Public function to make a move on clicking Play.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(getSelected() == null)
			{
				printMsg("Select cards to play.\n");
			}
			else
			{
				game.makeMove(activePlayer, getSelected());
			}
		}
		
	}
	/**
	 * Class to create a listener for the Pass Button.
	 * @author shubh31
	 *
	 */
	class PassButtonListener implements ActionListener
	{

		/**
		 * Public function to make a move with no cards.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			game.makeMove(activePlayer, null);
		}	
		
	}
	/**
	 * Class to create a listener for the Connect Button.
	 * @author shubh31
	 *
	 */
	class ConnectMenuItemListener implements ActionListener
	{

		/**
		 * Public function to restart the game with a new deck.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			//BigTwoDeck newBigTwoDeck = new BigTwoDeck();
			//newBigTwoDeck.shuffle();
			//game.start(newBigTwoDeck);
			if(game.getPlayerID() == -1)
			{
				clearMsgArea();
				game.makeConnection();
			}
			else
			{
				printMsg("You're already in the game.\n");
			}
		}
		
	}
	/**
	 * Class to create a listener for the Quit Button.
	 * @author shubh31
	 *
	 */
	class QuitMenuItemListener implements ActionListener
	{

		/**
		 * Public function to quit the application.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			System.exit(activePlayer);
		}
		
	}
	/**
	 * Class to create a listener for the Clear Button.
	 * @author shubh31
	 *
	 */
	class ClearMenuItemListener implements ActionListener
	{

		/**
		 * Public function to clear messages.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			chat.setText("");
		}
		
	}
}
