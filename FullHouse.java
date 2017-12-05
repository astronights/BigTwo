
/**
 * Class to represent a Hand of the type FullHouse.
 * @author shubh31
 *
 */
public class FullHouse extends Hand implements abstractHand
{
private static final long serialVersionUID = 1L;
	
	/**
	 * Public constructor to create and initialize a FullHouse hand by calling the constructor of the superclass.
	 * @param player
	 * 		The player who played the hand.
	 * @param cards
	 * 		The set of cards which comprise the hand.
	 */
	public FullHouse(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	/**
	 * Public getter function to return the card with the highest rank of the Triple in the FullHouse.
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard()
	{
		this.sort();
		if(this.getCard(2).rank == this.getCard(0).rank)
		{
			return this.getCard(2);
		}
		else
		{
			return this.getCard(4);
		}
	}
	/**
	 * Public getter function to return the type FullHouse.
	 * @see Hand#getType()
	 */
	public String getType()
	{
		return "Full House";
	}
	/**
	 * Public function to check whether the hand is of type FullHouse and returns boolean.
	 * @see Hand#isValid()
	 */
	public boolean isValid()
	{
		if(this.size() != 5)
		{
			return false;
		}
		this.sort();
		if(this.getCard(2).rank == this.getCard(0).rank)
		{
			if(this.getCard(0).rank != this.getCard(1).rank || this.getCard(0).rank != this.getCard(2).rank || this.getCard(3).rank != this.getCard(4).rank)
			{
				return false;
			}
		}
		else
		{
			if(this.getCard(0).rank != this.getCard(1).rank || this.getCard(2).rank != this.getCard(4).rank || this.getCard(3).rank != this.getCard(4).rank)
			{
				return false;
			}
		}
		return true;
	}
}
