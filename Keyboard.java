import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by victo on 02-08-2017.
 */

public class Keyboard implements KeyListener {
	public boolean[] keys = new boolean[160];
	public boolean upKey, downKey, wKey, sKey;

	public void update() {
		upKey = keys[KeyEvent.VK_UP];
		downKey = keys[KeyEvent.VK_DOWN];

		wKey = keys[KeyEvent.VK_W];
		sKey = keys[KeyEvent.VK_S];
	}

	public boolean[] getKeys() {
		return new boolean[] {upKey, downKey, wKey, sKey};
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
