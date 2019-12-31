package benchmarker;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sebastian
 */
public class TasksParser
{
    public final String filename;
    private File f;
    private FileReader fr;
    private BufferedReader br;
    private List<BenchmarkingTask> res = new LinkedList<>();
    private String line;

    public TasksParser (String filename)
    {
        this.filename = filename;
    }

    public List<BenchmarkingTask> parse ()
    {
        f = new File(filename);
        if (!f.exists())
        {
            throw new RuntimeException("File does not exist: " + f.getAbsolutePath());
        }

        try
        {
            fr = new FileReader(f);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("File does not exist: " + f.getAbsolutePath());
        }
        br = new BufferedReader(fr);

        while (true)
        {
            try
            {
                line = br.readLine();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e.getMessage(), e);
            }

            if (line == null)
            {
                return res;
            }

            // skip empty lines
            if (line.length() == 0)
            {
                continue;
            }

            String[] splits = line.split("\"");

            if (splits.length != 3)
            {
                throw new RuntimeException("Input format should be \"PATH_TO_FILE\" true/false/? TIMEOUT");
            }

            String cnf_path = splits[1];
            String[] splits2 = splits[2].split(" ");

            if (splits2.length != 3)
            {
                throw new RuntimeException("Input format should be \"PATH_TO_FILE\" true/false/? TIMEOUT");
            }

            Boolean expected_result;

            switch (splits2[1])
            {
                case "true":
                {
                    expected_result = true;
                    break;
                }
                case "false":
                {
                    expected_result = false;
                    break;
                }
                case "?":
                {
                    expected_result = null;
                    break;
                }
                default:
                {
                    throw new RuntimeException("Input format should be \"PATH_TO_FILE\" true/false/? TIMEOUT");
                }
            }

            int time = Integer.parseInt(splits2[2]);

            res.add(new BenchmarkingTask(cnf_path, expected_result, time));
        }
    }
}
