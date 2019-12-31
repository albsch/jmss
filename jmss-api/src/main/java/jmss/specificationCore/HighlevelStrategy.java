package jmss.specificationCore;

/**
 * Specifications for a high-level strategy.
 * <p>
 * Every strategy has to save the reference to the main module, as they operate
 * on the jmss.formula {@code F} and the trail {@code M}.
 * 
 * @author Albert Schimpf
 */
public interface HighlevelStrategy {
	/**
	 * The abstract function which needs to be implemented by an actual
	 * strategy.
	 * 
	 * @return true if jmss.formula {@code F} is satisfied, false if not.
	 */
	boolean applyStrategy();
}
