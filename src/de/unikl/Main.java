package de.unikl;

import exceptions.ConvertException;
import exceptions.SolverTimeoutException;
import factory.*;
import heuristicForget.ForgetSimple;
import heuristicForget.ForgetSize;
import heuristicRestart.RestartConflictCountingFixed;
import highlevelStrategy.DPLLSimple;
import specificationCore.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static factory.EForget.*;
import static factory.EHighlevel.*;


/**
 * parameter.Param black-box application for using this SAT solver framework. Solver
 * expects the file path as the first argument, other parameters can be
 * specified in any order. If no parameters are given, help will be printed to
 * the console.
 * <p>
 * If finished, the solver prints a line to console depending if the formula is
 * satisfied or not. Possible lines:
 * <ul>
 * <li>"s SATISFIABLE"
 * <li>"s UNSATISFIABLE"
 * </ul>
 * <p>
 * If satisfied, the solver additionally prints the variable assignments to
 * console according to this {@link trail.TrailSimple#createModel() format}. If
 * unsatisfiable, the solver generates a refutation proof.
 * <p>
 * If logging is enabled, the solver prints additional comment lines with
 * information above those two lines and times out after a certain amount of
 * seconds, throwing a runtime Exception.
 *
 * <ul>
 * <li>The file path. Either relative to current location or an absolute path.
 *
 * <li>Write log to console (true/false).
 *
 * <li>High-level strategy selection. Available options:
 * <ul>
 * <li>"Chaff" : {@link highlevelStrategy.Chaff Chaff}
 * <li>"DPLLExtended": {@link highlevelStrategy.ExtendedDPLL Simple DPLL} with
 * additional rules
 * <li>"DPLLSimple": {@link DPLLSimple Simple DPLL}
 * </ul>
 *
 * <li>Trail structure selection. Available options:
 * <ul>
 * <li>"TrailSimple" : {@link trail.TrailSimple Simple Trail}
 * </ul>
 *
 * <li>Data-structure selection. Available options:
 * <ul>
 * <li>"Data2WL" : {  2-Watched Literals}
 * <li>"DataCB" : {  Counter-Based}
 * </ul>
 *
 *
 * <li>Decide Heuristic. Available options:
 * <ul>
 * <li>"MiniSAT": {@link MiniSATVar Conflict prioritizing
 * variable selection} with {@link heuristics.heuristicDecide.PolarityCachingPol Polarity
 * Caching}
 * <li>"SLIS" : {@link heuristics.heuristicDecide.SLISLit Static Largest Individual Sum}
 * <li>"RandomLit": {@link heuristics.heuristicDecide.RandomLit Random BuilderLiteral Selection}
 * </ul>
 *
 * <li>Learn heuristic. Available options:
 * <ul>
 * <li>"LearnNever" : {@link heuristics.heuristicLearn.LearnNever ClauseAbs Learning} off
 * <li>"LearnSimple" : {@link heuristics.heuristicLearn.LearnSimple ClauseAbs Learning} on
 * </ul>
 *
 * <li>Forget heuristic. Available options:
 * <ul>
 * <li>"ForgetNever": {@link heuristics.heuristicForget.ForgetNever Forget} disabled
 * <li>"ForgetSimple": {@link heuristics.heuristicForget.ForgetSimple Forget} 25% learned
 * clauses
 * <li>"ForgetRandomLarge": {@link heuristics.heuristicForget.ForgetRandomLarge Forget} 50%
 * learned large clauses
 * <li>"ForgetRandomShort": {@link heuristics.heuristicForget.ForgetRandomShort Forget} 50%
 * learned small clauses
 * </ul>
 *
 *
 * <li>Restart heuristic. Available options:
 * <ul>
 * <li>"RestartNever": {@link heuristics.heuristicRestart.RestartNever Restarts} disabled
 * <li>"RFixed" + i ( {@link heuristics.heuristicRestart.RestartConflictCountingFixed
 * Fixed}):
 * <ul>
 * <li>0: Berkmin (550 conflicts)
 * <li>1: Chaff (700 conflicts)
 * <li>2: Eureka (2000 conflicts)
 * <li>3: Siege (16000 conflicts)
 * </ul>
 * <li>"RGeometric" + i (
 * {@link heuristics.heuristicRestart.RestartConflictCountingGeometric Geometric}):
 * <ul>
 * <li>0: MiniSAT (Base 100, factor 1.5) (Default)
 * </ul>
 * </ul>
 * <li>Preprocessing. Available options:
 * <ul>
 * <li>"NPP": Preprocess disabled
 * </ul>
 * </ul>
 * <ul>
 *
 * <li>Timeout. Terminates the solving process after given amount of seconds.
 * Input as an Integer. Default: 10000 seconds.
 * </ul>
 *
 */
