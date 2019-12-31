package heaps;

import org.junit.Test;
import scores.Activity;

import static org.junit.Assert.*;

/**
 * Created by nas on 05.02.16.
 */
public class VariableHeapTest {

    //Init heap size test
    //bumping
    @Test
    public void T1(){
        Heap h = Heap.getImpl(1,new ActivityDummy(1));

        assertFalse(h.contains(0));
        h.push(0);
        assertTrue(h.contains(0));
        h.pop();
        assertFalse(h.contains(0));

        h = Heap.getImpl(3,new ActivityDummy(3));

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
        Heap h = Heap.getImpl(3,new ActivityDummy(3));

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
        assertTrue(l == 2);
        assertTrue(l2 == 1);
    }

    //Increase bump value
    @Test
    public void T3(){
        Activity act = new ActivityDummy(3);
        Heap h = Heap.getImpl(3,act);

        h.push(0);
        h.push(1);
        h.push(2);

        act.bump(0);
        h.increase(0);
        int l = h.pop();
        int l2= h.pop();
        //dummyActivity maps higher initial scores to higher indexes
        assertTrue(l == 0);
        assertTrue(l2 == 2);

        h.push(0);
        h.push(2);
        l = h.pop();
        l2= h.pop();
        assertTrue(l == 0);
        assertTrue(l2 == 2);
    }

    //Bug1: undefined space in array
    @Test
    public void T4(){
        Activity act = new ActivityDummy(50);
        Heap h = Heap.getImpl(50,act);
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


    private void push(int i, Heap h, int c){
        for (int j = 0; j < i; j++) {
            h.push(c);
        }
    }

    private void pop(int i, Heap h){
        for (int j = 0; j < i; j++) {
            h.pop();
        }
    }
}