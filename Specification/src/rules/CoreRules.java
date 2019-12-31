package rules;

import formula.Clause;
import specificationCore.HighlevelStrategy;
import specificationHeuristics.ForgetStrategy;
import specificationHeuristics.LearnStrategy;
import specificationHeuristics.LiteralSelectionStrategy;
import specificationHeuristics.RestartStrategy;

/**
 * Specification of all supported rules of the state transition system
 * <p>
 * Rules can be implemented directly by a {@link HighlevelStrategy high-level
 * strategy}, or in an external {@link RuleSystem Rule System}, which can
 * be extended by the actual high-level strategy.
 * <p>
 * Rules may check guards with other conditions, which have to imply the
 * specification guard (i.e. stronger or equally strong pre-conditions).
 *
 * @author Albert Schimpf
 */
public interface CoreRules {
    /*
	 * GUARDS BASIC
	 */

    /**
     * Checks, if there are free variables to assign.
     *
     * @return {@code false}, if no variables are free; {@code true} otherwise.
     */
    boolean decideGuard();

    /**
     * Checks, if there is a unit clause in the current formula {@code F} with
     * current assignments in trail {@code M}.
     * <p>
     * The data structure that implements the formula {@code F} should support
     * fast unit detection, because unit propagation takes up most of the
     * solving process.
     * <p>
     * For efficiency reasons, the clause should be saved internally for further application.
     * </p>
     *
     * @return {@code true}, if there is such a clause; {@code false}
     * otherwise.
     */
    Clause unitGuard();

    /**
     * Checks, if there is a conflict clause in the current formula {@code F}
     * with current assignments in trail {@code M}.
     *
     * @return {@code true}, if there is such a clause; {@code false}
     * otherwise.
     */
    boolean conflictGuard();

    /**
     * Checks, if there is a saved conflict and if the current decision level is
     * greater than 0 (i.e. there is at least 1 decision literal left). If both
     * conditions are fulfilled, backtrack can be applied.
     *
     * @return {@code true}, if backtrack can be applied;{@code false} otherwise
     */
    boolean backtrackGuard();

	/*
	 * GUARDS ADVANCED
	 */

    /**
     * Checks, if resolution can be applied on conflict clause {@code C}.
     * <p>
     * Resolution can only be applied, if
     * <ul>
     * <li>at least 2 literals are in current decision level
     * <li>at least 1 literal of conflict clause {@code C} is unit literal in
     * current decision level with an antecedent
     * </ul>
     */
    boolean explainUIPGuard();

    /**
     * Checks, if there is only one literal (UIP) in current conflict clause
     * {@code C} left.
     *
     */
    Integer backjumpGuard();

    /**
     * Checks, if a conflict {@code C} is detected. If learning is disabled,
     * returns always {@code false}.
     * <p>
     * Conflicts can always be added negated to a formula, because the formula
     * entails the conflict clause.
     *
     * @return {@code true}, if conflict detected and learning enabled;
     * {@code false} otherwise
     */
    boolean learnGuard();

    /**
     * Checks, if there is at least one learned clause to forget. If forget is
     * disabled, returns always {@code false}.
     *
     * @return {@code true}, if at least one learned clause and forget enabled;
     * {@code false} otherwise
     */
    boolean forgetGuard();

    /**
     * Checks, if restart can be applied. If restart is disabled, returns always
     * {@code false}.
     * <p>
     * In theory, a restart can occur at any poInteger in the solving process. This
     * should be limited by a restart heuristic, to ensure that the solver
     * terminates.
     * <p>
     * Aggressive restarts are generally used for conflict-driven clause
     * learning strategies, and loose restarts for chronological backtracking.
     *
     * @return {@code true}, if restart can be applied and restart enabled;
     * {@code false} otherwise
     */
    boolean restartGuard();

	/*
	 * APPLICATION BASIC
	 */

    /**
     * The application of the decide rule. Should only be used after a
     * {@code decideGuard()} check.
     * <p>
     * <p>
     * Selects a literal and adds it to the trail {@code M} as a decision
     * literal, increasing the current decision level.
     * <p>
     * Selection can be done directly, or via the {@code getLiteral()} function
     * of {@link LiteralSelectionStrategy literal
     * selection strategy}.
     * <p>
     * A good decide heuristic is one of the things that contribute much to the
     * solver's efficiency and should be a focus of the solver.
     * <p>
     * It should be noted that, while a unit literal has an antecedent clause,
     * a decision literal has not. The antecedent of a decision literal is
     * always {@code null}.
     */
    void applyDecide();

    void applyUnit(Clause cl);

    /**
     * The application of the conflict rule. Should only be used after a
     * {@code conflictGuard()} check.
     * <p>
     * <p>
     * Saves the conflict clause for later access, to enable other rules which
     * need a detected conflict clause.
     */
    void applyConflict();

    /**
     * The application of the backtrack rule. Should only be used after a
     * {@code backtrackGuard()} check.
     * <p>
     * <p>
     * Deletes the currently saved conflict clause and undoes all assignments of
     * the current level. After that, the negated recent
     * {@code decision literal} is added to trail {@code M}.
     */
    void applyBacktrack();

	/*
	 * APPLICATION ADVANCED
	 */

    /**
     * The exhaustive application of the explain rule. Should only be used after
     * a {@code explainUIPGuard()} check.
     * <p>
     * Applies resolution on the conflict clause {@code C} until only one
     * literal of conflict clause {@code C} is left in current decision level.
     *
     * @return first UIP of current decision level
     */
    Integer applyExplainUIP();

    /**
     * The application of the backjump rule. Should only be used after a
     * {@code backjumpGuard()} check.
     * <p>
     * Backjumps to the second most recent decision level of {@code C}, adding
     * the negation of the UIP to the trail, with {@code C} being the antecedent
     * of the UIP. Deletes {@code C} afterwards.
     * <p>
     * It should be noted that backjump only works in conjunction with
     * resolution (conflict-directed backjumping), because it is not guaranteed
     * that a conflict clause has one UIP right after a conflict is detected.
     *
     * @param UIP of current decision level
     */
    void applyBackjump(Integer UIP);

    /**
     * The application of the learn rule. Should only be used after a
     * {@code learnGuard()} check.
     * <p>
     * Adds the current conflict clause {@code C} negated to the formula.
     * <p>
     * Excessive use of learn can slow down the solver. Size should be
     * controlled by a forget heuristic.
     */
    void applyLearn();

    /**
     * The application of the forget rule. Should only be used after a
     * {@code forgetGuard()} check.
     * <p>
     * Forgets some learned clauses according to a forget heuristic.
     */
    void applyForget();

    /**
     * The application of the restart rule. Should only be used after a
     * {@code restartGuard()} check.
     * <p>
     * Restarts the solving process. Learned clauses are kept, and the trail
     * {@code M} is reseted.
     */
    void applyRestart();

    /**
     * Returns the currently used forget heuristic.
     *
     * @return forget heuristic
     */
    ForgetStrategy getForgetStrategy();

    /**
     * Returns the currently used learn heuristic.
     *
     * @return learn heuristic
     */
    LearnStrategy getLearnStrategy();

    /**
     * Returns the currently used decide heuristic.
     *
     * @return decide heuristic
     */
    LiteralSelectionStrategy getLiteralSelectionStrategy();

    /**
     * Returns the currently used restart heuristic.
     *
     * @return restart heuristic
     */
    RestartStrategy getRestartStrategy();
}
