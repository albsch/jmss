package rules;

import org.jetbrains.annotations.NotNull;
import formula.Clause;
import formula.SetOfClauses;
import ruleSystem.RuleSystem;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationCore.State;

/**
 * Implementation of the conflict rule.
 * <p>
 * For detailed specifications see
 * {@link CoreRules#conflictGuard conflict guard} and
 * {@link CoreRules#applyConflict apply conflict}.
 *
 * @author Albert Schimpf
 * @see Rule
 */
public class RConflict{
	private final SetOfClauses F;
	private final State state;
	private final Solver solver;

	/**
	 * Default constructor which saves the main module reference.
	 *  @param solver
	 *            the main module reference.
	 * @param ruleSystem
	 */
	public RConflict(@NotNull Solver solver, CoreRules ruleSystem) {
		F = solver.getState().F();
		this.state = solver.getState();
		this.solver = solver;
	}

	public boolean guard() {
		// If a conflict was already found, conflict can not be applied anymore
		if (state.C() != null) {
			return false;
		}

		return F.foundConflict();
	}

	public void apply() {
		//noinspection AssertWithSideEffects

		// Save reference for other rules
		Clause conflictClause = F.getConflictingClause();
		state.setC(conflictClause.getLiterals());


		// Inform observer on conflict clause
		for (SolverListener ob : solver.getObs()) {
			ob.onConflict(conflictClause);
		}
	}


}
