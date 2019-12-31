package jmss.heuristics.literalselection;


import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.discovery.ModuleDiscovery;
import jmss.discovery.Option;
import jmss.formula.SetOfClauses;
import jmss.heaps.Heap;
import jmss.scores.Activity;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationHeuristics.LiteralSelectionStrategy;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * 2010/12/29.
 * @author Albert Schimpf
 */
@Module(module = "literal-selection", name = "slis")
public class SLISLit implements SolverListener, LiteralSelectionStrategy {

    private final SetOfClauses F;
    private Activity activities;
    private Heap activityHeap;

    private List<Integer> toBump = new ArrayList<>();

    private final String[] args;
    private final Solver solver;

    public SLISLit(@NotNull Solver solver, String[] args) {
        this.F = solver.getState().F();
        solver.addObserver(this);
        this.args = args;
        this.solver = solver;
    }

    @Override
    public Integer getLiteral() {
        boolean undef;
        int literal;

        do {
            literal = unmap(activityHeap.pop());

            undef = F.isVariableUndefined(abs(literal));
        } while(!undef);


        return literal;
    }

    @Override
    public void onBacktrack(Integer l) {
        if(!activityHeap.contains(map(abs(l)))) {
            activityHeap.push(map(abs(l)));
        }
        if(!activityHeap.contains(map(-abs(l)))) {
            activityHeap.push(map(-abs(l)));
        }
    }

    @Override
    public void onSetOfClausesLoaded() {
        // Initialize variable array
        int freeVariables = solver.getState().F().getFreeVariablesCount();

        // Initialize activity class
        // map literals to a 0-n and n+1 to n*2-1 space
        activities = ModuleDiscovery.getModule("activity", Option.parseModule("activity", args), solver, args, 100.0, 1.0, freeVariables*2);

        // Initialize priority queue with custom activity comparator
        activityHeap = ModuleDiscovery.getModule("heap", Option.parseModule("heap", args), solver, args, activities, freeVariables*2);

        for (int i = 0; i < freeVariables*2; i++) {
            activityHeap.push(i);
        }

        bumpLiteralsInClause(toBump);

        assert activityHeap.size() == freeVariables*2;
        assert activities.size() == freeVariables*2;
    }


    @Override
    public void onLearnInitial(List<Integer> cl) {
        toBump.addAll(cl);
    }

    private void bumpLiteralActivity(Integer l) {
        activities.bump(map(l));

        if(activityHeap.contains(map(l)))
            activityHeap.increase(map(l));
    }


    private void bumpLiteralsInClause(List<Integer> cl) {
        for (Integer lit : cl) {
            bumpLiteralActivity(lit);
        }
    }

    private int map(Integer l) {
        int maxSize = (solver.getState().F().getVariables().length-1);

        // if negative, add to max size and shift
        if(l < 0) return maxSize + (abs(l)-1);

        // else only shift index
        return l-1;
    }

    private int unmap(Integer l) {
        int maxSize = (solver.getState().F().getVariables().length-1);

        // if over limit, remove maxSize and shift back
        if(l >= maxSize) return (abs(l)+1) - maxSize;

        // else only shift index back
        return l+1;
    }

}
