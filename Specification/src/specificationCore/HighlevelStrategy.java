package specificationCore;

import specificationHeuristics.ForgetStrategy;
import specificationHeuristics.LearnStrategy;
import specificationHeuristics.LiteralSelectionStrategy;
import specificationHeuristics.RestartStrategy;

/**
 * Specifications for a high-level strategy.
 * <p>
 * Every strategy has to save the reference to the main module, as they operate
 * on the formula {@code F} and the trail {@code M}.
 * 
 * @author Albert Schimpf
 */
public interface HighlevelStrategy {
	/**
	 * The abstract function which needs to be implemented by an actual
	 * strategy.
	 * 
	 * @return true if formula {@code F} is satisfied, false if not.
	 */
	boolean applyStrategy();

	/**
	 * Returns the currently used forget heuristic.
	 * 
	 * @return forget heuristic
	 */
	ForgetStrategy getForgetStrategy();

	/**
	 * Returns the currently used learn heuristic.
	 * 
	 * @return learn heuristic
	 */
	LearnStrategy getLearnStrategy();

	/**
	 * Returns the currently used decide heuristic.
	 * 
	 * @return decide heuristic
	 */
	LiteralSelectionStrategy getLiteralSelectionStrategy();

	/**
	 * Returns the currently used restart heuristic.
	 * 
	 * @return restart heuristic
	 */
	RestartStrategy getRestartStrategy();
}
