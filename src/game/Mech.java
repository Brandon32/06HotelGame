package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Sprite.Sprite;
import engine.GameDisplay;

public class Mech implements Sprite {

	private Font f1;
	private Dimension displayBounds;
	
	private final double turn = 1;
	
	boolean rtArm;
	boolean ltArm;
	boolean rtPowerArm;
	boolean ltPowerArm;
	boolean rtGrab;
	boolean ltGrab;
	private boolean ltLegDown;
	private boolean ltLegUp;
	private boolean rtLegDown;
	private boolean rtLegUp;
	
	private double maxLegSpeed = 10;
	private double minLegSpeed = -10;
	private double maxTurningSpeed = 3;
	double angle = 0;
	private int x = 100; // center
	private int y = 100; // center
	private int width = 20;
	private int height = 20;
	private double xVel;
	private double yVel;
	private double rightLegSpeed;
	private double leftLegSpeed;
	

	public Mech() {
		displayBounds = GameDisplay.getBounds();
		f1 = new Font("Times New Roman", Font.BOLD, (int) 11);

		x = displayBounds.width / 2;
		y = displayBounds.height / 2;
	}

	public void update() {
		// TODO: calculate angle

		// TODO: calculate speed
		calcMovement();
		recalcAngle();
	}

	public void draw(Graphics2D g) {

		g.setColor(Color.BLUE);
		g.drawRect(x - width / 2, y - height / 2, width, height);
		g.setColor(Color.BLACK);
		// g.drawLine(x, y, x - rtVel, y - ltVel); // direction
		g.drawLine(x, y, x - (int) (xVel * 10), y - (int) (yVel * 10));

		{
			g.setFont(f1);
			g.setColor(Color.RED);
			g.drawRect(x + 99, y + 90, 200, 200);
			g.drawString("Right Speed: " + rightLegSpeed, x + 100, y + 100);
			g.drawString("Left Speed:  " + leftLegSpeed, x + 100, y + 110);
			g.drawString("Angle:       " + (int)Math.toDegrees(angle) + " Degrees", x + 100, y + 120);
			g.drawString("X Velocity:  " + xVel, x + 100, y + 130);
			g.drawString("Y Velocity:  " + yVel, x + 100, y + 140);
		}
	}

	public void keyboardEvent(KeyEvent ke) {
		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			if (ke.getKeyCode() == KeyEvent.VK_W) // right UP
				ltLegUp = true;
		}
		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			if (ke.getKeyCode() == KeyEvent.VK_S) // right down
				ltLegDown = true;
		}
		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			if (ke.getKeyCode() == KeyEvent.VK_UP
					|| ke.getKeyCode() == KeyEvent.VK_8) // left UP
				rtLegUp = true;
		}
		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			if (ke.getKeyCode() == KeyEvent.VK_DOWN
					|| ke.getKeyCode() == KeyEvent.VK_2) // left down
				rtLegDown = true;
		}
	}

	public void mouseEvent(MouseEvent me) {

	}

	private void calcMovement() {
		if (rtLegUp) {
			if (rightLegSpeed < maxLegSpeed)
				rightLegSpeed += turn;
			rtLegUp = false;
		}
		if (rtLegDown) {
			if (rightLegSpeed > minLegSpeed)
				rightLegSpeed -= turn;
			rtLegDown = false;
		}
		if (ltLegUp) {
			if (leftLegSpeed < maxLegSpeed)
				leftLegSpeed += turn;
			ltLegUp = false;
		}
		if (ltLegDown) {
			if (leftLegSpeed > minLegSpeed)
				leftLegSpeed -= turn;
			ltLegDown = false;
		}
	}

	private void recalcAngle() {
		if (rightLegSpeed>leftLegSpeed){
			double a=rightLegSpeed/leftLegSpeed;
			angle-=(maxTurningSpeed * a);
		}
		if (rightLegSpeed<leftLegSpeed){
			double a=leftLegSpeed/rightLegSpeed;
			angle+=(maxTurningSpeed * a);
		}
		if (rightLegSpeed==leftLegSpeed){
			angle+=(maxTurningSpeed * 0);
		}
		
//		angle += (leftLegSpeed + rightLegSpeed);
		if (angle > Math.PI*2)
			angle = 0;
		if (angle < -Math.PI*2)
			angle = 0;
//		xVel = Math.sin(angle);
//		yVel = Math.cos(angle);
	}

	@Override
	public void checkCollision(Sprite obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		// TODO Auto-generated method stub
		return null;
	}

}
