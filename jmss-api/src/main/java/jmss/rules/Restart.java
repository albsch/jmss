package jmss.rules;

/**
 * @author Albert Schimpf
 */
public interface Restart {
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
    boolean guard();

    /**
     * The application of the restart rule. Should only be used after a
     * {@code restartGuard()} check.
     * <p>
     * Restarts the solving process. Learned clauses are kept, and the trail
     * {@code M} is reseted.
     */
    void apply();
}
