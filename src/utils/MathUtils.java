package utils;

public class MathUtils {
	public static int clamp(int value, int min, int max) {
		if (value <= min) {
			return min;
		} else if (value >= max) {
			return max;
		} else {
			return value;
		}
	}

	public static double clamp(double value, double min, double max) {
		if (value <= min) {
			return min;
		} else if (value >= max) {
			return max;
		} else {
			return value;
		}
	}

	public static double getPrincipalAngle(double angle) {
		// recursive until an angle between 0 and 360 is found
		if (angle < 0) {
			return getPrincipalAngle(angle + 360);
		} else if (angle > 360) {
			return getPrincipalAngle(angle - 360);
		}
		return angle;
	}
}
