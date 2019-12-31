package jmss.specificationCore;

import jmss.formula.Clause;

import java.util.List;

/**
 * Observer interface for listener of the solving process.
 * <p>
 * If a satellite component (heuristic, proof logger, GUI etc.) needs to be
 * updated during the solving process, it needs to implement this interface and
 * add itself to the observers with the method addObserver.
 * <p>
 * To use an implemented custom rule, a dedicated {@code onCustomRule} update
 * with desired parameters has to be specified. A custom rule can easily
 * interrupt/disrupt the solving process or distort the solution, if the rule is
 * not proven beforehand and used in an incorrect strategy.
 * 
 * @author Albert Schimpf
 *
 */
public interface SolverListener {
	/**
	 * Notifies the observer that the decision literal {@code ld} was added to
	 * the trail {@code M}.
	 * 
	 * @param ld
	 *            : decision literal
	 */
	default void onDecide(Integer ld) {}

	/**
	 * Notifies the observer that the unit literal {@code lu} was added to the
	 * trail {@code M}, with {@code cl} being its antecedent.
	 * 
	 * @param lu
	 *            : unit literal
	 * @param cl
	 *            : antecedent of unit literal
	 */
	default void onUnitPropagate(List<Integer> cl, Integer lu) {}

	/**
	 * Notifies the observer that the clause {@code cl} is currently
	 * conflicting.
	 *
	 * @param cl
	 *            : conflicting clause
	 */
	default void onConflict(Clause cl) {}

	/**
	 * Notifies the observer that the solver backtracked one literal, of which
	 * corresponding variable is now undefined again.
	 * 
	 * @param l
	 *            backtracked literal
	 */
	default void onBacktrack(Integer l) {}

	/**
	 * Notifies the observer that a resolution step was done, with literal
	 * {@code resLit} being resolved from the conflict clause {@code C} with the
	 * antecedent of {@code resLit}. {@code resolved} being the concluded
	 * clause.
	 * 
	 * @param ante
	 *            : antecedent of the resolved literal
	 * @param resLit
	 *            : the literal to resolve
	 * @param resolved
	 *            : the resolved clause
	 */
	default void onExplain(List<Integer> ante, Integer resLit, List<Integer> resolved) {}

	/**
	 * Notifies the observer that the clause {@code cl} was added to the jmss.formula
	 * {@code F} as a learned clause.
	 *
	 * @param cl
	 *            : learned clause
	 */
	default void onLearn(Clause cl) {}

	/**
	 * Notifies the observer that the (learned) clause {@code cl} was removed
	 * from jmss.formula {@code F}.
	 *
	 * @param cl
	 *            : learned clause
	 */
	default void onForget(Clause cl) {}

	/**
	 * Notifies the observer that the solver has restarted the solving process.
	 */
	default void onRestart() {}

	/**
	 * Notifies the observer that the solver backjumped from current level to
	 * given decision level and given flipped literal.
	 * 
	 * @param uip added to decision trail after backjumping
	 * @param bl backjump level
	 * @param current decision level
	 */
	default void onBackjump(Integer current, Integer bl, Integer uip) {}

	/**
	 * Notifies the observer that the converting process has finished and the
	 * jmss.formula {@code F} is fully constructed.
	 */
	default void onSetOfClausesLoaded() {}

	/**
	 * Notifies the observer that the clause {@code cl} was added to the jmss.formula
	 * {@code F} as an initial clause.
	 *
	 * @param cl
	 *            : initial clause
	 */
	default void onLearnInitial(List<Integer> cl) {}

	/**
	 * Notifies the observer that the module was selected to use for the solving process.
	 */
	default void onModuleSelect(String module, String name) {}
}
