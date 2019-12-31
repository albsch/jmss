package jmss.formula.wl;

import jmss.formula.ClauseAbs;
import jmss.specificationCore.Solver;

import java.util.List;

import static java.lang.Math.abs;

/**
 * 2016/02/15.
 */
public class ClauseWL extends ClauseAbs {
    //Watches
    private Integer moving = null;
    private int movingIndex = -1;

    private Integer fixed = null;
    private int fixedIndex = -1;

    public ClauseWL(Solver solver) {
        super(solver);
    }


    public void removeWatchesFromVariables() {
        if (size() == 1) {
            // Remove only watch
            ((VariableW) state.F().getVariable(moving)).removeWatch(this,moving);
        } else {
            ((VariableW) state.F().getVariable(moving)).removeWatch(this,moving);
            ((VariableW) state.F().getVariable(fixed)).removeWatch(this,fixed);
        }
    }

    public void connectInitialWachtes() {
        List<Integer> lits = getLiterals();

        //TODO random selection, heuristic
        if(size() == 1){
            moving = fixed = lits.get(0);
            movingIndex = fixedIndex = 0;
        }
        else{
            moving = lits.get(0);
            movingIndex = 0;
            fixed = lits.get(1);
            fixedIndex = 1;
        }

        //connect variables

        ((VariableW) state.F().getVariable(abs(moving))).connectToClause(this,moving);
        if(size()>1){
            ((VariableW) state.F().getVariable(abs(fixed))).connectToClause(this,fixed);
        }
    }

    public boolean searchNewWatch(Integer watch) {
        if(size() == 1){
            state.F().foundConflictClause(this);
            return false;
        }
        if(size() == 2){
            if(isConflicting()) state.F().foundConflictClause(this);
            if(isUnit()) state.F().foundUnitClause(this);
            return false;
        }

        if(watch.equals(fixed)) flipSearch();

        //search for new watch
        for (int i = 0; i < getLiterals().size(); i++) {
            Integer lit = getLiterals().get(i);
            //ignore watches
            if(lit.equals(fixed)) continue;
            if(lit.equals(moving)) continue;

            //case 1:literal is free
            if(state.F().isVariableUndefined(abs(lit)) ||
                    (state.M().inTrail(lit))
                    ){
                int prevVar = abs(moving);

                moving = lit;
                movingIndex = i;

                // Add moving to a queue to avoid concurrent modification
                ((VariableW) state.F().getVariable(abs(moving))).connectToClauseL(this,moving);
                if(prevVar != abs(moving)) ((VariableW) state.F().getVariable(abs(moving))).update();

//                if(isConflicting()) throw new IllegalStateException("2WL conflicting");
//                if(isUnit()) throw new IllegalStateException("2WL unit");

                //signal watch change
                return true;
            }
        }



        if(isConflicting()) {
            state.F().foundConflictClause(this);
        }
        if(isUnit()) {
            state.F().foundUnitClause(this);
        }
        return false;
    }

    private void flipSearch() {
        Integer t = moving;
        Integer ind = movingIndex;
        moving = fixed;
        movingIndex = fixedIndex;
        fixed = t;
        fixedIndex = ind;
    }


//    @Override
//    public boolean isConflicting() {
//        //not conflicting as long as moving is not false in trail
//        return state.M().inTrail(-moving);
//    }
//
//    @Override
//    public boolean isUnit() {
//        //last unassigned literal is always moving
//        return state.F().isVariableUndefined(abs(moving));
//    }

    @Override
    public boolean isUnit() {
        // If one watch is conflicting and the other free, clause is unit
        if ((state.M().inTrail(-moving))
                && (state.F().isVariableUndefined(abs(fixed)))) {
            return true;
        }
        if ((state.M().inTrail(-fixed))
                && (state.F().isVariableUndefined(abs(moving)))) {
            return true;
        }
        // If moving == fixed (clause size 1) and undefined, clause is unit
        return (size() == 1)
                && (state.F().isVariableUndefined(abs(moving)));
    }

    @Override
    public boolean isConflicting() {
        // If both watches are conflicting in M, clause is conflicting
        if ((state.M().inTrail(-moving)) && (state.M().inTrail(-fixed))) {
            return true;
        }

        // Otherwise, clause is not conflicting
        return false;
    }

    @Override
    public Integer getUnitLiteral() {
        if(state.F().isVariableUndefined(moving)) return moving;
        if(state.F().isVariableUndefined(fixed)) return fixed;
        throw new IllegalStateException("Not unit");
//        return fixed;
    }

















    @Override
    public void addLiteral(Integer lit) {

    }









    @Override
    public void assertConflictingState() {
        List<Integer> lits = getLiterals();
        if(size() == 1){
            moving = fixed = lits.get(0);
            movingIndex = fixedIndex = 0;
            ((VariableW) state.F().getVariable(abs(moving))).connectToClause(this,moving);
            return;
        }

        if(size() == 2){
            moving = lits.get(0);
            fixed = lits.get(1);
            movingIndex = 0;
            fixedIndex = 1;
            ((VariableW) state.F().getVariable(abs(moving))).connectToClause(this,moving);
            ((VariableW) state.F().getVariable(abs(fixed))).connectToClause(this,fixed);
            return;
        }

        // Size > 2: status may be affected
        // Find the 2 literals which are last assigned in trail
        int l1 = 0;
        int l2 = 1;

        // ensure bigger status
        if (state.M().isAssertedBefore(-lits.get(l2), -lits.get(l1))) {
            l2 = 0;
            l1 = 1;
        }

        for (int i = 2; i < lits.size(); i++) {
            // if new literal later than l2: set new literal as l2, l1 as l2
            if (state.M().isAssertedBefore(-lits.get(l2), -lits.get(i)
            )) {
                l1 = l2;
                l2 = i;
            } else {
                // new literal later than l1, but not l2: set new l1
                if (state.M().isAssertedBefore(-lits.get(l1), -lits.get(i))) {
                    l1 = i;
                }
                // New literal before l1, l2: do nothing
            }
        }

        // set watches and indexes
        moving = lits.get(l1);
        movingIndex = l1;
        fixed = lits.get(l2);
        fixedIndex = l2;
        ((VariableW) state.F().getVariable(abs(moving))).connectToClause(this,moving);
        ((VariableW) state.F().getVariable(abs(fixed))).connectToClause(this,fixed);
    }

    public void updateStatus() {
        throw new NullPointerException("NYI");
    }



    public String toString() {
        String ret = "";
        List<Integer> lits = getLiterals();
        for (int i = 0; i < lits.size(); i++) {
            if (i == movingIndex) {
                ret += " [" + lits.get(i) + "]";
            } else {
                if (i == fixedIndex) {
                    ret += " {" + lits.get(i) + "}";
                } else {
                    ret += " " + lits.get(i);
                }
            }
        }
        return ret;
    }
}
