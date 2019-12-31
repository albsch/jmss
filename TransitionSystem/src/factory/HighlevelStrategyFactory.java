package factory;

import highlevelStrategy.Chaff;
import highlevelStrategy.DPLLSimple;
import specificationCore.HighlevelStrategy;
import specificationCore.Solver;

/**
 * Highlevel strategy factory.
 * Created by Albert Schimpf on 04.10.2015.
 */
public class HighlevelStrategyFactory {

    private HighlevelStrategyFactory(){}

    public static HighlevelStrategy getHighlevelStrategy(Solver solver, EHighlevel coreStrategy, EDecide literalHeuristic, ERestart restartHeuristic, ELearn learnHeuristic, EForget forgetHeuristic) {
        switch (coreStrategy) {
            case Chaff:
                return new Chaff(solver, literalHeuristic,
                        restartHeuristic, learnHeuristic, forgetHeuristic);
            case DPLLSimple:
                return new DPLLSimple(solver, literalHeuristic,
                        restartHeuristic, learnHeuristic, forgetHeuristic);

            default:
                throw new IllegalArgumentException(
                        "Invalid high-level strategy selected." + coreStrategy);
        }
    }
}