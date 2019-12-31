package highlevelStrategy;

import abstractImplementation.SetOfClausesAbs;
import factory.EDecide;
import factory.EForget;
import factory.ELearn;
import factory.ERestart;
import formula.Clause;
import formula.SetOfClauses;
import org.jetbrains.annotations.NotNull;
import ruleSystem.RuleSystem;
import specificationCore.HighlevelStrategy;
import specificationCore.Solver;
import specificationCore.Trail;
import wl.SetOfClausesW;


/**
 * Implementation of the classic Davis-Putnam-Logemann-Loveland strategy.
 * 
 * <p>
 * {@code (((Conflict; Backtrack) || UnitPropag)* ; [Decide])*   }
 * 
 * <p>
 * This strategy consists of 2 main phases using only the minimum amount of
 * rules: conflict, unit propagation, decide and backtrack (chronological
 * backtracking).
 * <p>
 * Phase 1: Unit propagation until either a conflict clause is found, or no unit
 * clauses remain. If a conflict was found, backtrack one decision level. If no
 * unit clauses remain and no clause is conflicting, go to phase 2.
 * <p>
 * Phase 2: Assert a decision literal. Go to phase 1.
 * 
 * <p>
 * Repeat phases 1 and 2 until either the formula is satisfiable or conflicting
 * at decision level 0 (i.e. no backtracking possible).
 * 
 * <p>
 * Implementation uses external defined rules, extending the {@link RuleSystem
 * Rule System}.
 * 
 * @see HighlevelStrategy
 * @author Albert Schimpf
 *
 */
public class DPLLSimple extends RuleSystem implements HighlevelStrategy {
	/**
	 * The strategy has to operate on the formula {@code F} and has to have
	 * access to the trail {@code M}.
	 * 
	 * @param solver
	 *            reference to access formula {@code F} and trail {@code M}
	 * @param literalHeuristic given decide heuristic
	 * @param forgetHeuristic given forget heuristic
	 * @param learnHeuristic given learn heuristic
	 * @param restartHeuristic given restart heuristic
	 */
	public DPLLSimple(@NotNull Solver solver,
					  @NotNull EDecide literalHeuristic, @NotNull ERestart restartHeuristic,
					  @NotNull ELearn learnHeuristic, @NotNull EForget forgetHeuristic) {
		super(solver, literalHeuristic, restartHeuristic, learnHeuristic,
				forgetHeuristic);
	}

	/**
	 * Actual implementation of the Classic DPLL strategy.
	 * <p>
	 * {@code (((Conflict; Backtrack) || UnitPropag)* ; [Decide])*   }
	 * <p>
	 * No clause learning and forgetting; Optional restarts if enabled.
	 * 
	 * @return true if formula {@code F} is satisfiable, false if not.
	 */
	@Override
	public boolean applyStrategy() {
		while (true) {
//			System.out.println(M);
			BCP();

			if (conflictGuard()) {
				applyConflict();

				if (getSolver().getState().M().getCurrentDecisionLevel() == 0) {
					return false;
				} else {

					assert backtrackGuard();
					applyBacktrack();
				}

			} else {
				if (decideGuard()) {
					applyDecide();
				} else {
					return true;
				}
			}
		}
	}

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
