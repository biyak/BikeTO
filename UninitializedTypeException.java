package group01_finalproject;
/**
 * @author Adele Olejarz, Rumsha Siddiqui, Biya Kazmi, Stephen Lamothe
 * @date March 26, 2017
 * @version 1.0
 * This exception is thrown when a type has not been initialized.
 */
public class UninitializedTypeException extends Exception {
	public UninitializedTypeException() {
	}

	public UninitializedTypeException(String message) {
		super(message);
	}

}
