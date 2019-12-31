package jmss.core;


import jmss.formula.SetOfClauses;
import jmss.specificationCore.State;
import jmss.specificationCore.Trail;

import java.util.List;

/**
 * Created by Albert Schimpf on 05.02.16.
 */
public class StateImpl implements State {
    private SetOfClauses F;
    private Trail M;
    private List<Integer> C;

    @Override public void setInitialF(SetOfClauses F) { this.F = F; }
    @Override public void setInitialM(Trail M) { this.M = M; }

    @Override public void setC(List<Integer> clause) { this.C = clause; }

    @Override public SetOfClauses F() { return F; }
    @Override public Trail M() { return M; }
    @Override public List<Integer> C() { return C; }
}
