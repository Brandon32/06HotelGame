package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.ImageUtil;
import engine.sprite.UI;

public class Loading implements UI {
	
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

	@Override
	public void keyboardEvent(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEvent(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

}
