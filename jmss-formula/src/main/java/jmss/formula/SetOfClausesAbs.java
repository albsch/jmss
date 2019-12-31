package jmss.formula;


import jmss.specificationCore.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;

public abstract class SetOfClausesAbs implements SetOfClauses {
    final protected Solver solver;

    final protected List<Clause> initialClauses;
    protected List<List<Integer>> clauses;

    final protected List<Clause> learnedClauses;
    protected VariableAbs[] variables;

    protected Integer freeVariables;

    protected List<Clause> lazyUnits = new ArrayList<>();
    protected List<Clause> lazyConflicts = new ArrayList<>();

    public SetOfClausesAbs(Solver solver) {
        this.solver = solver;

        this.initialClauses = new ArrayList<>();
        this.learnedClauses = new ArrayList<>();
    }

    @Override
    public Clause getConflictingClause() {
        if(lazyConflicts.isEmpty()) return null;
        if(!lazyConflicts.get(0).isConflicting()) throw new IllegalStateException("Not conflicting");

        return lazyConflicts.get(0);
    }

    @Override
    public Clause getUnitClause() {
        //search found unit clauses first
        Iterator<Clause> i = lazyUnits.iterator();
        while (i.hasNext()) {
            Clause cl = i.next();
            i.remove();
            if(cl.isUnit()) return cl;
        }

        // If no unit clause is found, return null
        return null;
    }

    @Override
    public void foundUnitClause(Clause cl){
        lazyUnits.add(cl);
    }

    @Override
    public void foundConflictClause(Clause cl){
        lazyConflicts.add(cl);
    }

    @Override
    public VariableAbs getVariable(Integer lit) {
        return getVariables()[abs(lit)];
    }

    @Override
    public void undoAssignment(Integer var) {
        // Call undo assignment directly from the variable
        getVariables()[abs(var)].undoAssignment();
    }

    @Override
    public boolean isVariableUndefined(Integer var) {
        Variable varr = getVariables()[abs(var)];
        return varr.getAssignment() == -1;
    }

    @Override
    public void setVariableTrue(Integer var) {
        // Call set true directly from the variable
        getVariables()[abs(var)].setTrue();
    }

    @Override
    public boolean isVariableTrue(Integer var) {
        if (var < 1) {// Catch negative index
            throw new IllegalArgumentException();
        }

        return getVariables()[var].getAssignment() == 1;
    }

    @Override
    public void setVariableFalse(Integer var) {
        // Call set false directly from the variable
        getVariables()[abs(var)].setFalse();
    }

    @Override
    public boolean isVariableFalse(Integer var) {
        return getVariables()[abs(var)].getAssignment() == 0;
    }

    @Override
    public List<Variable> getUnassignedVariablesList() {
        // Instance new list
        ArrayList<Variable> ret = new ArrayList<>();

        // Iterate through all variables and add unassigned ones
        VariableAbs[] vars = getVariables();
        for (int i = 1; i < vars.length; i++) {
            if (vars[i].getAssignment() == -1) {
                ret.add(vars[i]);
            }
        }

        // Return list
        return ret;
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    @Override
    public Solver getSolver() {
        return solver;
    }

    @Override
    public List<Clause> getInitialClauses() {
        return initialClauses;
    }

    @Override
    public List<Clause> getLearnedClauses() {
        return learnedClauses;
    }

    @Override
    public VariableAbs[] getVariables() {
        return variables;
    }

    @Override
    public void decreaseFreeVariable(){
        freeVariables--;
    }

    @Override
    public void increaseFreeVariable(){
        freeVariables++;
    }

    @Override
    public Integer getFreeVariablesCount(){
        return freeVariables;
    }


    /*
        Convert operations
	 */

    private ArrayList<Integer> tempLits;

    @Override
    public void clauseCount(int clauses) {
        this.clauses = new ArrayList<>(clauses);
    }

    @Override
    public void startClause() {
        tempLits = new ArrayList<>();
    }

    @Override
    public void endClause() {
        addClause(tempLits);

        for (SolverListener ob : solver.getObs()) {
            ob.onLearnInitial(tempLits);
        }
        tempLits = null;
    }

    @Override
    public void literal(int index, boolean negated) {
        Integer containCheck =index;
        if(negated) containCheck = -containCheck;
        // Do not add duplicate literals
        if (!tempLits.contains(containCheck)) {
            tempLits.add(containCheck);
        }
    }

    @Override
    public boolean foundConflict(){
        return lazyConflicts.size() > 0;
    }

    @Override
    public List<Integer> getLiterals(int id) {
        return clauses.get(id);
    }

    @Override
    public void resetConflicts() {
        lazyUnits.clear();
        lazyConflicts.clear();
    }

}
