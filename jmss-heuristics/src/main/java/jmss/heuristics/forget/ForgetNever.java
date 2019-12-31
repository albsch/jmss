package jmss.heuristics.forget;

import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.formula.Clause;
import jmss.specificationCore.Solver;
import jmss.specificationHeuristics.ForgetStrategy;

import java.util.ArrayList;

/**
 * Forget strategy with a default response to never forget clauses.
 * 
 * @author Albert Schimpf
 *
 */
@Module(module = "heuristic-forget", name = "forget-never")
public class ForgetNever implements ForgetStrategy {
	public ForgetNever(@NotNull Solver solver) {}

	@Override
	public boolean shouldForget() {
		return false;
	}

	@Override
	public ArrayList<Clause> forgetClauses() {
		throw new NullPointerException("Function call forbidden.");
	}
}
