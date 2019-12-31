package heaps;

import scores.Activity;

import java.util.ArrayList;

public class VariableHeapIntList implements Heap
{
    static private int UNDEFINED = -1;

    public VariableHeapIntList(Activity gt, int size)
    {
        _gt = gt;
        _heap = new ArrayList<>(size);
        _position_in_heap = new ArrayList<>(size);
    }

    Activity _gt;
    ArrayList<Integer> _heap;
    ArrayList<Integer> _position_in_heap;

    static int parrent (int i)
    {
        return (i - 1) >> 1;
    }

    static int left_child (int i)
    {
        return (i << 1) | 1;
    }

    static int right_child (int i)
    {
        return (i + 1) << 1;
    }

    boolean gt (Integer x, Integer y)
    {
        double ax = _gt.getActivity(x);
        double ay = _gt.getActivity(y);

        if(ax > ay){
            return true;
        }
        else if( ax < ay){
            return false;
        }
        else{
            return x < y;
        }
    }

    void moveUp (int i)
    {
        Integer x = _heap.get(i);
        while (i != 0)
        {
            if (gt(_heap.get(parrent(i)), x))
            {
                break;
            }

            int p = parrent(i);
            _heap.set(i, _heap.get(p));
            // _position_in_heap.set(_heap.get(i), i);
            _position_in_heap.set(_heap.get(i), i);
            i = p;
        }
        _heap.set(i, x);
        _position_in_heap.set(_heap.get(i), i);

        // assert (checkHeap(0));
    }

    void moveDown (int i)
    {
        Integer x = _heap.get(i);
        int size = _heap.size();
        int left = left_child(i),
                right = right_child(i);

        while (left < size)
        {
            int child;
            if (right >= size)
            {
                child = left;
            }
            else
            {
                child = gt(_heap.get(left), _heap.get(right)) ? left : right;
            }

            if (gt(x, _heap.get(child)))
            {
                break;
            }

            _heap.set(i, _heap.get(child));
            _position_in_heap.set(_heap.get(i), i);
            i = child;

            left = left_child(i);
            right = right_child(i);
        }
        _heap.set(i, x);
        _position_in_heap.set(_heap.get(i), i);

        // assert (checkHeap(0));
    }

//    boolean checkHeap (int i)
//    {
//        if (i >= size())
//        {
//            return true;
//        }
//
//        int left = left_child(i),
//                right = right_child(i);
//        if (left < size() && _gt(_heap[left], _heap[i]))
//        {
//            cout << "Wrong: " << _heap[left] << "  " << _heap[i] << endl;
//            return false;
//        }
//        if (right < size() && _gt(_heap[right], _heap[i]))
//        {
//            cout << "Wrong: " << _heap[right] << "  " << _heap[i] << endl;
//            return false;
//        }
//        return checkHeap(left) && checkHeap(right);
//    }

    // The heap is notified about x's existence, but
    // it's not inserted into it.
    public void push_inactive (Integer x)
    {
        _position_in_heap.add(UNDEFINED);
    }

    public void push (int x)
    {
        _heap.add(x);
        _position_in_heap.add(size() - 1);

        moveUp(size() - 1);
    }

    public int pop ()
    {
        assert (_heap.size() > 0);
        Integer x = _heap.get(0);
        _heap.set(0, _heap.get(size() - 1));
        _heap.remove(size() - 1);
        _position_in_heap.set(x, UNDEFINED);
        if (!empty())
        {
            moveDown(0);
        }
        assert (!contains(x));
        return x;
    }

    public void update_heap (Integer x)
    {
        if (!contains(x))
        {
            push(x);
        }
        else
        {
            moveUp(_position_in_heap.get(x));
            moveDown(_position_in_heap.get(x));
        }
    }

    public void increase (int x)
    {
        moveUp(_position_in_heap.get(x));
    }

    public void decrease (int x)
    {
        moveDown(_position_in_heap.get(x));
    }

    public boolean contains (int x)
    {
        // assert (x < _position_in_heap.size());
        return _position_in_heap.get(x) != UNDEFINED;
    }

    public int size ()
    {
        return _heap.size();
    }

    public boolean empty ()
    {
        return size() == 0;
    }

}
