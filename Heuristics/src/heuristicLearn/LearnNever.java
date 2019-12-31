package heuristicLearn;

import formula.Clause;
import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;
import specificationHeuristics.LearnStrategy;

/**
 * Learn strategy with a default response to never learn new clauses.
 * 
 * @author Albert Schimpf
 *
 */
public class LearnNever implements LearnStrategy {
	public LearnNever(@NotNull Solver solver) {
	}

	@Override
	public boolean shouldLearn() {
		return false;
	}

	@Override
	public Clause learnClause() {
		throw new NullPointerException("Function call forbidden.");
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
