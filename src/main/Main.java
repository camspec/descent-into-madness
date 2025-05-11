package main;

import data.AppData;
import extra.MinigameUI;
import frame.Game;

public class Main {
	public static void main(String[] args) {
		if (AppData.minigameUIEnabled) {
			new MinigameUI();
		} else {
			new Game();
		}
	}
}
