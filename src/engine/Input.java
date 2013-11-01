package engine;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Input {

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
	
	
	private static int mouseX;
	private static int mouseY;
	private static boolean mousePressed;
	
	public static void mouseEvent(MouseEvent me){
		mouseX = me.getX();
		mouseY = me.getY();
		
		if(me.getButton() == MouseEvent.BUTTON1){
			if(me.getID() == MouseEvent.MOUSE_PRESSED){
				mousePressed = true;
			}
			if(me.getID() == MouseEvent.MOUSE_RELEASED){
				mousePressed = false;
			}
		}
	}
	/**
	 * @return the mouseX
	 */
	public static int getX() {
		return mouseX;
	}

	/**
	 * @param mouseX the mouseX to set
	 */
	public static void setX(int mouseX) {
		Input.mouseX = mouseX;
	}

	/**
	 * @return the mouseY
	 */
	public static int getY() {
		return mouseY;
	}

	/**
	 * @param mouseY the mouseY to set
	 */
	public static void setY(int mouseY) {
		Input.mouseY = mouseY;
	}
	
	/**
	 * @return the mousePressed
	 */
	public static boolean getLeftClick() {
		return mousePressed;
	}

	/**
	 * @param mousePressed the mousePressed to set
	 */
	public static void setMousePressed(boolean mousePressed) {
		Input.mousePressed = mousePressed;
	}
	
}
