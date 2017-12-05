import java.util.Arrays;

/**
 * Public class to represent a StraightFlush hand of cards.
 * @author shubh31
 *
 */
public class StraightFlush extends Hand implements abstractHand
{
private static final long serialVersionUID = 1L;
	
	/**
	 * Public constructor to create and initialize a StraightFlush hand by calling the constructor of the superclass.
	 * @param player
	 * 		The player who played the hand.
	 * @param cards
	 * 		The set of cards which comprise the hand.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	/**
	 * Public getter function to return the card with the highest rank.
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard()
	{
		int[] trank = {this.getCard(0).rank, this.getCard(1).rank, this.getCard(2).rank, this.getCard(3).rank, this.getCard(4).rank};
		for(int i = 0; i < 5; i++)
		{
			if(trank[i] < 2)
			{
				trank[i] += 13;
			}
		}
		Arrays.sort(trank);
		int in = 0;
		for(int i = 0; i < 5; i++)
		{
			if(trank[4] > 12)
			{
				trank[4] -= 13;
			}
			if(this.getCard(i).rank == trank[4])
			{
				in = i;
			}
		}
		return this.getCard(in);
	}
	/**
	 * Public getter function to return the type StraightFlush.
	 * @see Hand#getType()
	 */
	public String getType()
	{
		return "Straight Flush";
	}
	/**
	 * Public function to check if the hand is a valid hand of type StraightFlush and returns boolean.
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
		int[] tempranks = {this.getCard(0).rank, this.getCard(1).rank, this.getCard(2).rank, this.getCard(3).rank, this.getCard(4).rank};
		for(int i = 0; i < 5; i++)
		{
			if(tempranks[i] < 2)
			{
				tempranks[i] += 13;
			}
		}
		Arrays.sort(tempranks);
		for(int i = 0; i < 4; i++)
		{
			if(tempranks[i] != tempranks[i+1] - 1)
			{
				return false;
			}
		}
		return true;
	}
}
