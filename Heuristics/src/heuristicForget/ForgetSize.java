package heuristicForget;

import formula.Clause;
import formula.SetOfClauses;
import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationHeuristics.ForgetStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForgetSize implements ForgetStrategy, SolverListener {
	public static int PERCENT = 100;

	private int learnClauseCount;

	/**
	 * Random seed.
	 */
	private final Random r;

	/**
	 * Saved formula reference.
	 */
	private final SetOfClauses F;

	/**
	 * Default constructor which saves the main module reference.
	 * <p>
	 * Adds itself as a observer to respond to learned clauses.
	 *
	 * @param solver
	 *            the main module reference.
	 */
	public ForgetSize(@NotNull Solver solver) {
		this.F = solver.getState().F();
		solver.addObserver(this);
		learnClauseCount = 0;
		r = new Random();
	}

	/**
	 * Solver should forget some learned clauses after every 200 clauses
	 * learned.
	 * 
	 * @return {@code true}, if solver should forget some clauses; {@code false}
	 *         otherwise
	 */
	@Override
	public boolean shouldForget() {
		return learnClauseCount > ((PERCENT/100.)*(F.getInitialClauses().size()));
	}

	/**
	 * Removes every learned clause with a chance of 25%.
	 * 
	 * @return removed clauses
	 */
	@Override
	public ArrayList<Clause> forgetClauses() {
		learnClauseCount = 0;
		
		List<Clause> cl = F.getLearnedClauses();
		ArrayList<Clause> forgot = new ArrayList<>();
		for (Clause c : cl) {
			float chance = r.nextFloat();
			if (chance <= 0.33f) {
				forgot.add(c);
			}
		}
		forgot.forEach(F::forgetLearnedClause);

		return forgot;
	}

	/**
	 * On clause learn increase counter.
	 * @param cl
	 */
	@Override
	public void onLearn(Clause cl) {
		learnClauseCount++;
	}
	
	/**
	 * Restart response not needed in this heuristic.
	 */
	public void onRestart() {
	}

	/**
	 * Conflict response not needed in this heuristic.
	 * @param cl
	 */
	public void onConflict(Clause cl) {
	}

	/**
	 * Unit response not needed in this heuristic.
	 */
	public void onUnitPropagate(List<Integer> cl, Integer l) {
	}

	/**
	 * Explain response not needed in this heuristic.
	 */
	public void onExplain(List<Integer> ante, Integer resLit,
						  List<Integer> resolvente) {
	}

	/**
	 * Forget response not needed in this heuristic.
	 * @param cl
	 */
	public void onForget(Clause cl) {
	}

	/**
	 * Initial learn response not needed in this heuristic.
	 */
	public void onLearnInitial(List<Integer> cl) {
	}

	/**
	 * Formula loaded response not needed in this heuristic.
	 */
	public void onSetOfClausesLoaded() {
	}

	/**
	 * Backtrack response not needed in this heuristic.
	 */
	public void onBacktrack(Integer l) {
	}

	/**
	 * Decide response not needed in this heuristic.
	 */
	public void onDecide(Integer l) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	public void onBackjump(Integer current, Integer bl, Integer uip) {}

}
