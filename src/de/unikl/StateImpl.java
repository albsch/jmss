package de.unikl;

import formula.SetOfClauses;
import specificationCore.State;
import specificationCore.Trail;

import java.util.List;

/**
 * Created by nas on 05.02.16.
 */
public class StateImpl implements State{
    public SetOfClauses F;
    public Trail M;
    public List<Integer> C;

    @Override
    public void setInitialF(SetOfClauses F) {
        this.F = F;
    }

    @Override
    public SetOfClauses F() {
        return F;
    }

    @Override
    public void setInitialM(Trail M) {
        this.M = M;
    }

    @Override
    public Trail M() {
        return M;
    }

    @Override
    public List<Integer> C() {
        return C;
    }

    @Override
    public void setC(List<Integer> clause) {
        this.C = clause;
    }
}
