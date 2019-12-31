package benchmarker;

public class BenchmarkerMain
{
    private String name;
    public String DEFAULT_TASKLIST_LOCATION = "ressources/"+name+".txt";
    public String DEFAULT_BENCHMARKS_LOCATION = "ressources/benchmarks/"+name;
    public String DEFAULT_TASKLIST_GEN_LOCATION = "ressources/"+name+"gen.txt";

//    public final String DEFAULT_TASKLIST_LOCATION = name+".txt";
//    public final String DEFAULT_BENCHMARKS_LOCATION = "benchmarks/"+name;
//    public final String DEFAULT_TASKLIST_GEN_LOCATION = name+"gen.txt";
    public final String[] PARAM = new String[]{"1000","Chaff","DataCB","ForgetNever","RestartGeometric","MiniSAT"};
    public int runs = 50;

    public BenchmarkerMain(String arg) {
        System.out.println(arg);
        name = arg;
        DEFAULT_TASKLIST_LOCATION =  "ressources/"+name+".txt";
        DEFAULT_BENCHMARKS_LOCATION =  "ressources/"+"benchmarks/"+name;
        DEFAULT_TASKLIST_GEN_LOCATION =  "ressources/"+name+"gen.txt";
    }

    /**
     * @param args pass path to custom task list location as first parameter to use a ... custom task list location (surprise!)
     */
    public static void main (String[] args)
    {
        BenchmarkerMain b = new BenchmarkerMain(args[0]);
        b.test();
    }

    private void test() {
        System.out.println(name);
        String loc = DEFAULT_TASKLIST_LOCATION;

        TasklistFromFolder tlff = new TasklistFromFolder(DEFAULT_BENCHMARKS_LOCATION, DEFAULT_TASKLIST_GEN_LOCATION);
        tlff.run();

        loc = DEFAULT_TASKLIST_GEN_LOCATION;

        //JVM startup runs
        Benchmarker bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //1:: NCB standard
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs,PARAM);

        //2:: 2WL standard
        PARAM[1] = "Chaff";
        PARAM[2] = "Data2WL";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //3:: Mixed standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataMixed";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //4:: SLIS standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataCB";
        PARAM[5] = "SLIS";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //5:: RN standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataCB";
        PARAM[4] = "RestartNever";
        PARAM[5] = "MiniSAT";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //5:: Rfixed700 standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataCB";
        PARAM[4] = "RestartFixed700";
        PARAM[5] = "MiniSAT";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //6:: Rfixed2000 standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataCB";
        PARAM[4] = "RestartFixed2000";
        PARAM[5] = "MiniSAT";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //7:: FSim1000 standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataCB";
        PARAM[3] = "ForgetSimple1000";
        PARAM[4] = "RestartGeometric";
        PARAM[5] = "MiniSAT";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //8:: FL1000 standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataCB";
        PARAM[3] = "ForgetRandomLarge1000";
        PARAM[4] = "RestartGeometric";
        PARAM[5] = "MiniSAT";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //9:: FSh1000 standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataCB";
        PARAM[3] = "ForgetRandomShort1000";
        PARAM[4] = "RestartGeometric";
        PARAM[5] = "MiniSAT";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //10:: FSize100 standard
        PARAM[1] = "Chaff";
        PARAM[2] = "DataCB";
        PARAM[3] = "ForgetSize100";
        PARAM[4] = "RestartGeometric";
        PARAM[5] = "MiniSAT";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

        //1:: CB standard
        PARAM[1] = "DPLLSimple";
        PARAM[2] = "DataCB";
        PARAM[3] = "ForgetNever";
        PARAM[4] = "RestartGeometric";
        PARAM[5] = "MiniSAT";
        System.out.print(runs+"xBENCHMARKING ");
        for (String s : PARAM) {
            System.out.print(s+" -- ");
        }
        System.out.println(";");
        bm = new Benchmarker(loc, Mode.DEFAULT);
        bm.run(runs, PARAM);

    }
}
