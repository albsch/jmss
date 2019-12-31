package jmss.core;


import jmss.converter.StrictConverter;
import jmss.discovery.Option;
import jmss.exceptions.ConvertException;
import jmss.exceptions.SolverTimeoutException;
import jmss.logger.SolverLogger;
import jmss.logger.TimeoutLogger;
import jmss.specificationCore.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static jmss.discovery.ModuleDiscovery.getModule;

/**
 * Param component connecting all different modules together to form a solver.
 *
 * @author Albert Schimpf
 *
 */
@SuppressWarnings("FieldCanBeLocal")
public class CoreDPLL implements Solver {
	private final File path;
	private State state;

	/*
	 * Observers & Loggers
	 */
	private final List<SolverListener> obs = new LinkedList<>();
	private final SolverLogger solverLogger;
	private final TimeoutLogger timeoutLogger;

	/*
	 * Strategies
	 */
	private HighlevelStrategy highlevelStrategy;

	/*
	 * Constructor
	 */
	/**
	 * Default constructor for the solver, which initializes strategies given.
	 *
	 * @param args args and parameters
	 * @param path to instance
	 * @param logProcess on/off
	 * @param timeout timeout in seconds
	 *
	 */
	public CoreDPLL(String[] args, File path, boolean logProcess, long timeout) {
		if (logProcess) {
			// Instance logger to gather additional information if enabled
			solverLogger = new SolverLogger(this);
		} else {
			solverLogger = null;
		}

		state = new StateImpl();

		this.path = path;

		timeoutLogger = new TimeoutLogger(this, timeout);

		// Instance high-level strategy, trail, and data structure
		state.setInitialF(getModule("formula", Option.parseModule("formula", args), this, args));
		state.setInitialM(getModule("trail",Option.parseModule("trail", args), this, args));
		highlevelStrategy = getModule("high-level-strategy", Option.parseModule("high-level-strategy", args), this, args);
	}


	/*
	 * PUBLIC METHODS
	 */

	/**
	 * Loads given DIMACS file and checks the internal formula after solver has
	 * been built.
	 * <p>
	 * Prints additional information, if logging is enabled.
	 * <p>
	 * Prints lines to console according to specified format in the
	 * {@code application.parameter.Param} module.
	 * <p>
	 * Throws an {@code ConvertException}, if the file does not fulfill the
	 * DIMACS format requirements.
	 * 
	 * @return true, if instance is satisfiable; false otherwise
	 * @throws ConvertException
	 *             if an error occurs during internal formula conversion
	 * @throws SolverTimeoutException
	 *             if the solver times out or is forced to time out (unchecked).
	 */
	public boolean checkCurrentInstance() throws ConvertException, SolverTimeoutException, IOException {
		StrictConverter.dimacsToSet(path, state.F());

		// Update observer on conversion finish
		getObs().forEach(SolverListener::onSetOfClausesLoaded);

		if (solverLogger != null) {
			// Print initial logging information if enabled
			System.out.println("c Loaded DIMACS instance: " + path.getName());
			solverLogger.printInitialLog();
		}

		// Get satisfiability
		boolean sat = this.getHighlevelStrategy().applyStrategy();

		if (solverLogger != null) {
			// Additional information is only printed if enabled
			solverLogger.printFinishLog();
		}

		if (sat) {
			// If satisfiable, generate model
			System.out.println("s SATISFIABLE");
			state.M().createModel();
		} else {
			// Else generate refutation
			System.out.println("s UNSATISFIABLE");
			state.M().createRefutation();
		}

		// Return satisfiability to the caller
		return sat;
	}

	/**
	 * Public method, to enable a forced solver timeout.
	 */
	@SuppressWarnings("unused")
	public void forceTimeout() {
		timeoutLogger.forceTimeout();
	}

	@Override
	public State getState() {
		return state;
	}

	/**
	 * Adds given listener to the observer list.
	 * 
	 * @param ob
	 *            solver observer
	 */
	public void addObserver(SolverListener ob) {
		getObs().add(ob);
	}

	public List<SolverListener> getObs() {
		return this.obs;
	}

	public HighlevelStrategy getHighlevelStrategy() {
		return this.highlevelStrategy;
	}
}
