package jmss.logger;


import jmss.formula.Clause;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Logs all operations and counts them. Displays them at the end of the solving
 * process in the console, if enabled in the main module.
 * 
 * @author Albert Schimpf
 *
 */
public class SolverLogger implements SolverListener {
	/*
	 * Various counters
	 */
	private Integer opCount = 0;
	private Integer decideCount = 0;
	private Integer unitCount = 0;
	private Integer conflictCount = 0;
	private Integer ignoredDl = 0;
	private Integer highestDl = 1;
	private Integer resolutionCount = 0;
	private Integer learnedCount = 0;
	private Integer learnedLit = 0;
	private Integer learnedBin = 0;
	private Integer learnedTer = 0;
	private Integer forgottenCount = 0;
	private Integer restartCount = 0;
	private Integer initialClauses = 0;
	private Integer initialLit = 0;
	private Integer initialBin = 0;
	private Integer initialTer = 0;

	private long start;

	private final Map<String, String> modules = new HashMap<>();

	private long conv = 0;

	/**
	 * Default constructor of a solver logger.
	 *
	 * @param solver parameter.Param solver reference.
	 */
	public SolverLogger(Solver solver) {
		solver.addObserver(this);
		start = System.currentTimeMillis();
	}

	@Override
	public void onModuleSelect(String module, String name) {
		modules.put(module, name);
	}

	@Override
	public void onDecide(Integer ld) {
		opCount++;
		decideCount++;
	}

	@Override
	public void onUnitPropagate(List<Integer> cl, Integer lu) {
		opCount++;
		unitCount++;
	}

	@Override
	public void onConflict(Clause cl) {
		opCount++;
		conflictCount++;
	}

	@Override
	public void onBacktrack(Integer l) {
		opCount++;
	}

	@Override
	public void onExplain(List<Integer> ante, Integer resLit,
						  List<Integer> resolved) {
		opCount++;
		resolutionCount++;
	}

	@Override
	public void onLearn(Clause cl) {
		opCount++;
		learnedCount++;

		int size = cl.getLiterals().size();
		if (size == 1) {
			learnedLit++;
		}
		if (size == 2) {
			learnedBin++;
		}
		if (size == 3) {
			learnedTer++;
		}
	}

	@Override
	public void onForget(Clause cl) {
		opCount++;
		forgottenCount++;
	}

	@Override
	public void onRestart() {
		opCount++;
		restartCount++;
	}

	@Override
	public void onBackjump(Integer current, Integer bl, Integer uip) {
		opCount++;
		assert (current - (bl + 1)) >= 0;
		ignoredDl += (current - (bl + 1));
		if (current - bl > highestDl) {
			highestDl = current - bl;
		}
	}

	@Override
	public void onLearnInitial(List<Integer> cl) {
		initialClauses++;
		if (cl.size() == 1) {
			initialLit++;
		}
		if (cl.size() == 2) {
			initialBin++;
		}
		if (cl.size() == 3) {
			initialTer++;
		}
	}

	@Override
	public void onSetOfClausesLoaded() {
		long stopTime = System.currentTimeMillis();

		conv = stopTime - start;
	}

	/**
	 * Prints accumulated information after the instances satisfiability has
	 * been determined.
	 */
	public void printFinishLog() {
		System.out
				.println("c START FINISH LOG --------------------------------------");

		System.out.println("c Initial (literal) [binary] {ternary}: "
				+ initialClauses + " (" + initialLit + ")[" + initialBin + "]{"
				+ initialTer + "} "
				+ (initialClauses - initialLit - initialBin - initialTer));

		System.out.println("c Operations: " + opCount);
		System.out.println("c Decision: " + decideCount);
		System.out.println("c Unit propagations: " + unitCount);
		System.out.println("c Conflicts/Branches: " + conflictCount);
		System.out.println("c Ignored decisionlevels: " + ignoredDl);
		if (conflictCount != 0) {
			System.out
					.println("c Average (Highest) decision level jump: "
							+ Math.round(((float) (decideCount + ignoredDl) / (float) conflictCount))
							+ " (" + highestDl + ")");
		} else {
			System.out.println("c Average decision level jump: 0");
		}

		System.out.println("c Resolutions: " + resolutionCount);
		System.out.println("c Learned (literal) [binary] {ternary}: "
				+ learnedCount + "(" + learnedLit + ")[" + learnedBin + "]{"
				+ learnedTer + "}");
		System.out.println("c Forgotten clauses: " + forgottenCount);
		System.out.println("c Restarts: " + restartCount);

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - start;
		assert elapsedTime >= 0;
		System.out.println("c Time spent: " + elapsedTime + " ms"+
			"("+conv+" ms + "+(elapsedTime-conv)+" ms)"
		);

	}

	/**
	 * Prints initial information before the solving process has been started.
	 */
	public void printInitialLog() {
		System.out.println("c START OPTIONS ------------------------------------");
		modules.forEach((module, name) -> System.out.println("c "+ module+": " + name));
	}
}
