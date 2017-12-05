
/**
 * An abstract interface which contains the declarations of the abstract functions.
 * @author shubh31
 *
 */
public interface abstractHand 
{
	/**
	 * An abstract function to check if a set of cards is valid.
	 * @return
	 * 		Returns a boolean value indicating validity. true if hand is valid, else false.
	 */
	public abstract boolean isValid();
	/**
	 * An abstract function to return the type of a set of cards.
	 * @return
	 * 		Returns a string containing the type of hand.
	 */
	public abstract String getType();
}
