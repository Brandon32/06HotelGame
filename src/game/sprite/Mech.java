package game.sprite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import engine.GameDisplay;
import engine.GameEngine;
import engine.sprites.interfaces.ColisionInterface;
import engine.sprites.interfaces.ImageInterface;
import engine.sprites.interfaces.UIInterface;
import engine.sprites.tools.Position;

public class Mech implements ColisionInterface, UIInterface {

	private Font f1;
	private Dimension displayBounds;

	private final double notch = 0.1;

	boolean rtArm;
	boolean ltArm;
	boolean rtPowerArm;
	boolean ltPowerArm;
	boolean rtGrab;
	boolean ltGrab;
	private boolean ltDown;
	private boolean ltUp;
	private boolean rtDown;
	private boolean rtUp;

	private double maxLegSpeed = 1;
	private double minLegSpeed = -1;
	double angle = 0;
	private Position position = new Position(100,100);
	private int width = 20;
	private int height = 20;
	private double xVel;
	private double yVel;
	private double rightLegSpeed;
	private double leftLegSpeed;
	private double speed;

	private long currentTime = GameEngine.getCurrentTime();
	private long rtAlarm = GameEngine.getCurrentTime();
	private long ltAlarm = rtAlarm;
	private Rectangle boundingBox = new Rectangle();

	public Mech() {
		
		displayBounds = GameDisplay.getBounds();
		f1 = new Font("Times New Roman", Font.BOLD, (int) 12);
		
		position.set(displayBounds.width / 2, displayBounds.height / 2);
		
		boundingBox.setLocation(position.getX(), position.getY());
		
	}

	@Override
	public void update() {
		// TODO: calculate angle
		currentTime = GameEngine.getCurrentTime();
		// TODO: calculate speed
		changeSpeed();
		recalcAngle();
		calcMovement();
		checkFrameCollision();
	}

