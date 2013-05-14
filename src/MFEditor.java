// Nathan

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import data.Constants;
import data.MembershipFunction;

public class MFEditor {

	/*
	 * Declaration of graphical elements
	 */
	private JPanel content;
	private JPanel parameterPanel;

	private JLabel nameLabel;
	private JTextField nameTextField;
	private JLabel functionTypeLabel;

	private JTextField inputOne;
	private JTextField inputTwo;
	private JTextField inputThree;
	private JTextField inputFour;
	private JTextField inputFive;

	/*
	 * Declaraction of function type combo box, with options
	 */

	private JComboBox<String> functionTypeCombo;
	final String[] mfNameList = { "Gaussian Curve", "Gaussian B Curve",
			"Triangular Function", "Trapezoidal Function" };

	/*
	 * The membership function being created
	 */
	private MembershipFunction mf;

	String original;

	private ArrayList<MembershipFunction> mfList;

	/*
	 * Constructors
	 */

	public MFEditor(ArrayList<MembershipFunction> mfList) {
		/**
		 * Default Constructor
		 * 
		 * @param mfList
		 *            other membership functions of this variable
		 */
		this.mfList = mfList;
		content = new JPanel(new GridBagLayout());
		addGUIElements();

		JOptionPane.showMessageDialog(null, content,
				"Membership Function Creator", JOptionPane.PLAIN_MESSAGE);

	}

	public MFEditor(ArrayList<MembershipFunction> mfList, MembershipFunction mff) {
		/**
		 * Secondary constructor, takes an initial MembershipFunction to
		 * populate the fields on the form.
		 * 
		 * @param mfList
		 *            other membership functions of this variable
		 * 
		 * @param mff
		 *            membership function to be edited
		 * 
		 */
		this.mfList = mfList;
		content = new JPanel(new GridBagLayout());
		addGUIElements();

		/*
		 * Extracts values from mff and displays them
		 */

		nameTextField.setText(mff.getName());

		int type = mff.getType();

		functionTypeCombo.setSelectedIndex(mff.getType());

		inputOne.setText(Double.toString(mff.getParameter(0)));
		inputTwo.setText(Double.toString(mff.getParameter(1)));
		inputThree.setText(Double.toString(mff.getParameter(2)));

		if (type == Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B
				|| type == Constants.MEMBERSHIPFUNCTION_TRIANGULAR
				|| type == Constants.MEMBERSHIPFUNCTION_TRAPEZOIDAL) {
			inputFour.setText(Double.toString(mff.getParameter(3)));
		}

		if (type == Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B
				|| type == Constants.MEMBERSHIPFUNCTION_TRAPEZOIDAL) {
			inputFive.setText(Double.toString(mff.getParameter(4)));
		}

		original = nameTextField.getText();

		JOptionPane.showMessageDialog(null, content,
				"Membership Function Creator", JOptionPane.PLAIN_MESSAGE);

	}

	public MembershipFunction getMF() {
		return this.mf;
	}

