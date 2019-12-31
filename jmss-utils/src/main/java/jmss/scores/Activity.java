package jmss.scores;

/**
 * Score management for variables
 *
 * Created by Albert Schimpf on 05.02.16.
 */
public interface Activity {
    /** Returns how many variables are tracked */
    int size();
    /** Bumps the score of variable i */
    void bump(int i);
    /** Decays the score of all variables */
    void decayAll();
    /** Gets score for variable i */
    double getActivity(int i);
}
