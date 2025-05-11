package minigames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPasswordField;

import data.AppData;

public class CaptchaMinigame extends Minigame {
	private static final long serialVersionUID = 1L;

	public CaptchaMinigame(int x, int y) {
		super("Please complete this captcha before accessing the minigame.", new Color(255, 255, 255), x, y);

		JLabel instructions1 = new JLabel();
		add(instructions1);
		instructions1.setBounds(120, 50, 400, 40);
		instructions1.setText("Prove you are not a robot.");
		instructions1.setFont(new Font("Monospaced", Font.BOLD, 15));

		JLabel instructions2 = new JLabel();
		add(instructions2);
		instructions2.setBounds(120, 70, 400, 40);
		instructions2.setText("Type the characters you see in the picture:");
		instructions2.setFont(new Font("Monospaced", Font.BOLD, 15));

		JLabel captchaText = new JLabel();
		String captcha = generateCaptcha("oO01lI", 14);
		add(captchaText);
		captchaText.setBounds(120, 100, 400, 40);
		captchaText.setText(captcha);
		captchaText.setFont(new Font("Serif", Font.ITALIC, 40));

		JPasswordField inputField = new JPasswordField();
		add(inputField);
		inputField.setBounds(120, 300, 400, 40);
		inputField.setFont(new Font("SansSerif", Font.BOLD, 50));
		inputField.addActionListener(new ActionListener() {
			@Override
			@SuppressWarnings("deprecation") // usually you don't store passwords in strings, but this isn't actually a
												// password
			public void actionPerformed(ActionEvent e) {
				if (inputField.getText().equals(captchaText.getText())) {
					completeMinigame();
				} else {
					showErrorMessage("Error", "You done messed up now");
				}
			}
		});
	}

	private String generateCaptcha(String possibleCharacters, int length) {
		String captcha = "";
		int randomIndex;
		for (int i = 0; i < length; i++) {
			randomIndex = AppData.RNG.nextInt(possibleCharacters.length());
			captcha += possibleCharacters.charAt(randomIndex);
		}
		return captcha;
	}
}
