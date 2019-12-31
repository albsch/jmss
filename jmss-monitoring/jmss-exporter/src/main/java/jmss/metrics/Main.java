package jmss.metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import jmss.core.CoreDPLL;
import jmss.formula.Clause;
import jmss.specificationCore.SolverListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static jmss.discovery.Option.*;

public class Main implements SolverListener {
  private static final Counter opCount = Counter.build()
      .name("operations_count_total")
      .help("Number of times any operation is called")
          .labelNames("type")
          .register();

  private static final Gauge clauses = Gauge.build()
          .name("clauses_count_total")
          .labelNames("type")
          .help("Clauses count initial, learned")
          .register();

  private CoreDPLL solver;

  public static void main(String[] args) throws Exception {
    // start server
    HTTPServer server = new HTTPServer(8004);
    System.out.println("c Started metrics server at " + 8004);

    try {
      Main m = new Main();
      m.run(args);
    } finally {
      server.stop();
    }
  }

  public boolean run(String[] args) throws Exception {
    File cnf = new File(file.parse(args)[0]);
    if(!(cnf.isFile() && cnf.exists())) throw new FileNotFoundException("Could not find cnf file " + cnf);

    CoreDPLL solver = new CoreDPLL(args, cnf, logprocess.parseBoolean(args), timeout.parseInteger(args));
    this.solver = solver;
    solver.addObserver(this);

    return solver.checkCurrentInstance();
  }

  @Override
  public void onDecide(Integer ld) {
    opCount.labels("total").inc();
    opCount.labels("decide").inc();
  }

  @Override
  public void onUnitPropagate(List<Integer> cl, Integer lu) {
    opCount.labels("total").inc();
    opCount.labels("unit").inc();
  }

  @Override
  public void onConflict(Clause cl) {
    opCount.labels("total").inc();
    opCount.labels("conflict").inc();
  }

  @Override
  public void onBacktrack(Integer l) {
    opCount.labels("total").inc();
    opCount.labels("backtrack").inc();
  }

  @Override
  public void onExplain(List<Integer> ante, Integer resLit, List<Integer> resolved) {
    opCount.labels("total").inc();
    opCount.labels("explain").inc();
  }

  @Override
  public void onLearn(Clause cl) {
    opCount.labels("total").inc();
    opCount.labels("learn").inc();

    clauses.labels("learned").inc();
  }

  @Override
  public void onForget(Clause cl) {
    opCount.labels("total").inc();
    opCount.labels("forget").inc();

    clauses.labels("learned").dec();
  }

  @Override
  public void onRestart() {
    opCount.labels("total").inc();
    opCount.labels("restart").inc();
  }

  @Override
  public void onBackjump(Integer current, Integer bl, Integer uip) {
    opCount.labels("total").inc();
    opCount.labels("backjump").inc();
  }

  @Override
  public void onSetOfClausesLoaded() {
    clauses.labels("initial").inc(solver.getState().F().getInitialClauses().size());
  }

  @Override
  public void onLearnInitial(List<Integer> cl) {

  }

  @Override
  public void onModuleSelect(String module, String name) {

  }
}

