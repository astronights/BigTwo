
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Public class to represent a BigTwo object which contains all variables and member functions of the game including the main.
 * @author shubh31
 *
 */
public class BigTwoClient implements NetworkGame, CardGame
{	

	private Deck deck; // The deck of cards the game is played with.
	private ArrayList<CardGamePlayer> playerList; //The list of players
	private ArrayList<Hand> handsOnTable; //All hands that have been played on table.
	private int currentIdx; //Index of the current player
	private BigTwoTable table; //Object of the BigTwoTable
	private int playerID; //ID of the local player
	private String playerName; //Name of the local player
	private String serverIP; //IP address of the game server
	private int serverPort; //TCP Port of the game server
	private Socket sock; //socket connection to game server
	private ObjectOutputStream oos; //Object Output Stream to send messages
	private ObjectInputStream ois; //Object Input Stream to receive messages
	
	/**
	 * Constructor to create and initialize an object of BigTwo and initialize variables of the class.
	 */
	public BigTwoClient()
	{
		playerList = new ArrayList<CardGamePlayer>();
		for(int i = 0; i < 4; i++)
		{
			CardGamePlayer player = new CardGamePlayer();
			playerList.add(player);
		}
		handsOnTable = new ArrayList<Hand>();
		this.playerName = "";
		this.setPlayerID(-1);
		table = new BigTwoTable(this);
		this.makeConnection();
	}
	/**
	 * Public getter function to get the deck of the game.
	 * @return
	 * 		Returns the value of the private variable deck.
	 */
	public Deck getDeck()
	{
		return this.deck;
	}
	/**
	 * Public getter function to get the list of players of the game.
	 * @return
	 * 		Returns the value of the private Array List playerList.
	 */
	public ArrayList<CardGamePlayer> getPlayerList()
	{
		return this.playerList;
	}
	/**
	 * Public getter function to get the handsOnTable.
	 * @return
	 * 		Returns the value of the private Array List handsOnTable.
	 */
	public ArrayList<Hand> getHandsOnTable()
	{
		return this.handsOnTable;
	}
	/**
	 * Public getter function to get the index value of the current player.
	 * @return
	 * 		Returns the value of the private variable currentIdx.
	 */
	public int getCurrentIdx()
	{
		return this.currentIdx;
	}
	/**
	 * Public getter function to get the number of players playing the game.
	 * @see CardGame#getNumOfPlayers()
	 */
	public int getNumOfPlayers()
	{
		return this.playerList.size();
	}
	/**
	 * Public function which starts the game and handles the entire gameplay.
	 * @param deck
	 * 		The deck with which the game is played.
	 */
	public void start(Deck deck)
	{
		int x = 0;
		handsOnTable.clear();
		for(int i = 0; i < 4; i++)
		{
			playerList.get(i).removeAllCards();
			for(int j = 0; j < 13; j++)
			{
				playerList.get(i).addCard(deck.getCard(x+i+j)); //Distributing the cards to all players.
			}
			x += 12;
			playerList.get(i).getCardsInHand().sort();
		}
		currentIdx = -1;
		//int pp = -1; //Index of previous player.
		Card card = new Card(0,2); //Card for the 3 of diamonds.
		for(int i = 0; i < 4; i++)
		{
			if(playerList.get(i).getCardsInHand().contains(card))
			{
				//table.setActivePlayer(this.getPlayerID()); //Finding the active player.
				currentIdx = i;
				
			}
		}
		table.printMsg(this.getPlayerList().get(this.currentIdx).getName() + "'s turn.\n");
		table.repaint();
	}
	/**
	 * Public function to make the selected move.
	 * @see CardGame#makeMove(int, int[])
	 */
	public void makeMove(int playerID, int[] cardIdx)
	{
		CardGameMessage cardGameMessage = new CardGameMessage(6, -1, cardIdx); //A new Message to be sent.
		this.sendMessage(cardGameMessage);
	}
	/**
	 * Public function to check if the move is valid and then play it.
	 * @see CardGame#checkMove(int, int[])
	 */
	public void checkMove(int playerID, int[] cardIdx)
	{
		boolean isLegal = true;
		if(cardIdx != null)
		{
			CardList played = this.getPlayerList().get(playerID).play(cardIdx); //Cardlist converting the array to cards.
			Hand temphand = composeHand(playerList.get(playerID), played); //Converting the hand to its relevant type.
			if(handsOnTable.isEmpty())
			{
				if(temphand != null && temphand.contains(new Card(0,2)))
				{
					isLegal = true;
				}
				else
				{
					isLegal = false;
				}
			}
			else
			{
				if(handsOnTable.get(handsOnTable.size() - 1).getPlayer() != playerList.get(playerID))
				{
					if(temphand != null)
					{
						isLegal = temphand.beats(handsOnTable.get(handsOnTable.size() - 1));	
					}
					else
					{
						isLegal = false;
					}
				}
				else
				{
					if(temphand != null)
					{
						isLegal = true;
					}
					else
					{
						isLegal = false;
					}
				}
			}
			if(isLegal)
			{
				for(int i = 0; i < played.size(); i++)
				{
					playerList.get(playerID).getCardsInHand().removeCard(played.getCard(i));
				}
				table.printMsg("{"+temphand.getType()+"} " + temphand + "\n");
				handsOnTable.add(temphand);
				currentIdx= (currentIdx+1)%4;
				table.setActivePlayer(currentIdx);
			}
			else
			{
				table.printMsg(played+" <= Not a legal move!!!\n");
			}
		}
		else
		{
			if(!handsOnTable.isEmpty() && handsOnTable.get(handsOnTable.size()-1).getPlayer() != playerList.get(playerID))
			{
				table.printMsg("{Pass}\n");
				currentIdx = (currentIdx+1)%4;
				table.setActivePlayer(currentIdx);
				isLegal = true;
			}
			else
			{
				table.printMsg("{pass} <= Not a legal move!!!\n");
				isLegal = false;
			}
		}
		table.repaint();
		if(endOfGame())
		{
			table.setActivePlayer(-1);
			table.repaint();
			table.disable();
			table.printMsg("\nGame ends.\n");
			JTextArea end = new JTextArea();
			end.setEditable(false);
			end.setFont(new Font("Sans Serif", Font.PLAIN, 12));
			for(int i = 0; i < 4; i++)
			{
				if(playerList.get(i).getCardsInHand().size() == 0)
				{
					end.append("Player "+ i + " wins the game.\n");
				}
				else
				{
					end.append("Player " + i +" has " + playerList.get(i).getCardsInHand().size() + " cards in hand.\n");
				}
			}
			JOptionPane.showMessageDialog(null, end);
			this.sendMessage(new CardGameMessage(4, -1, null));
		}
	}
	/**
	 * Public function to check if a game has ended.
	 * @see CardGame#endOfGame()
	 */
	public boolean endOfGame()
	{
		for(int i = 0; i < this.getNumOfPlayers(); i++)
		{
			if(this.playerList.get(i).getNumOfCards() == 0)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Public function to convert the hand to a hand of its type.
	 * @param player
	 * 		The player who played the hand.
	 * @param cards
	 * 		The set of cards played.
	 * @return
	 * 		Returns a new object created of a particular type.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards)
	{
		Single new0 = new Single(player, cards); //Object of class Single.
		if(new0.isValid())
		{
			return new0;
		}
		Pair new1 = new Pair(player, cards); //Object of class Pair.
		if(new1.isValid())
		{
			return new1;
		}
		Triple new2 = new Triple(player, cards); //Object of class Triple.
		if(new2.isValid())
		{
			return new2;
		}
		StraightFlush new3 = new StraightFlush(player, cards); //Object of class Straight Flush.
		if(new3.isValid())
		{
			return new3;
		}
		Quad new4 = new Quad(player, cards); //Object of class Quad.
		if(new4.isValid())
		{
			return new4;
		}
		FullHouse new5 = new FullHouse(player, cards); //Object of class FullHouse.
		if(new5.isValid())
		{
			return new5;
		}
		Flush new6 = new Flush(player, cards); //Object of class Flush.
		if(new6.isValid())
		{
			return new6;
		}
		Straight new7 = new Straight(player, cards); //Object of class Straight.
		if(new7.isValid())
		{
			return new7;
		}
		return null;
	}
	/**
	 * Public getter function to obtain the value of the private variable playerID
	 * @see NetworkGame#getPlayerID()
	 */
	public int getPlayerID() 
	{
		return this.playerID;
	}
	/**
	 * Public setter function to set the value of the private variable playerID
	 * @see NetworkGame#setPlayerID(int)
	 */
	public void setPlayerID(int playerID) 
	{
		this.playerID = playerID;
	}
	/**
	 * Public getter function to get the value of the private variable playerName.
	 * @see NetworkGame#getPlayerName()
	 */
	public String getPlayerName() 
	{
		return this.playerName;
	}
	/**
	 * Public setter function to set the value of the private variable playerName.
	 * @see NetworkGame#setPlayerName(java.lang.String)
	 */
	public void setPlayerName(String playerName) 
	{
		this.playerName = playerName;		
	}
	/**
	 * Public getter function to get the value of the private variable serverIP.
	 * @see NetworkGame#getServerIP()
	 */
	public String getServerIP() 
	{
		return this.serverIP;
	}
	/**
	 * Public setter function to set the value of the private variable serverIP.
	 * @see NetworkGame#setServerIP(java.lang.String)
	 */
	public void setServerIP(String serverIP) 
	{
		this.serverIP = serverIP;
	}
	/**
	 * Public getter function to get the value of the private variable serverPort.
	 * @see NetworkGame#getServerPort()
	 */
	public int getServerPort() 
	{
		return this.serverPort;
	}
	/**
	 * Public setter function to set the value of the private variable serverPort.
	 * @see NetworkGame#setServerPort(int)
	 */
	public void setServerPort(int serverPort) 
	{
		this.serverPort = serverPort;		
	}
	/**
	 * Public function to make the connection of the client to the server.
	 * @see NetworkGame#makeConnection()
	 */
	public void makeConnection() 
	{
        this.setPlayerName(JOptionPane.showInputDialog("Name:", "Player name"));
		this.setServerIP("127.0.0.1");
		this.setServerPort(2396);
		try {
			sock = new Socket(getServerIP(), getServerPort());
			System.out.println("Connection established.");
			ois = new ObjectInputStream(sock.getInputStream());
			oos = new ObjectOutputStream(sock.getOutputStream());
			Thread myThread = new Thread(new ServerHandler()); //The thread created for multi-threading.
			myThread.start();
			this.sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
			this.sendMessage(new CardGameMessage(4, -1, null));
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	/**
	 * Public function to parse the message this client receives.
	 * @see NetworkGame#parseMessage(GameMessage)
	 */
	public synchronized void parseMessage(GameMessage message) 
	{
		switch(message.getType())
		{
		case 0:
			this.setPlayerID(message.getPlayerID());
			if(message.getData() != null)
			{
				String[] playerNames = (String[])message.getData();
				for(int i = 0; i < 4; i++)
				{
					this.getPlayerList().get(i).setName(playerNames[i]);
				}
			}
			table.setActivePlayer(playerID);
			break;
		case 1:
			if((String)message.getData() != null)
			{
				this.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			}
			break;
		case 2:
			table.printMsg("Game is full.\n");
			break;
		case 3:
			this.getPlayerList().get(message.getPlayerID()).setName("");
			table.disable();
			if(!this.endOfGame())
			{
				table.printMsg((String)message.getData() + " has left the game. \n");
				this.sendMessage(new CardGameMessage(4, -1, null));
				/*try 
				{
					this.oos.close();
					this.ois.close();
					this.sock.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}*/
			}
			break;
		case 4:
			table.printMsg(this.getPlayerList().get(message.getPlayerID()).getName() + " is ready.\n");
			break;
		case 5:
			this.start((BigTwoDeck)message.getData());
			break;
		case 6:
			this.checkMove(message.getPlayerID(), (int[])message.getData());
			table.disable();
			if(this.getPlayerID() == this.getCurrentIdx())
			{
				table.enable();
				table.printMsg("Your turn\n");
			}
			else
			{
				table.printMsg(this.getPlayerList().get(this.currentIdx).getName() + "'s turn.\n");
			}
			break;
		case 7:
			table.chatMsg((String)message.getData());
			break;
		default:
			System.out.println("Invalid message.");
			break;
		}
	}
	/**
	 * Public function to send message to the server.
	 * @see NetworkGame#sendMessage(GameMessage)
	 */
	public void sendMessage(GameMessage message) 
	{
		try 
		{
			oos.writeObject(message);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Inner class to handle functions with the server.
	 * @author shubh31
	 *
	 */
	class ServerHandler implements Runnable
	{
		/**
		 * Standard runnable function.
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			try 
			{
				CardGameMessage inMessage; //Object for CardGameMessage.
				while((inMessage = (CardGameMessage)ois.readObject()) != null)
				{
					parseMessage(inMessage);
				}
				
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * Main function where the running of code begins.
	 * @param args
	 * 		Command line argument.
	 */	
	public static void main(String args[])
	{
		BigTwoClient bigTwo = new BigTwoClient(); //An object of BigTwoClient class.
	}
}
