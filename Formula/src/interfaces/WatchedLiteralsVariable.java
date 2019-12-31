package interfaces;

import org.jetbrains.annotations.NotNull;
import formula.Variable;
import wl.ClauseWL;

public interface WatchedLiteralsVariable extends Variable {
    void connectToClause(@NotNull WatchedLiteralsClause clauseWL, Integer lit);
    void removeWatch(@NotNull WatchedLiteralsClause clauseWL, Integer lit);
    void connectToClauseL(WatchedLiteralsClause clauseWL, Integer lit);
    void update();
}
