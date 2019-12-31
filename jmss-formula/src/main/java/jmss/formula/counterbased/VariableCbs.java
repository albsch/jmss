package jmss.formula.counterbased;

import jmss.annotations.NotNull;
import jmss.formula.VariableAbs;
import jmss.formula.Clause;
import jmss.specificationCore.Solver;

import java.util.ArrayList;

public class VariableCbs extends VariableAbs {
	private final ArrayList<ClauseCbs> containedPositively = new ArrayList<ClauseCbs>();
	private final ArrayList<ClauseCbs> containedNegatively = new ArrayList<ClauseCbs>();

	public VariableCbs(int i, Solver solver) {
		super(i, solver);
	}

	@Override public Solver getSolver() { throw new NullPointerException("getSolver not needed."); }

	@Override
	public void setTrue() {
		setAssignment(1);

		// increase unsat literals for negative clauses
		containedNegatively.forEach(ClauseCbs::incrementUnsatisfied);

		// increase sat literals for positive clauses
		containedPositively.forEach(ClauseCbs::incrementSatisfied);
	}

	@Override
	public void setFalse() {
		assert getAssignment() == -1;
		setAssignment(0);

		containedPositively.forEach(ClauseCbs::incrementUnsatisfied);
		containedNegatively.forEach(ClauseCbs::incrementSatisfied);
	}

	@Override
	public void undoAssignment() {
		assert getAssignment() == 1 || getAssignment() == 0;

		// Case 1
		if (getAssignment() == 1) {
			containedPositively.forEach(ClauseCbs::decrementSatisfied);
			containedNegatively.forEach(ClauseCbs::decrementUnsatisfied);
		}

		// Case 2
		if (getAssignment() == 0) {
			containedNegatively.forEach(ClauseCbs::decrementSatisfied);
			containedPositively.forEach(ClauseCbs::decrementUnsatisfied);
		}

		setAssignment(-1);
	}


	@Override
	public void removeConnections(Integer l, Clause cl) {
		ClauseCbs cb = (ClauseCbs) cl;
		if(l < 0){
			containedNegatively.remove(cb);
		}
		else{
			containedPositively.remove(cb);
		}
	}

	public void connectToClausePositive(@NotNull ClauseCbs cl) {
		containedPositively.add(cl);
	}

	public void connectToClauseNegative(@NotNull ClauseCbs cl) {
		containedNegatively.add(cl);
	}
}
