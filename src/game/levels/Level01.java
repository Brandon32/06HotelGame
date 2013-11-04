package game.levels;

import engine.GameDisplay;
import engine.ImageUtil;
import engine.events.GameEvent;
import engine.events.GameEvent.GameEventType;
import engine.events.GameEventDispatcher;
import game.Level;
import game.sprite.Hotel;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Level01 extends Level {
	private Hotel myMech;
	private BufferedImage backgroundImage;
	private Dimension displayBounds;

	public Level01() {
		super();
		displayBounds = GameDisplay.getBounds();
		myMech = new Hotel();

		try {
			backgroundImage = ImageUtil.loadBufferedImage(this,
					"/Backgrounds/Level1.png");
		} catch (IOException e) {
			System.out.println("Level1 Image Not Loaded");
		}
		GameEventDispatcher.dispatchEvent(new GameEvent(this,
				GameEventType.AddFirst, myMech));
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(Graphics2D g) {
		if (backgroundImage != null)
			g.drawImage(backgroundImage, 0, 0, displayBounds.width,
					displayBounds.height, null);
		super.draw(g);
	}
}
