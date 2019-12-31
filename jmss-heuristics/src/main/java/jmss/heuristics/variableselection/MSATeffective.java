package jmss.heuristics.variableselection;

import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.discovery.ModuleDiscovery;
import jmss.discovery.Option;
import jmss.formula.Clause;
import jmss.formula.SetOfClauses;
import jmss.formula.Variable;
import jmss.heaps.Heap;
import jmss.scores.Activity;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationHeuristics.VariableSelectionStrategy;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * 2016/02/02.
 */
@Module(module = "variable-selection", name = "minisat", defaultModule = true)
public class MSATeffective implements SolverListener, VariableSelectionStrategy {
    private final SetOfClauses F;
    private Activity activities;
    private Heap activityHeap;

    private List<Integer> toBump = new ArrayList<>();

    private final String[] args;
    private final Solver solver;

    public MSATeffective(@NotNull Solver solver, String[] args) {
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
        //shift index back
        int varHeap = abs(l) - 1;

        if(!activityHeap.contains(varHeap))
            activityHeap.push(varHeap);
    }

    @Override
    public void onSetOfClausesLoaded() {
        // Initialize variable array
        int freeVariables = solver.getState().F().getFreeVariablesCount();

        // Initialize activity class with decay
        activities = ModuleDiscovery.getModule("activity", Option.parseModule("activity", args), solver, args, 100.0, 0.95, freeVariables);

        // Initialize priority queue with custom activity comparator
        activityHeap = ModuleDiscovery.getModule("heap", Option.parseModule("heap", args), solver, args, activities, freeVariables);

        for (int i = 0; i < freeVariables; i++) {
            activityHeap.push(i);
        }

        bumpVarsInClause(toBump);
    }

    @Override
    public void onLearnInitial(List<Integer> cl) {
        toBump.addAll(cl);
    }


    private void bumpVariableActivity(int var) {
        //shift index back
        int varHeap = var - 1;

        activities.bump(varHeap);

        if(activityHeap.contains(varHeap)){
            activityHeap.increase(varHeap);
        }
    }

    private void bumpVarsInClause(List<Integer> cl) {
        for (Integer lit : cl) {
            bumpVariableActivity(abs(lit));
        }
    }

}
