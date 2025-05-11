package game;

import java.awt.Color;
import java.awt.Graphics;

import data.AppData;
import utils.MathUtils;

public class Ray {
	private double relativeAngle, vIntersectX, vIntersectY, hIntersectX, hIntersectY, distanceVertical,
			distanceHorizontal, fixedDistance;
	private double distance;
	private int rayNumber, hitBlockX, hitBlockY, hitBlock;
	private int wallSlice;
	static final int PROJECTION_DISTANCE = 640;
	private double degreeAngle;
	Color floorColor = new Color(30, 32, 33);

	public Ray(int num) {
		this.rayNumber = num;
		// for 2d projection the distances between intersections must be constant, not
		// the angles
		// base the angles off constant length intervals (ray number) and the distance
		// to the 2D projection plane
		relativeAngle = Math.toDegrees(Math.atan((double) -num / PROJECTION_DISTANCE));
	}

	public void cast() {
		degreeAngle = Player.getPlayerAngle() + relativeAngle;
		degreeAngle = MathUtils.getPrincipalAngle(degreeAngle);

		// because the map is a grid, we can just check collisions with grid lines
		// instead of having periodic checks
		// check vertical line collisions
		int collisionChecks = 0;
		do {
			if (degreeAngle <= 90 || degreeAngle >= 270) {
				// ray facing right
				vIntersectX = Math.ceil(Player.getX() / GridMap.UNIT_SIZE) * GridMap.UNIT_SIZE
						+ collisionChecks * GridMap.UNIT_SIZE;
				hitBlockX = (int) vIntersectX / GridMap.UNIT_SIZE;

			} else {
				// ray facing left
				vIntersectX = Math.floor(Player.getX() / GridMap.UNIT_SIZE) * GridMap.UNIT_SIZE
						- collisionChecks * GridMap.UNIT_SIZE;
				hitBlockX = (int) vIntersectX / GridMap.UNIT_SIZE - 1;
			}
			// to use the trig ratio we must convert vIntersectX to a length relative to the
			// player, and then convert back
			vIntersectY = -((vIntersectX - Player.getX()) * Math.tan(Math.toRadians(degreeAngle))) + Player.getY();
			hitBlockY = (int) vIntersectY / GridMap.UNIT_SIZE;

			// when no collision is detected, continue the ray until the next grid line by
			// adding the unit size
			collisionChecks++;

			// prevent out of bounds error by ensuring the hit block is not outside the map
			hitBlockX = MathUtils.clamp(hitBlockX, 0, GridMap.MAP_WIDTH - 1);
			hitBlockY = MathUtils.clamp(hitBlockY, 0, GridMap.MAP_HEIGHT - 1);
		} while (GridMap.gameMap[hitBlockX][hitBlockY] == -1); // keep checking until the ray has collided
		// distance between player and the intersection
		distanceVertical = Math
				.sqrt(Math.pow(vIntersectX - Player.getX(), 2) + Math.pow(vIntersectY - Player.getY(), 2));

		hitBlock = GridMap.gameMap[hitBlockX][hitBlockY];

		// check horizontal line collisions
		collisionChecks = 0;
		do {
			if (degreeAngle >= 0 && degreeAngle <= 180) {
				// ray facing up
				hIntersectY = Math.floor(Player.getY() / GridMap.UNIT_SIZE) * GridMap.UNIT_SIZE
						- collisionChecks * GridMap.UNIT_SIZE;
				hitBlockY = (int) hIntersectY / GridMap.UNIT_SIZE - 1;
			} else {
				// ray facing down
				hIntersectY = Math.ceil(Player.getY() / GridMap.UNIT_SIZE) * GridMap.UNIT_SIZE
						+ collisionChecks * GridMap.UNIT_SIZE;
				hitBlockY = (int) hIntersectY / GridMap.UNIT_SIZE;
			}
			hIntersectX = -((hIntersectY - Player.getY()) / Math.tan(Math.toRadians(degreeAngle))) + Player.getX();
			hitBlockX = (int) hIntersectX / GridMap.UNIT_SIZE;

			collisionChecks++;

			hitBlockX = MathUtils.clamp(hitBlockX, 0, GridMap.MAP_WIDTH - 1);
			hitBlockY = MathUtils.clamp(hitBlockY, 0, GridMap.MAP_HEIGHT - 1);
		} while (GridMap.gameMap[hitBlockX][hitBlockY] == -1);
		distanceHorizontal = Math
				.sqrt(Math.pow(hIntersectX - Player.getX(), 2) + Math.pow(hIntersectY - Player.getY(), 2));

		if (distanceHorizontal < distanceVertical) {
			hitBlock = GridMap.gameMap[hitBlockX][hitBlockY];
		}

		distance = Math.min(distanceVertical, distanceHorizontal);
		// the ray's distance travelled is whichever collided first
		// to remove the "fisheye" effect of middle rays being shorter than outer rays,
		// take only the vertical component of each ray vector relative to player angle
		fixedDistance = Math.min(distanceVertical, distanceHorizontal) * Math.cos(Math.toRadians(relativeAngle));
	}

	public void drawRay(Graphics g) {
		// if screen width is 1280, rays are from -640 to 640 excluding 0, so rays -640
		// to -1 represent pixels 0 to 639 and 1 to 640 represent 640 to 1279
		int sliceX;
		if (rayNumber < 0) {
			sliceX = rayNumber + AppData.SCREEN_WIDTH / 2;
		} else {
			sliceX = rayNumber + AppData.SCREEN_WIDTH / 2 - 1;
		}
		// slice height is inversely proportional to the ray's distance (farther things
		// appear smaller)
		double sliceHeight = GridMap.UNIT_SIZE / fixedDistance * PROJECTION_DISTANCE;
		double sliceY1 = (double) AppData.SCREEN_HEIGHT / 2 - sliceHeight / 2; // top of slice
		double sliceY2 = (double) AppData.SCREEN_HEIGHT / 2 + sliceHeight / 2; // bottom of slice

		// check whether the ray hit top/bottom or left/right of a block
		if (distanceVertical < distanceHorizontal) {
			// account for flipped textures when rays are facing the other direction
			if (degreeAngle <= 90 || degreeAngle >= 270) {
				// ray facing right
				wallSlice = (int) (vIntersectY % GridMap.UNIT_SIZE); // choose a texture slice based on where block is
																		// hit

			} else {
				// ray facing left
				wallSlice = (int) (GridMap.UNIT_SIZE - vIntersectY % GridMap.UNIT_SIZE - 1);
			}
		} else {
			if (degreeAngle >= 0 && degreeAngle <= 180) {
				// ray facing up
				wallSlice = (int) (hIntersectX % GridMap.UNIT_SIZE);
			} else {
				// ray facing down
				wallSlice = (int) (GridMap.UNIT_SIZE - hIntersectX % GridMap.UNIT_SIZE - 1);
			}
		}

		if (GridMap.wallSlices != null) {
			g.drawImage(GridMap.wallSlices[hitBlock][wallSlice], sliceX, (int) Math.round(sliceY1), 1,
					(int) Math.round(sliceHeight), null);
		}
		g.setColor(floorColor);
		g.drawLine(sliceX, (int) sliceY2 - 1, sliceX, AppData.SCREEN_HEIGHT);
	}

	public double getDistance() {
		return distance;
	}
}