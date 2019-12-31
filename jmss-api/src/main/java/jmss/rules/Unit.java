package jmss.rules;

import jmss.formula.Clause;

/**
 * @author Albert Schimpf
 */
public interface Unit {
    /**
     * Checks if there is a unit clause in the current jmss.formula {@code F} with
     * current assignments in trail {@code M}.
     * <p>
     * The data structure that implements the jmss.formula {@code F} should support
     * fast unit detection, because unit propagation takes up most of the
     * solving process.
     * <p>
     *
     * @return the unit clause, if there is such a clause; {@code null} otherwise.
     */
    Clause guard();

    /**
     * Applies unit by inserting the unit literal of the givne clause to the trail
     */
    void apply(Clause cl);
}
