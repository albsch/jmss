package jmss.heuristics.learn;

import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.formula.Clause;
import jmss.specificationCore.Solver;
import jmss.specificationHeuristics.LearnStrategy;

/**
 * Learn strategy with a default response to never learn new clauses.
 * 
 * @author Albert Schimpf
 *
 */
@Module(module = "heuristic-learn", name = "never")
public class LearnNever implements LearnStrategy {
	public LearnNever(@NotNull Solver solver) {}

	@Override
	public boolean shouldLearn() {
		return false;
	}

	@Override
	public Clause learnClause() { throw new IllegalStateException("Forbidden"); }
}
