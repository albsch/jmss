package heuristicRestart;

import formula.Clause;
import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationHeuristics.RestartStrategy;

import java.util.List;

/**
 * Restart heuristic based on MiniSAT restart.
 * <p>
 * After an initial count of conflict, this strategy tells the solver to
 * restart, and multiplying the restart threshold by a given factor.
 * 
 * @author Albert Schimpf
 */
public class RestartConflictCountingGeometric implements RestartStrategy,
		SolverListener {

	public static double NEXTRESTARTCONST = 1.5;
	public static Integer NEXTRESTART = 150;
	/**
	 * Counter to keep track of conflicts.
	 */
	private Integer conflictCount;
	/**
	 * Counter to keep track of when to restart.
	 */
	private float conflictsForNextRestart;
	/**
	 * Factor to multiply the threshold, when it is reached.
	 */
	private float conflictsForNextRestartConst;

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
	public RestartConflictCountingGeometric(@NotNull Solver solver) {
		solver.addObserver(this);
		conflictCount = 0;
		conflictsForNextRestart = 0;
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
	 * On restart reset conflict count, and increase threshold.
	 */
	@Override
	public void onRestart() {
		conflictCount = 0;
		calculateConflictsForNextRestart();
	}

	/**
	 * Calculates the threshold for the first restart based on which geometric
	 * heuristic is given.
	 */
	private void calculateConflictForFirstRestart() {
		conflictsForNextRestartConst = (float) NEXTRESTARTCONST;
		conflictsForNextRestart = NEXTRESTART;
	}

	/**
	 * Multiplies the current threshold with
	 * {@code conflictsForNextRestartConst} factor.
	 */
	private void calculateConflictsForNextRestart() {
		conflictsForNextRestart *= conflictsForNextRestartConst;
	}

	/**
	 * Increase conflict counter on conflict.
	 * @param cl
	 */
	public void onConflict(Clause cl) {
		conflictCount++;
	}

	/**
	 * Not needed in this heuristic.
	 */
	public void onUnitPropagate(List<Integer> cl, Integer l) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	public void onExplain(List<Integer> ante, Integer resLit,
						  List<Integer> resolvente) {
	}

	/**
	 * Not needed in this heuristic.
	 * @param cl
	 */
	public void onLearn(Clause cl) {
	}

	/**
	 * Not needed in this heuristic.
	 * @param cl
	 */
	public void onForget(Clause cl) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	public void onLearnInitial(List<Integer> cl) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	public void onSetOfClausesLoaded() {
	}

	/**
	 * Not needed in this heuristic.
	 */
	public void onBacktrack(Integer l) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	public void onDecide(Integer l) {
	}


	/**
	 * Not needed in this heuristic.
	 */
	public void onBackjump(Integer current, Integer bl, Integer uip) {
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

}
