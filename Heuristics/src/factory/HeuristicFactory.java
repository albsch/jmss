package factory;

import heuristicDecide.*;
import heuristicForget.*;
import heuristicLearn.LearnNever;
import heuristicLearn.LearnSimple;
import heuristicRestart.RestartConflictCountingFixed;
import heuristicRestart.RestartConflictCountingGeometric;
import heuristicRestart.RestartNever;
import specificationCore.Solver;
import specificationHeuristics.*;

/**
 * Heuristic factory.
 * Created by Albert Schimpf on 04.10.2015.
 */
public class HeuristicFactory {
    private HeuristicFactory() {
    }

    public static ForgetStrategy getForgetHeuristic(Solver solver, EForget forgetHeuristic) {
        switch (forgetHeuristic) {
            case ForgetNever:
                return new ForgetNever(solver);
            case ForgetSimple:
                return new ForgetSimple(solver);
            case ForgetRandomShort:
                return new ForgetRandomShort(solver);
            case ForgetRandomLarge:
                return new ForgetRandomLarge(solver);
            case ForgetSize:
                return new ForgetSize(solver);
            default:
                throw new IllegalArgumentException(
                        "Invalid forget strategy selected. " + forgetHeuristic);
        }
    }


    public static LearnStrategy getLearnHeuristic(Solver solver,ELearn learnHeuristic) {
        switch (learnHeuristic) {
            case LearnNever:
                return new LearnNever(solver);
            case LearnSimple:
                return new LearnSimple(solver);

            default:
                throw new IllegalArgumentException(
                        "Invalid learn strategy selected. " + learnHeuristic);
        }
    }

    public static RestartStrategy getRestartHeuristic(Solver solver,ERestart restartHeuristic) {
        switch (restartHeuristic) {
            case RestartNever:
                return new RestartNever(solver);
            case RestartGeometric:
                return new RestartConflictCountingGeometric(solver);
            case RestartFixed:
                return new RestartConflictCountingFixed(solver);
            default:
                throw new IllegalArgumentException(
                        "Invalid restart strategy selected. " + restartHeuristic);
        }
    }

    public static LiteralSelectionStrategy getLiteralSelectionHeuristic(Solver solver,EDecide literalHeuristic) {
        switch (literalHeuristic) {
            case RandomLit:
                return new RandomLit(solver);

            case SLIS:
                VariableSelectionStrategy slis = new SLISVar(solver);
                PolaritySelectionStrategy poll = new PolarityCachingPol(solver);
                return new VariablePolarityLit(solver, slis,
                        poll);

            case MiniSAT:
                VariableSelectionStrategy mini = new MSATeffective(solver);
                PolaritySelectionStrategy pol = new PolarityCachingPol(solver);
                return new VariablePolarityLit(solver, mini,
                        pol);


            default:
                throw new IllegalArgumentException(
                        "Invalid decide strategy selected. " + literalHeuristic);
        }
    }
}
