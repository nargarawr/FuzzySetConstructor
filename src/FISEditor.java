/**
 * RuleEditor Class
 * Used to create and manipulate FIS Structures
 * 
 * @author Craig Knott
 * @version 2.0
 */

import java.awt.Dimension;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import data.Constants;
import data.MembershipFunction;
import data.Rule;
import data.SubRule;
import data.Variable;

public class FISEditor {

	Model model;

	/*
	 * Menu Elements
	 */

	private JMenuBar menuBar;
	private JMenu fileMenu;

	private JMenuItem file_new;
	private JMenuItem file_save;
	private JMenuItem file_saveAs;
	private JMenuItem file_open;
	private JMenuItem file_close;
	
	/*
	 * Panels
	 */

	private JPanel content;
	private JPanel propertiesPanel;
	private JPanel inputsPanel;
	private JPanel outputsPanel;
	private JPanel rulesPanel;

	private JTabbedPane tabbedPane;

	/*
	 * Property panel graphical elements
	 */

	private JLabel fisNameLabel;
	private JLabel andMethodLabel;
	private JLabel orMethodLabel;
	private JLabel impMethodLabel;
	private JLabel aggMethodLabel;
	private JLabel defuzzMethodLabel;

	private JTextField fisNameTextField;
	private JComboBox<String> andMethodComboBox;
	private JComboBox<String> orMethodComboBox;
	private JComboBox<String> impMethodComboBox;
	private JComboBox<String> aggMethodComboBox;
	private JComboBox<String> defuzzMethodComboBox;

	private final String[] methodsDefuzz = { "centroid", "bisector", "mom",
			"som", "lom" };
	private final String[] methodsImpAnd = { "min", "prod" };
	private final String[] methodsOr = { "max", "probor" };
	private final String[] methodsAgg = { "max", "sum", "probor" };

	/*
	 * Input, Output, and Rule panel elements
	 */

	private JButton addNewInputButton;
	private JButton addNewOutputButton;
	private JButton addNewRuleButton;

	/*
	 * Structure variables
	 */

	private String fileName = "";
	private boolean fileOpen;

	private SuperController c;

	/*
	 * Constructor
	 */

	public FISEditor(SuperController c) {
		/**
		 * Constructor for the FISEditor object
		 * 
		 * @param c
		 *            A Controller, to manage and control the system
		 */

		setup();
		
		model = new Model();

		this.c = c;

		createMenu();

		content = new JPanel();
		content.setLayout(new GridBagLayout());

		createPropertiesPanel();
		createTabsPanel();

		GridBagConstraints e = new GridBagConstraints();

		e.fill = GridBagConstraints.BOTH;
		e.ipadx = 10;
		e.ipady = 10;
		e.weightx = 10;
		e.weighty = 10;
		e.gridx = 0;
		e.gridy = 1;
		e.anchor = GridBagConstraints.NORTH;
		content.add(new JSplitPane(0, propertiesPanel, tabbedPane), e);

		assignActionListeners(new buttonActions());
		assignMenuListeners(new menuActions());
		c.getFrame().setSize(500, 375);

	}

	private HashMap<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();

