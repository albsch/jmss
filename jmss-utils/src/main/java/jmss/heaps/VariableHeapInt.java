package jmss.heaps;

import jmss.annotations.Module;
import jmss.scores.Activity;

import java.util.Arrays;


// Assumptions
//   1) only unique values inserted with push(int value) (undefined behavior otherwise)
@SuppressWarnings("Duplicates")
@Module(module = "heap", name = "array-heap", defaultModule = true)
public class VariableHeapInt implements Heap {
    static private int UNDEFINED = -1;

    public VariableHeapInt(Object... opt) { this(((Activity) opt[0]), ((int) opt[1])); }

    public VariableHeapInt(Activity gt, int size) {
        _gt = gt;
        _heap = new int[size];
        _position_in_heap = new int[size];
        Arrays.fill(_heap, UNDEFINED);
        Arrays.fill(_position_in_heap, UNDEFINED);
    }

    private Activity _gt;
    private int[] _heap;
    private int current = 0;
    private int[] _position_in_heap;

    private int parrent(int i) {
        assert i >= 0;
        assert ((i - 1) >> 1) >= 0;
        return (i - 1) >> 1;
    }
    private int left_child(int i) {
        assert i >= 0;
        assert ((i<< 1) | 1) >= 0;
        return (i << 1) | 1;
    }
    private int right_child(int i) {
        assert i >= 0;
        assert ((i + 1) << 1) >= 0;
        return (i + 1) << 1;
    }

    private int gt(int x, int y) {
        assert x >= 0;
        assert y >= 0;
        double ax = _gt.getActivity(x);
        double ay = _gt.getActivity(y);

        return Double.compare(ax, ay);

    }

    private void moveUp(int elementPosition) {
        assert elementPosition >= 0 && elementPosition < size();

        // finished if position 0 (top of heap)
        if(elementPosition == 0) return;

        int parentPosition = parrent(elementPosition);

        // if not max heap property, swap parent and element
        // parent < child, child > parent => swap
        if (gt(_heap[elementPosition], _heap[parentPosition]) > 0) {

            // swap elements to restore heap property
            int elementValue = _heap[elementPosition];
            int parentValue = _heap[parentPosition];

            // swap values
            _heap[parentPosition] = elementValue;
            _heap[elementPosition] = parentValue;

            // swap positions
            _position_in_heap[elementValue] = parentPosition;
            _position_in_heap[parentValue] = elementPosition;

            // restore heap property for up sifted value
            moveUp(parentPosition);
        }

        // heap property restored
        assert (verifyHeapProperty(0)) :
                printStr();
    }

    private void moveDown(int parentPosition) {
        assert parentPosition >= 0 && parentPosition < size();

        Integer parentValue = _heap[parentPosition];
        int size = size();

        int left = left_child(parentPosition),
                right = right_child(parentPosition);

        while (left < size) {
            int child;
            if (right >= size) {
                child = left;
            } else {
                int leftt = _heap[left];
                int rightt = _heap[right];
                child = gt(leftt, rightt) > 0 ? left : right;
            }

            if (gt(parentValue, _heap[child]) >= 0) {
                break;
            }

            _heap[parentPosition] = _heap[child];
            _position_in_heap[_heap[parentPosition]] = parentPosition;
            parentPosition = child;

            left = left_child(parentPosition);
            right = right_child(parentPosition);
        }
        _heap[parentPosition] = parentValue;
        _position_in_heap[_heap[parentPosition]] = parentPosition;

        assert (verifyHeapProperty(0));
    }

    public void push(int value) {
        // values should be unique for this implementation
        assert !contains(value);

        // insert element at the end of the array
        assert _heap[current] == UNDEFINED :
                printStr();
        _heap[current] = value;

        // save position of value to current
        _position_in_heap[value] = current;

        // increase size
        current++;

        // restore heap property beginning from the inserted element
        moveUp(current - 1);

        // heap property should be restored for whole tree
        assert verifyHeapProperty(0);
    }

