package jmss.sts.rules;


import jmss.rules.Backtrack;
import jmss.specificationCore.Solver;
import jmss.specificationCore.State;
import jmss.specificationCore.Trail;

public class RBacktrack implements Backtrack {
	private final Trail M;
	private final State state;

	public RBacktrack(Solver solver) {
		M = solver.getState().M();
		this.state = solver.getState();
	}

	@Override
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

	@Override
	public void apply() {
		M.backtrack();
		state.setC(null);
	}
}
