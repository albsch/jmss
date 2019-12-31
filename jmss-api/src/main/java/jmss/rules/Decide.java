package jmss.rules;

import jmss.specificationHeuristics.LiteralSelectionStrategy;

/**
 * @author Albert Schimpf
 */
public interface Decide {
    /**
     * Checks, if there are free variables to assign.
     *
     * @return {@code false}, if no variables are free; {@code true} otherwise.
     */
    boolean guard();

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
    void apply();
}
