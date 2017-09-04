import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by victo on 02-08-2017.
 */
public class PongGame extends Canvas implements Runnable {

	public static final boolean DEBUG = false;

	public static String OS;

	public static int width = 900;
	public static int height = 600;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	Thread gameThread;

	JFrame window;
	Screen screen;
	Keyboard keyboard;
	public static Font pressStart50p, pressStart60p;

	public static Ball ball;
	Paddle paddleRight, paddleLeft;
	Player playerOne, playerTwo;
	boolean newScore;
	int winScore;
	boolean winScreen;

	private boolean running;

	private boolean spaceToggle;

	public PongGame() {
		OS = System.getProperty("os.name");

		this.setSize(width, height);

		window = new JFrame();
		window.setSize(width, height);
		window.setResizable(false);
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

		pressStart50p = new Font("Press Start 2P", Font.TRUETYPE_FONT, 50);
		pressStart60p = new Font("Press Start 2P", Font.TRUETYPE_FONT, 60);

		initGame();

		winScore = 10;

		window.setVisible(true);
		this.requestFocus();
		this.requestFocusInWindow();

		if (DEBUG) {
			ball.xVelocity = 1;
		}
	}

	private void initGame() {
		ball = new Ball(new int[] {width, height});
		ball.setX((width - ball.width) / 2);
		ball.setY((height - ball.height) / 2);
		ball.setVelocity(getRandomVelocity(ball.xVelocity, true));

		paddleRight = new Paddle(width - Paddle.width - 5, (height - Paddle.height) / 2);
		paddleLeft = new Paddle(5, (height - Paddle.height) / 2);

		playerOne = new Player(paddleRight);
		playerTwo = new Player(paddleLeft);
	}

	private void resetPlayingField() {
		ball = new Ball(new int[] {width, height});
		ball.setX((width - ball.width) / 2);
		ball.setY((height - ball.height) / 2);
		ball.setVelocity(getRandomVelocity(ball.xVelocity, false));
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
				if (!winScreen) {
					update();
				} else {
					keyboard.update();
					if (keyboard.getKeys()[4]) {
						winScreen = false;
						initGame();
					}
				}
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

		if (ball.x < 0) {
			playerOne.score++;
			resetPlayingField();
			ball.xVelocity = +(Math.abs(ball.xVelocity));
			newScore = true;
		} else if (ball.x > width - 20) {
			playerTwo.score++;
			resetPlayingField();
			ball.xVelocity = -(Math.abs(ball.xVelocity));
			newScore = true;
		}

		if (playerOne.score == winScore || playerTwo.score == winScore) {
			winScreen = true;
		}

		if (DEBUG) {
			if (keyboard.getExtraKeys()[0] && !spaceToggle) {
				ball.xVelocity = 6 * ball.xVelocity / Math.abs(ball.xVelocity);
				spaceToggle = true;
			} else if (!keyboard.getExtraKeys()[0] && spaceToggle) {
				ball.xVelocity = 1 * ball.xVelocity / Math.abs(ball.xVelocity);
				spaceToggle = false;
			}
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

		screen.drawMiddleLine();

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

		drawPlayerScore(g);
		drawWinnerMessage(g);

		screen.clear();
		g.dispose();
		bs.show();
	}

	private void drawPlayerScore(Graphics g) {
		g.setColor(Color.white);
		g.setFont(pressStart50p);
		FontMetrics fM = g.getFontMetrics();
		int pOneScoreWidth = fM.stringWidth("0");
		int pTwoScoreWidth = fM.stringWidth("0");
		if (newScore) {
			pOneScoreWidth = fM.stringWidth(String.valueOf(playerOne.score));
			pTwoScoreWidth = fM.stringWidth(String.valueOf(playerTwo.score));
			newScore = false;
		}
		g.drawString(String.valueOf(playerOne.score), (5 * width - pOneScoreWidth) / 8, fM.getHeight() + 40);
		g.drawString(String.valueOf(playerTwo.score), (3 * width - 6 * pTwoScoreWidth) / 8, fM.getHeight() + 40);
	}

	private void drawWinnerMessage(Graphics g) {
		if (playerOne.score < winScore && playerTwo.score < winScore) {
			return;
		} else {
			g.setColor(Color.white);
			g.setFont(pressStart60p);
			FontMetrics fM = g.getFontMetrics();
			int textWidth = fM.stringWidth("WINNER");
			if (playerOne.score == winScore) {
				g.drawString("WINNER", (3 * width - 2 * textWidth) / 4, (height + fM.getHeight()) / 2);
			} else if (playerTwo.score == winScore) {
				g.drawString("WINNER", (width - 2 * textWidth) / 4, (height + fM.getHeight()) / 2);
			}
		}
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

	public float[] getRandomVelocity(float xVelocity, boolean randomXVelocity) {
		float[] velocity = new float[2];

		float yVelocity = (float) (Math.random() * Math.tan(Math.toRadians(ball.MAX_ANGLE))) * xVelocity;
		if (Math.random() < 0.5) {
			yVelocity = -yVelocity;
		}
		if (randomXVelocity) {
			if (Math.random() < 0.5) {
				xVelocity = -xVelocity;
			}
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
