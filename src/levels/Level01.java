package levels;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Sprite.UI;
import engine.Event.GameEvent;
import engine.Event.GameEvent.GameEventType;
import engine.Event.GameEventDispatcher;
import game.Mech;

public class Level01 implements UI {
	private Mech myMech;

	public Level01() {
		myMech = new Mech();
		GameEventDispatcher.dispatchEvent(new GameEvent(this,GameEventType.AddFirst, myMech));
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEvent(MouseEvent me) {
		// TODO Auto-generated method stub

	}

}
