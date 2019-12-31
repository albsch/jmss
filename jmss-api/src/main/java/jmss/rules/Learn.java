package jmss.rules;

/**
 * @author Albert Schimpf
 */
public interface Learn {
    /**
     * Checks, if a conflict {@code C} is detected. If learning is disabled,
     * returns always {@code false}.
     * <p>
     * Conflicts can always be added negated to a jmss.formula, because the jmss.formula
     * entails the conflict clause.
     *
     * @return {@code true}, if conflict detected and learning enabled;
     * {@code false} otherwise
     */
    boolean guard();

    /**
     * The application of the learn rule. Should only be used after a
     * {@code learnGuard()} check.
     * <p>
     * Adds the current conflict clause {@code C} negated to the jmss.formula.
     * <p>
     * Excessive use of learn can slow down the solver. Size should be
     * controlled by a forget heuristic.
     */
    void apply();
}
