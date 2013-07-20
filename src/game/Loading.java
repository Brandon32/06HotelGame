package game;

import engine.ImageUtil;
import game.levels.LevelSuper;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Loading extends LevelSuper{
	
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
