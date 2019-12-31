package counterbased;

import abstractImplementation.VariableAbs;
import interfaces.CounterBasedClause;
import interfaces.CounterBasedVariable;
import org.jetbrains.annotations.NotNull;
import formula.Clause;
import specificationCore.Solver;

import java.util.ArrayList;

public class VariableCbs extends VariableAbs implements CounterBasedVariable {
	private final ArrayList<CounterBasedClause> containedPositively;
	private final ArrayList<CounterBasedClause> containedNegatively;

	public VariableCbs(int i, Solver solver) {
		super(i, solver);
		containedPositively = new ArrayList<>();
		containedNegatively = new ArrayList<>();
	}

	@Override
	public Solver getSolver() {
		throw new NullPointerException("getSolver not needed.");
	}

	@Override
	public void setTrue() {
		setAssignment(1);

		// increase unsat literals for negative clauses
		containedNegatively.forEach(CounterBasedClause::incrementUnsatisfied);

		// increase sat literals for positive clauses
		containedPositively.forEach(CounterBasedClause::incrementSatisfied);
	}

	@Override
	public void setFalse() {
		assert getAssignment() == -1;
		setAssignment(0);

		containedPositively.forEach(CounterBasedClause::incrementUnsatisfied);
		containedNegatively.forEach(CounterBasedClause::incrementSatisfied);
	}

	@Override
	public void undoAssignment() {
		assert getAssignment() == 1 || getAssignment() == 0;

		// Case 1
		if (getAssignment() == 1) {
			containedPositively.forEach(CounterBasedClause::decrementSatisfied);
			containedNegatively.forEach(CounterBasedClause::decrementUnsatisfied);
		}

		// Case 2
		if (getAssignment() == 0) {
			containedNegatively.forEach(CounterBasedClause::decrementSatisfied);
			containedPositively.forEach(CounterBasedClause::decrementUnsatisfied);
		}

		setAssignment(-1);
	}


	@Override
	public void removeConnections(Integer l, Clause cl) {
		CounterBasedClause cb = (CounterBasedClause) cl;
		if(l < 0){
			containedNegatively.remove(cb);
		}
		else{
			containedPositively.remove(cb);
		}
	}

	@Override
	public void connectToClausePositive(@NotNull CounterBasedClause cl) {
		containedPositively.add(cl);
	}

	@Override
	public void connectToClauseNegative(@NotNull CounterBasedClause cl) {
		containedNegatively.add(cl);
	}
}
