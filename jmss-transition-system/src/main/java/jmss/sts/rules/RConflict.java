package jmss.sts.rules;

import jmss.annotations.NotNull;
import jmss.formula.Clause;
import jmss.formula.SetOfClauses;
import jmss.rules.Conflict;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationCore.State;

/**
 * Implementation of the conflict rule.
 *
 * @author Albert Schimpf
 */
public class RConflict implements Conflict {
	private final SetOfClauses F;
	private final State state;
	private final Solver solver;

	/**
	 * Default constructor which saves the main module reference.
	 *  @param solver the main module reference.
	 */
	public RConflict(@NotNull Solver solver) {
		F = solver.getState().F();
		this.state = solver.getState();
		this.solver = solver;
	}

	@Override
	public boolean guard() {
		// If a conflict was already found, conflict can not be applied anymore
		if (state.C() != null) {
			return false;
		}

		return F.foundConflict();
	}

	@Override
	public void apply() {
		// Save reference for other rules
		Clause conflictClause = F.getConflictingClause();
		state.setC(conflictClause.getLiterals());


		// Inform observer on conflict clause
		for (SolverListener ob : solver.getObs()) {
			ob.onConflict(conflictClause);
		}
	}
}
