package de.unikl;

import exceptions.ConvertException;
import exceptions.SolverTimeoutException;
import factory.*;
import org.junit.BeforeClass;
import org.junit.Test;
import specificationCore.Solver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by nas on 05.02.16.
 */
@SuppressWarnings("ALL")
public class HighlevelTests {
    static List<File> satTests = new ArrayList<>();
    static List<File> unsatTests = new ArrayList<>();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Files.walk(Paths.get("test/resources/sat"))
                .filter(Files::isRegularFile)
                .forEach((f) -> satTests.add(f.toFile()));
        Files.walk(Paths.get("test/resources/easySat"))
                .filter(Files::isRegularFile)
                .forEach((f) -> satTests.add(f.toFile()));

        Files.walk(Paths.get("test/resources/unsat"))
                .filter(Files::isRegularFile)
                .forEach((f) -> unsatTests.add(f.toFile()));
        Files.walk(Paths.get("test/resources/easyUnsat"))
                .filter(Files::isRegularFile)
                .forEach((f) -> unsatTests.add(f.toFile()));

        if(Parameter.full){
            Files.walk(Paths.get("test/resources/BenchmarkSat"))
                    .filter(Files::isRegularFile)
                    .forEach((f) -> satTests.add(f.toFile()));
            Files.walk(Paths.get("test/resources/BenchmarkHardSat"))
                    .filter(Files::isRegularFile)
                    .forEach((f) -> satTests.add(f.toFile()));

            Files.walk(Paths.get("test/resources/BenchmarkUnsat"))
                    .filter(Files::isRegularFile)
                    .forEach((f) -> unsatTests.add(f.toFile()));

        }
    }

    @Test
    public void T1() {
        System.out.println(satTests.size());
        System.out.println(unsatTests.size());
    }



    @Test
    public void SatTests() throws ConvertException, IOException {
        int i = 0;
        int j = 0;

        int max = generateMax();
        for (EHighlevel hl : Parameter.HIGHLEVEL) {
            for (ESetOfClauses s : Parameter.FORMULA) {
                for (ETrail t : Parameter.TRAIL) {
                    for (EDecide d : Parameter.DECIDE) {
                        for (EForget f : Parameter.FORGET) {
                            for (ELearn l : Parameter.LEARN) {
                                for (ERestart r : ERestart.values()) {

                                    for (File file : satTests) {

                                        double prog = ((i * 1.0) / (max * 1.0)) * 100;
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        System.out.print("[PROGRESS] " + df.format(prog) + "% || " + i + "/" + max + " [" + j + " t/o]");
                                        System.out.println(" -- "+file.getName());

                                        System.out.print("["+hl+"]");
                                        System.out.print("["+s+"]");
                                        System.out.print("["+t+"]");
                                        System.out.print("["+d+"]");
                                        System.out.println();
                                        System.out.print("("+f+")");
                                        System.out.print("("+l+")");
                                        System.out.print("("+r+")");
                                        System.out.println();

                                        Solver dl = new CoreDPLL(file, Parameter.LOGGING, hl,
                                                s, d, r,
                                                l,
                                                f, "NPP", t, Parameter.TIMEOUT);

                                        // Return satisfiability
                                        try {
                                            assertTrue(dl.checkCurrentInstance());
                                        } catch (SolverTimeoutException e) {
                                            System.out.println("Timeout nr " + (j + 1));
                                            j++;
                                        }

                                        i++;
                                    }

                                    for (File file : unsatTests) {
                                        double prog = ((i * 1.0) / (max * 1.0)) * 100;
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        System.out.print("[PROGRESS] " + df.format(prog) + "% || " + i + "/" + max + " [" + j + " t/o]");
                                        System.out.println(" -- "+file.getName());

                                        System.out.print("["+hl+"]");
                                        System.out.print("["+s+"]");
                                        System.out.print("["+t+"]");
                                        System.out.print("["+d+"]");
                                        System.out.println();
                                        System.out.print("("+f+")");
                                        System.out.print("("+l+")");
                                        System.out.print("("+r+")");
                                        System.out.println();

                                        Solver dl = new CoreDPLL(file, Parameter.LOGGING, hl,
                                                s, d, r,
                                                l,
                                                f, "NPP", t, Parameter.TIMEOUT);

                                        // Return satisfiability
                                        try {
                                            boolean ass = dl.checkCurrentInstance();
                                            assertFalse(ass);
                                        } catch (SolverTimeoutException e) {
                                            System.out.println("Timeout nr " + (j + 1));
                                            j++;
                                        }

                                        i++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    private int generateMax() {
        int i = (satTests.size() +unsatTests.size())*
                Parameter.DECIDE.length*
                Parameter.FORGET.length*
                Parameter.HIGHLEVEL.length*
                Parameter.LEARN.length*
                ERestart.values().length*
                ESetOfClauses.values().length;

        return i;
    }
}
