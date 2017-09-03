/**
 * Created by victo on 02-08-2017.
 */
public class Paddle {
	public int y, x;
	public static int width = 15;
	public static int height = 100;
	public int paddleVelocity;

	//no good names for these variables, since they were just values derived from an equation
	//they set are used in a calculation that changes the ball's direction based on where it hit the paddle
	//minimum angle is 15 deg, max is 165 deg
	float ballDirectionMultiplier = 0.8474576f;
	float ballDirectionOffset = -33.898305f;

	public Paddle(int x, int y) {
		this.y = y;
		this.x = x;
		paddleVelocity = 8;
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

	public boolean collide(Ball ball) {
		boolean isHit = false;
		int yRelationship = 0;
		double ballAngle = 0;
		if (ball.y > this.y - ball.height && ball.y < this.y + height) {
			//checks right paddle
			if (ball.x <= this.x) {
				if (ball.x + ball.width >= this.x) {
					yRelationship = ball.y - this.y;
					ballAngle = yRelationship * ballDirectionMultiplier + ballDirectionOffset;
					isHit = true;
				}
			//checks left paddle
			} else if (ball.x >= this.x + width - ball.width) {
				if (ball.x <= this.x + width && ball.x < this.x + width) {
					yRelationship = ball.y - this.y;
					ballAngle = yRelationship * -ballDirectionMultiplier - ballDirectionOffset;
					isHit = true;
				}
			}
			//changes angle of the ball based on where on the paddle it was hit, max angle to x axis is 65 deg
			if (isHit) {
				ball.xVelocity = 7 * ball.xVelocity / Math.abs(ball.xVelocity);
				ball.yVelocity = (float) (ball.xVelocity * Math.tan(Math.toRadians(ballAngle)));
				ball.xVelocity = -ball.xVelocity;
//				System.out.println("yRelationship: " + yRelationship);
//				System.out.println("ballAngle: " + ballAngle);
//				System.out.println("yVelocity: " + (float) (ball.xVelocity * Math.tan(Math.toRadians(ballAngle))));
//				System.out.println("xVelocity: " + ball.xVelocity);
//				System.out.println("netVelocity: " + (Math.sqrt(Math.pow(ball.xVelocity, 2) + Math.pow(ball.yVelocity, 2))));
				return true;
			}
		}
		return false;
	}

	public void render(Screen screen) {
		screen.fillRect(this.x, this.y, width, height);
	}

}
