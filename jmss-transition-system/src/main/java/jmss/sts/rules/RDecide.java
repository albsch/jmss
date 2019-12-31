package jmss.sts.rules;


import jmss.annotations.NotNull;
import jmss.discovery.ModuleDiscovery;
import jmss.discovery.Option;
import jmss.formula.SetOfClauses;
import jmss.rules.Decide;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationCore.Trail;
import jmss.specificationHeuristics.LiteralSelectionStrategy;

public class RDecide implements Decide {

	private final SetOfClauses F;
	private final Trail M;
	private final Solver solver;
	private LiteralSelectionStrategy heuristic;

	public RDecide(@NotNull Solver solver,@NotNull String[] args) throws Exception {
		F = solver.getState().F();
		M = solver.getState().M();
		this.solver = solver;
		heuristic = ModuleDiscovery.getModule("literal-selection", Option.parseModule("literal-selection", args), solver, args);
	}

	@Override
	public boolean guard() {
		// Get unassigned literal count from the formula F
		return F.getFreeVariablesCount() > 0;
	}

	@Override
	public void apply() {
		assert guard();

		// Use the selection strategy to get the decision level
		int ld = heuristic.getLiteral();

		// Add as a decision literal to the trail
		M.addDecisionLiteral(ld);

		// Inform observer on decide application
		for (SolverListener ob : solver.getObs()) {
			ob.onDecide(ld);
		}
	}
}
