
/**
 * Public class to represent a Single type hand of cards.
 * @author shubh31
 *
 */
public class Single extends Hand implements abstractHand
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Public constructor to create and initialize a Single hand by calling the constructor of the superclass.
	 * @param player
	 * 		The player who played the hand.
	 * @param cards
	 * 		The set of cards which comprise the hand.
	 */
	public Single(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	/**
	 * Public getter function to return the only card.
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard()
	{
		return this.getCard(0);
	}
	/**
	 * Public getter function to return the type Quad.
	 * @see Hand#getType()
	 */
	public String getType()
	{
		return "Single";
	}
	/**
	 * Public function to check if the hand is a valid hand of type Single and returns boolean.
	 * @see Hand#isValid()
	 */
	public boolean isValid()
	{
		if(this.size() == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
