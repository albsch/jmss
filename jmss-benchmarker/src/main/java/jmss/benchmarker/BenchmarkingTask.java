package jmss.benchmarker;

/**
 * Represents a benchmarking task
 */
public class BenchmarkingTask
{
    public final String filename;
    public final Boolean expected_result;
    public final long timeout;

    @Override
    public String toString ()
    {
        return "\"" + filename + "\" " + (expected_result == null ? "?" : expected_result) + " " + timeout;
    }

    public BenchmarkingTask (String filename, Boolean expected_result, long timeout)
    {
        this.filename = filename;
        this.expected_result = expected_result;
        this.timeout = timeout;
    }
}
