/**
 * Created by victo on 03-09-2017.
 */
public class Player {
	public int score;
	Paddle paddle;

	public Player(Paddle paddle) {
		this.paddle = paddle;
	}

	public void update(boolean[] keys) {
		paddle.collide(PongGame.ball);
		paddle.update(keys);
		if (score == 10) {

		}
	}
}
