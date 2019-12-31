package exceptions;

/**
 * Checked exception, which gets thrown, if an error occurs during internal file conversion from a DIMACS file.
 */
public class ConvertException extends Exception{
	
	public ConvertException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
}
