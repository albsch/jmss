package jmss.formula;

import jmss.specificationCore.Solver;
import jmss.specificationCore.State;

import java.util.List;


@SuppressWarnings("WeakerAccess") // abstract class
public abstract class VariableAbs implements Variable {
	protected final Integer index;

	protected Integer assignment;
	protected State state;

	public VariableAbs(Integer i, Solver solver) {
		if (i < 0) {
			throw new IllegalArgumentException();
		}
		this.index = i;
		this.assignment = -1;

		this.state = solver.getState();
	}

	@Override
	public Integer getIndex() {
		return this.index;
	}

	@Override
	public Integer getAssignment() {
		return this.assignment;
	}

	public void setAssignment(Integer assignment) {
		this.assignment = assignment;

		if(assignment == -1){
			state.F().increaseFreeVariable();
		}
		else{
			state.F().decreaseFreeVariable();
		}
	}


	protected int dl = -1;

	@Override
	public int getDl(){
		return dl;
	}

	@Override
	public void setDl(int i){
		dl = i;
	}

	int order = -1;

	@Override
	public int getOrder(){
		return order;
	}

	@Override
	public void setOrder(int i){
		order = i;
	}

	protected List<Integer> ante;

	@Override
	public List<Integer> getReason(){
		return ante;
	}

	@Override
	public void setReason(List<Integer> i){
		ante = i;
	}


}
