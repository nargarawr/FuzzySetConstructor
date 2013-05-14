/**
 * VariableEditor Class
 * Used to create and edit FIS Variables
 * 
 * @author Craig Knott, Luke Hovell 
 * 
 * Using the JFreeChart package, found here http://www.jfree.org/jfreechart/
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

import data.Constants;
import data.MembershipFunction;
import data.Variable;

public class VariableEditor {
	
	private JPanel content;

	// Declares the panels used
	private JPanel mfPanel;
	private ChartPanel chartPanel;

	// Declares elements of the main panel
	private JLabel varNameLabel;
	private JLabel rangeLabel;
	// private JLabel graphImageLabel;

	private JTextField varNameTextField;
	private JTextField rangeMinTextField;
	private JTextField rangeMaxTextField;

	private JButton addNewMF;

	private JFreeChart chart;

	private GridBagConstraints c;

	// Defines whether or not we are working with input or output variables
	private boolean input;

	private ArrayList<MembershipFunction> mfList;

	/*
	 * Constructors
	 */

	public VariableEditor(Variable v) {
		/**
		 * Constructor. Creates graphical content, and sets values taken from
		 * the passed in Variable object
		 * 
		 * @param v
		 *            a variable used to add values to input boxes
		 */

		this.mfList = v.getMFs();
		this.input = v.isInput();

		content = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		addGraphicalElements();

		varNameTextField.setText(v.getName());
		rangeMinTextField.setText(v.getRangeMin() + "");
		rangeMaxTextField.setText(v.getRangeMax() + "");

		if (mfList.size() >= 1) {
			displayChart();
		}

		JOptionPane.showMessageDialog(null, content, "Variable Editor",
				JOptionPane.PLAIN_MESSAGE);
		// content.remove(chartPanel);
	}

	public VariableEditor(boolean input) {
		/**
		 * Constructor for the variable editor class. Creates the main panel,
		 * adds the graphical elements, initialises data storage, and shows this
		 * all on a JOptionPane
		 * 
		 * @param input
		 *            boolean showing whether variable is an input or output
		 */

		this.input = input;

		/** Creating the content panel */
		content = new JPanel(new GridBagLayout());

		/** Creating the data storage arraylist */
		mfList = new ArrayList<MembershipFunction>();

		/** Adding graphical elements */
		c = new GridBagConstraints();
		addGraphicalElements();
		JOptionPane.showMessageDialog(null, content, "Variable Editor",
				JOptionPane.PLAIN_MESSAGE);
	}

	/*
	 * Data Retreival Method
	 */

	public Variable getVariable() throws NumberFormatException, Exception {
		/**
		 * Returns the variable is this editor window
		 * 
		 * @return Variable object created by the form
		 * 
		 * @throws NumberFormatException
		 *             When trying to read not numerical values for the min and
		 *             max range
		 */

		double rangeMin = Double.valueOf(rangeMinTextField.getText());
		double rangeMax = Double.valueOf(rangeMaxTextField.getText());

		if (rangeMin >= rangeMax) {
			throw new Exception();
		}

		Variable v = new Variable(varNameTextField.getText(), input, mfList,
				rangeMin, rangeMax);
		return v;

	}

	public String typeAsIntToString(int i) {
		/**
		 * Returns a String representing a membership function type, based on
		 * given index
		 * 
		 * @param i
		 *            integer representing membership function type, from the
		 *            MembershipFunction.getType() function
		 * @return String object representing the membership function type
		 */

		switch (i) {
		case (0):
			return "Gaussian: ";
		case (1):
			return "Gaussian B: ";
		case (2):
			return "Triangular: ";
		case (3):
			return "Trapezoidal: ";
		default:
			return "Error";
		}
	}

	/*
	 * Graphical Elements
	 */

	public void addGraphicalElements() {
		/**
		 * Creates the graphical elements of the system and adds them to the
		 * JPanel content.
		 */

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.weighty = 10;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;

		varNameLabel = new JLabel("Name");
		c.gridx = 0;
		c.gridy = 0;
		content.add(varNameLabel, c);

		varNameTextField = new JTextField();
		varNameTextField.addFocusListener(new FocusListener() {

			String firstValue;

			public void focusGained(FocusEvent arg0) {
				((JTextField) arg0.getComponent()).selectAll();
				firstValue = varNameTextField.getText();
			}

			public void focusLost(FocusEvent arg0) {
				if (!(firstValue.equals(varNameTextField.getText()))) {
					if (mfList.size() >= 1) {
						refreshView();
					}

				}
			}
		});

		varNameTextField.setMinimumSize(new Dimension(150, 30));
		varNameTextField.setPreferredSize(new Dimension(150, 30));
		c.gridx = 2;
		c.gridy = 0;
		content.add(varNameTextField, c);

		rangeLabel = new JLabel("Range");
		c.gridx = 0;
		c.gridy = 1;
		content.add(rangeLabel, c);

		rangeMinTextField = new JTextField();
		rangeMinTextField.addFocusListener(new FocusListener() {

			String firstValue;

			public void focusGained(FocusEvent arg0) {
				((JTextField) arg0.getComponent()).selectAll();
				firstValue = rangeMinTextField.getText();
			}

			public void focusLost(FocusEvent arg0) {
				if (!(firstValue.equals(rangeMinTextField.getText()))) {
					if (mfList.size() >= 1) {
						refreshView();
					}
				}
			}
		});
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 1;
		content.add(rangeMinTextField, c);

		rangeMaxTextField = new JTextField();
		rangeMaxTextField.addFocusListener(new FocusListener() {

			String firstValue;

			public void focusGained(FocusEvent arg0) {
				((JTextField) arg0.getComponent()).selectAll();
				firstValue = rangeMaxTextField.getText();
			}

			public void focusLost(FocusEvent arg0) {
				if (!(firstValue.equals(rangeMaxTextField.getText()))) {
					if (mfList.size() >= 1) {
						refreshView();
					}
				}
			}
		});
		c.gridx = 3;
		c.gridy = 1;
		content.add(rangeMaxTextField, c);

		mfPanel = createMFPanel();
		mfPanel.setBorder(BorderFactory
				.createTitledBorder("Membership Functions"));
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 3;
		content.add(mfPanel, c);

		content.setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	public void refreshView() {
		/**
		 * Closes the current JOptionPane and creates it a new, to deal with
		 * necessary resizing
		 */
		JOptionPane.getRootFrame().dispose();

		String varName = varNameTextField.getText();
		String minText = rangeMinTextField.getText();
		String maxText = rangeMaxTextField.getText();

		content = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		addGraphicalElements();

		varNameTextField.setText(varName);
		rangeMinTextField.setText(minText);
		rangeMaxTextField.setText(maxText);

		if (mfList.size() >= 1) {
			displayChart();
		}

		JOptionPane.showMessageDialog(null, content, "Variable Editor",
				JOptionPane.PLAIN_MESSAGE);
	}

	private JPanel createMFPanel() {
		/**
		 * Creates the MF Panel to display all membership functions in the data
		 * 
		 * @return JPanel with Membership function panel
		 */
		JPanel returnPanel = new JPanel(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.HORIZONTAL;
		d.ipadx = 10;
		d.ipady = 10;
		d.weightx = 10;
		d.weighty = 10;
		d.anchor = GridBagConstraints.CENTER;

		/**
		 * Uses the ArrayList mfLists to populate the Membership Function Panel
		 */
		int y = 0;

		for (MembershipFunction f : mfList) {
			d.gridx = 0;
			d.gridy = y;
			returnPanel.add(new JLabel(typeAsIntToString(f.getType())), d);

			d.gridx = 1;
			returnPanel.add(new JLabel(f.getName()), d);

			/**
			 * Adds action listeners to each of the buttons, using 'x' as a
			 * distinguisher between them
			 */
			JButton tempEdit = new JButton("Edit");
			final int x = y;
			tempEdit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					launchMFEditorWithValues(mfList.get(x), x);
				}
			});
			d.gridx = 2;
			returnPanel.add(tempEdit, d);

			JButton tempDel = new JButton("Delete");
			tempDel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					int returnValue = JOptionPane
							.showConfirmDialog(
									null,
									"Are you sure you want to delete this Membership Function?",
									"Deletion", JOptionPane.ERROR_MESSAGE);
					if (returnValue == JOptionPane.OK_OPTION) {
						mfPanel.setVisible(false);
						mfList.remove(x);

						mfPanel.removeAll();

						mfPanel = createMFPanel();
						mfPanel.setBorder(BorderFactory
								.createTitledBorder("Membership Functions"));
						c.fill = GridBagConstraints.BOTH;
						c.anchor = GridBagConstraints.NORTH;
						c.gridwidth = 5;
						c.gridx = 0;
						c.gridy = 3;
						content.add(mfPanel, c);

						mfPanel.setVisible(true);
						refreshView();
					}
				}
			});
			d.gridx = 3;
			returnPanel.add(tempDel, d);

			y++;
		}

		addNewMF = new JButton("New MF");
		d.gridx = 0;
		d.gridy = y;
		returnPanel.add(addNewMF, d);

		/** Reassign the button listener */
		assignButtonListener(new buttonListener());

		return returnPanel;
	}

	public void launchMFEditorWithValues(MembershipFunction mf, int x) {
		/**
		 * Launches an MF Editor window with a MembershipFunction used to
		 * populate data entry fields
		 * 
		 * @param mf
		 *            MembershipFunction used to populate MFEditor fields
		 * 
		 * @param x
		 *            integer representing the position of the membership
		 *            function being used
		 */
		MFEditor m = new MFEditor(mfList, mf);
		try {
			m.updateMF();
			MembershipFunction mff = m.getMF();

			mfList.add(x, mff);
			mfList.remove(x + 1);

			mfPanel.setVisible(false);
			mfPanel.removeAll();

			mfPanel = createMFPanel();
			mfPanel.setBorder(BorderFactory
					.createTitledBorder("Membership Functions"));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.NORTH;
			c.gridwidth = 5;
			c.gridx = 0;
			c.gridy = 3;
			content.add(mfPanel, c);

			mfPanel.setVisible(true);
			refreshView();

		} catch (NumberFormatException ex) {
			JOptionPane
					.showMessageDialog(
							null,
							"Please make sure you are entering numbers for these fields",
							"Number Format Required", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * Action Listeners
	 */

	private void assignButtonListener(buttonListener bl) {
		/**
		 * Creates the action listener on the "new" button
		 * 
		 * @param bl
		 *            a buttonListener object, used to listen for the button
		 *            presses
		 */

		addNewMF.addActionListener(bl);
		addNewMF.setActionCommand("btn_new");
	}

	class assignFocusListener implements FocusListener {

		/*
		 * Selecting all text in an input box when selecting it
		 */

		public void focusGained(FocusEvent arg0) {
			((JTextField) arg0.getComponent()).selectAll();
		}

		public void focusLost(FocusEvent arg0) {
		}

	}

	class buttonListener implements ActionListener {
		/**
		 * Sub class used to assign actions to buttons on the window
		 */

		public void actionPerformed(ActionEvent e) {
			/**
			 * Necessary method from implementing ActionListner
			 * 
			 * @param e
			 *            an ActionEvent that comes from an action on the form,
			 *            to be dealt with in the ActionListener
			 * 
			 */

			if (e.getActionCommand().equals("btn_new")) {
				/**
				 * If the "new" button is pressed, launch a new MFEditor, and
				 * extract the entered MembershipFunction from it. Add this to
				 * the Membership Function List, and refresh the display.
				 */
				MFEditor m = new MFEditor(mfList);
				try {
					m.updateMF();
					MembershipFunction mf = m.getMF();
					mfList.add(mf);

					mfPanel.setVisible(false);
					mfPanel.removeAll();

					mfPanel = createMFPanel();
					mfPanel.setBorder(BorderFactory
							.createTitledBorder("Membership Functions"));
					c.fill = GridBagConstraints.BOTH;
					c.anchor = GridBagConstraints.NORTH;
					c.gridwidth = 5;
					c.gridx = 0;
					c.gridy = 3;
					content.add(mfPanel, c);

					mfPanel.setVisible(true);
					refreshView();
				} catch (NumberFormatException ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure you are entering numbers for these fields",
									"Number Format Required",
									JOptionPane.ERROR_MESSAGE);
				}
			}

		}

	}

	/*
	 * Charting functions
	 */

	private XYDataset createDataset() {
		/**
		 * Creates a data set for all membership functions, using the plotter
		 * class
		 * 
		 * @return dataset object of all membership functions x and y values
		 */

		final XYSeriesCollection dataset = new XYSeriesCollection();

		for (MembershipFunction mf : mfList) {

			double rangeMin = Double.valueOf(rangeMinTextField.getText());
			double rangeMax = Double.valueOf(rangeMaxTextField.getText());

			Plotter p = new Plotter(mf, rangeMin, rangeMax);

			dataset.addSeries(p.getMFXYSeries());

		}

		return dataset;

	}

	public void displayChart() {
		/**
		 * Draws and add the chart to the content panel
		 */
		final XYDataset dataset = createDataset();

		chart = ChartFactory.createXYLineChart(varNameTextField.getText(),
				"Variable Value", "Truth Value", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		chartPanel = new ChartPanel(chart);

		if (mfList.size() > 0) {
			if (mfList.get(0).getType() == Constants.MEMBERSHIPFUNCTION_GAUSSIAN
					|| mfList.get(0).getType() == Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B) {
				chart.getXYPlot().setRenderer(new XYSplineRenderer());
			}
		}
		c.gridx = 10;
		c.gridheight = 15;
		c.gridy = 0;
		content.add(chartPanel, c);

	}

}
