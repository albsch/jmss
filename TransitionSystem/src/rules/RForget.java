package rules;

import org.jetbrains.annotations.NotNull;
import formula.Clause;
import formula.SetOfClauses;
import ruleSystem.RuleSystem;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationHeuristics.ForgetStrategy;

import java.util.ArrayList;
import java.util.List;

public class RForget {
	private final SetOfClauses F;
	private final ForgetStrategy forgetStrategy;
	private final Solver solver;

	public RForget(@NotNull Solver solver, CoreRules ruleSystem) {
		F = solver.getState().F();
		this.solver = solver;
		forgetStrategy = ruleSystem.getForgetStrategy();
	}


	public boolean guard() {
		return F.getLearnedClauses().size() != 0 && forgetStrategy.shouldForget();
	}

	public void apply() {
		assert guard();

		List<Clause> cF = forgetStrategy.forgetClauses();

		// Inform observer on forgotten clauses
		for (Clause c : cF) {
			for (SolverListener ob : solver.getObs()) {
				ob.onForget(c);
			}
		}
	}
}
