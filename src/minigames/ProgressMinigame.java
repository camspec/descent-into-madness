package minigames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private double progress = 0;
	private double progressPerClick = 1;
	private int maxProgress = 10000;
	DecimalFormat df = new DecimalFormat("0.00");

	public ProgressMinigame(int x, int y) {
		super("Click click click...", new Color(255, 255, 255), x, y);

		JLabel instructions = new JLabel();
		add(instructions);
		instructions.setBounds(180, 50, 400, 40);
		instructions.setText("Click for progress.");
		instructions.setFont(new Font("SansSerif", Font.BOLD, 30));

		JLabel percentage = new JLabel();
		add(percentage);
		percentage.setBounds(290, 210, 400, 40);
		percentage.setText("0.00%");
		percentage.setFont(new Font("SansSerif", Font.PLAIN, 20));

		JProgressBar progressBar = new JProgressBar();
		add(progressBar);
		progressBar.setBounds(115, 250, 400, 40);
		progressBar.setMaximum(maxProgress);

		JButton progressButton = new JButton();
		add(progressButton);
		progressButton.setBounds(215, 350, 200, 40);
		progressButton.setText("Click me!");
		progressButton.setFont(new Font("SansSerif", Font.PLAIN, 25));
		progressButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 73 clicks required
				progress += progressPerClick;
				progressBar.setValue((int) (progress));
				percentage.setText(df.format(progress / maxProgress * 100) + "%");
				progressPerClick *= 1.1;
				if (progress >= maxProgress) {
					completeMinigame();
				}
			}
		});
	}
}
