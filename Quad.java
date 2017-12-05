
/**
 * Public class to represent a Quad hand of cards.
 * @author shubh31
 *
 */
public class Quad extends Hand implements abstractHand
{
private static final long serialVersionUID = 1L;
	
	/**
	 * Public constructor to create and initialize a Quad hand by calling the constructor of the superclass.
	 * @param player
	 * 		The player who played the hand.
	 * @param cards
	 * 		The set of cards which comprise the hand.
	 */
	public Quad(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	/**
	 * Public getter function to return the card with the highest suit from the four cards having the same rank.
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard()
	{
		this.sort();
		if(this.getCard(0).rank == this.getCard(1).rank)
		{
			return this.getCard(3);
		}
		else
		{
			return this.getCard(4);
		}
	}
	/**
	 * Public getter function to return the type Quad.
	 * @see Hand#getType()
	 */
	public String getType()
	{
		return "Quad";
	}
	/**
	 * Public function to check if the hand is a valid hand of type Quad and returns boolean.
	 * @see Hand#isValid()
	 */
	public boolean isValid()
	{
		if(this.size() != 5)
		{
			return false;
		}
		this.sort();
		if(this.getCard(0).rank == this.getCard(1).rank)
		{
			if(this.getCard(0).rank != this.getCard(1).rank || this.getCard(0).rank != this.getCard(2).rank || this.getCard(0).rank != this.getCard(3).rank)
			{
				return false;
			}
		}
		else
		{
			if(this.getCard(1).rank != this.getCard(2).rank || this.getCard(1).rank != this.getCard(3).rank || this.getCard(1).rank != this.getCard(4).rank)
			{
				return false;
			}
		}
		return true;
	}
}
