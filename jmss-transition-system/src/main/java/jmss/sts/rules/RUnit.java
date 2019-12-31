package jmss.sts.rules;

import jmss.annotations.NotNull;
import jmss.formula.Clause;
import jmss.formula.SetOfClauses;
import jmss.rules.Unit;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationCore.Trail;

public class RUnit implements Unit {
	private final SetOfClauses F;
	private final Trail M;
	private final Solver solver;

	public RUnit(@NotNull Solver solver) {
		F = solver.getState().F();
		M = solver.getState().M();
		this.solver = solver;
	}

	@Override
	public Clause guard() {
		return F.getUnitClause();
	}

	@Override
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
