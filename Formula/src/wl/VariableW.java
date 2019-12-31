package wl;

import abstractImplementation.VariableAbs;
import interfaces.WatchedLiteralsClause;
import interfaces.WatchedLiteralsVariable;
import org.jetbrains.annotations.NotNull;
import formula.Clause;
import specificationCore.Solver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * 2016/02/01.
 */
public class VariableW extends VariableAbs implements WatchedLiteralsVariable {
    private final ArrayList<WatchedLiteralsClause> ClausePositiveWatched;
    private final ArrayList<WatchedLiteralsClause> ClauseNegativeWatched;

    private ArrayList<WatchedLiteralsClause> toAddPositive = new ArrayList<>();
    private ArrayList<WatchedLiteralsClause> toAddNegative = new ArrayList<>();

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


        for (Iterator<WatchedLiteralsClause> it = ClauseNegativeWatched.iterator(); it
                .hasNext();) {
            WatchedLiteralsClause cl = it.next();
            if (cl.searchNewWatch(-getIndex())) {
                it.remove();
            }
        }

        ClauseNegativeWatched.addAll(toAddNegative.stream().collect(Collectors.toList()));
        ClausePositiveWatched.addAll(toAddPositive.stream().collect(Collectors.toList()));
        toAddNegative.clear();
        toAddPositive.clear();
    }

    @Override
    public void setFalse() {
        assert getAssignment() == -1;
        setAssignment(0);

        for (Iterator<WatchedLiteralsClause> it = ClausePositiveWatched.iterator(); it
                .hasNext();) {
            WatchedLiteralsClause cl = it.next();
            if (cl.searchNewWatch(getIndex())) {
                it.remove();
            }
        }


        ClauseNegativeWatched.addAll(toAddNegative.stream().collect(Collectors.toList()));
        ClausePositiveWatched.addAll(toAddPositive.stream().collect(Collectors.toList()));
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

    @Override
    public void connectToClause(@NotNull WatchedLiteralsClause cl, Integer lit) {
        if(lit < 0){
            ClauseNegativeWatched.add(cl);
        }
        else{
            ClausePositiveWatched.add(cl);
        }
    }


    @Override
    public void removeWatch(@NotNull WatchedLiteralsClause clauseWL, Integer lit) {
        if(lit < 0){
            ClauseNegativeWatched.remove(clauseWL);
        }
        else{
            ClausePositiveWatched.remove(clauseWL);
        }
    }


    public void connectToClauseL(WatchedLiteralsClause clauseW, Integer toLit) {
        if(toLit < 0){
            toAddNegative.add(clauseW);
        }
        else{
            toAddPositive.add(clauseW);
        }

    }

    public int isConnectedTo(WatchedLiteralsClause clauseWL) {
        int c = 0;
        for (WatchedLiteralsClause cl : ClausePositiveWatched) {
            if(cl == clauseWL) c++;
        }
        for (WatchedLiteralsClause cl : ClauseNegativeWatched) {
            if(cl == clauseWL) c++;
        }

        return c;
    }

    public void update() {
        ClauseNegativeWatched.addAll(toAddNegative.stream().collect(Collectors.toList()));
        ClausePositiveWatched.addAll(toAddPositive.stream().collect(Collectors.toList()));
        toAddNegative.clear();
        toAddPositive.clear();
    }
}
