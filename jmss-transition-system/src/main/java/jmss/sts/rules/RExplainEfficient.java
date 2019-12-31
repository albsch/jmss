package jmss.sts.rules;

import jmss.annotations.NotNull;
import jmss.formula.SetOfClauses;
import jmss.formula.Variable;
import jmss.rules.Explain;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationCore.State;
import jmss.specificationCore.Trail;

import java.util.LinkedList;
import java.util.List;


public class RExplainEfficient implements Explain {
	private final SetOfClauses F;
	private final Trail M;
	private final State state;
	private final Solver solver;

	private Integer UIP;
	private List<Integer> orderedLits;

	public RExplainEfficient(@NotNull Solver solver) {
		F = solver.getState().F();
		M = solver.getState().M();
		this.state = solver.getState();
		this.solver = solver;
	}

	@Override
	public boolean guard() {
		orderedLits = new LinkedList<>();

		// C should not be empty
		if (state.C() == null) {
			return false;
		}

		// Create ordered list of literals to be resolved in a backwards process
		List<Integer> toBeResolved = M
				.getCurrentDecisionLevelLiteralsFromEnd();

		assert toBeResolved.size() > 0;

		// If there are no literals or only one (UIP), resolution is not needed
		// (first UIP is decision literal)
		if (toBeResolved.size() < 2) {
			return false;
		}



		// Count literals in C
		int anteCount = 0;
		int litCount = 0;
		for (Integer l : toBeResolved) {
			if (state.C().contains(-l)) {
				litCount++;
				if (M.getReason(l) != null) {
					orderedLits.add(l);
					anteCount++;
				}
			}
		}

		// There should be at least 2 literals of C in current level
		if (litCount > 1) {
			// And 1 unit literal
			if (anteCount > 0) {
				return true;
			}
		}

		// Otherwise, resolution not applicable
		return false;
	}

	@Override
	public Integer apply() {
		UIP = null;

		while(!orderedLits.isEmpty()){
//			System.out.println("CURRENT CONFLICT" +state.C());
//			System.out.println("List to be resolved: "+orderedLits);
			Integer lit = orderedLits.remove(0);


			// FirstUIP: Check, if only one literal is left in current decision
			// level
			int firstUIPCount = 0;
			Integer firstUIP = null;
			for (Integer cLit : state.C()) {
				if (F.getVariable(cLit).getDl() == M.getCurrentDecisionLevel()) {
					firstUIP = cLit;
					firstUIPCount++;
				}
			}

			// One literal in decision level, first UIP
			if (firstUIPCount == 1) {
//				assert M.inTrail(firstUIP);
				UIP = firstUIP;
				return UIP;
			}

			// If literal has no antecedent, do nothing
			List<Integer> ante = M.getReason(lit);
			if (null != ante) {
				// Apply one resolve step
				state.setC(applyExplain(ante, F.getVariable(lit)));
//				System.out.print("Produced ");

				state.C().stream().filter(i -> F.getVariable(i).getDl() == M.getCurrentDecisionLevel()).filter(i -> !i.equals(lit) && !orderedLits.contains(i)).forEach(i -> orderedLits.add(i));

//				System.out.println();


				// Update listener on resolution step
				for (SolverListener l : solver.getObs()) {
					l.onExplain(ante, lit, state.C());
				}
			}

			// Iterate and search for next literal
		}

		return UIP;
	}


	private List<Integer> applyExplain(@NotNull List<Integer> ante,
									   @NotNull Variable var) {
		return SetOfClauses.generalResolve(state.C(),ante,var.getIndex());
	}

}
