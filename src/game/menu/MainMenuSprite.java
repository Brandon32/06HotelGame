package game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.GameDisplay;
import engine.ImageUtil;
import engine.sprite.UI;

public class MainMenuSprite implements UI {
	private final int MAX_SELECTIONS = 4;
	private final double MAX_SIZE = 40;
	private final double MIN_SIZE = 30;

	private enum Selected {
		NEW(0), LOAD(1), HELP(2), SETTINGS(3), EXIT(4);

		private int value;

		private Selected(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public Selected getNext() {
			return values()[(ordinal() + 1) % values().length];
			// return this.ordinal() < Selected.values().length - 1 ?
			// Selected.values()[ this.ordinal() + 1 ] : null;
		}

		public Selected getPrev() {
			return values()[(this.ordinal() + values().length - 1)
					% values().length];
		}
	}

	Dimension displayBounds;
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

	private String[] element;

	private Selected selected = Selected.NEW;
	private Color color1 = Color.BLUE;
	private Color color2 = Color.BLACK;
	private Font f1;
	private Font f2;

	private BufferedImage backgroundImage;

	boolean down;
	boolean up;
	int mouseX;
	int mouseY;
	boolean done;

	public MainMenuSprite() {
		element = new String[6];
		element[0] = "Continue Game";
		element[1] = "New Game";
		element[2] = "Help";
		element[3] = "Settings";
		element[4] = "Exit Game";

		displayBounds = GameDisplay.getBounds();

		down = false;
		up = false;
		f1 = new Font("Times New Roman", Font.BOLD, (int) MIN_SIZE);
		f2 = new Font("Times New Roman", Font.BOLD, (int) MAX_SIZE);

		try {
			backgroundImage = ImageUtil.loadBufferedImage(this,
					"/Backgrounds/Menu.png");
		} catch (IOException e) {
			System.out.println("Menu Image Not Loaded");
		}

	}

	@Override
	public void draw(Graphics2D g) {
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, displayBounds.width,
					displayBounds.height, null);
		}

		for (int i = 0; i < element.length; i++) {
			setText(g, i);
			g.drawString(
					element[i],
					((displayBounds.width - g.getFontMetrics().stringWidth(element[i])) / 2),
					(int) (((i / (float) element.length) * (0.9) * displayBounds.height) + (displayBounds.height * 0.075)));

		}
	}

	@Override
	public void update() {
		// move the selected value to the next one
		if (down == true) {
			if (selected.getValue() < MAX_SELECTIONS) {
				selected = selected.getNext();
			} else {
				selected = Selected.NEW;
			}
			down = false;
		}
		if (up == true) {
			if (selected.getValue() > 0) {
				selected = selected.getPrev();
			} else {
				selected = Selected.EXIT;
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
		return (selected.getValue());
	}

	private void mouseSelect() {

		if ((mouseY > height0 - MAX_SIZE) && (mouseY < height0 + MAX_SIZE))
			selected = Selected.NEW;
		if ((mouseY > height1 - MAX_SIZE) && (mouseY < height1 + MAX_SIZE))
			selected = Selected.LOAD;
		if ((mouseY > height2 - MAX_SIZE) && (mouseY < height2 + MAX_SIZE))
			selected = Selected.HELP;
		if ((mouseY > height3 - MAX_SIZE) && (mouseY < height3 + MAX_SIZE))
			selected = Selected.SETTINGS;
		if ((mouseY > height4 - MAX_SIZE) && (mouseY < height4 + MAX_SIZE))
			selected = Selected.EXIT;

	}

	void setText(Graphics2D g, int val) {
		if (selected.getValue() == val) {
			g.setFont(f2);
			g.setColor(color2);
		} else {
			g.setFont(f1);
			g.setColor(color1);
		}
	}

}
