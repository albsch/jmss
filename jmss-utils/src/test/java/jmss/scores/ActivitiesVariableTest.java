package jmss.scores;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Albert Schimpf on 05.02.16.
 */
public class ActivitiesVariableTest {

    //Init variable size test
    //bumping
    @Test
    public void T1(){
        //Size 1, Var 0
        Activity a = new ActivitiesVariable(1);
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
        Activity a = new ActivitiesVariable(3);
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

    //rescale after bump
    @Test
    public void T3(){
        //internal bump limit 10^20, this test will crash once the limit changes
        Activity a = new ActivitiesVariable(Math.pow(10,20)-1, 3, 3);
        double act0 = a.getActivity(0);
        double act1 = a.getActivity(1);
        double act2 = a.getActivity(2);

        // just below the rescale limit
        a.bump(1);

        double act0_1 = a.getActivity(0);
        double act1_1 = a.getActivity(1);
        double act2_1 = a.getActivity(2);

        assertTrue(act0 <= act0_1);
        assertTrue(act1 <= act1_1);
        assertTrue(act2 <= act2_1);

        // trigger a rescale
        a.bump(1);

        double act0_2 = a.getActivity(0);
        double act1_2 = a.getActivity(1);
        double act2_2 = a.getActivity(2);

        // everything should be smaller now
        assertTrue(act0 >= act0_2);
        assertTrue(act1_1 >= act1_2);
        assertTrue(act2 >= act2_2);

        //noinspection ResultOfMethodCallIgnored
        a.toString();
        assertEquals(3, a.size());
    }

    // no rescale for decay == 1
    @Test
    public void T4(){
        //internal bump limit 10^20, this test will crash once the limit changes
        Activity a = new ActivitiesVariable(Math.pow(10,21), 1, 3);
        double act0 = a.getActivity(0);
        double act1 = a.getActivity(1);
        double act2 = a.getActivity(2);

        // trigger a rescale
        a.bump(1);

        double act0_1 = a.getActivity(0);
        double act1_1 = a.getActivity(1);
        double act2_1 = a.getActivity(2);

        // rescale should not be triggered
        assertTrue(act0 <= act0_1);
        assertTrue(act1_1 <= act1_1);
        assertTrue(act2 <= act2_1);
    }

    //illegal size
    @Test(expected = IllegalArgumentException.class)
    public void TE1(){ new ActivitiesVariable(1,1,0); }
    @Test(expected = IllegalArgumentException.class)
    public void TE2(){ new ActivitiesVariable(1,1,-1); }
    @Test(expected = IllegalArgumentException.class)
    public void TE3(){ new ActivitiesVariable(1,0,1); }
    @Test(expected = IllegalArgumentException.class)
    public void TE4(){ new ActivitiesVariable(1,-1,1); }
    @Test(expected = IllegalArgumentException.class)
    public void TE5(){ new ActivitiesVariable(0,1,1); }
    @Test(expected = IllegalArgumentException.class)
    public void TE6(){ new ActivitiesVariable(-1,1,1); }
}