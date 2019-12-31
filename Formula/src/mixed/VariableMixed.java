package mixed;


import abstractImplementation.VariableAbs;
import formula.Clause;
import interfaces.CounterBasedClause;
import interfaces.CounterBasedVariable;
import interfaces.WatchedLiteralsClause;
import interfaces.WatchedLiteralsVariable;
import org.jetbrains.annotations.NotNull;
import specificationCore.Solver;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Created by tp on 07.12.15.
 */
public class VariableMixed extends VariableAbs implements WatchedLiteralsVariable, CounterBasedVariable {
    //non-lazy VarCB stuff
    private final ArrayList<CounterBasedClause> containedPositively;
    private final ArrayList<CounterBasedClause> containedNegatively;

    //VarWL stuff
    private final ArrayList<WatchedLiteralsClause> ClausePositiveWatched;
    private final ArrayList<WatchedLiteralsClause> ClauseNegativeWatched;
    private ArrayList<WatchedLiteralsClause> toAddPositive = new ArrayList<>();
    private ArrayList<WatchedLiteralsClause> toAddNegative = new ArrayList<>();

    public VariableMixed(int i, Solver solver) {
        super(i, solver);
        containedPositively = new ArrayList<>();
        containedNegatively = new ArrayList<>();
        ClausePositiveWatched = new ArrayList<>();
        ClauseNegativeWatched = new ArrayList<>();
    }

    @Override
    public Solver getSolver() {
        throw new NullPointerException("getSolver not needed.");
    }

    @Override
    public void setTrue() {
        setAssignment(1);

        // increase unsat literals for negative clauses
        containedNegatively.forEach(CounterBasedClause::incrementUnsatisfied);

        // increase sat literals for positive clauses
        containedPositively.forEach(CounterBasedClause::incrementSatisfied);

        //VARWL STUFF
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
        setAssignment(0);

        containedPositively.forEach(CounterBasedClause::incrementUnsatisfied);
        containedNegatively.forEach(CounterBasedClause::incrementSatisfied);

        //varWL stuff
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
        // Case 1
        if (getAssignment() == 1) {
            containedPositively.forEach(CounterBasedClause::decrementSatisfied);
            containedNegatively.forEach(CounterBasedClause::decrementUnsatisfied);
        }

        // Case 2
        if (getAssignment() == 0) {
            containedNegatively.forEach(CounterBasedClause::decrementSatisfied);
            containedPositively.forEach(CounterBasedClause::decrementUnsatisfied);
        }

        setAssignment(-1);

        //VARWL STUFF
        //do nothing
    }

    @Override
    public void removeConnections(Integer l, Clause cl) {
        CounterBasedClause cb = (CounterBasedClause) cl;
        if(l < 0){
            containedNegatively.remove(cb);
        }
        else{
            containedPositively.remove(cb);
        }
    }


    @Override
    public void connectToClausePositive(@NotNull CounterBasedClause cl) {
        containedPositively.add(cl);
    }

    @Override
    public void connectToClauseNegative(@NotNull CounterBasedClause cl) {
        containedNegatively.add(cl);
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

    @Override
    public void connectToClauseL(WatchedLiteralsClause clauseW, Integer toLit) {
        if(toLit < 0){
            toAddNegative.add(clauseW);
        }
        else{
            toAddPositive.add(clauseW);
        }
    }

    @Override
    public void update() {
        ClauseNegativeWatched.addAll(toAddNegative.stream().collect(Collectors.toList()));
        ClausePositiveWatched.addAll(toAddPositive.stream().collect(Collectors.toList()));
        toAddNegative.clear();
        toAddPositive.clear();
    }
}
