package engine;

import java.awt.event.KeyEvent;

public class Keyboard {

	private static boolean keyState;
	private static boolean[] Key = new boolean[KeyEvent.KEY_LAST];

	public static void keyboardEvent(KeyEvent ke) {

		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			keyState = true;
		}
		if (ke.getID() == KeyEvent.KEY_RELEASED) {
			keyState = false;
		}
		Key[ke.getKeyCode()] = keyState;
	}

	public static boolean isActive(int keyCode) {
		return (Key[keyCode]);
	}

	public static boolean takeActive(int keyCode) {
		if(Key[keyCode])
			Key[keyCode] = false;
		return (Key[keyCode]);
	}
}
