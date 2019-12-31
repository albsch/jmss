package counterbased;

import abstractImplementation.SetOfClausesAbs;
import org.jetbrains.annotations.NotNull;
import formula.Clause;
import specificationCore.Solver;

import java.util.List;

import static java.lang.Math.abs;

public class SetOfClausesCbs extends SetOfClausesAbs {

    public SetOfClausesCbs(@NotNull Solver solver) {
        super(solver);
    }

    @Override
    public void addClause(@NotNull List<Integer> cl) {
        Clause n = addCl(cl);
        if (n.isUnit()) foundUnitClause(n);
    }

    @Override
    public Clause addLearnedClause(@NotNull List<Integer> cl) {
        Clause n = addCl(cl);
        n.assertConflictingState();

        return n;
    }

    private Clause addCl(@NotNull List<Integer> cl) {
        // only for instancing the initial formula
        ClauseCbs n = new ClauseCbs(solver);
        for (int l : cl) {
            VariableCbs var = (VariableCbs) getVariables()[abs(l)];

            if (l < 0) {
                var.connectToClauseNegative(n);
            } else {
                var.connectToClausePositive(n);
            }

        }

        n.setId(clauses.size());
        n.setLiterals(cl.size());
        initialClauses.add(n);
        clauses.add(cl);

        return n;
    }




    @Override
    public void forgetLearnedClause(@NotNull Clause cl) {
        cl.removeConnectionToVariables();

        learnedClauses.remove(cl);
        clauses.set(((ClauseCbs) cl).getId(), null);
    }

    @Override
    public void removeClauseConnection(Clause cl) {
        for (Integer lit : cl.getLiterals()) {
            variables[abs(lit)].removeConnections(lit, cl);
        }
    }


    @Override
    public void variableCount(int variables) {
        instanceVariableArray(variables + 1);
        for (int i = 1; i < this.getVariables().length; i++) {
            VariableCbs temp = new VariableCbs(i, solver);
            this.getVariables()[i] = temp;
        }
    }

    private void instanceVariableArray(Integer length) {
        variables = new VariableCbs[length];
        freeVariables = variables.length - 1;
    }
}
