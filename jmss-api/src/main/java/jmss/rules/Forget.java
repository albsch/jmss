package jmss.rules;

/**
 * @author Albert Schimpf
 */
public interface Forget {
    /**
     * Checks, if there is at least one learned clause to forget. If forget is
     * disabled, returns always {@code false}.
     *
     * @return {@code true}, if at least one learned clause and forget enabled;
     * {@code false} otherwise
     */
    boolean guard();

    /**
     * The application of the forget rule. Should only be used after a
     * {@code forgetGuard()} check.
     * <p>
     * Forgets some learned clauses according to a forget heuristic.
     */
    void apply();
}