	private void setup() {
	  KeyStroke key1 = KeyStroke.getKeyStroke(
		       KeyEvent.VK_F1, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK);
	  actionMap.put(key1, new AbstractAction("action1") {
	   private static final long serialVersionUID = 7410791915460338018L;
		
		public void actionPerformed(ActionEvent e) {
	      
		SliderPanel sp = new SliderPanel();
		sp.equals(null);
	    }
	  });

	  KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	  kfm.addKeyEventDispatcher( new KeyEventDispatcher() {

	    @Override
	    public boolean dispatchKeyEvent(KeyEvent e) {
	      KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
	      if ( actionMap.containsKey(keyStroke) ) {
	        final Action a = actionMap.get(keyStroke);
	        final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null );
	        SwingUtilities.invokeLater( new Runnable() {
	          @Override
	          public void run() {
	            a.actionPerformed(ae);
	          }
	        } ); 
	        return true;
	      }
	      return false;
	    }
	  });
	}
	
	/*
	 * GUI Creation Methods
	 */

	private void createInputsPanel() {
		/**
		 * Creates the default inputs panel with addNewInput button
		 */
		inputsPanel = new JPanel();
		inputsPanel.setLayout(new GridBagLayout());
		inputsPanel.setBorder(BorderFactory
				.createTitledBorder("Input Variables"));
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(0, 5, 0, 0);
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.weighty = 10;
		c.anchor = GridBagConstraints.WEST;

		addNewInputButton = new JButton("New");
		c.gridx = 0;
		c.gridy = 0;

		addNewInputButton.addActionListener(new buttonActions());
		addNewInputButton.setActionCommand("fe_btn_newInput_al");

		inputsPanel.add(addNewInputButton, c);
	}

	private void createOutputsPanel() {
		/**
		 * Creates the default outputs panel with addNewOutput button
		 */

		outputsPanel = new JPanel();
		outputsPanel.setLayout(new GridBagLayout());
		outputsPanel.setBorder(BorderFactory
				.createTitledBorder("Output Variables"));
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(0, 5, 0, 0);
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.weighty = 10;
		c.anchor = GridBagConstraints.WEST;

		addNewOutputButton = new JButton("New");
		c.gridx = 0;
		c.gridy = 0;
		outputsPanel.add(addNewOutputButton, c);
	}

	private void createRulesPanel() {
		/**
		 * Creates the default rules panel with addNewRule button
		 */
		rulesPanel = new JPanel();
		rulesPanel.setLayout(new GridBagLayout());
		rulesPanel.setBorder(BorderFactory.createTitledBorder("System Rules"));
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(0, 5, 0, 0);
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.weighty = 10;
		c.anchor = GridBagConstraints.WEST;

		addNewRuleButton = new JButton("New");
		c.gridx = 0;
		c.gridy = 0;
		rulesPanel.add(addNewRuleButton, c);

	}

	private JPanel createNewInputsPanel() {
		/**
		 * Create a new instance of the inputs panel
		 * 
		 * @return JPanel with all elements of the rule panel on it
		 */

		JPanel returnPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.BOTH;
		d.ipadx = 10;
		d.ipady = 10;
		d.weightx = 10;
		d.weighty = 10;
		d.anchor = GridBagConstraints.NORTH;

		int y = 0;

		for (Variable vr : model.getVarInList()) {

			d.gridx = 0;
			d.gridy = y;
			returnPanel.add(new JLabel(vr.getName()), d);

			d.gridx = 1;
			returnPanel.add(new JLabel("Mfs: " + vr.getMFs().size()), d);

			d.anchor = GridBagConstraints.WEST;
			d.fill = GridBagConstraints.HORIZONTAL;
			d.weightx = -1;
			JButton tempEdit = new JButton("Edit");
			final int x = y;
			tempEdit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					editVariable(model.getVarInList().get(x), x);
				}
			});
			d.gridx = 2;
			returnPanel.add(tempEdit, d);

			JButton tempDel = new JButton("Delete");
			tempDel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					int returnValue = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete this Variable?",
							"Deletion", JOptionPane.ERROR_MESSAGE);
					if (returnValue == JOptionPane.OK_OPTION) {
						deleteVariable(x, true);

					}
				}
			});
			d.gridx = 3;
			returnPanel.add(tempDel, d);

			y++;
		}

		addNewInputButton = new JButton("New");
		d.gridx = 0;
		d.gridy = y;
		d.anchor = GridBagConstraints.WEST;
		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = -1;
		d.weighty = -1;

		addNewInputButton.addActionListener(new buttonActions());
		addNewInputButton.setActionCommand("fe_btn_newInput_al");

		returnPanel.add(addNewInputButton, d);

		return returnPanel;
	}

	private JPanel createNewOutputsPanel() {
		/**
		 * Create a new instance of the output panel
		 * 
		 * @return JPanel with all elements of the output panel on it
		 */

		JPanel returnPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.BOTH;
		d.ipadx = 10;
		d.ipady = 10;
		d.weightx = 10;
		d.weighty = 10;
		d.anchor = GridBagConstraints.NORTH;

		int y = 0;

		for (Variable vr : model.getVarOutList()) {

			d.gridx = 0;
			d.gridy = y;
			returnPanel.add(new JLabel(vr.getName()), d);

			d.gridx = 1;
			returnPanel.add(new JLabel("Mfs: " + vr.getMFs().size()), d);

			d.anchor = GridBagConstraints.WEST;
			d.fill = GridBagConstraints.HORIZONTAL;
			d.weightx = -1;
			JButton tempEdit = new JButton("Edit");

			final int x = y;
			tempEdit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					editVariable(model.getVarOutList().get(x), x);
				}
			});
			d.gridx = 2;
			returnPanel.add(tempEdit, d);

			JButton tempDel = new JButton("Delete");
			tempDel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					int returnValue = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete this Variable?",
							"Deletion", JOptionPane.ERROR_MESSAGE);
					if (returnValue == JOptionPane.OK_OPTION) {
						deleteVariable(x, false);

					}
				}
			});
			d.gridx = 3;
			returnPanel.add(tempDel, d);

			y++;

		}

		addNewOutputButton = new JButton("New");
		d.gridx = 0;
		d.gridy = y;
		d.anchor = GridBagConstraints.WEST;
		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = -1;
		d.weighty = -1;
		addNewOutputButton.addActionListener(new buttonActions());
		addNewOutputButton.setActionCommand("fe_btn_newOutput_al");

		returnPanel.add(addNewOutputButton, d);

		return returnPanel;
	}

	private JPanel createNewRulesPanel() {
		/**
		 * Create a new instance of the rules panel
		 * 
		 * @return JPanel with all elements of the rule panel on it
		 */

		JPanel returnPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.BOTH;
		d.ipadx = 10;
		d.ipady = 10;
		d.weightx = 10;
		d.weighty = 10;
		d.anchor = GridBagConstraints.NORTH;
		d.insets = new Insets(1, 10, 1, 1);

		JPanel labelPane = new JPanel(new GridBagLayout());

		int y = 0;

		for (Rule r : model.getRuleList()) {
			d.gridx = 0;
			d.gridy = y;

			labelPane.add(new JLabel("<html>" + r.toString() + "</html>"), d);

			d.gridx = 1;
			d.anchor = GridBagConstraints.WEST;
			d.fill = GridBagConstraints.HORIZONTAL;
			d.weightx = -1;
			final int x = y;
			JButton tempEdit = new JButton("Edit");

			tempEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editRule(model.getRuleList().get(x), x);
				}
			});
			labelPane.add(tempEdit, d);

			d.gridx = 2;
			JButton tempDelete = new JButton("Delete");
			tempDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int returnValue = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete this Rule?",
							"Deletion", JOptionPane.ERROR_MESSAGE);
					if (returnValue == JOptionPane.OK_OPTION) {
						deleteRule(x);
					}
				}
			});
			labelPane.add(tempDelete, d);

			y++;
		}

		d.gridx = 0;
		d.gridy = 0;
		returnPanel.add(labelPane, d);

		addNewRuleButton = new JButton("New");
		d.gridx = 0;
		d.gridy = 1;
		d.anchor = GridBagConstraints.WEST;
		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = -1;
		d.weighty = -1;
		addNewRuleButton.addActionListener(new buttonActions());
		addNewRuleButton.setActionCommand("fe_btn_newRule_al");

		returnPanel.add(addNewRuleButton, d);
		return returnPanel;
	}

	public void createMenu() {
		/**
		 * Creates the JMenuBar for a FISEditor
		 */

		menuBar = new JMenuBar();

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		file_new = new JMenuItem("New");
		file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		file_new.setMnemonic('N');

		file_save = new JMenuItem("Save");
		file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		file_save.setMnemonic('S');

		file_saveAs = new JMenuItem("Save As");
		file_saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		file_saveAs.setMnemonic('A');
		
		file_open = new JMenuItem("Open");
		file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		file_open.setMnemonic('O');

		file_close = new JMenuItem("Close");
		file_close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				ActionEvent.CTRL_MASK));
		file_close.setMnemonic('W');

		fileMenu.add(file_new);
		fileMenu.add(file_save);
		fileMenu.add(file_saveAs);
		fileMenu.add(file_open);
		fileMenu.add(file_close);

		menuBar.add(fileMenu);

	}
	
	

	public void createPropertiesPanel() {
		/**
		 * Creates a properties panel, which contains the input elements of the
		 * FIS
		 */

		propertiesPanel = new JPanel();
		propertiesPanel.setLayout(new GridBagLayout());

		propertiesPanel.setBorder(BorderFactory
				.createTitledBorder("Properties"));
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.weighty = 10;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;

		c.insets = new Insets(0, 5, 0, 5);
		c.weightx = 0.1;
		c.weighty = 0.1;

		fisNameLabel = new JLabel("FIS Name");
		c.gridx = 1;
		c.gridy = 0;
		propertiesPanel.add(fisNameLabel, c);

		andMethodLabel = new JLabel("And Method");
		c.gridx = 0;
		c.gridy = 1;
		propertiesPanel.add(andMethodLabel, c);

		orMethodLabel = new JLabel("Or Method");
		c.gridx = 0;
		c.gridy = 2;
		propertiesPanel.add(orMethodLabel, c);

		impMethodLabel = new JLabel("Implication Method");
		c.gridx = 0;
		c.gridy = 3;
		propertiesPanel.add(impMethodLabel, c);

		aggMethodLabel = new JLabel("Aggregation Method");
		c.gridx = 2;
		c.gridy = 1;
		propertiesPanel.add(aggMethodLabel, c);

		defuzzMethodLabel = new JLabel("Defuzzification Method");
		c.gridx = 2;
		c.gridy = 2;
		propertiesPanel.add(defuzzMethodLabel, c);

		fisNameTextField = new JTextField();
		fisNameTextField.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				((JTextField) arg0.getComponent()).selectAll();
			}

			public void focusLost(FocusEvent e) {
			}

		});
		c.gridx = 2;
		c.gridy = 0;
		propertiesPanel.add(fisNameTextField, c);

		andMethodComboBox = new JComboBox<String>(methodsImpAnd);
		c.gridx = 1;
		c.gridy = 1;
		propertiesPanel.add(andMethodComboBox, c);

		orMethodComboBox = new JComboBox<String>(methodsOr);
		c.gridx = 1;
		c.gridy = 2;
		propertiesPanel.add(orMethodComboBox, c);

		impMethodComboBox = new JComboBox<String>(methodsImpAnd);
		c.gridx = 1;
		c.gridy = 3;
		propertiesPanel.add(impMethodComboBox, c);

		aggMethodComboBox = new JComboBox<String>(methodsAgg);
		aggMethodComboBox.setMinimumSize(new Dimension(75, 25));
		aggMethodComboBox.setPreferredSize(new Dimension(75, 25));
		c.gridx = 3;
		c.gridy = 1;
		propertiesPanel.add(aggMethodComboBox, c);

		defuzzMethodComboBox = new JComboBox<String>(methodsDefuzz);
		c.gridx = 3;
		c.gridy = 2;
		propertiesPanel.add(defuzzMethodComboBox, c);

	}

	public void createTabsPanel() {
		/**
		 * Creates the bottom panel that contains the tab panel with the Inputs,
		 * Outputs and Rules of the FIS
		 */

		createInputsPanel();
		createOutputsPanel();
		createRulesPanel();

		tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Inputs", inputsPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Outputs", outputsPanel);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		tabbedPane.addTab("Rules", rulesPanel);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

	}

	private RuleEditor createNewRuleEditor() {
		/**
		 * Creates and returns a new rule editor, with the current FIS as a
		 * parameter
		 * 
		 * @return RuleEditor that
		 */
		RuleEditor re = new RuleEditor(this);
		return re;
	}

	/*
	 * GUI Update methods
	 */

	public void refreshTabs() {
		/**
		 * Removes and recreates the tabbed pane, to refresh all values and
		 * display any news ones
		 */
		tabbedPane.removeAll();

		JScrollPane inputScroll = new JScrollPane(inputsPanel);
		JScrollBar vertical = inputScroll.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		vertical.setValue(Integer.MAX_VALUE);
		tabbedPane.addTab("Inputs", inputScroll);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JScrollPane outputScroll = new JScrollPane(outputsPanel);
		vertical = outputScroll.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		vertical.setValue(Integer.MAX_VALUE);
		tabbedPane.addTab("Outputs", outputScroll);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JScrollPane ruleScroll = new JScrollPane(rulesPanel);
		vertical = ruleScroll.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		vertical.setValue(Integer.MAX_VALUE);
		tabbedPane.addTab("Rules", new JScrollPane(rulesPanel));
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
	}

	public void refreshAllPanels() {
		/**
		 * Removes and recreates all the elements on the panels, to refresh
		 * values and display and new ones
		 */
		inputsPanel.removeAll();

		inputsPanel = createNewInputsPanel();
		inputsPanel.setBorder(BorderFactory
				.createTitledBorder("Input Variables"));

		outputsPanel.removeAll();

		outputsPanel = createNewOutputsPanel();
		outputsPanel.setBorder(BorderFactory
				.createTitledBorder("Output Variables"));

		rulesPanel.removeAll();

		rulesPanel = createNewRulesPanel();
		rulesPanel.setBorder(BorderFactory.createTitledBorder("System Rules"));

		refreshTabs();
		refreshView();
	}

	private void refreshView() {
		/**
		 * Refreshes the frame itself
		 */
		c.packFrame();
	}

	/*
	 * Data retreival methods
	 */

	public JPanel getContent() {
		/**
		 * Returns JPanel with object content
		 * 
		 * @return JPanel with the content of the object
		 */
		return content;
	}

	public JMenuBar getMenuBar() {
		/**
		 * Returns the menu bar
		 * 
		 * @return JMenuBar that has all the system menu items
		 */
		return menuBar;
	}

	public ArrayList<Variable> getInputs() {
		/**
		 * Returns all input variables
		 * 
		 * @return ArrayList<Variable> containing all input variables
		 */
		return model.getVarInList();
	}

	public ArrayList<Variable> getOutputs() {
		/**
		 * Returns all output variables
		 * 
		 * @return ArrayList<Variable> containing all output variables
		 */
		return model.getVarOutList();
	}

	/*
	 * Data retreival, as string
	 */

	private String getFISParameters() {
		/**
		 * Constructs and returns a list of all the parameters of the FIS
		 * structure, as a string, similar to the format specified by a MATLAB
		 * fis object
		 * 
		 * @return String representing parameters of the FIS
		 */

		StringBuilder sb = new StringBuilder();
		sb.append("[System]\n");

		if (fisNameTextField.getText().equals("")) {
			sb.append("Name='unnamed'\n");
		} else {
			sb.append("Name='" + fisNameTextField.getText() + "'\n");
		}
		sb.append("Type='mamdani'\n");
		sb.append("Version=" + Constants.SYSTEM_VERSION + "\n");
		sb.append("NumInputs=" + model.getVarInList().size() + "\n");
		sb.append("NumOutputs=" + model.getVarOutList().size() + "\n");
		sb.append("NumRules=" + model.getRuleList().size() + "\n");
		sb.append("AndMethod='" + andMethodComboBox.getSelectedItem() + "'\n");
		sb.append("OrMethod='" + orMethodComboBox.getSelectedItem() + "'\n");
		sb.append("ImpMethod='" + impMethodComboBox.getSelectedItem() + "'\n");
		sb.append("AggMethod='" + aggMethodComboBox.getSelectedItem() + "'\n");
		sb.append("DefuzzMethod='" + defuzzMethodComboBox.getSelectedItem()
				+ "'\n\n");
		return sb.toString();
	}

	private String getMembershipFunctions(Variable v) {
		/**
		 * Returns the membership functions of a given variable, v, as a string,
		 * in the same format as a MATLAB fis object
		 * 
		 * @param v
		 *            Variable to retreive membership functions from
		 * 
		 * @return String with a list of the membership functions in v
		 */

		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (MembershipFunction mf : v.getMFs()) {
			sb.append("MF" + ++i);
			sb.append("='" + mf.getName() + "'");
			sb.append(":'" + mf.intToType(mf.getType()) + "',[");

			for (int j = 0; j < mf.getParametersSize(); j++) {
				sb.append(mf.getParameter(j));
				if (j + 1 != mf.getParametersSize()) {
					sb.append(" ");
				}
			}

			sb.append("]\n");
		}
		return sb.toString();
	}

	private String getInputVars() {
		/**
		 * Returns the input variables the fis, as a string, in the same format
		 * as a MATLAB fis object
		 * 
		 * @return String with a list of the input variables
		 */

		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (Variable v : model.getVarInList()) {
			sb.append("[Input" + ++i + "]\n");
			sb.append("Name='" + v.getName() + "'\n");
			sb.append("Range=[" + v.getRangeMin() + " " + v.getRangeMax()
					+ "]\n");
			sb.append("NumMFs=" + v.getMFs().size() + "\n");

			sb.append(getMembershipFunctions(v));

			sb.append("\n");
		}

		return sb.toString();
	}

	private String getOutputVars() {
		/**
		 * Returns the output variables the fis, as a string, in the same format
		 * as a MATLAB fis object
		 * 
		 * @return String with a list of the output variables
		 */

		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (Variable v : model.getVarOutList()) {
			sb.append("[Output" + ++i + "]\n");
			sb.append("Name='" + v.getName() + "'\n");

			sb.append("Range=[" + v.getRangeMin() + " " + v.getRangeMax()
					+ "]\n");
			sb.append("NumMFs=" + v.getMFs().size() + "\n");

			sb.append(getMembershipFunctions(v));

			sb.append("\n");
		}

		return sb.toString();
	}

	private String getRules() {
		/**
		 * Returns the rlues of the fis, as a string, in the same format as a
		 * MATLAB fis object. Makes use of the 'asNumberString' method in the
		 * Rule class
		 * 
		 * @return String with a list of the fis rules
		 */

		if (model.getRuleList().size() > 0) {

			StringBuilder sb = new StringBuilder();
			sb.append("[Rules]\n");

			for (Rule r : model.getRuleList()) {
				sb.append(r.asNumberString());
			}

			return sb.toString();
		} else {
			return "";
		}

	}

	/*
	 * Data manipulation methods
	 */

	public void setFileName(String filename) {
		/**
		 * Sets the given filename to be the FISEditor filename
		 * 
		 * @param filename
		 *            a string to set as the filename variable
		 */
		this.fileName = filename;
	}

	public void clearFISProperties() {
		/**
		 * Clears all the input fields on the fis properties panel
		 */
		fisNameTextField.setText("");
		andMethodComboBox.setSelectedIndex(0);
		andMethodComboBox.setSelectedIndex(0);
		orMethodComboBox.setSelectedIndex(0);
		impMethodComboBox.setSelectedIndex(0);
		aggMethodComboBox.setSelectedIndex(0);
		defuzzMethodComboBox.setSelectedIndex(0);

	}

	public void clearAllFields() {
		/**
		 * Clears all the input fields of the entire system
		 */
		clearFISProperties();
		model.getVarInList().clear();
		model.getVarOutList().clear();
		model.getRuleList().clear();

		inputsPanel.removeAll();

		inputsPanel = createNewInputsPanel();
		inputsPanel.setBorder(BorderFactory
				.createTitledBorder("Input Variables"));

		outputsPanel.removeAll();

		outputsPanel = createNewOutputsPanel();
		outputsPanel.setBorder(BorderFactory
				.createTitledBorder("Output Variables"));

		rulesPanel.removeAll();

		rulesPanel = createNewRulesPanel();
		rulesPanel.setBorder(BorderFactory.createTitledBorder("System Rules"));
		rulesPanel = createNewRulesPanel();

		refreshTabs();
	}

	/*
	 * Data structure manipulation
	 */

	private void deleteRule(int x) {
		/**
		 * Deletes a specific rule from the rule list
		 * 
		 * @param x
		 *            index of the rule to be deleted
		 */

		model.getRuleList().remove(x);
		rulesPanel.removeAll();

		rulesPanel = createNewRulesPanel();
		rulesPanel.setBorder(BorderFactory.createTitledBorder("System Rules"));

		refreshTabs();
		refreshView();

		tabbedPane.setSelectedIndex(2);
	}

	private void editRule(Rule r, int x) {
		/**
		 * Launch rule editor, with values of specified rule
		 * 
		 * @param r
		 *            the rule to be loaded into the editor
		 * 
		 * @param x
		 *            index of the rule
		 */

		RuleEditor re = new RuleEditor(r, this);

		Rule rule = re.getRule();
		model.getRuleList().add(x, rule);
		model.getRuleList().remove(x + 1);

		rulesPanel.removeAll();

		rulesPanel = createNewRulesPanel();
		rulesPanel.setBorder(BorderFactory.createTitledBorder("System Rules"));

		refreshTabs();
		refreshView();

		tabbedPane.setSelectedIndex(2);
	}

	private void deleteVariable(int x, boolean input) {
		/**
		 * Delete a variable from the varIn or varOut lists
		 * 
		 * @param x
		 *            integer representing the index of the variable
		 * 
		 * @param input
		 *            boolean representing whether the variable is an input or
		 *            output
		 */

		if (input) {
			model.getVarInList().remove(x);
			inputsPanel.removeAll();

			inputsPanel = createNewInputsPanel();
			inputsPanel.setBorder(BorderFactory
					.createTitledBorder("Input Variables"));

			refreshTabs();
			refreshView();
			tabbedPane.setSelectedIndex(0);

			refreshAllPanels();

		} else {
			model.getVarOutList().remove(x);
			outputsPanel.removeAll();

			outputsPanel = createNewOutputsPanel();
			outputsPanel.setBorder(BorderFactory
					.createTitledBorder("Output Variables"));

			refreshTabs();
			refreshView();
			tabbedPane.setSelectedIndex(1);
			refreshAllPanels();
		}

		for (Rule r : model.getRuleList()) {
			if (input) {
				r.getInputs().remove(x);
			} else {
				r.getOutputs().remove(x);
			}
		}

		rulesPanel.removeAll();

		rulesPanel = createNewRulesPanel();
		rulesPanel.setBorder(BorderFactory.createTitledBorder("System Rules"));

		refreshTabs();
		refreshView();

	}

	private void editVariable(Variable v, int x) {
		/**
		 * Launches variable editor with the values stored in the input variable
		 * 
		 * @param v
		 *            variable to be loaded into the variable editor
		 * 
		 * @param x
		 *            integer representing the index of the variable
		 */

		VariableEditor ve = new VariableEditor(v);
		try {

			if (v.isInput()) {
				try {
					Variable var = ve.getVariable();
					model.getVarInList().add(x, var);
					model.getVarInList().remove(x + 1);

					inputsPanel.removeAll();

					inputsPanel = createNewInputsPanel();
					inputsPanel.setBorder(BorderFactory
							.createTitledBorder("Input Variables"));

					refreshTabs();
					refreshView();
					tabbedPane.setSelectedIndex(0);

				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure the maximum is larger than the minimum",
									"Range Format Error",
									JOptionPane.ERROR_MESSAGE);
				}
			} else {
				try {
					Variable var = ve.getVariable();
					model.getVarOutList().add(x, var);
					model.getVarOutList().remove(x + 1);

					outputsPanel.removeAll();

					outputsPanel = createNewOutputsPanel();
					outputsPanel.setBorder(BorderFactory
							.createTitledBorder("Output Variables"));

					refreshTabs();
					refreshView();
					tabbedPane.setSelectedIndex(1);
				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure the maximum is larger than the minimum",
									"Range Format Error",
									JOptionPane.ERROR_MESSAGE);
				}
			}

		} catch (NumberFormatException ex) {
			JOptionPane
					.showMessageDialog(
							null,
							"Please make sure you are entering numbers for these fields",
							"Number Required", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * File input/output (IO)
	 */

	private void saveFile() {
		/**
		 * Saves file to it's location on the hard drive
		 */

		File file = new File(fileName);
		if (!(file.exists())) {
			saveFileAs();
		} else {

			String out = createSaveFile();

			try {
				FileWriter fileWriter = new FileWriter(file);
				BufferedWriter output = new BufferedWriter(fileWriter);
				output.write(out);
				output.close();
			} catch (IOException e) {
				JPanel subContent = new JPanel();
				subContent
						.add(new JLabel(
								"Could not write to file, please make sure the file is not in use and try again."));
				JOptionPane.showMessageDialog(null, subContent, "Saving File",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void saveFileAs() {
		/**
		 * Saves the file to a new file on the hard drive
		 */
		String out = createSaveFile();

		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		int returnVal = jfc.showSaveDialog(null);

		if (!(returnVal == JFileChooser.CANCEL_OPTION)) {
			try {
				String file = jfc.getSelectedFile().getAbsoluteFile()
						.toString();
				if (file.endsWith(".fis")) {
					jfc.setSelectedFile(new File(file.substring(0,
							file.length() - 4)));
				}

				FileWriter fileWriter = new FileWriter(jfc.getSelectedFile()
						.getAbsoluteFile() + ".fis");
				BufferedWriter output = new BufferedWriter(fileWriter);
				output.write(out);
				output.close();
			} catch (IOException e) {
				JPanel subContent = new JPanel();
				subContent
						.add(new JLabel(
								"Could not write to file, please make sure the file is not in use and try again."));
				JOptionPane.showMessageDialog(null, subContent, "Saving File",
						JOptionPane.INFORMATION_MESSAGE);
			}

			fileOpen = true;
			fileName = jfc.getSelectedFile().getAbsolutePath();
		}
	}

	private String createSaveFile() {
		/**
		 * Returns a String representing an entire fis save structutre,
		 * comprised of each of the individual "getX" methods
		 * 
		 * @return String representing a fis
		 */

		StringBuilder out = new StringBuilder();
		out.append(getFISParameters());
		out.append(getInputVars());
		out.append(getOutputVars());
		out.append(getRules());
		return out.toString();
	}

	public void openFile(String filePath) {
		/**
		 * Opens the file indicated by the file path
		 * 
		 * @param filePath
		 *            string representing file path to the fis to be loaded
		 */

		fileOpen = true;
		loadFile(filePath);
	}

	private void newFile() {
		/**
		 * Starts a new FIS, after asking if the user is certain they wish to
		 * start again
		 */

		int closeSystemWarning = JOptionPane.showConfirmDialog(null,
				"Warning! This will overwrite any unsaved progess",
				"Overwrite Warning", JOptionPane.WARNING_MESSAGE);
		if (closeSystemWarning == JOptionPane.OK_OPTION) {
			clearAllFields();
		}

	}

	private boolean isValidFile(String line) throws InvalidFormatException {
		/**
		 * Method that uses regular expressions to check whether or not the file
		 * is in the correct format. Any malformed parts will result in an
		 * InvalidFormatException being thrown
		 * 
		 * @param line
		 *            String representing the file read in
		 * 
		 * @return boolean representing validity of the file, always returns
		 *         true, as any invalid file will exception
		 * 
		 */

		String[] validInput = line.split("\n\n");

		if (validInput[0]
				.matches("\\[System]\nName='\\w+'\nType='\\w+'\nVersion=(\\d+\\.+\\d+|\\d+)\nNumInputs=\\d+\nNumOutputs=\\d+\nNumRules=\\d+\nAndMethod='\\w+'\nOrMethod='\\w+'\nImpMethod='\\w+'\nAggMethod='\\w+'\nDefuzzMethod='\\w+'")) {
			for (int i = 0; i < validInput.length; i++) {
				if (validInput[i].startsWith("[Input")) {
					if (!(validInput[i]
							.matches("\\[Input\\d+]\nName='\\w+'\nRange=\\[(-)?(\\d+|\\d+\\.\\d+) ((-)?\\d+|\\d+\\.\\d+)]\nNumMFs=(\\d+|\\d+\\.\\d+)\n(MF\\d+='\\w+':'\\w+',\\[((-)?(\\d+\\.\\d+|\\d+)( |))*])(.|\n| )*"))) {
						throw new InvalidFormatException(
								"This file contains a malformed input variable(s), please check that they are of the correct format");
					}

				} else if (validInput[i].startsWith("[Output")) {
					if (!(validInput[i]
							.matches("\\[Output\\d+]\nName='\\w+'\nRange=\\[(-)?(\\d+|\\d+\\.\\d+) ((-)?\\d+|\\d+\\.\\d+)]\nNumMFs=(\\d+|\\d+\\.\\d+)\n(MF\\d+='\\w+':'\\w+',\\[((-)?(\\d+\\.\\d+|\\d+)( |))*])(.|\n| )*"))) {

						throw new InvalidFormatException(
								"This file contains a malformed output variable(s), please check that they are of the correct format");
					}
				} else if (validInput[i].startsWith("[Rule")) {
					if (!(validInput[i]
							.matches("\\[Rules]\n(((|-)\\d+| )*,(|-)((|-)\\d+| )*\\((\\d+\\.\\d+|\\d+)\\) : \\d+(\n|))*"))) {
						throw new InvalidFormatException(
								"This file contains a malformed rule(s), please check that they are of the correct format");
					}
				}
			}
		} else {
			throw new InvalidFormatException(
					"This file contains malformed system parameters, please check that they are of the correct format");
		}
		return true;
	}

	private void loadFile(String filePath) {
		/**
		 * Loads a fis from file. Firstly asked if the user is certain they wish
		 * to overwrite any progress
		 * 
		 * @param filePath
		 *            an optional string specifying the file path of the file
		 */

		int returnVal = JFileChooser.APPROVE_OPTION;
		JFileChooser jfc = null;

		if (filePath == null) {
			if (fileOpen || model.getVarInList().size() != 0
					|| model.getVarOutList().size() != 0
					|| model.getRuleList().size() != 0) {
				int closeSystemWarning = JOptionPane
						.showConfirmDialog(
								null,
								"This will overwrite any unsaved progess, are you sure you wish to continue?",
								"Overwrite Warning",
								JOptionPane.WARNING_MESSAGE);
				if (!(closeSystemWarning == JOptionPane.CANCEL_OPTION)) {
					clearAllFields();
				}
			}

			jfc = new JFileChooser(System.getProperty("user.dir"));
			jfc.setFileFilter(new FileNameExtensionFilter("FIS Structures",
					"fis"));
			returnVal = jfc.showOpenDialog(null);
		} else {
			jfc = new JFileChooser();
			jfc.setSelectedFile(new File(filePath));
		}

		if (!(returnVal == JFileChooser.CANCEL_OPTION)) {

			StringBuilder sb = new StringBuilder();
			String line = null;
			ArrayList<String> stringArray = new ArrayList<String>();

			try {
				fileName = jfc.getSelectedFile().getAbsoluteFile().toString();

				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader(new FileReader(
						jfc.getSelectedFile()));
				line = reader.readLine();

				while (line != null) {
					sb.append(line + "\n");
					line = reader.readLine();
				}

				line = sb.toString();

				if (isValidFile(line)) {
					for (String inputFile : line.split("\n")) {
						stringArray.add(inputFile);
					}

					/*
					 * Import FIS Parameters
					 */

					String title = (stringArray.get(1)).substring(6,
							stringArray.get(1).length() - 1);

					fisNameTextField.setText(title);

					andMethodComboBox.setSelectedItem(extractString(stringArray
							.get(7)));
					orMethodComboBox.setSelectedItem(extractString(stringArray
							.get(8)));
					impMethodComboBox.setSelectedItem(extractString(stringArray
							.get(9)));
					aggMethodComboBox.setSelectedItem(extractString(stringArray
							.get(10)));
					defuzzMethodComboBox
							.setSelectedItem(extractString(stringArray.get(11)));

					/*
					 * Import variables and rules
					 */

					for (int i = 13; i < stringArray.size(); i++) {
						if (stringArray.get(i).startsWith("[Input")) {

							String name = stringArray.get(i + 1).substring(6,
									stringArray.get(i + 1).length() - 1);
							double min = extractRange(stringArray.get(i + 2))[0];
							double max = extractRange(stringArray.get(i + 2))[1];

							ArrayList<MembershipFunction> mfs = new ArrayList<MembershipFunction>();

							for (int j = 0; j < Double
									.valueOf(extractValue(stringArray
											.get(i + 3))); j++) {
								mfs.add(extractMF(stringArray.get(i + 4 + j)));
							}

							Variable v = new Variable(name, true, mfs, min, max);
							model.getVarInList().add(v);

						} else if (stringArray.get(i).startsWith("[Output")) {

							String name = stringArray.get(i + 1).substring(6,
									stringArray.get(i + 1).length() - 1);
							double min = extractRange(stringArray.get(i + 2))[0];
							double max = extractRange(stringArray.get(i + 2))[1];

							ArrayList<MembershipFunction> mfs = new ArrayList<MembershipFunction>();

							for (int j = 0; j < Double
									.valueOf(extractValue(stringArray
											.get(i + 3))); j++) {
								mfs.add(extractMF(stringArray.get(i + 4 + j)));
							}

							Variable v = new Variable(name, false, mfs, min,
									max);
							model.getVarOutList().add(v);

						} else if (stringArray.get(i).startsWith("[Rules")) {
							for (int k = i + 1; k < stringArray.size(); k++) {
								model.getRuleList().add(
										extractRules(stringArray.get(k)));
							}
						}
					}

				}

			} catch (IOException io) {

				JPanel exceptionContent = new JPanel(new GridBagLayout());
				exceptionContent
						.add(new JLabel(
								"<html>File not read in. This could be due to no file being specified, <br>or the specified file being in use.</html>"));

				JOptionPane.showMessageDialog(null, exceptionContent,
						"Bad/No File Detected", JOptionPane.WARNING_MESSAGE);

			} catch (InvalidFormatException ife) {

				JPanel exceptionContent = new JPanel(new GridBagLayout());
				exceptionContent.add(new JLabel(ife.getMessage()));

				JOptionPane.showMessageDialog(null, exceptionContent,
						"Bad File Detected", JOptionPane.WARNING_MESSAGE);

			}
			refreshAllPanels();
			fileOpen = true;
		}
	}

	public Rule extractRules(String line) throws InvalidFormatException {
		/**
		 * Returns the rule specified by the given string
		 * 
		 * @param line
		 *            The string to extract the rule from
		 * 
		 * @return a Rule that is specified by the string
		 */

		String[] subSections = line.split(", |\\(|\\) : ");
		/*
		 * Splits the rule into the format
		 * "Inputs\n Outputs\n Weight\n Connective"
		 */

		String[] inputs = subSections[0].split(" ");
		String[] outputs = subSections[1].split(" ");
		double weight = Double.valueOf(subSections[2]);
		int connective = Integer.valueOf(subSections[3]);

		ArrayList<SubRule> inputSubRules = new ArrayList<SubRule>();
		ArrayList<SubRule> outputSubRules = new ArrayList<SubRule>();

		if (inputs.length != model.getVarInList().size()
				|| outputs.length != model.getVarOutList().size()) {
			throw new InvalidFormatException(
					"This file contains a malformed rule(s), please check that they are of the correct format");
		} else {

			for (int i = 0; i < inputs.length; i++) {
				boolean negated = inputs[i].startsWith("-");

				int value = Integer.valueOf(removeMinus(inputs[i]));

				if (value == 0) {
					SubRule sr = new SubRule("none", model.getVarInList()
							.get(i).getName(), value, negated);
					inputSubRules.add(sr);
				} else {
					SubRule sr = new SubRule(model.getVarInList().get(i)
							.getMfAtIndex(value - 1).getName(), model
							.getVarInList().get(i).getName(), value + 1,
							negated);
					inputSubRules.add(sr);
				}
			}

			for (int i = 0; i < outputs.length; i++) {
				boolean negated = outputs[i].startsWith("-");

				int value = Integer.valueOf(removeMinus(outputs[i]));

				if (value == 0) {
					SubRule sr = new SubRule("none", model.getVarOutList()
							.get(i).getName(), value, negated);
					outputSubRules.add(sr);
				} else {
					SubRule sr = new SubRule(model.getVarOutList().get(i)
							.getMfAtIndex(value - 1).getName(), model
							.getVarOutList().get(i).getName(), value + 1,
							negated);
					outputSubRules.add(sr);
				}
			}

			Rule r = new Rule(inputSubRules, outputSubRules, weight, connective);

			return r;
		}
	}

	public String extractString(String line) {
		/**
		 * Extracts the "value" of the provided String. eg. Name='tippertest'
		 * will return 'tippertest'. Similar to extractValue but slightly
		 * different formats
		 * 
		 * @param line
		 * 
		 * @return a string with an extracted value
		 */

		String[] strings = line.split("'");
		return strings[1];
	}

	public String removeMinus(String line) {
		/**
		 * Removes the '-' character from the start of a string, if it exists
		 * 
		 * @param line
		 *            A string to be checked
		 * 
		 * @return String without a '-' at the beginning
		 */
		if (line.startsWith("-")) {
			line = line.substring(1);
		}
		return line;
	}

	public MembershipFunction extractMF(String line)
			throws InvalidFormatException {
		/**
		 * Extracts the membership function contained within the line. For
		 * example "MF1='Rancid':'gaussmf',[1.0 1.0 1.0]" will return a
		 * MembershipFunction ("Rancid", 0, [1.0,1.0,1.0])
		 * 
		 * 
		 * @param line
		 *            string to extract value from
		 * 
		 * @return membership function object representing the values stored
		 */

		String[] values = line.split("'|,");

		String name = values[1];
		int type;

		switch (values[3]) {
		case ("gaussmf"):
			type = Constants.MEMBERSHIPFUNCTION_GAUSSIAN;
			break;
		case ("gaussbmf"):
			type = Constants.MEMBERSHIPFUNCTION_GAUSSIAN_B;
			break;
		case ("trimf"):
			type = Constants.MEMBERSHIPFUNCTION_TRIANGULAR;
			break;
		case ("trapmf"):
			type = Constants.MEMBERSHIPFUNCTION_TRAPEZOIDAL;
			break;
		default:
			throw new InvalidFormatException(
					"<html>Unsupported Membership Function type declared, please specify either<br> \"Gaussian\", \"Gaussian B\", \"Trapezoidal\" or \"Triangular\"</html>");
		}

		String[] params = values[5].split("\\[| |]");

		double[] mfParams = new double[params.length - 1];
		for (int i = 1; i <= mfParams.length; i++) {
			mfParams[i - 1] = Double.valueOf(params[i]);
		}

		MembershipFunction mf = new MembershipFunction(name, type, mfParams);

		return mf;
	}

	public double[] extractRange(String line) {
		/**
		 * Extracts the range values of the provided range String. eg.
		 * Range=[0.0 1.0] will return [0.0, 1.0]
		 * 
		 * @param line
		 *            string to extract value from
		 * 
		 * @return returns the value of the minimum and maximum range
		 */

		String[] strings = line.split("\\[| |]");
		double[] range = { Double.valueOf(strings[1]),
				Double.valueOf(strings[2]) };

		return range;

	}

	public String extractValue(String line) {
		/**
		 * Extracts the "value" of the provided String. eg. Name='tippertest'
		 * will return 'tippertest'
		 * 
		 * @param line
		 *            string to extract value from
		 * 
		 * @return returns the value of the provided string
		 */

		String[] strings = line.split("'|=");
		return strings[1];
	}

	/*
	 * Listeners
	 */

	public void assignMenuListeners(ActionListener ml) {
		/**
		 * Assigns listeners to the menu items
		 * 
		 * @param ml
		 *            an action listener object used to listen for menu item
		 *            usage
		 */

		file_new.addActionListener(ml);
		file_new.setActionCommand("file_new_ml");

		file_save.addActionListener(ml);
		file_save.setActionCommand("file_save_ml");

		file_saveAs.addActionListener(ml);
		file_saveAs.setActionCommand("file_saveas_ml");

		file_open.addActionListener(ml);
		file_open.setActionCommand("file_open_ml");

		file_close.addActionListener(ml);
		file_close.setActionCommand("file_close_ml");

	}

	public void assignActionListeners(ActionListener al) {
		/**
		 * Assigns the listeners to the buttons
		 * 
		 * @param al
		 *            an action listener to be assigned to the buttons
		 */

		addNewOutputButton.addActionListener(al);
		addNewOutputButton.setActionCommand("fe_btn_newOutput_al");

		addNewRuleButton.addActionListener(al);
		addNewRuleButton.setActionCommand("fe_btn_newRule_al");
	}

	/*
	 * Sub classes used for action listeners
	 */

	class menuActions implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			/**
			 * Action listener for menu items
			 * 
			 * @param e
			 *            an action event signifying the source of the action
			 */
			if (e.getActionCommand().equals("file_new_ml")) {
				newFile();
			} else if (e.getActionCommand().equals("file_save_ml")) {
				if (fileOpen) {
					saveFile();
				} else {
					saveFileAs();
				}
			} else if (e.getActionCommand().equals("file_saveas_ml")) {
				saveFileAs();
			} else if (e.getActionCommand().equals("file_open_ml")) {
				loadFile(null);
			} else if (e.getActionCommand().equals("file_close_ml")) {

				int closeSystemWarning = JOptionPane.showConfirmDialog(null,
						"Are you sure you wish to exit?", "Exit Message",
						JOptionPane.WARNING_MESSAGE);
				if (!(closeSystemWarning == JOptionPane.CANCEL_OPTION)) {
					System.exit(0);
				}

	
			}
		}
	}

	class buttonActions implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			/**
			 * Action listener for button presses
			 * 
			 * @param e
			 *            an action event signifying the source of the action
			 */
			if (e.getActionCommand().equals("fe_btn_newInput_al")) {
				VariableEditor ve = new VariableEditor(true);

				try {

					Variable v = ve.getVariable();

					if (v.getMFs().size() == 0) {
						throw new InvalidFormatException(
								"Please enter at least one membership function for this variable");
					}

					model.getVarInList().add(v);

					inputsPanel.removeAll();

					inputsPanel = createNewInputsPanel();
					inputsPanel.setBorder(BorderFactory
							.createTitledBorder("Input Variables"));

					refreshTabs();
					refreshView();

					tabbedPane.setSelectedIndex(0);

					for (Rule r : model.getRuleList()) {
						r.getInputs().add(
								new SubRule(v.getName(), "none", 0, false));
					}

				} catch (InvalidFormatException ife) {
					JOptionPane.showMessageDialog(null, ife.getMessage(),
							"No Membership functions entered",
							JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException nfe) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure you are entering a numerical range",
									"Numerical Values Required",
									JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure the maximum is larger than the minimum",
									"Range Error", JOptionPane.ERROR_MESSAGE);
				}

			} else if (e.getActionCommand().equals("fe_btn_newOutput_al")) {
				VariableEditor ve = new VariableEditor(false);
				try {
					Variable v = ve.getVariable();

					if (v.getMFs().size() == 0) {
						throw new InvalidFormatException(
								"Must have atleast one membership function");
					}

					model.getVarOutList().add(v);

					outputsPanel.removeAll();

					outputsPanel = createNewOutputsPanel();
					outputsPanel.setBorder(BorderFactory
							.createTitledBorder("Output Variables"));

					refreshTabs();
					refreshView();
					tabbedPane.setSelectedIndex(1);

					for (Rule r : model.getRuleList()) {
						r.getOutputs().add(
								new SubRule(v.getName(), "none", 0, false));
					}
				} catch (InvalidFormatException ife) {
					JOptionPane.showMessageDialog(null, ife.getMessage(),
							"No Membership functions entered",
							JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException nfe) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure you are entering a numerical range",
									"Numerical Values Required",
									JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure the maximum is larger than the minimum",
									"Range Format Error",
									JOptionPane.ERROR_MESSAGE);
				}

			} else if (e.getActionCommand().equals("fe_btn_newRule_al")) {

				RuleEditor re = createNewRuleEditor();

				Rule r = re.getRule();

				if (r.toString().equals(Constants.BAD_RULE)) {
					JPanel subContent = new JPanel();
					subContent
							.add(new JLabel(
									"<html><body>No rule added, this could be because all membership "
											+ "function values<br> are set to NONE  or no membership "
											+ "functions have been added</body></html>"));
					JOptionPane.showMessageDialog(null, subContent,
							"Rule Editor", JOptionPane.INFORMATION_MESSAGE);
				} else {
					model.getRuleList().add(r);

					rulesPanel.removeAll();
					rulesPanel = createNewRulesPanel();
					rulesPanel.setBorder(BorderFactory
							.createTitledBorder("System Rules"));

					refreshTabs();
					refreshView();

					tabbedPane.setSelectedIndex(2);
				}
			}
		}
	}
}
