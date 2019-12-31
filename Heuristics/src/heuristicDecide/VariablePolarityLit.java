package heuristicDecide;

import org.jetbrains.annotations.NotNull;
import formula.Variable;
import specificationCore.Solver;
import specificationHeuristics.LiteralSelectionStrategy;
import specificationHeuristics.PolaritySelectionStrategy;
import specificationHeuristics.VariableSelectionStrategy;

import java.util.Random;

/**
 * A strategy to select a variable and the polarity of it separately. Returns a
 * random decision literal at 5% chance.
 * 
 * @author Albert Schimpf
 *
 */
public class VariablePolarityLit implements LiteralSelectionStrategy {
	/**
	 * The variable selection heuristic.
	 */
	private final VariableSelectionStrategy varStrategy;
	/**
	 * The polarity selection heuristic.
	 */
	private final PolaritySelectionStrategy polStrategy;
	/**
	 * A random selection heuristic used as decoration.
	 */
	private final RandomLit randStrategy;
	/**
	 * A random seed.
	 */
	private final Random seed = new Random();

	/**
	 * Default constructor which saves the main module reference, the variable
	 * selection heuristic and the polarity selection heuristic.
	 * 
	 * @param solver
	 *            the main module reference.
	 * @param varS
	 *            variable selection heuristic
	 * @param varP
	 *            polarity selection heuristic
	 */
	public VariablePolarityLit(@NotNull Solver solver, @NotNull VariableSelectionStrategy varS,
							   @NotNull PolaritySelectionStrategy varP) {
		varStrategy = varS;
		polStrategy = varP;

		randStrategy = new RandomLit(solver);
	}

	/**
	 * Returns the literal with the variable index from the variable selection
	 * heuristic and the polarity of the polarity selection heuristic.
	 * 
	 * @return free literal
	 */
	@Override
	public Integer getLiteral() {
		float chance = seed.nextFloat();
		int lit;

		// Return a random literal 5% of the time
		if (chance <= 0.001f) {
			lit = randStrategy.getLiteral();
		} else {
			Variable var = varStrategy.getVariable();
			assert var != null : "Null variable given";
			lit = polStrategy.getPolarity(var.getIndex());
		}

		return lit;
	}

	public VariableSelectionStrategy getVarStrategy() {
		return this.varStrategy;
	}

	public PolaritySelectionStrategy getPolStrategy() {
		return this.polStrategy;
	}

	public String toString(){
		return getPolStrategy().toString()+ " || "+ getVarStrategy().toString();
	}
}
