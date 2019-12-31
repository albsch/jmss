package specificationHeuristics;


/**
 * General literal selection strategy, which every literal selection heuristic
 * must implement.
 * <p>
 * Is used in the main module to get the next decision literal.
 * <p>
 * Selection heuristics may need to get information from the formula {@code F} or the
 * trail {@code M}.
 * <p>
 * If the heuristic needs to be updated after certain rule application, it needs
 * to add itself as a observer with the addObserver method in its
 * constructor.
 *
 * @author Albert Schimpf
 */
public interface LiteralSelectionStrategy {
    /**
     * Method body to select a free literal in F according to the implemented
     * heuristic. Should never return a {@code null} object.
     *
     * @return free literal
     */
    Integer getLiteral();
}