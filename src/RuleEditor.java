/**
 * RuleEditor Class
 * Used to create and manipulate FIS Rules
 * 
 * @author Craig Knott
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import data.Rule;
import data.SubRule;
import data.Variable;

public class RuleEditor {

	/*
	 * Rule Editor Data
	 */

	private ArrayList<Variable> inputList;
	private ArrayList<Variable> outputList;
	private ArrayList<JList<String>> inputJListCollection;
	private ArrayList<JList<String>> outputJListCollection;
	private ArrayList<JCheckBox> checkboxList;

	/*
	 * Graphical Elements
	 */

	private JPanel content;

	private JPanel inputsPanel;
	private JPanel outputsPanel;
	private JPanel bottomPanel;

	private JTextField weightInput;

	private ButtonGroup group;

	private JRadioButton radioAND;
	private JRadioButton radioOR;

	/*
	 * Constructors
	 */

	public RuleEditor(FISEditor f) {
		/**
		 * Default Constructor
		 * 
		 * @param f
		 *            FISEditor used to extract information from
		 */
		this.inputList = f.getInputs();
		this.outputList = f.getOutputs();
		inputJListCollection = new ArrayList<JList<String>>();
		outputJListCollection = new ArrayList<JList<String>>();
		checkboxList = new ArrayList<JCheckBox>();

		content = new JPanel(new GridBagLayout());
		addGUIElements();

		JOptionPane.showMessageDialog(null, content, "Rule Creator",
				JOptionPane.PLAIN_MESSAGE);
	}

	public RuleEditor(Rule r, FISEditor f) {
		/**
		 * Secondary Constructor, takes an initial rule and populates the entry
		 * fields with it's values
		 * 
		 * @param r
		 *            Rule used to populate input fields
		 * 
		 * @param f
		 *            FISEditor used to extract information from
		 */
		this.inputList = f.getInputs();
		this.outputList = f.getOutputs();
		inputJListCollection = new ArrayList<JList<String>>();
		outputJListCollection = new ArrayList<JList<String>>();
		checkboxList = new ArrayList<JCheckBox>();

		content = new JPanel(new GridBagLayout());
		addGUIElements();

		/**
		 * Assigns values from the Rule, r, into the entry fields
		 */

		for (int i = 0; i < inputJListCollection.size(); i++) {
			inputJListCollection.get(i).setSelectedIndex(
					r.getInputs().get(i).getValue() - 1);
			checkboxList.get(i).setSelected(r.getInputs().get(i).isNegated());
		}

		for (int i = 0; i < outputJListCollection.size(); i++) {
			outputJListCollection.get(i).setSelectedIndex(
					r.getOutputs().get(i).getValue() - 1);
			checkboxList.get(i + inputJListCollection.size()).setSelected(
					r.getOutputs().get(i).isNegated());
		}

		if (r.getConnective() == 2) {
			radioOR.setSelected(true);
		}

		weightInput.setText(r.getWeight() + "");

		JOptionPane.showMessageDialog(null, content, "Rule Creator",
				JOptionPane.PLAIN_MESSAGE);
	}

	/*
	 * Creation of Graphical elements
	 */

	public void addGUIElements() {
		/**
		 * Adds the graphical elements to the system. Mostly just gridbag
		 * constraints and formatting. Calls sub methods for input, output, and
		 * bottom panels
		 */
		content = new JPanel();
		content.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;

		inputsPanel = new JPanel();
		inputsPanel.setBorder(BorderFactory.createTitledBorder("Inputs"));

		createInputsPanel();
		c.gridwidth = 4;
		c.gridheight = 2;
		c.weightx = 12;
		c.weighty = 12;
		c.gridx = 0;
		c.gridy = 0;
		JScrollPane scrollPaneInputs = new JScrollPane(inputsPanel);
		scrollPaneInputs.setPreferredSize(new Dimension(300, 300));
		content.add(scrollPaneInputs, c);

		outputsPanel = new JPanel();
		outputsPanel.setBorder(BorderFactory.createTitledBorder("Outputs"));
		createOutputPanel();
		c.gridwidth = 2;
		c.weightx = 10;
		c.weighty = 10;
		c.gridx = 4;
		c.gridy = 0;
		JScrollPane scrollPaneOutput = new JScrollPane(outputsPanel);
		scrollPaneOutput.setPreferredSize(new Dimension(300, 300));
		content.add(scrollPaneOutput, c);

		bottomPanel = new JPanel();
		bottomPanel.setBorder(BorderFactory.createTitledBorder(""));
		createBottomPanel();
		c.gridheight = 1;
		c.gridwidth = 6;
		c.weightx = 3;
		c.weighty = 10;
		c.gridx = 0;
		c.gridy = 2;
		content.add(bottomPanel, c);
	}

	private void createBottomPanel() {
		/**
		 * Creates the bottom panel
		 */
		bottomPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);

		radioAND = new JRadioButton("AND");
		radioOR = new JRadioButton("OR");

		group = new ButtonGroup();
		group.add(radioAND);
		group.add(radioOR);

		JPanel connectivePanel = new JPanel();
		connectivePanel.setLayout(new GridLayout(2, 1));
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 0;
		connectivePanel.add(radioAND, c);

		c.gridx = 0;
		c.gridy = 1;
		connectivePanel.add(radioOR, c);

		radioAND.setSelected(true);

		c.gridx = 0;
		c.gridy = 0;
		bottomPanel.add(new JLabel("Connective"), c);

		c.gridx = 1;
		bottomPanel.add(connectivePanel, c);

		c.gridx = 0;
		c.gridy = 1;
		bottomPanel.add(new JLabel("Weight"), c);

		/**
		 * Creates the weightInput field. Has a focus listener, so that the user
		 * cannot leave the textfield without a valid number in it
		 */

		c.gridx = 1;
		weightInput = new JTextField("1");
		weightInput.addFocusListener(new FocusListener() {

			String lastValue;

			public void focusGained(FocusEvent arg0) {
				((JTextField) arg0.getComponent()).selectAll();
				lastValue = weightInput.getText();
			}

			public void focusLost(FocusEvent arg0) {
				try {
					Double d = Double.valueOf(weightInput.getText());

					if (d < 0 || d > 1) {
						JOptionPane
								.showMessageDialog(
										null,
										"Please make sure you enter a weight of between 0 and 1",
										"Incorrect range",
										JOptionPane.ERROR_MESSAGE);
						weightInput.setText(Double.valueOf(lastValue) + "");
					}

				} catch (NumberFormatException ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure you are entering a number for this field",
									"Number required",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		bottomPanel.add(weightInput, c);
	}

	private void createOutputPanel() {
		/**
		 * Creates the outputs panel
		 */
		outputsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		c.insets = new Insets(10, 10, 10, 10);

		int x = 0;

		for (Variable v : outputList) {

			c.gridx = x;
			c.gridy = 0;
			outputsPanel.add(new JLabel(v.getName()), c);

			StringBuilder sb = new StringBuilder();

			sb.append("none\n");
			for (int i = 0; i < v.getMFs().size(); i++) {
				sb.append(v.getMfAtIndex(i).getName() + "\n");
			}

			String[] names = (sb.toString()).split("\n");

			JList<String> temp = new JList<String>(names);
			outputJListCollection.add(temp);
			temp.setSelectedIndex(0);

			c.gridy = 1;
			outputsPanel.add(temp, c);

			JCheckBox tempBox = new JCheckBox("Invert");
			checkboxList.add(tempBox);

			c.gridy = 2;
			outputsPanel.add(tempBox, c);

			x++;
		}

	}

	private void createInputsPanel() {
		/**
		 * Creates the inputs panel
		 */
		inputsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		c.insets = new Insets(10, 10, 10, 10);

		int x = 0;

		for (Variable v : inputList) {

			c.gridx = x;
			c.gridy = 0;
			inputsPanel.add(new JLabel(v.getName()), c);

			StringBuilder sb = new StringBuilder();

			sb.append("none\n");
			for (int i = 0; i < v.getMFs().size(); i++) {
				sb.append(v.getMfAtIndex(i).getName() + "\n");
			}

			String[] names = (sb.toString()).split("\n");

			JList<String> temp = new JList<String>(names);
			inputJListCollection.add(temp);
			temp.setSelectedIndex(0);

			c.gridy = 1;
			inputsPanel.add(temp, c);

			JCheckBox tempBox = new JCheckBox("Invert");
			checkboxList.add(tempBox);

			c.gridy = 2;
			inputsPanel.add(tempBox, c);

			x++;
		}

	}

	/*
	 * Data Retreival Methods
	 */

	public Rule getRule() {
		/**
		 * Creates and returns a rule, using the values entered in the
		 * RuleEditor input boxes. Remember that the MembershipFunction
		 * selection is BASE ONE, and not 0.
		 * 
		 * @return Rule object created in the form
		 */

		ArrayList<SubRule> in = new ArrayList<SubRule>();
		ArrayList<SubRule> out = new ArrayList<SubRule>();

		for (int i = 0; i < inputList.size(); i++) {
			SubRule s = new SubRule(inputJListCollection.get(i)
					.getSelectedValue(), inputList.get(i).getName(),
					inputJListCollection.get(i).getSelectedIndex() + 1,
					checkboxList.get(i).isSelected());
			in.add(s);
		}

		for (int i = 0; i < outputList.size(); i++) {
			SubRule s = new SubRule(outputJListCollection.get(i)
					.getSelectedValue(), outputList.get(i).getName(),
					outputJListCollection.get(i).getSelectedIndex() + 1,
					checkboxList.get(i + inputList.size()).isSelected());
			out.add(s);
		}

		int connective = 1;

		if (radioOR.isSelected()) {
			connective = 2;
		}

		Rule r = new Rule(in, out, Double.valueOf(weightInput.getText()),
				connective);

		return r;
	}
}
