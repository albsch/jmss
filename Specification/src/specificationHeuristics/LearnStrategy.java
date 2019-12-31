package specificationHeuristics;

import formula.Clause;

/**
 * Specification for all learn strategies.
 * <p>
 * A learn strategy has to implement a check, if the solver should learn a
 * clause.
 * <p>
 * Heuristics may need to get information from the formula {@code F} or the
 * trail {@code M}.
 * <p>
 * If the heuristic needs to be updated after certain rule application, it needs
 * to add itself as a observer with the addObserver method in its
 * constructor.
 *
 * @author Albert Schimpf
 */
public interface LearnStrategy {
    /**
     * Checks, if the solver should learn some clause according to a heuristic.
     *
     * @return {@code true}, if clause should be learned; {@code false}
     * otherwise
     */
    boolean shouldLearn();

    /**
     * Learns some clause according to the learn strategy. Returns the learned
     * clause.
     *
     * @return learned clause
     */
    Clause learnClause();
}