package engine.sprites.inheritance;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import engine.sprites.interfaces.ColisionInterface;
import engine.sprites.interfaces.ImageInterface;
import engine.sprites.interfaces.UIInterface;
import engine.sprites.tools.Position;

public class CharacterObject implements ColisionInterface, UIInterface {

	private Position position = new Position();
	
	public CharacterObject(){
		position.set(0,0);
	}
	public CharacterObject(int x, int y){
		position.set(x,y);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(ImageInterface image) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEvent(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkCollision(ColisionInterface obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle intersects(Rectangle boundingBox) {
		// TODO Auto-generated method stub
		return null;
	}

}
