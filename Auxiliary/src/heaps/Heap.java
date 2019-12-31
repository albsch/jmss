package heaps;

import scores.Activity;

/**
 * Created by nas on 05.02.16.
 */
public interface Heap {
    /**
     * Pushes element onto the heap
     *
     * Time requirement: O(log n)
     */
    void push(int x);

    /**
     * Returns the element with the greatest score
     *
     * Time requirement: O(log n)
     */
    int pop();

    /**
     * Increases score of element, sorts heap
     *
     * Time requirement: O(log n)
     */
    void increase (int x);

    /**
     * Decreases score of element, sorts heap
     *
     * Time requirement: O(log n)
     */
    void decrease (int x);

    /**
     * Contain check
     *
     * Time requirement: O(1)
     */
    boolean contains (int x);

    /**
     * Returns current preferred implementation of the heap structure as a MaxHeap.
     */
    static Heap getImpl(int size, Activity activities){
        return new VariableHeapIntList(activities ,size);
    }
}
