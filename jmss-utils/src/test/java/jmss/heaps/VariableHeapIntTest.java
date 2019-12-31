package jmss.heaps;

import jmss.helper.ActivityMock;
import jmss.scores.Activity;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Created by Albert Schimpf on 05.02.16.
 */
public class VariableHeapIntTest {

    //Init heap size test
    //bumping
    @Test
    public void T1(){
        Heap h = new VariableHeapInt(new ActivityMock(1), 1);

        assertFalse(h.contains(0));
        h.push(0);
        assertTrue(h.contains(0));
        h.pop();
        assertFalse(h.contains(0));

        h = new VariableHeapInt(new ActivityMock(3), 3);

        assertFalse(h.contains(0));
        assertFalse(h.contains(1));
        assertFalse(h.contains(2));
        h.push(0);
        assertTrue(h.contains(0));
        assertFalse(h.contains(1));
        assertFalse(h.contains(2));
        h.push(1);
        assertTrue(h.contains(0));
        assertTrue(h.contains(1));
        assertFalse(h.contains(2));
        h.push(2);
        assertTrue(h.contains(0));
        assertTrue(h.contains(1));
        assertTrue(h.contains(2));
        h.pop();
        h.pop();
        h.pop();
        assertFalse(h.contains(0));
        assertFalse(h.contains(1));
        assertFalse(h.contains(2));
    }

    //MaxHeap property
    @Test
    public void T2(){
        Heap h = new VariableHeapInt(new ActivityMock(3), 3);

        assertFalse(h.contains(0));
        assertFalse(h.contains(1));
        assertFalse(h.contains(2));
        h.push(0);
        h.push(1);
        h.push(2);
        assertTrue(h.contains(0));
        assertTrue(h.contains(1));
        assertTrue(h.contains(2));

        int l = h.pop();
        int l2= h.pop();
        //dummyActivity maps higher initial scores to higher indexes
        assertEquals(2, l);
        assertEquals(1, l2);
    }

    //Increase bump value
    @Test
    public void T3(){
        Activity act = new ActivityMock(3);
        Heap h = new VariableHeapInt(act, 3);

        h.push(0);
        h.push(1);
        h.push(2);

        act.bump(0);
        h.increase(0);
        int l = h.pop();
        int l2= h.pop();
        //dummyActivity maps higher initial scores to higher indexes
        assertEquals(0, l);
        assertEquals(2, l2);

        h.push(0);
        h.push(2);
        l = h.pop();
        l2= h.pop();
        assertEquals(0, l);
        assertEquals(2, l2);
    }

    //Bug1: undefined space in array
    @Test
    public void T4(){
        Activity act = new ActivityMock(50);
        Heap h = new VariableHeapInt(act, 50);
        for (int i = 0; i < 50; i++) {
            h.push(i);
        }

        for (int i = 0; i < 11; i++) {
            h.pop();
        }
        h.push(39);
        h.push(40);
        h.push(41);
        h.push(42);
        h.push(43);

        for (int i = 0; i < 4; i++) {
            h.pop();
        }

        h.push(40);
        h.push(41);
        h.push(42);
    }

    // always-0 activity, order undefined for equality
//    @Test
//    public void T5() {
//
//        Stream.of(
//                new Integer[]{0,1,2},
//                new Integer[]{0,2,1},
//                new Integer[]{1,2,0},
//                new Integer[]{1,0,2},
//                new Integer[]{2,1,0},
//                new Integer[]{2,0,1}
//        ).forEach(l -> {
//            Activity act = new Activity() {
//                @Override public int size() { return 3; }
//                @Override public void bump(int i) {}
//                @Override public void decayAll() {}
//                @Override public double getActivity(int i) { return 0; }
//            };
//
//            Heap h = new VariableHeapInt(act, 3);
//
//            Stream.of(l).forEach(h::push);
//
//            assertEquals(0, h.pop());
//            assertEquals(1, h.pop());
//            assertEquals(2, h.pop());
//        });
//
//    }

    // heap property violation for activity decay
    @Test
    public void T6() {

        Activity act = new Activity() {
            boolean decay = false;
            @Override public int size() { return 3; }
            @Override public void bump(int i) {}
            @Override public void decayAll() { decay = true; }
            @Override public double getActivity(int i) {
                if (!decay) { return i * 10; } else { return 0; }
            }
        };

        VariableHeapInt h = new VariableHeapInt(act, 3);

        // push on heap
        h.push(0);
        h.push(1);
        h.push(2);

        // heap is now
        //    2
        //  1   0
        // not decayed, score is variable index*10

        int test = h.pop();
        assertEquals(2, test);
        h.push(test);

        // heap is now
        //        2 [20]
        //  0 [0]        1 [10]

        // decay
        act.decayAll();

        // heap property should never be violated by decaying scores (for this heap and activity implementation)
        //        2 [0]
        //  0 [0]        1 [0]
        Assert.assertTrue(h.verifyHeapProperty(0));
    }
}