package highlevelStrategy;

import factory.EDecide;
import factory.EForget;
import factory.ELearn;
import factory.ERestart;
import formula.Clause;
import formula.SetOfClauses;
import org.jetbrains.annotations.NotNull;
import ruleSystem.RuleSystem;
import specificationCore.*;

/**
 * Implementation of the strategy of the Chaff solver.
 * <p>
 * Extension of the classic DPLL algorithm with clause learning/forgetting (if
 * enabled), restarts (if enabled) and non-chronological backtracking.
 * 
 * @author Albert Schimpf
 *
 */
public class Chaff extends RuleSystem implements HighlevelStrategy {
	/**
	 * Saved trail reference.
	 */
	public boolean condition = true;

	/**
	 * The strategy has to operate on the formula {@code F} and has to have
	 * access to the trail {@code M}.
	 * 
	 * @param solver
	 *            reference to access formula {@code F} and trail {@code M}
	 * @param literalHeuristic
	 *            given decide heuristic
	 * @param forgetHeuristic
	 *            given forget heuristic
	 * @param learnHeuristic
	 *            given learn heuristic
	 * @param restartHeuristic
	 *            given restart heuristic
	 */
	public Chaff(@NotNull Solver solver, @NotNull EDecide literalHeuristic,
				 @NotNull ERestart restartHeuristic, @NotNull ELearn learnHeuristic,
				 @NotNull EForget forgetHeuristic) {
		super(solver, literalHeuristic, restartHeuristic, learnHeuristic,
				forgetHeuristic);
	}

	/**
	 * Actual iterative implementation of the chaff strategy.
	 */
	public boolean applyStrategy() {
		while (condition) {
			BCP();

			if (conflictGuard()) {
				applyConflict();

				if (getSolver().getState().M().getCurrentDecisionLevel() == 0) {
					return false;
				} else {


					Integer UIP = null;
					if (explainUIPGuard()) {
						UIP = applyExplainUIP();
						if (learnGuard()) {
							applyLearn();
						}
					}



					if(UIP == null){
						UIP = backjumpGuard();
					}

					applyBackjump(UIP);
				}

			} else {
				if (forgetGuard()) {
					applyForget();
				}
				if (restartGuard()) {
					applyRestart();
				}
				if (decideGuard()) {
					applyDecide();
				} else {
					return true;
				}
			}
		}

		throw new NullPointerException("Interupted Strategy");
	}

	/**
	 * Exhaustive application of unit rule, also called Boolean Constraint
	 * Propagation.
	 */
	private void BCP() {
		Clause unit = unitGuard();
		while (unit != null) {
			if(getSolver().getState().F().foundConflict()){
				break;
			}
			applyUnit(unit);
			unit = unitGuard();
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

}
