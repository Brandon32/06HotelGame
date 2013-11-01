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
import engine.tools.DebugInfo;

public class Mech implements ColisionInterface, UIInterface {

	private Font f1;
	private Dimension displayBounds;

	private final double notch = 0.1;
	private final double maxLegSpeed = 1;
	private final double minLegSpeed = -1;
	private final double scale = (1);

	boolean rtArm;
	boolean ltArm;
	boolean rtPowerArm;
	boolean ltPowerArm;
	boolean rtGrab;
	boolean ltGrab;
	private boolean ltBck;
	private boolean ltFwd;
	private boolean rtBck;
	private boolean rtFwd;

	double angle = 0;
	private Position position = new Position(100, 100);
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

	private int mouseX;
	private int mouseY;

	public Mech() {

		displayBounds = GameDisplay.getBounds();
		f1 = new Font("Times New Roman", Font.BOLD, (int) 12);

		position.set(displayBounds.width / 2, displayBounds.height / 2);

		boundingBox.setLocation(position.getX(), position.getY());

	}

	@Override
	public void update() {
		currentTime = GameEngine.getCurrentTime();
		changeSpeed();
		recalcAngle();
		calcMovement();
		checkFrameCollision();
	}

	@Override
	public void draw(Graphics2D g) {

		g.rotate(-angle, position.getX(), position.getY());// mech
		g.setColor(Color.BLUE);
		g.fillRect(position.getX() - width / 2, position.getY() - height / 2,
				width, height);

		// top left
		if (leftLegSpeed > 0)
			g.setColor(new Color((int) Math.abs((int) (leftLegSpeed
					/ maxLegSpeed * 231)), 155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(position.getX() - width / 2, position.getY() - height / 2,
				10, 10);

		// botom left
		if (leftLegSpeed <= 0)
			g.setColor(new Color((int) Math.abs((int) (leftLegSpeed
					/ maxLegSpeed * 231)), 155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(position.getX() - width / 2, position.getY(), 10, 10);

		// top right
		if (rightLegSpeed > 0)
			g.setColor(new Color((int) Math.abs((int) (rightLegSpeed
					/ maxLegSpeed * 231)), 155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(position.getX(), position.getY() - height / 2, 10, 10);

		// botom right
		if (rightLegSpeed <= 0)
			g.setColor(new Color((int) Math.abs((int) (rightLegSpeed
					/ maxLegSpeed * 231)), 155, 155));
		else
			g.setColor(new Color(0, 155, 155));
		g.fillOval(position.getX(), position.getY(), 10, 10);

		g.rotate(angle, position.getX(), position.getY());// end mech

		// movement direction
		g.setColor(Color.BLACK);
		g.drawLine(position.getX(), position.getY(), position.getX()
				- (int) (xVel * 20 * scale), position.getY()
				- (int) (yVel * 20 * scale));

		// mouse
		g.drawLine(position.getX(), position.getY(), mouseX, mouseY);

		g.setColor(Color.red);
		g.drawLine(position.getX(), position.getY(),
				(int) (position.getX() - Math.sin(angle) * 10),
				(int) (position.getY() - Math.cos(angle) * 10)); // facing
																	// direction

		if (DebugInfo.isDebug()) {
			g.setFont(f1);
			g.setColor(Color.RED);
			g.drawRect(position.getX() + 99, position.getY() + 89, 200, 200);
			g.drawLine(position.getX() + width / 2, position.getY() + height
					/ 2, position.getX() + 99, position.getY() + 89);
			g.drawString(
					"Right Speed: " + String.format("%.3f", rightLegSpeed * 10),
					position.getX() + 100, position.getY() + 100);
			g.drawString(
					"Left Speed:  " + String.format("%.3f", leftLegSpeed * 10),
					position.getX() + 100, position.getY() + 110);
			g.drawString("Angle: " + (int) Math.toDegrees(angle) + " Degrees",
					position.getX() + 100, position.getY() + 120);
			g.drawString("Speed: " + Math.round(speed * 10),
					position.getX() + 100, position.getY() + 130);
			g.drawString("X Velocity: " + String.format("%.3f", xVel * 10),
					position.getX() + 100, position.getY() + 140);
			g.drawString("Y Velocity: " + String.format("%.3f", yVel * 10),
					position.getX() + 100, position.getY() + 150);
		}
	}

	@Override
	public void keyboardEvent() {
		 
			ltFwd = false;
			rtFwd = false;
			ltBck = false;
			rtBck = false;
		 
		 
		// Forward
		if (Input.isActive(KeyEvent.VK_W)) {
			ltFwd = true;
			rtFwd = true;
		}
		// Backwards
		else if (Input.isActive(KeyEvent.VK_S)) {
			ltBck = true;
			rtBck = true;
		}
		else{

		}

		if (Input.isActive(KeyEvent.VK_E)) // Forward right
			ltFwd = true;
		else if (Input.isActive(KeyEvent.VK_D)) // Reverse right
			ltBck = true;

		if (Input.isActive(KeyEvent.VK_Q)) // Forward left
			rtFwd = true;
		else if (Input.isActive(KeyEvent.VK_A)) // Reverse left
			rtBck = true;
	}

	@Override
	public void mouseEvent() {
		mouseX = Input.getX();
		mouseY = Input.getY();
	}

	private void changeSpeed() {
		if (rtFwd) {
			if (rightLegSpeed < maxLegSpeed)
				if (rtAlarm < currentTime) {
					rightLegSpeed += notch;
					rtAlarm = currentTime + 100000000;
				}
		}
		if (rtBck) {
			if (rightLegSpeed > minLegSpeed)
				if (rtAlarm < currentTime) {
					rightLegSpeed -= notch;
					rtAlarm = currentTime + 100000000;
				}
		}
		if (ltFwd) {
			if (leftLegSpeed < maxLegSpeed)
				if (ltAlarm < currentTime) {
					leftLegSpeed += notch;
					ltAlarm = currentTime + 100000000;
				}
		}
		if (ltBck) {
			if (leftLegSpeed > minLegSpeed)
				if (ltAlarm < currentTime) {
					leftLegSpeed -= notch;
					ltAlarm = currentTime + 100000000;
				}
		}
		// slow down left
		if (!ltBck && !ltFwd) {
			if (ltAlarm < currentTime) {
				if (leftLegSpeed > notch)
					leftLegSpeed -= notch;
				else if (leftLegSpeed < -notch)
					leftLegSpeed += notch;
				else
					leftLegSpeed = 0;
				ltAlarm = currentTime + 100000000;
			}
		}
		// slow down right
		if (!rtBck && !rtFwd) {
			if (rtAlarm < currentTime) {
				if (rightLegSpeed > notch)
					rightLegSpeed -= notch;
				else if (rightLegSpeed < -notch)
					rightLegSpeed += notch;
				else
					rightLegSpeed = 0;
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
		position.moveX(-xVel * scale);
		position.moveY(-yVel * scale);
	}

	private void checkFrameCollision() {
		/* Check for right collision */

		if ((position.getX() + width / 2) >= displayBounds.width) {
			position.setX(displayBounds.width - width / 2 - 1);
			xVel = 0;
		}

		/* Check for left collision */

		if (position.getX() <= width / 2) {
			position.setX(width / 2 + 1);
			xVel = 0;
		}

		/* Check for bottom collision */

		if ((position.getY() + height / 2) >= displayBounds.height) {
			position.setY(displayBounds.height - height / 2 - 1);
			yVel = 0;
		}

		/* Check for top collision */

		if (position.getY() <= height / 2) {
			position.setY(height / 2 + 1);
			yVel = 0;
		}
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
