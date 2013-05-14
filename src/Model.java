/**
 * Model Class
 * Used to co-ordinate access to stored data of the system
 * 
 * @author Craig Knott
 */

import java.util.ArrayList;

import data.Rule;
import data.Variable;

public class Model {

	private ArrayList<Variable> varInList;
	private ArrayList<Variable> varOutList;
	private ArrayList<Rule> ruleList;

	/*
	 * Constructor
	 */

	public Model() {
		/*
		 * Constructor
		 */
		varInList = new ArrayList<Variable>();
		varOutList = new ArrayList<Variable>();
		ruleList = new ArrayList<Rule>();
	}

	/*
	 * Data retrieval methods
	 */

	public ArrayList<Variable> getVarInList() {
		/**
		 * Returns the list of input variables
		 * 
		 * @return ArrayList<Variable>, of all system input variables
		 */
		return varInList;
	}

	public ArrayList<Variable> getVarOutList() {
		/**
		 * Returns the list of output variables
		 * 
		 * @return ArrayList<Variable>, of all system output variables
		 */
		return varOutList;
	}

	public ArrayList<Rule> getRuleList() {
		/**
		 * Returns the list of rules
		 * 
		 * @return ArrayList<Rule>, of all system rules
		 */
		return ruleList;
	}
}