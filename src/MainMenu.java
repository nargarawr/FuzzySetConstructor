/**
 * Main Menu used to manipulate the main menu, for creating and
 * loading initial FISs to the system
 * 
 * @author Craig Knott, George Tretyakov
 */

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainMenu {

	/*
	 * Main content panel
	 */
	private JPanel content;

	/*
	 * GUI Elements
	 */
	private JLabel welcomeLabel;
	private JLabel instructionsLabel;

	private JButton newButton;
	private JButton loadButton;
	private JButton closeButton;
	private JFileChooser jfc;

	/*
	 * Constructor
	 */

	public MainMenu() {
		/**
		 * Constructor for the MainMenu class. Creates the content panel and
		 * adds the graphical elements
		 */

		content = new JPanel(new GridBagLayout());
		addGraphicalElements();
	}

	/*
	 * Creation of Graphical elements
	 */

	public void addGraphicalElements() {
		/**
		 * Creates the graphical elements of the system and adds them to the
		 * JPanel "content".
		 */

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.weighty = 10;
		c.anchor = GridBagConstraints.NORTH;

		welcomeLabel = new JLabel(
				"<html>Welcome to the <br>Fuzzy Inference System Constructor<html>");
		welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
		c.gridy = 0;
		c.insets = new Insets(0, 20, 0, 20);
		content.add(welcomeLabel, c);

		instructionsLabel = new JLabel(
				"To begin, select one of the following options");
		c.insets = new Insets(0, 35, 0, 0);
		c.gridy = 1;
		content.add(instructionsLabel, c);

		newButton = new JButton("New FIS");
		c.gridy = 2;
		c.insets = new Insets(5, 150, 5, 150);
		content.add(newButton, c);

		loadButton = new JButton("Load FIS");
		c.gridy = 3;
		content.add(loadButton, c);

		closeButton = new JButton("Close");
		c.gridy = 4;
		c.insets = new Insets(5, 150, 15, 150);
		content.add(closeButton, c);
	}

	/*
	 * Data Retreival Methods
	 */

	public JPanel getContent() {
		/**
		 * Returns the JPanel holding all graphical elements of the main menu
		 * 
		 * @return JPanel holding all the graphical elements
		 */
		return content;
	}

	/*
	 * Action listeners
	 */

	public void assignActionListeners(ActionListener al) {
		/**
		 * Assigns actions listeners to the buttons on the main menu
		 * 
		 * @param al
		 *            An action listener created by the controller
		 */

		newButton.addActionListener(al);
		newButton.setActionCommand("mm_new_al");

		loadButton.addActionListener(al);
		loadButton.setActionCommand("mm_load_al");

		closeButton.addActionListener(al);
		closeButton.setActionCommand("mm_close_al");
	}

	/*
	 * Other methods
	 */

	public void close() {
		/**
		 * Closes the system, after asking if the user is certain they want to
		 */

		int closeSystemWarning = JOptionPane.showConfirmDialog(null,
				"Are you sure you wish to exit?", "Exit Message",
				JOptionPane.WARNING_MESSAGE);
		if (!(closeSystemWarning == JOptionPane.CANCEL_OPTION)) {
			System.exit(0);
		}
	}

	public String loadFile() {
		/**
		 * Opens a JFileChooser for the user to select and .fis file to load
		 * into the system.
		 * 
		 * @return String indicating file path of file to be loaded
		 */

		String filePath = "";

		jfc = new JFileChooser(System.getProperty("user.dir"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Fuzzy Inference System", "fis");
		jfc.setFileFilter(filter);

		int returnValue = JFileChooser.CANCEL_OPTION;
		returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			filePath = jfc.getSelectedFile().getAbsoluteFile().toString();

		}

		return filePath;

	}

}