package jmss.heuristics.restart;

import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.formula.Clause;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationHeuristics.RestartStrategy;

/**
 * Restart heuristic based on a fixed restart poInteger after a certain conflict
 * threshold has been reached.
 * <p>
 * Options:
 * <ul>
 * <li>0: Berkmin (550 conflicts)
 * <li>1: Chaff (700 conflicts)
 * <li>2: Eureka (2000 conflicts)
 * <li>3: Siege (16000 conflicts)
 * </ul>
 * <p>
 * Aggressive restarts should only be used in conjunction with clause learning.
 * 
 * @author Albert Schimpf
 */
@Module(module = "heuristic-restart", name = "conflicts-fixed")
public class RestartConflictCountingFixed implements RestartStrategy, SolverListener {
	private static final Integer NEXTRESTART = 500;

	/**
	 * Counter to keep track of conflicts.
	 */
	private Integer conflictCount;
	/**
	 * Counter to keep track of when to restart.
	 */
	private Integer conflictsForNextRestart;
	/**
	 * Factor to increase the threshold, when it is reached.
	 */
	private Integer conflictsForNextRestartConst;

	/**
	 * Default constructor which saves the main module reference.
	 * <p>
	 * Adds itself as a observer to respond to certain rules.
	 * <p>
	 * Calculates the number of conflicts for the first restart.
	 */
	public RestartConflictCountingFixed(@NotNull Solver solver) {
		solver.addObserver(this);
		conflictCount = 0;
		conflictsForNextRestart = 0;
		conflictsForNextRestartConst = 0;
		calculateConflictForFirstRestart();
	}

	/**
	 * If conflicts reach the conflict threshold, tell the solver to restart.
	 * 
	 * @return {@code true}, if threshold is reached; {@code false} otherwise
	 */
	@Override
	public boolean shouldRestart() {
		return conflictCount >= conflictsForNextRestart;
	}

	/**
	 * On restart reset conflict count, and increase threshold by given
	 * constant.
	 */
	@Override
	public void onRestart() {
		conflictCount = 0;
		calculateConflictsForNextRestart();
	}

	/**
	 * Calculates the threshold for the first restart based on which fixed
	 * heuristic is given.
	 */
	private void calculateConflictForFirstRestart() {
		conflictsForNextRestartConst = 0;
		// Berkmin: 550 // Zchaff: 700
		// Eureka: 2000 // Siege: 20000
		conflictsForNextRestart = NEXTRESTART;
	}

	/**
	 * Increases the current threshold by {@code conflictsForNextRestartConst} constant.
	 */
	private void calculateConflictsForNextRestart() {
		conflictsForNextRestart += conflictsForNextRestartConst;
	}

	/** Increase conflict counter on conflict */
	@Override
	public void onConflict(Clause cl) {
		conflictCount++;
	}
	
}
