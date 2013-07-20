package game.levels;

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

	@Override
	public void checkCollision(SpriteInterface obj) {
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

	@Override
	public void update() {
		synchronized (drawList) {
			for (ImageInterface imageObj : drawList) {
				imageObj.update();
			}
		}
	}

	@Override
	public void draw(Graphics2D offscreenGraphics) {
		synchronized (drawList) {
			for (ImageInterface imageObj : drawList) {
				imageObj.draw(offscreenGraphics);
			}
		}
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		synchronized (keyList) {
			for (UIInterface spriteObj : keyList) {
				spriteObj.keyboardEvent(ke);
			}
		}
	}

	@Override
	public void mouseEvent(MouseEvent me) {
		synchronized (keyList) {
			for (UIInterface spriteObj : keyList) {
				spriteObj.mouseEvent(me);
			}
		}
	}

}
