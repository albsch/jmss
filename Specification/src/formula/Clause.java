package formula;


import java.util.List;


public interface Clause {
	boolean isConflicting();
	boolean isUnit();

	Integer getUnitLiteral();
	List<Integer> getLiterals();

	void addLiteral(Integer lit);
	void removeConnectionToVariables();
	void assertConflictingState();
}
