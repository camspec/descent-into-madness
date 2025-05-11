package minigames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import data.AppData;

public class CopyTheTextMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private final int numTextPrompts = 27;

	public CopyTheTextMinigame(int x, int y) {
		super("Get typing!", new Color(0, 0, 0), x, y);

		JLabel instructions = new JLabel();
		add(instructions);
		instructions.setBounds(120, 5, 400, 40);
		instructions.setText("Type the following text into the box below.");
		instructions.setFont(new Font("Monospaced", Font.BOLD, 15));
		instructions.setForeground(new Color(0, 255, 0));

		JTextArea inputField = new JTextArea();
		inputField.setBounds(120, 200, 400, 80);
		add(inputField);
		inputField.setFont(new Font("SansSerif", Font.PLAIN, 18));
		inputField.setBackground(new Color(0, 0, 0));
		inputField.setForeground(new Color(0, 255, 0));

		JScrollPane scrollPane = new JScrollPane(inputField);
		add(scrollPane);
		scrollPane.setBounds(120, 200, 400, 80);

		JTextArea exampleTextArea = new JTextArea() {
			private static final long serialVersionUID = 1L;

			// we override the copy method to disable it
			@Override
			public void copy() {
				showErrorMessage("Nice try", "Nice try");
			}

			@Override
			public void cut() {
				showErrorMessage("Nice try", "Nice try");
			}
		};
		exampleTextArea.setBounds(120, 80, 400, 100);
		add(exampleTextArea);
		String exampleText = readTextFile();
		exampleTextArea.setText(exampleText);
		exampleTextArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
		exampleTextArea.setEditable(false);
		exampleTextArea.setForeground(new Color(0, 255, 0));
		exampleTextArea.setBackground(new Color(0, 0, 0));

		JButton submitButton = new JButton();
		submitButton.setBounds(250, 330, 140, 50);
		add(submitButton);
		submitButton.setBackground(new Color(0, 0, 0));
		submitButton.setForeground(new Color(0, 255, 0));
		submitButton.setText("Submit");
		submitButton.setFont(new Font("Monospaced", Font.BOLD, 15));
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String trimmedInput = "";
				String[] inputArray = inputField.getText().split("\n");
				for (String s : inputArray) {
					// remove whitespace from each line by splitting the string and trimming each
					// line
					trimmedInput += s.trim() + '\n';
				}
				// get rid of remaining newlines
				if (trimmedInput.trim().equals(exampleText.trim())) {
					completeMinigame();
				} else {
					showErrorMessage("You mistyped!", "Don't do that again. By the way, it's case-sensitive.");
				}
			}
		});
	}

	private String readTextFile() {
		Scanner reader;
		String text = "";
		int randomQuote = AppData.RNG.nextInt(numTextPrompts);
		int q = 0;
		reader = new Scanner(getClass().getClassLoader().getResourceAsStream("text/copy_the_text/examples.txt"));
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			if (q == randomQuote && !line.contains("//")) {
				// remove whitespace from each line
				text += line.trim() + '\n';
			}
			if (line.isEmpty()) {
				q++;
			}
		}
		reader.close();
		return text;
	}
}
