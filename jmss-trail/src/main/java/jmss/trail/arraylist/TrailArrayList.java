package jmss.trail.arraylist;


import jmss.annotations.Module;
import jmss.annotations.NotNull;
import jmss.formula.SetOfClauses;
import jmss.formula.Variable;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;
import jmss.specificationCore.Trail;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.util.Collections.max;

/**
 * 2016/02/01.
 */
@Module(module = "trail", name = "stack-and-sequence", defaultModule = true)
public class TrailArrayList implements Trail {
    private final SetOfClauses F;
    private final Solver solver;


    private Stack<TLiteral> previousDlLits = new Stack<>();
    private TLiteral currentDlLit = null;
    private int currentDl = 0;
    private int currentOrder = 0;

    private List<TLiteral> sequence;

    public int firstLevel = 0;

    public TrailArrayList(@NotNull Solver solver) {
        this.F = solver.getState().F();
        this.solver = solver;
        sequence = new ArrayList<>();
    }


    private void assertLiteral(@NotNull TLiteral l) {
        if(currentDl == 0){
            firstLevel++;
        }


        Variable var = F.getVariable(l.lit);
        //case  decide
        if (l.decision) {
            //set decision literal references
            previousDlLits.push(currentDlLit);
            currentDlLit = l;
            //increase level
            currentDl++;
            //reset order
            currentOrder = 0;

        }

        //set dl, order, increase order and add to tail literals
        var.setDl(currentDl);
        var.setOrder(currentOrder);
        currentOrder++;

        sequence.add(l);

        // Assign variable in F
        if (l.lit < 0) {
            F.setVariableFalse(l.lit);
        } else {
            F.setVariableTrue(l.lit);
        }
    }


    //takes care of current DL
    private void backtrackLastLiteral() {
        if(currentDl == 0){
            firstLevel--;
        }

        TLiteral bLit = sequence.remove(sequence.size()-1);

        //set variable attributes
        Variable var = F.getVariable(bLit.lit);
        var.setDl(-1);
        var.setOrder(-1);
        var.setReason(null);

        //case decision
        if(bLit.decision){
            //set decision literal references
            currentDlLit = previousDlLits.pop();
            //decrease level
            currentDl--;
            //decrease order
            if(sequence.size() != 0){
                TLiteral bLit2 = sequence.get(sequence.size()-1);
                currentOrder = F.getVariable(bLit2.lit).getOrder()+1;
            } else{
                currentOrder = 0;
            }

        } else{
            //decrease order
            currentOrder--;
        }

        //undo assignment
        var.undoAssignment();

        // Update all observer that literal l has been backtracked
        for (SolverListener ob : solver.getObs()) {
            ob.onBacktrack(bLit.lit);
        }
    }


    @Override
    public void createRefutation() {
        //TODO
    }

    @Override
    public void createModel() {
        String ret = "v ";
        for (TLiteral l : sequence) {
            ret += (l.lit) + " ";
        }
        System.out.println(ret + "0");
    }


    @Override
    public List<Integer> getCurrentDecisionLevelLiteralsFromEnd() {
        // TODO more efficient storage of current decision level literals?
        List<Integer> res = new ArrayList<>();
        int i = sequence.size() - 1;

        if (i == -1) {
            return res;
        }

        TLiteral l = sequence.get(i);
        while (!l.decision) {
            res.add(l.lit);
            i--;
            if (i == -1) {
                return res;
            }
            l = sequence.get(i);
        }

        res.add(l.lit);

        return res;
    }

    @Override
    public void backjumpToLevel(@NotNull Integer i,@NotNull Integer flip) {
        if (i == currentDl) {
            throw new IllegalArgumentException("Decision parameter is the same as current level");
        }
        if (i < 0) {
            throw new IllegalArgumentException("Illegal backjump level. " + i);
        }
//        if (!inTrail(-flip)) {
//            throw new IllegalArgumentException("Integer not false in trail: " + flip);
//        }


        // Backtrack one decision level until given level is reached
        while (!getCurrentDecisionLevel().equals(i)) {
            backtrackLastLiteral();
        }
        F.resetConflicts();
        addUnitLiteral(flip, solver.getState().C());
    }

