package jmss.exceptions;

/**
 * Checked exception which is thrown if an error occurs during internal file conversion from a DIMACS file.
 */
public class ConvertException extends Exception{
	public ConvertException(String string) { super(string); }
}
