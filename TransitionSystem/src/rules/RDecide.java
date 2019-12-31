package rules;

import org.jetbrains.annotations.NotNull;
import formula.SetOfClauses;
import ruleSystem.RuleSystem;
import specificationCore.*;
import specificationHeuristics.LiteralSelectionStrategy;

import static java.lang.Math.abs;



public class RDecide {
	private final SetOfClauses F;
	private final Trail M;
	private final Solver solver;
	private LiteralSelectionStrategy heuristic;

	public RDecide(@NotNull Solver solver, CoreRules ruleSystem) {
		F = solver.getState().F();
		M = solver.getState().M();
		this.solver = solver;
		heuristic = ruleSystem.getLiteralSelectionStrategy();
	}

	public boolean guard() {
		// Get unassigned literal count from the formula F
		return F.getFreeVariablesCount() > 0;
	}

	public void apply() {
		assert guard();

		// Use the selection strategy to get the decision level
		int ld =heuristic.getLiteral();

		// Add as a decision literal to the trail
		M.addDecisionLiteral(ld);

		// Inform observer on decide application
		for (SolverListener ob : solver.getObs()) {
			ob.onDecide(ld);
		}
		// Return decision literal
//		return ld;
	}
}
