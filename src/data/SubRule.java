/**
 * SubRule Class
 * Data structure used to represent individual input and output constructs
 * 
 * @author Craig Knott
 */

package data;

public class SubRule {

	private String name;
	private String varName;
	private int value;
	private boolean negated;

	/*
	 * Constructor
	 */

	public SubRule(String name, String varName, int value, boolean negated) {
		/**
		 * Constructor.
		 * 
		 * @param name
		 *            name of this SubRule (will be one of the
		 *            MembershipFunction names of the variable)
		 * @param varName
		 *            Name of the variable that this sub rule is under
		 * @param value
		 *            integer representing the "value" of the rule (i.e, which
		 *            membership function is being used, BASE ONE!)
		 * @param negated
		 *            boolean representing if the SubRule is inverted or not
		 */
		this.name = name;
		this.varName = varName;
		this.value = value;
		this.negated = negated;
	}

	/*
	 * Data Retreival Methods
	 */

	public String getVarName() {
		/**
		 * Returns this SubRule's Variable's name
		 * 
		 * @return String representing the name of the variable
		 */
		return varName;
	}

	public String getName() {
		/**
		 * Returns this SubRule's name
		 * 
		 * @return String repesenting the name of the sub rule
		 */
		return name;
	}

	public int getValue() {
		/**
		 * Returns this SubRule's value
		 * 
		 * @return integer value of the subrule
		 */
		return value;
	}

	public boolean isNegated() {
		/**
		 * Returns this SubRule's negation status
		 * 
		 * @return boolean whether or not the sub rule is negated
		 */
		return negated;
	}

	/*
	 * Data Assignment Methods
	 */

	public void setName(String name) {
		/**
		 * Sets the name field of this class to be the given name
		 * 
		 * @param name
		 *            String to be used as the SubRule name
		 */
		this.name = name;
	}

	public void setValue(int value) {
		/**
		 * Sets the value field of this class to be the given value
		 * 
		 * @param value
		 *            integer to be used as the SubRule value
		 */
		this.value = value;
	}

	public void setNegated(boolean negated) {
		/**
		 * Sets the negated field of this class to be the given value
		 * 
		 * @param negated
		 *            boolean to be used as the negated status
		 */
		this.negated = negated;
	}
}