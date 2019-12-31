package jmss.formula;

import jmss.specificationCore.Solver;
import jmss.specificationCore.State;

import java.util.List;

/**
 * Abstract implementation of a {@link Clause ClauseAbs} with a basic
 * function for resolution.
 *
 * @author Albert Schimpf
 */
public abstract class ClauseAbs implements Clause {
    protected int id = -1;

    public void setLiterals(int literals) {
        this.literals = literals;
    }

    public int size(){
        return literals;
    }

    private int literals = -1;
    protected final State state;

    public ClauseAbs(Solver solver) {
        this.state = solver.getState();
    }


    public void setId(int i){
        id = i;
    }

    public int getId(){
        return id;
    }


    @Override
    public List<Integer> getLiterals() {
        return state.F().getLiterals(id);
    }

    @Override
    public void removeConnectionToVariables() {
        state.F().removeClauseConnection(this);
    }

}


