package jmss.heuristics.variablepolarityselection;


import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.formula.SetOfClauses;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationHeuristics.PolaritySelectionStrategy;

import java.util.List;

import static java.lang.Math.abs;

/**
 * Polarity selection heuristic based on caching.
 * <p>
 * Returns the polarity of a given variable based on previous used polarity.
 * Initially random or either false or true for every variable.
 * 
 * @author Albert Schimpf
 *
 */
@Module(module = "variable-polarity-selection", name = "cache-polarity", defaultModule = true)
public class PolarityCachingPol implements PolaritySelectionStrategy, SolverListener {
	/**
	 * Array to cache the polarity of all variables.
	 */
	private Boolean[] literalCache;
	/**
	 * Saved formula reference.
	 */
	private final SetOfClauses F;

	/**
	 * Default constructor which saves the main module reference.
	 * <p>
	 * Adds itself to the observers to respond to some rules.
	 * 
	 * @param solver
	 *            the main module reference.
	 */
	public PolarityCachingPol(@NotNull Solver solver) {
		this.F = solver.getState().F();
		solver.addObserver(this);
	}

	/**
	 * Returns a literal with cached polarity.
	 * 
	 * @param varIndex
	 *            variable to get the polarity
	 */
	@Override
	public Integer getPolarity(Integer varIndex) {
		if(literalCache[varIndex]){
			return varIndex;
		}
		else{
			return -varIndex;
		}
	}

	/**
	 * Cache the polarity of given literal on decide.
	 */
	@Override
	public void onDecide(@NotNull Integer l) {
		if(l > 0){
			literalCache[abs(l)] = true;
		} else{
			literalCache[abs(l)] = false;
		}
	}


	/**
	 * Cache the polarity of given literal on unit propagation.
	 */
	@Override
	public void onUnitPropagate(@NotNull List<Integer> cl,
			@NotNull Integer l) {
		if(l > 0){
			literalCache[abs(l)] = true;
		} else{
			literalCache[abs(l)] = false;
		}
	}

	/**
	 * Instance variable array when formula is fully loaded.
	 */
	@Override
	public void onSetOfClausesLoaded() {
		literalCache = new Boolean[F.getVariables().length];
		for (int i = 0; i < literalCache.length; i++) {
			literalCache[i] = true;
		}
	}
}
