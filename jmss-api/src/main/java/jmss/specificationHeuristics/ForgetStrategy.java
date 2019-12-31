package jmss.specificationHeuristics;


import jmss.formula.Clause;

import java.util.List;

/**
 * Specification for all forget strategies.
 * <p>
 * A forget strategy has to implement a check if the solver should forget some
 * clauses and an actual forget method.
 * <p>
 * Heuristics may need to get information from the jmss.formula {@code F} or the
 * trail {@code M}.
 * <p>
 * If the heuristic needs to be updated after certain rule application, it needs
 * to add itself as a observer with the addObserver method in its
 * constructor.
 *
 * @author Albert Schimpf
 */
public interface ForgetStrategy {
    /**
     * Checks, if the solver should forget some learned clauses according to a
     * heuristic.
     *
     * @return {@code true}, if some learned clauses should be removed;
     * {@code false} otherwise
     */
    boolean shouldForget();

    /**
     * Forgets some learned clauses according to a selection heuristic. Returns
     * all clauses which were removed from the jmss.formula.
     *
     * @return removed clauses
     */
    List<Clause> forgetClauses();
}
