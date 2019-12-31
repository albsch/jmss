package jmss.formula.wl;

import jmss.annotations.NotNull;
import jmss.formula.VariableAbs;
import jmss.formula.Clause;
import jmss.specificationCore.Solver;

import java.util.ArrayList;

/**
 * 2016/02/01.
 */
public class VariableW extends VariableAbs {
    private final ArrayList<ClauseWL> ClausePositiveWatched;
    private final ArrayList<ClauseWL> ClauseNegativeWatched;

    private ArrayList<ClauseWL> toAddPositive = new ArrayList<>();
    private ArrayList<ClauseWL> toAddNegative = new ArrayList<>();

    public VariableW(int i, Solver solver) {
        super(i, solver);
        ClausePositiveWatched = new ArrayList<>();
        ClauseNegativeWatched = new ArrayList<>();
    }

    @Override
    public Solver getSolver() {
        throw new NullPointerException("NYI");
    }

    @Override
    public void setTrue() {
        assert getAssignment() == -1;
        setAssignment(1);

        ClauseNegativeWatched.removeIf(cl -> cl.searchNewWatch(-getIndex()));

        ClauseNegativeWatched.addAll(new ArrayList<>(toAddNegative));
        ClausePositiveWatched.addAll(new ArrayList<>(toAddPositive));
        toAddNegative.clear();
        toAddPositive.clear();
    }

    @Override
    public void setFalse() {
        assert getAssignment() == -1;
        setAssignment(0);

        ClausePositiveWatched.removeIf(cl -> cl.searchNewWatch(getIndex()));

        ClauseNegativeWatched.addAll(new ArrayList<>(toAddNegative));
        ClausePositiveWatched.addAll(new ArrayList<>(toAddPositive));
        toAddNegative.clear();
        toAddPositive.clear();
    }

    @Override
    public void undoAssignment() {
        // No clause has to be visited when backtracking
        assert getAssignment() == 1 || getAssignment() == 0;
        setAssignment(-1);
    }

    @Override
    public void removeConnections(Integer lit, Clause contained) {
        throw new NullPointerException("NYI");
    }

    public void connectToClause(@NotNull ClauseWL cl, Integer lit) {
        if(lit < 0){
            ClauseNegativeWatched.add(cl);
        }
        else{
            ClausePositiveWatched.add(cl);
        }
    }

    public void removeWatch(@NotNull ClauseWL clauseWL, Integer lit) {
        if(lit < 0){
            ClauseNegativeWatched.remove(clauseWL);
        }
        else{
            ClausePositiveWatched.remove(clauseWL);
        }
    }


    public void connectToClauseL(ClauseWL clauseW, Integer toLit) {
        if(toLit < 0){
            toAddNegative.add(clauseW);
        }
        else{
            toAddPositive.add(clauseW);
        }

    }

    public int isConnectedTo(ClauseWL clauseWL) {
        int c = 0;
        for (ClauseWL cl : ClausePositiveWatched) {
            if(cl == clauseWL) c++;
        }
        for (ClauseWL cl : ClauseNegativeWatched) {
            if(cl == clauseWL) c++;
        }

        return c;
    }

    public void update() {
        ClauseNegativeWatched.addAll(new ArrayList<>(toAddNegative));
        ClausePositiveWatched.addAll(new ArrayList<>(toAddPositive));
        toAddNegative.clear();
        toAddPositive.clear();
    }
}
