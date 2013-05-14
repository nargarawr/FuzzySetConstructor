import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SliderPanel {

	private JPanel inputOne;
	private JPanel inputTwo;
	private JPanel outputOne;

	private JSlider inputOneSlider;
	private JSlider inputTwoSlider;

	private JLabel outputText;

	private String[][] dataSet;

	private File data;

	public SliderPanel() {

		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		jfc.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
		int returnVal = jfc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			data = new File(jfc.getSelectedFile().getAbsolutePath());

			JPanel sliderPanel = null;
			sliderPanel = addContent(sliderPanel);

			try {
				readFile();
			} catch (IOException e) {
				System.err.println("Could not read in file");
			}

			updateValue(inputOneSlider.getValue(), inputTwoSlider.getValue());

			JOptionPane.showMessageDialog(null, sliderPanel,
					"Interactive Sliders", JOptionPane.PLAIN_MESSAGE);
		}
	}

	public JPanel addContent(JPanel sliderPanel) {
		sliderPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 10;
		c.anchor = GridBagConstraints.NORTH;

		inputOne = new JPanel();
		inputOne.setBorder(BorderFactory.createTitledBorder("Service"));
		c.gridx = 0;
		c.gridy = 0;
		sliderPanel.add(inputOne, c);

		inputOneSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
		inputOneSlider.setMajorTickSpacing(1);
		inputOneSlider.setPaintTicks(true);
		inputOneSlider.setPaintLabels(true);
		inputOneSlider.addChangeListener(new myChangeListener());
		c.gridx = 0;
		c.gridy = 1;
		inputOne.add(inputOneSlider, c);

		inputTwo = new JPanel();
		inputTwo.setBorder(BorderFactory.createTitledBorder("Food Quality"));
		c.gridx = 0;
		c.gridy = 3;
		sliderPanel.add(inputTwo, c);

		inputTwoSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
		inputTwoSlider.setMajorTickSpacing(1);
		inputTwoSlider.setPaintTicks(true);
		inputTwoSlider.setPaintLabels(true);
		inputTwoSlider.addChangeListener(new myChangeListener());
		c.gridx = 0;
		c.gridy = 4;
		inputTwo.add(inputTwoSlider, c);

		outputOne = new JPanel();
		outputOne.setBorder(BorderFactory.createTitledBorder("Tip Quantity"));
		c.gridx = 0;
		c.gridy = 6;
		sliderPanel.add(outputOne, c);

		outputText = new JLabel("");
		c.gridx = 0;
		c.gridy = 7;
		outputOne.add(outputText, c);

		return sliderPanel;
	}

	public void updateValue(int i, int j) {
		outputText.setText(dataSet[i][j]);
	}

	public void readFile() throws IOException {

		ArrayList<String> stringArray = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		String line;

		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(data));
		line = reader.readLine();

		while (line != null) {
			sb.append(line);
			line = reader.readLine();
		}

		line = sb.toString();

		for (String returnVal : line.split(",")) {
			stringArray.add(returnVal);
		}

		final int MAX_VALUE = 11;

		String[][] values = new String[MAX_VALUE][MAX_VALUE];

		int k = 0;

		for (int i = 0; i < MAX_VALUE; i++) {
			for (int j = 0; j < MAX_VALUE; j++) {
				values[i][j] = stringArray.get(k);
				k++;
			}
		}

		dataSet = values;

	}

	class myChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			updateValue(inputOneSlider.getValue(), inputTwoSlider.getValue());
		}
	}
}
