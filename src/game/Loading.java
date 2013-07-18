package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.ImageUtil;
import engine.sprite.Level;

public class Loading implements Level{
	
	private BufferedImage backgroundImage;

	public Loading()
	{
		try {
			backgroundImage = ImageUtil.loadBufferedImage( this, "/Backgrounds/Loading.png" );
		} catch (IOException e) {
			System.out.println("Loading Image Not Loaded");
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
    	g.drawImage(backgroundImage, null, 0, 0);		
	}

}
