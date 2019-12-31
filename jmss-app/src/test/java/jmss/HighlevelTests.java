package jmss;

import jmss.discovery.ModuleDiscovery;
import jmss.exceptions.SolverTimeoutException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by Albert Schimpf on 05.02.16.
 */
public class HighlevelTests {
    private static List<File> satTests = new ArrayList<>();
    private static List<File> unsatTests = new ArrayList<>();
    private static final Integer TIMEOUT_SECONDS = 1;

    @BeforeClass
    public static void setUpBeforeClass() {
        Stream.of("/sat","/easySat","/BenchmarkSat","/BenchmarkHardSat").forEach(resource -> {
            try {
                Files.walk( Paths.get(HighlevelTests.class.getResource(resource).getPath()) )
                        .filter(Files::isRegularFile)
                        .forEach((f) -> satTests.add(f.toFile()));
            } catch (IOException e) { throw new RuntimeException(e); }
        });

        Stream.of("/unsat","/easyUnsat","/BenchmarkUnsat").forEach(resource -> {
            try {
                Files.walk( Paths.get(HighlevelTests.class.getResource(resource).getPath()) )
                        .filter(Files::isRegularFile)
                        .forEach((f) -> unsatTests.add(f.toFile()));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    @Test
    public void T1() {
        System.out.println(satTests.size());
        System.out.println(unsatTests.size());
    }

    @Test
    public void SatTests() throws Exception {
        int i = 0;
        int j = 0;
        int max;

        final List<List<String>> product = new LinkedList<>();

        Map<String, Map<String, Class>> allModules = ModuleDiscovery.instance.getAllModules();
        for (String module : allModules.keySet()) {
            Map<String, Class> pair = allModules.get(module);

            List<List<String>> localCopy = new LinkedList<>();
            for (List<String> strings : product) {
                localCopy.add(new LinkedList<>(strings));
            }

            List<List<String>> toFill = new LinkedList<>();

            pair.forEach((name, _c) -> {
                // for each name, append to every product
                if(localCopy.isEmpty()) {
                    List<String> args = new LinkedList<>();
                    args.add("-m " + module+ " " +name);
                    toFill.add(args);
                } else {
                    for (List<String> copiedArgs : localCopy) {
                        List<String> merged = new LinkedList<>(copiedArgs);
                        merged.add("-m " + module+ " " +name);
                        toFill.add(merged);
                    }
                }

            });

            product.clear();
            product.addAll(toFill);
        }


        max = product.size() * (satTests.size() + unsatTests.size());
        DecimalFormat df = new DecimalFormat("#.##");

        for (List<String> combination : product) {
            String[] args = normalize(combination);
            System.out.println(product.size());

            String[] argsWithFile = Arrays.copyOf(args, args.length+6);
            argsWithFile[args.length+2] = "-l";
            argsWithFile[args.length+3] = "false";
            argsWithFile[args.length+4] = "-t";
            argsWithFile[args.length+5] = ""+ TIMEOUT_SECONDS;

            for (File file : satTests) {
                double prog = ((i * 1.0) / (max * 1.0)) * 100;
                System.out.print("[PROGRESS] " + df.format(prog) + "% || " + i + "/" + max + " [" + j + " t/o]");
                System.out.println(" -- " + file.getName());
                System.out.println(combination);

                // Return satisfiability
                try {
                    argsWithFile[args.length] = "-i";
                    argsWithFile[args.length+1] = file.getAbsolutePath();
                    assertTrue(Main.run(argsWithFile));
                } catch (SolverTimeoutException e) {
                    System.out.println("Timeout nr " + (j + 1));
                    j++;
                }

                i++;
            }


            for (File file : unsatTests) {
                double prog = ((i * 1.0) / (max * 1.0)) * 100;
                System.out.print("[PROGRESS] " + df.format(prog) + "% || " + i + "/" + max + " [" + j + " t/o]");
                System.out.println(" -- " + file.getName());
                System.out.println(combination);

                // Return satisfiability
                try {
                    argsWithFile[args.length] = "-i";
                    argsWithFile[args.length + 1] = file.getAbsolutePath();
                    assertFalse(Main.run(argsWithFile));
                } catch (SolverTimeoutException e) {
                    System.out.println("Timeout nr " + (j + 1));
                    j++;
                }

                i++;
            }
        }

        double prog = ((i * 1.0) / (max * 1.0)) * 100;
        System.out.print("[PROGRESS] " + df.format(prog) + "% || " + i + "/" + max + " [" + j + " t/o]");
    }

    private String[] normalize(List<String> combination) {
        String[] args = new String[combination.size()*3];
        int i = 0;
        for (String option : combination) {
            for (String arg : option.split(" ")) {
                args[i] = arg;
                i++;
            }
        }

        return args;
    }

}
