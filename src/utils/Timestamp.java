package utils;

public class Timestamp {
	private long lastTime = 0;

	public void setTimestamp() {
		lastTime = System.nanoTime();
	}

	public long getTimestamp() {
		return lastTime;
	}

	public long getTimeElapsedMillis() {
		return (System.nanoTime() - lastTime) / 1000000;
	}
}
