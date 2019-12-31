package jmss.formula.wl;

import jmss.annotations.Module;
import jmss.formula.SetOfClausesAbs;
import jmss.formula.Clause;
import jmss.specificationCore.Solver;

import java.util.List;

import static java.lang.Math.abs;


/**
 * 2016/02/01.
 */
@Module(module = "formula", name = "two-watched-literals")
public class SetOfClausesW extends SetOfClausesAbs {

    public SetOfClausesW(Solver solver) {
        super(solver);
    }

    @Override
    public void addClause(List<Integer> cl) {
        Clause n = addCl(cl);
        if (n.isUnit()) foundUnitClause(n);
    }

    @Override
    public Clause addLearnedClause(List<Integer> cl) {
        Clause n = addCl(cl);
        n.assertConflictingState();

        return n;
    }

    private Clause addCl(List<Integer> cl) {
        // only for instancing the initial formula
        ClauseWL n = new ClauseWL(solver);

        n.setId(clauses.size());
        n.setLiterals(cl.size());
        initialClauses.add(n);
        clauses.add(cl);

        n.connectInitialWachtes();

        return n;
    }


    @Override
    public void forgetLearnedClause(Clause cl) {
        cl.removeConnectionToVariables();

        learnedClauses.remove(cl);
        clauses.set(((ClauseWL) cl).getId(), null);
    }

    @Override
    public void removeClauseConnection(Clause cl) {
        for (Integer lit : cl.getLiterals()) {
            variables[abs(lit)].removeConnections(lit, cl);
        }
    }

    private void instanceVariableArray(Integer length) {
        variables = new VariableW[length];
        freeVariables = variables.length - 1;
    }

    @Override
    public void variableCount(int variables) {
        instanceVariableArray(variables + 1);
        for (int i = 1; i < this.getVariables().length; i++) {
            VariableW temp = new VariableW(i, solver);
            this.getVariables()[i] = temp;
        }
    }
}
