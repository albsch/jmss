package jmss.sts.rules;

import jmss.annotations.NotNull;
import jmss.discovery.ModuleDiscovery;
import jmss.discovery.Option;
import jmss.formula.Clause;
import jmss.rules.Learn;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationHeuristics.LearnStrategy;

public class RLearn implements Learn {
	private final LearnStrategy learnStrategy;
	private final Solver solver;

	public RLearn(@NotNull Solver solver, String[] args) {
		this.solver = solver;
		learnStrategy = ModuleDiscovery.getModule("heuristic-learn", Option.parseModule("heuristic-learn", args), solver, args);
	}

	@Override
	public boolean guard() {
		return learnStrategy.shouldLearn();
	}

	@Override
	public void apply() {
		assert guard();

		Clause cF = learnStrategy.learnClause();

		// Inform observer on learned clause
		for (SolverListener ob : solver.getObs()) {
			ob.onLearn(cF);
		}
	}
}
