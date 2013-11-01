package engine.sprites.interfaces;


public interface UIInterface extends ImageInterface {
	/**
	 * Receive a keyboard event.
	 */
	public abstract void keyboardEvent();

	/**
	 * Receive a mouse event.
	 * 
	 * @param me
	 */
	public abstract void mouseEvent();
}
