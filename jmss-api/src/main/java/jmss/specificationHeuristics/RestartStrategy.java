package jmss.specificationHeuristics;

/**
 * Specification for all restart strategies.
 * <p>
 * A restart strategy has to implement a check, if the solver should restart at
 * some point in the process.
 * <p>
 * Restart heuristics may need to get information from the jmss.formula {@code F} or
 * the trail {@code M}.
 * <p>
 * If the heuristic needs to be updated after certain rule application, it needs
 * to add itself as a observer with the addObserver method in its
 * constructor.
 * 
 * @author Albert Schimpf
 */
public interface RestartStrategy {
	/**
	 * Checks, if the solver should restart according to a restart heuristic.
	 * 
	 * @return {@code true}, solver should restart; {@code false} otherwise
	 */
	boolean shouldRestart();
}
