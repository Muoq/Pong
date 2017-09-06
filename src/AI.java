/**
 * Created by victo on 06-09-2017.
 */
public class AI {

	Player player;
	Paddle paddle;
	Ball ball;

	int yPrediction;

	boolean[] keyAction;

	public AI (Player player, Ball ball) {
		this.player = player;
		this.paddle = player.paddle;
		this.ball = ball;
		keyAction = new boolean[2];
	}

	public void update() {
		int deltaX = ball.x - paddle.x - Paddle.width;
//		System.out.println(deltaX);
//		System.out.println("yVelocity: " + ball.yVelocity);
		yPrediction = (int) (((double) ball.yVelocity / Math.abs(ball.xVelocity)) * deltaX + ball.y);
//		System.out.println("yPrediction: " + yPrediction);

		if (paddle.y <= yPrediction && paddle.y + Paddle.height > yPrediction) {
			keyAction[0] = false;
			keyAction[1] = false;
		} else if (paddle.y + (Paddle.height - ball.height) / 2 < yPrediction) {
			keyAction[0] = false;
			keyAction[1] = true;
		} else if (paddle.y + (Paddle.height - ball.height) / 2 > yPrediction) {
			keyAction[0] = true;
			keyAction[1] = false;
		}
	}

	public boolean[] getAction() {
		return keyAction;
	}

}
