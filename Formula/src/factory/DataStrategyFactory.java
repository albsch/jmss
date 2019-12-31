package factory;

import counterbased.SetOfClausesCbs;
import formula.SetOfClauses;
import mixed.SetOfClausesMixed;
import specificationCore.Solver;
import wl.SetOfClausesW;

/**
 * Data strategy factory.
 * <p>
 * Created by Albert Schimpf on 04.10.2015.
 */
public class DataStrategyFactory {

    public static SetOfClauses getDataStrategy(Solver solver, ESetOfClauses dataStructure) {
        switch (dataStructure) {
            case DataCB:
                return new SetOfClausesCbs(solver);
            case Data2WL:
                return new SetOfClausesW(solver);
            case DataMixed:
                return new SetOfClausesMixed(solver);
            default:
                throw new IllegalArgumentException(
                        "Invalid data structure selected. " + dataStructure);
        }
    }
}
