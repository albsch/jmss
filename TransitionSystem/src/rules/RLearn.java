package rules;

import formula.Clause;
import org.jetbrains.annotations.NotNull;
import ruleSystem.RuleSystem;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationHeuristics.LearnStrategy;

import java.util.List;

public class RLearn {
	private final LearnStrategy learnStrategy;
	private final Solver solver;

	public RLearn(@NotNull Solver solver, CoreRules ruleSystem) {
		learnStrategy = ruleSystem.getLearnStrategy();
		this.solver = solver;
	}

	public boolean guard() {
		return learnStrategy.shouldLearn();
	}

	public void apply() {
		assert guard();

		Clause cF = learnStrategy.learnClause();

		// Inform observer on learned clause
		for (SolverListener ob : solver.getObs()) {
			ob.onLearn(cF);
		}
	}
}
