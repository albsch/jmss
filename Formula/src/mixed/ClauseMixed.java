package mixed;


import abstractImplementation.ClauseAbs;
import counterbased.ClauseCbs;
import formula.Clause;
import interfaces.CounterBasedClause;
import interfaces.WatchedLiteralsClause;
import org.w3c.dom.css.Counter;
import specificationCore.Solver;
import wl.ClauseWL;

import java.util.List;

/**
 *
 * Created by tp on 16.12.15.
 */
public class ClauseMixed extends ClauseAbs implements CounterBasedClause, WatchedLiteralsClause {
    private final Clause cl;

    /**
     * Default constructor for all 2WL clauses.
     * <p>
     * Clauses have to operate on the formula {@code F} and have to have access
     * to the trail {@code M}.
     *
     * @param solver
     *            reference to access formula {@code F} and trail {@code M}
     */
    public ClauseMixed(Solver solver, boolean big) {
        super(solver);

        if(big){
            cl = new ClauseWL(solver);
        }
        else{
            cl = new ClauseCbs(solver);
        }
    }

    @Override
    public boolean isConflicting() {
        return cl.isConflicting();
    }

    @Override
    public boolean isUnit() {
        return cl.isUnit();
    }

    @Override
    public Integer getUnitLiteral() {
        return cl.getUnitLiteral();
    }

    @Override
    public void addLiteral(Integer lit) {
        cl.addLiteral(lit);
    }

    @Override
    public void assertConflictingState() {
        cl.assertConflictingState();
    }

    @Override
    public void incrementSatisfied() {
        ((ClauseCbs) cl).incrementSatisfied();
    }

    @Override
    public void incrementUnsatisfied() {
        ((ClauseCbs) cl).incrementUnsatisfied();
    }

    @Override
    public void decrementSatisfied() {
        ((ClauseCbs) cl).decrementSatisfied();
    }

    @Override
    public void decrementUnsatisfied() {
        ((ClauseCbs) cl).decrementUnsatisfied();
    }

    @Override
    public void connectInitialWachtes() {
        ((ClauseWL) cl).connectInitialWachtes();
    }

    @Override
    public boolean searchNewWatch(Integer watch) {
        return ((ClauseWL) cl).searchNewWatch(watch);
    }

    @Override
    public void updateStatus() {
        ((ClauseWL) cl).updateStatus();
    }

    @Override
    public void removeWatchesFromVariables() {
        ((ClauseWL) cl).removeWatchesFromVariables();
    }

    @Override
    public void setLiterals(int literals){
        ((ClauseAbs) cl).setLiterals(literals);
    }

    @Override
    public int size(){
        return ((ClauseAbs) cl).size();
    }

    @Override
    public void setId(int i){
        id = i;
        ((ClauseAbs) cl).setId(i);
    }

    @Override
    public int getId(){
        return ((ClauseAbs) cl).getId();
    }

    @Override
    public List<Integer> getLiterals(){
        return state.F().getLiterals(((ClauseAbs) cl).getId());
    }

    public boolean isBig(){
        if(cl instanceof CounterBasedClause){
            return false;
        }
        return true;
    }
}
