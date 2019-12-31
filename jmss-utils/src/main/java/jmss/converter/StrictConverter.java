package jmss.converter;

import jmss.exceptions.ConvertException;
import jmss.specificationCore.ConverterObserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class container for the static convert method.
 */
public final class StrictConverter {
    private StrictConverter() {}

    /**
     * Reads a DIMACS file and converts it to be able to hand it to the internal data structure.
     * <p>
     * Updates the observer accordingly.
     *
     * @param path to DIMACS file
     * @throws ConvertException if an error occurs during file conversion.
     * @throws IOException      if an error occurs during file loading
     */
    public static void dimacsToSet(File path, ConverterObserver obs)
            throws ConvertException, IOException {
        if (path == null) {
            throw new NullPointerException();
        }
        if (obs == null) {
            throw new NullPointerException();
        }
        assert path.exists() && !path.isDirectory(): "Path error: "+path.toString();

        //open file as BufferedReader, closes stream automatically after try block
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            convert(reader, obs);
        }
    }

    private static void convert(BufferedReader bufferedReader, ConverterObserver obs)
            throws IOException, ConvertException {
        /*
		 *	Example Implementation
		 */
        String curLine;
        curLine = bufferedReader.readLine();

        //read lines
        while (curLine != null) {
            curLine = curLine.trim();
            if (curLine.charAt(0) != 'c') {
                if (curLine.charAt(0) == 'p') {
                    String[] splitted = curLine.split(" ");
                    obs.clauseCount(Integer.valueOf(splitted[3]));
                    obs.variableCount(Integer.valueOf(splitted[2]));
                } else {
                    obs.startClause();

                    String[] splitted;
                    if (!curLine.contains("\t")) {
                        splitted = curLine.split(" ");
                    } else {
                        splitted = curLine.split("\\t");
                    }

                    for (String s : splitted) {
                        if (!s.equals("0")) {
                            if (s.charAt(0) == '-') {
                                int index = Integer.valueOf(s.substring(1));
                                obs.literal(index, true);
                            } else {
                                int index = Integer.valueOf(s);
                                obs.literal(index, false);
                            }
                        }
                    }

                    obs.endClause();
                }
            }
            curLine = bufferedReader.readLine();
        }
    }
}
