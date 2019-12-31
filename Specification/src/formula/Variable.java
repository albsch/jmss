package formula;

import specificationCore.Solver;

import java.util.List;

public interface Variable {
	Solver getSolver();

	Integer getIndex();
	Integer getAssignment();

	void setTrue();
	void setFalse();
	void undoAssignment();

	int getDl();
	void setDl(int i);

	int getOrder();
	void setOrder(int i);

	List<Integer> getReason();
	void setReason(List<Integer> ante);

	void removeConnections(Integer lit, Clause contained);
}