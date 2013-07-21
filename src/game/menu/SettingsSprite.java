package game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import engine.GameDisplay;
import engine.ImageUtil;
import engine.interfaces.UIInterface;

public class SettingsSprite implements UIInterface {

	private int selected;

	private boolean done;
	private Image backgroundImage;

	private static int width0;
	private static int width1;
	private static int width2;
	private static int width3;
	private static int width4;
	private static int height0;
	private static int height1;
	private static int height2;
	private static int height3;
	private static int height4;
	
	private String loadGame;
	private String newGame;
	private String help;
	private String settings;
	private String exit;

	private Dimension displayBounds;
	
	private final double MAX_SIZE = 40;
	private final double MIN_SIZE = 30;
	private Font f1;
	private Font f2;
	private Color color1 = Color.BLUE;
	private Color color2 = Color.BLACK;

	public SettingsSprite(){
		displayBounds = GameDisplay.getBounds();
		f1 = new Font("Times New Roman", Font.BOLD, (int) MIN_SIZE);
		f2 = new Font("Times New Roman", Font.BOLD, (int) MAX_SIZE);

		try {
			backgroundImage = ImageUtil.loadBufferedImage(this,"/Backgrounds/Settings.png");
		} catch (IOException e) {
			System.out.println("Settings Image Not Loaded");
		}
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, displayBounds.width,
					displayBounds.height, null);
		}

		setText(g, 0);
		width0 = ((displayBounds.width - g.getFontMetrics()
				.stringWidth(newGame)) / 2);
		g.drawString(newGame, width0, height0);
		setText(g, 1);
		width1 = ((displayBounds.width - g.getFontMetrics().stringWidth(
				loadGame)) / 2);
		g.drawString(loadGame, width1, height1);
		setText(g, 2);
		width2 = ((displayBounds.width - g.getFontMetrics().stringWidth(help)) / 2);
		g.drawString(help, width2, height2);
		setText(g, 3);
		width3 = ((displayBounds.width - g.getFontMetrics().stringWidth(
				settings)) / 2);
		g.drawString(settings, width3, height3);
		setText(g, 4);
		width4 = ((displayBounds.width - g.getFontMetrics().stringWidth(exit)) / 2);
		g.drawString(exit, width4, height4);
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEvent(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	public boolean isDone() {
		return done;
	}

	public int getSelectedValue() {
		return (selected);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	void setText(Graphics2D g, int val) {
		if (selected == val) {
			g.setFont(f2);
			g.setColor(color2);
		} else {
			g.setFont(f1);
			g.setColor(color1);
		}
	}

}
