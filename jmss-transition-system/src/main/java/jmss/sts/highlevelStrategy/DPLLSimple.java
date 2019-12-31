package jmss.sts.highlevelStrategy;


import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.formula.Clause;
import jmss.specificationCore.HighlevelStrategy;
import jmss.specificationCore.Solver;
import jmss.sts.rules.*;

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
 *
 * @see HighlevelStrategy
 * @author Albert Schimpf
 *
 */
@Module(module = "high-level-strategy", name = "dpll-simple")
public class DPLLSimple implements HighlevelStrategy {

	private final Solver solver;

	private final RUnit unit;
	private final RConflict conflict;
	private final RBacktrack backtrack;
	private final RDecide decide;

	/**
	 * The strategy has to operate on the formula {@code F} and has to have
	 * access to the trail {@code M}.
	 */
	public DPLLSimple(@NotNull Solver solver, String[] args) throws Exception {
		this.solver = solver;
		unit = new RUnit(solver);
		conflict = new RConflict(solver);
		backtrack = new RBacktrack(solver);
		decide = new RDecide(solver, args);
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
			BCP();

			if (conflict.guard()) {
				conflict.apply();

				if (solver.getState().M().getCurrentDecisionLevel() == 0) {
					return false;
				} else {
					assert backtrack.guard();
					backtrack.apply();
				}

			} else {
				if (decide.guard()) {
					decide.apply();
				} else {
					return true;
				}
			}
		}
	}

	private void BCP() {
		Clause unit = this.unit.guard();
		while (unit != null) {
			if(solver.getState().F().foundConflict()){
				break;
			}
			this.unit.apply(unit);
			unit = this.unit.guard();
		}
	}
}
