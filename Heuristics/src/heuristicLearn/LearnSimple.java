package heuristicLearn;


import formula.Clause;
import formula.SetOfClauses;
import specificationCore.Solver;
import specificationCore.State;
import specificationHeuristics.LearnStrategy;

/**
 * Implementation of a simple learn heuristic. Learns every (resolved) clause
 * after a conflict.
 * 
 * @author Albert Schimpf
 */
public class LearnSimple implements LearnStrategy {	/**
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
		Clause c = state.F().addLearnedClause(state.C());

		return c;
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