package game;

import java.awt.Point;

import utils.MathUtils;

public class Player {
	private static double x = 150, y = 150; // default spawn point if not on map
	private static double playerAngle = 90;
	private static double baseSpeed = 3, speed = baseSpeed, turnSpeed = 2;
	private static double sprintMultiplier = 1.7;
	public static boolean forward, backward, left, right, turningLeft, turningRight, sprinting;
	private static boolean walking;
	private static int hitboxSize = 15;
	private static double maxStamina = 100, stamina = maxStamina;
	private static double staminaFrames = 0;
	private static double staminaCooldownFrames = 200;
	private static double staminaRegenSpeed = 0.3;
	private static double staminaUseSpeed = 0.7;
	private static boolean staminaBlock = false;
	private static int maxHealth = 100, health = maxHealth;
	private static boolean dead = false;

	public static void updateMovement() {
		// vector for movement relative to the direction the player faces
		Point direction = new Point(0, 0);
		if (forward) {
			direction.setLocation(direction.getX(), direction.getY() + 1);
		}
		if (backward) {
			direction.setLocation(direction.getX(), direction.getY() - 1);
		}
		if (left) {
			direction.setLocation(direction.getX() - 1, direction.getY());
		}
		if (right) {
			direction.setLocation(direction.getX() + 1, direction.getY());
		}
		double directionMagnitude = direction.distance(0, 0);
		// we're walking if direction vector's magnitude is not 0
		walking = directionMagnitude != 0;

		// normalize movement vector by dividing each component by magnitude
		if (directionMagnitude > 1) {
			direction.setLocation(direction.getX() / directionMagnitude, direction.getY() / directionMagnitude);
		}

		// the player moves based on the direction it is facing. horizontal movement
		// uses a phase shift of vertical movement
		// e.g. moving to the right is the same as rotating 90 degrees to the right and
		// moving forward
		// subtract 90 as turning right is -90 degrees from standard position
		double deltaX = direction.getY() * Math.cos(Math.toRadians(playerAngle))
				+ direction.getX() * Math.cos(Math.toRadians(playerAngle - 90));
		// y-axis is flipped from standard orientation
		double deltaY = -(direction.getY() * Math.sin(Math.toRadians(playerAngle))
				+ direction.getX() * Math.sin(Math.toRadians(playerAngle - 90)));

		if (turningLeft) {
			playerAngle += turnSpeed;
		}
		if (turningRight) {
			playerAngle -= turnSpeed;
		}

		playerAngle = MathUtils.getPrincipalAngle(playerAngle);

		// handle stamina
		if (staminaFrames == 0) {
			stamina += staminaRegenSpeed;
		}
		stamina = MathUtils.clamp(stamina, 0, maxStamina);
		if (sprinting && stamina > 0 && !staminaBlock && walking) {
			speed = baseSpeed * sprintMultiplier;
			stamina -= staminaUseSpeed;
		} else {
			speed = baseSpeed;
		}
		if (stamina == 0) {
			staminaFrames++;
		}
		if (staminaFrames == staminaCooldownFrames) {
			staminaFrames = 0;
			staminaBlock = true;
		}
		if (staminaBlock && stamina > maxStamina / 4) {
			staminaBlock = false;
		}

		// the "hitbox" is really just four points around the player forming a square
		// it works pretty well when the only objects we can collide with are squares
		// themselves
		double[][] hitbox = { { x + hitboxSize, y - hitboxSize }, { x + hitboxSize, y + hitboxSize },
				{ x - hitboxSize, y - hitboxSize }, { x - hitboxSize, y + hitboxSize } };
		boolean canMoveX = true, canMoveY = true;
		for (int i = 0; i < 4; i++) {
			// test if each hitbox point is in a wall after the frame's movement
			// we separate x and y so that you can still move in one dimension and "slide"
			// across walls
			int gridX = (int) (Math.floor(hitbox[i][0]) / GridMap.UNIT_SIZE);
			int gridY = (int) (Math.floor(hitbox[i][1]) / GridMap.UNIT_SIZE);
			int movedGridX = (int) (Math.floor(hitbox[i][0] + deltaX * speed) / GridMap.UNIT_SIZE);
			int movedGridY = (int) (Math.floor(hitbox[i][1] + deltaY * speed) / GridMap.UNIT_SIZE);
			if (GridMap.gameMap[movedGridX][gridY] != -1) {
				canMoveX = false;
			}
			if (GridMap.gameMap[gridX][movedGridY] != -1) {
				canMoveY = false;
			}
		}

		// finally move the player with adjustable speed
		if (canMoveX) {
			x += deltaX * speed;
		}
		if (canMoveY) {
			y += deltaY * speed;
		}
	}

	public static Point getAdjacentMinigame() {
		int gridX = (int) (x / GridMap.UNIT_SIZE);
		int gridY = (int) (y / GridMap.UNIT_SIZE);
		int[] dx = { -1, 0, 0, 1 };
		int[] dy = { 0, -1, 1, 0 };
		for (int i = 0; i < 4; i++) {
			if (GridMap.gameMap[gridX + dx[i]][gridY + dy[i]] == 6) {
				return new Point(gridX + dx[i], gridY + dy[i]);
			}
		}
		return null;
	}

	static void setX(double x) {
		Player.x = x;
	}

	static void setY(double y) {
		Player.y = y;
	}

	static double getX() {
		return x;
	}

	static double getY() {
		return y;
	}

	public static int getGridX() {
		return (int) Math.floor(x / GridMap.UNIT_SIZE);
	}

	public static int getGridY() {
		return (int) Math.floor(y / GridMap.UNIT_SIZE);
	}

	static double getPlayerAngle() {
		return playerAngle;
	}

	public static double getStamina() {
		return stamina;
	}

	public static boolean isStaminaBlocked() {
		return staminaBlock;
	}

	public static int getHealth() {
		return health;
	}

	public static void reduceHealth(int damage) {
		if (health > 0) {
			health -= damage;
			if (health <= 0) {
				health = 0;
				dead = true;
			}
		}
	}

	public static boolean isDead() {
		return dead;
	}

	public static void stopControls() {
		forward = false;
		backward = false;
		left = false;
		right = false;
		turningLeft = false;
		turningRight = false;
		sprinting = false;
	}
}
