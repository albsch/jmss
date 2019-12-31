package benchmarker;


import exceptions.ConvertException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;



public class Benchmarker
{
    /**
     * location of the task list file
     */
    private final String loc;

    /**
     * normal console output
     */
    private final PrintStream console = System.out;

    /**
     * used to redirect console output
     */
    private final ByteArrayOutputStream os = new ByteArrayOutputStream();

    private final PrintStream ps = new PrintStream(os);

    private int error_count = 0;

    /**
     * show output of solver iff true
     */
    private Mode mode;

    public Benchmarker (String loc, Mode mode)
    {
        this.loc = loc;
        this.mode = mode;
    }

    public void run(int runs, String[] PARAM)
    {
        // parse task list
        TasksParser tp = new TasksParser(loc);
        List<BenchmarkingTask> tl = tp.parse();

        List<BenchmarkingResult> rl = new LinkedList<>();

        int count = runs;
//        for (int j = tl.size()-1; j >= 0;j--) {
        for (int j =0; j <  tl.size();j++) {
            BenchmarkingTask bt = tl.get(j);
            for (int i = 0; i < count; i++) {
                BenchmarkingResult br;

                try
                {
                    System.out.print("["+bt.filename+"]");
                    br = benchmark(bt,PARAM);
                }
                catch (Exception e)
                {
                    System.out.println("TIMEOUT solving " + bt.filename + e.toString());
                    br = null;
                }

                if (br != null)
                {
                    System.out.println(br);
                    rl.add(br);
                }
                else
                {
                    error_count++;

                }


            }
        }




        System.out.println("#ERRORS: " + error_count);
        long total_time = 0;

        for (BenchmarkingResult br : rl)
        {
           // System.out.println(br);
            total_time += br.time;
        }
        System.out.println("TOTAL TIME: " + total_time);
    }

    public BenchmarkingResult benchmark(BenchmarkingTask bt, String[] PARAM)
            throws IOException, ConvertException
    {
        try
        {


            System.setOut(ps);

            long start = System.nanoTime();

            if (bt.timeout == 0)
            {
                // default timeout
                de.unikl.Main.main(bt.filename, PARAM[0], PARAM[1],PARAM[2], PARAM[3],PARAM[4],PARAM[5]);

            }
            else
            {
                // custom timeout

                de.unikl.Main.main(bt.filename, PARAM[0], PARAM[1],PARAM[2], PARAM[3],PARAM[4],PARAM[5]);
                //parameter.Param.main(bt.filename, String.valueOf(bt.timeout));
            }

            long end = System.nanoTime();

            String res = os.toString();

            os.reset();
            System.setOut(console);

            if (mode == Mode.DEBUG)
            {
                System.out.println(res);
            }

            String lines[] = res.split("[\r?\n]+");

            Boolean result = null;
            for (String line : lines)
            {
                if (line.length() > 0 && line.charAt(0) == 's')
                {
                    if (line.equals("s SATISFIABLE"))
                    {
                        result = true;
                        break;
                    }
                    else if (line.equals("s UNSATISFIABLE"))
                    {
                        result = false;
                        break;
                    }
                }
            }
            if (result == null)
            {
                throw new RuntimeException("Could not find \"s SATISFIABLE\" or \"s UNSATISFIABLE\" in output");
            }

            if (bt.expected_result != null && !bt.expected_result.equals(result))
            {
                throw new RuntimeException("Result different from expected result: expected " + bt.expected_result + ", got " + result);
            }

            if (mode != Mode.ALBERT)
            {
                return new BenchmarkingResult(bt.filename, result, end - start);
            }
            else
            {
                return new BenchmarkingResult(bt.filename, result, 5 * (end - start));
            }
        }
        finally
        {
            System.setOut(console);
        }
    }
}
