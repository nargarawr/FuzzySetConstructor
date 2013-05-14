/**
 * InvalidFormatException Class Used to throw custom exception
 * 
 * @author Craig Knott
 */

@SuppressWarnings("serial")
public class InvalidFormatException extends Exception {

	/**
	 * Custom exception class used to throw an exception when a file being read
	 * in is of an incorrect format
	 */

	public InvalidFormatException(String message) {
		/**
		 * Construct for the Exception
		 * 
		 * @param message
		 *            cause of the exception
		 */
		super(message);
	}

}
