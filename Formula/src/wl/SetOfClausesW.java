package wl;

import abstractImplementation.ClauseAbs;
import abstractImplementation.SetOfClausesAbs;
import formula.Clause;
import interfaces.WatchedLiteralsClause;
import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;

import java.util.List;

import static java.lang.Math.abs;


/**
 * 2016/02/01.
 */
public class SetOfClausesW extends SetOfClausesAbs {
    public SetOfClausesW(@NotNull Solver solver) {
        super(solver);
    }

    @Override
    public void addClause(@NotNull List<Integer> cl) {
        Clause n = addCl(cl);
        if (n.isUnit()) foundUnitClause(n);
    }

    @Override
    public Clause addLearnedClause(List<Integer> cl) {
        Clause n = addCl(cl);
        n.assertConflictingState();

        return n;
    }

    private Clause addCl(@NotNull List<Integer> cl) {
        // only for instancing the initial formula
        WatchedLiteralsClause n = new ClauseWL(solver);

        ((ClauseAbs) n).setId(clauses.size());
        ((ClauseAbs) n).setLiterals(cl.size());
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
