package jmss.discovery;

import java.util.Arrays;

public enum Option {

    help("-h","--help",0,"print help" ),
    logprocess("-l","--log-process",1,"arg0 true/false: enable logging of statistics", "true"),
    timeout("-t","--timeout",1,"arg0 int: timeout of the solver", "10000"),
    file("-i","--input",1, "arg0 string: input cnf file to check satisfiability");

    private final String shortName;
    private final String longName;
    private final int expectedArguments;
    private final String[] defaultValues;

    private final String description;


    Option(String shortName, String longName, int expectedArguments, String description, String... defaultValues) {
        this.shortName = shortName;
        this.longName = longName;
        this.expectedArguments = expectedArguments;
        this.description = description;
        this.defaultValues = defaultValues;
    }

    public static String parseModule(String module, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("-m") || args[i].equalsIgnoreCase("--module")) {
                if (args[i+1].equalsIgnoreCase(module)) {
                    return args[i+2];
                }
            }
        }

        return null;
    }

    public String[] parse(String[] args) {
        return parse(args, shortName, longName, expectedArguments, defaultValues);
    }

    public static String[] parse(String[] args, String shortName, String longName, int expectedArguments, String[] defaultValues) {
        int lastFoundIndex = -1;
        for (int i = 0; i < args.length; i++) {
            if(args[i].equalsIgnoreCase(shortName)) lastFoundIndex = i;
            if(args[i].equalsIgnoreCase(longName)) lastFoundIndex = i;
        }

        if(expectedArguments == 0) return new String[0];
        // default value
        if(lastFoundIndex == -1 && defaultValues.length != 0)
            return defaultValues;

        // mandatory argument
        //noinspection ConstantConditions readability
        if(lastFoundIndex == -1 && defaultValues.length == 0)
            throw new IllegalArgumentException("Mandatory argument not found: " + longName);

        String[] result = new String[expectedArguments];
        for (int i = 0; i < expectedArguments; i++) {
            result[i] = args[lastFoundIndex + i + 1];
        }

        return result;
    }

    public static void printHelp() {
        System.out.println("=== Options");
        for (Option value : Option.values()) {
            System.out.print(value.shortName+", "+value.longName);
            for (int i = 0; i < value.expectedArguments; i++) {
                System.out.print(" arg"+i);
            }
            System.out.println();
            System.out.println("    " + value.description);
            for (int i = 0; i < value.defaultValues.length; i++) {
                System.out.println("    arg" + i+" default: "+value.defaultValues[i]);
            }
        }

        System.out.println();
        ModuleDiscovery.printHelp();
    }

    public Boolean parseFlag(String[] args) {
        assert this.expectedArguments == 0;
        return parse(args,shortName, longName, expectedArguments, defaultValues).length != 0;
    }

    public Boolean parseBoolean(String[] args) {
        assert this.expectedArguments == 1;
        return Boolean.valueOf(parse(args, shortName, longName, expectedArguments, defaultValues)[0]);
    }

    public Integer parseInteger(String[] args) {
        assert this.expectedArguments == 1;
        return Integer.valueOf(parse(args, shortName, longName, expectedArguments, defaultValues)[0]);
    }

    public String[] addArgs(String[] args, String... toAdd) {
        String[] newArgs = Arrays.copyOf(args, args.length+ toAdd.length);
        System.arraycopy(toAdd, 0, newArgs, args.length, toAdd.length);
        return newArgs;
    }

}
