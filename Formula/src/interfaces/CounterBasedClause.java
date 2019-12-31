package interfaces;

import formula.Clause;

public interface CounterBasedClause extends Clause {
    void incrementSatisfied();

    void incrementUnsatisfied();

    void decrementSatisfied();

    void decrementUnsatisfied();
}
