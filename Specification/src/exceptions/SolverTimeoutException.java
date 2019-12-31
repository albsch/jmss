package exceptions;

/**
 * Unchecked exception, which will be thrown, if a given time threshold is reached.
 */
public class SolverTimeoutException extends RuntimeException{
	
	public SolverTimeoutException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
}
