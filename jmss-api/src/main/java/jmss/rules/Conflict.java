package jmss.rules;

/**
 * @author Albert Schimpf
 */
public interface Conflict {
    /**
     * Checks, if there is a conflict clause in the current formula {@code F}
     * with current assignments in trail {@code M}.
     *
     * @return {@code true}, if there is such a clause; {@code false}
     * otherwise.
     */
    boolean guard();

    /**
     * The application of the conflict rule. Should only be used after a
     * {@code conflictGuard()} check.
     * <p>
     * <p>
     * Saves the conflict clause for later access, to enable other rules which
     * need a detected conflict clause.
     */
    void apply();
}
