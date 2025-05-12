package jmss.helper;

import jmss.scores.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 05.02.16.
 */
public class ActivityMock implements Activity {
    Map<Integer,Integer> actMap = new HashMap<>();

    public ActivityMock(int size){
        for (int i = 0; i < size; i++) {
            actMap.put(i,i*10);
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void bump(int element) {
        actMap.put(element,actMap.get(element) + 100);
    }

    @Override
    public void decayAll() {
        for (Integer i : actMap.keySet()) {
            actMap.put(i, actMap.get(i) - 1 );
        }
    }

    @Override
    public double getActivity(int i) {
        return actMap.get(i);
    }
}
