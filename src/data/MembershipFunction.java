/**
 * MembershipFunction Class
 * Data storage class for MembershipFunctions
 * Currently supports Guassian, Gaussian b, Triangular and Trapezoidal
 * 
 * @author Craig Knott
 */

package data;

public class MembershipFunction {

	/**
	 * Generic "membership function" class, that has an array of parameters,
	 * that is dependant on the membership function it represents
	 */

	String name;
	int type;
	double[] parameters;

	/*
	 * Constructor
	 */
	public MembershipFunction(String name, int i, double[] params) {
		/**
		 * Constructor
		 * 
		 * @param name
		 *            String representing the MembershipFunction's name
		 * @param i
		 *            integer representing the type (see finals declared above)
		 * @param params
		 *            array of doubles, parameters used to generate this
		 *            function
		 */

		this.name = name;
		this.type = i;

		/**
		 * Dependant on type given, constructor sets up a relevant membership
		 * function
		 */
		switch (type) {
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN):
			parameters = new double[3];
			gaussianCreator(params);
			break;
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B):
			parameters = new double[5];
			gaussianBCreator(params);
			break;
		case (Constants.MEMBERSHIPFUNCTION_TRIANGULAR):
			parameters = new double[4];
			triangularCreator(params);
			break;
		case (Constants.MEMBERSHIPFUNCTION_TRAPEZOIDAL):
			parameters = new double[5];
			trapezoidalCreator(params);
			break;
		}
	}

	/*
	 * Data Retrieval Methods
	 */

	public double getParameter(int i) {
		/**
		 * Returns a specific parameter
		 * 
		 * @param i
		 *            the index of the parameter to retreive
		 * 
		 * @return double, specific requested parameter
		 */

		return parameters[i];
	}

	public int getType() {
		/**
		 * Returns the type of the function
		 * 
		 * @return integer, type of the function
		 */
		return this.type;
	}

	public String getName() {
		/**
		 * Returns the name of the function
		 * 
		 * @return String representing the name of the function
		 */
		return this.name;
	}

	public int getParametersSize() {
		/**
		 * Returns the length of the parameters array
		 * 
		 * @return integer representing the length of the parameters array
		 */
		return parameters.length;
	}

	public void setName(String s) {
		/**
		 * Sets the name of this membership function
		 * 
		 * @param s
		 *            String to set as the name of the membership function
		 */
		this.name = s;
		
	}

	/*
	 * Function creation methods
	 */

	public void gaussianCreator(double[] params) {
		/**
		 * Creates a gaussian function
		 * 
		 * @param params
		 *            array of doubles used to represent the function parameters
		 */

		parameters[0] = params[0];
		parameters[1] = params[1];
		parameters[2] = params[2];
	}

	public void gaussianBCreator(double[] params) {
		/**
		 * Creates a gaussian b function
		 * 
		 * @param params
		 *            array of doubles used to represent the function parameters
		 */

		parameters[0] = params[0];
		parameters[1] = params[1];
		parameters[2] = params[2];
		parameters[3] = params[3];
		parameters[4] = params[4];
	}

	public void triangularCreator(double[] params) {
		/**
		 * Creates a triangular function
		 * 
		 * @param params
		 *            array of doubles used to represent the function parameters
		 */

		parameters[0] = params[0];
		parameters[1] = params[1];
		parameters[2] = params[2];
		parameters[3] = params[3];
	}

	public void trapezoidalCreator(double[] params) {
		/**
		 * Creates a trapezoidal function
		 * 
		 * @param params
		 *            array of doubles used to represent the function parameters
		 */

		parameters[0] = params[0];
		parameters[1] = params[1];
		parameters[2] = params[2];
		parameters[3] = params[3];
		parameters[4] = params[4];
	}

	/*
	 * Auxiliary Methods
	 */

	public String intToType(int type) {
		/**
		 * Returns a string representation of a given type number
		 * 
		 * @param type
		 *            integer representing the type of a membership function
		 * 
		 * @return a String representing the type inputted
		 * 
		 */
		switch (type) {
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN):
			return "gaussmf";
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B):
			return "gaussbmf";
		case (Constants.MEMBERSHIPFUNCTION_TRIANGULAR):
			return "trimf";
		case (Constants.MEMBERSHIPFUNCTION_TRAPEZOIDAL):
			return "trapmf";
		default:
			return null;
		}

	}
}
