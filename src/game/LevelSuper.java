package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import engine.interfaces.ImageInterface;
import engine.interfaces.LevelInterface;
import engine.interfaces.SpriteInterface;
import engine.interfaces.UIInterface;

public abstract class LevelSuper implements LevelInterface {

	private LinkedList<ImageInterface> drawList;
	private LinkedList<SpriteInterface> colisionList;
	private LinkedList<UIInterface> keyList;

	public LevelSuper() {
		drawList = new LinkedList<ImageInterface>();
		colisionList = new LinkedList<SpriteInterface>();
		keyList = new LinkedList<UIInterface>();

	}

	/**
	 * Check collisions on the Sprite objects
	 */
	@Override
	public void checkCollision() {
		synchronized (colisionList) {
			for (SpriteInterface spriteObj : colisionList) {
				for (SpriteInterface otherSprite : colisionList) {
					if (!otherSprite.equals(spriteObj)) {
						spriteObj.checkCollision(otherSprite);
					}
				}
			}
		}
	}

	/**
	 * Update the Sprite objects and Level
	 */
	@Override
	public void update() {
		synchronized (drawList) {
			for (ImageInterface imageObj : drawList) {
				imageObj.update();
			}
		}
	}

	/**
	 * Draw the Image objects
	 */
	@Override
	public void draw(Graphics2D offscreenGraphics) {
		synchronized (drawList) {
			for (ImageInterface imageObj : drawList) {
				imageObj.draw(offscreenGraphics);
			}
		}
	}

	/**
	 * Send the keyboard event to each Sprite
	 */
	@Override
	public void keyboardEvent(KeyEvent ke) {
		synchronized (keyList) {
			for (UIInterface spriteObj : keyList) {
				spriteObj.keyboardEvent(ke);
			}
		}
	}

	/**
	 * Send the mouse event to each Sprite
	 */
	@Override
	public void mouseEvent(MouseEvent me) {
		synchronized (keyList) {
			for (UIInterface spriteObj : keyList) {
				spriteObj.mouseEvent(me);
			}
		}
	}

	public synchronized void clearLists() {
		synchronized (drawList) {
			drawList.clear();
		}
		synchronized (colisionList) {
			colisionList.clear();
		}
		synchronized (keyList) {
			keyList.clear();
		}
	}

	public synchronized void addFirst(ImageInterface ge) {
		System.out.println("Added First");
		if (ge instanceof SpriteInterface)
			synchronized (colisionList) {
				colisionList.addFirst((SpriteInterface) ge);
			}
		if (ge instanceof ImageInterface)
			synchronized (drawList) {
				drawList.addFirst((ImageInterface) ge);
			}
		if (ge instanceof UIInterface)
			synchronized (keyList) {
				keyList.addFirst((UIInterface) ge);
			}
	}

	public synchronized void remove(ImageInterface ge) {
		System.out.println("Removed");
		if (ge instanceof SpriteInterface)
			synchronized (colisionList) {
				colisionList.remove((SpriteInterface) ge);
			}
		if (ge instanceof ImageInterface)
			synchronized (drawList) {
				drawList.remove((ImageInterface) ge);
			}
		if (ge instanceof UIInterface)
			synchronized (keyList) {
				keyList.remove((UIInterface) ge);
			}
	}

	public synchronized void addLast(ImageInterface ge) {
		System.out.println("Added Last");
		if (ge instanceof SpriteInterface)
			synchronized (colisionList) {
				colisionList.addLast((SpriteInterface) ge);
			}
		if (ge instanceof ImageInterface)
			synchronized (drawList) {
				drawList.addLast((ImageInterface) ge);
			}
		if (ge instanceof UIInterface)
			synchronized (keyList) {
				keyList.addLast((UIInterface) ge);
			}
	}
}
