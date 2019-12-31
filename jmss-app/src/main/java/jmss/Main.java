package jmss;

import jmss.core.CoreDPLL;
import jmss.discovery.Option;

import java.io.File;
import java.io.FileNotFoundException;

import static jmss.discovery.Option.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length == 0) {
            Option.printHelp();
            return;
        }

        if(help.parseFlag(args)) {
            Option.printHelp();
            return;
        }

        run(args);
    }

    public static boolean run(String[] args) throws Exception {
        File cnf = new File(file.parse(args)[0]);
        if(!(cnf.isFile() && cnf.exists())) throw new FileNotFoundException("Could not find cnf file " + cnf);

        CoreDPLL solver = new CoreDPLL(args, cnf, logprocess.parseBoolean(args), timeout.parseInteger(args));
        return solver.checkCurrentInstance();
    }

}
