import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by victo on 02-08-2017.
 */

public class Keyboard implements KeyListener {
	public boolean[] keys = new boolean[160];
	public boolean up, down;

	public void update() {
		up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
	}

	public boolean[] getKeys() {
		return new boolean[] {up, down};
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
	}
}
