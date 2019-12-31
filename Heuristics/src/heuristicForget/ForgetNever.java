package heuristicForget;

import org.jetbrains.annotations.NotNull;
import formula.Clause;
import specificationCore.Solver;
import specificationHeuristics.ForgetStrategy;

import java.util.ArrayList;

/**
 * Forget strategy with a default response to never forget clauses.
 * 
 * @author Albert Schimpf
 *
 */
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
	
	/**
	 * Returns the name of the strategy without the hash-code.
	 * 
	 * @return name of strategy
	 */
	@Override
	public String toString(){
		return getClass().getName();
	}
}
