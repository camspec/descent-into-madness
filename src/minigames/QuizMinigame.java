package minigames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import data.AppData;
import utils.ImageUtils;

public class QuizMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private final int numEasyQuestions = 25;
	private final int numImpossibleQuestions = 10;
	private String question;
	private String[] answers;
	private int correctAnswer;
	private final int questionCycles = 1;
	private int consecutiveCorrects = 0;
	private int randomEasyQuestion = AppData.RNG.nextInt(numEasyQuestions);
	private int randomImpossibleQuestion = AppData.RNG.nextInt(numImpossibleQuestions);

	public QuizMinigame(int x, int y) {
		super("Easy Simple Quiz!™", new Color(0, 255, 0), x, y);

		JLabel title = new JLabel();
		add(title);
		title.setBounds(10, 20, 640, 40);
		title.setText("Easy Simple Quiz!™,The 2024 Educationfor Young Children ,brain Teaser Test your smarts");
		title.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		title.setForeground(new Color(0, 0, 255));

		ImageIcon thumbnailIcon = ImageUtils.getImageIcon("images/minigames/quiz/thumbnail.png");
		JLabel thumbnail = new JLabel(thumbnailIcon);
		add(thumbnail);
		thumbnail.setBounds(0, 50, 640, 300);

		JButton submitButton = new JButton();
		submitButton.setBounds(250, 330, 140, 50);
		add(submitButton);
		submitButton.setBackground(new Color(255, 0, 255));
		submitButton.setForeground(new Color(255, 255, 0));
		submitButton.setText("PLAY GAME !");
		submitButton.setFont(new Font("Arial", Font.BOLD, 15));
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
	}

	private void startGame() {
		askQuestion("Get three questions right in a row to win!", new String[] { "O.K.", "I understand", "Yes" });
		// question cycles before we let the player actually win
		for (int i = 0; i < questionCycles; i++) {
			// 2 easy questions, 1 impossible question
			for (int j = 0; j < 2; j++) {
				getEasyQuestion();
				if (askQuestion(question, answers) != correctAnswer) {
					showErrorMessage("Wrong answer!", "You silly goose!");
				}
			}
			getImpossibleQuestion();
			askQuestion(question, answers);
			showErrorMessage("Wrong answer!", "You silly goose!");
		}
		while (consecutiveCorrects < 3) {
			getEasyQuestion();
			if (askQuestion(question, answers) == correctAnswer) {
				consecutiveCorrects++;
			} else {
				consecutiveCorrects = 0;
				showErrorMessage("Wrong answer!", "You silly goose!");
			}
		}
		askQuestion(
				"Thank you very much for playing Easy Simple Quiz!™ the new best game for children and adults alike to enjoy and play lots of fun!",
				new String[] { "O.K.", "I understand", "Yes" });
		completeMinigame();
	}

	private int askQuestion(String question, String[] answers) {
		return JOptionPane.showOptionDialog(this, question, "Easy Simple Quiz!™", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, answers, null);
	}

	private void getEasyQuestion() {
		Scanner reader;
		randomEasyQuestion++;
		if (randomEasyQuestion == numEasyQuestions) {
			randomEasyQuestion = 0;
		}
		int q = 0;
		reader = new Scanner(getClass().getClassLoader().getResourceAsStream("text/quiz/easy_questions.txt"), "UTF-8");
		while (reader.hasNextLine()) {
			String nextQuestion = reader.nextLine();
			String[] nextAnswers = reader.nextLine().split(", ");
			int nextCorrectAnswer = Integer.parseInt(reader.nextLine());
			if (q == randomEasyQuestion) {
				question = nextQuestion;
				answers = nextAnswers;
				correctAnswer = nextCorrectAnswer;
			}
			q++;
		}
		reader.close();
	}

	private void getImpossibleQuestion() {
		Scanner reader;
		randomImpossibleQuestion++;
		if (randomImpossibleQuestion == numImpossibleQuestions) {
			randomImpossibleQuestion = 0;
		}
		int q = 0;
		reader = new Scanner(getClass().getClassLoader().getResourceAsStream("text/quiz/impossible_questions.txt"),
				"UTF-8");
		while (reader.hasNextLine()) {
			String nextQuestion = reader.nextLine();
			String[] nextAnswers = reader.nextLine().split(", ");
			if (q == randomImpossibleQuestion) {
				question = nextQuestion;
				answers = nextAnswers;
			}
			q++;
		}
		reader.close();
	}
}
