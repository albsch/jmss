package specificationCore;

/**
 * Observer interface to observe the conversion process.
 * <p>
 * Created by Albert Schimpf on 10.09.2015.
 */
public interface ConverterObserver {
    /**
     * Clause count of the Formula.
     *
     * @param clauses
     */
    void clauseCount(int clauses);

    /**
     * Variable count of the Formula.
     *
     * @param variables
     */
    void variableCount(int variables);

    /**
     * Indicates the start of a new Clause.
     */
    void startClause();

    /**
     * Closes the last open Clause.
     */
    void endClause();

    /**
     * Indicates a new Literal in last open Clause.
     *
     * @param index of Literal
     * @param negated status of Literal
     */
    void literal(int index, boolean negated);
}
