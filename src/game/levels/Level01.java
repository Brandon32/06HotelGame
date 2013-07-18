package game.levels;

import java.awt.Graphics2D;

import engine.event.GameEvent;
import engine.event.GameEvent.GameEventType;
import engine.event.GameEventDispatcher;
import engine.sprite.Level;
import game.sprite.Mech;

public class Level01 implements Level {
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
}
