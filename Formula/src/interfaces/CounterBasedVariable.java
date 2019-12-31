package interfaces;

import formula.Clause;
import org.jetbrains.annotations.NotNull;
import formula.Variable;

public interface CounterBasedVariable extends Variable{
    void removeConnections(Integer l, Clause cl);

    void connectToClausePositive(@NotNull CounterBasedClause cl);
    void connectToClauseNegative(@NotNull CounterBasedClause cl);
}
