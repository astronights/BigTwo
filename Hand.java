/**
 * This class is used to represent a Hand played by a player which includes a list of cards.
 * 
 * @author shubh31
 *
 */
public class Hand extends CardList implements abstractHand
{
	private static final long serialVersionUID = 0;
	private CardGamePlayer player; //Object for player of hand.
	
	/**
	 * Constructor which creates and returns an object of type Hand 
	 * @param player
	 * 		The player who has played the hand
	 * @param cards
	 * 		The list of cards which get added to the hand
	 */
	public Hand(CardGamePlayer player, CardList cards)
	{
		this.player = player;
		for(int  i = 0; i < cards.size(); i++)
		{
			this.addCard(cards.getCard(i));
		}
	}
	/**
	 * Public getter function to obtain the player of a hand.
	 * @return
	 * 		Return the private variable player.
	 */
	public CardGamePlayer getPlayer()
	{
		return this.player;
	}
	/**
	 * Public function to obtain the top card of a hand. Is overridden in subclasses.
	 * @return
	 * 		Returns the first card of the hand.
	 */
	public Card getTopCard()
	{
		return this.getCard(0);
	}
	/**
	 * Function to check if the current hand beats another hand.
	 * @param hand
	 * 		The hand representing the last hand on table.
	 * @return
	 * 		Returns a boolean value true if the last hand is beaten, else false.
	 */
	public boolean beats(Hand hand)
	{
		if(this.size() < 4 && this.isValid())
		{
			if(this.size() == hand.size() && this.getTopCard().compareTo(hand.getTopCard()) == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(this.isValid())
		{
			//Straight Flush
			if(this instanceof StraightFlush)
			{
				if(this.size() == hand.size())
				{
					if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) == 1 )
					{
						return true;
					}
					else if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1 )
					{
						return false;
					}
					else if(this.getType() != hand.getType())
					{
						return true;
					}
				}
				else
				{
					return false;
				}
			}
			//Quad
			else if(this instanceof Quad)
			{
				if(this.size() == hand.size())
				{
					if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) == 1 )
					{
						return true;
					}
					else if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1 )
					{
						return false;
					}
					else if(this.getType() != hand.getType())
					{
						if(hand.getType() != "Straight Flush")
						{
							return true;
						}
						else
						{
							return false;
						}
					}
				}
				else
				{
					return false;
				}
			}
			//Full House
			else if(this instanceof FullHouse)
			{
				if(this.size() == hand.size())
				{
					if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) == 1 )
					{
						return true;
					}
					else if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1 )
					{
						return false;
					}
					else if(this.getType() != hand.getType())
					{
						if(hand.getType() != "Straight Flush"  && hand.getType() != "Quad")
						{
							return true;
						}
						else
						{
							return false;
						}
					}
				}
				else
				{
					return false;
				}
			}
			//Flush
			else if(this instanceof Flush)
			{
				if(this.size() == hand.size())
				{
					if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) == 1 )
					{
						return true;
					}
					else if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1 )
					{
						return false;
					}
					else if(this.getType() != hand.getType())
					{
						if(hand.getType() == "Straight")
						{
							return true;
						}
						else
						{
							return false;
						}
					}
				}
				else
				{
					return false;
				}
			}
			//Straight
			else if(this instanceof Straight)
			{
				if(this.size() == hand.size())
				{
					if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) == 1 )
					{
						return true;
					}
					else if(this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1 )
					{
						return false;
					}
					else if(this.getType() != hand.getType())
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}
	/**
	 * Abstract method to check if a hand is a valid hand.
	 * @see abstractHand#isValid()
	 */
	//@Override
	public boolean isValid() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Abstract method to return the type of the hand.
	 * @see abstractHand#getType()
	 */
	//@Override
	public String getType() 
	{ 
		// TODO Auto-generated method stub
		return "";
	}
}
