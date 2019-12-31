package jmss.heuristics.restart;

import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.specificationCore.Solver;
import jmss.specificationHeuristics.RestartStrategy;

/**
 * Restart strategy with a default response to never restart.
 * 
 * @author Albert Schimpf
 *
 */
@Module(module = "heuristic-restart", name = "never")
public class RestartNever implements RestartStrategy {
	public RestartNever(@NotNull Solver solver) {}

	@Override
	public boolean shouldRestart() {
		return false;
	}

}
