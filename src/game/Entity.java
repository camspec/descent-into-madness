package game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import data.AppData;
import utils.ImageUtils;
import utils.MathUtils;

public class Entity {
	private BufferedImage image;
	private double x = 150, y = 150; // default spawn point if not on map
	private double entityDistance;
	private double entityAngle, playerAngleLeftBound;
	private int entitySize, entityScreenX;
	private int entityScreenY = (int) (0.52 * AppData.SCREEN_HEIGHT);
	private double speed = 0;
	public LinkedList<Node> path = new LinkedList<>(); // functionally a stack
	public int gridX;
	public int gridY;

	public Entity(String imagePath) {
		image = ImageUtils.getImage(imagePath);
	}

	public void updateEntity() {
		gridX = (int) Math.floor(x / GridMap.UNIT_SIZE);
		gridY = (int) Math.floor(y / GridMap.UNIT_SIZE);
		if (!path.isEmpty()) {
			move(path.peekLast().x, path.peekLast().y);
		}
		double entityDistanceX = x - Player.getX();
		double entityDistanceY = y - Player.getY();

		entityDistance = Point2D.distance(Player.getX(), Player.getY(), x, y);

		// use atan2 to get angle without dealing with quadrants
		// -y for flipped axis
		entityAngle = Math.toDegrees(Math.atan2(-entityDistanceY, entityDistanceX));
		entityAngle = MathUtils.getPrincipalAngle(entityAngle);

		// multiply max size by ratio of projection distance to entity distance
		entitySize = (int) (GridMap.UNIT_SIZE * Ray.PROJECTION_DISTANCE / entityDistance);

		playerAngleLeftBound = MathUtils.getPrincipalAngle(Player.getPlayerAngle() + 45); // add half of fov (90)

		// angle between left side of fov and entity
		double angleFromLeft = getAngleFromLeft();

		// it's not perfect, but it gets the job done
		// from the left ray, find ratio of entity's angle to fov and multiply by screen
		// width. subtract half the entity size to center image
		entityScreenX = (int) (AppData.SCREEN_WIDTH * angleFromLeft / 90 - entitySize / 2);

		if (Math.abs(Player.getX() - x) < GridMap.UNIT_SIZE / 2
				&& Math.abs(Player.getY() - y) < GridMap.UNIT_SIZE / 2) {
			Player.reduceHealth(4);
		}
	}

	private double getAngleFromLeft() {
		double angleFromLeft = playerAngleLeftBound - entityAngle;
		/*
		 * if the left side of fov and entity are in quadrants 1 and 4 or 4 and 1, the
		 * difference is much greater than normal (e.g. 45 - 360) though we can still
		 * see the entity when it's outside the fov due to its width, so I leave a bit
		 * more room for that with 110 and 250 instead of 90 and 270
		 */
		if (playerAngleLeftBound <= 110 && entityAngle >= 250) {
			// relative angle is negative, so add 360
			angleFromLeft += 360;
		} else if (entityAngle <= 110 && playerAngleLeftBound >= 250) {
			// relative angle is bigger than normal, so subtract 360
			angleFromLeft -= 360;
		}
		return angleFromLeft;
	}

	public void generatePath() {
		// A* pathfinding algorithm

		Node[][] nodes = new Node[GridMap.MAP_WIDTH][GridMap.MAP_HEIGHT];

		for (int y = 0; y < GridMap.MAP_HEIGHT; y++) {
			for (int x = 0; x < GridMap.MAP_WIDTH; x++) {
				nodes[x][y] = new Node(x, y);
			}
		}

		ArrayList<Node> openNodes = new ArrayList<>();
		ArrayList<Node> closedNodes = new ArrayList<>();

		Node startNode = nodes[getGridX()][getGridY()];
		openNodes.add(startNode);

		Node currentNode;

		while (true) {
			Node bestNode = null;
			double lowestScore = 0;
			boolean firstNode = true;
			// get node with lowest f
			for (Node n : openNodes) {
				if (firstNode) {
					lowestScore = n.f;
					bestNode = n;
					firstNode = false;
				} else if (n.f < lowestScore) {
					lowestScore = n.f;
					bestNode = n;
				}
			}
			currentNode = bestNode;
			openNodes.remove(currentNode);
			closedNodes.add(currentNode);

			if (currentNode.x == Player.getGridX() && currentNode.y == Player.getGridY()) {
				break;
			}

			// top left, clockwise
			int[] dx = { -1, 0, 1, 1, 1, 0, -1, -1 };
			int[] dy = { -1, -1, -1, 0, 1, 1, 1, 0 };
			for (int i = 0; i < 8; i++) {
				int neighbourX = currentNode.x + dx[i];
				int neighbourY = currentNode.y + dy[i];
				if (GridMap.checkValidPath(neighbourX, neighbourY)
						&& !closedNodes.contains(nodes[neighbourX][neighbourY])) {
					currentNode.neighbours.add(nodes[neighbourX][neighbourY]);
				}
			}
			for (Node n : currentNode.neighbours) {
				double g = 0;
				Node node = n;
				// get currently travelled distance (of path) between node and start
				while (node != startNode) {
					if (node.parent == null) {
						g += Point2D.distance(node.x, node.y, currentNode.x, currentNode.y);
						node = currentNode;
					} else {
						g += Point2D.distance(node.x, node.y, node.parent.x, node.parent.y);
						node = node.parent;
					}

				}
				// get distance between node and player
				double h = Point2D.distance(n.x, n.y, Player.getGridX(), Player.getGridY());

				// get f values of nodes to open or nodes already opened that have a new shorter
				// path
				if (g + h < n.f || !openNodes.contains(n)) {
					n.f = g + h;

					n.parent = currentNode;
					if (!openNodes.contains(n)) {
						openNodes.add(n);
					}
				}
			}
		}

		// follow path backwards
		Node node = currentNode;
		while (node != startNode) {
			path.add(node);
			node = node.parent;
		}
	}

	private class Node {
		int x, y;
		Node parent = null;
		double f;
		ArrayList<Node> neighbours = new ArrayList<>();

		private Node(int x, int y) {
			this.x = x;
			this.y = y;

		}
	}

	private void move(int targetGridX, int targetGridY) {
		int targetX = targetGridX * GridMap.UNIT_SIZE + GridMap.UNIT_SIZE / 2;
		int targetY = targetGridY * GridMap.UNIT_SIZE + GridMap.UNIT_SIZE / 2;
		Point directionVector = new Point((int) (targetX - x), (int) (targetY - y));
		// don't divide by zero!
		if (Point2D.distance(x, y, targetX, targetY) > speed && directionVector.distance(0, 0) != 0) {
			// normalize vector
			x += directionVector.getX() / directionVector.distance(0, 0) * speed;
			y += directionVector.getY() / directionVector.distance(0, 0) * speed;
		} else {
			x = targetX;
			y = targetY;
			path.removeLast();
		}
	}

	public void drawEntity(Graphics g) {
		// subtract half entity size to center image
		g.drawImage(image, entityScreenX, entityScreenY - entitySize / 2, entitySize, entitySize, null);
	}

	public int getGridX() {
		return (int) Math.floor(x / GridMap.UNIT_SIZE);
	}

	public int getGridY() {
		return (int) Math.floor(y / GridMap.UNIT_SIZE);
	}

	public int getEntityScreenX() {
		return entityScreenX;
	}

	public int getEntitySize() {
		return entitySize;
	}

	public double getEntityDistance() {
		return entityDistance;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
