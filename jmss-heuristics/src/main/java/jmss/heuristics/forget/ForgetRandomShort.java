package jmss.heuristics.forget;

import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.formula.Clause;
import jmss.formula.SetOfClauses;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationHeuristics.ForgetStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of a simple forget heuristic based on learned clause count and
 * random forget chance, only for large clauses.
 * <p>
 * After every 100 clauses learned, this strategy tells the solver to forget
 * each learned clause with a chance of 50%, if the clause is a small clause.
 * 
 * @author Albert Schimpf
 *
 */
@Module(module = "heuristic-forget", name = "forget-short")
public class ForgetRandomShort implements ForgetStrategy, SolverListener {
	public static Integer THRESHOLD = 100;
	/**
	 * Counter to keep track of learned clauses.
	 */
	private int learnClauseCount = 0;
	/**
	 * Random seed.
	 */
	private final Random r;
	/**
	 * Saved formula reference;
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
	public ForgetRandomShort(@NotNull Solver solver) {
		this.F = solver.getState().F();
		solver.addObserver(this);
		r = new Random();
	}

	/**
	 * Solver should forget some learned small clauses after every 100 clauses
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
	 * Removes every learned small clause with a chance of 50%.
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
			if (c.getLiterals().size() <= 20) {
				if (chance <= 0.50f) {
					forgot.add(c);
				}
			}
		}
		forgot.forEach(F::forgetLearnedClause);
		
		return forgot;
	}

	/** On clause learn increase counter. */
	@Override
	public void onLearn(Clause cl) {
		learnClauseCount++;
	}

}
