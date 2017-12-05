
/**
 * Public class to represent a Triple hand of cards.
 * @author shubh31
 *
 */
public class Triple extends Hand implements abstractHand
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Public constructor to create and initialize a Triple hand by calling the constructor of the superclass.
	 * @param player
	 * 		The player who played the hand.
	 * @param cards
	 * 		The set of cards which comprise the hand.
	 */
	public Triple(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	/**
	 * Public getter function to return the card with the highest suit.
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard()
	{
		if(this.getCard(0).suit > this.getCard(1).suit)
		{
			if(this.getCard(0).suit > this.getCard(2).suit)
			{
				return this.getCard(0);
			}
			else
			{
				return this.getCard(2);
			}
		}
		else
		{
			if(this.getCard(1).suit > this.getCard(2).suit)
			{
				return this.getCard(1);
			}
			else
			{
				return this.getCard(2);
			}
		}
	}
	/**
	 * Public getter function to return the type Triple.
	 * @see Hand#getType()
	 */
	public String getType()
	{
		return "Triple";
	}
	/**
	 * Public function to check if the hand is a valid hand of type Triple and returns boolean.
	 * @see Hand#isValid()
	 */
	public boolean isValid()
	{
		if(this.size() != 3)
		{
			return false;
		}
		if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(0).rank == this.getCard(2).rank)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}