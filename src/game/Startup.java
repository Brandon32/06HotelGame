package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.GameDisplay;
import engine.ImageUtil;
import engine.sprite.Level;

public class Startup implements Level {
	
	private BufferedImage backgroundImage;
	private Dimension displayBounds;
	private Font f1;
	
	public Startup() {
		displayBounds = GameDisplay.getBounds();
		f1 = new Font("Times New Roman", Font.PLAIN, (int) 14);
		try {
			backgroundImage = ImageUtil.loadBufferedImage( this, "/Backgrounds/Startup.png" );
		} catch (IOException e) {
			System.out.println("Startup Image Not Loaded");
		}
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		g.setFont(f1);
		g.setColor(Color.RED);
		g.drawString("Cyber Penguin (c) 2013", displayBounds.width / 2, displayBounds.height - 20);
    	g.drawImage(backgroundImage, 0, 0, displayBounds.width, displayBounds.height, null);
	}

}
