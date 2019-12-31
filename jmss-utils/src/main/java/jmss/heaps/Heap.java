package jmss.heaps;

/**
 * Created by Albert Schimpf on 05.02.16.
 */
public interface Heap {
    /**
     * Restores heap property from this position in the heap
     *
     * Time requirement: O(log n)
     */
    void update(int x);

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
     * How many items are contained in the heap
     *
     * Time requirement: O(1)
     */
    int size ();
}
