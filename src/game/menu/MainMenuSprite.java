package game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import engine.GameDisplay;
import engine.Input;
import engine.sprites.interfaces.ImageInterface;
import engine.sprites.interfaces.UIInterface;

public class MainMenuSprite implements UIInterface {
	private final int MAX_SELECTIONS = 5;
	private final double MAX_SIZE = 40;
	private final double MIN_SIZE = 30;

	private int selected;

	Dimension displayBounds;

	private String[] element;
	private int[] length;
	private int[] height;

	private Color color1 = Color.BLUE;
	private Color color2 = Color.BLACK;
	private Font f1;
	private Font f2;

	boolean down;
	boolean up;
	int mouseX;
	int mouseY;
	boolean done;

	public MainMenuSprite() {
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
		f1 = new Font("Times New Roman", Font.BOLD, (int) MIN_SIZE);
		f2 = new Font("Times New Roman", Font.BOLD, (int) MAX_SIZE);

	}

	@Override
	public void draw(Graphics2D g) {
		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setRenderingHints(rh);

		for (int i = 0; i < MAX_SELECTIONS; i++) {
			setText(g, i);
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
			if (selected < MAX_SELECTIONS - 1) {
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
				selected = MAX_SELECTIONS - 1;
			}
			up = false;
		}
	}
	
	private void mouseSelect() {

		for (int i = 0; i < MAX_SELECTIONS; i++) {
			if ((mouseY > height[i] - MAX_SIZE)	&& (mouseY < height[i] + MAX_SIZE))
				selected = i;
		}
	}
	
	@Override
	public void keyboardEvent() {
		if (Input.isActive(KeyEvent.VK_W) || Input.isActive(KeyEvent.VK_UP)) // UP
		{
			up = true;
		}
		if (Input.isActive(KeyEvent.VK_S) || Input.isActive(KeyEvent.VK_DOWN)) // DOWN
		{
			down = true;
		}
		if (Input.isActive(KeyEvent.VK_ENTER)) {
			done = true;
		}
	}

	@Override
	public void mouseEvent() {
		if (mouseX != Input.getX()) {
			mouseX = Input.getX();
			mouseSelect();
		}
		if (mouseY != Input.getY()) {
			mouseY = Input.getY();
			mouseSelect();
		}
		if (Input.getLeftClick()) {
			done = true;
		}
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getSelectedValue() {
		return (selected);
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

	@Override
	public int compareTo(ImageInterface compareImage) {
		return this.getLayer() - ((ImageInterface) compareImage).getLayer();
	}

	@Override
	public int getLayer() {
		return 100;
	}
}
