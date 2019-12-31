package heuristicDecide;

import org.jetbrains.annotations.NotNull;
import formula.SetOfClauses;
import formula.Variable;
import specificationCore.Solver;
import specificationHeuristics.LiteralSelectionStrategy;

import java.util.List;
import java.util.Random;


/**
 * A random literal selection heuristic. Returns a free variable at random with
 * random polarity.
 * <p>
 * If used sparsely, this heuristic can speed up the solving process (i.e. used
 * as decoration 5% of the time).
 * 
 * @author Albert Schimpf
 *
 */
public class RandomLit implements LiteralSelectionStrategy {
	private final SetOfClauses F;

	/**
	 * The heuristic has to operate on the formula {@code F}.
	 * 
	 * @param solver
	 *            reference to access formula {@code F}
	 */
	public RandomLit(@NotNull Solver solver) {
		this.F = solver.getState().F();
	}

	/**
	 * Returns a free random variable with random polarity.
	 *
	 * @return Free literal
	 */
	@Override
	public Integer getLiteral() {
		List<Variable> vars = F.getUnassignedVariablesList();

		Random rand = new Random();
		int randomNum = rand.nextInt(vars.size());
		Variable var = vars.get(randomNum);

		int randomBool = rand.nextInt(2);
		int result;
		if (randomBool == 1) {
			result = var.getIndex();
		} else {
			result = -var.getIndex();
		}

		return result;

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