public class Main {
    /**
     * parameter.Param method to check a given DIMACS instance. Returns satisfiable (with a
     * model in a separate file), or unsatisfiable (with a resolution proof in a
     * separate file).
     * <p>
     * First parameter should always be the path to the DIMACS instance. Other
     * parameters are optional.
     *
     * @param args
     *            as specified in {@link Param}
     * @throws FileNotFoundException
     *             if file does not exist.
     * @throws IOException
     *             if stream to the DIMACS file cannot be written to or closed.
     * @throws ConvertException
     *             if an error occurs during file conversion
     * @throws IllegalArgumentException
     *             if file is a directory.
     * @throws SolverTimeoutException
     *             if solver times out and logging is enabled
     *
     */
    public static void main(String... args) throws IOException,
            ConvertException, SolverTimeoutException {
        if (args.length == 0) {
            printHelp();
            return;
        }

        File selected = new File(args[0]);

        if (!selected.exists()) {
            throw new FileNotFoundException("File does not exist: " + selected);
        }
        if (!selected.isFile()) {
            throw new IllegalArgumentException("Must not be a directory: "
                    + selected);
        }




        // Set options (default)
        boolean logging = true;
        EHighlevel coreStrategy = Chaff;
        ESetOfClauses dataStructure = ESetOfClauses.DataCB;
        EDecide literalHeuristic = EDecide.MiniSAT;
        ERestart restartHeuristic = ERestart.RestartGeometric;
        EForget forgetHeuristic = ForgetNever;
        ELearn learnHeuristic = ELearn.LearnSimple;
        ETrail trail = ETrail.TrailEfficient;
        String preprocess = "NPP";
        long time = 1000;

        // Fetch parameters
        for (String s : args) {
            if (s.equals(args[0])) {
                continue;
            }
            try {
                time = Integer.parseInt(s);
                continue;
            } catch (NumberFormatException ignored) {}

            if(s.equals("true")){
                logging = true;
            }
            else if(s.equals("false")){
                logging = false;
            }
            else if(s.equals("Chaff")){
                coreStrategy = Chaff;
            }
            else if(s.equals("DPLLSimple")){
                coreStrategy = DPLLSimple;
            }
            else if(s.equals("DataCB")){
                dataStructure = ESetOfClauses.DataCB;
            }
            else if(s.equals("Data2WL")){
                dataStructure = ESetOfClauses.Data2WL;
            }
            else if(s.equals("DataMixed")){
                dataStructure = ESetOfClauses.DataMixed;
            }
            else if(s.equals("MiniSAT")){
                literalHeuristic = EDecide.MiniSAT;
            }
            else if(s.equals("SLIS")){
                literalHeuristic = EDecide.SLIS;
            }
            else if(s.equals("RandomLit")){
                literalHeuristic = EDecide.RandomLit;
            }
            else if(s.equals("ForgetNever")){
                forgetHeuristic = ForgetNever;
            }
            else if(s.startsWith("ForgetSimple")){
                forgetHeuristic = ForgetSimple;
                heuristicForget.ForgetSimple.THRESHOLD = Integer.valueOf(s.substring(12));
            }
            else if(s.startsWith("ForgetRandomShort")){
                forgetHeuristic = ForgetRandomShort;
                heuristicForget.ForgetRandomShort.THRESHOLD = Integer.valueOf(s.substring(17));
            }
            else if(s.startsWith("ForgetRandomLarge")){
                forgetHeuristic = ForgetRandomLarge;
                heuristicForget.ForgetRandomLarge.THRESHOLD = Integer.valueOf(s.substring(17));
            }
            else if(s.startsWith("ForgetSize")){
                forgetHeuristic = ForgetSize;
                heuristicForget.ForgetSize.PERCENT = Integer.valueOf(s.substring(10));
            }
            else if(s.equals("LearnNever")){
                learnHeuristic = ELearn.LearnNever;
            }
            else if(s.equals("LearnSimple")){
                learnHeuristic = ELearn.LearnSimple;
            }
            //FIXME Trail
            else if(s.equals("RestartNever")){
                restartHeuristic = ERestart.RestartNever;
            }
            else if(s.startsWith("RestartGeometric")){
                restartHeuristic = ERestart.RestartGeometric;
                //FIXME arguments
            }
            else if(s.startsWith("RestartFixed")){
                restartHeuristic = ERestart.RestartFixed;
                RestartConflictCountingFixed.NEXTRESTART = Integer.valueOf(s.substring(12));
                //FIXME arguments
            }
            //FIXME preprocessing
            else{
                throw new IllegalArgumentException("Argument not valid: "+s);
            }

        }

        // Instance parameter.Param module
        Solver dl = new CoreDPLL(selected, logging, coreStrategy,
                dataStructure, literalHeuristic, restartHeuristic,
                learnHeuristic,
                forgetHeuristic, preprocess, trail, time);


        // Return satisfiability
        dl.checkCurrentInstance();
    }

    private static void printHelp() {
        // TODO
        System.out.println("Console help not implemented yet.");
    }
}
