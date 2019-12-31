# JMSS - Java Modular SAT Solver

JMSS is a minimal zero-dependency efficient SAT solver.

This work is based on the system defined by Sava Krstic and Amit Goel, 'Architecting Solvers for SAT Modulo Theories: Nelson-Oppen with DPLL' (2007).

# JMSS

![License](https://img.shields.io/badge/license-MIT-%23373737)
![Language](https://img.shields.io/badge/language-java-blue.svg)
![Version](https://img.shields.io/badge/version-8-9cf.svg)

# JMSS - Usage

Download a runnable JAR form a tagged release.

Execute

```
   java -jar jmss-1.0.0.jar
```

for console help and usable modules. 


# JMSS - Exporter & Monitoring

If `jmss-exporter` is used instead of the `jmss` runnable jar, then metrics are exported at port `8004`.
These metrics can be used with the provided Prometheus and Grafana Docker setup:

![metrics](https://user-images.githubusercontent.com/38429047/71622767-5fe51380-2bd8-11ea-89d9-8119b8acbc20.png)
