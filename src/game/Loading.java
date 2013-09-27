package game;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.GameDisplay;
import engine.ImageUtil;

public class Loading extends Level {

	private BufferedImage backgroundImage;
	private Dimension displayBounds;

	public Loading() {
		super();
		displayBounds = GameDisplay.getBounds();
		try {
			backgroundImage = ImageUtil.loadBufferedImage(this,
					"/Backgrounds/Loading.png");
		} catch (IOException e) {
			System.out.println("Loading Image Not Loaded");
		}
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		g.drawImage(backgroundImage, 0, 0, displayBounds.width,
				displayBounds.height, null);
	}

}
