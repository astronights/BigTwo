
/**
 * This class is used to represent a card of the BigTwo game.
 * @author shubh31
 *
 */
public class BigTwoCard extends Card
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor which creates and returns a Card for the BigTwo game by calling its superclass constructor.
	 * @param suit
	 * 		The suit of the card.
	 * @param rank
	 * 		The rank of the card.
	 */
	public BigTwoCard(int suit, int rank)
	{
		super(suit, rank);
	}
	/**
	 * Public function to compare whether a BigTwoCard beats another BigTwoCard by comparing the rank and the suit.
	 * @see Card#compareTo(Card)
	 */
	public int compareTo(Card card)
	{
		int card1 = this.getRank(); //Variable to store rank of current card.
		int card2 = card.getRank(); //Variable to store rank of checked card.
		if(card1 == 0)
		{
			card1 = 13;
		}
		else if(card1 == 1)
		{
			card1 = 14;
		}
		if(card2 == 0)
		{
			card2 = 13;
		}
		else if(card2 == 1)
		{
			card2 = 14;
		}
		if (card1 > card2) 
		{
			return 1;
		} 
		else if (card1 < card2) 
		{
			return -1;
		} 
		else if (this.suit > card.suit) 
		{
			return 1;
		} 
		else if (this.suit < card.suit) 
		{
			return -1;
		} 
		else 
		{
			return 0;
		}
	}
}
