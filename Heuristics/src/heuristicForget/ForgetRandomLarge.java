package heuristicForget;

import org.jetbrains.annotations.NotNull;
import formula.Clause;
import formula.SetOfClauses;
import specificationCore.*;
import specificationHeuristics.ForgetStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Implementation of a simple forget heuristic based on learned clause count and
 * random forget chance, only for large clauses.
 * <p>
 * After every 100 clauses learned, this strategy tells the solver to forget
 * each learned clause with a chance of 50%, if the clause is a big clause.
 * 
 * @author Albert Schimpf
 *
 */
public class ForgetRandomLarge implements ForgetStrategy, SolverListener {
	public static Integer THRESHOLD;
	/**
	 * Counter to keep track of learned clauses.
	 */
	Integer learnClauseCount = 0;
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
	public ForgetRandomLarge(@NotNull Solver solver) {
		this.F = solver.getState().F();
		solver.addObserver(this);
		r = new Random();
	}

	/**
	 * Solver should forget some learned big clauses after every 100 clauses
	 * learned.
	 * 
	 * @return {@code true}, if solver should forget some big clauses;
	 *         {@code false} otherwise
	 */
	@Override
	public boolean shouldForget() {
		return learnClauseCount > THRESHOLD;
	}

	/**
	 * Removes every learned big clause with a chance of 50%.
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
			if (c.getLiterals().size() > 20) {
				if (chance <= 0.50f) {
					forgot.add(c);
				}
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
	 * Not needed in this heuristic.
	 */
	public void onRestart() {
	}

	/**
	 * Not needed in this heuristic.
	 * @param cl
	 */
	public void onConflict(Clause cl) {
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
