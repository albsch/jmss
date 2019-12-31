package rules;

import formula.SetOfClauses;
import formula.Variable;
import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationCore.State;
import specificationCore.Trail;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RExplain {
	private final SetOfClauses F;
	private final Trail M;
	private final State state;

	public RExplain(@NotNull Solver solver, @NotNull CoreRules ruleSystem) {
		F = solver.getState().F();
		M = solver.getState().M();
		this.state = solver.getState();
	}

	public Integer guard() {
		// C should not be empty
		if (state.C() == null) {
			return null;
		}

		List<Integer> lits = new ArrayList<>();

		int cdl = M.getCurrentDecisionLevel();
		lits.addAll(state.C().stream().filter(lit -> (M.getDecisionLevel(lit) == cdl) && (M.getReason(lit) != null)).collect(Collectors.toList()));

		int max = -1;
		for (Integer p : lits) {
			if (F.getVariable(p).getOrder() > max)
				max = p;
		}

		return max;
	}

	public void apply(Integer resolve) {
		SetOfClauses.generalResolve(state.C(),M.getReason(resolve),Math.abs(resolve));
	}
}
