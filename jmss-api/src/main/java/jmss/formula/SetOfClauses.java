package jmss.formula;

import jmss.specificationCore.ConverterObserver;
import jmss.specificationCore.Solver;

import java.util.*;

import static java.lang.Math.abs;


public interface SetOfClauses extends ConverterObserver {

    Clause getUnitClause();
    Clause getConflictingClause();
    Variable getVariable(Integer lit);

    void undoAssignment(Integer varIndex);
    void setVariableTrue(Integer varIndex);
    void setVariableFalse(Integer varIndex);

    boolean isVariableUndefined(Integer varIndex);
    boolean isVariableTrue(Integer varIndex);
    boolean isVariableFalse(Integer varIndex);

    List<Variable> getUnassignedVariablesList();

    void addClause(List<Integer> cl);
    Clause addLearnedClause(List<Integer> cl);
    void forgetLearnedClause(Clause cl);
    void removeClauseConnection(Clause cl);

	/*
     * Mandatory Getters
	 */

    Variable[] getVariables();

    Solver getSolver();

    List<Clause> getInitialClauses();
    List<Clause> getLearnedClauses();

    List<Integer> getLiterals(int clauseId);

    //store unit clauses lazily
    void foundUnitClause(Clause cl);
    void foundConflictClause(Clause cl);
    void resetConflicts();

    boolean foundConflict();

    void decreaseFreeVariable();
    void increaseFreeVariable();
    Integer getFreeVariablesCount();

    static List<Integer> generalResolve(List<Integer> cl, List<Integer> ante, Integer res){
//            System.out.println("B/efore "+cl+ " "+ante);

            // Instance new list for resolved clause
            List<Integer> resolventLits = new ArrayList<>();

            // Go through all literals of this clause and filter
            // If not resolved variable and not already contained

            // If negated lit, add new negated lit; else add new positive
            // lit
            cl.stream().filter(l -> !(abs(l) == (res))).forEach(resolventLits::add);

            // Go through all literals of antecedent and filter
            // If not resolved variable and not already contained
            // If negated lit, add new negated lit; else add new positive
            // lit
//            resolventLits.addAll(ante.stream().filter(lit -> abs(lit) != res).collect(Collectors.toList()));
            ante.stream().filter(l ->
                            !(abs(l) == (res)) &&
                            !resolventLits.contains(l)).forEach(resolventLits::add);

            if (resolventLits.size() == 0) {
                System.err.println("ERR Resolved empty clause");
                throw new NullPointerException("Resolved empty clause.");
            }

            // Return resolved literals
            return resolventLits;
    }



}
