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

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * 2016/02/02.
 */
public class MSATeffective implements SolverListener,VariableSelectionStrategy {
    private final SetOfClauses F;
    public Activity activities;
    private Heap activityHeap;

    private List<Integer> toBump = new ArrayList<>();

    public MSATeffective(@NotNull Solver solver) {
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
        activities.decayAll();
        bumpVarsInClause(cl.getLiterals());
    }

    @Override
    public void onExplain(List<Integer> ante, Integer resLit, List<Integer> resolved) {
        bumpVarsInClause(resolved);
    }

    @Override
    public void onBacktrack(Integer l) {
        //activityHeap.print();

        //shift index back
        if(!activityHeap.contains(abs(l)-1)){
            activityHeap.push(abs(l)-1);
        }


        //activityHeap.print();
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



    public void onSetOfClausesLoaded() {
        // Initialize variable array
        Variable[] vars = F.getVariables();
        int size = vars.length - 1;

        // Initialize activity class by default constructor
        activities = new ActivitiesVariable(size);

        // Initialize priority queue with custom activity comparator
//        activityHeap = new VariableHeapInt(activities,size);
        activityHeap = Heap.getImpl(size, activities);

        for (Integer i = 0; i < size; i++) {
            activityHeap.push(abs(i));
        }

        bumpVarsInClause(toBump);

        //activityHeap.print();
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
