package jmss.benchmarker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/** @author Sebastian Muskalla */
class Benchmarker {
    /** location of the task list file */
    private final String loc;

    /** normal console output */
    private final PrintStream console = System.out;

    /** used to redirect console output */
    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private final PrintStream ps = new PrintStream(os);

    private int error_count = 0;

    Benchmarker(String loc) { this.loc = loc; }

    void run(int runs, String[] PARAM) {
        // parse task list
        TasksParser tp = new TasksParser(loc);
        List<BenchmarkingTask> tl = tp.parse();

        List<BenchmarkingResult> rl = new LinkedList<>();

        for (BenchmarkingTask bt : tl) {
            for (int i = 0; i < runs; i++) {
                BenchmarkingResult br;

                try {
                    br = benchmark(bt, PARAM);
                } catch (Exception e) {
                    System.out.println("\""+bt.filename + "\" ? -1 " + e.getClass().getSimpleName());
                    br = null;
                }

                if (br != null) {
                    System.out.println(br);
                    rl.add(br);
                } else {
                    error_count++;
                }
            }
        }

        System.out.println("#ERRORS: " + error_count);
        long total_time = 0;

        for (BenchmarkingResult br : rl) { total_time += br.time; }
        System.out.println("TOTAL TIME: " + total_time);
    }

    private BenchmarkingResult benchmark(BenchmarkingTask bt, String[] PARAM) throws Exception {
        ArrayList<String> newArgs = new ArrayList<>(Arrays.asList(PARAM));
        newArgs.add(0,"FILENAME");
        newArgs.add(0,"-i");

        PARAM = newArgs.toArray(new String[0]);

        try {
            System.setOut(ps);

            long start = System.nanoTime();

            // change param file location
            PARAM[1] = bt.filename;
            jmss.Main.main(PARAM);

            long end = System.nanoTime();

            String res = os.toString();

            os.reset();
            System.setOut(console);

            // parse output of solver
            String[] lines = res.split("[\r?\n]+");

            Boolean result = null;
            for (String line : lines) {
                if (line.length() > 0 && line.charAt(0) == 's') {
                    if (line.equals("s SATISFIABLE")) {
                        result = true;
                        break;
                    }
                    else if (line.equals("s UNSATISFIABLE")) {
                        result = false;
                        break;
                    }
                }
            }
            if (result == null) {
                throw new RuntimeException("Could not find \"s SATISFIABLE\" or \"s UNSATISFIABLE\" in output");
            }

            if (bt.expected_result != null && !bt.expected_result.equals(result)) {
                throw new RuntimeException("Result different from expected result: expected " + bt.expected_result + ", got " + result);
            }

            return new BenchmarkingResult(bt.filename, result, 5 * (end - start));
        } finally {
            System.setOut(console);
        }
    }
}
