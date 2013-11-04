package game.sprite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import engine.GameDisplay;
import engine.GameEngine;
import engine.Input;
import engine.sprites.interfaces.ColisionInterface;
import engine.sprites.interfaces.ImageInterface;
import engine.sprites.interfaces.UIInterface;
import engine.sprites.tools.Position;

public class Hotel implements ColisionInterface, UIInterface {

	private Font f1;
	private Dimension displayBounds;

	double angle = 0;
	private Position position = new Position(100, 100);
	private double xVel;
	private double yVel;
	private double rightLegSpeed;
	private double leftLegSpeed;
	private double speed;

	private long currentTime = GameEngine.getCurrentTime();
	private long rtAlarm = GameEngine.getCurrentTime();
	private long ltAlarm = rtAlarm;
	private Rectangle boundingBox = new Rectangle();

	private int mouseX;
	private int mouseY;
	private Floor[] levels;
	private int maxfloors = 5;
	private int[] roof =  {0,0,0,0};

	public Hotel() {

		displayBounds = GameDisplay.getBounds();
		f1 = new Font("Times New Roman", Font.BOLD, (int) 12);

		position.set(displayBounds.width / 2, displayBounds.height * 3 / 4);

		boundingBox.setLocation(position.getX(), position.getY());

		levels = new Floor[maxfloors ];
		for (int i = 0; i < levels.length; i++) {
			levels[i] = new Floor(i);
		}
	}

	@Override
	public void update() {
		currentTime = GameEngine.getCurrentTime();
		angle = 0;
		levels[maxfloors-1].update(0,roof);
		for (int i = maxfloors - 2; i >= 0; i--) {
			levels[i].update(0, levels[i+1].getWeight());
		}
		for (Floor l : levels) {
			l.update(0, l.getWeight());
			angle += l.getAngle();
		}
		// check for degrees
		if (angle > Math.PI * 2)
			angle = 0;
		if (angle < -Math.PI * 2)
			angle = 0;
	}

	@Override
	public void draw(Graphics2D g) {
		for (Floor l : levels) {
			l.draw(g);
		}

		// movement direction
		g.setColor(Color.red);
		g.drawLine(position.getX(), position.getY(),
				(int) (position.getX() - Math.sin(angle) * 200),
				(int) (position.getY() - Math.cos(angle) * 200)); // facing
																	// direction

		g.setColor(Color.BLACK);

		// mouse
		// g.drawLine(position.getX(), position.getY(), mouseX, mouseY);

//		if (DebugInfo.isDebug()) {
//			g.setFont(f1);
//			g.setColor(Color.RED);
//			g.drawRect(position.getX() + 400, position.getY() - 189, 200, 200);
//			g.drawString(
//					"Right Speed: " + String.format("%.3f", rightLegSpeed * 10),
//					position.getX() + 400, position.getY() - 100);
//			g.drawString(
//					"Left Speed:  " + String.format("%.3f", leftLegSpeed * 10),
//					position.getX() + 400, position.getY() - 110);
//			g.drawString("Angle: " + (int) Math.toDegrees(angle) + " Degrees",
//					position.getX() + 400, position.getY() - 120);
//			g.drawString("Speed: " + Math.round(speed * 10),
//					position.getX() + 400, position.getY() - 130);
//			g.drawString("X Velocity: " + String.format("%.3f", xVel * 10),
//					position.getX() + 400, position.getY() - 140);
//			g.drawString("Y Velocity: " + String.format("%.3f", yVel * 10),
//					position.getX() + 400, position.getY() - 150);
//		}
	}

	@Override
	public void keyboardEvent() {
		if(Input.isActive(KeyEvent.VK_W)){
			for (Floor l : levels) {
				l.addWeight(3, 5);
			}
		}
		if(Input.isActive(KeyEvent.VK_S)){
			for (Floor l : levels) {
				l.addWeight(3, -5);
			}
		}
	}

	@Override
	public void mouseEvent() {
		mouseX = Input.getX();
		mouseY = Input.getY();
	}

	@Override
	public void checkCollision(ColisionInterface obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		return (this.boundingBox.intersects(boundingBox) ? this.boundingBox
				.getBounds() : null);
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
