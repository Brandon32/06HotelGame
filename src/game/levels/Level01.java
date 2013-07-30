package game.levels;

import engine.ImageUtil;
import engine.events.GameEvent;
import engine.events.GameEvent.GameEventType;
import engine.events.GameEventDispatcher;
import game.LevelSuper;
import game.sprite.Mech;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Level01 extends LevelSuper {
	private Mech myMech;
	private BufferedImage backgroundImage;

	public Level01() {
		super();
		myMech = new Mech();

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
			g.drawImage(backgroundImage, null, 0, 0);
		super.draw(g);
	}
}
