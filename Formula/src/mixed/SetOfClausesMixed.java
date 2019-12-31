package mixed;

import abstractImplementation.SetOfClausesAbs;
import counterbased.ClauseCbs;
import counterbased.VariableCbs;
import formula.Clause;
import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;

import java.util.List;

import static java.lang.Math.abs;

public class SetOfClausesMixed extends SetOfClausesAbs {
    private static int BIG = 10;

    public SetOfClausesMixed(@NotNull Solver solver) {
        super(solver);
    }

    @Override
    public void addClause(@NotNull List<Integer> cl) {
        Clause n;
        if(cl.size() < BIG){
            n = addClSmall(cl);
        }else{
            n = addClBig(cl);
        }

        if (n.isUnit()) foundUnitClause(n);
    }

    @Override
    public Clause addLearnedClause(@NotNull List<Integer> cl) {
        Clause n;
        if(cl.size() < BIG){
            n = addClSmall(cl);
        }else{
            n = addClBig(cl);
        }

        n.assertConflictingState();

        return n;
    }

    private Clause addClBig(List<Integer> cl) {
        // only for instancing the initial formula
        ClauseMixed n = new ClauseMixed(solver,true);

        n.setId(clauses.size());
        n.setLiterals(cl.size());
        initialClauses.add(n);
        clauses.add(cl);

        n.connectInitialWachtes();

        return n;
    }

    private Clause addClSmall(@NotNull List<Integer> cl) {
        // only for instancing the initial formula
        ClauseMixed n = new ClauseMixed(solver,false);
        for (int l : cl) {
            VariableMixed var = (VariableMixed) getVariables()[abs(l)];

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
            VariableMixed temp = new VariableMixed(i, solver);
            this.getVariables()[i] = temp;
        }
    }

    private void instanceVariableArray(Integer length) {
        variables = new VariableMixed[length];
        freeVariables = variables.length - 1;
    }

//    public String toString(){
//        int small = 0;
//        int big = 0;
//        for (Clause cl : initialClauses) {
//            if(((ClauseMixed) cl).isBig()){
//                big++;
//            }
//            else{
//                small++;
//            }
//        }
//
//        return "small: "+small+ " || big: "+big;
//    }
}
