package game.menu;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import engine.sprite.UI;

public class HelpSprite extends MainMenuSprite implements UI {

	private boolean done;
	private int selected;
	private Image backgroundImage;
	

	private String loadGame;
	private String newGame;
	private String help;
	private String settings;
	private String exit;
	
	public HelpSprite(){
		
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
		return (selected.getValue());
	}

}
