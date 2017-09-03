import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by victo on 02-08-2017.
 */
public class PongGame extends Canvas implements Runnable {

	public static String OS;

	public static int width = 1000;
	public static int height = 600;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	Thread gameThread;

	JFrame window;
	Screen screen;
	Keyboard keyboard;

	public static Ball ball;
	Paddle paddleRight, paddleLeft;
	Player playerOne, playerTwo;

	private boolean running;

	public PongGame() {
		OS = System.getProperty("os.name");

		this.setSize(width, height);

		window = new JFrame();
		window.setSize(width, height);
		window.getContentPane().add(this);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.pack();

		screen = new Screen(width, height);

		keyboard = new Keyboard();
		this.addKeyListener(keyboard);
		if (OS == "MacOS") {
			keyboard.disableAccentMenu();
		}

		ball = new Ball(new int[] {width, height});
		ball.x = (width - ball.width) / 2;
		ball.y = (height - ball.height) / 2;
		ball.setVelocity(getRandomVelocity(ball.xVelocity));
		System.out.println(ball.xVelocity);

		paddleRight = new Paddle(width - Paddle.width - 5, (height - Paddle.height) / 2);
		paddleLeft = new Paddle(5, (height - Paddle.height) / 2);

		playerOne = new Player(paddleRight);
		playerTwo = new Player(paddleLeft);

		window.setVisible(true);
		this.requestFocus();
		this.requestFocusInWindow();
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

		keyboard.update();
		playerOne.update(keyboard.getKeys());
		playerTwo.update(new boolean[] {keyboard.getKeys()[2], keyboard.getKeys()[3]});

		if (ball.x <= 0) {
			playerOne.score++;
			ball = new Ball(new int[] {width, height});
			ball.x = (width - ball.width) / 2;
			ball.y = (height - ball.height) / 2;
			ball.setVelocity(getRandomVelocity(ball.xVelocity));
			ball.xVelocity = -(Math.abs(ball.xVelocity));
		} else if (ball.x > width - 20) {
			playerTwo.score++;
			ball = new Ball(new int[] {width, height});
			ball.x = (width - ball.width) / 2;
			ball.y = (height - ball.height) / 2;
			ball.setVelocity(getRandomVelocity(ball.xVelocity));
			ball.xVelocity = (Math.abs(ball.xVelocity));
		}
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

//		screen.setColor(Color.yellow.getRGB());
//		screen.fillRect(100, 0, 30, height);

		ball.render(screen);
		paddleRight.render(screen);
		paddleLeft.render(screen);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		g.drawImage(image, 0, 0, width, height, null);
		screen.clear();
		g.dispose();
		bs.show();
	}

	public float[] getRandomNetVelocity(float netVelocity) {
		float[] velocity = new float[2];



//		velocity[0] = (float) Math.random() * netVelocity;
//		//uses the pythagorean theorem to find what the y velocity should be to get a net force of "netVelocity"
//		netVelocity = (float) Math.sqrt(Math.pow(netVelocity, 2) - Math.pow(velocity[0], 2));
//		velocity[1] = netVelocity;

		velocity[0] = 6;
//		velocity[1] = 3;

		return velocity;
	}

	public float[] getRandomVelocity(float xVelocity) {
		float[] velocity = new float[2];

		float yVelocity = (float) (Math.random() * Math.tan(Math.toRadians(ball.MAX_ANGLE))) * xVelocity;
		if (Math.random() < 0.5) {
			yVelocity = -yVelocity;
		}

		velocity[0] = xVelocity;
		velocity[1] = yVelocity;

		return velocity;
	}

	public static void main(String[] args) {
		PongGame pg = new PongGame();
		pg.window.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (OS == "MacOS") {
					pg.keyboard.enableAccentMenu();
				}
				System.exit(0);
			}
		});
		pg.start();
	}

}