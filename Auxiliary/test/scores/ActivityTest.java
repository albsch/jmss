package scores;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nas on 05.02.16.
 */
public class ActivityTest {

    //Init variable size test
    //bumping
    @Test
    public void T1(){
        //Size 1, Var 0
        Activity a = Activity.getImpl(1, Activity.type.variableActivity);
        a.getActivity(0);

        double act = a.getActivity(0);
        a.bump(0);
        double bumped = a.getActivity(0);
        assertTrue(act <= bumped);

        try{
            a.getActivity(-1);
            fail("Negative activity allowed.");
        }catch (Exception ignored){}

        try{
            a.getActivity(1);
            fail("Size limit exceeded.");
        }catch (Exception ignored){}
    }

    //Decaying
    @Test
    public void T2(){
        //Size 1, Var 0
        Activity a = Activity.getImpl(3, Activity.type.variableActivity);
        a.bump(2);
        a.bump(2);
        a.bump(2);
        a.bump(1);

        double act = a.getActivity(2);
        double act1 = a.getActivity(1);
        double act0 = a.getActivity(0);
        a.decayAll();
        double decayed = a.getActivity(2);
        double decayed1 = a.getActivity(1);
        double decayed0 = a.getActivity(0);
        assertTrue(act >= decayed);
        assertTrue(act1 >= decayed1);
        assertTrue(act0 >= decayed0);
    }

}