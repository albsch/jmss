package interfaces;


import formula.Clause;

/**
 * Created by tp on 17.12.15.
 */
public interface WatchedLiteralsClause extends Clause{
    //TODO abstract initial watch selection to a heuristic
    void connectInitialWachtes();

    //TODO abstract watch selection to a heuristic
    boolean searchNewWatch(Integer watch);

    void updateStatus();

    void removeWatchesFromVariables();
}
