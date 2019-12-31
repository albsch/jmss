package rules;


import ruleSystem.RuleSystem;
import specificationCore.Solver;
import specificationCore.State;
import specificationCore.Trail;

public class RBacktrack {
	private final Trail M;
	private final State state;

	public RBacktrack(Solver solver, CoreRules ruleSystem) {
		M = solver.getState().M();
		this.state = solver.getState();
	}

	public boolean guard() {
		// If no conflict, backtrack is not applicable
		if (state.C() == null) {
			return false;
		}
		// If decision level == 0, there is no decision literal to backtrack
		if (M.getCurrentDecisionLevel() == 0) {
			return false;
		}

		// Else backtrack is applicable
		return true;
	}

	public void apply() {
		M.backtrack();
		state.setC(null);
	}
}
