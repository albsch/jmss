package jmss.specificationCore;

import jmss.formula.SetOfClauses;

import java.util.List;

/**
 * A main component in the state transition system to keep track of the assigned
 * literals and decision levels during the solving process.
 * <p>
 * More specifically, the trail has to keep track of the following:
 * <ul>
 * <li>Assigned literals
 * <li>Order of assigned literals
 * <li>Antecedents of unit literals
 * <li>Decision level tracking (checkpoints)
 * <li>Ability to backtrack to any given level below the current one
 * </ul>
 * <p>
 * All functions should be implemented as efficiently as possible, as to not
 * become the bottleneck of the solving process.
 *
 * @author Albert Schimpf
 */
public interface Trail {
    /**
     * Checks if literal {@code lit} is in the current trail.
     * <p>
     * This function should cost at most O(1).
     *
     * @param lit : literal to be checked
     * @return true if {@code lit} is in trail; false otherwise
     */
    boolean inTrail(Integer lit);

    /**
     * Adds literal {@code lit} as a decision literal in the current trail.
     * Increases current decision level by 1.
     * <p>
     * The antecedent of a decision literal is always {@code null}.
     *
     * @param lit : decision literal to be added
     * @see SetOfClauses#setVariableFalse(int)
     * F.setVariableFalse
     * @see SetOfClauses#setVariableTrue(int)
     * F.setVariableTrue
     */
    void addDecisionLiteral(Integer lit);

    /**
     * Adds literal {@code lit} as a unit literal with it's antecedent {@code cl}
     * in the current trail. Does not increase current decision level.
     *
     * @param lit : unit literal to be added
     * @param cl  : antecedent of lit
     * @see SetOfClauses#setVariableFalse(int)
     * F.setVariableFalse
     * @see SetOfClauses#setVariableTrue(int)
     * F.setVariableTrue
     */
    void addUnitLiteral(Integer lit, List<Integer> cl);

    /**
     * Undoes all assignments of the current decision level and adds the
     * negation of the current decision literal after undoing the assignments.
     * Decreases current decision level by 1.
     * <p>
     * Backtrack at decision level 0 is not possible and should throw a
     * {@code Exception}.
     *
     * @see SetOfClauses#undoAssignment(int)
     * F.undoVariableAssignment
     * @see SetOfClauses#setVariableFalse(int)
     * F.setVariableFalse
     * @see SetOfClauses#setVariableTrue(int)
     * F.setVariableTrue
     */
    void backtrack();

    /**
     * Undoes all assignments until given decision level is reached and adds the
     * given literal {@code d} after undoing the assignments.
     * <p>
     * The decision level parameter is exclusive: assignments on level {@code i}
     * will not be undone.
     * <p>
     * Backjumping below level 0, to the current level, or above current level
     * is not allowed and should throw an {@code IllegalArgumentException}.
     *
     * @param i : level to backjump to (exclusive)
     * @param d : literal which will be added to the trail after backjump
     * @throws IllegalArgumentException if backjump level is not allowed
     * @see SetOfClauses#undoAssignment(int)
     * F.undoVariableAssignment
     * @see SetOfClauses#setVariableFalse(int)
     * F.setVariableFalse
     * @see SetOfClauses#setVariableTrue(int)
     * F.setVariableTrue
     */
    void backjumpToLevel(Integer i, Integer d);

    /**
     * Returns the most recent decision literal, {@code null} if there is none.
     * <p>
     * Per definition, there is only one decision literal per decision level.
     *
     * @return recent decision literal
     */
    Integer getCurrentDecisionLiteral();

    /**
     * Returns current decision level. Decision levels of literals not in trail
     * are not consistent.
     *
     * @return current decision level
     */
    Integer getCurrentDecisionLevel();

    /**
     * Returns the second biggest decision level of the literals of a given
     * clause.
     * <p>
     * Two literals with the same decision level are filtered beforehand. If
     * there is only one decision level among the literals in given clause, -1
     * is returned.
     * <p>
     * Is not checked, but all literals of given clause should be in the trail.
     *
     * @param cl ClauseAbs
     * @return Second recent decision level of clause, -1 if there is no second
     * recent
     */
    Integer getSecondRecentDecisionLevel(List<Integer> cl);

    /**
     * Resets the trail completely, undoing all assignments, inclusive decision
     * level 0 (i.e. unit assignments).
     *
     * @see SetOfClauses#undoAssignment(int)
     * F.undoVariableAssignment
     */
    void reset();

    /**
     * Returns the decision level of given literal {@code lit}. Returns -1, if
     * literal is not in trail.
     *
     * @param lit : literal
     * @return decision level of literal
     */
    Integer getDecisionLevel(Integer lit);

    /**
     * Prints the current assignment to the console. Should only be used after
     * satisfaction of a jmss.formula.
     * <p>
     * Format: v( literal)* 0
     * <p>
     * Negation is printed as '-' in front of the literal. Example model:
     * <p>
     * v 1 -2 3 -5 4 0
     */
    void createModel();

    /**
     * Checks, if a literal is asserted before another literal.
     * <p>
     * Is not checked, but both literals should be in the trail.
     *
     * @param lit1 First literal
     * @param lit2 Second BuilderLiteral
     * @return {@code true}, if lit1 is asserted before lit2; {@code false}
     * otherwise
     */
    boolean isAssertedBefore(Integer lit1, Integer lit2);

    /**
     * Returns the antecedent of the given literal, {@code null} if literal does
     * not have one.
     * <p>
     * Not checked, but literal should be in the trail.
     *
     * @param lit : literal
     * @return antecedent of (unit) literal
     */
    List<Integer> getReason(Integer lit);

    /**
     * Returns a list with all current literals sorted from end of the trail to
     * the end of the last decision level.
     *
     * @return literals in current decision level sorted from end to a
     * checkpoint
     */
    List<Integer> getCurrentDecisionLevelLiteralsFromEnd();

    /**
     * Returns a refutation proof, if clause is determined to be unsatisfiable.
     */
    void createRefutation();


}
