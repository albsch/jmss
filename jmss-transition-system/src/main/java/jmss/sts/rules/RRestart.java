package jmss.sts.rules;

import jmss.annotations.NotNull;
import jmss.discovery.ModuleDiscovery;
import jmss.discovery.Option;
import jmss.rules.Restart;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationCore.Trail;
import jmss.specificationHeuristics.RestartStrategy;

public class RRestart implements Restart {
	private final Trail M;
	private final Solver S;
	private final RestartStrategy restartStrategy;

	/**
	 * Default constructor which saves the main module reference.
	 */
	public RRestart(@NotNull Solver solver, String[] args) {
		M = solver.getState().M();
		S = solver;
		restartStrategy = ModuleDiscovery.getModule("heuristic-restart", Option.parseModule("heuristic-restart", args), solver, args);
	}

	/**
	 * Checks, if the solver should restart according to a restart heuristic.
	 *
	 * @return {@code true}, solver should restart; {@code false} otherwise
	 */
	@Override
	public boolean guard() {
		return restartStrategy.shouldRestart();
	}

	/**
	 * Resets the trail, restarting the solving process.
	 */
	@Override
	public void apply() {
		assert guard();

		M.reset();

		S.getObs().forEach(SolverListener::onRestart);
	}
}
