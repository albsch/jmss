package jmss.heuristics.learn;


import jmss.annotations.Module;
import jmss.formula.Clause;
import jmss.specificationCore.Solver;
import jmss.specificationCore.State;
import jmss.specificationHeuristics.LearnStrategy;

/**
 * Implementation of a simple learn heuristic.
 * Learns every (resolved) clause after a conflict.
 * 
 * @author Albert Schimpf
 */
@Module(module = "heuristic-learn", name = "all-resolved-conflicts", defaultModule = true)
public class LearnSimple implements LearnStrategy {
	/**
	 * Saved solver reference.
	 */
	private final State state;

	public LearnSimple(Solver solver) {
		this.state = solver.getState();
	}

	@Override
	public boolean shouldLearn() {
		return state.C() != null;

		//proven to be not needed
//		// If clause is already in learned clauses, learn can not be applied
//		if (F.getLearnedClauses().indexOf(state.getC()) != -1) {
//			return false;
//		}
//
//		// If clause is already in initial clauses, learn can not be applied
//		return F.getInitialClauses().indexOf(state.getC()) == -1;

	}

	@Override
	public Clause learnClause() {
		return state.F().addLearnedClause(state.C());
	}
}