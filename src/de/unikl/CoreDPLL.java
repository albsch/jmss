package de.unikl;

import dimacsConverter.StrictConverter;
import exceptions.ConvertException;
import exceptions.SolverTimeoutException;
import factory.*;
import logger.SolverLogger;
import logger.TimeoutLogger;
import org.jetbrains.annotations.NotNull;
import rules.*;
import specificationCore.*;
import specificationHeuristics.LiteralSelectionStrategy;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * parameter.Param component connecting all different modules together to form a solver.
 * 
 * <p>
 * Necessary minimum modules for correct solver behavior are:
 * <ul>
 * <li> {@link Trail Trail} implementation
 * <li> {@link SetOfClauses Formula} full
 * implementation or formula based on {@link SetOfClauses Abstract formula}
 * <li>Complete and correct strategy (i.e.
 * {@link highlevelStrategy.ExtendedDPLL Classic DPLL}) extending a
 * corresponding {@link CoreRules rule system} or implementing
 * the rules itself.
 * </ul>
 * <p>
 * Additions to the core solver can include:
 * <ul>
 * <li>More strategy with new rule system (i.e. {@link highlevelStrategy.Chaff
 * Conflict-driven non-chronological backtracking})
 * <li>Efficient data structures (i.e.
 * {@link SetOfClausesWL 2 Watched Literals})
 * <li>Heuristics for rule application (i.e. good decide heuristics)
 * <li>Restarts
 * <li>ClauseAbs learning and forgetting
 * </ul>
 * 
 * @author Albert Schimpf
 *
 */
public class CoreDPLL implements Solver {
	private final File path;
	protected State state;

	/*
	 * Observers & Loggers
	 */
	private final List<SolverListener> obs;
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
	 * @param path
	 *            to instance
	 * @param logProcess
	 *            on/off
	 * @param coreStrategy
	 *            High-level strategy
	 * @param dataStructure
	 *            Data structure
	 * @param literalHeuristic
	 *            Decide heuristic
	 * @param forgetHeuristic
	 *            Forget heuristic
	 * @param learnHeuristic
	 *            Learn heuristic
	 * @param restartHeuristic
	 *            Restart heuristic
	 * @param trail
	 *            data structure for trail interface
	 * @param preprocess
	 *            Preprocess strategy
	 * @param timeout
	 *            timeout in seconds
	 *
	 */
	public CoreDPLL(@NotNull File path, boolean logProcess,
					@NotNull EHighlevel coreStrategy, @NotNull ESetOfClauses dataStructure,
					@NotNull EDecide literalHeuristic, @NotNull ERestart restartHeuristic,
					@NotNull ELearn learnHeuristic, @NotNull EForget forgetHeuristic,
					@NotNull String preprocess, @NotNull ETrail trail, long timeout) {
		obs = new LinkedList<>();
		state = new StateImpl();

		this.path = path;

		timeoutLogger = new TimeoutLogger(this, timeout);

		// Instance high-level strategy, trail, and select data structure and
		// preprocessing strategy; pass heuristic parameters to strategies
		selectPreprocessStrategy(preprocess);
		selectDataStrategy(dataStructure);
		selectTrailStrategy(trail);



		instanceStrategy(coreStrategy, literalHeuristic, restartHeuristic,
				learnHeuristic, forgetHeuristic);

		if (logProcess) {
			// Instance logger to gather additional information if enabled
			solverLogger = new SolverLogger(this);
		} else {
			solverLogger = null;
		}
	}

	public CoreDPLL(File path){
		this(path,true,EHighlevel.Chaff,ESetOfClauses.DataCB,
				EDecide.MiniSAT,ERestart.RestartGeometric,
				ELearn.LearnSimple,EForget.ForgetNever,"NPP",
				ETrail.TrailEfficient,1000);
	}



	/*
	 * HIGHLEVEL STRATEGY
	 */

	/**
	 * Instances given high-level strategy.
	 * 
	 * @param coreStrategy
	 *            High-level strategy
	 * @param literalHeuristic
	 *            Decide heuristic
	 * @param forgetHeuristic
	 *            Forget heuristic
	 * @param learnHeuristic
	 *            Learn heuristic
	 * @param restartHeuristic
	 *            Restart heuristic
	 *
	 */
	private void instanceStrategy(EHighlevel coreStrategy, EDecide literalHeuristic,
								  ERestart restartHeuristic, ELearn learnHeuristic,
								  EForget forgetHeuristic) {
		highlevelStrategy = HighlevelStrategyFactory.getHighlevelStrategy(this, coreStrategy,literalHeuristic,restartHeuristic,learnHeuristic,forgetHeuristic);
	}

	/**
	 * Instances given data structure strategy.
	 * 
	 * @param dataStructure
	 *            Data structure
	 *
	 */
	private void selectDataStrategy(ESetOfClauses dataStructure) {
		state.setInitialF(DataStrategyFactory.getDataStrategy(this,dataStructure));
	}

	/**
	 * Instances given trail strategy.
	 * 
	 * @param trail
	 *            data structure for trail interface

	 */
	private void selectTrailStrategy(ETrail trail) {
		state.setInitialM(TrailFactory.getTrail(this,trail));
	}

	/**
	 * Instance given preprocess strategy.
	 * 
	 * @param preprocess
	 *            Preprocess strategy
	 *
	 */
	private void selectPreprocessStrategy(String preprocess) {
		switch (preprocess) {
		case "NPP":
			break;
		default:
			throw new IllegalArgumentException(
					"Invalid preprocess strategy selected. " + preprocess);
		}
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
		// TODO preprocess
//		state.getF().DimacsToSet(path);

		StrictConverter.dimacsToSet(path, state.F());
//		StrictConverter.dimacsToSet();


		// Update observer on conversion finish
		getObs().forEach(SolverListener::onSetOfClausesLoaded);

		if (solverLogger != null) {
			// Print initial logging information if enabled
			System.out.println("c Loaded DIMACS instance: " + path.getName());
			solverLogger.printInitialLog();
		}

		// Get satisfiability
//		assert (F != null) && (M != null);
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
	public void addObserver(@NotNull SolverListener ob) {
		getObs().add(ob);
	}

//	/**
//	 * Removes given listener from the observer list, if contained. Does
//	 * nothing, if given object is not contained in the list.
//	 *
//	 * @param observer
//	 *            to be removed
//	 */
//	public void removeObserver(@NotNull SolverListener observer) {
//		if (getObs().contains(this))
//			getObs().remove(observer);
//	}

	public List<SolverListener> getObs() {
		return this.obs;
	}

//	public SolverLogger getSolverLogger() {
//		return this.solverLogger;
//	}
//
//	public TimeoutLogger getTimeoutLogger() {
//		return this.timeoutLogger;
//	}

	public HighlevelStrategy getHighlevelStrategy() {
		return this.highlevelStrategy;
	}
}
