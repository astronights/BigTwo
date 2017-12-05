//import java.util.ArrayList;

/**
 * A class to represent an object which is a deck containing BigTwo cards.
 * @author shubh31
 *
 */
public class BigTwoDeck extends Deck
{
	private static final long serialVersionUID = -1L;
	/**
	 * Public function to initialize the deck.
	 * @see Deck#initialize()
	 */
	public void initialize()
	{
		removeAllCards();
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 13; j++) 
			{
				BigTwoCard bigTwoCard = new BigTwoCard(i, j); //Creating an card of BigTwo type.
				if(bigTwoCard != null)
				{
				addCard(bigTwoCard);
				}
			}
		}
	}
}
