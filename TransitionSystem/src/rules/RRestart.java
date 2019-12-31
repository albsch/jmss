package rules;

import org.jetbrains.annotations.NotNull;
import ruleSystem.RuleSystem;
import specificationCore.Solver;
import specificationCore.Trail;
import specificationHeuristics.RestartStrategy;

public class RRestart{
	private final Trail M;
	private final RestartStrategy restartStrategy;

	/**
	 * Default constructor which saves the main module reference.
	 *
	 * @param solver
	 *            the main module reference.
	 * @param ruleSystem
	 *            rule system, which uses this rule
	 */
	public RRestart(@NotNull Solver solver, @NotNull CoreRules ruleSystem) {
		M = solver.getState().M();
		restartStrategy = ruleSystem.getRestartStrategy();
	}

	/**
	 * Checks, if the solver should restart according to a restart heuristic.
	 *
	 * @return {@code true}, solver should restart; {@code false} otherwise
	 */
	public boolean guard() {
		return restartStrategy.shouldRestart();
	}

	/**
	 * Resets the trail, restarting the solving process.
	 */
	public void apply() {
		assert guard();

		M.reset();
	}
}
