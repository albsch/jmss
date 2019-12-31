package specificationCore;

import exceptions.ConvertException;
import exceptions.SolverTimeoutException;

import java.io.IOException;
import java.util.List;

/**
 * Solver main interface.
 * <p>
 * Manages state, observers and the main high-level function.
 * </p>
 * Created by Albert Schimpf on 15.09.2015.
 */
public interface Solver {
    List<SolverListener> getObs();

    State getState();

    void addObserver(SolverListener obs);

    HighlevelStrategy getHighlevelStrategy();

    boolean checkCurrentInstance() throws ConvertException, SolverTimeoutException, IOException;
}
