package jmss.rules;

/**
 * @author Albert Schimpf
 */
public interface Backtrack {
    /**
     * Checks, if there is a saved conflict and if the current decision level is
     * greater than 0 (i.e. there is at least 1 decision literal left). If both
     * conditions are fulfilled, backtrack can be applied.
     *
     * @return {@code true}, if backtrack can be applied;{@code false} otherwise
     */
    boolean guard();

    /**
     * The application of the backtrack rule. Should only be used after a
     * {@code backtrackGuard()} check.
     * <p>
     * Deletes the currently saved conflict clause and undoes all assignments of
     * the current level. After that, the negated recent
     * {@code decision literal} is added to trail {@code M}.
     */
    void apply();
}
