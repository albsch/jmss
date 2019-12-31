package heuristicDecide;

import formula.Clause;
import org.jetbrains.annotations.NotNull;
import formula.SetOfClauses;
import specificationCore.*;
import specificationHeuristics.PolaritySelectionStrategy;

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
public class PolarityCachingPol implements PolaritySelectionStrategy,
		SolverListener {
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
		for (Integer i = 0; i < literalCache.length; i++) {
			literalCache[i] = true;
		}
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
	 * Not needed in this heuristic.
	 * @param cl
	 */
	@Override
	public void onConflict(Clause cl) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	@Override
	public void onExplain(List<Integer> ante, Integer resLit,
						  List<Integer> resolvente) {
	}

	/**
	 * Not needed in this heuristic.
	 * @param cl
	 */
	@Override
	public void onLearn(Clause cl) {
	}

	/**
	 * Not needed in this heuristic.
	 * @param cl
	 */
	@Override
	public void onForget(Clause cl) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	@Override
	public void onLearnInitial(List<Integer> cl) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	@Override
	public void onBacktrack(Integer l) {
	}

	/**
	 * Not needed in this heuristic.
	 */
	@Override
	public void onRestart() {
	}

	/**
	 * Not needed in this heuristic.
	 */
	@Override
	public void onBackjump(Integer current, Integer bl, Integer uip) {
	}

}