    @Override
    public void backtrack() {
        // Use general back jump for backtrack
        Integer flip = getCurrentDecisionLiteral();

        backjumpToLevel(getCurrentDecisionLevel() - 1, -flip);
    }


    @Override
    public void reset() {
        while (currentDl > 0) {
            backtrackLastLiteral();
        }
    }

    //Old: complex
    //New: O(cl.size + O(getSecondMostRecentLevel) + hashset.remove*collections.max)
    @Override
    public Integer getSecondRecentDecisionLevel(@NotNull List<Integer> cl) {
        //filter duplicates
        Set<Integer> filtered = new HashSet<>(cl.size());
        filtered.addAll(cl.stream().map(lit -> F.getVariable(lit).getDl()).collect(Collectors.toList()));

        if (filtered.size() == 1) {
            return -1;
        } else {
            filtered.remove(max(filtered));
            return max(filtered);
        }
    }


    //Old: O(n)
    //O(1)
    @Override
    public List<Integer> getReason(@NotNull Integer lit) {
        return F.getVariable(lit).getReason();
    }


    //Old: O(2*n)
    //New: O(1)
    @Override
    public boolean isAssertedBefore(@NotNull Integer lit,@NotNull Integer target) {
        //both have to be in trail //TODO doc

        int dlLit = F.getVariable(lit).getDl();
        int dlTarget = F.getVariable(target).getDl();

        if (dlLit < dlTarget) {
            return true;
        } else if (dlLit > dlTarget) {
            return false;
        } else {
            return F.getVariable(lit).getOrder() < F.getVariable(target).getOrder();
        }

    }

    //O(assertLiteral)
    @Override
    public void addDecisionLiteral(@NotNull Integer lit) {
        TLiteral decideLit = new TLiteral(lit, true);
        Variable var = F.getVariable(lit);
        var.setDl(currentDl + 1);
        var.setOrder(currentOrder);
//        var.setReason(null);
        assertLiteral(decideLit);
    }

    //O(assertLiteral)
    @Override
    public void addUnitLiteral(@NotNull Integer lit,@NotNull List<Integer> ante) {
        // set reason of literal to the clause
        TLiteral unitLit = new TLiteral(lit, false);
        Variable var = F.getVariable(lit);
        var.setDl(currentDl);
        var.setOrder(currentOrder);
        var.setReason(ante);
//        assert ((VariableAbs) var).getAnteIndex() != -1;
        //((VariableAbs) var).setAnteIndex(); //FIXME not here
        // call function to assert literal as a unit literal
        assertLiteral(unitLit);
    }

    //Old: O(#CurrentDlLiteral)
    //New: O(1)
    @Override
    public Integer getCurrentDecisionLiteral() {
        //FIXME nullpointer
        return currentDlLit.lit;
    }

    //O(1)
    @Override
    public Integer getCurrentDecisionLevel() {
        return currentDl;
    }

    @Override
    public Integer getDecisionLevel(@NotNull Integer lit) {
        assert inTrail(lit) || inTrail(-lit);
        //not checked FIXME check if in trail
        return F.getVariable(lit).getDl();
    }

    //O(1)
    //3 array get calls on F
    @Override
    public boolean inTrail(@NotNull Integer l) {
        // // Instead of traversing the trail,
        // // check if variable is undefined in F
//        System.out.print("Checking "+l+"... ");
        Integer index = abs(l);
        boolean neg = l < 0;

        if (F.isVariableUndefined(index)) {
//            System.out.println(" false because undef!");
            return false;
        }

        // If variable is false and ld is negated, then ld is in trail
        if ((F.isVariableFalse(index)) && neg) {
//            System.out.println(" true because var false!!");
            return true;
        }

        // If variable is true and ld is not negated, then ld is in trail
        if ((F.isVariableTrue(index)) && !neg) {
//            System.out.println(" true because var true!");
            return true;
        }

//        System.out.println(" other! ... varFalse("+F.isVariableFalse(index)+") varTrue("+F.isVariableTrue(index)+") "+neg);
        // For other cases return false
        return false;
    }



//
//    @Override
//    public String toString() {
//        String s = "[ ";
//
//        for (TLiteral literal : sequence) {
//            if(literal.decision) s+= "X ";
//            s+=literal.lit+" ";
//        }
//
//        return s + " ]";
//    }
}
