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

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	Thread gameThread;

	JFrame window;
	Screen screen;
	Keyboard keyboard;

	Ball ball;
	Paddle paddleRight, paddleLeft;

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
		float[] velocity = getRandomVelocity(3);
		ball.xVelocity = velocity[0];
		ball.yVelocity = velocity[1];
		System.out.println(ball.xVelocity);

		paddleRight = new Paddle(width - Paddle.width - 5, (height - Paddle.height) / 2);
//		paddleLeft = new Paddle(5, (height - Paddle.height) / 2);

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

		if (paddleRight.collide(ball)) {
			ball.xVelocity = -ball.xVelocity;
		}

//		if (paddleLeft.collide(ball)) {
//			ball.xVelocity = -ball.xVelocity;
//		}

		if (ball.x > width - 20 || ball.x <= 0) {
			ball.xVelocity = -ball.xVelocity;
		}

		keyboard.update();
		paddleRight.update(keyboard.getKeys());
//		paddleLeft.update(new boolean[] {keyboard.getKeys()[2], keyboard.getKeys()[3]});
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
		paddleRight.render(screen);
//		paddleLeft.render(screen);

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

		velocity[0] = 6;
//		velocity[1] = 3;

		return velocity;
	}

	public static void main(String[] args) {
		PongGame pg = new PongGame();
		pg.start();
	}

}
