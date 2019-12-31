package jmss.benchmarker;

/**
 * Represents a benchmarking result
 */
public class BenchmarkingResult
{
    public final String filename;
    public final Boolean result;
    public final long time;

    @Override
    public String toString ()
    {
        return "\"" + filename + "\" " + result + " " + time;
    }

    public BenchmarkingResult (String filename, Boolean result, long time)
    {
        this.filename = filename;
        this.result = result;
        this.time = time;
    }
}
