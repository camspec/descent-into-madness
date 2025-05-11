package minigames;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import data.AppData;

public class WaldoTreeMinigame extends Minigame {
	private static final long serialVersionUID = 1L;
	private final int galaxies = 3;
	private final int planets = 2;
	private final int countries = 2;
	private final int averageCitizens = 10;
	private final String[] randomCitizenNames = { "Michael", "Shivam", "Tommy", "Saatvik", "Zaid", "Phillip", "Harissh",
			"Ishma", "Mohammad", "Cameron", "Kaiwen", "Caedon", "Jiya", "Megha", "Benjamin", "Saeyon", "Shivaani",
			"Aksejan", "Lakely", "Gariijan", "Skye", "OP", "Roosham", "Alvin", "Srishti", "Virgil", "Allen" };

	public WaldoTreeMinigame(int x, int y) {
		super("Find Waldo.", new Color(255, 255, 255), x, y);

		JLabel instructions = new JLabel();
		instructions.setForeground(new Color(0, 0, 0));
		add(instructions);
		instructions.setBounds(250, 5, 400, 40);
		instructions.setText("Find Waldo.");
		instructions.setFont(new Font("Comic Sans MS", Font.BOLD, 22));

		int waldoGalaxy = AppData.RNG.nextInt(galaxies) + 1;
		int waldoPlanet = AppData.RNG.nextInt(planets) + 1;
		int waldoCountry = AppData.RNG.nextInt(countries) + 1;
		int waldoCitizen = 0;

		DefaultMutableTreeNode universe = new DefaultMutableTreeNode("Universe");
		DefaultMutableTreeNode waldo = new DefaultMutableTreeNode("Waldo");

		for (int i = 1; i <= galaxies; i++) {
			DefaultMutableTreeNode galaxy = new DefaultMutableTreeNode("Galaxy " + i);
			universe.add(galaxy);
			for (int j = 1; j <= planets; j++) {
				DefaultMutableTreeNode planet = new DefaultMutableTreeNode("Planet " + j);
				galaxy.add(planet);
				for (int k = 1; k <= countries; k++) {
					DefaultMutableTreeNode country = new DefaultMutableTreeNode("Country " + k);
					planet.add(country);
					// generate random number within extended range with average
					int citizens = AppData.RNG.nextInt(averageCitizens * 2 - 1) + 1;
					if (i == waldoGalaxy && j == waldoPlanet && k == waldoCountry) {
						waldoCitizen = AppData.RNG.nextInt(citizens) + 1;
					}
					for (int l = 1; l <= citizens; l++) {
						String citizenName = randomCitizenNames[AppData.RNG.nextInt(randomCitizenNames.length)];
						DefaultMutableTreeNode citizen = new DefaultMutableTreeNode(citizenName);
						country.add(citizen);
						if (i == waldoGalaxy && j == waldoPlanet && k == waldoCountry && l == waldoCitizen) {
							country.add(waldo);
						}
					}
				}
			}
		}
		JTree waldoTree = new JTree(universe);
		waldoTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) waldoTree.getLastSelectedPathComponent();

				if (node == null) {
					return;
				} else if (node == waldo) {
					completeMinigame();
				}
			}
		});
		waldoTree.setBounds(50, 50, 520, 350);
		JScrollPane scrollPane = new JScrollPane(waldoTree);
		add(scrollPane);
		scrollPane.setBounds(50, 50, 520, 350);
	}
}
