package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.ImageUtil;

public class Loading extends LevelSuper {

	private BufferedImage backgroundImage;

	public Loading() {
		super();
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
		if (backgroundImage != null)
			g.drawImage(backgroundImage, null, 0, 0);
	}

}
