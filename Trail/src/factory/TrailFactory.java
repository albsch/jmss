package factory;

import specificationCore.Solver;
import specificationCore.Trail;
import arraylist.TrailArrayList;

public class TrailFactory {
    private TrailFactory(){}

    public static Trail getTrail(Solver solver, ETrail trail) {
        switch (trail) {
//            case TrailSimple:
//                return new TrailSimple(solver);
            case TrailEfficient:
                return new TrailArrayList(solver);
            default:
                throw new IllegalArgumentException("Invalid trail selected. "
                        + trail);
        }
    }
}
