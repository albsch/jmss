package jmss.benchmarker;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sebastian
 */
public class TasklistFromFolder
{
    public final String foldername;
    public final String output_path;
    private List<BenchmarkingTask> res;

    public TasklistFromFolder (String foldername)
    {
        this.foldername = foldername;
        this.output_path = null;
    }

    public TasklistFromFolder (String foldername, String output_path)
    {
        this.foldername = foldername;
        this.output_path = output_path;
    }

    public List<BenchmarkingTask> run ()
    {
        res = new LinkedList<>();
        File f = new File(foldername);

        if (!f.exists() || !f.isDirectory())
        {
            throw new RuntimeException("Not a valid directory path: " + foldername);
        }

        generate(f, foldername);

        if (output_path != null)
        {
            try
            {
                PrintWriter writer = new PrintWriter(output_path, "UTF-8");
                for (BenchmarkingTask bt : res)
                {
                    writer.write(bt.toString() + "\n");
                }
                writer.close();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    private void generate(File folder, String path)
    {
        File[] files = folder.listFiles();

        for (File f : files)
        {
            String name = path + "/" + f.getName();
            if (f.isDirectory())
            {
                generate(f, name);
            }
            else
            {
                res.add(new BenchmarkingTask(name, null, 0));
            }
        }
    }
}