    public int pop() {
        // retrieve max element
        int maxHeapElementValue = _heap[0];
        int maxHeapElementPosition = _position_in_heap[maxHeapElementValue];

        // obviously should be the first element of the array (root of tree)
        assert maxHeapElementPosition == 0;

        // get last element value
        int lastElementValue = _heap[size() - 1];
        int lastElementPosition = _position_in_heap[lastElementValue];

        // obviously should be the last element of the array
        assert lastElementPosition == size() - 1;


        // remove root element from position array and set root to undefined
        _position_in_heap[maxHeapElementValue] = UNDEFINED;
        _heap[0] = UNDEFINED;

        if(lastElementValue != maxHeapElementValue) {
            // set last element to the new root, update position
            _heap[0] = lastElementValue;
            _position_in_heap[lastElementValue] = 0;

            // remove last element
            _heap[size() - 1] = UNDEFINED;
        } else {
            // special case, one element heap (last element equal max element, root)
            // do nothing (only remove element)
            assert size() == 1;
        }

        // reduce size of heap
        current--;
        assert size() >= 0;

        // if not empty, restore heap property from root
        if (!empty()) moveDown(0);

        // element should not be contained anymore
        assert (!contains(maxHeapElementValue));

        // heap property should be restored for whole tree
        assert verifyHeapProperty(0);

        return maxHeapElementValue;
    }

    public void update(int x) {
        assert x >= 0;
        if(x >= size()) return;

        assert _position_in_heap[x] != UNDEFINED;

        moveUp(x);
        moveDown(x);
    }

    public void increase(int x) {
        assert contains(x);
        moveUp(_position_in_heap[x]);
    }

    public void decrease(int x) {
        assert contains(x);
        moveDown(_position_in_heap[x]);
    }

    public boolean contains(int x) {
        assert (x >= 0);
        assert (x < _position_in_heap.length);

        return _position_in_heap[x] != UNDEFINED;
    }

    public int size() {
        return current;
    }

    private boolean empty() {
        return current == 0;
    }

    public void print() {
        System.out.print("[ ");
        for (int i : _heap) {
            System.out.print(i + " ");
        }
        System.out.println("]");

        System.out.print("[ ");
        for (int i : _position_in_heap) {
            System.out.print(i + " ");
        }
        System.out.println("]");
    }

    public String printStr() {
        String s = "\n[ ";
        for (int i : _heap) {
            s += (i + " ");
        }
        s += ("]\n");

        s += ("[ ");
        for (int i : _position_in_heap) {
            s += (i + " ");
        }
        s += ("]\n");

        return s;
    }


    // starting from node 'parentPosition'
    // verifies that the heap property is satisfied for each node beneath that position recursively
    public boolean verifyHeapProperty(int parentPosition) {
        if (parentPosition >= size()) return true;

        int leftPosition = left_child(parentPosition);
        int rightPosition = right_child(parentPosition);

        int parentElement = _heap[parentPosition];
        double valueParent = _gt.getActivity(parentElement);

        if(leftPosition < size()) {
            int leftElement = _heap[leftPosition];
            double valueLeftChild = _gt.getActivity(leftElement);

            // good: parent > child
            if(gt(parentElement, leftElement) < 0) {
                System.out.println("Heap property violated");
                System.out.println("Left child @" + leftPosition+ " (element "+leftElement+") value: " + valueLeftChild);
                System.out.println("Parent @" + parentPosition+ " (element "+parentElement+") value: " + valueParent);
                System.out.println("parent gt left: " + gt(parentElement, leftElement));
                return false;
            }
        }

        if(rightPosition < size()) {
            int rightElement = _heap[rightPosition];
            double valueRightChild = _gt.getActivity(rightElement);

            // good: parent > child
            if(gt(parentElement, rightElement) < 0) {
                System.out.println("Heap property violated");
                System.out.println("Right child @" + rightPosition+ " (element "+rightElement+") value: " + valueRightChild);
                System.out.println("Parent @" + parentPosition+ " (element "+parentElement+") value: " + valueParent);
                System.out.println("parent gt right: " + gt(parentElement, rightElement));
                return false;
            }
        }

        // continue checking heap property for left and right children
        return verifyHeapProperty(leftPosition) && verifyHeapProperty(rightPosition);
    }
}
