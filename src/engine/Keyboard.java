package engine;

import java.awt.event.KeyEvent;

import engine.interfaces.UserInputInterface;

public class Keyboard implements UserInputInterface {

	private static boolean keyState;
	private static boolean[] Key = new boolean[KeyEvent.KEY_LAST];

	public void keyboardEvent(KeyEvent ke) {

		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			keyState = true;
		}
		if (ke.getID() == KeyEvent.KEY_RELEASED) {
			keyState = false;
		}
		Key[ke.getKeyCode()] = keyState;
	}

	public boolean isActive(int keyCode) {
		return (Key[keyCode]);
	}

	@Override
	public void takeActive(int keyCode) {
		Key[keyCode] = false;
	}
}
