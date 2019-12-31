package jmss.specificationCore;

import jmss.exceptions.ConvertException;
import jmss.exceptions.SolverTimeoutException;

import java.io.IOException;
import java.util.List;

/**
 * Solver main interface.
 *
 * Manages state, observers and the main high-level function.
 *
 * Created by Albert Schimpf on 15.09.2015.
 */
public interface Solver {
    List<SolverListener> getObs();

    State getState();

    void addObserver(SolverListener obs);

    HighlevelStrategy getHighlevelStrategy();

    boolean checkCurrentInstance() throws ConvertException, SolverTimeoutException, IOException;
}
