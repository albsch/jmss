package jmss.heuristics.variableselection;



import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.discovery.ModuleDiscovery;
import jmss.discovery.Option;
import jmss.formula.SetOfClauses;
import jmss.formula.Variable;
import jmss.heaps.Heap;
import jmss.scores.Activity;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationHeuristics.VariableSelectionStrategy;

import java.util.*;

import static java.lang.Math.abs;

/**
 * 2016/02/02.
 */
@Module(module = "variable-selection", name = "slis")
public class SLISVar implements SolverListener, VariableSelectionStrategy {
    private final SetOfClauses F;
    private Activity activities;
    private Heap activityHeap;

    private List<Integer> toBump = new ArrayList<>();
    private final String[] args;
    private final Solver solver;

    public SLISVar(@NotNull Solver solver, String[] args) {
        this.F = solver.getState().F();
        solver.addObserver(this);
        this.args = args;
        this.solver = solver;
    }

    @Override
    public Variable getVariable() {
        boolean undef;
        int varFormula;

        do {
            // shift index + 1 from heap to formula
            varFormula = activityHeap.pop() + 1;

            undef = F.isVariableUndefined(varFormula);
        } while(!undef);


        return F.getVariable(varFormula);
    }

    @Override
    public void onBacktrack(Integer l) {
        //shift index back - 1 to heap
        int varHeap = abs(l) - 1;

        if(!activityHeap.contains(varHeap)) {
            activityHeap.push(varHeap);
        }
    }

    @Override
    public void onSetOfClausesLoaded() {
        // Initialize variable array
        int freeVariables = solver.getState().F().getFreeVariablesCount();

        // Initialize activity class
        activities = ModuleDiscovery.getModule("activity", Option.parseModule("activity", args), solver, args, 10.0, 1.0, freeVariables);

        // Initialize priority queue with custom activity comparator
        activityHeap = ModuleDiscovery.getModule("heap", Option.parseModule("heap", args), solver, args, activities, freeVariables);

        for (int i = 0; i < freeVariables; i++) {
            activityHeap.push(i);
        }

        bumpVarsInClause(toBump);

        assert activityHeap.size() == freeVariables;
        assert activities.size() == freeVariables;
    }

    @Override
    public void onDecide(Integer ld) {
        assert activities.size() == solver.getState().F().getVariables().length - 1;
        assert activityHeap.size() <= solver.getState().F().getVariables().length - 1 && activityHeap.size() >= 0;
    }

    @Override
    public void onLearnInitial(List<Integer> cl) {
        toBump.addAll(cl);
    }

    private void bumpVariableActivity(int var) {
        //shift index back - 1 to heap
        int varHeap = var - 1;

        activities.bump(varHeap);

        if(activityHeap.contains(varHeap))
            activityHeap.increase(varHeap);
    }

    private void bumpVarsInClause(List<Integer> cl) {
        for (Integer lit : cl) {
            bumpVariableActivity(abs(lit));
        }
    }

}
