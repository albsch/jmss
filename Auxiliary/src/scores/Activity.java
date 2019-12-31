package scores;

/**
 * Created by nas on 05.02.16.
 */
public interface Activity {
    int size();
    void bump(int element);
    void decayAll();
    double getActivity(int i);

    /**
     * Returns current preferred implementation of the activity structure,
     * either for variables or literals.
     */
    static Activity getImpl(int size, type t){
        switch (t){
            case variableActivity:
                return new ActivitiesVariable(size);
            case literalActivity:
                throw new NullPointerException("LiteralHeap not implemented yet");
            default:
                throw new NullPointerException("'"+t+" not implemented yet");
        }
    }

    enum type{
        variableActivity,literalActivity;
    }
}
