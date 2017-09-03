import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by victo on 02-08-2017.
 */

public class Keyboard implements KeyListener {
	public boolean[] keys = new boolean[160];
	public boolean upKey, downKey, wKey, sKey;

	public void update() {
		upKey = keys[KeyEvent.VK_K];
		downKey = keys[KeyEvent.VK_M];

		wKey = keys[KeyEvent.VK_A];
		sKey = keys[KeyEvent.VK_Z];
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

	public void disableAccentMenu() {
		String command = "defaults write -g ApplePressAndHoldEnabled -bool false\n";
		System.out.println(executeCommand(command));
	}

	public void enableAccentMenu() {
		String command = "defaults write -g ApplePressAndHoldEnabled -bool true\n";
		executeCommand(command);
	}

	private String executeCommand(String command) {
		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
}
