//package jmss.sts.rules;
//
//import jmss.annotations.NotNull;
//import jmss.formula.SetOfClauses;
//import jmss.specificationCore.Solver;
//import jmss.specificationCore.State;
//import jmss.specificationCore.Trail;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class RExplain {
//	private final SetOfClauses F;
//	private final Trail M;
//	private final State state;
//
//	public RExplain(@NotNull Solver solver) {
//		F = solver.getState().F();
//		M = solver.getState().M();
//		this.state = solver.getState();
//	}
//
//	public Integer guard() {
//		// C should not be empty
//		if (state.C() == null) {
//			return null;
//		}
//
//		int cdl = M.getCurrentDecisionLevel();
//		List<Integer> lits = state.C().getLiterals().stream()
//				.filter(lit -> (M.getDecisionLevel(lit) == cdl) && (M.getReason(lit) != null))
//				.collect(Collectors.toList());
//
//		int max = -1;
//		for (Integer p : lits) {
//			if (F.getVariable(p).getOrder() > max)
//				max = p;
//		}
//
//		return max;
//	}
//
//	public void apply(Integer resolve) {
//		SetOfClauses.generalResolve(state.C().getLiterals(),M.getReason(resolve),Math.abs(resolve));
//	}
//}
