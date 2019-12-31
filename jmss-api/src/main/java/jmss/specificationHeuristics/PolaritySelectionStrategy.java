package jmss.specificationHeuristics;


/**
 * General polarity selection strategy, which every polarity selection heuristic
 * must implement.
 * <p>
 * Used to decide the polarity of a given variable according to a heuristic.
 * <p>
 * Should be used in a literal selection strategy, which uses separate variable
 * and polarity heuristics.
 *
 * @author Albert Schimpf
 * @see LiteralSelectionStrategy
 */
public interface PolaritySelectionStrategy {
    /**
     * Method body to select a polarity of a variable according to the
     * implemented heuristic. Returns a new BuilderLiteral with said polarity.
     *
     * @param varIndex Index of given VariableAbs
     * @return literal with selected polarity
     */
    Integer getPolarity(Integer varIndex);
}