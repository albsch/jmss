package rules;

import org.jetbrains.annotations.NotNull;
import formula.Clause;
import formula.SetOfClauses;
import ruleSystem.RuleSystem;
import specificationCore.*;

import static java.lang.Math.abs;

public class RUnit {
	private final SetOfClauses F;
	private final Trail M;
	private final Solver solver;

	public RUnit(@NotNull Solver solver, CoreRules ruleSystem) {
		F = solver.getState().F();
		M = solver.getState().M();
		this.solver = solver;
	}

	public Clause guard() {
		return F.getUnitClause();
	}

	public void apply(Clause cl) {
		// Get the unit literal from data structure
		Integer lu = cl.getUnitLiteral();
		// add unit literal with antecedent to trail
		M.addUnitLiteral(lu, cl.getLiterals());

		assert lu != null : "Returned null unit literal";

		// Inform observer on unit application
		for (SolverListener ob : solver.getObs()) {
			ob.onUnitPropagate(cl.getLiterals(), lu);
		}
	}
}
