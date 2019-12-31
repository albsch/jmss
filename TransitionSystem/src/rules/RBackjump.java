package rules;

import ruleSystem.RuleSystem;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationCore.State;
import specificationCore.Trail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2016/02/14.
 */
public class RBackjump {
    private final Trail M;
    private final Solver solver;
    private final State state;

    public RBackjump(Solver solver, CoreRules ruleSystem) {
        M = solver.getState().M();
        this.solver = solver;
        this.state = solver.getState();
    }

    public Integer guard() {
        // Check basic conditions
        if (solver.getState().C() == null) {
            return null;
        }
        if (M.getCurrentDecisionLevel() == 0) {
            return null;
        }

        for (Integer lit : state.C()) {
            if(M.getDecisionLevel(lit).equals(M.getCurrentDecisionLevel())){
                return lit;
            }
        }

        return null;
    }

    public void apply(Integer UIP) {
        // Save current level for observer update
        int current = M.getCurrentDecisionLevel();

        // Get second recent decision level of conflict clause
        List<Integer> lneg = new ArrayList<>(state.C().size());
        lneg.addAll(state.C().stream().map(l -> -l).collect(Collectors.toList()));

        int bl = M.getSecondRecentDecisionLevel(lneg);

        // If no SRDL:jump only one level back, and add negation of UIP to trail
        if (bl == -1) {
            M.backjumpToLevel(M.getCurrentDecisionLevel() - 1, UIP);

            state.setC(null);
            return;
        }

        M.backjumpToLevel(bl, UIP);
        state.setC(null);

        // Update observer
        for (SolverListener o : solver.getObs()) {
            o.onBackjump(current, bl, UIP);
        }
    }
}
