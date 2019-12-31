package heuristicRestart;

import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;
import specificationHeuristics.RestartStrategy;

/**
 * Restart strategy with a default response to never restart.
 * 
 * @author Albert Schimpf
 *
 */
public class RestartNever implements RestartStrategy {
	public RestartNever(@NotNull Solver solver) {
	}

	@Override
	public boolean shouldRestart() {
		return false;
	}

	/**
	 * Returns the name of the strategy without the hash-code.
	 * 
	 * @return name of strategy
	 */
	@Override
	public String toString() {
		return getClass().getName();
	}

}
