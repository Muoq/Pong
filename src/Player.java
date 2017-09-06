import java.awt.*;

/**
 * Created by victo on 03-09-2017.
 */
public class Player {
	public int score;
	Paddle paddle;
	AI ai;

	public Player(Paddle paddle) {
		this.paddle = paddle;
	}

	public void bindAI(Ball ball) {
		this.ai = new AI(this, ball);
	}

	public void update(boolean[] keys) {
		paddle.collide(PongGame.ball, true);
		paddle.update(keys);
	}

	public void update() {
		ai.update();
		paddle.update(ai.getAction());
		paddle.collide(PongGame.ball, false);
	}
}
