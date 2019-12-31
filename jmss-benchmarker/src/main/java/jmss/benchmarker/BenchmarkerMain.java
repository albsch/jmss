package jmss.benchmarker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class BenchmarkerMain {
    private static final String DEFAULT_BENCHMARKS_LOCATION = "res/benchmark-instances/all";
    private static final String DEFAULT_TASKLIST_GEN_LOCATION = "res/benchmark_tasklist_gen.txt";
    private static final int RUNS = 10;

    private final String[] PARAM = new String[]{
        "--timeout", "2",
        "-m", "trail", "stack-and-sequence",
        "-m", "high-level-strategy", "chaff",
        "-m", "formula", "counter-based",
        "-m", "heuristic-forget", "forget-large-random",
        "-m", "heuristic-restart", "conflicts-geometric",
        "-m", "literal-selection", "var-pol-random",
        "-m", "variable-polarity-selection", "cache-polarity",
        "-m", "variable-selection", "minisat"
    };


    public static void main (String[] args) {
        BenchmarkerMain b = new BenchmarkerMain();
        b.test();
    }

    private void test() {
        TasklistFromFolder tlff = new TasklistFromFolder(DEFAULT_BENCHMARKS_LOCATION, DEFAULT_TASKLIST_GEN_LOCATION);
        tlff.run();

        String loc = DEFAULT_TASKLIST_GEN_LOCATION;

        //JVM startup RUNS without console
        System.out.println("Starting JVM warmup runs...");
        PrintStream oldStream = System.out;
        try {
            System.setOut(new PrintStream(new OutputStream() { @Override public void write(int arg0){}}));
            Benchmarker bm = new Benchmarker(loc);
            bm.run(1, PARAM);
        } finally {
            System.setOut(oldStream);
            System.out.println("Finished JVM warmup runs...");
        }

        //1:: default values
        System.out.print(RUNS +"xBENCHMARKING");
        for (String s : PARAM) { System.out.print(" "+s); }
        System.out.println(";");


        Benchmarker bm = new Benchmarker(loc);
        bm.run(RUNS, PARAM);
    }
}
