package jmss.logger;


import jmss.exceptions.SolverTimeoutException;
import jmss.formula.Clause;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;

import java.util.List;

/**
 * Logger, to throw a {@code runtimeException}, if given time threshold is
 * reached.
 * 
 * @author Albert Schimpf
 *
 */
public class TimeoutLogger implements SolverListener {
	/**
	 * Starting system time.
	 */
	private long start;
	/**
	 * Internal time threshold convert to milliseconds..
	 */
	private long timeThreshold;

	/**
	 * Default constructor to save given time threshold and add the logger to
	 * the observer list.
	 * 
	 * @param solver
	 *            parameter.Param module
	 * @param timeout
	 *            Time threshold in seconds
	 */
	public TimeoutLogger(Solver solver, long timeout) {
		solver.addObserver(this);
		start = System.currentTimeMillis();
		timeThreshold = timeout * 1000;
	}

	/**
	 * Forces the solver to time out, if called by another thread.
	 */
	public void forceTimeout() {
		timeThreshold = 0;
	}

	/**
	 * Check on conflict, if time threshold is reached.
	 * 
	 * @throws SolverTimeoutException
	 *             if threshold is reached.
	 * @param cl
	 */
	@Override
	public void onConflict(Clause cl) {
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - start;
		if (elapsedTime > timeThreshold) {
			throw new SolverTimeoutException(
					"Solver timed out. Given time in seconds: " + timeThreshold
							/ 1000);
		}
	}

	/**
	 * Check on explain, if time threshold is reached.
	 * 
	 * @throws SolverTimeoutException
	 *             if threshold is reached.
	 */
	@Override
	public void onExplain(List<Integer> ante, Integer resLit,
						  List<Integer> resolved) {

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - start;
		if (elapsedTime > timeThreshold) {
			throw new SolverTimeoutException(
					"Solver timed out. Given time in seconds: " + timeThreshold
							/ 1000);
		}

	}

	/*
	 * Other responses not needed.
	 */
	@Override
	public void onDecide(Integer ld) {
	}

	@Override
	public void onUnitPropagate(List<Integer> cl, Integer lu) {
	}

	@Override
	public void onBacktrack(Integer l) {
	}

	@Override
	public void onLearn(Clause cl) {
	}

	@Override
	public void onForget(Clause cl) {
	}

	@Override
	public void onRestart() {
	}

	@Override
	public void onBackjump(Integer current, Integer bl, Integer uip) {
	}

	@Override
	public void onLearnInitial(List<Integer> cl) {
	}

	@Override
	public void onSetOfClausesLoaded() {
	}
}
