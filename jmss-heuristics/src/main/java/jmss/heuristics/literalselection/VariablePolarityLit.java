package jmss.heuristics.literalselection;

import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.discovery.ModuleDiscovery;
import jmss.discovery.Option;
import jmss.formula.Variable;
import jmss.specificationCore.Solver;
import jmss.specificationHeuristics.LiteralSelectionStrategy;
import jmss.specificationHeuristics.PolaritySelectionStrategy;
import jmss.specificationHeuristics.VariableSelectionStrategy;

import java.util.Random;

/**
 * A strategy to select a variable and the polarity of it separately.
 * Returns a random decision literal at 5% chance.
 * 
 * @author Albert Schimpf
 *
 */
@Module(module = "literal-selection", name = "var-pol-random", defaultModule = true)
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
	 */
	public VariablePolarityLit(@NotNull Solver solver, String[] args) {
		varStrategy = ModuleDiscovery.getModule("variable-selection", Option.parseModule("variable-selection", args), solver, args);
		polStrategy = ModuleDiscovery.getModule("variable-polarity-selection", Option.parseModule("variable-polarity-selection", args), solver, args);

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
		if (chance <= 0.05f) {
			lit = randStrategy.getLiteral();
		} else {
			Variable var = varStrategy.getVariable();
			assert var != null : "Null variable given";
			lit = polStrategy.getPolarity(var.getIndex());
		}

		return lit;
	}

}
