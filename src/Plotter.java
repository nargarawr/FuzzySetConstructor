/**
 * Plotter class
 * Used to calculate, construct and return XYSeries data from membership functions
 * 
 * @author Craig Knott
 * 
 * Using the JFreeChart package, found here http://www.jfree.org/jfreechart/
 */

import org.jfree.data.xy.XYSeries;
import data.Constants;

import data.MembershipFunction;

public class Plotter {

	private MembershipFunction mf;
	private double rangeMin;
	private double rangeMax;

	/*
	 * Constructor
	 */

	public Plotter(MembershipFunction mf, double rangeMin, double rangeMax) {
		/**
		 * Constructor
		 * 
		 * @param mf
		 *            membership function to be plotted
		 * 
		 * @param rangeMin
		 *            double representing min range of the graph
		 * 
		 * @param rangeMax
		 *            double representing max range of the graph
		 * 
		 */

		this.mf = mf;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
	}

	public double[] getYValues(int interval) {
		/**
		 * Returns a double array of y values, which has size of interval
		 * 
		 * @param interval
		 *            int to determine size of array
		 * 
		 * @return double[] with the y values of the graph
		 * 
		 */

		double[] yValues = new double[interval + 1];

		switch (mf.getType()) {
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN):
			yValues = calculateGaussian(interval);
			break;
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B):
			yValues = calculateGaussianB(interval);
			break;
		case (Constants.MEMBERSHIPFUNCTION_TRIANGULAR):
			yValues = calculateTriangular(interval);
			break;
		case (Constants.MEMBERSHIPFUNCTION_TRAPEZOIDAL):
			yValues = calculateTrapezoidal(interval);
			break;
		}

		return yValues;
	}

	public XYSeries getMFXYSeries() {
		/**
		 * Returns theXYSeries data of a membership function, which is
		 * calculated on the fly
		 * 
		 * @return XYSeries object of the X and Y points of the membership
		 *         function
		 */

		final XYSeries series = new XYSeries(mf.getName());

		int interval = (int) (rangeMax - rangeMin);

		double[] yValues = getYValues(interval);

		int j = 0;
		for (int i = (int) rangeMin; i <= rangeMax; i++, j++) {
			series.add(i, yValues[j]);
		}

		return series;
	}

	public double[] calculateGaussian(int interval) {
		/**
		 * Returns a double array of y values for a gaussian membership function
		 * 
		 * @param interval
		 *            int to determine size of array
		 * 
		 * @return double[] with the y values of the graph
		 * 
		 */

		double[] values = new double[interval + 1];
		double sigma = mf.getParameter(0);
		double mean = mf.getParameter(1);
		double height = mf.getParameter(2);

		int j = 0;
		for (int i = (int) rangeMin; i <= (int) rangeMax; i++, j++) {
			values[j] = (height * Math
					.exp(-((Math.pow(i - mean, 2) / (2 * (Math.pow(sigma, 2)))))));
		}

		return values;
	}

	public double[] calculateGaussianB(int interval) {
		/**
		 * Returns a double array of y values for a gaussian b membership
		 * function
		 * 
		 * @param interval
		 *            int to determine size of array
		 * 
		 * @return double[] with the y values of the graph
		 * 
		 */

		double[] values = new double[interval + 1];

		double leftSigma = mf.getParameter(0);
		double leftMean = mf.getParameter(1);
		double rightSigma = mf.getParameter(2);
		double rightMean = mf.getParameter(3);
		double height = mf.getParameter(4);

		int lastLeft = 0;

		int j = 0;
		for (int i = (int) rangeMin; i <= (int) rangeMax; i++, j++) {
			if (i <= leftMean) {
				values[j] = (height * Math
						.exp(-((Math.pow(i - leftMean, 2) / (2 * (Math.pow(
								leftSigma, 2)))))));
				lastLeft = j;
			} else if (i >= rightMean) {
				values[j] = (height * Math
						.exp(-((Math.pow(i - rightMean, 2) / (2 * (Math.pow(
								rightSigma, 2)))))));
			}
		}
		values[lastLeft + 1] = (values[lastLeft] + values[lastLeft + 2]) / 2;

		return values;

	}

	public double[] calculateTrapezoidal(int interval) {
		/**
		 * Returns a double array of y values for a trapezoidal membership
		 * function
		 * 
		 * @param interval
		 *            int to determine size of array
		 * 
		 * @return double[] with the y values of the graph
		 * 
		 */

		double[] values = new double[interval + 1];

		double leftFoot = mf.getParameter(0);
		double leftShoulder = mf.getParameter(1);
		double rightShoulder = mf.getParameter(2);
		double rightFoot = mf.getParameter(3);
		double height = mf.getParameter(4);
		
		int j = 0;
		for (int i = (int) rangeMin; j < values.length; i++, j++) {
			double a = (i - leftFoot) / (leftShoulder - leftFoot);
			double b = 1.0;
			double c = (rightFoot - i) / (rightFoot - rightShoulder);

			double e = height * Math.max(Math.min(Math.min(a, b), c), 0);
			
			if(Double.isNaN(e)){
				e = 1;
			}
			
			values[j] = e; 

		}

		return values;
	}

	public double[] calculateTriangular(int interval) {
		/**
		 * Returns a double array of y values for a triangular membership
		 * function
		 * 
		 * @param interval
		 *            int to determine size of array
		 * 
		 * @return double[] with the y values of the graph
		 * 
		 */

		double[] values = new double[interval + 1];

		double left = mf.getParameter(0);
		double mean = mf.getParameter(1);
		double right = mf.getParameter(2);
		double height = mf.getParameter(3);

		int j = 0;
		for (int i = (int) rangeMin; j < values.length; i++, j++) {
			double a = (i - left) / (mean - left);
			double b = (right - i) / (right - mean);

			values[j] = height * (Math.max(Math.min(a, b), 0));
		}

		return values;

	}

}
