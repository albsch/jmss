package jmss.sts.rules;

import jmss.annotations.NotNull;
import jmss.discovery.ModuleDiscovery;
import jmss.discovery.Option;
import jmss.formula.Clause;
import jmss.formula.SetOfClauses;
import jmss.rules.Forget;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationHeuristics.ForgetStrategy;

import java.util.List;

public class RForget implements Forget {
	private final SetOfClauses F;
	private final ForgetStrategy forgetStrategy;
	private final Solver solver;

	public RForget(@NotNull Solver solver, String[] args) {
		F = solver.getState().F();
		this.solver = solver;
		forgetStrategy = ModuleDiscovery.getModule("heuristic-forget", Option.parseModule("heuristic-forget", args), solver, args);
	}

	@Override
	public boolean guard() {
		return F.getLearnedClauses().size() != 0 && forgetStrategy.shouldForget();
	}

	@Override
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
