package ruleSystem;


import factory.*;
import formula.Clause;
import org.jetbrains.annotations.NotNull;
import rules.*;
import specificationCore.Solver;
import specificationCore.SolverListener;
import specificationHeuristics.ForgetStrategy;
import specificationHeuristics.LearnStrategy;
import specificationHeuristics.LiteralSelectionStrategy;
import specificationHeuristics.RestartStrategy;


/**
 * A rule system which can be extended by an actual high-level strategy, if the
 * strategy does not want to implement the rules directly.
 * <p>
 * All currently supported rules are implemented in this module.
 *
 * @author Albert Schimpf
 * @see CoreRules Core Rules
 */
public abstract class RuleSystem implements CoreRules {
    /**
     * parameter.Param module reference.
     */
    private final @NotNull Solver solver;

	/*
     * External Rules - Basic
	 */
    /**
     * External decide rule.
     *
     * @see RuleDecide Decide
     */
    private final RDecide rDecide;
    /**
     * External unit rule.
     *
     * @see RuleUnit Unit
     */
    private final RUnit rUnit;
    /**
     * External conflict rule.
     *
     * @see RuleConflict Conflict
     */
    private final RConflict rConflict;
    /**
     * External backtrack rule.
     *
     * @see RuleBacktrack Backtrack
     */
    private final RBacktrack rBacktrack;

	/*
	 * External Rules - Advanced
	 */
    /**
     * External backjump rule.
     *
     * @see RuleBackjump Backjump
     */
    private final RBackjump rBackjump;
    /**
     * External explain rule.
     *
     * @see RuleExplain Explain
     */
    private final RExplainEfficient rExplain;
    /**
     * External forget rule.
     *
     * @see RuleForget Forget
     */
    private final RForget rForget;
    /**
     * External learn rule.
     *
     * @see RuleLearn Learn
     */
    private final RLearn rLearn;
    /**
     * External restart rule.
     *
     * @see RuleRestart Restart
     */
    private final RRestart rRestart;

	/*
	 * Strategies
	 */
    /**
     * Returns a decision literal via a given selected heuristic every time a
     * decide rule is applied.
     *
     * @see LiteralSelectionStrategy
     */
    private final @NotNull LiteralSelectionStrategy literalSelectionStrategy;
    /**
     * Indicates, whether the solver should restart or not via a given restart
     * heuristic.
     *
     * @see RestartStrategy
     */
    private final @NotNull RestartStrategy restartStrategy;
    /**
     * Indicates, when to learn clauses.
     *
     * @see LearnStrategy
     */
    private final @NotNull LearnStrategy learnStrategy;
    /**
     * Indicates, what learned clauses the solver should forget via a given
     * forget heuristic
     *
     * @see ForgetStrategy
     */
    private final @NotNull ForgetStrategy forgetStrategy;

    public RuleSystem(@NotNull Solver solver,
                      @NotNull EDecide literalHeuristic, @NotNull ERestart restartHeuristic,
                      @NotNull ELearn learnHeuristic, @NotNull EForget forgetHeuristic) {
        this.solver = solver;

		/*
		 * Instance heuristics
		 */

        forgetStrategy = HeuristicFactory.getForgetHeuristic(solver,forgetHeuristic);

        learnStrategy = HeuristicFactory.getLearnHeuristic(solver, learnHeuristic);

        restartStrategy = HeuristicFactory.getRestartHeuristic(solver, restartHeuristic);

        literalSelectionStrategy = HeuristicFactory.getLiteralSelectionHeuristic(solver,literalHeuristic);

		/*
		 * External Rules - Basic
		 */
        rDecide = new RDecide(solver, this);
        rUnit = new RUnit(solver, this);
        rConflict = new RConflict(solver, this);
        rBacktrack = new RBacktrack(solver, this);

		/*
		 * External Rules - Advanced
		 */
        rBackjump = new RBackjump(solver, this);
        rExplain = new RExplainEfficient(solver, this);
        rForget = new RForget(solver, this);
        rLearn = new RLearn(solver, this);
        rRestart = new RRestart(solver, this);

    }

    /*
     * RULE GUARDS BASIC
     */
    @Override
    public boolean decideGuard() {
        // Get guard of external decide rule
        return rDecide.guard();
    }

    @Override
    public Clause unitGuard() {
        // Get guard of external unit rule
        return rUnit.guard();
    }

    @Override
    public boolean conflictGuard() {
        // Get guard of external conflict rule
        return rConflict.guard();
    }

    @Override
    public boolean backtrackGuard() {
        // Get guard of external backtrack rule
        return rBacktrack.guard();
    }

    /*
     * RULE GUARDS ADVANCED
     */
    @Override
    public Integer backjumpGuard() {
        // Get guard of external backjump rule
        return rBackjump.guard();
    }

    @Override
    public boolean learnGuard() {
        // Get guard of external learn rule
        return rLearn.guard();
    }

    @Override
    public boolean forgetGuard() {
        // Get guard of external forget rule
        return rForget.guard();
    }

    @Override
    public boolean restartGuard() {
        // Get guard of external restart rule
        return rRestart.guard();
    }

    @Override
    public boolean explainUIPGuard() {
        return rExplain.guard();
    }

    /*
     * RULE APPLICATIONS BASIC
     */
    @Override
    public void applyDecide() {
        // Apply external decide rule and get decision literal
        rDecide.apply();
    }

    @Override
    public void applyUnit(Clause cl) {
        // Apply external unit rule and get unit literal
        rUnit.apply(cl);
    }

    @Override
    public void applyConflict() {
        // Apply external conflict rule
        rConflict.apply();
    }

    @Override
    public void applyBacktrack() {
        // Apply external backtrack rule
        rBacktrack.apply();

        // Solver are updated in trail while backtracking
    }

    /*
     * RULE APPLICATIONS ADVANCED
     */
    @Override
    public void applyBackjump(Integer UIP) {
        rBackjump.apply(UIP);
    }

    @Override
    public void applyLearn() {
        // Apply external learn rule and get learned clause
        rLearn.apply();
    }

    @Override
    public void applyForget() {
        // Apply external forget rule and get forgotten clause
        rForget.apply();
    }

    @Override
    public void applyRestart() {
        // Apply external restart rule
        rRestart.apply();

        // Inform observer
        getSolver().getObs().forEach(SolverListener::onRestart);
    }

    @Override
    public Integer applyExplainUIP() {
        return rExplain.apply();
        // Observers are notified in rule
    }

    @NotNull
    public Solver getSolver() {
        return solver;
    }

    @NotNull
    public LiteralSelectionStrategy getLiteralSelectionStrategy() {
        return literalSelectionStrategy;
    }

    @NotNull
    public RestartStrategy getRestartStrategy() {
        return restartStrategy;
    }

    @NotNull
    public LearnStrategy getLearnStrategy() {
        return learnStrategy;
    }

    @NotNull
    public ForgetStrategy getForgetStrategy() {
        return forgetStrategy;
    }
}
