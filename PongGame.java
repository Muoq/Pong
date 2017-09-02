import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by victo on 02-08-2017.
 */
public class PongGame extends Canvas implements Runnable {

	public static int width = 1000;
	public static int height = 600;
	public static int paddleHeight, paddleWidth;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	Thread gameThread;

	JFrame window;
	Screen screen;
	Keyboard keyboard;

	Ball ball;
	Paddle paddle;

	private boolean running;

	public PongGame() {
		this.setSize(width, height);

		window = new JFrame();
		window.setSize(width, height);
		window.getContentPane().add(this);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.pack();

		screen = new Screen(width, height);

		keyboard = new Keyboard();
		this.addKeyListener(keyboard);

		ball = new Ball(new int[] {width, height});
		ball.x = (width - ball.width) / 2;
		ball.y = (height - ball.height) / 2;
		float[] velocity = getRandomVelocity(6);
		ball.xVelocity = velocity[0];
		ball.yVelocity = velocity[1];

		paddleHeight = 70;
		paddleWidth = 15;
		paddle = new Paddle(width - paddleWidth - 5, (height - paddleHeight) / 2);
		paddle.height = paddleHeight;
		paddle.width = paddleWidth;

		window.setVisible(true);
		requestFocus();
	}

	public synchronized void start() {
		gameThread = new Thread(this,"Pong");
		running = true;
		gameThread.start();
	}

	public void run() {
		long FPSTrack = 0;
		long UPSTrack = 0;
		long oneSecondTimer = 0;

		long pastTime = System.nanoTime();
		double deltaTime = 0;
		double frequency = 1000000000 / 60.0;
		while(running) {

			long nowTime = System.nanoTime();
			deltaTime += (nowTime - pastTime) / (float) (frequency);
			pastTime = nowTime;


			while (deltaTime >= 1) {
				update();
				UPSTrack++;
				deltaTime--;
			}

			if ((System.nanoTime() - oneSecondTimer) / 1000000000.0 >= 1) {
				window.setTitle("FPS: " + FPSTrack + " | " + "UPS: " + UPSTrack);
				FPSTrack = 0;
				UPSTrack = 0;
				oneSecondTimer = System.nanoTime();
			}

			render();
			FPSTrack++;

		}
	}

	public void update() {
		ball.update();

		if (ball.getYCollision()) {
			ball.yVelocity = -ball.yVelocity;
		}

//		if (ball.x > paddle.hitBoxSurface - ball.width) {
//			if (paddle.collide(ball)) {
//				ball.xVelocity = -ball.xVelocity;
//				System.out.println(ball.x);
//				System.out.println(paddle.hitBoxSurface);
//			}
//		}

		if (paddle.collide(ball)) {
			ball.xVelocity = -ball.xVelocity;
		}

		if (ball.x > width - 20 || ball.x <= 0) {
			ball.xVelocity = -ball.xVelocity;
		}

		keyboard.update();
		paddle.update(keyboard.getKeys());
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		screen.g = g;

		screen.setBackgroundColor(0);
		screen.setColor(0xffffff);

		ball.render(screen);
		paddle.render(screen);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		g.drawImage(image, 0, 0, width, height, null);
		screen.clear();
		g.dispose();
		bs.show();
	}

	public float[] getRandomVelocity(float netVelocity) {
		float[] velocity = new float[2];



//		velocity[0] = (float) Math.random() * netVelocity;
//		//uses the pythagorean theorem to find what the y velocity should be to get a net force of "netVelocity"
//		netVelocity = (float) Math.sqrt(Math.pow(netVelocity, 2) - Math.pow(velocity[0], 2));
//		velocity[1] = netVelocity;

		velocity[0] = 5.068f;
		velocity[1] = 3;

		return velocity;
	}

	public static void main(String[] args) {
		PongGame pg = new PongGame();
		pg.start();
	}

}
