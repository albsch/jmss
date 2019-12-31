package heaps;

import scores.Activity;

import java.util.Arrays;

//FIXME
//port to int array buggy
public class VariableHeapInt implements Heap
{
    static private int UNDEFINED = -2;
    public VariableHeapInt(Activity gt, int size)
    {
        _gt = gt;
        _heap = new int[size];
        _position_in_heap = new int[size];
        Arrays.fill(_heap,UNDEFINED);
        Arrays.fill(_position_in_heap, UNDEFINED);
    }

    Activity _gt;
    int[] _heap;
    int current = 0;
    int[] _position_in_heap;

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

    boolean gt (int x, int y)
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
        int x = _heap[i];
        while (i != 0)
        {
            if (gt(_heap[parrent(i)], x))
            {
                break;
            }

            int p = parrent(i);
            _heap[i] = _heap[p];
            // _position_in_heap[_heap[i), i);
            _position_in_heap[_heap[i]] = i;
            i = p;
        }
        _heap[i] = x;
        _position_in_heap[_heap[i]] =  i;

        // assert (checkHeap(0));
    }

    void moveDown (int i)
    {
        Integer x = _heap[i];
        int size = size();
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
                child = gt(_heap[left], _heap[right]) ? left : right;
            }

            if (gt(x, _heap[child]))
            {
                break;
            }

            _heap[i] = _heap[child];
            _position_in_heap[_heap[i]] = i;
            i = child;

            left = left_child(i);
            right = right_child(i);
        }
        _heap[i] = x;
        _position_in_heap[_heap[i]] = i;

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
    public void push_inactive (int x)
    {
        //_position_in_heap.add(UNDEFINED);
    }

    public void push (int x)
    {
        _heap[current] = x;
        _position_in_heap[current] = size() - 1;
        current++;
        
        moveUp(size() - 1);
    }

    public int pop ()
    {
        Integer x = _heap[0];
        _heap[0] = _heap[(size() - 1)];
        _heap[(size() - 1)] = UNDEFINED;
        current--;
        _position_in_heap[x] = UNDEFINED;
        if (!empty())
        {
            moveDown(0);
        }
        assert (!contains(x));
        return x;
    }

    public void update_heap (int x)
    {
        if (!contains(x))
        {
            push(x);
        }
        else
        {
            moveUp(_position_in_heap[x]);
            moveDown(_position_in_heap[x]);
        }
    }

    public void increase (int x)
    {
        moveUp(_position_in_heap[x]);
    }

    public void decrease (int x)
    {
        moveDown(_position_in_heap[x]);
    }

    public boolean contains (int x)
    {
        // assert (x < _position_in_heap.size());
        return _position_in_heap[x] != UNDEFINED;
    }

    public int size ()
    {
        return current;
    }

    public boolean empty ()
    {
        return current == 0;
    }

    public void print(){
        System.out.print("[ ");
        for (int i : _heap) {
            System.out.print(i+" ");
        }
        System.out.println(" ]");

        System.out.print("[ ");
        for (int i : _position_in_heap) {
            System.out.print(i+" ");
        }
        System.out.println(" ]");


    }
}
