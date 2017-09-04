import java.awt.*;

/**
 * Created by victo on 02-08-2017.
 */
public class Ball {

	public static final float MAX_ANGLE = 55;
	public static float xVelocity = 9.6f;

	int x, y;
	float xPrecise, yPrecise;
	public float yVelocity;
	public int width, height;

	int[] bounds;

	boolean yCollision;

	public Ball(int[] bounds) {
		this(0, 0, bounds);
	}

	public Ball(int x, int y, int[] bounds) {
		System.out.println(this.xPrecise);
		this.x = x;
		this.y = y;
		this.width = 20;
		this.height = 20;
		this.bounds = bounds;
	}

	public void render(Screen screen) {
		screen.fillRect((int) Math.floor(this.x),(int) Math.floor(this.y), width, height);
	}

	public boolean getYCollision() {
		if (yCollision) {
			yCollision = false;
			return true;
		}
		return false;
	}

	public void update() {
		this.xPrecise += xVelocity;
		this.yPrecise += yVelocity;
		this.x = Math.round(xPrecise * 10) / 10;
		this.y = Math.round(yPrecise * 10) / 10;

		if (this.y < 0 || this.y > bounds[1] - height) {
			yCollision = true;
		}
	}

	public void setVelocity(float[] velocity) {
		xVelocity = velocity[0];
		yVelocity = velocity[1];
	}

	public void setX(int x) {
		this.x = x;
		this.xPrecise = x;
	}

	public void setY(int y) {
		this.y = y;
		this.yPrecise = y;
	}
}
