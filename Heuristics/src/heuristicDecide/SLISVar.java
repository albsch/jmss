package heuristicDecide;


import formula.Clause;
import heaps.Heap;
import org.jetbrains.annotations.NotNull;
import formula.SetOfClauses;
import formula.Variable;
import scores.ActivitiesVariable;
import scores.Activity;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationHeuristics.VariableSelectionStrategy;
import heaps.VariableHeapIntList;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * 2016/02/02.
 */
public class SLISVar implements SolverListener,VariableSelectionStrategy {
    private final SetOfClauses F;
    public Activity activities;
    private Heap activityHeap;

    private List<Integer> toBump = new ArrayList<>();

    public SLISVar(@NotNull Solver solver) {
        this.F = solver.getState().F();
        solver.addObserver(this);
    }

    @Override
    public Variable getVariable() {
        Integer var = activityHeap.pop();

        //shift index from heap
        boolean undef = F.isVariableUndefined(var+1);
        while(!undef){
            var = activityHeap.pop();
            //shift index from heap
            undef = F.isVariableUndefined(var+1);
        }
        //shift index from heap
        return F.getVariable(var+1);
    }

    @Override
    public void onConflict(Clause cl) {
    }

    @Override
    public void onExplain(List<Integer> ante, Integer resLit, List<Integer> resolved) {
    }

    @Override
    public void onBacktrack(Integer l) {
        //shift index back
        if(!activityHeap.contains(abs(l)-1))
            activityHeap.push(abs(l)-1);
    }


    private void bumpVariableActivity(int var) {
        //shift index back
        activities.bump(var-1);

        if(activityHeap.contains(var-1)){
            activityHeap.increase(var-1);
        }
    }

    private void bumpVarsInClause(List<Integer> cl) {
        for (Integer lit : cl) {
            bumpVariableActivity(abs(lit));
        }
    }



    @Override
    public void onSetOfClausesLoaded() {
        // Initialize variable array
        Variable[] vars = F.getVariables();

        // Initialize activity class by default constructor
        activities = new ActivitiesVariable(10,1.0,vars.length);

        // Initialize priority queue with custom activity comparator
        activityHeap = new VariableHeapIntList(activities,vars.length);

        for (Integer i = 0; i < F.getFreeVariablesCount(); i++) {
            activityHeap.push(abs(i));
        }

        bumpVarsInClause(toBump);
    }

    @Override
    public void onDecide(Integer ld) {

    }

    @Override
    public void onUnitPropagate(List<Integer> cl, Integer lu) {

    }

    @Override
    public void onLearn(Clause cl) {

    }

    @Override
    public void onForget(Clause cl) {

    }

    @Override
    public void onLearnInitial(List<Integer> cl) {
        toBump.addAll(cl);
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onBackjump(Integer current, Integer bl, Integer uip) {
    }


}
