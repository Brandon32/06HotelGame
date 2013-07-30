package game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.GameDisplay;
import engine.ImageUtil;
import engine.interfaces.UIInterface;

public class HelpSprite implements UIInterface {

	private final int MAX_SELECTIONS;
	private final double MAX_SIZE = 40;

	private int selected;

	Dimension displayBounds;

	private String[] element;
	private int[] length;
	private int[] height;

	private Color color2 = Color.BLACK;
	private Font f2;

	private BufferedImage backgroundImage;

	boolean down;
	boolean up;
	int mouseX;
	int mouseY;
	boolean done;

	public HelpSprite() {
		MAX_SELECTIONS = 5;
		element = new String[MAX_SELECTIONS];
		length = new int[MAX_SELECTIONS];
		height = new int[MAX_SELECTIONS];

		element[0] = "Continue Game";
		element[1] = "New Game";
		element[2] = "Help";
		element[3] = "Settings";
		element[4] = "Exit Game";

		displayBounds = GameDisplay.getBounds();

		down = false;
		up = false;
		f2 = new Font("Times New Roman", Font.BOLD, (int) MAX_SIZE);

		try {
			backgroundImage = ImageUtil.loadBufferedImage(this,
					"/Backgrounds/Menu.png");
		} catch (IOException e) {
			System.out.println("Help Image Not Loaded");
		}
	}

	@Override
	public void draw(Graphics2D g) {
		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setRenderingHints(rh);

		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, displayBounds.width,
					displayBounds.height, null);
		}
		g.setFont(f2);
		g.setColor(color2);

		for (int i = 0; i < MAX_SELECTIONS; i++) {
			length[i] = ((displayBounds.width - g.getFontMetrics().stringWidth(
					element[i])) / 2);
			height[i] = (int) (((i / (float) element.length) * (0.9) * displayBounds.height) + (displayBounds.height * 0.075));
			g.drawString(element[i], length[i], height[i]);
		}
	}

	@Override
	public void update() {
		// move the selected value to the next one
		if (down == true) {
			if (selected < MAX_SELECTIONS) {
				selected++;
			} else {
				selected = 0;
			}
			down = false;
		}
		if (up == true) {
			if (selected > 0) {
				selected--;
			} else {
				selected = MAX_SELECTIONS;
			}
			up = false;
		}
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			if (ke.getKeyCode() == KeyEvent.VK_W
					|| ke.getKeyCode() == KeyEvent.VK_UP) // UP
			{
				up = true;
			}
			if (ke.getKeyCode() == KeyEvent.VK_S
					|| ke.getKeyCode() == KeyEvent.VK_DOWN) // DOWN
			{
				down = true;
			}
			if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
				done = true;
			}
		}
	}

	@Override
	public void mouseEvent(MouseEvent me) {
		if (mouseX != me.getX()) {
			mouseX = me.getX();
			mouseSelect();
		}
		if (mouseY != me.getY()) {
			mouseY = me.getY();
			mouseSelect();
		}
		if (me.getButton() == MouseEvent.BUTTON1) {
			done = true;
		}
	}

	public boolean isDone() {
		return done;
	}

	public int getSelectedValue() {
		return (selected);
	}

	private void mouseSelect() {

		for (int i = 0; i < element.length; i++) {
			if ((mouseY > height[i] - MAX_SIZE)
					&& (mouseY < height[i] + MAX_SIZE))
				selected = i;
		}
	}

}
