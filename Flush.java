
/**
 * Class to represent a Hand of the type Flush
 * @author shubh31
 *
 */
public class Flush extends Hand implements abstractHand
{
private static final long serialVersionUID = 1L;
	
	/**
	 * Public constructor to create and initialize a Flush hand by calling the constructor of the superclass.
	 * @param player
	 * 		The player who played the hand.
	 * @param cards
	 * 		The set of cards which comprise the hand.
	 */
	public Flush(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	/**
	 * Public function to return the card with the highest rank.
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard()
	{
		this.sort();
		if(this.contains(new Card(this.getCard(0).suit, 1)))
		{
			return new Card(this.getCard(0).suit, 1);
		}
		else if(this.contains(new Card(this.getCard(0).suit, 0)))
		{
			return new Card(this.getCard(0).suit, 0);
		}
		else
		{
			return this.getCard(4);
		}
	}
	/**
	 * Public getter function to return a string Flush.
	 * @see Hand#getType()
	 */
	public String getType()
	{
		return "Flush";
	}
	/**
	 * Public function to check if the hand is a valid hand of type Flush and returns boolean.
	 * @see Hand#isValid()
	 */
	public boolean isValid()
	{
		if(this.size() != 5)
		{
			return false;
		}
		for(int i = 0; i < 4; i++)
		{
			if(this.getCard(i).suit != this.getCard(i+1).suit)
			{
				return false;
			}
		}
		return true;
	}
}
