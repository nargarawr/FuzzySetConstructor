package data;

public class Constants {

	public static final double SYSTEM_VERSION = 2.0;

	/*
	 * Defuzzification constants
	 */
	public static final int DEFUZZ_CENTROID = 1;
	public static final int DEFUZZ_BISECTOR = 2;
	public static final int DEFUZZ_MOM = 3;
	public static final int DEFUZZ_SOM = 4;
	public static final int DEFUZZ_LOM = 5;

	/*
	 * Indicates an inputless rule
	 */
	public static final String BAD_RULE = "IF  THEN<br>";

	/*
	 * Membership function constants
	 */
	public static final int MEMBERSHIPFUNCTION_GAUSSIAN = 0;
	public static final int MEMBERSHIPFUNCTION_GAUSSIAN_B = 1;
	public static final int MEMBERSHIPFUNCTION_TRIANGULAR = 2;
	public static final int MEMBERSHIPFUNCTION_TRAPEZOIDAL = 3;

	/*
	 * Connectives
	 */

	public static final int CONNECTIVE_AND = 1;
	public static final int CONNECTIVE_OR = 2;
	
	/*
	 * Fail safes
	 */
	
	public static int failsafe_counter = 0;
	}
