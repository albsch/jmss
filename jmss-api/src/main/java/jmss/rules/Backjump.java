package jmss.rules;

/**
 * @author Albert Schimpf
 */
public interface Backjump {
    /**
     * Checks, if there is only one literal (UIP) in current conflict clause
     * {@code C} left.
     *
     */
    Integer guard();

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
    void apply(Integer UIP);
}
