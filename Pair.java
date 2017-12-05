
/**
 * Public class to represent a Pair of cards with the same rank.
 * @author shubh31
 *
 */
public class Pair extends Hand implements abstractHand
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Public constructor to create and initialize a Pair hand by calling the constructor of the superclass.
	 * @param player
	 * 		The player who played the hand.
	 * @param cards
	 * 		The set of cards which comprise the hand.
	 */
	public Pair(CardGamePlayer player, CardList cards)
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
			return this.getCard(0);
		}
		else
		{
			return this.getCard(1);
		}
	}
	/**
	 * Public getter function to return the type Pair.
	 * @see Hand#getType()
	 */
	public String getType()
	{
		return "Pair";
	}
	/**
	 * Public function to check if the hand is a valid hand of type Pair and returns boolean.
	 * @see Hand#isValid()
	 */
	public boolean isValid()
	{
		if(this.size() != 2)
		{
			return false;
		}
		if(this.getCard(0).rank == this.getCard(1).rank)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}