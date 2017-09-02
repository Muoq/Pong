/**
 * Created by victo on 02-08-2017.
 */
public class Paddle {
	public int y, x;
	public int height, width;
	public int paddleVelocity;

	int hitBoxSurface;

	public Paddle(int x, int y) {
		this.y = y;
		this.x = x;
		paddleVelocity = 6;

		if (this.x < PongGame.width / 2) {
			hitBoxSurface = this.x + this.width;
		} else {
			hitBoxSurface = this.x;
		}
	}

	public void update(boolean[] keys) {
		if (keys[0] && keys[1]) {
			return;
		} else if (keys[0]) {
			this.y -=paddleVelocity;
		} else if (keys[1]) {
			this.y += paddleVelocity;
		}

		if (this.y < 0) {
			this.y = 0;
		}
		if (this.y > PongGame.height - this.height) {
			this.y = PongGame.height - this.height;
		}

//		System.out.println(this.y);
	}

	public boolean collide(Ball ball) {
		System.out.println(ball.x);

		return false;
	}

	public void render(Screen screen) {
		screen.fillRect(this.x, this.y, this.width, this.height);
	}

}
