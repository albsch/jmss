package specificationHeuristics;


import formula.Variable;

/**
 * General variable selection strategy, which every variable selection heuristic
 * must implement.
 * <p>
 * Is used in a literal selection strategy, to decide the variable of the
 * literal to return.
 *
 * @author Albert Schimpf
 * @see LiteralSelectionStrategy
 */
public interface VariableSelectionStrategy {
    /**
     * Method body to select a free variable in F according to the implemented
     * heuristic.
     *
     * @return selected variable
     */
    Variable getVariable();
}
