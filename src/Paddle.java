/**
 * Created by victo on 02-08-2017.
 */
public class Paddle {
	public int y, x;
	public static int width = 15;
	public static int height = 100;
	public float paddleVelocity;

	public Paddle(int x, int y) {
		this.y = y;
		this.x = x;
		paddleVelocity = 8.5f;
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
		if (this.y > PongGame.height - height) {
			this.y = PongGame.height - height;
		}

//		System.out.println(this.y);
	}

	public boolean collide(Ball ball, boolean calledByR) {
		float ballDirectionMultiplier = Ball.MAX_ANGLE / Math.abs(-(ball.height - 1) - (height - ball.height) / 2);
		float ballDirectionOffset = (height - ball.height) / 2 * -ballDirectionMultiplier;

		boolean isHit = false;
		int yRelationship = 0;
		double ballAngle = 0;

		if (ball.getY() > this.y - ball.height && ball.getY() < this.y + height) {
			//checks right paddle
			if (ball.getX() <= this.x) {
				if (ball.getX() + ball.width >= this.x/* && ball.x + ball.width / 3 * 2 < this.x*/) {
					ball.setX(this.x - ball.width);
					yRelationship = ball.getY() - this.y;
					ballAngle = yRelationship * ballDirectionMultiplier + ballDirectionOffset;
					isHit = true;
				}
			//checks left paddle
			} else if (ball.getX() >= this.x + width - ball.width) {
				if (ball.getX() <= this.x + width/* && ball.x + ball.width / 3 * 2 > this.x + width*/) {
					ball.setX(this.x + width);
					yRelationship = ball.getY() - this.y;
					ballAngle = yRelationship * -ballDirectionMultiplier - ballDirectionOffset;
					isHit = true;
				}
			}
			//changes angle of the ball based on where on the paddle it was hit, max angle to x axis is 65 deg
			if (isHit) {
				ball.yVelocity = (float) (ball.xVelocity * Math.tan(Math.toRadians(ballAngle)));
				ball.xVelocity = -ball.xVelocity;
				return true;
			}
		}
		return false;
	}

	public void render(Screen screen) {
		screen.fillRect(this.x, this.y, width, height);
	}

}
