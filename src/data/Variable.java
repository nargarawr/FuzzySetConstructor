/**
 * Variable Class 
 * Data storage class for variables, which are a collection of
 * MembershipFunctions.
 * 
 * @author Craig Knott
 */

package data;

import java.util.ArrayList;

public class Variable {

	private String name;
	private boolean input;
	private ArrayList<MembershipFunction> mfList;
	private double rangeMin;
	private double rangeMax;

	/*
	 * Constructor
	 */

	public Variable(String name, boolean input,
			ArrayList<MembershipFunction> mfList, double min, double max) {
		/**
		 * Constructor, assigns values to fields
		 * 
		 * @param name
		 *            String used to represent the Variable's name
		 * @param input
		 *            Boolean used to represent whether the Variable is an input
		 *            or an output
		 * @param mfList
		 *            ArrayList of MembershipFunctions
		 */

		if (name.equals("")) {
			this.name = "unnamed";
		} else {
			this.name = name;
		}

		this.input = input;
		this.mfList = mfList;
		this.rangeMin = min;
		this.rangeMax = max;
	}

	/*
	 * Data Retrieval
	 */

	public double getRangeMin() {
		return this.rangeMin;
	}

	public double getRangeMax() {
		return this.rangeMax;
	}

	public String getName() {
		/**
		 * Returns the name of the variable
		 * 
		 * @return String with name of the variable
		 */
		return name;
	}

	public boolean isInput() {
		/**
		 * Returns whether the variable is an input (true) or an ouput (false)
		 * 
		 * @return boolean whether or not the variable is an input
		 */
		return input;
	}

	public ArrayList<MembershipFunction> getMFs() {
		/**
		 * Returns the list of MembershipFunctions part of this variable
		 * 
		 * @return ArrayList of Membership functions of the variable
		 */
		return this.mfList;
	}

	public MembershipFunction getMfAtIndex(int i) {
		/**
		 * Returns the specific MembershipFunction at the given index
		 * 
		 * @param i
		 *            Index of the required MembershipFunction
		 * 
		 * @return Membership function at the given index
		 */
		return mfList.get(i);
	}

	/*
	 * Data Assignment Methods
	 */

	public void setName(String name) {
		/**
		 * Sets the name field of the Variable
		 * 
		 * @param name
		 *            A String to be used as the new name of the Variable
		 */
		this.name = name;
	}

	public void setInput(boolean input) {
		/**
		 * Sets the input/output status of the variable
		 * 
		 * @param input
		 *            Boolean representing whether or not the Variable is an
		 *            input
		 */
		this.input = input;
	}

}
