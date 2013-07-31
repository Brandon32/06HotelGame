package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import engine.interfaces.ColisionInterface;
import engine.interfaces.ImageInterface;
import engine.interfaces.LevelInterface;
import engine.interfaces.UIInterface;

public abstract class LevelSuper implements LevelInterface {

	List<ImageInterface> drawList;
	List<ColisionInterface> colisionList;
	List<UIInterface> keyList;
	
	{
		Start.debug("New lists");
		drawList = new ArrayList<>();
		colisionList = new ArrayList<>();
		keyList = new ArrayList<>();
	}
	
	public LevelSuper() {
		//clearLists();

	}

	/**
	 * Check collisions on the Sprite objects
	 */
	@Override
	public synchronized void checkCollision() {
		synchronized (colisionList) {
			for (ColisionInterface spriteObj : colisionList) {
				for (ColisionInterface otherSprite : colisionList) {
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
	public synchronized void update() {
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
	public void draw(Graphics2D g) {
		drawSprites(g);
	}

	public synchronized void drawSprites(Graphics2D g) {
		synchronized (drawList) {
			//Start.debug("Drawing drawList " + drawList.size());
			for (ImageInterface imageObj : drawList) {
				imageObj.draw(g);
			}
		}
	}

	/**
	 * Send the keyboard event to each Sprite
	 */
	@Override
	public synchronized void keyboardEvent(KeyEvent ke) {
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
	public synchronized void mouseEvent(MouseEvent me) {
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
		Start.debug("Lists Cleared");
	}

	public synchronized void addFirst(ImageInterface ge) {
		System.out.println("Added First: " + ge.getClass());
		if (ge instanceof ColisionInterface) {
			//System.out.println("Added colisonList");
			synchronized (colisionList) {
				colisionList.add((ColisionInterface) ge);
			}
		}
		if (ge instanceof ImageInterface) {
			//System.out.println("Added drawList");
			synchronized (drawList) {
				drawList.add((ImageInterface) ge);
			}
		}
		if (ge instanceof UIInterface) {
			//System.out.println("Added keyList");
			synchronized (keyList) {
				keyList.add((UIInterface) ge);
			}
		}
	}

	public synchronized void remove(ImageInterface ge) {
		System.out.println("Removed: " + ge.getClass());
		if (ge instanceof ColisionInterface) {
			//System.out.println("Removed colisonList");
			synchronized (colisionList) {
				colisionList.remove((ColisionInterface) ge);
			}
		}
		if (ge instanceof ImageInterface) {
			//System.out.println("Removed drawList");
			synchronized (drawList) {
				drawList.remove((ImageInterface) ge);
			}
		}
		if (ge instanceof UIInterface) {
			//System.out.println("Removed keyList");
			synchronized (keyList) {
				keyList.remove((UIInterface) ge);
			}
		}
	}

	public synchronized void addLast(ImageInterface ge) {
		addFirst(ge);
		// System.out.println("Added Last: " + ge.getClass());
		// if (ge instanceof ColisionInterface) {
		// System.out.println("Added colisonList");
		// synchronized (colisionList) {
		// colisionList.addLast((ColisionInterface) ge);
		// }
		// }
		// if (ge instanceof ImageInterface) {
		// System.out.println("Added drawList");
		// synchronized (drawList) {
		// drawList.addLast((ImageInterface) ge);
		// }
		// }
		// if (ge instanceof UIInterface) {
		// System.out.println("Added keyList");
		// synchronized (keyList) {
		// keyList.addLast((UIInterface) ge);
		// }
		// }
	}
}
