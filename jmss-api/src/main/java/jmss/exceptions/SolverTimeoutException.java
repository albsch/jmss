package jmss.exceptions;

/**
 * Unchecked exception which will be thrown if a given time threshold is reached.
 */
public class SolverTimeoutException extends RuntimeException{
	public SolverTimeoutException(String string) { super(string); }
}
