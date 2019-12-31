package jmss.rules;

/**
 * @author Albert Schimpf
 */
public interface Explain {

    /**
     * Checks if resolution can be applied on conflict clause {@code C}.
     * <p>
     * Resolution can only be applied if
     * <ul>
     * <li>at least 2 literals are in current decision level
     * <li>at least 1 literal of conflict clause {@code C} is unit literal in
     * current decision level with an antecedent
     * </ul>
     */
    boolean guard();

    /**
     * The exhaustive application of the explain rule. Should only be used after
     * a {@code explainUIPGuard()} check.
     * <p>
     * Applies resolution on the conflict clause {@code C} until only one
     * literal of conflict clause {@code C} is left in current decision level.
     *
     * @return first UIP of current decision level
     */
    Integer apply();
}
