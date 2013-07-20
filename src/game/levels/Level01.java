package game.levels;

import java.awt.Graphics2D;

import engine.events.GameEvent;
import engine.events.GameEventDispatcher;
import engine.events.GameEvent.GameEventType;
import engine.interfaces.LevelInterface;
import game.sprite.Mech;

public class Level01 implements LevelInterface {
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
