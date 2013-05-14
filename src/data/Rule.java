/**
 * Rule Class 
 * Data storage class for Rules, which are a 
 * logical statements defined with Membershipfunction
 * values and are used in the analysis of Fuzzy Logic
 * System.
 * 
 * @author Craig Knott
 */

package data;

import java.util.ArrayList;

public class Rule {

	private ArrayList<SubRule> inputs;
	private ArrayList<SubRule> outputs;

	private double weight;
	private int connective;

	/*
	 * Constructor
	 */

	public Rule(ArrayList<SubRule> inputs, ArrayList<SubRule> outputs,
			double weight, int connective) {
		/**
		 * Constructor
		 * 
		 * @param inputs
		 *            Arraylist of SubRules to be used as the inputs of the rule
		 * @param ouputs
		 *            Arraylist of SubRules to be used as the ouputs of the rule
		 * @param weight
		 *            double representing the weight of the rule (between 0 and
		 *            1)
		 * @param connective
		 *            integer representing the connective of the rules (1 = AND,
		 *            2 = OR)
		 * 
		 */
		this.inputs = inputs;
		this.outputs = outputs;
		this.weight = weight;
		this.connective = connective;

	}

	public boolean thereAreOthers(int i, boolean input) {
		if (input) {
			if (i + 1 == inputs.size()) {
				return false;
			}

			for (int j = i + 1; j < inputs.size(); j++) {
				if (inputs.get(j).getValue() > 1) {
					return true;
				}
			}

		} else {
			if (i + 1 == outputs.size()) {
				return false;
			}

			for (int j = i + 1; j < outputs.size(); j++) {
				if (outputs.get(j).getValue() > 1) {
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * Override Methods
	 */

	@Override
	public String toString() {
		/**
		 * Displays the rule as a string in the format IF x AND/OR y THEN z
		 * 
		 * @return String representation of the Rule
		 */

		StringBuilder sb = new StringBuilder("IF ");

		String connectiveString = null;
		if (connective == Constants.CONNECTIVE_AND) {
			connectiveString = " AND<br>";
		} else if (connective == Constants.CONNECTIVE_OR) {
			connectiveString = " OR<br>";
		}

		for (int i = 0; i < inputs.size(); i++) {
			if (inputs.get(i).getValue() > 1) {
				sb.append(inputs.get(i).getVarName() + " IS ");
				if (inputs.get(i).isNegated()) {
					sb.append("NOT ");
				}
				sb.append(inputs.get(i).getName());
				if (thereAreOthers(i, true)) {
					sb.append(connectiveString);
				}
			}
		}

		sb.append(" THEN<br>");

		for (int i = 0; i < outputs.size(); i++) {
			if (outputs.get(i).getValue() > 1) {
				sb.append(outputs.get(i).getVarName() + " IS ");
				if (outputs.get(i).isNegated()) {
					sb.append("NOT ");
				}
				sb.append(outputs.get(i).getName());
				if (thereAreOthers(i, false)) {
					sb.append(connectiveString);
				}
			}
		}

		return sb.toString();
	}

	public String asNumberString() {
		/**
		 * Displays the rule as a String in the a b, c (d) : e
		 * 
		 * @return String representation of the Rule
		 */

		StringBuilder sb = new StringBuilder();

		/*
		 * Gather input values
		 */

		for (int i = 0; i < inputs.size(); i++) {
			int k = inputs.get(i).getValue();
			if (k == 0) {
				sb.append(0);
			} else {
				k--;

				if (inputs.get(i).isNegated()) {
					sb.append("-");
				}
				sb.append(k);
			}
			if (i + 1 != inputs.size()) {
				sb.append(" ");
			}
		}

		sb.append(", ");

		/*
		 * Gather output values
		 */
		for (int i = 0; i < outputs.size(); i++) {
			int k = outputs.get(i).getValue();
			if (k == 0) {
				sb.append(0 + " ");
			} else {
				k--;
				if (outputs.get(i).isNegated()) {
					sb.append("-");
				}
				sb.append(k + " ");
			}

		}

		/*
		 * Gather weight and connective.
		 */

		sb.append("(" + weight + ") ");

		sb.append(": " + connective + "\n");

		return sb.toString();
	}

	public ArrayList<SubRule> getInputs() {
		/**
		 * Returns the list of Input SubRules
		 * 
		 * @return Array of input sub rules
		 */
		return inputs;
	}

	public ArrayList<SubRule> getOutputs() {
		/**
		 * Returns the list of Output SubRules
		 * 
		 * @return Array of output sub rules
		 */
		return outputs;
	}

	/*
	 * Data Retreival Methods
	 */

	public double getWeight() {
		/**
		 * Returns the weight of the rule
		 * 
		 * @return double, the weight of the rule (0-1)
		 */
		return weight;
	}

	public int getConnective() {
		/**
		 * Returns the connective of the rule
		 * 
		 * @return integer, the connective used in the rule (1 or 2)
		 */
		return connective;
	}

	/*
	 * Data Assignements Methods
	 */

	public void setWeight(double weight) {
		/**
		 * Sets the weight of the rule
		 * 
		 * @param weight
		 *            double between 0 and 1, to set as the weight
		 */
		this.weight = weight;
	}

	public void setConnective(int connective) {
		/**
		 * Sets the connective of the rule
		 * 
		 * @param connective
		 *            integer representing the connective, (1 = AND, 2 = OR)
		 */
		this.connective = connective;
	}
}
