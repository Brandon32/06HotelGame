package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.GameDisplay;
import engine.ImageUtil;

public class Startup extends Level {

	private BufferedImage backgroundImage;
	private Dimension displayBounds;
	private Font f1;

	public Startup() {
		super();
		displayBounds = GameDisplay.getBounds();
		f1 = new Font("Times New Roman", Font.PLAIN, (int) 14);
		try {
			backgroundImage = ImageUtil.loadBufferedImage(this,
					"/Backgrounds/Startup.png");
		} catch (IOException e) {
			System.out.println("Startup Image Not Loaded");
		}
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, displayBounds.width,
					displayBounds.height, null);
		}
		g.setFont(f1);
		g.setColor(Color.RED);
		g.drawString("Cyber Penguin (c) 2013", displayBounds.width / 2,
				displayBounds.height - 20);
	}

}
