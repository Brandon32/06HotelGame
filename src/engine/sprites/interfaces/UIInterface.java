package engine.sprites.interfaces;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface UIInterface extends ImageInterface {
	/**
	 * Receive a keyboard event.
	 * 
	 * @param ke
	 */
	public abstract void keyboardEvent(KeyEvent ke);

	/**
	 * Receive a mouse event.
	 * 
	 * @param me
	 */
	public abstract void mouseEvent(MouseEvent me);
}
