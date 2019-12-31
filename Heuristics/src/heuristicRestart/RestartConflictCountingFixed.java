package heuristicRestart;

import formula.Clause;
import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationHeuristics.RestartStrategy;

import java.util.List;

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
public class RestartConflictCountingFixed implements RestartStrategy,
		SolverListener {
	//can be set by param class when parsing program arguments
	public static Integer NEXTRESTART = 500;

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
	 * 
	 * @param solver
	 *            the main module reference.
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
	 * Increases the current threshold by {@code conflictsForNextRestartConst}
	 * constant.
	 */
	private void calculateConflictsForNextRestart() {
		conflictsForNextRestart += conflictsForNextRestartConst;
	}

	/**
	 * Increase conflict counter on conflict
	 * @param cl
	 */
	@Override
	public void onConflict(Clause cl) {
		conflictCount++;
	}
	
	/**
	 * Returns the name of the strategy without the hash-code.
	 * 
	 * @return name of strategy
	 */
	@Override
	public String toString() {
		return getClass().getName();
	}

	/**
	 * Response not needed in this heuristic
	 */
	public void onUnitPropagate(List<Integer> cl, Integer l) {
	}

	/**
	 * Response not needed in this heuristic
	 */
	public void onExplain(List<Integer> ante, Integer resLit,
						  List<Integer> resolvente) {
	}

	/**
	 * Response not needed in this heuristic
	 * @param cl
	 */
	public void onLearn(Clause cl) {
	}

	/**
	 * Response not needed in this heuristic
	 * @param cl
	 */
	public void onForget(Clause cl) {
	}

	/**
	 * Response not needed in this heuristic
	 */
	public void onLearnInitial(List<Integer> cl) {
	}

	/**
	 * Response not needed in this heuristic
	 */
	public void onSetOfClausesLoaded() {
	}

	/**
	 * Response not needed in this heuristic
	 */
	public void onBacktrack(Integer l) {
	}

	/**
	 * Response not needed in this heuristic
	 */
	public void onDecide(Integer l) {
	}

	/**
	 * Response not needed in this heuristic
	 */
	public void onBackjump(Integer current, Integer bl, Integer uip) {
	}
}
