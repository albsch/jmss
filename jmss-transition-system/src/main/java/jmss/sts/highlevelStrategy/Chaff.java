package jmss.sts.highlevelStrategy;


import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.formula.Clause;
import jmss.specificationCore.HighlevelStrategy;
import jmss.specificationCore.Solver;
import jmss.sts.rules.*;

/**
 * Implementation of the strategy of the Chaff solver.
 * <p>
 * Extension of the classic DPLL algorithm with clause learning/forgetting (if
 * enabled), restarts (if enabled) and non-chronological backtracking.
 *
 * @author Albert Schimpf
 *
 */
@Module(module = "high-level-strategy", name = "chaff", defaultModule = true)
public class Chaff implements HighlevelStrategy {
	public boolean condition = true;

	private final Solver solver;

	private final RUnit unit;
	private final RConflict conflict;
	private final RBackjump backjump;
	private final RDecide decide;
	private final RExplainEfficient explain;
	private final RLearn learn;
	private final RForget forget;
	private final RRestart restart;

	/**
	 * The strategy has to operate on the formula {@code F} and has to have
	 * access to the trail {@code M}.
	 */
	public Chaff(@NotNull Solver solver, String[] args) throws Exception {
		this.solver = solver;
		unit = new RUnit(solver);
		conflict = new RConflict(solver);
		backjump = new RBackjump(solver);
		decide = new RDecide(solver, args);
		explain = new RExplainEfficient(solver);
		learn = new RLearn(solver, args);
		forget = new RForget(solver, args);
		restart = new RRestart(solver, args);
	}

	/**
	 * Actual iterative implementation of the chaff strategy.
	 */
	public boolean applyStrategy() {
		while (condition) {
			BCP();

			if (conflict.guard()) {
				conflict.apply();

				if (solver.getState().M().getCurrentDecisionLevel() == 0) {
					return false;
				} else {

					Integer UIP = null;
					if (explain.guard()) {
						UIP = explain.apply();
						if (learn.guard()) {
							learn.apply();
						}
					}

					if(UIP == null){
						UIP = backjump.guard();
					}

					backjump.apply(UIP);
				}

			} else {
				if (forget.guard()) {
					forget.apply();
				}
				if (restart.guard()) {
					restart.apply();
				}
				if (decide.guard()) {
					decide.apply();
				} else {
					return true;
				}
			}
		}

		throw new NullPointerException("Interrupted Strategy");
	}

	/**
	 * Exhaustive application of unit rule, also called Boolean Constraint
	 * Propagation.
	 */
	private void BCP() {
		Clause unitClause = unit.guard();
		while (unitClause != null) {
			if(solver.getState().F().foundConflict()){
				break;
			}
			unit.apply(unitClause);
			unitClause = unit.guard();
		}
	}

}
