package specificationCore;

import formula.SetOfClauses;

import java.util.List;

/**
 * State container class for the solver state.
 *
 * Created by Albert Schimpf on 01.10.2015.
 */
public interface State {
    void setInitialF(SetOfClauses F);
    SetOfClauses F();
    void setInitialM(Trail M);
    Trail M();
    List<Integer> C();
    void setC(List<Integer> clause);
}