	@Override
	public void draw(Graphics2D g) {

		g.rotate(-angle, position.getX(), position.getY());// mech
		g.setColor(Color.BLUE);
		g.fillRect(position.getX()- width / 2, position.getY() - height / 2, width, height);

		// top left
		if (leftLegSpeed > 0)
			g.setColor(new Color((int) Math.abs((int) (leftLegSpeed * 231)),
					155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(position.getX() - width / 2, position.getY() - height / 2, 10, 10);

		// botom left
		if (leftLegSpeed <= 0)
			g.setColor(new Color((int) Math.abs((int) (leftLegSpeed * 231)),
					155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(position.getX() - width / 2, position.getY(), 10, 10);

		// top right
		if (rightLegSpeed > 0)
			g.setColor(new Color((int) Math.abs((int) (rightLegSpeed * 231)),
					155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(position.getX(), position.getY() - height / 2, 10, 10);

		// botom right
		if (rightLegSpeed <= 0)
			g.setColor(new Color((int) Math.abs((int) (rightLegSpeed * 231)),
					155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(position.getX(), position.getY(), 10, 10);

		g.rotate(angle, position.getX(), position.getY());// end mech

		g.setColor(Color.BLACK);
		g.drawLine(position.getX(), position.getY(), position.getX() - (int) (xVel * 20), position.getY() - (int) (yVel * 20)); // movement
																		// direction
		g.setColor(Color.red);
		g.drawLine(position.getX(), position.getY(), (int) (position.getX() - Math.sin(angle) * 10),
				(int) (position.getY() - Math.cos(angle) * 10)); // facing direction

		// debug
		{
			g.setFont(f1);
			g.setColor(Color.RED);
			g.drawRect(position.getX() + 99, position.getY() + 89, 200, 200);
			g.drawLine(position.getX() + width/2, position.getY() + height/2, position.getX() + 99, position.getY() + 89);
			g.drawString(
					"Right Speed: " + String.format("%.3f", rightLegSpeed * 10),
					position.getX() + 100, position.getY() + 100);
			g.drawString(
					"Left Speed:  " + String.format("%.3f", leftLegSpeed * 10),
					position.getX() + 100, position.getY() + 110);
			g.drawString("Angle: " + (int) Math.toDegrees(angle) + " Degrees",
					position.getX() + 100, position.getY() + 120);
			g.drawString("Speed: " + Math.round(speed * 10), position.getX() + 100, position.getY() + 130);
			g.drawString("X Velocity: " + String.format("%.3f", xVel * 10),
					position.getX() + 100, position.getY() + 140);
			g.drawString("Y Velocity: " + String.format("%.3f", yVel * 10),
					position.getX() + 100, position.getY() + 150);
		}
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			if (ke.getKeyCode() == KeyEvent.VK_D || ke.getKeyCode() == KeyEvent.VK_W) // Forward right
				ltUp = true;
			if (ke.getKeyCode() == KeyEvent.VK_D || ke.getKeyCode() == KeyEvent.VK_S) // Reverse right
				ltDown = true;
			if (ke.getKeyCode() == KeyEvent.VK_A || ke.getKeyCode() == KeyEvent.VK_W) // Forward left
				rtUp = true;
			if (ke.getKeyCode() == KeyEvent.VK_A || ke.getKeyCode() == KeyEvent.VK_S) // Reverse left
				rtDown = true;
		}
		if (ke.getID() == KeyEvent.KEY_RELEASED) {
			if (ke.getKeyCode() == KeyEvent.VK_D ||  ke.getKeyCode() == KeyEvent.VK_W) // Forward right
				ltUp = false;
			if (ke.getKeyCode() == KeyEvent.VK_D ||  ke.getKeyCode() == KeyEvent.VK_S) // Reverse right
				ltDown = false;
			if (ke.getKeyCode() == KeyEvent.VK_A ||  ke.getKeyCode() == KeyEvent.VK_W) // Forward left
				rtUp = false;
			if (ke.getKeyCode() == KeyEvent.VK_A ||  ke.getKeyCode() == KeyEvent.VK_S) // Reverse left
				rtDown = false;
		}
	}

	@Override
	public void mouseEvent(MouseEvent me) {

	}

	private void changeSpeed() {
		if (rtUp) {
			if (rightLegSpeed < maxLegSpeed)
				if (rtAlarm < currentTime) {
					rightLegSpeed += notch;
					rtAlarm = currentTime + 100000000;
				}
		}
		if (rtDown) {
			if (rightLegSpeed > minLegSpeed)
				if (rtAlarm < currentTime) {
					rightLegSpeed -= notch;
					rtAlarm = currentTime + 100000000;
				}
		}
		if (ltUp) {
			if (leftLegSpeed < maxLegSpeed)
				if (ltAlarm < currentTime) {
					leftLegSpeed += notch;
					ltAlarm = currentTime + 100000000;
				}
		}
		if (ltDown) {
			if (leftLegSpeed > minLegSpeed)
				if (ltAlarm < currentTime) {
					leftLegSpeed -= notch;
					ltAlarm = currentTime + 100000000;
				}
		}
		// slow down lt
		if (!ltDown && !ltUp) {
			if (ltAlarm < currentTime) {
				if (leftLegSpeed >= 0.1)
					leftLegSpeed -= notch;
				if (leftLegSpeed <= -0.1)
					leftLegSpeed += notch;
				ltAlarm = currentTime + 100000000;
			}
		}
		// slow down rt
		if (!rtDown && !rtUp) {
			if (rtAlarm < currentTime) {
				if (rightLegSpeed > 0)
					rightLegSpeed -= notch;
				if (rightLegSpeed < 0)
					rightLegSpeed += notch;
				rtAlarm = currentTime + 100000000;
			}
		}
	}

	private void recalcAngle() {

		speed = Math.abs(rightLegSpeed + leftLegSpeed);

		angle += (Math.atan(rightLegSpeed - leftLegSpeed))
				* (Math.abs(rightLegSpeed) + Math.abs(leftLegSpeed)) / 100;
		if (rightLegSpeed + leftLegSpeed < 0) {
			speed *= -1;
		}

		// check for degrees
		if (angle > Math.PI * 2)
			angle = 0;
		if (angle < -Math.PI * 2)
			angle = 0;
		xVel = Math.sin(angle) * speed;
		yVel = Math.cos(angle) * speed;
	}

	private void calcMovement() {
		position.moveX(-xVel);
		position.moveY(-yVel);
	}

	private void checkFrameCollision() {
		/* Check for right collision */

		if ((position.getX() + width / 2) >= displayBounds.width) {
			position.setX(displayBounds.width - width / 2 - 1);
		}

		/* Check for left collision */

		if (position.getX() <= 0) {
			position.setX(1);
		}

		/* Check for bottom collision */

		if ((position.getY() + height / 2) >= displayBounds.height) {
			position.setY(displayBounds.height - height / 2 - 1);
		}

		/* Check for top collision */

		if (position.getY() <= 0) {
			position.setY(1);
		}
	}

	@Override
	public void checkCollision(ColisionInterface obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		return ( this.boundingBox.intersects( boundingBox ) ? this.boundingBox.getBounds() : null );
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