	public void updateMF() {
		/**
		 * Updates the Membership function stored to reflect current input
		 * values.
		 */
		int i = functionTypeCombo.getSelectedIndex();
		double[] params = null;

		switch (i) {
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN):
			params = new double[3];
			params[0] = Double.valueOf(inputOne.getText());
			params[1] = Double.valueOf(inputTwo.getText());
			params[2] = Double.valueOf(inputThree.getText());
			break;
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B):
			params = new double[5];
			params[0] = Double.valueOf(inputOne.getText());
			params[1] = Double.valueOf(inputTwo.getText());
			params[2] = Double.valueOf(inputThree.getText());
			params[3] = Double.valueOf(inputFour.getText());
			params[4] = Double.valueOf(inputFive.getText());
			break;
		case (Constants.MEMBERSHIPFUNCTION_TRIANGULAR):
			params = new double[4];
			params[0] = Double.valueOf(inputOne.getText());
			params[1] = Double.valueOf(inputTwo.getText());
			params[2] = Double.valueOf(inputThree.getText());
			params[3] = Double.valueOf(inputFour.getText());
			break;
		case (Constants.MEMBERSHIPFUNCTION_TRAPEZOIDAL):
			params = new double[5];
			params[0] = Double.valueOf(inputOne.getText());
			params[1] = Double.valueOf(inputTwo.getText());
			params[2] = Double.valueOf(inputThree.getText());
			params[3] = Double.valueOf(inputFour.getText());
			params[4] = Double.valueOf(inputFive.getText());
			break;
		}

		String name = nameTextField.getText();

		if (name.equals("")) {
			name = "unnamed" + Constants.failsafe_counter;
			Constants.failsafe_counter++;
		}

		mf = new MembershipFunction(name, i, params);
	}

	public void addGUIElements() {

		/**
		 * Creates and add all graphical elements to the content JPanel
		 */

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.anchor = GridBagConstraints.NORTH;

		nameLabel = new JLabel("Identification Name");
		c.gridx = 0;
		c.gridy = 0;
		content.add(nameLabel, c);

		nameTextField = new JTextField();
		nameTextField
				.addFocusListener(new assignIndentificationNameFocusListener());
		nameTextField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					e.consume();
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

		});

		c.gridx = 1;
		c.gridy = 0;
		content.add(nameTextField, c);

		functionTypeLabel = new JLabel("Function Type");
		c.gridx = 0;
		c.gridy = 1;
		content.add(functionTypeLabel, c);

		/*
		 * Create and populate the Combobox
		 */
		functionTypeCombo = new JComboBox<String>();
		for (int i = 0; i < mfNameList.length; i++) {
			functionTypeCombo.addItem(mfNameList[i]);
		}
		c.gridx = 1;
		c.gridy = 1;

		/*
		 * Add listeners to the combobox for item change, so that membership
		 * function input elements can be generated
		 */

		functionTypeCombo
				.setSelectedIndex(Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B);

		functionTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {

				GridBagConstraints c = new GridBagConstraints();
				c.anchor = GridBagConstraints.CENTER;
				c.fill = GridBagConstraints.CENTER;
				c.gridwidth = 2;
				c.weighty = 10;
				c.gridx = 0;
				c.gridy = 3;

				parameterPanel.setVisible(false);

				parameterPanel = generateParameterPanel(functionTypeCombo
						.getSelectedIndex());

				parameterPanel.setBorder(BorderFactory
						.createTitledBorder("Parameters"));

				parameterPanel.setVisible(true);

				content.add(parameterPanel, c);
			}
		});

		content.add(functionTypeCombo, c);

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		parameterPanel = generateParameterPanel(Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B);
		parameterPanel
				.setBorder(BorderFactory.createTitledBorder("Parameters"));
		content.add(parameterPanel, c);
		content.setMinimumSize(new Dimension(310, 310));
		content.setBorder(new EmptyBorder(10, 10, 10, 10));

	}

	private JPanel generateParameterPanel(int i) {
		/**
		 * Create a JPanel that allows for the creation of membership functions
		 * 
		 * @param i
		 *            integer representing the type of the membership function
		 * 
		 * @return JPanel containing the generated graphical elements
		 */

		JPanel returnPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.anchor = GridBagConstraints.NORTH;

		/**
		 * Creates the relevant input elements that can be used to make up each
		 * of the membership functions. No difficult code, just lots of gridbag
		 * construction
		 */

		switch (i) {
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN):
			// Create inputs for the Gaussian function

			c.gridx = 0;
			c.gridy = 0;
			returnPanel.add(new JLabel("Sigma"), c);

			inputOne = new JTextField();
			inputOne.setMinimumSize(new Dimension(150, 30));
			inputOne.setPreferredSize(new Dimension(150, 30));
			c.gridx = 1;
			c.gridy = 0;
			returnPanel.add(inputOne, c);

			c.gridx = 0;
			c.gridy = 1;
			returnPanel.add(new JLabel("Mean"), c);

			inputTwo = new JTextField();
			c.gridx = 1;
			c.gridy = 1;
			returnPanel.add(inputTwo, c);

			c.gridx = 0;
			c.gridy = 2;
			returnPanel.add(new JLabel("Height"), c);

			inputThree = new JTextField();
			c.gridx = 1;
			c.gridy = 2;
			returnPanel.add(inputThree, c);
			break;
		case (Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B):
			// Create inputs for the Gaussian B function
			c.gridx = 0;
			c.gridy = 0;
			returnPanel.add(new JLabel("Left Sigma"), c);

			inputOne = new JTextField();
			inputOne.setMinimumSize(new Dimension(150, 30));
			inputOne.setPreferredSize(new Dimension(150, 30));
			c.gridx = 1;
			c.gridy = 0;
			returnPanel.add(inputOne, c);

			c.gridx = 0;
			c.gridy = 1;
			returnPanel.add(new JLabel("Left Mean"), c);

			inputTwo = new JTextField();
			c.gridx = 1;
			c.gridy = 1;
			returnPanel.add(inputTwo, c);

			c.gridx = 0;
			c.gridy = 3;
			returnPanel.add(new JLabel("Right Sigma"), c);

			inputThree = new JTextField();
			c.gridx = 1;
			c.gridy = 3;
			returnPanel.add(inputThree, c);

			c.gridx = 0;
			c.gridy = 4;
			returnPanel.add(new JLabel("Right Mean"), c);

			inputFour = new JTextField();
			inputFour.addFocusListener(new assignFocusListener());
			c.gridx = 1;
			c.gridy = 4;
			returnPanel.add(inputFour, c);

			c.gridx = 0;
			c.gridy = 5;
			returnPanel.add(new JLabel("Height"), c);

			inputFive = new JTextField();
			inputFive.addFocusListener(new assignFocusListener());
			c.gridx = 1;
			c.gridy = 5;
			returnPanel.add(inputFive, c);
			break;
		case (Constants.MEMBERSHIPFUNCTION_TRIANGULAR):
			// Create inputs for the triangular function
			c.gridx = 0;
			c.gridy = 0;
			returnPanel.add(new JLabel("Left"), c);

			inputOne = new JTextField();
			inputOne.setMinimumSize(new Dimension(150, 30));
			inputOne.setPreferredSize(new Dimension(150, 30));
			c.gridx = 1;
			c.gridy = 0;
			returnPanel.add(inputOne, c);

			c.gridx = 0;
			c.gridy = 1;
			returnPanel.add(new JLabel("Mean"), c);

			inputTwo = new JTextField();
			c.gridx = 1;
			c.gridy = 1;
			returnPanel.add(inputTwo, c);

			c.gridx = 0;
			c.gridy = 3;
			returnPanel.add(new JLabel("Right"), c);

			inputThree = new JTextField();
			c.gridx = 1;
			c.gridy = 3;
			returnPanel.add(inputThree, c);

			c.gridx = 0;
			c.gridy = 4;
			returnPanel.add(new JLabel("Height"), c);

			inputFour = new JTextField();
			inputFour.addFocusListener(new assignFocusListener());
			c.gridx = 1;
			c.gridy = 4;
			returnPanel.add(inputFour, c);
			break;

		case (Constants.MEMBERSHIPFUNCTION_TRAPEZOIDAL):
			// Create inputs for the trapezoidal function
			c.gridx = 0;
			c.gridy = 0;
			returnPanel.add(new JLabel("Left Foot"), c);

			inputOne = new JTextField();
			inputOne.setMinimumSize(new Dimension(150, 30));
			inputOne.setPreferredSize(new Dimension(150, 30));
			c.gridx = 1;
			c.gridy = 0;
			returnPanel.add(inputOne, c);

			c.gridx = 0;
			c.gridy = 1;
			returnPanel.add(new JLabel("Left Shoulder"), c);

			inputTwo = new JTextField();
			c.gridx = 1;
			c.gridy = 1;
			returnPanel.add(inputTwo, c);

			c.gridx = 0;
			c.gridy = 3;
			returnPanel.add(new JLabel("Right Shoulder"), c);

			inputThree = new JTextField();
			c.gridx = 1;
			c.gridy = 3;
			returnPanel.add(inputThree, c);

			c.gridx = 0;
			c.gridy = 4;
			returnPanel.add(new JLabel("Right Foot"), c);

			inputFour = new JTextField();
			inputFour.addFocusListener(new assignFocusListener());
			c.gridx = 1;
			c.gridy = 4;
			returnPanel.add(inputFour, c);

			c.gridx = 0;
			c.gridy = 5;
			returnPanel.add(new JLabel("Height"), c);

			inputFive = new JTextField();
			inputFive.addFocusListener(new assignFocusListener());
			c.gridx = 1;
			c.gridy = 5;
			returnPanel.add(inputFive, c);
			break;
		}
		inputOne.addFocusListener(new assignFocusListener());
		inputTwo.addFocusListener(new assignFocusListener());
		inputThree.addFocusListener(new assignFocusListener());

		return returnPanel;
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

	class assignIndentificationNameFocusListener implements FocusListener {

		/*
		 * Specific listener to identification name to make sure no repeated
		 * names are created
		 */

		public void focusGained(FocusEvent arg0) {
			((JTextField) arg0.getComponent()).selectAll();
		}

		public void focusLost(FocusEvent arg0) {

			for (MembershipFunction mf : mfList) {

				String s = nameTextField.getText();

				if (s.equals(mf.getName()) && !(s.equals(original))) {

					nameTextField.setText(s + Constants.failsafe_counter);
					Constants.failsafe_counter++;

				}
			}

		}
	}
}
