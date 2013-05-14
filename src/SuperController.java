/**
 * Controller Class
 * Used to manipulate the system
 * 
 * @author Craig Knott, Luke Hovell, Nathan Karimian 
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class SuperController {

	private JFrame frame;
	private JPanel panel;

	private MainMenu mm;

	/*
	 * Constructor
	 */

	public SuperController() {
		/**
		 * Constructor for the system. Creates the main frame, and main content
		 * panel. Also adds relevant action listeners
		 */
		frame = new JFrame("Fuzzy Logic Toolkit");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/**
		 * Create main menu and assign action listeners
		 */
		mm = new MainMenu();
		mm.assignActionListeners(new ActionListeners());

		/**
		 * Create main content panel
		 */
		panel = new JPanel();
		panel.add(mm.getContent());

		/**
		 * Add content to the frame and compact
		 */
		frame.add(panel);
		frame.pack();
	}

	/*
	 * Data Retreival Methods
	 */

	public JFrame getFrame() {
		/**
		 * Returns the frame. Used for accessing frame properties from other
		 * classes
		 * 
		 * @return JFrame of the window
		 */
		return this.frame;
	}

	/*
	 * Update Graphical Elements
	 */

	public void setMenuBar(JMenuBar menu) {
		/**
		 * Sets "menu" to be the JMenuBar of the frame
		 * 
		 * @param menu
		 *            A JMenuBar to be assigned
		 */

		frame.setJMenuBar(menu);
	}

	public void packFrame() {
		/**
		 * Packs the frame (as a method, to be called from other classes). If
		 * the frame height is over 500, it is not packed, so that the GUI does
		 * not start looking stretched
		 */

		frame.pack();

		updateComponentTree();
	}

	public void updateComponentTree() {
		/**
		 * Used to "refresh" the GUI Display elements
		 */
		SwingUtilities.updateComponentTreeUI(frame);
	}

	/*
	 * Other methods
	 */

	public void openFISEditor(boolean fileSelected, String filePath) {
		/**
		 * Opens a new instance of the Fuzzy Inference System Editor
		 * 
		 * @param fileSelected
		 *            a boolean representing whether or not a file is being
		 *            opened
		 * 
		 * @param filePath
		 *            a string representing the file path of the file to be
		 *            opened, if there is one
		 */

		/*
		 * Deletes current frame elements
		 */
		frame.setVisible(false);
		frame.setTitle("FIS Editor");
		frame.remove(panel);

		/*
		 * Creates a FIS editor
		 */

		FISEditor f = new FISEditor(this);

		if (fileSelected) {
			f.openFile(filePath);
		}

		/*
		 * Sets the content panel to be the content of the FISEditor and
		 * displays this
		 */
		panel = f.getContent();
		frame.add(panel);
		setMenuBar(f.getMenuBar());
		frame.setVisible(true);
		f.refreshAllPanels();
	}

	/*
	 * Action Listener Sub Class
	 */

	class ActionListeners implements ActionListener {
		/**
		 * Sub class used to assign action listeners to system elements
		 */

		@Override
		public void actionPerformed(ActionEvent e) {
			/**
			 * Necessary method from the implementation of ActionListener
			 * 
			 * @param e
			 *            ActionEvent caused by system elements
			 */

			if (e.getActionCommand().equals("mm_new_al")) {
				/**
				 * Main Menu - "New" button
				 */
				openFISEditor(false, null);

			} else if (e.getActionCommand().equals("mm_load_al")) {
				/**
				 * Main Menu - "Load" Button
				 */
				openFISEditor(true, mm.loadFile());

			} else if (e.getActionCommand().equals("mm_close_al")) {
				/**
				 * Main Menu - "Close" button
				 */
				mm.close();
			}
		}
	}

	/*
	 * System Main
	 */

	public static void main(String[] args) {
		/**
		 * Main method. Sets theme to Nimbus and launches the SuperController
		 */

		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (Exception e) {
					System.err
							.println("Nimbus GUI Cannot be found, resorting to default");
				}
				break;
			}
		}

		@SuppressWarnings("unused")
		SuperController c = new SuperController();
	}

}
